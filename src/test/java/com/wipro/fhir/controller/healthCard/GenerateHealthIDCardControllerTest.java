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
package com.wipro.fhir.controller.healthCard;

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

import com.wipro.fhir.service.healthID.HealthID_CardService;
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
@DisplayName("GenerateHealthIDCardController Test Suite")
class GenerateHealthIDCardControllerTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String REQUEST = "{\"healthIdNumber\":\"11-1111-1111-1111\"}";

    @Mock
    private HealthID_CardService healthID_CardService;

    @InjectMocks
    private GenerateHealthIDCardController controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("mapHealthIDToBeneficiary should generate an OTP for the ABHA card and wrap the service payload in a success response")
    void mapHealthIDToBeneficiary_shouldReturnServicePayload() throws Exception {
        when(healthID_CardService.generateOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDCard/generateOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(healthID_CardService).generateOTP(REQUEST);
    }

    @Test
    @DisplayName("mapHealthIDToBeneficiary should surface a FHIRException from the service as a 5000 error")
    void mapHealthIDToBeneficiary_shouldReportServiceFailure() throws Exception {
        when(healthID_CardService.generateOTP(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDCard/generateOTP")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("mapHealthIDToBeneficiary should reject an empty request object without calling the service")
    void mapHealthIDToBeneficiary_shouldRejectNullRequest() throws Exception {
        String response = controller.mapHealthIDToBeneficiary(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(healthID_CardService);
    }

    @Test
    @DisplayName("verifyOTPAndGenerateHealthCard should verify the OTP and generate the ABHA card and wrap the service payload in a success response")
    void verifyOTPAndGenerateHealthCard_shouldReturnServicePayload() throws Exception {
        when(healthID_CardService.verifyOTPAndGenerateCard(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        mockMvc.perform(post("/healthIDCard/verifyOTPAndGenerateHealthCard")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.txnId").value("txn-1"));

        verify(healthID_CardService).verifyOTPAndGenerateCard(REQUEST);
    }

    @Test
    @DisplayName("verifyOTPAndGenerateHealthCard should surface a FHIRException from the service as a 5000 error")
    void verifyOTPAndGenerateHealthCard_shouldReportServiceFailure() throws Exception {
        when(healthID_CardService.verifyOTPAndGenerateCard(anyString())).thenThrow(new FHIRException("downstream refused"));

        mockMvc.perform(post("/healthIDCard/verifyOTPAndGenerateHealthCard")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("downstream refused"));
    }

    @Test
    @DisplayName("verifyOTPAndGenerateHealthCard should reject an empty request object without calling the service")
    void verifyOTPAndGenerateHealthCard_shouldRejectNullRequest() throws Exception {
        String response = controller.verifyOTPAndGenerateHealthCard(null, AUTHORIZATION);

        assertEquals(OutputResponse.GENERIC_FAILURE, new org.json.JSONObject(response).getInt("statusCode"));
        assertTrue(response.contains("NDHM_FHIR Empty request object"));
        verifyNoInteractions(healthID_CardService);
    }
}
