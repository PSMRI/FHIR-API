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
package com.wipro.fhir.controller.v3.abha;

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

import com.wipro.fhir.service.v3.abha.LoginAbhaV3Service;
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
@DisplayName("LoginAbhaV3Controller Test Suite")
class LoginAbhaV3ControllerTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String REQUEST = "{\"healthIdNumber\":\"11-1111-1111-1111\"}";

    @Mock
    private LoginAbhaV3Service loginAbhaV3Service;

    @InjectMocks
    private LoginAbhaV3Controller controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("requestOtpForAbhaLogin should request an OTP for the ABHA login and wrap the service payload in a success response")
    void requestOtpForAbhaLogin_shouldReturnServicePayload() throws Exception {
        when(loginAbhaV3Service.requestOtpForAbhaLogin(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/abhaLogin/abhaLoginRequestOtp")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(loginAbhaV3Service).requestOtpForAbhaLogin(REQUEST);
    }

    @Test
    @DisplayName("requestOtpForAbhaLogin should surface a FHIRException from the service as a 5000 error")
    void requestOtpForAbhaLogin_shouldReportServiceFailure() throws Exception {
        when(loginAbhaV3Service.requestOtpForAbhaLogin(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/abhaLogin/abhaLoginRequestOtp")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("requestOtpForAbhaLogin should reject an empty request object without calling the service")
    void requestOtpForAbhaLogin_shouldRejectNullRequest() throws Exception {
        String response = controller.requestOtpForAbhaLogin(null);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(loginAbhaV3Service);
    }

    @Test
    @DisplayName("verifyAbhaLogin should verify the ABHA login and wrap the service payload in a success response")
    void verifyAbhaLogin_shouldReturnServicePayload() throws Exception {
        when(loginAbhaV3Service.verifyAbhaLogin(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/abhaLogin/verifyAbhaLogin")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(loginAbhaV3Service).verifyAbhaLogin(REQUEST);
    }

    @Test
    @DisplayName("verifyAbhaLogin should surface a FHIRException from the service as a 5000 error")
    void verifyAbhaLogin_shouldReportServiceFailure() throws Exception {
        when(loginAbhaV3Service.verifyAbhaLogin(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/abhaLogin/verifyAbhaLogin")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("verifyAbhaLogin should reject an empty request object without calling the service")
    void verifyAbhaLogin_shouldRejectNullRequest() throws Exception {
        String response = controller.verifyAbhaLogin(null);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(loginAbhaV3Service);
    }

    @Test
    @DisplayName("printWebLoginPhrCard should print the PHR card for a web login and wrap the service payload in a success response")
    void printWebLoginPhrCard_shouldReturnServicePayload() throws Exception {
        when(loginAbhaV3Service.getWebLoginPhrCard(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/abhaLogin/printWebLoginPhrCard")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(loginAbhaV3Service).getWebLoginPhrCard(REQUEST);
    }

    @Test
    @DisplayName("printWebLoginPhrCard should surface a FHIRException from the service as a 5000 error")
    void printWebLoginPhrCard_shouldReportServiceFailure() throws Exception {
        when(loginAbhaV3Service.getWebLoginPhrCard(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/abhaLogin/printWebLoginPhrCard")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("printWebLoginPhrCard should reject an empty request object without calling the service")
    void printWebLoginPhrCard_shouldRejectNullRequest() throws Exception {
        String response = controller.printWebLoginPhrCard(null);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(loginAbhaV3Service);
    }
}
