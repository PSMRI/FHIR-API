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
package com.wipro.fhir.controller.healthID;

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

import com.wipro.fhir.service.healthID.HealthIDService;
import com.wipro.fhir.service.healthID.HealthID_WithMobileOTPService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateHealthIDWithMobileOTP Test Suite")
class CreateHealthIDWithMobileOTPTest {

    private static final String AUTHORIZATION = "session-key-123";

    @Mock
    private HealthID_WithMobileOTPService healthID;

    @Mock
    private HealthIDService healthIDService;

    @InjectMocks
    private CreateHealthIDWithMobileOTP controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Nested
    @DisplayName("generateOTP")
    class GenerateOtpTests {

        @Test
        @DisplayName("should wrap the transaction id from the service in a success response")
        void generateOTP_shouldReturnServicePayload() throws Exception {
            when(healthID.generateOTP("{\"mobile\":\"9999999999\"}")).thenReturn("{\"txnId\":\"txn-1\"}");

            mockMvc.perform(post("/healthID/generateOTP")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"mobile\":\"9999999999\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.txnId").value("txn-1"));
        }

        @Test
        @DisplayName("should surface a FHIRException as a 5000 error")
        void generateOTP_shouldReportServiceFailure() throws Exception {
            when(healthID.generateOTP(anyString())).thenThrow(new FHIRException("ABDM refused"));

            mockMvc.perform(post("/healthID/generateOTP")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorMessage").value("ABDM refused"));
        }

        @Test
        @DisplayName("should refuse an empty request object without touching the service")
        void generateOTP_shouldRejectNullRequest() throws Exception {
            String response = controller.generateOTP(null, AUTHORIZATION);

            assertTrue(response.contains("NDHM_FHIR Empty request object"));
            verifyNoInteractions(healthID);
        }
    }

    @Nested
    @DisplayName("verifyOTPAndGenerateHealthID")
    class VerifyOtpTests {

        @Test
        @DisplayName("should wrap the newly created ABHA in a success response")
        void verifyOTP_shouldReturnServicePayload() throws Exception {
            when(healthID.verifyOTPandGenerateHealthID("{\"otp\":\"123456\"}"))
                    .thenReturn("{\"healthIdNumber\":\"11-1111-1111-1111\"}");

            mockMvc.perform(post("/healthID/verifyOTPAndGenerateHealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"otp\":\"123456\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.healthIdNumber").value("11-1111-1111-1111"));
        }

        @Test
        @DisplayName("should surface a FHIRException as a 5000 error")
        void verifyOTP_shouldReportServiceFailure() throws Exception {
            when(healthID.verifyOTPandGenerateHealthID(anyString())).thenThrow(new FHIRException("wrong OTP"));

            mockMvc.perform(post("/healthID/verifyOTPAndGenerateHealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorMessage").value("wrong OTP"));
        }

        @Test
        @DisplayName("should refuse an empty request object without touching the service")
        void verifyOTP_shouldRejectNullRequest() throws Exception {
            String response = controller.verifyOTPAndGenerateHealthID(null, AUTHORIZATION);

            assertTrue(response.contains("NDHM_FHIR Empty request object"));
            verifyNoInteractions(healthID);
        }
    }

    @Nested
    @DisplayName("getBenhealthID")
    class GetBenHealthIdTests {

        @Test
        @DisplayName("should look the ABHA up by the beneficiaryRegID in the body")
        void getBenhealthID_shouldLookUpByBenRegId() throws Exception {
            when(healthIDService.getBenHealthID(4321L)).thenReturn("{\"healthId\":\"abc@sbx\"}");

            mockMvc.perform(post("/healthID/getBenhealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"beneficiaryRegID\":4321}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.healthId").value("abc@sbx"));

            verify(healthIDService).getBenHealthID(4321L);
        }

        @Test
        @DisplayName("should answer 4001 when beneficiaryRegID is missing from the body")
        void getBenhealthID_shouldRejectMissingBenRegId() throws Exception {
            mockMvc.perform(post("/healthID/getBenhealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(4001))
                    .andExpect(jsonPath("$.errorMessage").value("Missing 'beneficiaryRegID' in request"));

            verifyNoInteractions(healthIDService);
        }

        @Test
        @DisplayName("should report a lookup failure as a 5000 error")
        void getBenhealthID_shouldReportServiceFailure() throws Exception {
            when(healthIDService.getBenHealthID(anyLong())).thenThrow(new IllegalStateException("no mapping"));

            mockMvc.perform(post("/healthID/getBenhealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"beneficiaryRegID\":4321}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                    .andExpect(jsonPath("$.errorMessage").value("Error: no mapping"));
        }

        @Test
        @DisplayName("should report a malformed body as a 5000 error")
        void getBenhealthID_shouldReportMalformedBody() throws Exception {
            String response = controller.getBenhealthID("not-json");

            assertEquals(OutputResponse.GENERIC_FAILURE, new JSONObject(response).getInt("statusCode"));
            verifyNoInteractions(healthIDService);
        }
    }

    @Nested
    @DisplayName("getBenIdForhealthID")
    class GetBenIdForHealthIdTests {

        @Test
        @DisplayName("should look the beneficiary ids up by the healthIdNumber in the body")
        void getBenIdForhealthID_shouldLookUpByHealthIdNumber() throws Exception {
            when(healthIDService.getMappedBenIdForHealthId("11-1111-1111-1111"))
                    .thenReturn("[{\"beneficiaryID\":\"9999\"}]");

            mockMvc.perform(post("/healthID/getBenIdForhealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"healthIdNumber\":\"11-1111-1111-1111\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].beneficiaryID").value("9999"));

            verify(healthIDService).getMappedBenIdForHealthId("11-1111-1111-1111");
        }

        @Test
        @DisplayName("should report a body without healthIdNumber as a 5000 error")
        void getBenIdForhealthID_shouldReportMissingHealthIdNumber() throws Exception {
            mockMvc.perform(post("/healthID/getBenIdForhealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE));

            verifyNoInteractions(healthIDService);
        }

        @Test
        @DisplayName("should report a lookup failure as a 5000 error")
        void getBenIdForhealthID_shouldReportServiceFailure() throws Exception {
            when(healthIDService.getMappedBenIdForHealthId(anyString()))
                    .thenThrow(new IllegalStateException("no beneficiary"));

            mockMvc.perform(post("/healthID/getBenIdForhealthID")
                    .header("Authorization", AUTHORIZATION)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"healthIdNumber\":\"11-1111-1111-1111\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorMessage").value("no beneficiary"));
        }
    }
}
