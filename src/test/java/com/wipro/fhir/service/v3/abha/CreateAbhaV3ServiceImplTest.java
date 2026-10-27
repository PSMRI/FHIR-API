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
package com.wipro.fhir.service.v3.abha;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.LocalHttpStub;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.v3.abhaCard.LoginMethod;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.utils.Encryption;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CreateAbhaV3ServiceImpl Test Suite")
class CreateAbhaV3ServiceImplTest {

    private static final String REQUEST_OTP_PATH = "/abdm/enrol/request-otp";
    private static final String AUTH_BY_ABDM_PATH = "/abdm/enrol/auth-by-abdm";
    private static final String ENROL_BY_AADHAAR_PATH = "/abdm/enrol/by-aadhaar";
    private static final String PRINT_CARD_PATH = "/abdm/profile/card";
    private static final String PUBLIC_KEY = "public-key";

    @Mock
    private GenerateAuthSessionService generateAuthSessionService;

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private Encryption encryption;

    @Mock
    private HealthIDRepo healthIDRepo;

    @Mock
    private CertificateKeyService certificateKeyService;

    @InjectMocks
    private CreateAbhaV3ServiceImpl service;

    private LocalHttpStub stub;

    @BeforeEach
    @DisplayName("Point the ABDM enrolment URLs at a loopback stub before each test")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        ReflectionTestUtils.setField(service, "requestOtpForEnrollment", stub.url(REQUEST_OTP_PATH));
        ReflectionTestUtils.setField(service, "requestAuthByAbdm", stub.url(AUTH_BY_ABDM_PATH));
        ReflectionTestUtils.setField(service, "abhaEnrollByAadhaar", stub.url(ENROL_BY_AADHAAR_PATH));
        ReflectionTestUtils.setField(service, "printAbhaCard", stub.url(PRINT_CARD_PATH));
        ReflectionTestUtils.setField(service, "abhaMode", "sbx");

