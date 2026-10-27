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
package com.wipro.fhir.service.ndhm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The care-context linking flow against the ABDM consent-manager gateway. Every step
 * posts a correlated request and then reads ABDM's asynchronous answer back out of the
 * Mongo collection the gateway callback writes into.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("LinkCareContext_NDHMServiceImpl Test Suite")
class LinkCareContext_NDHMServiceImplTest {

    private static final String OTP_URL = "http://abdm.example.org/carecontext/otp";
    private static final String VALIDATE_URL = "http://abdm.example.org/carecontext/verify";
    private static final String ADD_URL = "http://abdm.example.org/carecontext/add";

    @Mock
    private HttpUtils httpUtils;

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private GenerateSession_NDHMService generateSession_NDHM;

    @InjectMocks
    private LinkCareContext_NDHMServiceImpl service;

    @BeforeEach
    @DisplayName("Configure the ABDM endpoints and stub the shared plumbing")
    void setUp() throws Exception {
        ReflectionTestUtils.setField(service, "abdmFacilityId", "IN-DEFAULT-1");
        ReflectionTestUtils.setField(service, "abhaMode", "sbx");
        ReflectionTestUtils.setField(service, "clientSecret", "secret-1");
        ReflectionTestUtils.setField(service, "generateOTPForCareContext", OTP_URL);
        ReflectionTestUtils.setField(service, "validateOTPForCareContext", VALIDATE_URL);
        ReflectionTestUtils.setField(service, "addCareContext", ADD_URL);

        when(generateSession_NDHM.getNDHMAuthToken()).thenReturn("Bearer abdm-token");
        NDHMRequest request = new NDHMRequest();
        request.setRequestId("req-1");
        request.setTimestamp("2026-08-24T10:00:00.000Z");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(request);
        when(common_NDHMService.getHeaders(anyString(), anyString())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.ACCEPTED));
        when(common_NDHMService.getStatusCode(any())).thenReturn("202 ACCEPTED");
    }

    private JsonObject sentPayload() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(httpUtils).postWithResponseEntity(anyString(), captor.capture(), any(HttpHeaders.class));
        return JsonParser.parseString(captor.getValue()).getAsJsonObject();
    }

    @Nested
    @DisplayName("generateOTPForCareContext")
    class GenerateOtpTests {

        @Test
        @DisplayName("should answer with the transaction id the ABDM callback carried")
        void generateOtp_shouldAnswerWithTransactionId() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            String result = service.generateOTPForCareContext(
                    "{\"healthID\":\"abc@sbx\",\"authenticationMode\":\"MOBILE\"}");

            assertEquals("txn-1", JsonParser.parseString(result).getAsJsonObject().get("txnId").getAsString());
        }

        @Test
        @DisplayName("should ask for the KYC-and-link purpose against the configured facility")
        void generateOtp_shouldAskForKycAndLink() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForCareContext("{\"healthID\":\"abc@sbx\"}");

            JsonObject query = sentPayload().getAsJsonObject("query");
            assertEquals("KYC_AND_LINK", query.get("purpose").getAsString());
            assertEquals("MOBILE_OTP", query.get("authMode").getAsString());
            assertEquals("abc@sbx", query.get("id").getAsString());
            assertEquals("IN-DEFAULT-1", query.getAsJsonObject("requester").get("id").getAsString());
            assertEquals("HIP", query.getAsJsonObject("requester").get("type").getAsString());
        }

        @Test
        @DisplayName("should route the request through the facility the caller named")
        void generateOtp_shouldPreferRequestFacility() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForCareContext("{\"healthID\":\"abc@sbx\",\"abdmFacilityId\":\"IN0710000001\","
                    + "\"abdmFacilityName\":\"PHC Kanke\"}");

            assertEquals("IN0710000001",
                    sentPayload().getAsJsonObject("query").getAsJsonObject("requester").get("id").getAsString());
        }

        @Test
        @DisplayName("should ask for an Aadhaar OTP when that is the authentication mode")
        void generateOtp_shouldAskForAadhaarOtp() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForCareContext(
                    "{\"healthID\":\"abc@sbx\",\"authenticationMode\":\"AADHAR\"}");

            assertEquals("AADHAAR_OTP",
                    sentPayload().getAsJsonObject("query").get("authMode").getAsString());
        }

        @Test
        @DisplayName("should fall back to the ABHA number when no ABHA address was given")
        void generateOtp_shouldFallBackToAbhaNumber() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForCareContext("{\"healthIdNumber\":\"11-1111-1111-1111\"}");

            assertEquals("11-1111-1111-1111",
                    sentPayload().getAsJsonObject("query").get("id").getAsString());
        }

        @Test
        @DisplayName("should insist on an ABHA address or number")
        void generateOtp_shouldInsistOnAbhaIdentifier() {
            assertEquals("Please pass ABHA/ABHA Number", assertThrows(FHIRException.class,
                    () -> service.generateOTPForCareContext("{}")).getMessage());
        }

        @Test
        @DisplayName("should fall back to the sandbox consent manager for an unrecognised mode")
        void generateOtp_shouldFallBackToSandboxMode() throws Exception {
            ReflectionTestUtils.setField(service, "abhaMode", "staging");
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForCareContext("{\"healthID\":\"abc@sbx\"}");

            verify(common_NDHMService).getHeaders("Bearer abdm-token", "sbx");
        }

        @Test
        @DisplayName("should surface the ABDM error the callback carried")
        void generateOtp_shouldSurfaceCallbackError() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Error\":{\"Message\":\"ABHA not found\"}}");

            assertEquals("NDHM_FHIR ABHA not found", assertThrows(FHIRException.class,
                    () -> service.generateOTPForCareContext("{\"healthID\":\"abc@sbx\"}")).getMessage());
        }

        @Test
        @DisplayName("should answer with an empty map when the ABDM callback never arrived")
        void generateOtp_shouldAnswerEmptyWhenCallbackNeverArrived() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("failure");

            assertEquals("{}", service.generateOTPForCareContext("{\"healthID\":\"abc@sbx\"}"));
        }

        @Test
        @DisplayName("should answer with an empty map when ABDM rejects the request outright")
        void generateOtp_shouldAnswerEmptyOnRejection() throws Exception {
            when(common_NDHMService.getStatusCode(any())).thenReturn("400 BAD_REQUEST");

            assertEquals("{}", service.generateOTPForCareContext("{\"healthID\":\"abc@sbx\"}"));
        }
    }

    @Nested
    @DisplayName("validateOTPForCareContext")
    class ValidateOtpTests {

        @Test
        @DisplayName("should answer with the access token the ABDM callback carried")
        void validateOtp_shouldAnswerWithAccessToken() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"AccessToken\":\"otp-token\"}}");

            assertEquals("otp-token",
                    service.validateOTPForCareContext("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}"));

            JsonObject sent = sentPayload();
            assertEquals("txn-1", sent.get("transactionId").getAsString());
            assertEquals("123456", sent.getAsJsonObject("credential").get("authCode").getAsString());
        }

        @Test
        @DisplayName("should surface the ABDM error the callback carried")
        void validateOtp_shouldSurfaceCallbackError() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Error\":{\"Message\":\"wrong OTP\"}}");

            assertEquals("NDHM_FHIR wrong OTP", assertThrows(FHIRException.class,
                    () -> service.validateOTPForCareContext("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}"))
                    .getMessage());
        }

        @Test
        @DisplayName("should answer with no token when the ABDM callback never arrived")
        void validateOtp_shouldAnswerNullWhenCallbackNeverArrived() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("failure");

            assertEquals(null, service.validateOTPForCareContext("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}"));
        }

        @Test
        @DisplayName("should fail when ABDM rejects the verification outright")
        void validateOtp_shouldFailOnRejection() throws Exception {
            when(common_NDHMService.getStatusCode(any())).thenReturn("400 BAD_REQUEST");

            assertEquals("NDHM_FHIR Error while validating OTP", assertThrows(FHIRException.class,
                    () -> service.validateOTPForCareContext("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}"))
                    .getMessage());
        }
    }

    @Nested
    @DisplayName("addCareContext")
    class AddCareContextTests {

        private String request(String identifier) {
            return "{" + identifier + ",\"visitCode\":\"987654\",\"visitCategory\":\"General OPD\"}";
        }

        @Test
        @DisplayName("should link the visit as a care context of the ABHA address")
        void addCareContext_shouldLinkByAbhaAddress() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Acknowledgement\":{\"Status\":\"SUCCESS\"}}");

            assertEquals("Care Context added successfully",
                    service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token"));

            JsonObject link = sentPayload().getAsJsonObject("link");
            assertEquals("otp-token", link.get("accessToken").getAsString());
            JsonObject patient = link.getAsJsonObject("patient");
            assertEquals("abc@sbx", patient.get("referenceNumber").getAsString());
            assertEquals("Care Context of abc@sbx", patient.get("display").getAsString());
            JsonObject careContext = patient.getAsJsonArray("careContexts").get(0).getAsJsonObject();
            assertEquals("987654", careContext.get("referenceNumber").getAsString());
            assertEquals("General OPD", careContext.get("display").getAsString());
        }

        @Test
        @DisplayName("should fall back to the ABHA number when no ABHA address was given")
        void addCareContext_shouldLinkByAbhaNumber() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Acknowledgement\":{\"Status\":\"SUCCESS\"}}");

            service.addCareContext(request("\"healthIdNumber\":\"11-1111-1111-1111\""), "otp-token");

            assertEquals("Care Context of 11-1111-1111-1111", sentPayload().getAsJsonObject("link")
                    .getAsJsonObject("patient").get("display").getAsString());
        }

        @Test
        @DisplayName("should accept a plain status field as the acknowledgement")
        void addCareContext_shouldAcceptPlainStatus() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("{\"status\":\"SUCCESS\"}");

            assertEquals("Care Context added successfully",
                    service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token"));
        }

        @Test
        @DisplayName("should report the failure when ABDM acknowledges something other than success")
        void addCareContext_shouldReportUnsuccessfulAcknowledgement() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("{\"status\":\"REJECTED\"}");

            assertEquals("Failure", service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token"));
        }

        @Test
        @DisplayName("should surface the ABDM error the callback carried")
        void addCareContext_shouldSurfaceCallbackError() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Error\":{\"Message\":\"link token expired\"}}");

            assertEquals("NDHM_FHIR link token expired", assertThrows(FHIRException.class,
                    () -> service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token")).getMessage());
        }

        @Test
        @DisplayName("should refuse a callback payload it cannot make sense of")
        void addCareContext_shouldRefuseUnexpectedCallback() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("{\"somethingElse\":true}");

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token")).getMessage()
                    .startsWith("NDHM_FHIR Unexpected care context response: "));
        }

        @Test
        @DisplayName("should fail when the ABDM callback never arrived")
        void addCareContext_shouldFailWhenCallbackNeverArrived() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("failure");

            assertEquals("NDHM_FHIR Error while adding care context", assertThrows(FHIRException.class,
                    () -> service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token")).getMessage());
        }

        @Test
        @DisplayName("should fail when ABDM rejects the link outright")
        void addCareContext_shouldFailOnRejection() throws Exception {
            when(common_NDHMService.getStatusCode(any())).thenReturn("400 BAD_REQUEST");

            assertEquals("NDHM_FHIR Error while adding care context", assertThrows(FHIRException.class,
                    () -> service.addCareContext(request("\"healthID\":\"abc@sbx\""), "otp-token")).getMessage());
        }

        @Test
        @DisplayName("should insist on an ABHA address or number")
        void addCareContext_shouldInsistOnAbhaIdentifier() {
            assertEquals("Please pass ABHA/ABHA Number", assertThrows(FHIRException.class,
                    () -> service.addCareContext("{\"visitCode\":\"987654\"}", "otp-token")).getMessage());
        }
    }
}
