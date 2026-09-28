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
package com.wipro.fhir.service.facility;

import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.LocalHttpStub;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.v3.abha.GenerateAuthSessionService;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FacilityServiceImpl Test Suite")
class FacilityServiceImplTest {

    private static final String SERVICES_PATH = "/abdm/services";

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private GenerateAuthSessionService generateAuthSessionService;

    @Mock
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @InjectMocks
    private FacilityServiceImpl service;

    private LocalHttpStub stub;

    @BeforeEach
    @DisplayName("Point the ABDM facility URL at a loopback stub before each test")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        ReflectionTestUtils.setField(service, "getAbdmServicies", stub.url(SERVICES_PATH));
        ReflectionTestUtils.setField(service, "xCMId", "sbx");
        ReflectionTestUtils.setField(service, "abdmFacilityId", "IN-DEFAULT-1");
    }

    @AfterEach
    void tearDown() {
        stub.close();
    }

    @Nested
    @DisplayName("fetchRegisteredFacilities")
    class FetchRegisteredFacilitiesTests {

        private void stubAbdm(String body) throws Exception {
            stub.stubOk(SERVICES_PATH, body);
            when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
            lenient().when(common_NDHMService.getBody(any(ResponseEntity.class)))
                    .thenAnswer(invocation -> ((ResponseEntity<String>) invocation.getArgument(0)).getBody());
        }

        @Test
        @DisplayName("should keep only the HIP services and answer with their id and name")
        void fetchRegisteredFacilities_shouldKeepOnlyHipServices() throws Exception {
            stubAbdm("{\"services\":["
                    + "{\"id\":\"IN0710000001\",\"name\":\"PHC Kanke\",\"types\":[\"HIP\"]},"
                    + "{\"id\":\"IN0710000002\",\"name\":\"HIU only\",\"types\":[\"HIU\"]},"
                    + "{\"id\":\"IN0710000003\",\"name\":\"Both\",\"types\":[\"HIP\",\"HIU\"]}"
                    + "]}");

            String result = service.fetchRegisteredFacilities();

            assertTrue(result.contains("IN0710000001"), result);
            assertTrue(result.contains("IN0710000003"), result);
            assertTrue(!result.contains("IN0710000002"), "an HIU-only service is not a registered HIP facility");
        }

        @Test
        @DisplayName("should answer with an empty list when no service is an HIP")
        void fetchRegisteredFacilities_shouldAnswerEmptyWithoutHipServices() throws Exception {
            stubAbdm("{\"services\":[{\"id\":\"IN0710000002\",\"name\":\"HIU only\",\"types\":[\"HIU\"]}]}");

            assertEquals("[]", service.fetchRegisteredFacilities());
        }

        @Test
        @DisplayName("should send the ABDM auth token and the correlation headers ABDM requires")
        void fetchRegisteredFacilities_shouldSendAbdmHeaders() throws Exception {
            stubAbdm("{\"services\":[]}");

            service.fetchRegisteredFacilities();

            LocalHttpStub.Request request = stub.request(SERVICES_PATH);
            assertEquals("GET", request.method());
            assertEquals("Bearer abdm-token", request.header("Authorization"));
            assertEquals("sbx", request.header("X-Cm-Id"));
            assertTrue(request.header("Request-Id") != null, "ABDM requires a per-request correlation id");
            assertTrue(request.header("Timestamp").endsWith("Z"), "the timestamp must be sent as UTC ISO-8601");
        }

        @Test
        @DisplayName("should wrap an ABDM transport failure in a FHIRException")
        void fetchRegisteredFacilities_shouldWrapTransportFailure() throws Exception {
            when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
            ReflectionTestUtils.setField(service, "getAbdmServicies", LocalHttpStub.unreachableUrl());

            FHIRException failure = assertThrows(FHIRException.class, () -> service.fetchRegisteredFacilities());

            assertTrue(failure.getMessage().startsWith("NDHM_FHIR Error while accessing ABDM API"));
        }

        @Test
        @DisplayName("should wrap a failure to mint the ABDM session token in a FHIRException")
        void fetchRegisteredFacilities_shouldWrapTokenFailure() throws Exception {
            when(generateAuthSessionService.getAbhaAuthToken()).thenThrow(new FHIRException("no ABDM session"));

            FHIRException failure = assertThrows(FHIRException.class, () -> service.fetchRegisteredFacilities());

            assertTrue(failure.getMessage().contains("no ABDM session"));
        }
    }

    @Nested
    @DisplayName("saveAbdmFacilityId")
    class SaveAbdmFacilityIdTests {

        @Test
        @DisplayName("should store the facility id the caller supplied against the visit")
        void saveAbdmFacilityId_shouldStoreSuppliedFacilityId() throws Exception {
            when(benHealthIDMappingRepo.updateFacilityIdForVisit(BigInteger.valueOf(987654L), "IN0710000001"))
                    .thenReturn(1);

            String result = service.saveAbdmFacilityId(
                    "{\"visitCode\":987654,\"abdmFacilityId\":\"IN0710000001\"}");

            assertEquals("ABDM Facility ID updated successfully", result);
            verify(benHealthIDMappingRepo).updateFacilityIdForVisit(BigInteger.valueOf(987654L), "IN0710000001");
        }

        @Test
        @DisplayName("should fall back to the configured facility id when the caller omits one")
        void saveAbdmFacilityId_shouldFallBackToConfiguredFacilityId() throws Exception {
            when(benHealthIDMappingRepo.updateFacilityIdForVisit(BigInteger.valueOf(987654L), "IN-DEFAULT-1"))
                    .thenReturn(1);

            assertEquals("ABDM Facility ID updated successfully",
                    service.saveAbdmFacilityId("{\"visitCode\":987654}"));
        }

        @Test
        @DisplayName("should fall back to the configured facility id when the caller sends a blank one")
        void saveAbdmFacilityId_shouldTreatBlankFacilityIdAsAbsent() throws Exception {
            when(benHealthIDMappingRepo.updateFacilityIdForVisit(BigInteger.valueOf(987654L), "IN-DEFAULT-1"))
                    .thenReturn(1);

            assertEquals("ABDM Facility ID updated successfully",
                    service.saveAbdmFacilityId("{\"visitCode\":987654,\"abdmFacilityId\":\"   \"}"));
        }

        @Test
        @DisplayName("should ask the caller to map a facility when none is configured either")
        void saveAbdmFacilityId_shouldAskForFacilityMappingWhenUnconfigured() throws Exception {
            ReflectionTestUtils.setField(service, "abdmFacilityId", "  ");

            assertEquals("ABDM Facility ID is not configured. Please map the facility before proceeding.",
                    service.saveAbdmFacilityId("{\"visitCode\":987654}"));

            verifyNoInteractions(benHealthIDMappingRepo);
        }

        @Test
        @DisplayName("should report the failure when the update touches no visit row")
        void saveAbdmFacilityId_shouldReportUpdateMiss() throws Exception {
            when(benHealthIDMappingRepo.updateFacilityIdForVisit(BigInteger.valueOf(987654L), "IN0710000001"))
                    .thenReturn(0);

            assertEquals("FHIR Error while updating ABDM Facility ID", service.saveAbdmFacilityId(
                    "{\"visitCode\":987654,\"abdmFacilityId\":\"IN0710000001\"}"));
        }

        @Test
        @DisplayName("should wrap a repository failure in a FHIRException")
        void saveAbdmFacilityId_shouldWrapRepositoryFailure() throws Exception {
            when(benHealthIDMappingRepo.updateFacilityIdForVisit(any(), any()))
                    .thenThrow(new IllegalStateException("deadlock"));

            assertEquals("deadlock", assertThrows(FHIRException.class, () -> service.saveAbdmFacilityId(
                    "{\"visitCode\":987654,\"abdmFacilityId\":\"IN0710000001\"}")).getMessage());
        }
    }
}
