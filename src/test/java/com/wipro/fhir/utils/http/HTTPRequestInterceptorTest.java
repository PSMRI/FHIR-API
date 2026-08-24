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
package com.wipro.fhir.utils.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.sessionobject.SessionObject;
import com.wipro.fhir.utils.validator.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HTTPRequestInterceptor Test Suite")
class HTTPRequestInterceptorTest {

    private static final String ALLOWED_ORIGIN = "https://amrit.example.org";
    private static final String AUTHORIZATION = "session-key-123";

    @Mock
    private Validator validator;

    @Mock
    private SessionObject sessionObject;

    private HTTPRequestInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    @DisplayName("Set up the interceptor with mocked collaborators before each test")
    void setUp() {
        interceptor = new HTTPRequestInterceptor();
        interceptor.setValidator(validator);
        interceptor.setSessionObject(sessionObject);
        ReflectionTestUtils.setField(interceptor, "allowedOrigins", ALLOWED_ORIGIN + ",http://localhost:*");
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("preHandle authorization")
    class PreHandleTests {

        @Test
        @DisplayName("preHandle should proceed without validation when no Authorization header is present")
        void preHandle_shouldProceedWhenAuthorizationHeaderIsMissing() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");

            assertTrue(interceptor.preHandle(request, response, new Object()));
            verify(validator, never()).checkKeyExists(anyString(), anyString());
        }

        @Test
        @DisplayName("preHandle should proceed without validation when the Authorization header is empty")
        void preHandle_shouldProceedWhenAuthorizationHeaderIsEmpty() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", "");

            assertTrue(interceptor.preHandle(request, response, new Object()));
            verify(validator, never()).checkKeyExists(anyString(), anyString());
        }

        @Test
        @DisplayName("preHandle should skip validation entirely for OPTIONS requests")
        void preHandle_shouldSkipValidationForOptions() throws Exception {
            request.setMethod("OPTIONS");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);

