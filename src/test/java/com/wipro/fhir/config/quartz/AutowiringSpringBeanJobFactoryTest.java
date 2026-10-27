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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDetail;
import org.quartz.JobDataMap;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutowiringSpringBeanJobFactory Test Suite")
class AutowiringSpringBeanJobFactoryTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private AutowireCapableBeanFactory beanFactory;

    @Mock
    private TriggerFiredBundle triggerFiredBundle;

    @Mock
    private OperableTrigger trigger;

    private AutowiringSpringBeanJobFactory jobFactory;

    @BeforeEach
    @DisplayName("Create the job factory before each test")
    void setUp() {
        jobFactory = new AutowiringSpringBeanJobFactory();
    }

    private void stubBundle() {
        lenient().when(triggerFiredBundle.getJobDetail()).thenReturn(fhirJobDetail());
        lenient().when(triggerFiredBundle.getTrigger()).thenReturn(trigger);
        lenient().when(trigger.getJobDataMap()).thenReturn(new JobDataMap());
    }

    private JobDetail fhirJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(Scheduler_Job_FHIR_R4_ResourceCreation_NDHM.class);
        factory.setName("fhir-job");
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Test
    @DisplayName("createJobInstance should autowire the new job through the application context")
    void createJobInstance_shouldAutowireJobThroughApplicationContext() throws Exception {
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
        stubBundle();
        jobFactory.setApplicationContext(applicationContext);

        Object job = jobFactory.createJobInstance(triggerFiredBundle);

        assertNotNull(job);
        assertTrue(job instanceof Scheduler_Job_FHIR_R4_ResourceCreation_NDHM);
        verify(beanFactory).autowireBean(job);
    }

    @Test
    @DisplayName("createJobInstance should fail when no application context has been supplied")
    void createJobInstance_shouldFailWithoutApplicationContext() {
        stubBundle();

        assertThrows(NullPointerException.class, () -> jobFactory.createJobInstance(triggerFiredBundle));
    }
}