        when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
        when(certificateKeyService.getCertPublicKey("Bearer abdm-token")).thenReturn(PUBLIC_KEY);
        when(encryption.encrypt(anyString(), anyString())).thenReturn("encrypted-login-id");
        when(common_NDHMService.getBody(any(ResponseEntity.class)))
                .thenAnswer(invocation -> ((ResponseEntity<String>) invocation.getArgument(0)).getBody());
    }

    @AfterEach
    void tearDown() {
        stub.close();
    }

    private JsonObject sentTo(String path) {
        return JsonParser.parseString(stub.request(path).body()).getAsJsonObject();
    }

    @Nested
    @DisplayName("getOtpForEnrollment")
    class RequestOtpTests {

        @Test
        @DisplayName("should ask ABDM for an Aadhaar OTP under the abha-enrol scope")
        void getOtpForEnrollment_shouldRequestAadhaarOtp() throws Exception {
            stub.stubOk(REQUEST_OTP_PATH, "{\"txnId\":\"txn-1\",\"message\":\"OTP sent\"}");

            String result = service.getOtpForEnrollment(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"AADHAAR\"}");

            JsonObject answer = JsonParser.parseString(result).getAsJsonObject();
            assertEquals("txn-1", answer.get("txnId").getAsString());
            assertEquals("OTP sent", answer.get("message").getAsString());
            JsonObject sent = sentTo(REQUEST_OTP_PATH);
            assertEquals("encrypted-login-id", sent.get("loginId").getAsString());
            assertEquals("aadhaar", sent.get("otpSystem").getAsString());
            assertEquals("aadhaar", sent.get("loginHint").getAsString());
            assertEquals("abha-enrol", sent.getAsJsonArray("scope").get(0).getAsString());
        }

        @Test
        @DisplayName("should ask ABDM for a mobile OTP under both the enrol and verify scopes")
        void getOtpForEnrollment_shouldRequestMobileOtp() throws Exception {
            stub.stubOk(REQUEST_OTP_PATH, "{\"txnId\":\"txn-2\",\"message\":\"OTP sent\"}");

            service.getOtpForEnrollment(
                    "{\"loginId\":\"9999999999\",\"loginMethod\":\"MOBILE\",\"txnId\":\"txn-2\"}");

            JsonObject sent = sentTo(REQUEST_OTP_PATH);
            assertEquals("abdm", sent.get("otpSystem").getAsString());
            assertEquals("mobile", sent.get("loginHint").getAsString());
            assertEquals("txn-2", sent.get("txnId").getAsString());
            assertEquals(2, sent.getAsJsonArray("scope").size());
        }

        @Test
        @DisplayName("should send the ABDM session token and the correlation headers")
        void getOtpForEnrollment_shouldSendAbdmHeaders() throws Exception {
            stub.stubOk(REQUEST_OTP_PATH, "{\"txnId\":\"txn-1\",\"message\":\"OTP sent\"}");

            service.getOtpForEnrollment("{\"loginId\":\"999999999999\",\"loginMethod\":\"AADHAAR\"}");

            LocalHttpStub.Request request = stub.request(REQUEST_OTP_PATH);
            assertEquals("Bearer abdm-token", request.header("Authorization"));
            assertTrue(request.header("Request-Id") != null);
            assertTrue(request.header("Timestamp").endsWith("Z"));
        }

        @Test
        @DisplayName("should skip the encryption step when the request carries no login id")
        void getOtpForEnrollment_shouldSkipEncryptionWithoutLoginId() throws Exception {
            stub.stubOk(REQUEST_OTP_PATH, "{\"txnId\":\"txn-1\",\"message\":\"OTP sent\"}");

            service.getOtpForEnrollment("{\"loginMethod\":\"AADHAAR\"}");

            verify(encryption, never()).encrypt(anyString(), anyString());
        }

        @Test
        @DisplayName("should surface the ABDM error body when ABDM rejects the request")
        void getOtpForEnrollment_shouldSurfaceAbdmRejection() {
            stub.stub(REQUEST_OTP_PATH, LocalHttpStub.Response.of(400, "{\"error\":\"invalid aadhaar\"}"));

            assertThrows(FHIRException.class, () -> service.getOtpForEnrollment(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"AADHAAR\"}"));
        }

        @Test
        @DisplayName("should wrap a failure to mint the ABDM session token")
        void getOtpForEnrollment_shouldWrapTokenFailure() throws Exception {
            when(generateAuthSessionService.getAbhaAuthToken()).thenThrow(new FHIRException("no ABDM session"));

            assertEquals("no ABDM session", assertThrows(FHIRException.class, () -> service.getOtpForEnrollment(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"AADHAAR\"}")).getMessage());
        }
    }

    @Nested
    @DisplayName("enrollmentByAadhaar")
    class EnrolByAadhaarTests {

        private String abdmProfile(String message, String isNew, String tokens) {
            return "{\"message\":\"" + message + "\",\"txnId\":\"txn-1\",\"isNew\":\"" + isNew + "\","
                    + "\"ABHAProfile\":{\"ABHANumber\":\"11-1111-1111-1111\",\"phrAddress\":[\"abc@sbx\",\"def@sbx\"],"
                    + "\"firstName\":\"Asha\",\"middleName\":\"Kumari\",\"lastName\":\"Devi\",\"dob\":\"15-08-1990\"}"
                    + tokens + "}";
        }

        @Test
        @DisplayName("should save a newly created ABHA and answer with its profile")
        void enrollmentByAadhaar_shouldSaveNewAbha() throws Exception {
            stub.stubOk(ENROL_BY_AADHAAR_PATH, abdmProfile("account created successfully", "true",
                    ",\"tokens\":{\"token\":\"x-token-1\"}"));
            when(healthIDRepo.getCountOfHealthIdNumber("11-1111-1111-1111")).thenReturn(0);

            String result = service.enrollmentByAadhaar(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"AADHAAR\",\"txnId\":\"txn-1\","
                            + "\"providerServiceMapId\":7,\"createdBy\":\"admin\"}");

            ArgumentCaptor<HealthIDResponse> captor = ArgumentCaptor.forClass(HealthIDResponse.class);
            verify(healthIDRepo).save(captor.capture());
            HealthIDResponse saved = captor.getValue();
            assertEquals("11-1111-1111-1111", saved.getHealthIdNumber());
            assertEquals("abc@sbx, def@sbx", saved.getHealthId(),
                    "every PHR address ABDM returns is kept, comma separated");
            assertEquals("Asha Kumari Devi", saved.getName());
            assertEquals("1990", saved.getYearOfBirth());
            assertEquals("08", saved.getMonthOfBirth());
            assertEquals("15", saved.getDayOfBirth());
            assertEquals(7, saved.getProviderServiceMapID());
            assertEquals("admin", saved.getCreatedBy());
            assertTrue(saved.getIsNewAbha());
            assertTrue(result.contains("x-token-1"), result);
            assertTrue(result.contains("txn-1"), result);
        }

        @Test
        @DisplayName("should not save again when the ABHA number is already on file")
        void enrollmentByAadhaar_shouldNotResaveKnownAbha() throws Exception {
            stub.stubOk(ENROL_BY_AADHAAR_PATH, abdmProfile("this account already exist", "false", ""));
            when(healthIDRepo.getCountOfHealthIdNumber("11-1111-1111-1111")).thenReturn(1);

            String result = service.enrollmentByAadhaar(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"AADHAAR\",\"txnId\":\"txn-1\"}");

            verify(healthIDRepo, never()).save(any(HealthIDResponse.class));
            assertTrue(result.contains("ABHAProfile"), result);
            assertFalse(result.contains("xToken"), "no token section means no xToken in the answer");
        }

        @Test
        @DisplayName("should build the biometric enrolment payload for a BIOMETRIC login")
        void enrollmentByAadhaar_shouldBuildBiometricPayload() throws Exception {
            stub.stubOk(ENROL_BY_AADHAAR_PATH, abdmProfile("account created successfully", "true", ""));
            when(healthIDRepo.getCountOfHealthIdNumber("11-1111-1111-1111")).thenReturn(0);

            service.enrollmentByAadhaar("{\"loginId\":\"999999999999\",\"loginMethod\":\"BIOMETRIC\","
                    + "\"txnId\":\"txn-1\",\"pId\":\"pid-blob\",\"mobileNumber\":\"9999999999\"}");

            JsonObject authData = sentTo(ENROL_BY_AADHAAR_PATH).getAsJsonObject("authData");
            assertEquals("bio", authData.getAsJsonArray("authMethods").get(0).getAsString());
            assertEquals("pid-blob", authData.getAsJsonObject("bio").get("fingerPrintAuthPid").getAsString());
            assertEquals("encrypted-login-id", authData.getAsJsonObject("bio").get("aadhaar").getAsString());
        }

        @Test
        @DisplayName("should send the abha-enrollment consent with the Aadhaar OTP payload")
        void enrollmentByAadhaar_shouldSendConsent() throws Exception {
            stub.stubOk(ENROL_BY_AADHAAR_PATH, abdmProfile("account created successfully", "true", ""));
            when(healthIDRepo.getCountOfHealthIdNumber("11-1111-1111-1111")).thenReturn(0);

            service.enrollmentByAadhaar("{\"loginId\":\"999999\",\"loginMethod\":\"AADHAAR\",\"txnId\":\"txn-1\","
                    + "\"mobileNumber\":\"9999999999\"}");

            JsonObject sent = sentTo(ENROL_BY_AADHAAR_PATH);
            assertEquals("abha-enrollment", sent.getAsJsonObject("consent").get("code").getAsString());
            assertEquals("1.4", sent.getAsJsonObject("consent").get("version").getAsString());
            assertEquals("otp", sent.getAsJsonObject("authData").getAsJsonArray("authMethods").get(0).getAsString());
        }

        @Test
        @DisplayName("should answer with an empty map when ABDM reports an unrecognised outcome")
        void enrollmentByAadhaar_shouldAnswerEmptyForUnknownMessage() throws Exception {
            stub.stubOk(ENROL_BY_AADHAAR_PATH, abdmProfile("otp expired", "false", ""));

            assertEquals("{}", service.enrollmentByAadhaar(
                    "{\"loginId\":\"999999\",\"loginMethod\":\"AADHAAR\",\"txnId\":\"txn-1\"}"));
            verify(healthIDRepo, never()).save(any(HealthIDResponse.class));
        }

        @Test
        @DisplayName("should surface the ABDM error body when ABDM rejects the enrolment")
        void enrollmentByAadhaar_shouldSurfaceAbdmRejection() {
            stub.stub(ENROL_BY_AADHAAR_PATH, LocalHttpStub.Response.of(422, "{\"error\":\"otp expired\"}"));

            assertThrows(FHIRException.class, () -> service.enrollmentByAadhaar(
                    "{\"loginId\":\"999999\",\"loginMethod\":\"AADHAAR\",\"txnId\":\"txn-1\"}"));
        }
    }

    @Nested
    @DisplayName("verifyAuthByAbdm")
    class VerifyAuthTests {

        @Test
        @DisplayName("should send the encrypted OTP under both the enrol and verify scopes")
        void verifyAuthByAbdm_shouldSendEncryptedOtp() throws Exception {
            stub.stubOk(AUTH_BY_ABDM_PATH, "{\"txnId\":\"txn-1\",\"message\":\"mobile verified\"}");

            String result = service.verifyAuthByAbdm("{\"loginId\":\"123456\",\"txnId\":\"txn-1\"}");

            JsonObject answer = JsonParser.parseString(result).getAsJsonObject();
            assertEquals("txn-1", answer.get("txnId").getAsString());
            assertEquals("mobile verified", answer.get("message").getAsString());
            JsonObject sent = sentTo(AUTH_BY_ABDM_PATH);
            JsonObject otp = sent.getAsJsonObject("authData").getAsJsonObject("otp");
            assertEquals("encrypted-login-id", otp.get("otpValue").getAsString());
            assertEquals("txn-1", otp.get("txnId").getAsString());
            assertEquals(2, sent.getAsJsonArray("scope").size());
        }

        @Test
        @DisplayName("should surface the ABDM error body when the OTP does not verify")
        void verifyAuthByAbdm_shouldSurfaceAbdmRejection() {
            stub.stub(AUTH_BY_ABDM_PATH, LocalHttpStub.Response.of(400, "{\"error\":\"wrong otp\"}"));

            assertThrows(FHIRException.class,
                    () -> service.verifyAuthByAbdm("{\"loginId\":\"123456\",\"txnId\":\"txn-1\"}"));
        }
    }

    @Nested
    @DisplayName("getAbhaCardPrinted")
    class PrintCardTests {

        @Test
        @DisplayName("should answer with the card ABDM returns on its 202")
        void getAbhaCardPrinted_shouldReturnCard() throws Exception {
            stub.stub(PRINT_CARD_PATH, LocalHttpStub.Response.of(202, "card-png-bytes"));

            String result = service.getAbhaCardPrinted("{\"xToken\":\"x-token-1\"}");

            assertTrue(result.contains("card-png-bytes"), result);
        }

        @Test
        @DisplayName("should forward the caller's X-token as a bearer token")
        void getAbhaCardPrinted_shouldForwardXToken() throws Exception {
            stub.stub(PRINT_CARD_PATH, LocalHttpStub.Response.of(202, "card-png-bytes"));

            service.getAbhaCardPrinted("{\"xToken\":\"x-token-1\"}");

            assertEquals("Bearer x-token-1", stub.request(PRINT_CARD_PATH).header("X-Token"));
        }

        @Test
        @DisplayName("should still call ABDM when the caller sends no X-token")
        void getAbhaCardPrinted_shouldTolerateMissingXToken() throws Exception {
            stub.stub(PRINT_CARD_PATH, LocalHttpStub.Response.of(202, "card-png-bytes"));

            service.getAbhaCardPrinted("{}");

            assertEquals(null, stub.request(PRINT_CARD_PATH).header("X-Token"));
        }

        @Test
        @DisplayName("should fail on any status other than the 202 ABDM answers with")
        void getAbhaCardPrinted_shouldFailOnNonAcceptedStatus() {
            stub.stubOk(PRINT_CARD_PATH, "card-png-bytes");

            assertThrows(FHIRException.class, () -> service.getAbhaCardPrinted("{\"xToken\":\"x-token-1\"}"));
        }
    }

    @Nested
    @DisplayName("Enrolment payload builders")
    class PayloadBuilderTests {

        private LoginMethod loginData() {
            LoginMethod loginData = new LoginMethod();
            loginData.setTxnId("txn-1");
            loginData.setMobileNumber("9999999999");
            loginData.setPId("pid-blob");
            return loginData;
        }

        @Test
        @DisplayName("formAadharEnrollReqObjByAadhar should carry the OTP, mobile and consent")
        void formAadharEnrollReqObjByAadhar_shouldCarryOtpAndConsent() {
            JsonObject payload = JsonParser
                    .parseString(service.formAadharEnrollReqObjByAadhar(loginData(), "encrypted"))
                    .getAsJsonObject();

            JsonObject otp = payload.getAsJsonObject("authData").getAsJsonObject("otp");
            assertEquals("encrypted", otp.get("otpValue").getAsString());
            assertEquals("9999999999", otp.get("mobile").getAsString());
            assertEquals("txn-1", otp.get("txnId").getAsString());
            assertEquals("abha-enrollment", payload.getAsJsonObject("consent").get("code").getAsString());
        }

        @Test
        @DisplayName("formAadharEnrollReqObjByBiometric should carry the fingerprint PID and consent")
        void formAadharEnrollReqObjByBiometric_shouldCarryPidAndConsent() {
            JsonObject payload = JsonParser
                    .parseString(service.formAadharEnrollReqObjByBiometric(loginData(), "encrypted"))
                    .getAsJsonObject();

            JsonObject bio = payload.getAsJsonObject("authData").getAsJsonObject("bio");
            assertEquals("encrypted", bio.get("aadhaar").getAsString());
            assertEquals("pid-blob", bio.get("fingerPrintAuthPid").getAsString());
            assertEquals("1.4", payload.getAsJsonObject("consent").get("version").getAsString());
        }
    }
}
