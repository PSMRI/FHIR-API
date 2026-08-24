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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.mongo.care_context.GenerateTokenAbdmResponses;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.NDHMResponse;
import com.wipro.fhir.repo.mongo.generateToken_response.GenerateTokenAbdmResponsesRepo;
import com.wipro.fhir.repo.mongo.ndhm_response.NDHMResponseRepo;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The shared NDHM gateway plumbing: the header and correlation-id helpers, the ABDM
 * session token, and the OTP-based ABHA validation flow that reads its answer back out of
 * the Mongo collection the ABDM callback writes into.
 */
@DisplayName("NDHM gateway shared services Test Suite")
class NdhmGatewayCommonServicesTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Common_NDHMServiceImpl")
    class CommonNdhmTests {

        @Mock
        private NDHMResponseRepo nDHMResponseRepo;

        @Mock
        private GenerateTokenAbdmResponsesRepo generateTokenAbdmResponsesRepo;

        @InjectMocks
        private Common_NDHMServiceImpl service;

        @Test
        @DisplayName("getHeaders should send JSON and the ABDM session token when there is one")
        void getHeaders_shouldSendJsonAndToken() {
            HttpHeaders headers = service.getHeaders("Bearer abdm-token");

            assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
            assertEquals("Bearer abdm-token", headers.getFirst("Authorization"));
        }

        @Test
        @DisplayName("getHeaders should omit the Authorization header when there is no token")
        void getHeaders_shouldOmitAbsentToken() {
            assertNull(service.getHeaders(null).getFirst("Authorization"));
        }

        @Test
        @DisplayName("getHeaders should add the consent-manager id for the two-argument form")
        void getHeaders_shouldAddConsentManagerId() {
            HttpHeaders headers = service.getHeaders("Bearer abdm-token", "sbx");

            assertEquals("sbx", headers.getFirst("X-CM-ID"));
            assertEquals("Bearer abdm-token", headers.getFirst("Authorization"));
            assertNull(service.getHeaders(null, "sbx").getFirst("Authorization"));
        }

        @Test
        @DisplayName("getHeadersWithXtoken should pass the X-token through verbatim")
        void getHeadersWithXtoken_shouldPassTokenVerbatim() {
            HttpHeaders headers = service.getHeadersWithXtoken("Bearer abdm-token", "x-token-1");

            assertEquals("x-token-1", headers.getFirst("X-Token"));
            assertNull(service.getHeadersWithXtoken(null, "x-token-1").getFirst("Authorization"));
        }

        @Test
        @DisplayName("getHeadersWithAadhaarBioXtoken should send the X-token as a bearer token")
        void getHeadersWithAadhaarBioXtoken_shouldSendBearerToken() {
            HttpHeaders headers = service.getHeadersWithAadhaarBioXtoken("Bearer abdm-token", "x-token-1");

            assertEquals("Bearer x-token-1", headers.getFirst("X-Token"));
            assertNull(service.getHeadersWithAadhaarBioXtoken(null, "x-token-1").getFirst("Authorization"));
        }

        @Test
        @DisplayName("getRequestIDAndTimeStamp should stamp a UTC timestamp and a fresh request id")
        void getRequestIDAndTimeStamp_shouldStampRequest() {
            NDHMRequest first = service.getRequestIDAndTimeStamp();
            NDHMRequest second = service.getRequestIDAndTimeStamp();

            assertNotNull(first.getRequestId());
            assertTrue(first.getTimestamp().endsWith("Z"), first.getTimestamp());
            assertTrue(!first.getRequestId().equals(second.getRequestId()),
                    "each ABDM call must carry its own correlation id");
        }

        @Test
        @DisplayName("getMongoNDHMResponse should answer with the callback payload the gateway stored")
        void getMongoNDHMResponse_shouldAnswerWithStoredPayload() throws Exception {
            NDHMResponse stored = new NDHMResponse();
            stored.setResponseData("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");
            when(nDHMResponseRepo.findByRequestID("req-1")).thenReturn(stored);

            assertEquals("{\"Auth\":{\"TransactionId\":\"txn-1\"}}", service.getMongoNDHMResponse("req-1"));
        }

        @Test
        @DisplayName("getResponseMongo should answer with nothing when the callback never arrived")
        void getResponseMongo_shouldAnswerNullWithoutPayload() {
            when(nDHMResponseRepo.findByRequestID("req-1")).thenReturn(null);

            assertNull(service.getResponseMongo("req-1"));
        }

        @Test
        @DisplayName("getLinkToken should answer with the stored link-token document")
        void getLinkToken_shouldAnswerWithStoredDocument() throws Exception {
            GenerateTokenAbdmResponses stored = new GenerateTokenAbdmResponses();
            stored.setResponse("{\"LinkToken\":\"link-1\"}");
            when(generateTokenAbdmResponsesRepo.findByRequestId("req-1")).thenReturn(stored);

            assertEquals("{\"LinkToken\":\"link-1\"}", service.getLinkToken("req-1").getResponse());
        }

        @Test
        @DisplayName("getLinkToken should answer with nothing when the callback never arrived")
        void getLinkToken_shouldAnswerNullWithoutDocument() throws Exception {
            when(generateTokenAbdmResponsesRepo.findByRequestId("req-1")).thenReturn(null);

            assertNull(service.getLinkToken("req-1"));
        }

        @Test
        @DisplayName("getBody should unwrap the response body, and refuse an absent response")
        void getBody_shouldUnwrapBody() throws Exception {
            assertEquals("payload", service.getBody(new ResponseEntity<>("payload", HttpStatus.OK)));
            assertEquals("NDHM_FHIR Null response returned from API",
                    assertThrows(FHIRException.class, () -> service.getBody(null)).getMessage());
        }

        @Test
        @DisplayName("getStatusCode should read the status, and refuse an absent response")
        void getStatusCode_shouldReadStatus() throws Exception {
            assertEquals("202 ACCEPTED", service.getStatusCode(new ResponseEntity<>("", HttpStatus.ACCEPTED)));
            assertEquals("NDHM_FHIR Null response returned from API",
                    assertThrows(FHIRException.class, () -> service.getStatusCode(null)).getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("GenerateSession_NDHMServiceImpl")
    class GenerateSessionTests {

        @Mock
        private HttpUtils httpUtils;

        @Mock
        private Common_NDHMService common_NDHMService;

        @InjectMocks
        private GenerateSession_NDHMServiceImpl service;

        @BeforeEach
        @DisplayName("Configure the client credentials and clear the cached token")
        void setUp() {
            ReflectionTestUtils.setField(service, "clientID", "client-1");
            ReflectionTestUtils.setField(service, "clientSecret", "secret-1");
            ReflectionTestUtils.setField(service, "ndhmUserAuthenticate", "http://abdm.example.org/sessions");
            ReflectionTestUtils.setField(GenerateSession_NDHMServiceImpl.class, "NDHM_AUTH_TOKEN", null);
            ReflectionTestUtils.setField(GenerateSession_NDHMServiceImpl.class, "NDHM_TOKEN_EXP", null);
            when(common_NDHMService.getHeaders(isNull())).thenReturn(new HttpHeaders());
        }

        private void stubAbdmSession(String body) throws Exception {
            ResponseEntity<String> answer = new ResponseEntity<>(body, HttpStatus.OK);
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(answer);
            when(common_NDHMService.getBody(answer)).thenReturn(body);
        }

        @Test
        @DisplayName("generateNDHMAuthToken should mint a bearer token from the ABDM access token")
        void generateNDHMAuthToken_shouldMintBearerToken() throws Exception {
            stubAbdmSession("{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            assertEquals("success", service.generateNDHMAuthToken());
            assertEquals("Bearer abdm-access",
                    ReflectionTestUtils.getField(GenerateSession_NDHMServiceImpl.class, "NDHM_AUTH_TOKEN"));
        }

        @Test
        @DisplayName("generateNDHMAuthToken should send the configured client credentials")
        void generateNDHMAuthToken_shouldSendClientCredentials() throws Exception {
            stubAbdmSession("{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            service.generateNDHMAuthToken();

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(httpUtils).postWithResponseEntity(eq("http://abdm.example.org/sessions"), captor.capture(),
                    any(HttpHeaders.class));
            JsonObject sent = JsonParser.parseString(captor.getValue()).getAsJsonObject();
            assertEquals("client-1", sent.get("clientId").getAsString());
            assertEquals("secret-1", sent.get("clientSecret").getAsString());
        }

        @Test
        @DisplayName("generateNDHMAuthToken should wrap an empty ABDM answer")
        void generateNDHMAuthToken_shouldWrapEmptyAnswer() throws Exception {
            ResponseEntity<String> answer = new ResponseEntity<>(HttpStatus.OK);
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(answer);
            when(common_NDHMService.getBody(answer)).thenReturn(null);

            assertTrue(assertThrows(FHIRException.class, () -> service.generateNDHMAuthToken()).getMessage()
                    .startsWith("NDHM_FHIR Error while accessing authenticate API"));
        }

        @Test
        @DisplayName("generateNDHMAuthToken should wrap an ABDM transport failure")
        void generateNDHMAuthToken_shouldWrapTransportFailure() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertThrows(FHIRException.class, () -> service.generateNDHMAuthToken());
        }

        @Test
        @DisplayName("getNDHMAuthToken should mint a token on first use and reuse it while it is valid")
        void getNDHMAuthToken_shouldMintOnceAndReuse() throws Exception {
            stubAbdmSession("{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            assertEquals("Bearer abdm-access", service.getNDHMAuthToken());
            assertEquals("Bearer abdm-access", service.getNDHMAuthToken());

            verify(httpUtils, org.mockito.Mockito.times(1))
                    .postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("getNDHMAuthToken should mint a fresh token once the cached one expired")
        void getNDHMAuthToken_shouldRefreshExpiredToken() throws Exception {
            stubAbdmSession("{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");
            ReflectionTestUtils.setField(GenerateSession_NDHMServiceImpl.class, "NDHM_AUTH_TOKEN", "Bearer stale");
            ReflectionTestUtils.setField(GenerateSession_NDHMServiceImpl.class, "NDHM_TOKEN_EXP", 1L);

            assertEquals("Bearer abdm-access", service.getNDHMAuthToken());
        }

        @Test
        @DisplayName("getNDHMAuthToken should wrap a failed session call")
        void getNDHMAuthToken_shouldWrapSessionFailure() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertThrows(FHIRException.class, () -> service.getNDHMAuthToken());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("ValidateHealthID_NDHMServiceImpl")
    class ValidateHealthIdTests {

        @Mock
        private HttpUtils httpUtils;

        @Mock
        private Common_NDHMService common_NDHMService;

        @Mock
        private GenerateSession_NDHMService generateSession_NDHM;

        @InjectMocks
        private ValidateHealthID_NDHMServiceImpl service;

        @BeforeEach
        @DisplayName("Configure the ABDM endpoints and stub the shared plumbing")
        void setUp() throws Exception {
            ReflectionTestUtils.setField(service, "clientID", "client-1");
            ReflectionTestUtils.setField(service, "clientSecret", "secret-1");
            ReflectionTestUtils.setField(service, "generateOTPHealthIDValidation", "http://abdm.example.org/otp");
            ReflectionTestUtils.setField(service, "validateOTPHealthIDValidation", "http://abdm.example.org/verify");
            ReflectionTestUtils.setField(service, "addCareContext", "http://abdm.example.org/carecontext");
            ReflectionTestUtils.setField(service, "abhaMode", "sbx");

            when(generateSession_NDHM.getNDHMAuthToken()).thenReturn("Bearer abdm-token");
            NDHMRequest request = new NDHMRequest();
            request.setRequestId("req-1");
            request.setTimestamp("2026-08-24T10:00:00.000Z");
            when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(request);
            when(common_NDHMService.getHeaders(anyString(), anyString())).thenReturn(new HttpHeaders());
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>("", HttpStatus.ACCEPTED));
            when(common_NDHMService.getStatusCode(any())).thenReturn("202");
        }

        private String sentPayload() {
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(httpUtils).postWithResponseEntity(anyString(), captor.capture(), any(HttpHeaders.class));
            return captor.getValue();
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should answer with the transaction id ABDM called back with")
        void generateOtp_shouldAnswerWithTransactionId() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            String result = service.generateOTPForHealthIDValidation(
                    "{\"healthID\":\"abc@sbx\",\"authenticationMode\":\"MOBILE\"}");

            assertEquals("txn-1", JsonParser.parseString(result).getAsJsonObject().get("txnId").getAsString());
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should ask for an Aadhaar OTP when that is the mode")
        void generateOtp_shouldAskForAadhaarOtp() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForHealthIDValidation(
                    "{\"healthID\":\"abc@sbx\",\"authenticationMode\":\"AADHAR\"}");

            JsonObject query = JsonParser.parseString(sentPayload()).getAsJsonObject().getAsJsonObject("query");
            assertEquals("AADHAAR_OTP", query.get("authMode").getAsString());
            assertEquals("KYC", query.get("purpose").getAsString());
            assertEquals("abc@sbx", query.get("id").getAsString());
            assertEquals("HIP", query.getAsJsonObject("requester").get("type").getAsString());
            assertEquals("client-1", query.getAsJsonObject("requester").get("id").getAsString());
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should ask for a mobile OTP for any other mode")
        void generateOtp_shouldAskForMobileOtp() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForHealthIDValidation("{\"healthID\":\"abc@sbx\"}");

            assertEquals("MOBILE_OTP", JsonParser.parseString(sentPayload()).getAsJsonObject()
                    .getAsJsonObject("query").get("authMode").getAsString());
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should fall back to the sandbox consent manager")
        void generateOtp_shouldFallBackToSandboxMode() throws Exception {
            ReflectionTestUtils.setField(service, "abhaMode", "staging");
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"TransactionId\":\"txn-1\"}}");

            service.generateOTPForHealthIDValidation("{\"healthID\":\"abc@sbx\"}");

            verify(common_NDHMService).getHeaders("Bearer abdm-token", "sbx");
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should surface the ABDM error the callback carried")
        void generateOtp_shouldSurfaceCallbackError() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Error\":{\"Message\":\"ABHA not found\"}}");

            assertEquals("ABHA not found", assertThrows(FHIRException.class,
                    () -> service.generateOTPForHealthIDValidation("{\"healthID\":\"abc@sbx\"}")).getMessage());
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should answer with an empty map when ABDM never called back")
        void generateOtp_shouldAnswerEmptyWhenCallbackNeverArrived() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1")).thenReturn("failure");

            assertEquals("{}", service.generateOTPForHealthIDValidation("{\"healthID\":\"abc@sbx\"}"));
        }

        @Test
        @DisplayName("generateOTPForHealthIDValidation should answer with an empty map when ABDM rejects the request")
        void generateOtp_shouldAnswerEmptyOnRejection() throws Exception {
            when(common_NDHMService.getStatusCode(any())).thenReturn("400");

            assertEquals("{}", service.generateOTPForHealthIDValidation("{\"healthID\":\"abc@sbx\"}"));
        }

        @Test
        @DisplayName("validateOTPForHealthIDValidation should answer with the patient ABDM called back with")
        void validateOtp_shouldAnswerWithPatient() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Auth\":{\"Patient\":{\"healthIdNumber\":\"11-1111-1111-1111\"}}}");

            String result = service.validateOTPForHealthIDValidation("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}");

            assertNotNull(result);
            JsonObject sent = JsonParser.parseString(sentPayload()).getAsJsonObject();
            assertEquals("txn-1", sent.get("transactionId").getAsString());
            assertEquals("123456", sent.getAsJsonObject("credential").get("authCode").getAsString());
        }

        @Test
        @DisplayName("validateOTPForHealthIDValidation should surface the ABDM error the callback carried")
        void validateOtp_shouldSurfaceCallbackError() throws Exception {
            when(common_NDHMService.getMongoNDHMResponse("req-1"))
                    .thenReturn("{\"Error\":{\"Message\":\"wrong OTP\"}}");

            assertEquals("NDHM_FHIR wrong OTP", assertThrows(FHIRException.class,
                    () -> service.validateOTPForHealthIDValidation("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}"))
                    .getMessage());
        }

        @Test
        @DisplayName("validateOTPForHealthIDValidation should report no ABHA when ABDM rejects the request")
        void validateOtp_shouldReportNoAbhaOnRejection() throws Exception {
            when(common_NDHMService.getStatusCode(any())).thenReturn("400");

            assertEquals("NDHM_FHIR No ABHA found", assertThrows(FHIRException.class,
                    () -> service.validateOTPForHealthIDValidation("{\"txnId\":\"txn-1\",\"otp\":\"123456\"}"))
                    .getMessage());
        }
    }
}
