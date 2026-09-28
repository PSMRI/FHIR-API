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

import com.wipro.fhir.service.healthID.HealthIDWithUIDService;
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
@DisplayName("CreateHealthIDWithUID Test Suite")
class CreateHealthIDWithUIDTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String REQUEST = "{\"healthIdNumber\":\"11-1111-1111-1111\"}";

    @Mock
    private HealthIDWithUIDService HealthIDWithUIDService;

    @InjectMocks
    private CreateHealthIDWithUID controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("generateOTP should generate an Aadhaar OTP and wrap the service payload in a success response")
    void generateOTP_shouldReturnServicePayload() throws Exception {
        when(HealthIDWithUIDService.generateOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithUID/generateOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(HealthIDWithUIDService).generateOTP(REQUEST);
    }

    @Test
    @DisplayName("generateOTP should surface a FHIRException from the service as a 5000 error")
    void generateOTP_shouldReportServiceFailure() throws Exception {
        when(HealthIDWithUIDService.generateOTP(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithUID/generateOTP")
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
        verifyNoInteractions(HealthIDWithUIDService);
    }

    @Test
    @DisplayName("verifyOTP should verify the Aadhaar OTP and wrap the service payload in a success response")
    void verifyOTP_shouldReturnServicePayload() throws Exception {
        when(HealthIDWithUIDService.verifyOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithUID/verifyOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(HealthIDWithUIDService).verifyOTP(REQUEST);
    }

    @Test
    @DisplayName("verifyOTP should surface a FHIRException from the service as a 5000 error")
    void verifyOTP_shouldReportServiceFailure() throws Exception {
        when(HealthIDWithUIDService.verifyOTP(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithUID/verifyOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("verifyOTP should reject an empty request object without calling the service")
    void verifyOTP_shouldRejectNullRequest() throws Exception {
        String response = controller.verifyOTP(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(HealthIDWithUIDService);
    }

    @Test
    @DisplayName("checkAndGenerateMobileOTP should check the mobile and generate an OTP and wrap the service payload in a success response")
    void checkAndGenerateMobileOTP_shouldReturnServicePayload() throws Exception {
        when(HealthIDWithUIDService.checkAndGenerateOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithUID/checkAndGenerateMobileOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(HealthIDWithUIDService).checkAndGenerateOTP(REQUEST);
    }

    @Test
    @DisplayName("checkAndGenerateMobileOTP should surface a FHIRException from the service as a 5000 error")
    void checkAndGenerateMobileOTP_shouldReportServiceFailure() throws Exception {
        when(HealthIDWithUIDService.checkAndGenerateOTP(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithUID/checkAndGenerateMobileOTP")
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
        verifyNoInteractions(HealthIDWithUIDService);
    }

    @Test
    @DisplayName("verifyMobileOTP should verify the mobile OTP and wrap the service payload in a success response")
    void verifyMobileOTP_shouldReturnServicePayload() throws Exception {
        when(HealthIDWithUIDService.verifyMobileOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithUID/verifyMobileOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(HealthIDWithUIDService).verifyMobileOTP(REQUEST);
    }

    @Test
    @DisplayName("verifyMobileOTP should surface a FHIRException from the service as a 5000 error")
    void verifyMobileOTP_shouldReportServiceFailure() throws Exception {
        when(HealthIDWithUIDService.verifyMobileOTP(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithUID/verifyMobileOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("verifyMobileOTP should reject an empty request object without calling the service")
    void verifyMobileOTP_shouldRejectNullRequest() throws Exception {
        String response = controller.verifyMobileOTP(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(HealthIDWithUIDService);
    }

    @Test
    @DisplayName("createHealthIDWithUID should create the ABHA from the Aadhaar UID and wrap the service payload in a success response")
    void createHealthIDWithUID_shouldReturnServicePayload() throws Exception {
        when(HealthIDWithUIDService.createHealthIDWithUID(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDWithUID/createHealthIDWithUID")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(HealthIDWithUIDService).createHealthIDWithUID(REQUEST);
    }

    @Test
    @DisplayName("createHealthIDWithUID should surface a FHIRException from the service as a 5000 error")
    void createHealthIDWithUID_shouldReportServiceFailure() throws Exception {
        when(HealthIDWithUIDService.createHealthIDWithUID(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDWithUID/createHealthIDWithUID")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("createHealthIDWithUID should reject an empty request object without calling the service")
    void createHealthIDWithUID_shouldRejectNullRequest() throws Exception {
        String response = controller.createHealthIDWithUID(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(HealthIDWithUIDService);
    }
}
