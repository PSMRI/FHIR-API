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
package com.wipro.fhir.service.api_channel;

import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wipro.fhir.LocalHttpStub;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("APIChannelImpl Test Suite")
class APIChannelImplTest {

    private static final String SEARCH_PATH = "/beneficiary/searchByBenId";
    private static final String AUTH_PATH = "/user/userAuthenticate";
    private static final String AUTHORIZATION = "session-key-123";

    private APIChannelImpl service;
    private LocalHttpStub stub;
    private ResourceRequestHandler request;

    @BeforeEach
    @DisplayName("Point the outbound URLs at a loopback stub before each test")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        service = new APIChannelImpl();
        ReflectionTestUtils.setField(service, "benSearchByBenIDURL", stub.url(SEARCH_PATH));
        ReflectionTestUtils.setField(service, "userAuthURL", stub.url(AUTH_PATH));
        ReflectionTestUtils.setField(service, "fhirUserName", "fhir-user");
        ReflectionTestUtils.setField(service, "fhirPassword", "fhir-secret");
        // The RestTemplate is a static field shared across instances; reset it per test.
        ReflectionTestUtils.setField(APIChannelImpl.class, "restTemplate", new RestTemplate());

        request = new ResourceRequestHandler();
        request.setBeneficiaryRegID(BigInteger.valueOf(4321L));
    }

    @AfterEach
    void tearDown() {
        stub.close();
        RequestContextHolder.resetRequestAttributes();
    }

    private void bindServletRequest() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("Authorization", AUTHORIZATION);
        servletRequest.addHeader("JwtToken", "jwt-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));
    }

    @Nested
    @DisplayName("benSearchByBenID")
    class BenSearchTests {

        @Test
        @DisplayName("should answer with the beneficiary search body on a 200")
        void benSearchByBenID_shouldReturnSearchBody() throws Exception {
            stub.stubOk(SEARCH_PATH, "{\"data\":[{\"beneficiaryRegID\":4321}]}");

            String result = service.benSearchByBenID(AUTHORIZATION, request);

            assertEquals("{\"data\":[{\"beneficiaryRegID\":4321}]}", result);
            assertEquals("POST", stub.request(SEARCH_PATH).method());
            assertEquals(AUTHORIZATION, stub.request(SEARCH_PATH).header("Authorization"));
        }

        @Test
        @DisplayName("should forward the JWT cookie and header when a servlet request is in scope")
        void benSearchByBenID_shouldForwardJwtFromServletRequest() throws Exception {
            stub.stubOk(SEARCH_PATH, "{\"data\":[]}");
            bindServletRequest();

            service.benSearchByBenID(AUTHORIZATION, request);

            assertEquals("jwt-token", stub.request(SEARCH_PATH).header("JwtToken"));
        }

        @Test
        @DisplayName("should fail when the search endpoint answers with something other than a 200 body")
        void benSearchByBenID_shouldFailOnNonOkAnswer() {
            stub.stub(SEARCH_PATH, LocalHttpStub.Response.of(204, ""));

            FHIRException failure = assertThrows(FHIRException.class,
                    () -> service.benSearchByBenID(AUTHORIZATION, request));

            assertEquals("error in patient search", failure.getMessage());
        }

        @Test
        @DisplayName("should let a transport failure surface to the caller")
        void benSearchByBenID_shouldPropagateTransportFailure() {
            ReflectionTestUtils.setField(service, "benSearchByBenIDURL", LocalHttpStub.unreachableUrl());

            assertThrows(Exception.class, () -> service.benSearchByBenID(AUTHORIZATION, request));
        }
    }

    @Nested
    @DisplayName("userAuthentication")
    class UserAuthenticationTests {

        @Test
        @DisplayName("should answer with the session key for an authenticated, active user")
        void userAuthentication_shouldReturnSessionKey() throws Exception {
            stub.stubOk(AUTH_PATH,
                    "{\"data\":{\"isAuthenticated\":true,\"Status\":\"Active\",\"key\":\"new-session-key\"}}");
            bindServletRequest();

            assertEquals("new-session-key", service.userAuthentication());
        }

        @Test
        @DisplayName("should send the configured FHIR credentials and ask for a fresh session")
        void userAuthentication_shouldSendConfiguredCredentials() throws Exception {
            stub.stubOk(AUTH_PATH, "{\"data\":{\"isAuthenticated\":true,\"Status\":\"Active\",\"key\":\"k\"}}");
            bindServletRequest();

            service.userAuthentication();

            String body = stub.request(AUTH_PATH).body();
            assertTrue(body.contains("\"userName\":\"fhir-user\""), body);
            assertTrue(body.contains("\"password\":\"fhir-secret\""), body);
            assertTrue(body.contains("\"doLogout\":true"), body);
        }

        @Test
        @DisplayName("should answer with no key when the user is not authenticated")
        void userAuthentication_shouldAnswerNullWhenNotAuthenticated() throws Exception {
            stub.stubOk(AUTH_PATH, "{\"data\":{\"isAuthenticated\":false,\"Status\":\"Active\",\"key\":\"k\"}}");
            bindServletRequest();

            assertNull(service.userAuthentication());
        }

        @Test
        @DisplayName("should answer with no key when the user is authenticated but inactive")
        void userAuthentication_shouldAnswerNullWhenInactive() throws Exception {
            stub.stubOk(AUTH_PATH, "{\"data\":{\"isAuthenticated\":true,\"Status\":\"Inactive\",\"key\":\"k\"}}");
            bindServletRequest();

            assertNull(service.userAuthentication());
        }

        @Test
        @DisplayName("should answer with no key when the auth endpoint does not answer 200")
        void userAuthentication_shouldAnswerNullOnNonOkStatus() throws Exception {
            stub.stub(AUTH_PATH, LocalHttpStub.Response.of(202, "{\"data\":null}"));
            bindServletRequest();

            assertNull(service.userAuthentication());
        }
    }
}
