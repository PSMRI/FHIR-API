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
package com.wipro.fhir.controller.patientdatahandler;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatientDataGatewayController Test Suite")
class PatientDataGatewayControllerTest {

    private static final String AUTHORIZATION = "session-key-123";

    @Mock
    private PatientDataGatewayService patientDataGatewayService;

    @InjectMocks
    private PatientDataGatewayController controller;

    private ResourceRequestHandler request;

    @BeforeEach
    @DisplayName("Build a demographic search request before each test")
    void setUp() throws Exception {
        request = new ResourceRequestHandler();
        request.setHealthIdNumber("11-1111-1111-1111");
    }

    @Test
    @DisplayName("patientDataSearchFromMongo should wrap the matched profile in a success response")
    void searchDemographic_shouldReturnServicePayload() throws Exception {
        when(patientDataGatewayService.searchPatientProfileMongo(AUTHORIZATION, request))
                .thenReturn("{\"amritId\":\"AM-1\"}");

        JSONObject response = new JSONObject(controller.patientDataSearchFromMongo(request, AUTHORIZATION));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals("AM-1", response.getJSONObject("data").getString("amritId"));
        verify(patientDataGatewayService).searchPatientProfileMongo(AUTHORIZATION, request);
    }

    @Test
    @DisplayName("patientDataSearchFromMongo should answer \"patient not found\" when nothing matches")
    void searchDemographic_shouldReportPatientNotFound() throws Exception {
        when(patientDataGatewayService.searchPatientProfileMongo(AUTHORIZATION, request)).thenReturn(null);

        JSONObject response = new JSONObject(controller.patientDataSearchFromMongo(request, AUTHORIZATION));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals("patient not found", response.getJSONObject("data").getString("response"));
    }

    @Test
    @DisplayName("patientDataSearchFromMongo should surface a search failure as a 5000 error")
    void searchDemographic_shouldReportServiceFailure() throws Exception {
        when(patientDataGatewayService.searchPatientProfileMongo(anyString(), any()))
                .thenThrow(new FHIRException("mongo unreachable"));

        JSONObject response = new JSONObject(controller.patientDataSearchFromMongo(request, AUTHORIZATION));

        assertEquals(OutputResponse.GENERIC_FAILURE, response.getInt("statusCode"));
        assertEquals("mongo unreachable", response.getString("errorMessage"));
    }

    @Test
    @DisplayName("patientDataSearchFromMongoPagination should wrap the requested page in a success response")
    void searchPaginated_shouldReturnServicePayload() throws Exception {
        when(patientDataGatewayService.searchPatientProfileMongoPagination(2)).thenReturn("[{\"amritId\":\"AM-1\"}]");

        JSONObject response = new JSONObject(controller.patientDataSearchFromMongoPagination(2));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals("AM-1", response.getJSONArray("data").getJSONObject(0).getString("amritId"));
    }

    @Test
    @DisplayName("patientDataSearchFromMongoPagination should answer \"No data found\" for an empty page")
    void searchPaginated_shouldReportNoData() throws Exception {
        when(patientDataGatewayService.searchPatientProfileMongoPagination(99)).thenReturn(null);

        JSONObject response = new JSONObject(controller.patientDataSearchFromMongoPagination(99));

        assertEquals("No data found", response.getJSONObject("data").getString("response"));
    }

    @Test
    @DisplayName("patientDataSearchFromMongoPagination should surface a search failure as a 5000 error")
    void searchPaginated_shouldReportServiceFailure() throws Exception {
        when(patientDataGatewayService.searchPatientProfileMongoPagination(anyInt()))
                .thenThrow(new FHIRException("page out of range"));

        JSONObject response = new JSONObject(controller.patientDataSearchFromMongoPagination(5));

        assertEquals("page out of range", response.getString("errorMessage"));
    }
}
