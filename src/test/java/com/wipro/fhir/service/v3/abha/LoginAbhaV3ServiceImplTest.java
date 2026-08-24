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

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.utils.Encryption;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("LoginAbhaV3ServiceImpl Test Suite")
class LoginAbhaV3ServiceImplTest {

    private static final String LOGIN_OTP_PATH = "/abdm/login/request-otp";
    private static final String WEB_LOGIN_OTP_PATH = "/abdm/phr/login/request-otp";
    private static final String VERIFY_LOGIN_PATH = "/abdm/login/verify";
    private static final String WEB_VERIFY_PATH = "/abdm/phr/login/verify";
    private static final String VERIFY_USER_PATH = "/abdm/profile/login/verify/user";
    private static final String PHR_CARD_PATH = "/abdm/phr/card";

    @Mock
    private GenerateAuthSessionService generateAuthSessionService;

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private Encryption encryption;

    @Mock
    private CertificateKeyService certificateKeyService;

    @InjectMocks
    private LoginAbhaV3ServiceImpl service;

    private LocalHttpStub stub;

    @BeforeEach
    @DisplayName("Point the ABDM login URLs at a loopback stub before each test")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        ReflectionTestUtils.setField(service, "abhaLoginRequestOtp", stub.url(LOGIN_OTP_PATH));
        ReflectionTestUtils.setField(service, "webLoginAbhaRequestOtp", stub.url(WEB_LOGIN_OTP_PATH));
        ReflectionTestUtils.setField(service, "verifyAbhaLoginUrl", stub.url(VERIFY_LOGIN_PATH));
        ReflectionTestUtils.setField(service, "webLoginAbhaVerify", stub.url(WEB_VERIFY_PATH));
        ReflectionTestUtils.setField(service, "abhaProfileLoginVerifyUser", stub.url(VERIFY_USER_PATH));
        ReflectionTestUtils.setField(service, "abhawebProfileLoginPhrCard", stub.url(PHR_CARD_PATH));

        when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
        when(certificateKeyService.getCertPublicKey("Bearer abdm-token")).thenReturn("public-key");
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

    private List<String> scopeOf(JsonObject payload) {
        return payload.getAsJsonArray("scope").asList().stream().map(element -> element.getAsString()).toList();
    }

    @Nested
    @DisplayName("requestOtpForAbhaLogin")
    class RequestOtpTests {

        /**
         * ABDM picks the OTP scope from the login method and the login hint together, and
         * routes an abha-address login to a different endpoint than an abha-number one.
         */
        @ParameterizedTest(name = "{0} + {1} -> {2}, {3} on {4}")
        @CsvSource({
                "AADHAAR, abha-number,  abha-login,          aadhaar-verify, aadhaar, false",
                "mobile,  abha-number,  abha-login,          mobile-verify,  abdm,    false",
                "aadhaar, abha-address, abha-address-login,  aadhaar-verify, aadhaar, true",
                "mobile,  abha-address, abha-address-login,  mobile-verify,  abdm,    true",
                "mobile,  mobile,       abha-login,          mobile-verify,  abdm,    false",
                "aadhaar, aadhaar,      abha-login,          aadhaar-verify, aadhaar, false",
        })
        @DisplayName("should map the login method and hint onto the right ABDM scope and endpoint")
        void requestOtp_shouldMapMethodAndHintToScope(String loginMethod, String loginHint, String firstScope,
                String secondScope, String otpSystem, boolean webEndpoint) throws Exception {
            String path = webEndpoint ? WEB_LOGIN_OTP_PATH : LOGIN_OTP_PATH;
            stub.stubOk(path, "{\"txnId\":\"txn-1\",\"message\":\"OTP sent\"}");

            String result = service.requestOtpForAbhaLogin("{\"loginId\":\"999999999999\",\"loginMethod\":\""
                    + loginMethod + "\",\"loginHint\":\"" + loginHint + "\"}");

            assertEquals("txn-1", JsonParser.parseString(result).getAsJsonObject().get("txnId").getAsString());
            JsonObject sent = sentTo(path);
            assertEquals(List.of(firstScope, secondScope), scopeOf(sent));
            assertEquals(otpSystem, sent.get("otpSystem").getAsString());
            assertEquals("encrypted-login-id", sent.get("loginId").getAsString());
        }

        @Test
        @DisplayName("should refuse a login method and hint combination ABDM has no scope for")
        void requestOtp_shouldRefuseUnknownCombination() {
            FHIRException failure = assertThrows(FHIRException.class, () -> service.requestOtpForAbhaLogin(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"aadhaar\",\"loginHint\":\"mobile\"}"));

            assertEquals("Invalid Login ID and Login Hint, Please Pass Valid ID", failure.getMessage());
        }

