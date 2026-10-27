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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;

import com.wipro.fhir.service.atoms.feed.bahmni.ClinicalFeedWorker;
import com.wipro.fhir.service.atoms.feed.bahmni.PatientFeedWorker;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.e_aushdhi.EAushadhiServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The scheduled jobs are thin wrappers: each one calls a single service method and, apart
 * from the e-aushadhi stock job, swallows whatever that call throws so a failing run never
 * takes the Quartz scheduler down with it.
 */
@DisplayName("Quartz scheduled jobs Test Suite")
class QuartzJobsTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Scheduler_Job_FHIR_R4_ResourceCreation_NDHM")
    class ResourceCreationJobTests {

        @Mock
        private CommonService commonService;

        @Mock
        private JobExecutionContext context;

        @InjectMocks
        private Scheduler_Job_FHIR_R4_ResourceCreation_NDHM job;

        @Test
        @DisplayName("execute should run the resource-creation pass")
        void execute_shouldRunResourceCreation() throws Exception {
            job.execute(context);

            verify(commonService).processResourceOperation();
        }

        @Test
        @DisplayName("execute should swallow a failing pass so the scheduler keeps running")
        void execute_shouldSwallowFailure() throws Exception {
            doThrow(new FHIRException("bundle creation failed")).when(commonService).processResourceOperation();

            assertDoesNotThrow(() -> job.execute(context));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Scheduler_Job_Patient_Profile_NDHM")
    class PatientProfileJobTests {

        @Mock
        private CommonService commonService;

        @Mock
        private JobExecutionContext context;

        @InjectMocks
        private Scheduler_Job_Patient_Profile_NDHM job;

        @Test
        @DisplayName("execute should run the patient-profile pass")
        void execute_shouldRunPatientProfileCreation() throws Exception {
            job.execute(context);

            verify(commonService).processPatientProfileCreationAMRIT();
        }

        @Test
        @DisplayName("execute should swallow a failing pass so the scheduler keeps running")
        void execute_shouldSwallowFailure() throws Exception {
            doThrow(new FHIRException("profile creation failed"))
                    .when(commonService).processPatientProfileCreationAMRIT();

            assertDoesNotThrow(() -> job.execute(context));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Schedule_Job_Patient_Feed_ATOM")
    class PatientFeedJobTests {

        @Mock
        private PatientFeedWorker patientFeedWorkerService;

        @Mock
        private JobExecutionContext context;

        @InjectMocks
        private Schedule_Job_Patient_Feed_ATOM job;

        @Test
        @DisplayName("execute should run the patient ATOM feed pass")
        void execute_shouldRunPatientFeed() throws Exception {
            job.execute(context);

            verify(patientFeedWorkerService).patientFeedManager();
        }

        @Test
        @DisplayName("execute should swallow a failing pass so the scheduler keeps running")
        void execute_shouldSwallowFailure() throws Exception {
            doThrow(new FHIRException("feed unreachable")).when(patientFeedWorkerService).patientFeedManager();

            assertDoesNotThrow(() -> job.execute(context));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("Schedule_Job_Clinical_Feed_Atom")
    class ClinicalFeedJobTests {

        @Mock
        private ClinicalFeedWorker clinicalFeedWorker;

        @Mock
        private JobExecutionContext context;

        @InjectMocks
        private Schedule_Job_Clinical_Feed_Atom job;

        @Test
        @DisplayName("execute should run the clinical ATOM feed pass")
        void execute_shouldRunClinicalFeed() throws Exception {
            job.execute(context);

            verify(clinicalFeedWorker).encounterFeedManager();
        }

        @Test
        @DisplayName("execute should swallow a failing pass so the scheduler keeps running")
        void execute_shouldSwallowFailure() throws Exception {
            doThrow(new FHIRException("feed unreachable")).when(clinicalFeedWorker).encounterFeedManager();

            assertDoesNotThrow(() -> job.execute(context));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("ScheduleForEAushadhiStockAddition")
    class EAushadhiStockJobTests {

        @Mock
        private EAushadhiServiceImpl eAushadhiServiceImpl;

        @Mock
        private JobExecutionContext context;

        @InjectMocks
        private ScheduleForEAushadhiStockAddition job;

        @Test
        @DisplayName("execute should run the e-aushadhi stock pass")
        void execute_shouldRunStockAddition() throws Exception {
            job.execute(context);

            verify(eAushadhiServiceImpl).getStockDetailsFromEAushadhi();
        }

        @Test
        @DisplayName("execute should let a failing stock pass surface, since this job does not guard itself")
        void execute_shouldPropagateFailure() {
            doThrow(new IllegalStateException("e-aushadhi unreachable"))
                    .when(eAushadhiServiceImpl).getStockDetailsFromEAushadhi();

            assertThrows(IllegalStateException.class, () -> job.execute(context));
        }
    }
}
