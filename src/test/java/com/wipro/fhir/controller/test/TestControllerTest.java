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
package com.wipro.fhir.controller.test;

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
import com.wipro.fhir.service.bundle_creation.OPConsultResourceBundleImpl;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test (ATOM feed probe controller) Test Suite")
class TestControllerTest {

    private static final String AUTHORIZATION = "session-key-123";

    @Mock
    private OPConsultResourceBundleImpl oPConsultRecordBundleImpl;

    @InjectMocks
    private com.wipro.fhir.controller.test.Test controller;

    private ResourceRequestHandler request;

    @BeforeEach
    @DisplayName("Build a resource request for a single visit before each test")
    void setUp() throws Exception {
        request = new ResourceRequestHandler();
        request.setVisitCode(BigInteger.valueOf(987654L));
    }

    @Test
    @DisplayName("parseFeeds should wrap the generated OP consult bundle in a success response")
    void parseFeeds_shouldReturnBundle() throws Exception {
        when(oPConsultRecordBundleImpl.populateOPConsultRecordResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenReturn("{\"resourceType\":\"Bundle\"}");

        JSONObject response = new JSONObject(controller.parseFeeds(request, AUTHORIZATION));

        assertEquals(OutputResponse.SUCCESS, response.getInt("statusCode"));
        assertEquals("Bundle", response.getJSONObject("data").getString("resourceType"));
        verify(oPConsultRecordBundleImpl).populateOPConsultRecordResourceBundle(request, null);
    }

    @Test
    @DisplayName("parseFeeds should swallow a bundle failure and answer with the untouched generic failure")
    void parseFeeds_shouldSwallowBundleFailure() throws Exception {
        when(oPConsultRecordBundleImpl.populateOPConsultRecordResourceBundle(any(ResourceRequestHandler.class), isNull()))
                .thenThrow(new FHIRException("no visit data"));

        JSONObject response = new JSONObject(controller.parseFeeds(request, AUTHORIZATION));

        assertEquals(OutputResponse.GENERIC_FAILURE, response.getInt("statusCode"));
        assertEquals("Failed with generic error", response.getString("errorMessage"),
                "the probe endpoint logs the failure but never sets an error on the response");
    }
}
