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
package com.wipro.fhir.service.care_context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.ndhm.LinkCareContext_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CareContextServiceImpl Test Suite")
class CareContextServiceImplTest {

    private static final String BOTH_IDS =
            "{\"healthID\":\"abc@sbx\",\"healthIdNumber\":\"11-1111-1111-1111\",\"visitCode\":\"987654\"}";

    @Mock
    private LinkCareContext_NDHMService linkCareContext_NDHMService;

    @Mock
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @InjectMocks
    private CareContextServiceImpl service;

    @Nested
    @DisplayName("generateOTPForCareContext")
    class GenerateOtpTests {

        @Test
        @DisplayName("should pass the ABDM answer straight through")
        void generateOtp_shouldReturnNdhmAnswer() throws Exception {
            when(linkCareContext_NDHMService.generateOTPForCareContext(BOTH_IDS)).thenReturn("{\"txnId\":\"txn-1\"}");

            assertEquals("{\"txnId\":\"txn-1\"}", service.generateOTPForCareContext(BOTH_IDS));
        }

        @Test
        @DisplayName("should fail when ABDM answers with nothing")
        void generateOtp_shouldFailOnNullAnswer() throws Exception {
            when(linkCareContext_NDHMService.generateOTPForCareContext(BOTH_IDS)).thenReturn(null);

            FHIRException failure = assertThrows(FHIRException.class,
                    () -> service.generateOTPForCareContext(BOTH_IDS));

            assertEquals("NDHM_FHIR Error while generating OTP", failure.getMessage());
        }

        @Test
        @DisplayName("should rewrap a downstream failure as a FHIRException")
        void generateOtp_shouldRewrapDownstreamFailure() throws Exception {
            when(linkCareContext_NDHMService.generateOTPForCareContext(BOTH_IDS))
                    .thenThrow(new FHIRException("ABDM refused"));

            assertEquals("ABDM refused", assertThrows(FHIRException.class,
                    () -> service.generateOTPForCareContext(BOTH_IDS)).getMessage());
        }
    }

    @Nested
    @DisplayName("validateOTPAndCreateCareContext")
    class ValidateOtpTests {

        @Test
        @DisplayName("should link the care context and update both ABHA columns when the request carries both")
        void validateOtp_shouldUpdateBothAbhaColumns() throws Exception {
            when(linkCareContext_NDHMService.validateOTPForCareContext(BOTH_IDS)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(BOTH_IDS, "otp-token")).thenReturn("{\"linked\":true}");
            when(benHealthIDMappingRepo.updateHealthIDAndHealthIDNumberForCareContext(
                    "abc@sbx", "11-1111-1111-1111", "987654")).thenReturn(1);

            assertEquals("{\"linked\":true}", service.validateOTPAndCreateCareContext(BOTH_IDS));

            verify(benHealthIDMappingRepo).updateHealthIDAndHealthIDNumberForCareContext(
                    "abc@sbx", "11-1111-1111-1111", "987654");
        }

        @Test
        @DisplayName("should still answer with the linked care context when the ABHA update touches no row")
        void validateOtp_shouldSucceedEvenWhenUpdateTouchesNoRow() throws Exception {
            when(linkCareContext_NDHMService.validateOTPForCareContext(BOTH_IDS)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(BOTH_IDS, "otp-token")).thenReturn("{\"linked\":true}");
            when(benHealthIDMappingRepo.updateHealthIDAndHealthIDNumberForCareContext(
                    "abc@sbx", "11-1111-1111-1111", "987654")).thenReturn(0);

            assertEquals("{\"linked\":true}", service.validateOTPAndCreateCareContext(BOTH_IDS));
        }

