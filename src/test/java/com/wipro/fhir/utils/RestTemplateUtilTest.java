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
package com.wipro.fhir.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("RestTemplateUtil Test Suite")
class RestTemplateUtilTest {

    private static final String AUTHORIZATION = "session-key-123";
    private static final String BODY = "{\"benCount\":5}";
    private static final String JSON_UTF8 = "application/json;charset=utf-8";

    private MockHttpServletRequest request;

    @BeforeEach
    @DisplayName("Bind a fresh mock request to the request context before each test")
    void setUp() {
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    @DisplayName("Clear the request context and User-Agent thread local after each test")
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
        UserAgentContext.clear();
    }

    @Nested
    @DisplayName("Outside a web request")
    class NoRequestContextTests {

        @Test
        @DisplayName("createRequestEntity should build a minimal entity when no request is bound")
        void createRequestEntity_shouldBuildMinimalEntityWithoutRequestContext() {
            RequestContextHolder.resetRequestAttributes();

            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertSame(BODY, entity.getBody());
            assertEquals(JSON_UTF8, entity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
            assertEquals(AUTHORIZATION, entity.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            assertFalse(entity.getHeaders().containsKey("JwtToken"));
            assertFalse(entity.getHeaders().containsKey(HttpHeaders.COOKIE));
        }
    }

    @Nested
    @DisplayName("Inside a web request")
    class WithRequestContextTests {

        @Test
        @DisplayName("createRequestEntity should carry the content type and authorization from the caller")
        void createRequestEntity_shouldCarryContentTypeAndAuthorization() {
            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertSame(BODY, entity.getBody());
            assertEquals(JSON_UTF8, entity.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
            assertEquals(AUTHORIZATION, entity.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        }

        @Test
        @DisplayName("createRequestEntity should forward the inbound JwtToken header")
        void createRequestEntity_shouldForwardInboundJwtTokenHeader() {
            request.addHeader("JwtToken", "header-token");

            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertEquals("header-token", entity.getHeaders().getFirst("JwtToken"));
        }

        @Test
        @DisplayName("createRequestEntity should replay the Jwttoken cookie as a Cookie header")
        void createRequestEntity_shouldReplayJwtTokenCookie() {
            request.setCookies(new Cookie("Jwttoken", "cookie-token"));

            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertEquals("Jwttoken=cookie-token", entity.getHeaders().getFirst(HttpHeaders.COOKIE));
        }

        @Test
        @DisplayName("createRequestEntity should omit the Cookie header when no Jwttoken cookie is present")
        void createRequestEntity_shouldOmitCookieHeaderWithoutJwtTokenCookie() {
            request.setCookies(new Cookie("theme", "dark"));

            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertNull(entity.getHeaders().getFirst(HttpHeaders.COOKIE));
        }

        @Test
        @DisplayName("createRequestEntity should propagate the mobile User-Agent when one is in scope")
        void createRequestEntity_shouldPropagateMobileUserAgent() {
            UserAgentContext.setUserAgent("okhttp/4.9.0");

            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertEquals("okhttp/4.9.0", entity.getHeaders().getFirst(HttpHeaders.USER_AGENT));
        }

        @Test
        @DisplayName("createRequestEntity should omit the User-Agent header when none is in scope")
        void createRequestEntity_shouldOmitUserAgentWhenNoneInScope() {
            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertNull(entity.getHeaders().getFirst(HttpHeaders.USER_AGENT));
        }

        @Test
        @DisplayName("createRequestEntity should carry both the cookie and header tokens together")
        void createRequestEntity_shouldCarryBothCookieAndHeaderTokens() {
            request.addHeader("JwtToken", "header-token");
            request.setCookies(new Cookie("Jwttoken", "cookie-token"));

            HttpEntity<Object> entity = RestTemplateUtil.createRequestEntity(BODY, AUTHORIZATION);

            assertEquals("header-token", entity.getHeaders().getFirst("JwtToken"));
            assertEquals("Jwttoken=cookie-token", entity.getHeaders().getFirst(HttpHeaders.COOKIE));
        }
    }
}
