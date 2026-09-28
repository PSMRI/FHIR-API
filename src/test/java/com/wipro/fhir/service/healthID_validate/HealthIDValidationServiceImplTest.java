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
package com.wipro.fhir.service.healthID_validate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.fhir.service.ndhm.ValidateHealthID_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HealthIDValidationServiceImpl Test Suite")
class HealthIDValidationServiceImplTest {

    private static final String REQUEST = "{\"healthID\":\"abc@sbx\",\"authenticationMode\":\"MOBILE_OTP\"}";

    @Mock
    private ValidateHealthID_NDHMService validateHealthID_NDHMService;

    @InjectMocks
    private HealthIDValidationServiceImpl service;

    @Test
    @DisplayName("generateOTPForHealthIDValidation should pass the ABDM answer straight through")
    void generateOtp_shouldReturnNdhmAnswer() throws Exception {
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

        assertEquals("{\"txnId\":\"txn-1\"}", service.generateOTPForHealthIDValidation(REQUEST));
    }

    @Test
    @DisplayName("generateOTPForHealthIDValidation should fail when ABDM answers with nothing")
    void generateOtp_shouldFailOnNullAnswer() throws Exception {
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(REQUEST)).thenReturn(null);

        FHIRException failure = assertThrows(FHIRException.class,
                () -> service.generateOTPForHealthIDValidation(REQUEST));

        assertEquals("NDHM_FHIR Error while generating OTP", failure.getMessage());
    }

    @Test
    @DisplayName("generateOTPForHealthIDValidation should rewrap a downstream failure as a FHIRException")
    void generateOtp_shouldRewrapDownstreamFailure() throws Exception {
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(REQUEST))
                .thenThrow(new FHIRException("ABDM refused"));

        FHIRException failure = assertThrows(FHIRException.class,
                () -> service.generateOTPForHealthIDValidation(REQUEST));

        assertEquals("ABDM refused", failure.getMessage());
    }

    @Test
    @DisplayName("validateOTPAndHealthID should pass the ABDM answer straight through")
    void validateOtp_shouldReturnNdhmAnswer() throws Exception {
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(REQUEST)).thenReturn("{\"valid\":true}");

        assertEquals("{\"valid\":true}", service.validateOTPAndHealthID(REQUEST));
    }

    @Test
    @DisplayName("validateOTPAndHealthID should fail when ABDM answers with nothing")
    void validateOtp_shouldFailOnNullAnswer() throws Exception {
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(REQUEST)).thenReturn(null);

        FHIRException failure = assertThrows(FHIRException.class, () -> service.validateOTPAndHealthID(REQUEST));

        assertEquals("NDHM_FHIR Error while validating OTP", failure.getMessage());
    }

    @Test
    @DisplayName("validateOTPAndHealthID should rewrap a downstream failure as a FHIRException")
    void validateOtp_shouldRewrapDownstreamFailure() throws Exception {
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(REQUEST))
                .thenThrow(new IllegalStateException("connection reset"));

        FHIRException failure = assertThrows(FHIRException.class, () -> service.validateOTPAndHealthID(REQUEST));

        assertEquals("connection reset", failure.getMessage());
    }
}