            assertTrue(interceptor.preHandle(request, response, new Object()));
            verify(validator, never()).checkKeyExists(anyString(), anyString());
        }

        @Test
        @DisplayName("preHandle should validate the session key against the client address for a guarded API")
        void preHandle_shouldValidateSessionKeyForGuardedApi() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.setRemoteAddr("10.1.2.3");

            assertTrue(interceptor.preHandle(request, response, new Object()));
            verify(validator).checkKeyExists(AUTHORIZATION, "10.1.2.3");
        }

        @Test
        @DisplayName("preHandle should prefer the X-FORWARDED-FOR address over the socket address")
        void preHandle_shouldPreferForwardedForAddress() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.addHeader("X-FORWARDED-FOR", "203.0.113.9");
            request.setRemoteAddr("10.1.2.3");

            interceptor.preHandle(request, response, new Object());

            verify(validator).checkKeyExists(AUTHORIZATION, "203.0.113.9");
        }

        @Test
        @DisplayName("preHandle should fall back to the socket address when X-FORWARDED-FOR is blank")
        void preHandle_shouldFallBackWhenForwardedForIsBlank() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.addHeader("X-FORWARDED-FOR", "   ");
            request.setRemoteAddr("10.1.2.3");

            interceptor.preHandle(request, response, new Object());

            verify(validator).checkKeyExists(AUTHORIZATION, "10.1.2.3");
        }

        @Test
        @DisplayName("preHandle should let whitelisted endpoints through without a session-key check")
        void preHandle_shouldBypassValidationForWhitelistedEndpoints() throws Exception {
            for (String api : new String[] { "swagger-ui.html", "index.html", "index.css",
                    "swagger-initializer.js", "swagger-config", "swagger-ui-bundle.js", "swagger-ui.css", "ui",
                    "swagger-ui-standalone-preset.js", "favicon-32x32.png", "favicon-16x16.png",
                    "swagger-resources", "api-docs", "version" }) {
                MockHttpServletRequest whitelisted = new MockHttpServletRequest();
                whitelisted.setMethod("POST");
                whitelisted.setRequestURI("/" + api);
                whitelisted.addHeader("Authorization", AUTHORIZATION);

                assertTrue(interceptor.preHandle(whitelisted, new MockHttpServletResponse(), new Object()),
                        api + " should be allowed through");
            }
            verify(validator, never()).checkKeyExists(anyString(), anyString());
        }

        @Test
        @DisplayName("preHandle should halt the request for the error endpoint")
        void preHandle_shouldHaltForErrorEndpoint() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/error");
            request.addHeader("Authorization", AUTHORIZATION);

            assertFalse(interceptor.preHandle(request, response, new Object()));
            verify(validator, never()).checkKeyExists(anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("preHandle failure handling")
    class PreHandleFailureTests {

        @Test
        @DisplayName("preHandle should halt and write the error payload when the session key is rejected")
        void preHandle_shouldHaltAndWriteErrorWhenSessionKeyRejected() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.setRemoteAddr("10.1.2.3");
            doThrow(new FHIRException("Invalid session key"))
                    .when(validator).checkKeyExists(AUTHORIZATION, "10.1.2.3");

            assertFalse(interceptor.preHandle(request, response, new Object()));
            assertTrue(response.getContentAsString().contains("Invalid session key"));
            assertEquals("application/json", response.getContentType());
        }

        @Test
        @DisplayName("preHandle should echo CORS headers on the error response for an allowed origin")
        void preHandle_shouldAddCorsHeadersOnErrorForAllowedOrigin() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.addHeader("Origin", ALLOWED_ORIGIN);
            request.setRemoteAddr("10.1.2.3");
            doThrow(new FHIRException("Invalid session key"))
                    .when(validator).checkKeyExists(AUTHORIZATION, "10.1.2.3");

            interceptor.preHandle(request, response, new Object());

            assertEquals(ALLOWED_ORIGIN, response.getHeader("Access-Control-Allow-Origin"));
            assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
        }

        @Test
        @DisplayName("preHandle should withhold CORS headers on the error response for an unauthorized origin")
        void preHandle_shouldWithholdCorsHeadersOnErrorForUnauthorizedOrigin() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.addHeader("Origin", "https://evil.example.com");
            request.setRemoteAddr("10.1.2.3");
            doThrow(new FHIRException("Invalid session key"))
                    .when(validator).checkKeyExists(AUTHORIZATION, "10.1.2.3");

            interceptor.preHandle(request, response, new Object());

            assertNull(response.getHeader("Access-Control-Allow-Origin"));
        }

        @Test
        @DisplayName("preHandle should withhold CORS headers when no origin allow-list is configured")
        void preHandle_shouldWithholdCorsHeadersWhenAllowListIsBlank() throws Exception {
            ReflectionTestUtils.setField(interceptor, "allowedOrigins", "");
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            request.addHeader("Origin", ALLOWED_ORIGIN);
            request.setRemoteAddr("10.1.2.3");
            doThrow(new FHIRException("Invalid session key"))
                    .when(validator).checkKeyExists(AUTHORIZATION, "10.1.2.3");

            interceptor.preHandle(request, response, new Object());

            assertNull(response.getHeader("Access-Control-Allow-Origin"));
        }
    }

    @Nested
    @DisplayName("postHandle and afterCompletion")
    class PostHandleTests {

        @Test
        @DisplayName("postHandle should refresh the session object when an Authorization header is present")
        void postHandle_shouldRefreshSessionObject() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            when(sessionObject.getSessionObject(AUTHORIZATION)).thenReturn("session-payload");

            interceptor.postHandle(request, response, new Object(), null);

            verify(sessionObject).updateSessionObject(AUTHORIZATION, "session-payload");
        }

        @Test
        @DisplayName("postHandle should do nothing when no Authorization header is present")
        void postHandle_shouldDoNothingWithoutAuthorizationHeader() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");

            interceptor.postHandle(request, response, new Object(), null);

            verify(sessionObject, never()).updateSessionObject(anyString(), anyString());
        }

        @Test
        @DisplayName("postHandle should swallow a session store failure so the response still completes")
        void postHandle_shouldSwallowSessionStoreFailure() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Authorization", AUTHORIZATION);
            when(sessionObject.getSessionObject(AUTHORIZATION))
                    .thenThrow(new IllegalStateException("redis down"));

            interceptor.postHandle(request, response, new Object(), null);

            verify(sessionObject, never()).updateSessionObject(anyString(), anyString());
        }

        @Test
        @DisplayName("afterCompletion should complete without touching the response")
        void afterCompletion_shouldLeaveResponseUntouched() throws Exception {
            interceptor.afterCompletion(request, response, new Object(), null);

            assertEquals(200, response.getStatus());
            assertEquals("", response.getContentAsString());
        }
    }
}
