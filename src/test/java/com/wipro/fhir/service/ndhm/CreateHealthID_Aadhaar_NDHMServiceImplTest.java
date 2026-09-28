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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
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
 * The Aadhaar-based ABHA creation flow: the Aadhaar OTP, the mobile-linking steps, the
 * biometric verification, and the ABHA the gateway finally hands back.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CreateHealthID_Aadhaar_NDHMServiceImpl Test Suite")
class CreateHealthID_Aadhaar_NDHMServiceImplTest {

    private static final String AADHAAR_OTP_URL = "http://abdm.example.org/aadhaar/otp";
    private static final String CREATE_URL = "http://abdm.example.org/aadhaar/create";
    private static final String VERIFY_OTP_URL = "http://abdm.example.org/aadhaar/verify";
    private static final String CHECK_MOBILE_URL = "http://abdm.example.org/mobile/check";
    private static final String VERIFY_MOBILE_URL = "http://abdm.example.org/mobile/verify";
    private static final String VERIFY_BIO_URL = "http://abdm.example.org/bio/verify";
    private static final String GENERATE_MOBILE_OTP_URL = "http://abdm.example.org/mobile/otp";
    private static final String CONFIRM_BIO_URL = "http://abdm.example.org/bio/confirm";

    @Mock
    private HttpUtils httpUtils;

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private GenerateSession_NDHMService generateSession_NDHM;

    @Mock
    private GenerateHealthID_CardService generateHealthID_CardService;

    @InjectMocks
    private CreateHealthID_Aadhaar_NDHMServiceImpl service;

    @BeforeEach
    @DisplayName("Configure the ABDM endpoints and the shared session plumbing")
    void setUp() throws Exception {
        ReflectionTestUtils.setField(service, "ndhmGenerateOTPWithAadhaar", AADHAAR_OTP_URL);
        ReflectionTestUtils.setField(service, "abdmcreateHealthIdWithPreVerified", CREATE_URL);
        ReflectionTestUtils.setField(service, "abdmVerifyOTP", VERIFY_OTP_URL);
        ReflectionTestUtils.setField(service, "abdmCheckAndGenerateMobileOTP", CHECK_MOBILE_URL);
        ReflectionTestUtils.setField(service, "abdmVerifyMobileOTP", VERIFY_MOBILE_URL);
        ReflectionTestUtils.setField(service, "abdmVerifyBio", VERIFY_BIO_URL);
        ReflectionTestUtils.setField(service, "abdmGenerateMobileOTP", GENERATE_MOBILE_OTP_URL);
        ReflectionTestUtils.setField(service, "abdmConfirmAadhaarBio", CONFIRM_BIO_URL);

        when(generateSession_NDHM.getNDHMAuthToken()).thenReturn("Bearer abdm-token");
        when(common_NDHMService.getHeaders(anyString())).thenReturn(new HttpHeaders());
    }

    /** Stubs the ABDM answer for the plain post-with-body form. */
    private void stubGateway(String body, HttpStatus status) throws Exception {
        ResponseEntity<String> answer = new ResponseEntity<>(body, status);
        when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                .thenReturn(answer);
        when(common_NDHMService.getBody(answer)).thenReturn(body);
    }

    private void stubGateway(String body) throws Exception {
        stubGateway(body, HttpStatus.OK);
    }

