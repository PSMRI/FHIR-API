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

import com.wipro.fhir.service.healthID.HealthIDWithBioService;
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
@DisplayName("CreateHealthIDWithBio Test Suite")
class CreateHealthIDWithBioTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String REQUEST = "{\"healthIdNumber\":\"11-1111-1111-1111\"}";

    @Mock
    private HealthIDWithBioService healthIDWithBioService;

    @InjectMocks
    private CreateHealthIDWithBio controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("verifyBio should verify the biometric and wrap the service payload in a success response")
    void verifyBio_shouldReturnServicePayload() throws Exception {
        when(healthIDWithBioService.verifyBio(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithBio/verifyBio")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(healthIDWithBioService).verifyBio(REQUEST);
    }

    @Test
    @DisplayName("verifyBio should surface a FHIRException from the service as a 5000 error")
    void verifyBio_shouldReportServiceFailure() throws Exception {
        when(healthIDWithBioService.verifyBio(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithBio/verifyBio")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("verifyBio should reject an empty request object without calling the service")
    void verifyBio_shouldRejectNullRequest() throws Exception {
        String response = controller.verifyBio(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(healthIDWithBioService);
    }

    @Test
    @DisplayName("checkAndGenerateMobileOTP should generate the mobile OTP and wrap the service payload in a success response")
    void checkAndGenerateMobileOTP_shouldReturnServicePayload() throws Exception {
        when(healthIDWithBioService.generateMobileOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithBio/generateMobileOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(healthIDWithBioService).generateMobileOTP(REQUEST);
    }

    @Test
    @DisplayName("checkAndGenerateMobileOTP should surface a FHIRException from the service as a 5000 error")
    void checkAndGenerateMobileOTP_shouldReportServiceFailure() throws Exception {
        when(healthIDWithBioService.generateMobileOTP(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithBio/generateMobileOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("checkAndGenerateMobileOTP should reject an empty request object without calling the service")
    void checkAndGenerateMobileOTP_shouldRejectNullRequest() throws Exception {
        String response = controller.checkAndGenerateMobileOTP(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(healthIDWithBioService);
    }

    @Test
    @DisplayName("confirmWithAadhaarBio should confirm the enrolment with the Aadhaar biometric and wrap the service payload in a success response")
    void confirmWithAadhaarBio_shouldReturnServicePayload() throws Exception {
        when(healthIDWithBioService.confirmWithAadhaarBio(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithBio/confirmWithAadhaarBio")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(healthIDWithBioService).confirmWithAadhaarBio(REQUEST);
    }

    @Test
    @DisplayName("confirmWithAadhaarBio should surface a FHIRException from the service as a 5000 error")
    void confirmWithAadhaarBio_shouldReportServiceFailure() throws Exception {
        when(healthIDWithBioService.confirmWithAadhaarBio(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithBio/confirmWithAadhaarBio")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("confirmWithAadhaarBio should reject an empty request object without calling the service")
    void confirmWithAadhaarBio_shouldRejectNullRequest() throws Exception {
        String response = controller.confirmWithAadhaarBio(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(healthIDWithBioService);
    }
}
