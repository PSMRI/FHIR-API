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
package com.wipro.fhir.controller.facility;

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

import com.wipro.fhir.service.facility.FacilityService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("FacilityController Test Suite")
class FacilityControllerTest {

    private static final String AUTHORIZATION = "session-key-123";

    @Mock
    private FacilityService facilityService;

    @InjectMocks
    private FacilityController controller;

    private MockMvc mockMvc;

    @BeforeEach
    @DisplayName("Stand the controller up on a standalone MockMvc before each test")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("getAbdmRegisteredFacilities should wrap the registered facility list in a success response")
    void getAbdmRegisteredFacilities_shouldReturnServicePayload() throws Exception {
        when(facilityService.fetchRegisteredFacilities()).thenReturn("[{\"facilityId\":\"IN0710000001\"}]");

        mockMvc.perform(get("/facility/getAbdmRegisteredFacilities")
                .header("Authorization", AUTHORIZATION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data[0].facilityId").value("IN0710000001"));

        verify(facilityService).fetchRegisteredFacilities();
    }

    @Test
    @DisplayName("getAbdmRegisteredFacilities should surface a FHIRException as a 5000 error")
    void getAbdmRegisteredFacilities_shouldReportServiceFailure() throws Exception {
        when(facilityService.fetchRegisteredFacilities()).thenThrow(new FHIRException("registry unreachable"));

        mockMvc.perform(get("/facility/getAbdmRegisteredFacilities")
                .header("Authorization", AUTHORIZATION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("registry unreachable"));
    }

    @Test
    @DisplayName("saveAbdmFacilityForVisit should hand the raw body to the service and wrap its answer")
    void saveAbdmFacilityForVisit_shouldReturnServicePayload() throws Exception {
        String request = "{\"visitCode\":\"12345\",\"abdmFacilityId\":\"IN0710000001\"}";
        when(facilityService.saveAbdmFacilityId(request)).thenReturn("saved");

        mockMvc.perform(post("/facility/saveAbdmFacilityId")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.SUCCESS))
                .andExpect(jsonPath("$.data.response").value("saved"));

        verify(facilityService).saveAbdmFacilityId(request);
    }

    @Test
    @DisplayName("saveAbdmFacilityForVisit should surface a FHIRException as a 5000 error")
    void saveAbdmFacilityForVisit_shouldReportServiceFailure() throws Exception {
        when(facilityService.saveAbdmFacilityId("{}")).thenThrow(new FHIRException("visit not found"));

        mockMvc.perform(post("/facility/saveAbdmFacilityId")
                .header("Authorization", AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(OutputResponse.GENERIC_FAILURE))
                .andExpect(jsonPath("$.errorMessage").value("visit not found"));
    }
}