    private static HttpClientErrorException abdmError(String body) {
        return HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request", new HttpHeaders(),
                body.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    private JsonObject answerOf(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    @Nested
    @DisplayName("generateOTP")
    class GenerateOtpTests {

        @Test
        @DisplayName("should answer with the Aadhaar and the transaction id ABDM issued")
        void generateOTP_shouldAnswerWithAadhaarAndTxnId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            JsonObject result = answerOf(service.generateOTP("{\"aadhaar\":\"999999999999\"}"));

            assertEquals("999999999999", result.get("aadhaar").getAsString());
            assertEquals("txn-1", result.get("txnId").getAsString());
            verify(httpUtils).postWithResponseEntity(eq(AADHAAR_OTP_URL), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("should report the failure plainly when ABDM answers with an empty body")
        void generateOTP_shouldReportEmptyBody() throws Exception {
            stubGateway(null);

            assertEquals("NDHM_FHIR Error while accessing generate OTP with Aadhaar API",
                    service.generateOTP("{\"aadhaar\":\"999999999999\"}"));
        }

        @Test
        @DisplayName("should surface the ABDM problem detail with its offending attribute")
        void generateOTP_shouldSurfaceProblemDetailWithAttribute() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError("{\"details\":[{\"message\":\"invalid aadhaar\","
                            + "\"attribute\":{\"key\":\"aadhaar\"}}]}"));

            assertEquals("NDHM_FHIR Error while generating OTP for ABHA creationinvalid aadhaar :aadhaar",
                    assertThrows(FHIRException.class,
                            () -> service.generateOTP("{\"aadhaar\":\"999999999999\"}")).getMessage());
        }

        @Test
        @DisplayName("should surface a problem detail that names no attribute")
        void generateOTP_shouldSurfaceProblemDetailWithoutAttribute() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError("{\"details\":[{\"message\":\"invalid aadhaar\"}]}"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"aadhaar\":\"999999999999\"}")).getMessage()
                    .endsWith("invalid aadhaar"));
        }

        @Test
        @DisplayName("should fall back to the ABDM message when there is no problem detail")
        void generateOTP_shouldFallBackToAbdmMessage() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError("{\"message\":\"gateway rejected the request\"}"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"aadhaar\":\"999999999999\"}")).getMessage()
                    .endsWith("gateway rejected the request"));
        }

        @Test
        @DisplayName("should wrap any other transport failure")
        void generateOTP_shouldWrapTransportFailure() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.generateOTP("{\"aadhaar\":\"999999999999\"}")).getMessage()
                    .startsWith("NDHM_FHIR Error while accessing generate OTP with Aadhaar API"));
        }
    }

    @Nested
    @DisplayName("The OTP and mobile-linking steps")
    class OtpAndMobileStepTests {

        @Test
        @DisplayName("verifyOTP should answer with the transaction id ABDM confirmed")
        void verifyOTP_shouldAnswerWithTxnId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            assertEquals("txn-1", answerOf(service.verifyOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}"))
                    .get("txnId").getAsString());
            verify(httpUtils).postWithResponseEntity(eq(VERIFY_OTP_URL), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("verifyOTP should fail when ABDM does not answer 200")
        void verifyOTP_shouldFailOnNonOkStatus() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}", HttpStatus.ACCEPTED);

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.verifyOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}")).getMessage()
                    .contains("Error while verifying the OTP"));
        }

        @Test
        @DisplayName("checkAndGenerateMobileOTP should report whether the mobile is already linked")
        void checkAndGenerateMobileOTP_shouldReportMobileLinked() throws Exception {
            stubGateway("{\"mobileLinked\":\"true\",\"txnId\":\"txn-1\"}");

            JsonObject result = answerOf(service.checkAndGenerateMobileOTP(
                    "{\"mobile\":\"9999999999\",\"txnId\":\"txn-1\"}"));

            assertEquals("true", result.get("mobileLinked").getAsString());
            assertEquals("txn-1", result.get("txnId").getAsString());
            verify(httpUtils).postWithResponseEntity(eq(CHECK_MOBILE_URL), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("checkAndGenerateMobileOTP should fail when ABDM does not answer 200")
        void checkAndGenerateMobileOTP_shouldFailOnNonOkStatus() throws Exception {
            stubGateway("{}", HttpStatus.BAD_REQUEST);

            assertThrows(FHIRException.class, () -> service.checkAndGenerateMobileOTP(
                    "{\"mobile\":\"9999999999\",\"txnId\":\"txn-1\"}"));
        }

        @Test
        @DisplayName("verifyMobileOTP should answer with the transaction id ABDM confirmed")
        void verifyMobileOTP_shouldAnswerWithTxnId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            assertEquals("txn-1", answerOf(service.verifyMobileOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}"))
                    .get("txnId").getAsString());
            verify(httpUtils).postWithResponseEntity(eq(VERIFY_MOBILE_URL), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("verifyMobileOTP should fail when ABDM does not answer 200")
        void verifyMobileOTP_shouldFailOnNonOkStatus() throws Exception {
            stubGateway("{}", HttpStatus.BAD_REQUEST);

            assertThrows(FHIRException.class,
                    () -> service.verifyMobileOTP("{\"otp\":\"123456\",\"txnId\":\"txn-1\"}"));
        }

        @Test
        @DisplayName("generateMobileOTP should answer with the transaction id ABDM issued")
        void generateMobileOTP_shouldAnswerWithTxnId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            assertEquals("txn-1", answerOf(service.generateMobileOTP(
                    "{\"mobile\":\"9999999999\",\"txnId\":\"txn-1\"}")).get("txnId").getAsString());
            verify(httpUtils).postWithResponseEntity(eq(GENERATE_MOBILE_OTP_URL), anyString(),
                    any(HttpHeaders.class));
        }

        @Test
        @DisplayName("generateMobileOTP should fail when ABDM does not answer 200")
        void generateMobileOTP_shouldFailOnNonOkStatus() throws Exception {
            stubGateway("{}", HttpStatus.BAD_REQUEST);

            assertThrows(FHIRException.class,
                    () -> service.generateMobileOTP("{\"mobile\":\"9999999999\",\"txnId\":\"txn-1\"}"));
        }
    }

    @Nested
    @DisplayName("createHealthIDWithUID")
    class CreateAbhaTests {

        @Test
        @DisplayName("should map the ABHA ABDM created back onto the response model")
        void createHealthIDWithUID_shouldMapCreatedAbha() throws Exception {
            stubGateway("{\"healthIdNumber\":\"11-1111-1111-1111\",\"healthId\":\"abc@sbx\"}");

            HealthIDResponse created = service.createHealthIDWithUID("{\"txnId\":\"txn-1\"}");

            assertEquals("11-1111-1111-1111", created.getHealthIdNumber());
            assertEquals("abc@sbx", created.getHealthId());
            verify(httpUtils).postWithResponseEntity(eq(CREATE_URL), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("should fail when ABDM sends an empty body")
        void createHealthIDWithUID_shouldFailOnEmptyBody() throws Exception {
            stubGateway(null);

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.createHealthIDWithUID("{\"txnId\":\"txn-1\"}")).getMessage()
                    .contains("create ABHA with Aadhaar API"));
        }

        @Test
        @DisplayName("should surface the ABDM problem detail")
        void createHealthIDWithUID_shouldSurfaceProblemDetail() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(abdmError("{\"details\":[{\"message\":\"ABHA exists\"}]}"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.createHealthIDWithUID("{\"txnId\":\"txn-1\"}")).getMessage()
                    .endsWith("ABHA exists"));
        }
    }

    @Nested
    @DisplayName("The biometric steps")
    class BiometricTests {

        @Test
        @DisplayName("verifyBio should answer with the transaction id ABDM issued for the biometric")
        void verifyBio_shouldAnswerWithTxnId() throws Exception {
            stubGateway("{\"txnId\":\"txn-1\"}");

            assertEquals("txn-1", answerOf(service.verifyBio(
                    "{\"Aadhaar\":\"999999999999\",\"pid\":\"pid-blob\"}")).get("txnId").getAsString());
            verify(httpUtils).postWithResponseEntity(eq(VERIFY_BIO_URL), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("verifyBio should report the failure plainly when ABDM answers with an empty body")
        void verifyBio_shouldReportEmptyBody() throws Exception {
            stubGateway(null);

            assertEquals("NDHM_FHIR Error while verifying Bio",
                    service.verifyBio("{\"Aadhaar\":\"999999999999\"}"));
        }

        @Test
        @DisplayName("verifyBio should wrap a transport failure")
        void verifyBio_shouldWrapTransportFailure() {
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.verifyBio("{\"Aadhaar\":\"999999999999\"}")).getMessage()
                    .startsWith("NDHM_FHIR Error while accessing VerifyBio"));
        }

        @Test
        @DisplayName("confirmWithAadhaarBio should mint the ABHA card with the X-token ABDM returned")
        void confirmWithAadhaarBio_shouldMintCard() throws Exception {
            ResponseEntity<String> answer = new ResponseEntity<>("{\"x-token\":\"x-token-1\"}", HttpStatus.OK);
            when(httpUtils.postWithResponseEntity(eq(CONFIRM_BIO_URL), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(answer);
            when(common_NDHMService.getBody(answer)).thenReturn("{\"x-token\":\"x-token-1\"}");
            when(generateHealthID_CardService.generateHealthCardForBio(anyString(), eq("x-token-1")))
                    .thenReturn("card-png");

            assertEquals("card-png", service.confirmWithAadhaarBio("{\"txnId\":\"txn-1\",\"pid\":\"pid-blob\"}"));
        }

        @Test
        @DisplayName("confirmWithAadhaarBio should report the failure plainly when ABDM answers with an empty body")
        void confirmWithAadhaarBio_shouldReportEmptyBody() throws Exception {
            ResponseEntity<String> answer = new ResponseEntity<>(HttpStatus.OK);
            when(httpUtils.postWithResponseEntity(eq(CONFIRM_BIO_URL), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(answer);
            when(common_NDHMService.getBody(answer)).thenReturn(null);

            assertEquals("NDHM_FHIR Error while confirm Aadhaar bio",
                    service.confirmWithAadhaarBio("{\"txnId\":\"txn-1\"}"));
        }

        @Test
        @DisplayName("confirmWithAadhaarBio should wrap a transport failure")
        void confirmWithAadhaarBio_shouldWrapTransportFailure() {
            when(httpUtils.postWithResponseEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.confirmWithAadhaarBio("{\"txnId\":\"txn-1\"}")).getMessage()
                    .startsWith("NDHM_FHIR Error while accessing confirm Aadhaar bio"));
        }
    }
}
