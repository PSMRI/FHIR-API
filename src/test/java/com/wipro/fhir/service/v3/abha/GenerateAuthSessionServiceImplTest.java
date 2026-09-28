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
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("GenerateAuthSessionServiceImpl Test Suite")
class GenerateAuthSessionServiceImplTest {

    private static final String SESSION_PATH = "/abdm/gateway/sessions";

    @Mock
    private Common_NDHMService common_NDHMService;

    @InjectMocks
    private GenerateAuthSessionServiceImpl service;

    private LocalHttpStub stub;

    @BeforeEach
    @DisplayName("Point the ABDM session URL at a loopback stub and clear the cached token")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        ReflectionTestUtils.setField(service, "abdmV3UserAuthenticate", stub.url(SESSION_PATH));
        ReflectionTestUtils.setField(service, "clientID", "client-1");
        ReflectionTestUtils.setField(service, "clientSecret", "secret-1");
        ReflectionTestUtils.setField(service, "xCMId", "sbx");
        clearCachedToken();

        when(common_NDHMService.getBody(any(ResponseEntity.class)))
                .thenAnswer(invocation -> ((ResponseEntity<String>) invocation.getArgument(0)).getBody());
    }

    @AfterEach
    void tearDown() {
        stub.close();
        clearCachedToken();
    }

    /** The minted token is cached in static state shared across instances. */
    private void clearCachedToken() {
        ReflectionTestUtils.setField(GenerateAuthSessionServiceImpl.class, "ABHA_AUTH_TOKEN", null);
        ReflectionTestUtils.setField(GenerateAuthSessionServiceImpl.class, "ABHA_TOKEN_EXP", null);
    }

    private String cachedToken() {
        return (String) ReflectionTestUtils.getField(GenerateAuthSessionServiceImpl.class, "ABHA_AUTH_TOKEN");
    }

    @Nested
    @DisplayName("generateAbhaAuthToken")
    class GenerateTokenTests {

        @Test
        @DisplayName("should mint a bearer token from the ABDM access token")
        void generateAbhaAuthToken_shouldMintBearerToken() throws Exception {
            stub.stubOk(SESSION_PATH, "{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            assertEquals("success", service.generateAbhaAuthToken());
            assertEquals("Bearer abdm-access", cachedToken());
        }

        @Test
        @DisplayName("should send the configured client credentials under the client_credentials grant")
        void generateAbhaAuthToken_shouldSendClientCredentials() throws Exception {
            stub.stubOk(SESSION_PATH, "{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            service.generateAbhaAuthToken();

            JsonObject sent = JsonParser.parseString(stub.request(SESSION_PATH).body()).getAsJsonObject();
            assertEquals("client-1", sent.get("clientId").getAsString());
            assertEquals("secret-1", sent.get("clientSecret").getAsString());
            assertEquals("client_credentials", sent.get("grantType").getAsString());
            assertEquals("sbx", stub.request(SESSION_PATH).header("X-Cm-Id"));
        }

        @Test
        @DisplayName("should record an expiry derived from the ABDM lifetime")
        void generateAbhaAuthToken_shouldRecordExpiry() throws Exception {
            stub.stubOk(SESSION_PATH, "{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            service.generateAbhaAuthToken();

            Long expiry = (Long) ReflectionTestUtils.getField(
                    GenerateAuthSessionServiceImpl.class, "ABHA_TOKEN_EXP");
            assertTrue(expiry > System.currentTimeMillis(),
                    "a freshly minted token must not already be treated as expired");
        }

        @Test
        @DisplayName("should wrap an ABDM transport failure in a FHIRException")
        void generateAbhaAuthToken_shouldWrapTransportFailure() {
            ReflectionTestUtils.setField(service, "abdmV3UserAuthenticate", LocalHttpStub.unreachableUrl());

            FHIRException failure = assertThrows(FHIRException.class, () -> service.generateAbhaAuthToken());

            assertTrue(failure.getMessage().startsWith("NDHM_FHIR Error while accessing authenticate API"));
        }

        @Test
        @DisplayName("should wrap an unusable ABDM answer in a FHIRException")
        void generateAbhaAuthToken_shouldWrapUnusableAnswer() {
            stub.stubOk(SESSION_PATH, "{\"noAccessTokenHere\":true}");

            assertThrows(FHIRException.class, () -> service.generateAbhaAuthToken());
        }
    }

    @Nested
    @DisplayName("getAbhaAuthToken")
    class GetTokenTests {

        @Test
        @DisplayName("should mint a token the first time it is asked for one")
        void getAbhaAuthToken_shouldMintOnFirstUse() throws Exception {
            stub.stubOk(SESSION_PATH, "{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            assertEquals("Bearer abdm-access", service.getAbhaAuthToken());
            assertEquals(1, stub.requests(SESSION_PATH).size());
        }

        @Test
        @DisplayName("should reuse the cached token while it is still valid")
        void getAbhaAuthToken_shouldReuseValidToken() throws Exception {
            stub.stubOk(SESSION_PATH, "{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            service.getAbhaAuthToken();
            service.getAbhaAuthToken();

            assertEquals(1, stub.requests(SESSION_PATH).size(),
                    "a valid cached token must not trigger a second ABDM session call");
        }

        @Test
        @DisplayName("should mint a fresh token once the cached one has expired")
        void getAbhaAuthToken_shouldRefreshExpiredToken() throws Exception {
            stub.stubOk(SESSION_PATH, "{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");
            ReflectionTestUtils.setField(GenerateAuthSessionServiceImpl.class, "ABHA_AUTH_TOKEN", "Bearer stale");
            ReflectionTestUtils.setField(GenerateAuthSessionServiceImpl.class, "ABHA_TOKEN_EXP", 1L);

            assertEquals("Bearer abdm-access", service.getAbhaAuthToken());
            assertEquals(1, stub.requests(SESSION_PATH).size());
        }

        @Test
        @DisplayName("should wrap a failed session call in a FHIRException")
        void getAbhaAuthToken_shouldWrapSessionFailure() {
            ReflectionTestUtils.setField(service, "abdmV3UserAuthenticate", LocalHttpStub.unreachableUrl());

            assertThrows(FHIRException.class, () -> service.getAbhaAuthToken());
        }
    }
}
