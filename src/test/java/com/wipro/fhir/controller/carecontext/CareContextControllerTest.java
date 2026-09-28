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
package com.wipro.fhir.controller.carecontext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.wipro.fhir.service.care_context.CareContextService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("CareContextController Test Suite")
class CareContextControllerTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String REQUEST = "{\"healthIdNumber\":\"11-1111-1111-1111\"}";

    @Mock
    private CareContextService careContextService;

    @InjectMocks
    private CareContextController controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("generateOTP should generate an OTP for care-context linking and wrap the service payload in a success response")
    void generateOTP_shouldReturnServicePayload() throws Exception {
        when(careContextService.generateOTPForCareContext(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/careContext/generateOTPForCareContext")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(careContextService).generateOTPForCareContext(REQUEST);
    }

    @Test
    @DisplayName("generateOTP should surface a FHIRException from the service as a 5000 error")
    void generateOTP_shouldReportServiceFailure() throws Exception {
        when(careContextService.generateOTPForCareContext(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/careContext/generateOTPForCareContext")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("generateOTP should reject an empty request object without calling the service")
    void generateOTP_shouldRejectNullRequest() throws Exception {
        String response = controller.generateOTP(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(careContextService);
    }

    @Test
    @DisplayName("validateOTPAndCreateCareContext should validate the OTP and create the care context and wrap the service payload in a success response")
    void validateOTPAndCreateCareContext_shouldReturnServicePayload() throws Exception {
        when(careContextService.validateOTPAndCreateCareContext(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/careContext/validateOTPAndCreateCareContext")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(careContextService).validateOTPAndCreateCareContext(REQUEST);
    }

    @Test
    @DisplayName("validateOTPAndCreateCareContext should surface a FHIRException from the service as a 5000 error")
    void validateOTPAndCreateCareContext_shouldReportServiceFailure() throws Exception {
        when(careContextService.validateOTPAndCreateCareContext(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/careContext/validateOTPAndCreateCareContext")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("validateOTPAndCreateCareContext should reject an empty request object without calling the service")
    void validateOTPAndCreateCareContext_shouldRejectNullRequest() throws Exception {
        String response = controller.validateOTPAndCreateCareContext(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(careContextService);
    }

    @Test
    @DisplayName("saveCareContextToMongo is a stub, so it answers with the untouched generic failure")
    void saveCareContextToMongo_shouldReturnUntouchedResponse() throws Exception {
        mockMvc.perform(post("/careContext/addCarecontextToMongo")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.status").value("FAILURE"));

        verifyNoInteractions(careContextService);
    }
}
