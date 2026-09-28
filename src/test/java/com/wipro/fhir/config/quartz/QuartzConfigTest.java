/*
* AMRIT - Accessible Medical Records via Integrated Technologies
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

import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Quartz wiring: one job detail and one cron trigger per scheduled pass, all hung off
 * a single scheduler that autowires its jobs out of the Spring context.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("QuartzConfig Test Suite")
class QuartzConfigTest {

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private ApplicationContext applicationContext;

    private QuartzConfig config;

    @BeforeEach
    @DisplayName("Wire the configuration with a mocked transaction manager and context")
    void setUp() {
        config = new QuartzConfig();
        ReflectionTestUtils.setField(config, "transactionManager", transactionManager);
        ReflectionTestUtils.setField(config, "applicationContext", applicationContext);
    }

    private JobDetail jobDetailOf(JobDetailFactoryBean factory) {
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    private Trigger triggerOf(CronTriggerFactoryBean factory) throws Exception {
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Test
    @DisplayName("init should log the configuration coming up without doing any work")
    void init_shouldNotThrow() {
        assertDoesNotThrow(() -> config.init());
    }

    @Test
    @DisplayName("quartzProperties should load the Quartz settings out of application.properties")
    void quartzProperties_shouldLoadApplicationProperties() {
        Properties properties = config.quartzProperties();

        assertNotNull(properties);
        assertTrue(properties.size() > 0, "application.properties is on the test classpath");
    }

    @Nested
    @DisplayName("Job details")
    class JobDetailTests {

        @Test
        @DisplayName("each scheduled pass should get its own job detail in the spring-quartz group")
        void jobDetails_shouldNameTheirJobClassAndGroup() {
            assertEquals(Scheduler_Job_FHIR_R4_ResourceCreation_NDHM.class,
                    jobDetailOf(config.processMQJobForFHIRResourceGeneration()).getJobClass());
            assertEquals(Scheduler_Job_Patient_Profile_NDHM.class,
                    jobDetailOf(config.processMQJobForPatientProfileCreation()).getJobClass());
            assertEquals(Schedule_Job_Patient_Feed_ATOM.class,
                    jobDetailOf(config.processMQJobForParsingAtomFeed()).getJobClass());
            assertEquals(Schedule_Job_Clinical_Feed_Atom.class,
                    jobDetailOf(config.processMQJobForParsingAtomFeedClinical()).getJobClass());
            assertEquals(ScheduleForEAushadhiStockAddition.class,
                    jobDetailOf(config.processMQJobForEAushadhiStockEntry()).getJobClass());
            assertEquals("spring-quartz",
                    jobDetailOf(config.processMQJobForFHIRResourceGeneration()).getKey().getGroup());
        }
    }

    @Nested
    @DisplayName("Cron triggers")
    class CronTriggerTests {

        @Test
        @DisplayName("each pass should get a cron trigger bound to its own job")
        void cronTriggers_shouldBindToTheirJob() throws Exception {
            assertNotNull(triggerOf(config.processMQTriggerForFHIRResourceGeneration()));
            assertNotNull(triggerOf(config.processMQTriggerForPatientProfileCreation()));
            assertNotNull(triggerOf(config.processMQTriggerForParsingAtomFeed()));
            assertNotNull(triggerOf(config.processMQTriggerForParsingAtomFeedClinical()));
            assertNotNull(triggerOf(config.processMQTriggerForEAushadhiStockEntry()));
        }

        @Test
        @DisplayName("each cron trigger should sit in the spring-quartz group alongside its job")
        void cronTriggers_shouldSitInSpringQuartzGroup() throws Exception {
            Trigger trigger = triggerOf(config.processMQTriggerForFHIRResourceGeneration());

            assertEquals("spring-quartz", trigger.getKey().getGroup());
        }
    }

    @Test
    @DisplayName("quartzScheduler should hang every trigger off one transactional scheduler")
    void quartzScheduler_shouldHangEveryTriggerOffOneScheduler() {
        SchedulerFactoryBean scheduler = config.quartzScheduler();

        assertNotNull(scheduler);
        assertEquals(5, ((java.util.List<?>) ReflectionTestUtils.getField(scheduler, "triggers")).size());
        assertTrue(ReflectionTestUtils.getField(scheduler, "jobFactory")
                instanceof AutowiringSpringBeanJobFactory,
                "the jobs are autowired out of the Spring context");
        assertEquals("jelies-quartz-scheduler", ReflectionTestUtils.getField(scheduler, "schedulerName"));
    }
}
