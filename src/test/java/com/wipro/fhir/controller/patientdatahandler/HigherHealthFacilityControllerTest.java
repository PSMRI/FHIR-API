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

import java.math.BigInteger;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HigherHealthFacilityController Test Suite")
class HigherHealthFacilityControllerTest {

    @Mock
    private HigherHealthFacilityServiceImpl higherHealthFacilityServiceImpl;

    @InjectMocks
    private HigherHealthFacilityController controller;

    private ResourceRequestHandler request;

    @BeforeEach
    @DisplayName("Build a resource request for a single beneficiary before each test")
    void setUp() throws Exception {
        request = new ResourceRequestHandler();
        request.setBeneficiaryRegID(BigInteger.valueOf(4321L));
    }

    @Test
    @DisplayName("feedPatientDemographicData should wrap the service answer in a success response")
    void feedPatientDemographicData_shouldReturnServicePayload() throws Exception {
        when(higherHealthFacilityServiceImpl.updateBengenIDToHigherHealthFacilityBeneficiary(request))
                .thenReturn("{\"updated\":1}");

        JSONObject response = new JSONObject(controller.feedPatientDemographicData(request));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals(1, response.getJSONObject("data").getInt("updated"));
        verify(higherHealthFacilityServiceImpl).updateBengenIDToHigherHealthFacilityBeneficiary(request);
    }

    @Test
    @DisplayName("feedPatientDemographicData should report a null service answer as a 5000 error")
    void feedPatientDemographicData_shouldReportNullAnswer() throws Exception {
        when(higherHealthFacilityServiceImpl.updateBengenIDToHigherHealthFacilityBeneficiary(request))
                .thenReturn(null);

        JSONObject response = new JSONObject(controller.feedPatientDemographicData(request));

        assertEquals(OutputResponse.GENERIC_FAILURE, response.getInt("statusCode"));
        assertEquals("Error in updating Beneficary ID to higher health data", response.getString("errorMessage"));
    }

    @Test
    @DisplayName("feedPatientDemographicData should report a service failure with the exception message appended")
    void feedPatientDemographicData_shouldReportServiceFailure() throws Exception {
        when(higherHealthFacilityServiceImpl.updateBengenIDToHigherHealthFacilityBeneficiary(any()))
                .thenThrow(new FHIRException("OpenMRS refused"));

        JSONObject response = new JSONObject(controller.feedPatientDemographicData(request));

        assertEquals("Error in updating Beneficary ID to higher health data. OpenMRS refused",
                response.getString("errorMessage"));
    }

    @Test
    @DisplayName("getCLinicalDataHigherhealthFacility should wrap the clinical data in a success response")
    void getClinicalData_shouldReturnServicePayload() throws Exception {
        when(higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(request))
                .thenReturn("[{\"visitCode\":\"12345\"}]");

        JSONObject response = new JSONObject(controller.getCLinicalDataHigherhealthFacility(request));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals("12345", response.getJSONArray("data").getJSONObject(0).getString("visitCode"));
    }

    @Test
    @DisplayName("getCLinicalDataHigherhealthFacility should report a null service answer as a 5000 error")
    void getClinicalData_shouldReportNullAnswer() throws Exception {
        when(higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(request)).thenReturn(null);

        JSONObject response = new JSONObject(controller.getCLinicalDataHigherhealthFacility(request));

        assertEquals("Error in getting higher health data", response.getString("errorMessage"));
    }

    @Test
    @DisplayName("getCLinicalDataHigherhealthFacility should report a service failure with the message appended")
    void getClinicalData_shouldReportServiceFailure() throws Exception {
        when(higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(any()))
                .thenThrow(new FHIRException("feed unavailable"));

        JSONObject response = new JSONObject(controller.getCLinicalDataHigherhealthFacility(request));

        assertEquals("Error in getting higher health data. feed unavailable", response.getString("errorMessage"));
    }
}
