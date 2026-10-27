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
package com.wipro.fhir.controller.health;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.wipro.fhir.service.health.HealthService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("HealthController Test Suite")
class HealthControllerTest {

    @Mock
    private HealthService healthService;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Set up standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new HealthController(healthService)).build();
    }

    private Map<String, Object> healthResponse(String overallStatus) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", overallStatus);
        response.put("timestamp", "2025-06-25T10:00:00Z");
        return response;
    }

    @Test
    @DisplayName("checkHealth should return 200 with the payload when all services are UP")
    void checkHealth_shouldReturnOkWhenStatusIsUp() throws Exception {
        when(healthService.checkHealth()).thenReturn(healthResponse("UP"));

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").value("2025-06-25T10:00:00Z"));
    }

    @Test
    @DisplayName("checkHealth should return 200 when DEGRADED, since the instance is still operational")
    void checkHealth_shouldReturnOkWhenStatusIsDegraded() throws Exception {
        when(healthService.checkHealth()).thenReturn(healthResponse("DEGRADED"));

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEGRADED"));
    }

    @Test
    @DisplayName("checkHealth should return 503 when a critical service is DOWN")
    void checkHealth_shouldReturnServiceUnavailableWhenStatusIsDown() throws Exception {
        when(healthService.checkHealth()).thenReturn(healthResponse("DOWN"));

        mockMvc.perform(get("/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("DOWN"));
    }

    @Test
    @DisplayName("checkHealth should return 503 with a DOWN payload when the service throws unexpectedly")
    void checkHealth_shouldReturnServiceUnavailableWhenServiceThrows() throws Exception {
        when(healthService.checkHealth()).thenThrow(new IllegalStateException("unexpected failure"));

        mockMvc.perform(get("/health"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
