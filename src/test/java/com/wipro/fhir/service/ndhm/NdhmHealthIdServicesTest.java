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

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The ABHA-creation and ABHA-card flows against the ABDM gateway. Each one talks through
 * {@link HttpUtils}, so these tests stub the gateway answers and cover the three shapes
 * ABDM comes back in: a usable body, an empty body, and an HTTP error whose body carries
 * the ABDM problem details.
 */
@DisplayName("NDHM ABHA creation and card services Test Suite")
class NdhmHealthIdServicesTest {

    /** Builds the HTTP error ABDM raises, with the problem-detail body it carries. */
    private static HttpClientErrorException abdmError(String body) {
        return HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request", new HttpHeaders(),
                body == null ? null : body.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    private static final String DETAILS_WITH_ATTRIBUTE =
            "{\"details\":[{\"message\":\"invalid OTP\",\"attribute\":{\"key\":\"otp\"}}]}";
    private static final String DETAILS_WITHOUT_ATTRIBUTE = "{\"details\":[{\"message\":\"invalid OTP\"}]}";
    private static final String MESSAGE_ONLY = "{\"message\":\"gateway rejected the request\"}";

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("CreateHealthID_MobileOTP_NDHMServiceImpl")
    class MobileOtpTests {

        @Mock
        private HttpUtils httpUtils;

        @Mock
        private GenerateSession_NDHMService generateSession_NDHM;

        @Mock
        private Common_NDHMService common_NDHMService;

        @InjectMocks
        private CreateHealthID_MobileOTP_NDHMServiceImpl service;

        @BeforeEach
        void setUp() throws Exception {
            ReflectionTestUtils.setField(service, "ndhmGenerateOTP", "http://abdm.example.org/otp");
            ReflectionTestUtils.setField(service, "ndhmVerifyOTP", "http://abdm.example.org/verify");
            ReflectionTestUtils.setField(service, "ndhmCreateHealthID", "http://abdm.example.org/create");
            when(generateSession_NDHM.getNDHMAuthToken()).thenReturn("Bearer abdm-token");
            when(common_NDHMService.getHeaders(anyString())).thenReturn(new HttpHeaders());
        }

        private void stubGateway(String body) throws Exception {
            ResponseEntity<String> answer = new ResponseEntity<>(body, HttpStatus.OK);
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(answer);
            when(common_NDHMService.getBody(answer)).thenReturn(body);
        }

        @Test
        @DisplayName("generateOTP should answer with the mobile and the transaction id ABDM issued")
        void generateOTP_shouldAnswerWithMobileAndTxnId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            JsonObject result = JsonParser.parseString(service.generateOTP("{\"mobile\":\"9999999999\"}"))
                    .getAsJsonObject();

            assertEquals("9999999999", result.get("mobile").getAsString());
            assertEquals("txn-1", result.get("txnId").getAsString());
        }

        @Test
        @DisplayName("generateOTP should fail when ABDM answers with an empty body")
        void generateOTP_shouldFailOnEmptyBody() throws Exception {
            stubGateway(null);

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"mobile\":\"9999999999\"}")).getMessage()
                    .contains("Error while accessing generate OTP API"));
        }

        @Test
        @DisplayName("generateOTP should surface the ABDM problem detail with its offending attribute")
        void generateOTP_shouldSurfaceProblemDetailWithAttribute() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITH_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while creating ABHA invalid OTP :otp", assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"mobile\":\"9999999999\"}")).getMessage());
        }

        @Test
        @DisplayName("generateOTP should surface a problem detail that names no attribute")
        void generateOTP_shouldSurfaceProblemDetailWithoutAttribute() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITHOUT_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while creating ABHA invalid OTP", assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"mobile\":\"9999999999\"}")).getMessage());
        }

        @Test
        @DisplayName("generateOTP should fall back to the ABDM message when there is no problem detail")
        void generateOTP_shouldFallBackToAbdmMessage() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(MESSAGE_ONLY));

            assertEquals("NDHM_FHIR Error while creating ABHA gateway rejected the request",
                    assertThrows(FHIRException.class,
                            () -> service.generateOTP("{\"mobile\":\"9999999999\"}")).getMessage());
        }

        @Test
        @DisplayName("validateOTP should answer with the OTP token ABDM issued")
        void validateOTP_shouldAnswerWithToken() throws Exception {
            stubGateway("{\"token\":\"otp-token\"}");

            assertEquals("otp-token", service.validateOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}"));

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(httpUtils).postWithResponseEntity(eq("http://abdm.example.org/verify"), captor.capture(),
                    any(HttpHeaders.class));
            JsonObject sent = JsonParser.parseString(captor.getValue()).getAsJsonObject();
            assertEquals("123456", sent.get("otp").getAsString());
            assertEquals("txn-1", sent.get("txnId").getAsString());
        }

        @Test
        @DisplayName("validateOTP should fail when ABDM answers with an empty body")
        void validateOTP_shouldFailOnEmptyBody() throws Exception {
            stubGateway(null);

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.validateOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}")).getMessage()
                    .contains("Error while accessing verify OTP API"));
        }

        @Test
        @DisplayName("validateOTP should surface the ABDM problem detail")
        void validateOTP_shouldSurfaceProblemDetail() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITH_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while creating ABHA invalid OTP :otp", assertThrows(FHIRException.class,
                    () -> service.validateOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}")).getMessage());
        }

        @Test
        @DisplayName("createHealthID should send the OTP token and map the created ABHA back")
        void createHealthID_shouldMapCreatedAbha() throws Exception {
            stubGateway("{\"healthIdNumber\":\"11-1111-1111-1111\",\"healthId\":\"abc@sbx\"}");

            HealthIDResponse created = service.createHealthID("{\"txnId\":\"txn-1\"}", "otp-token");

            assertEquals("11-1111-1111-1111", created.getHealthIdNumber());
            assertEquals("abc@sbx", created.getHealthId());
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(httpUtils).postWithResponseEntity(eq("http://abdm.example.org/create"), captor.capture(),
                    any(HttpHeaders.class));
            assertEquals("otp-token",
                    JsonParser.parseString(captor.getValue()).getAsJsonObject().get("token").getAsString());
        }

        @Test
        @DisplayName("createHealthID should answer with a blank ABHA when ABDM sends an empty body")
        void createHealthID_shouldAnswerBlankOnEmptyBody() throws Exception {
            stubGateway(null);

            assertNotNull(service.createHealthID("{\"txnId\":\"txn-1\"}", "otp-token"));
        }

        @Test
        @DisplayName("createHealthID should surface the ABDM problem detail")
        void createHealthID_shouldSurfaceProblemDetail() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITHOUT_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while creating ABHA invalid OTP", assertThrows(FHIRException.class,
                    () -> service.createHealthID("{\"txnId\":\"txn-1\"}", "otp-token")).getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("GenerateHealthID_CardServiceImpl")
    class HealthCardTests {

        @Mock
        private HttpUtils httpUtils;

        @Mock
        private GenerateSession_NDHMService generateSession_NDHM;

        @Mock
        private Common_NDHMService common_NDHMService;

        @InjectMocks
        private GenerateHealthID_CardServiceImpl service;

        @BeforeEach
        void setUp() throws Exception {
            ReflectionTestUtils.setField(service, "generateOTP_ForCard", "http://abdm.example.org/card/otp");
            ReflectionTestUtils.setField(service, "verifyOTP_ForCard", "http://abdm.example.org/card/verify/mobile");
            ReflectionTestUtils.setField(service, "verifyOTP_ForCard_Aadhaar",
                    "http://abdm.example.org/card/verify/aadhaar");
            ReflectionTestUtils.setField(service, "generateHealthCard", "http://abdm.example.org/card/png");
            ReflectionTestUtils.setField(service, "generateHealthIDCard", "http://abdm.example.org/card/bio/png");
            when(generateSession_NDHM.getNDHMAuthToken()).thenReturn("Bearer abdm-token");
            when(common_NDHMService.getHeaders(anyString())).thenReturn(new HttpHeaders());
            when(common_NDHMService.getHeadersWithXtoken(anyString(), anyString())).thenReturn(new HttpHeaders());
            when(common_NDHMService.getHeadersWithAadhaarBioXtoken(anyString(), anyString()))
                    .thenReturn(new HttpHeaders());
        }

        private void stubGateway(String body) throws Exception {
            ResponseEntity<String> answer = new ResponseEntity<>(body, HttpStatus.OK);
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(answer);
            when(common_NDHMService.getBody(answer)).thenReturn(body);
        }

        @Test
        @DisplayName("generateOTP should ask ABDM for a card OTP against the ABHA address")
        void generateOTP_shouldUseHealthId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            JsonObject result = JsonParser.parseString(service.generateOTP(
                    "{\"authMethod\":\"MOBILE_OTP\",\"healthid\":\"abc@sbx\"}")).getAsJsonObject();

            assertEquals("txn-1", result.get("txnId").getAsString());
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(httpUtils).postWithResponseEntity(anyString(), captor.capture(), any(HttpHeaders.class));
            JsonObject sent = JsonParser.parseString(captor.getValue()).getAsJsonObject();
            assertEquals("abc@sbx", sent.get("healthid").getAsString());
            assertEquals("MOBILE_OTP", sent.get("authMethod").getAsString());
        }

        @Test
        @DisplayName("generateOTP should fall back to the ABHA number when no address was given")
        void generateOTP_shouldFallBackToHealthIdNumber() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            service.generateOTP("{\"authMethod\":\"MOBILE_OTP\",\"healthIdNumber\":\"11-1111-1111-1111\"}");

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(httpUtils).postWithResponseEntity(anyString(), captor.capture(), any(HttpHeaders.class));
            assertEquals("11-1111-1111-1111",
                    JsonParser.parseString(captor.getValue()).getAsJsonObject().get("healthid").getAsString());
        }

        @Test
        @DisplayName("generateOTP should insist on an ABHA address or number")
        void generateOTP_shouldInsistOnAbhaIdentifier() {
            assertTrue(assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"authMethod\":\"MOBILE_OTP\"}")).getMessage()
                    .endsWith("Please pass ABHA/ABHA Number"));
        }

        @Test
        @DisplayName("generateOTP should fail when ABDM answers with an empty body")
        void generateOTP_shouldFailOnEmptyBody() throws Exception {
            stubGateway(null);

            assertTrue(assertThrows(FHIRException.class, () -> service.generateOTP(
                    "{\"authMethod\":\"MOBILE_OTP\",\"healthid\":\"abc@sbx\"}")).getMessage()
                    .contains("generate OTP"));
        }

        @Test
        @DisplayName("generateOTP should surface the ABDM problem detail with its offending attribute")
        void generateOTP_shouldSurfaceProblemDetail() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITH_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while generating OTP for ABHA card invalid OTP :otp",
                    assertThrows(FHIRException.class, () -> service.generateOTP(
                            "{\"authMethod\":\"MOBILE_OTP\",\"healthid\":\"abc@sbx\"}")).getMessage());
        }

        @Test
        @DisplayName("validateOTP should verify a mobile OTP against the mobile endpoint")
        void validateOTP_shouldVerifyMobileOtp() throws Exception {
            stubGateway("{\"token\":\"x-token-1\"}");

            assertEquals("Bearer x-token-1", service.validateOTP(
                    "{\"otp\":\"123456\",\"txnId\":\"txn-1\",\"authMethod\":\"MOBILE_OTP\"}"));

            verify(httpUtils).postWithResponseEntity(eq("http://abdm.example.org/card/verify/mobile"), anyString(),
                    any(HttpHeaders.class));
        }

        @Test
        @DisplayName("validateOTP should verify an Aadhaar OTP against the Aadhaar endpoint")
        void validateOTP_shouldVerifyAadhaarOtp() throws Exception {
            stubGateway("{\"token\":\"x-token-1\"}");

            service.validateOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\",\"authMethod\":\"AADHAAR_OTP\"}");

            verify(httpUtils).postWithResponseEntity(eq("http://abdm.example.org/card/verify/aadhaar"), anyString(),
                    any(HttpHeaders.class));
        }

        @Test
        @DisplayName("validateOTP should refuse an authentication method ABDM cannot mint a card for")
        void validateOTP_shouldRefuseUnsupportedAuthMethod() {
            assertTrue(assertThrows(FHIRException.class, () -> service.validateOTP(
                    "{\"otp\":\"123456\",\"txnId\":\"txn-1\",\"authMethod\":\"AADHAAR_BIO\"}")).getMessage()
                    .contains("Currently this facility is not available"));
        }

        @Test
        @DisplayName("validateOTP should insist on an authentication method")
        void validateOTP_shouldInsistOnAuthMethod() {
            assertTrue(assertThrows(FHIRException.class,
                    () -> service.validateOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}")).getMessage()
                    .endsWith("Please pass correct Authentication method : MOBILE_OTP / AADHAAR_OTP"));
        }

        @Test
        @DisplayName("validateOTP should fail when ABDM answers with an empty body")
        void validateOTP_shouldFailOnEmptyBody() throws Exception {
            stubGateway(null);

            assertTrue(assertThrows(FHIRException.class, () -> service.validateOTP(
                    "{\"otp\":\"123456\",\"txnId\":\"txn-1\",\"authMethod\":\"MOBILE_OTP\"}")).getMessage()
                    .contains("verify OTP"));
        }

        @Test
        @DisplayName("validateOTP should surface the ABDM problem detail")
        void validateOTP_shouldSurfaceProblemDetail() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(MESSAGE_ONLY));

            assertTrue(assertThrows(FHIRException.class, () -> service.validateOTP(
                    "{\"otp\":\"123456\",\"txnId\":\"txn-1\",\"authMethod\":\"MOBILE_OTP\"}")).getMessage()
                    .contains("gateway rejected the request"));
        }

        @Test
        @DisplayName("generateCard should answer with the card ABDM returned, base64 encoded")
        void generateCard_shouldAnswerWithEncodedCard() throws Exception {
            byte[] png = new byte[] { 1, 2, 3, 4 };
            when(httpUtils.getWithResponseEntityByte(eq("http://abdm.example.org/card/png"), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>(png, HttpStatus.OK));

            assertEquals(Base64.getEncoder().encodeToString(png),
                    service.generateCard("{}", "Bearer x-token-1"));
        }

        @Test
        @DisplayName("generateCard should fail when ABDM returns no card bytes")
        void generateCard_shouldFailOnEmptyCard() {
            when(httpUtils.getWithResponseEntityByte(anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>(HttpStatus.OK));

            assertTrue(assertThrows(FHIRException.class, () -> service.generateCard("{}", "Bearer x-token-1"))
                    .getMessage().contains("generate card API"));
        }

        @Test
        @DisplayName("generateCard should surface the ABDM problem detail")
        void generateCard_shouldSurfaceProblemDetail() {
            when(httpUtils.getWithResponseEntityByte(anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITH_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while generating ABHA card : invalid OTP :otp",
                    assertThrows(FHIRException.class, () -> service.generateCard("{}", "Bearer x-token-1"))
                            .getMessage());
        }

        @Test
        @DisplayName("generateHealthCardForBio should answer with the card the biometric flow returned")
        void generateHealthCardForBio_shouldAnswerWithEncodedCard() throws Exception {
            byte[] png = new byte[] { 9, 8, 7 };
            when(httpUtils.getWithResponseEntityByte(eq("http://abdm.example.org/card/bio/png"),
                    any(HttpHeaders.class))).thenReturn(new ResponseEntity<>(png, HttpStatus.OK));

            assertEquals(Base64.getEncoder().encodeToString(png),
                    service.generateHealthCardForBio("{}", "x-token-1"));
        }

        @Test
        @DisplayName("generateHealthCardForBio should fail when ABDM returns no card bytes")
        void generateHealthCardForBio_shouldFailOnEmptyCard() {
            when(httpUtils.getWithResponseEntityByte(anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>(HttpStatus.OK));

            assertThrows(FHIRException.class, () -> service.generateHealthCardForBio("{}", "x-token-1"));
        }

        @Test
        @DisplayName("generateHealthCardForBio should surface the ABDM problem detail")
        void generateHealthCardForBio_shouldSurfaceProblemDetail() {
            when(httpUtils.getWithResponseEntityByte(anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError(DETAILS_WITHOUT_ATTRIBUTE));

            assertEquals("NDHM_FHIR Error while generating ABHA card : invalid OTP",
                    assertThrows(FHIRException.class, () -> service.generateHealthCardForBio("{}", "x-token-1"))
                            .getMessage());
        }
    }
}
