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
package com.wipro.fhir.controller.generateresource;

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
import com.wipro.fhir.service.bundle_creation.DiagnosticRecordResourceBundle;
import com.wipro.fhir.service.bundle_creation.OPConsultResourceBundle;
import com.wipro.fhir.service.bundle_creation.PrescriptionResourceBundle;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResourceRequestGateway Test Suite")
class ResourceRequestGatewayTest {

    private static final String AUTHORIZATION = "session-key-123";

    @Mock
    private OPConsultResourceBundle opConsultRecordBundle;

    @Mock
    private PrescriptionResourceBundle prescriptionRecordBundle;

    @Mock
    private DiagnosticRecordResourceBundle diagnosticReportRecord;

    @InjectMocks
    private ResourceRequestGateway gateway;

    private ResourceRequestHandler request;

    @BeforeEach
    @DisplayName("Build a resource request for a single visit before each test")
    void setUp() throws Exception {
        request = new ResourceRequestHandler();
        request.setBeneficiaryRegID(BigInteger.valueOf(4321L));
        request.setVisitCode(BigInteger.valueOf(987654L));
    }

    private JSONObject parse(String response) throws Exception {
        return new JSONObject(response);
    }

    @Test
    @DisplayName("getPatientResource should wrap the OP consult bundle in a success response")
    void getPatientResource_shouldReturnBundle() throws Exception {
        when(opConsultRecordBundle.populateOPConsultRecordResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenReturn("{\"resourceType\":\"Bundle\"}");

        JSONObject response = parse(gateway.getPatientResource(request, AUTHORIZATION));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals("Bundle", response.getJSONObject("data").getString("resourceType"));
        verify(opConsultRecordBundle).populateOPConsultRecordResourceBundle(request, null);
    }

    @Test
    @DisplayName("getPatientResource should report a bundle-creation failure as a 5000 error")
    void getPatientResource_shouldReportFailure() throws Exception {
        when(opConsultRecordBundle.populateOPConsultRecordResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenThrow(new FHIRException("no visit data"));

        JSONObject response = parse(gateway.getPatientResource(request, AUTHORIZATION));

        assertEquals(OutputResponse.GENERIC_FAILURE, response.getInt("statusCode"));
        assertEquals("error in creating OP Consult Record bundle : no visit data", response.getString("errorMessage"));
    }

    @Test
    @DisplayName("getDiagnosticReportRecord should wrap the diagnostic bundle in a success response")
    void getDiagnosticReportRecord_shouldReturnBundle() throws Exception {
        when(diagnosticReportRecord.populateDiagnosticReportResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenReturn("{\"resourceType\":\"Bundle\"}");

        JSONObject response = parse(gateway.getDiagnosticReportRecord(request, AUTHORIZATION));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        verify(diagnosticReportRecord).populateDiagnosticReportResourceBundle(request, null);
    }

    @Test
    @DisplayName("getDiagnosticReportRecord should report a bundle-creation failure as a 5000 error")
    void getDiagnosticReportRecord_shouldReportFailure() throws Exception {
        when(diagnosticReportRecord.populateDiagnosticReportResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenThrow(new FHIRException("no lab results"));

        JSONObject response = parse(gateway.getDiagnosticReportRecord(request, AUTHORIZATION));

        assertEquals("error in creating Diagnostic Report Record bundle : no lab results",
                response.getString("errorMessage"));
    }

    @Test
    @DisplayName("getPrescriptionRecord should wrap the prescription bundle in a success response")
    void getPrescriptionRecord_shouldReturnBundle() throws Exception {
        when(prescriptionRecordBundle.populatePrescriptionResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenReturn("{\"resourceType\":\"Bundle\"}");

        JSONObject response = parse(gateway.getPrescriptionRecord(request, AUTHORIZATION));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        verify(prescriptionRecordBundle).populatePrescriptionResourceBundle(request, null);
    }

    @Test
    @DisplayName("getPrescriptionRecord should report a bundle-creation failure as a 5000 error")
    void getPrescriptionRecord_shouldReportFailure() throws Exception {
        when(prescriptionRecordBundle.populatePrescriptionResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenThrow(new FHIRException("no prescription"));

        JSONObject response = parse(gateway.getPrescriptionRecord(request, AUTHORIZATION));

        assertEquals("error in creating Prescription Record bundle : no prescription",
                response.getString("errorMessage"));
    }
}
