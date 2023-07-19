/*
* AMRIT – Accessible Medical Records via Integrated Technology 
* Integrated EHR (Electronic Health Records) Solution 
*
* Copyright (C) "Piramal Swasthya Management and Research Institute" 
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.config.quartz;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.wipro.fhir.utils.config.ConfigProperties;

/***
 * 
 * @author NE298657
 *
 */

// quartz configuration

@Configuration
public class QuartzConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		log.debug("QuartzConfig initialized.");
	}

	@Bean
	public Properties quartzProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/application.properties"));
		Properties properties = null;
		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();

		} catch (IOException e) {
			log.warn("Cannot load application.properties.");
		}

		return properties;
	}

	@Bean
	public SchedulerFactoryBean quartzScheduler() {
		SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

		
		quartzScheduler.setTransactionManager(transactionManager);
		quartzScheduler.setOverwriteExistingJobs(true);
		quartzScheduler.setSchedulerName("jelies-quartz-scheduler");

		// custom job factory of spring with DI support for @Autowired!
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		quartzScheduler.setJobFactory(jobFactory);

		quartzScheduler.setQuartzProperties(quartzProperties());

		Trigger[] triggers = { processMQTriggerForFHIRResourceGeneration().getObject(),

				processMQTriggerForPatientProfileCreation().getObject(),
				processMQTriggerForParsingAtomFeed().getObject(),
				processMQTriggerForParsingAtomFeedClinical().getObject(), processMQTriggerForEAushadhiStockEntry().getObject() };

		quartzScheduler.setTriggers(triggers);

		return quartzScheduler;
	}

	/*
	 * Job For FHIRResource Generation
	 */

	// job bean
	@Bean
	public JobDetailFactoryBean processMQJobForFHIRResourceGeneration() {
		JobDetailFactoryBean jobDetailFactory;
		jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(Scheduler_Job_FHIR_R4_ResourceCreation_NDHM.class);
		jobDetailFactory.setGroup("spring-quartz");
		return jobDetailFactory;
	}

	// trigger bean
	@Bean
	public CronTriggerFactoryBean processMQTriggerForFHIRResourceGeneration() {
		log.debug("start");

		Boolean startJob = ConfigProperties.getBoolean("start-FHIR-resource-bundle-scheduler");
		CronTriggerFactoryBean cronTriggerFactoryBean = null;
		String scheduleConfig = "5 0 0 * * ?";
		if (startJob) {
			scheduleConfig = ConfigProperties.getPropertyByName("cron-scheduler-FHIR-resource-bundle");
		}
		cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(processMQJobForFHIRResourceGeneration().getObject());

		cronTriggerFactoryBean.setCronExpression(scheduleConfig);
		cronTriggerFactoryBean.setGroup("spring-quartz");

		log.debug("End");

		return cronTriggerFactoryBean;
	}

	/*
	 * Job For Patient Profile Creation
	 */

	// job bean
	@Bean
	public JobDetailFactoryBean processMQJobForPatientProfileCreation() {
		JobDetailFactoryBean jobDetailFactory;
		jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(Scheduler_Job_Patient_Profile_NDHM.class);
		jobDetailFactory.setGroup("spring-quartz");
		return jobDetailFactory;
	}

	// trigger bean
	@Bean
	public CronTriggerFactoryBean processMQTriggerForPatientProfileCreation() {
		log.debug("start");

		Boolean startJob = ConfigProperties.getBoolean("start-FHIR-patient-profile-scheduler");
		CronTriggerFactoryBean cronTriggerFactoryBean = null;
		String scheduleConfig = "5 0 0 * * ?";
		if (startJob) {
			scheduleConfig = ConfigProperties.getPropertyByName("cron-scheduler-FHIR-patient-profile");
		}
		cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(processMQJobForPatientProfileCreation().getObject());

		cronTriggerFactoryBean.setCronExpression(scheduleConfig);
		cronTriggerFactoryBean.setGroup("spring-quartz");

		log.debug("End");

		return cronTriggerFactoryBean;
	}


	/*
	 * Job For Parsing Atom Feed - patient
	 */

	@Bean
	public JobDetailFactoryBean processMQJobForParsingAtomFeed() {
		JobDetailFactoryBean jobDetailFactory;
		jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(Schedule_Job_Patient_Feed_ATOM.class);
		jobDetailFactory.setGroup("spring-quartz");
		return jobDetailFactory;
	}

	// trigger bean
	@Bean
	public CronTriggerFactoryBean processMQTriggerForParsingAtomFeed() {
		log.debug("start");

		Boolean startJob = ConfigProperties.getBoolean("start-parsing-atom-feed-scheduler");
		CronTriggerFactoryBean cronTriggerFactoryBean = null;
		String scheduleConfig = "5 0 0 * * ?";
		if (startJob) {
			scheduleConfig = ConfigProperties.getPropertyByName("cron-scheduler-atom-patient-feed");
		}
		cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(processMQJobForParsingAtomFeed().getObject());

		cronTriggerFactoryBean.setCronExpression(scheduleConfig);
		cronTriggerFactoryBean.setGroup("spring-quartz");

		log.debug("End");

		return cronTriggerFactoryBean;
	}

	
	@Bean
	public CronTriggerFactoryBean processMQTriggerForEAushadhiStockEntry()
	{
		Boolean startJob = ConfigProperties.getBoolean("start-eAushadhi-stock-entry-scheduler");
		CronTriggerFactoryBean cronTriggerFactoryBean = null;
		String scheduleConfig = "5 0 0 * * ?";
		if (startJob)
		{
			scheduleConfig = ConfigProperties.getPropertyByName("cron-scheduler-eAushadhi-stock-entry");
		}
		cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(processMQJobForEAushadhiStockEntry().getObject());

		cronTriggerFactoryBean.setCronExpression(scheduleConfig);
		cronTriggerFactoryBean.setGroup("spring-quartz");
		return cronTriggerFactoryBean;
	}
	
	@Bean
	public JobDetailFactoryBean processMQJobForEAushadhiStockEntry()
	{
		JobDetailFactoryBean jobDetailFactory;
		jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ScheduleForEAushadhiStockAddition.class);
		jobDetailFactory.setGroup("spring-quartz");
		return jobDetailFactory;
	}


	/*
	 * Job For Parsing Atom Feed - patient
	 */

	@Bean
	public JobDetailFactoryBean processMQJobForParsingAtomFeedClinical() {
		JobDetailFactoryBean jobDetailFactory;
		jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(Schedule_Job_Clinical_Feed_Atom.class);
		jobDetailFactory.setGroup("spring-quartz");
		return jobDetailFactory;
	}

	// trigger bean
	@Bean
	public CronTriggerFactoryBean processMQTriggerForParsingAtomFeedClinical() {
		log.debug("start");

		Boolean startJob = ConfigProperties.getBoolean("start-parsing-atom-feed-clinical-scheduler");
		CronTriggerFactoryBean cronTriggerFactoryBean = null;
		String scheduleConfig = "5 0 0 * * ?";
		if (startJob) {
			scheduleConfig = ConfigProperties.getPropertyByName("cron-scheduler-atom-clinical-feed");
		}
		cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(processMQJobForParsingAtomFeedClinical().getObject());

		cronTriggerFactoryBean.setCronExpression(scheduleConfig);
		cronTriggerFactoryBean.setGroup("spring-quartz");

		log.debug("End");

		return cronTriggerFactoryBean;
	}

}
