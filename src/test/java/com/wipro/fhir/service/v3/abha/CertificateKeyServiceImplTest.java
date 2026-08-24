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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.LocalHttpStub;
import com.wipro.fhir.service.ndhm.Common_NDHMService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CertificateKeyServiceImpl Test Suite")
class CertificateKeyServiceImplTest {

    private static final String CERT_PATH = "/abdm/auth/cert";

    @Mock
    private Common_NDHMService common_NDHMService;

    @InjectMocks
    private CertificateKeyServiceImpl service;

    private LocalHttpStub stub;

    @BeforeEach
    @DisplayName("Point the ABDM certificate URL at a loopback stub before each test")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        ReflectionTestUtils.setField(service, "getAuthCertPublicKey", stub.url(CERT_PATH));
        when(common_NDHMService.getBody(any(ResponseEntity.class)))
                .thenAnswer(invocation -> ((ResponseEntity<String>) invocation.getArgument(0)).getBody());
    }

    @AfterEach
    void tearDown() {
        stub.close();
    }

    @Test
    @DisplayName("getCertPublicKey should answer with the public key ABDM publishes")
    void getCertPublicKey_shouldReturnPublicKey() throws Exception {
        stub.stubOk(CERT_PATH, "{\"publicKey\":\"MIIBIjANBg\"}");

        assertEquals("MIIBIjANBg", service.getCertPublicKey("Bearer abdm-token"));
    }

    @Test
    @DisplayName("getCertPublicKey should send the ABDM session token and a correlation id")
    void getCertPublicKey_shouldSendAuthorizationHeader() throws Exception {
        stub.stubOk(CERT_PATH, "{\"publicKey\":\"MIIBIjANBg\"}");

        service.getCertPublicKey("Bearer abdm-token");

        LocalHttpStub.Request request = stub.request(CERT_PATH);
        assertEquals("GET", request.method());
        assertEquals("Bearer abdm-token", request.header("Authorization"));
        assertTrue(request.header("Request-Id") != null);
        assertTrue(request.header("Timestamp").endsWith("Z"));
    }

    @Test
    @DisplayName("getCertPublicKey should answer with no key when ABDM does not answer 200")
    void getCertPublicKey_shouldAnswerNullOnNonOkStatus() throws Exception {
        stub.stub(CERT_PATH, LocalHttpStub.Response.of(204, ""));

        assertNull(service.getCertPublicKey("Bearer abdm-token"));
    }

    @Test
    @DisplayName("getCertPublicKey should let a transport failure surface to the caller")
    void getCertPublicKey_shouldPropagateTransportFailure() {
        ReflectionTestUtils.setField(service, "getAuthCertPublicKey", LocalHttpStub.unreachableUrl());

        assertThrows(Exception.class, () -> service.getCertPublicKey("Bearer abdm-token"));
    }
}
