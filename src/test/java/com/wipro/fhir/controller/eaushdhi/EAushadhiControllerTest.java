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
package com.wipro.fhir.controller.eaushdhi;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.wipro.fhir.service.e_aushdhi.EAushadhiService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("EAushadhiController Test Suite")
class EAushadhiControllerTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String REQUEST = "{\"facilityID\":42}";

    @Mock
    private EAushadhiService eAushadhiService;

    @InjectMocks
    private EAushadhiController controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("getStoreStockDetails")
    class GetStoreStockDetailsTests {

        @Test
        @DisplayName("a \"success\" verdict from the service becomes the stock-added confirmation")
        void getStoreStockDetails_shouldConfirmWhenServiceReportsSuccess() throws Exception {
            when(eAushadhiService.getEaushadhiStoreDetailsByFacilityID(REQUEST)).thenReturn("success");

            mockMvc.perform(post("/eAushadhi/getStoreStockDetails")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.response").value("E-aushadhi Stock Added Successfully"));
        }

        @Test
        @DisplayName("any other verdict is reported as a 5000 error carrying the verdict")
        void getStoreStockDetails_shouldReportNonSuccessVerdict() throws Exception {
            when(eAushadhiService.getEaushadhiStoreDetailsByFacilityID(REQUEST)).thenReturn("facility not mapped");

            mockMvc.perform(post("/eAushadhi/getStoreStockDetails")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                    .andExpect(jsonPath("$.errorMessage")
                            .value("E-aushadhi Error while getting store stock details : facility not mapped"));
        }

        @Test
        @DisplayName("a FHIRException from the service becomes a 5000 error")
        void getStoreStockDetails_shouldReportServiceFailure() throws Exception {
            when(eAushadhiService.getEaushadhiStoreDetailsByFacilityID(anyString()))
                    .thenThrow(new FHIRException("e-aushadhi unreachable"));

            mockMvc.perform(post("/eAushadhi/getStoreStockDetails")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorMessage").value("e-aushadhi unreachable"));
        }
    }

    @Nested
    @DisplayName("syncDrugDispenseAndPatientDetails")
    class SyncDispenseTests {

        @Test
        @DisplayName("the request and the Authorization header are both handed to the service")
        void syncDispense_shouldPassAuthorizationToService() throws Exception {
            when(eAushadhiService.syncDispenseDetailsToEAushadhi(REQUEST, AUTHORIZATION))
                    .thenReturn("{\"synced\":7}");

            mockMvc.perform(post("/eAushadhi/syncDrugDispenseDetails")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.synced").value(7));

            verify(eAushadhiService).syncDispenseDetailsToEAushadhi(REQUEST, AUTHORIZATION);
        }

        @Test
        @DisplayName("a FHIRException from the service becomes a 5000 error")
        void syncDispense_shouldReportServiceFailure() throws Exception {
            when(eAushadhiService.syncDispenseDetailsToEAushadhi(anyString(), anyString()))
                    .thenThrow(new FHIRException("sync rejected"));

            mockMvc.perform(post("/eAushadhi/syncDrugDispenseDetails")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorMessage").value("sync rejected"));
        }

        @Test
        @DisplayName("an empty request object is refused without touching the service")
        void syncDispense_shouldRejectNullRequest() throws Exception {
            String response = controller.syncDrugDispenseAndPatientDetails(null, AUTHORIZATION);

            assertEquals(OutputResponse.GENERIC_FAILURE, new JSONObject(response).getInt("statusCode"));
            assertTrue(response.contains("Error empty request object"));
            verifyNoInteractions(eAushadhiService);
        }
    }

    @Nested
    @DisplayName("getFacilityStockProcessLog")
    class ProcessLogTests {

        @Test
        @DisplayName("the service payload is wrapped in a success response")
        void getFacilityStockProcessLog_shouldReturnServicePayload() throws Exception {
            when(eAushadhiService.getFacilityStockProcessLog(REQUEST)).thenReturn("{\"rows\":3}");

            mockMvc.perform(post("/eAushadhi/getFacilityStockProcessLog")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.rows").value(3));
        }

        @Test
        @DisplayName("a FHIRException from the service becomes a 5000 error")
        void getFacilityStockProcessLog_shouldReportServiceFailure() throws Exception {
            when(eAushadhiService.getFacilityStockProcessLog(anyString()))
                    .thenThrow(new FHIRException("log unavailable"));

            mockMvc.perform(post("/eAushadhi/getFacilityStockProcessLog")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(REQUEST))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorMessage").value("log unavailable"));
        }

        @Test
        @DisplayName("an empty request object is refused without touching the service")
        void getFacilityStockProcessLog_shouldRejectNullRequest() throws Exception {
            String response = controller.getFacilityStockProcessLog(null);

            assertTrue(response.contains("Error empty request object"));
            verifyNoInteractions(eAushadhiService);
        }
    }

    @Nested
    @DisplayName("updatePatientIssueSyncStatus")
    class UpdateSyncStatusTests {

        @Test
        @DisplayName("the JSON array of issue ids is parsed and forwarded to the service")
        void addFacility_shouldForwardParsedIssueIds() throws Exception {
            when(eAushadhiService.updateSyncStatusForEAushadhiDispense(List.of("issue-1", "issue-2")))
                    .thenReturn("{\"updated\":2}");

            mockMvc.perform(post("/eAushadhi/updatePatientIssueSyncStatus")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[\"issue-1\",\"issue-2\"]"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.updated").value(2));

            verify(eAushadhiService).updateSyncStatusForEAushadhiDispense(List.of("issue-1", "issue-2"));
        }

        @Test
        @DisplayName("an empty array is refused without touching the service")
        void addFacility_shouldRejectEmptyArray() throws Exception {
            mockMvc.perform(post("/eAushadhi/updatePatientIssueSyncStatus")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[]"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                    .andExpect(jsonPath("$.errorMessage").value("Error empty request object"));

            verifyNoInteractions(eAushadhiService);
        }

        @Test
        @DisplayName("a malformed array is caught and mapped through setError(Throwable)")
        void addFacility_shouldReportMalformedArray() throws Exception {
            mockMvc.perform(post("/eAushadhi/updatePatientIssueSyncStatus")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("not-a-json-array"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE));

            verifyNoInteractions(eAushadhiService);
        }

        @Test
        @DisplayName("a FHIRException from the service is mapped through setError(Throwable)")
        void addFacility_shouldReportServiceFailure() throws Exception {
            when(eAushadhiService.updateSyncStatusForEAushadhiDispense(anyList()))
                    .thenThrow(new FHIRException("update rejected"));

            mockMvc.perform(post("/eAushadhi/updatePatientIssueSyncStatus")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("[\"issue-1\"]"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                    .andExpect(jsonPath("$.errorMessage").value("update rejected"));
        }
    }
}