        @Test
        @DisplayName("should send no login id at all when the request carries none")
        void requestOtp_shouldOmitLoginIdWhenAbsent() throws Exception {
            stub.stubOk(LOGIN_OTP_PATH, "{\"txnId\":\"txn-1\",\"message\":\"OTP sent\"}");

            service.requestOtpForAbhaLogin("{\"loginMethod\":\"mobile\",\"loginHint\":\"mobile\"}");

            assertFalse(sentTo(LOGIN_OTP_PATH).has("loginId"));
        }

        @Test
        @DisplayName("should surface the ABDM error body when ABDM rejects the request")
        void requestOtp_shouldSurfaceAbdmRejection() {
            stub.stub(LOGIN_OTP_PATH, LocalHttpStub.Response.of(400, "{\"error\":\"unknown abha\"}"));

            assertThrows(FHIRException.class, () -> service.requestOtpForAbhaLogin(
                    "{\"loginId\":\"999999999999\",\"loginMethod\":\"mobile\",\"loginHint\":\"mobile\"}"));
        }
    }

    @Nested
    @DisplayName("verifyAbhaLogin")
    class VerifyLoginTests {

        private String accountsAnswer(String extra) {
            return "{\"authResult\":\"success\",\"txnId\":\"txn-1\","
                    + "\"accounts\":[{\"ABHANumber\":\"11-1111-1111-1111\",\"name\":\"Asha\"}]" + extra + "}";
        }

        @ParameterizedTest(name = "{0} -> {1}, {2}")
        @CsvSource({
                "AADHAAR,      abha-login,         aadhaar-verify, false",
                "MOBILE,       abha-login,         mobile-verify,  false",
                "abha-mobile,  abha-address-login, mobile-verify,  true",
                "abha-aadhaar, abha-address-login, aadhaar-verify, true",
        })
        @DisplayName("should map the login method onto the right verify scope and endpoint")
        void verifyAbhaLogin_shouldMapMethodToScope(String loginMethod, String firstScope, String secondScope,
                boolean webEndpoint) throws Exception {
            String path = webEndpoint ? WEB_VERIFY_PATH : VERIFY_LOGIN_PATH;
            stub.stubOk(path, accountsAnswer(""));

            service.verifyAbhaLogin("{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\""
                    + loginMethod + "\"}");

            JsonObject sent = sentTo(path);
            assertEquals(List.of(firstScope, secondScope), scopeOf(sent));
            assertEquals("encrypted-login-id",
                    sent.getAsJsonObject("authData").getAsJsonObject("otp").get("otpValue").getAsString());
        }

        @Test
        @DisplayName("should answer with the matched ABHA account and the transaction id")
        void verifyAbhaLogin_shouldAnswerWithAccount() throws Exception {
            stub.stubOk(VERIFY_LOGIN_PATH, accountsAnswer(""));

            String result = service.verifyAbhaLogin(
                    "{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\"AADHAAR\"}");

            assertTrue(result.contains("11-1111-1111-1111"), result);
            assertTrue(result.contains("txn-1"), result);
        }

        @Test
        @DisplayName("should pass an account token straight through as the xToken")
        void verifyAbhaLogin_shouldPassAccountTokenThrough() throws Exception {
            stub.stubOk(VERIFY_LOGIN_PATH, accountsAnswer(",\"token\":\"account-token\""));

            String result = service.verifyAbhaLogin(
                    "{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\"AADHAAR\"}");

            assertTrue(result.contains("account-token"), result);
        }

        @Test
        @DisplayName("should exchange the T-token for an X-token on a mobile-hinted mobile login")
        void verifyAbhaLogin_shouldExchangeTokenForMobileLogin() throws Exception {
            stub.stubOk(VERIFY_LOGIN_PATH, accountsAnswer(",\"token\":\"t-token\""));
            stub.stubOk(VERIFY_USER_PATH, "{\"token\":\"exchanged-x-token\"}");

            String result = service.verifyAbhaLogin("{\"loginId\":\"123456\",\"txnId\":\"txn-1\","
                    + "\"loginMethod\":\"MOBILE\",\"loginHint\":\"MOBILE\"}");

            assertTrue(result.contains("exchanged-x-token"), result);
            assertEquals("Bearer t-token", stub.request(VERIFY_USER_PATH).header("T-Token"));
            JsonObject sent = sentTo(VERIFY_USER_PATH);
            assertEquals("11-1111-1111-1111", sent.get("ABHANumber").getAsString());
            assertEquals("txn-1", sent.get("txnId").getAsString());
        }