        @Test
        @DisplayName("should update only the ABHA address when the request carries no ABHA number")
        void validateOtp_shouldUpdateOnlyHealthId() throws Exception {
            String request = "{\"healthID\":\"abc@sbx\",\"visitCode\":\"987654\"}";
            when(linkCareContext_NDHMService.validateOTPForCareContext(request)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(request, "otp-token")).thenReturn("linked");
            when(benHealthIDMappingRepo.updateHealthIDForCareContext("abc@sbx", "987654")).thenReturn(1);

            assertEquals("linked", service.validateOTPAndCreateCareContext(request));

            verify(benHealthIDMappingRepo).updateHealthIDForCareContext("abc@sbx", "987654");
        }

        @Test
        @DisplayName("should update only the ABHA number when the request carries no ABHA address")
        void validateOtp_shouldUpdateOnlyHealthIdNumber() throws Exception {
            String request = "{\"healthIdNumber\":\"11-1111-1111-1111\",\"visitCode\":\"987654\"}";
            when(linkCareContext_NDHMService.validateOTPForCareContext(request)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(request, "otp-token")).thenReturn("linked");
            when(benHealthIDMappingRepo.updateHealthIDNumberForCareContext("11-1111-1111-1111", "987654"))
                    .thenReturn(1);

            assertEquals("linked", service.validateOTPAndCreateCareContext(request));

            verify(benHealthIDMappingRepo).updateHealthIDNumberForCareContext("11-1111-1111-1111", "987654");
        }

        @Test
        @DisplayName("should skip the ABHA update entirely when the request carries neither identifier")
        void validateOtp_shouldSkipUpdateWithoutAnyIdentifier() throws Exception {
            String request = "{\"visitCode\":\"987654\"}";
            when(linkCareContext_NDHMService.validateOTPForCareContext(request)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(request, "otp-token")).thenReturn("linked");

            assertEquals("linked", service.validateOTPAndCreateCareContext(request));

            verifyNoInteractions(benHealthIDMappingRepo);
        }

        @Test
        @DisplayName("should treat a JSON-null identifier the same as an absent one")
        void validateOtp_shouldTreatJsonNullAsAbsent() throws Exception {
            String request = "{\"healthID\":null,\"healthIdNumber\":null,\"visitCode\":\"987654\"}";
            when(linkCareContext_NDHMService.validateOTPForCareContext(request)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(request, "otp-token")).thenReturn("linked");

            assertEquals("linked", service.validateOTPAndCreateCareContext(request));

            verifyNoInteractions(benHealthIDMappingRepo);
        }

        @Test
        @DisplayName("should fail when the OTP does not validate")
        void validateOtp_shouldFailWhenOtpInvalid() throws Exception {
            when(linkCareContext_NDHMService.validateOTPForCareContext(BOTH_IDS)).thenReturn(null);

            assertEquals("NDHM_FHIR Error while validating OTP", assertThrows(FHIRException.class,
                    () -> service.validateOTPAndCreateCareContext(BOTH_IDS)).getMessage());
        }

        @Test
        @DisplayName("should fail when the care context cannot be added")
        void validateOtp_shouldFailWhenCareContextNotAdded() throws Exception {
            when(linkCareContext_NDHMService.validateOTPForCareContext(BOTH_IDS)).thenReturn("otp-token");
            when(linkCareContext_NDHMService.addCareContext(BOTH_IDS, "otp-token")).thenReturn(null);

            assertEquals("NDHM_FHIR Error while adding care context", assertThrows(FHIRException.class,
                    () -> service.validateOTPAndCreateCareContext(BOTH_IDS)).getMessage());
        }

        @Test
        @DisplayName("should rewrap a downstream failure as a FHIRException")
        void validateOtp_shouldRewrapDownstreamFailure() throws Exception {
            when(linkCareContext_NDHMService.validateOTPForCareContext(BOTH_IDS))
                    .thenThrow(new IllegalStateException("gateway timeout"));

            assertEquals("gateway timeout", assertThrows(FHIRException.class,
                    () -> service.validateOTPAndCreateCareContext(BOTH_IDS)).getMessage());
        }
    }
}