        @Test
        @DisplayName("should answer with the matched PHR user and its token for an abha-address login")
        void verifyAbhaLogin_shouldAnswerWithPhrUser() throws Exception {
            stub.stubOk(WEB_VERIFY_PATH, "{\"authResult\":\"success\",\"txnId\":\"txn-1\","
                    + "\"users\":[{\"abhaAddress\":\"abc@sbx\"}],\"tokens\":{\"token\":\"phr-token\"}}");

            String result = service.verifyAbhaLogin(
                    "{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\"abha-mobile\"}");

            assertTrue(result.contains("abc@sbx"), result);
            assertTrue(result.contains("phr-token"), result);
        }

        @Test
        @DisplayName("should answer with an empty map when ABDM reports neither accounts nor users")
        void verifyAbhaLogin_shouldAnswerEmptyWithoutAccountsOrUsers() throws Exception {
            stub.stubOk(VERIFY_LOGIN_PATH, "{\"authResult\":\"success\",\"txnId\":\"txn-1\"}");

            assertEquals("{}", service.verifyAbhaLogin(
                    "{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\"AADHAAR\"}"));
        }

        @Test
        @DisplayName("should surface the ABDM message when the OTP does not authenticate")
        void verifyAbhaLogin_shouldSurfaceFailedAuthMessage() {
            stub.stubOk(VERIFY_LOGIN_PATH, "{\"authResult\":\"failed\",\"message\":\"wrong otp\"}");

            assertEquals("wrong otp", assertThrows(FHIRException.class, () -> service.verifyAbhaLogin(
                    "{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\"AADHAAR\"}")).getMessage());
        }

        @Test
        @DisplayName("should surface the ABDM error body on a non-200 answer")
        void verifyAbhaLogin_shouldSurfaceAbdmRejection() {
            stub.stub(VERIFY_LOGIN_PATH, LocalHttpStub.Response.of(500, "{\"error\":\"ABDM down\"}"));

            assertThrows(FHIRException.class, () -> service.verifyAbhaLogin(
                    "{\"loginId\":\"123456\",\"txnId\":\"txn-1\",\"loginMethod\":\"AADHAAR\"}"));
        }
    }

    @Nested
    @DisplayName("verifyProfileLoginUser")
    class VerifyProfileLoginUserTests {

        @Test
        @DisplayName("should answer with the token ABDM mints for the verified user")
        void verifyProfileLoginUser_shouldReturnToken() throws Exception {
            stub.stubOk(VERIFY_USER_PATH, "{\"token\":\"x-token-1\"}");

            assertEquals("x-token-1",
                    service.verifyProfileLoginUser("t-token", "txn-1", "11-1111-1111-1111"));
        }

        @Test
        @DisplayName("should answer with no token when ABDM does not answer 200")
        void verifyProfileLoginUser_shouldAnswerNullOnNonOkStatus() throws Exception {
            stub.stub(VERIFY_USER_PATH, LocalHttpStub.Response.of(204, ""));

            assertNull(service.verifyProfileLoginUser("t-token", "txn-1", "11-1111-1111-1111"));
        }

        @Test
        @DisplayName("should wrap a transport failure in a FHIRException")
        void verifyProfileLoginUser_shouldWrapTransportFailure() {
            ReflectionTestUtils.setField(service, "abhaProfileLoginVerifyUser", LocalHttpStub.unreachableUrl());

            assertThrows(FHIRException.class,
                    () -> service.verifyProfileLoginUser("t-token", "txn-1", "11-1111-1111-1111"));
        }
    }

    @Nested
    @DisplayName("getWebLoginPhrCard")
    class PhrCardTests {

        @Test
        @DisplayName("should answer with the PHR card ABDM returns on its 202")
        void getWebLoginPhrCard_shouldReturnCard() throws Exception {
            stub.stub(PHR_CARD_PATH, LocalHttpStub.Response.of(202, "phr-card-png"));

            String result = service.getWebLoginPhrCard("{\"xToken\":\"x-token-1\"}");

            assertTrue(result.contains("phr-card-png"), result);
            assertEquals("Bearer x-token-1", stub.request(PHR_CARD_PATH).header("X-Token"));
        }

        @Test
        @DisplayName("should still call ABDM when the caller sends no X-token")
        void getWebLoginPhrCard_shouldTolerateMissingXToken() throws Exception {
            stub.stub(PHR_CARD_PATH, LocalHttpStub.Response.of(202, "phr-card-png"));

            service.getWebLoginPhrCard("{}");

            assertNull(stub.request(PHR_CARD_PATH).header("X-Token"));
        }

        @Test
        @DisplayName("should fail on any status other than the 202 ABDM answers with")
        void getWebLoginPhrCard_shouldFailOnNonAcceptedStatus() {
            stub.stubOk(PHR_CARD_PATH, "phr-card-png");

            assertThrows(FHIRException.class, () -> service.getWebLoginPhrCard("{\"xToken\":\"x-token-1\"}"));
        }
    }
}
