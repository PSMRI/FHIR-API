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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.wipro.fhir.utils.http.AuthorizationHeaderRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUserIdValidationFilter Test Suite")
class JwtUserIdValidationFilterTest {

    private static final String ALLOWED_ORIGINS = "https://amrit.example.org,http://localhost:*";
    private static final String ALLOWED_ORIGIN = "https://amrit.example.org";
    private static final String DISALLOWED_ORIGIN = "https://evil.example.com";

    @Mock
    private JwtAuthenticationUtil jwtAuthenticationUtil;

    @Mock
    private FilterChain filterChain;

    private JwtUserIdValidationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    @DisplayName("Set up the filter with a configured allow-list before each test")
    void setUp() {
        filter = new JwtUserIdValidationFilter(jwtAuthenticationUtil, ALLOWED_ORIGINS);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Nested
    @DisplayName("Origin validation and CORS")
    class OriginValidationTests {

        @Test
        @DisplayName("doFilter should reject an OPTIONS request that carries no Origin header")
        void doFilter_shouldRejectOptionsWithoutOrigin() throws Exception {
            request.setMethod("OPTIONS");
            request.setRequestURI("/generateResource/createResource");

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
            assertEquals("OPTIONS request requires Origin header", response.getErrorMessage());
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should reject an OPTIONS request from an origin outside the allow-list")
        void doFilter_shouldRejectOptionsFromDisallowedOrigin() throws Exception {
            request.setMethod("OPTIONS");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Origin", DISALLOWED_ORIGIN);

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
            assertEquals("Origin not allowed", response.getErrorMessage());
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should answer an allowed OPTIONS preflight with 200 and the CORS headers")
        void doFilter_shouldAnswerAllowedPreflightWithCorsHeaders() throws Exception {
            request.setMethod("OPTIONS");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Origin", ALLOWED_ORIGIN);

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_OK, response.getStatus());
            assertEquals(ALLOWED_ORIGIN, response.getHeader("Access-Control-Allow-Origin"));
            assertEquals("GET, POST, PUT, PATCH, DELETE, OPTIONS",
                    response.getHeader("Access-Control-Allow-Methods"));
            assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
            assertEquals("3600", response.getHeader("Access-Control-Max-Age"));
            assertNotNull(response.getHeader("Access-Control-Allow-Headers"));
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should match a wildcard localhost origin pattern")
        void doFilter_shouldMatchWildcardLocalhostOrigin() throws Exception {
            request.setMethod("OPTIONS");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Origin", "http://localhost:4200");

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_OK, response.getStatus());
            assertEquals("http://localhost:4200", response.getHeader("Access-Control-Allow-Origin"));
        }

        @Test
        @DisplayName("doFilter should reject a non-OPTIONS request from an origin outside the allow-list")
        void doFilter_shouldRejectNonOptionsFromDisallowedOrigin() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Origin", DISALLOWED_ORIGIN);

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
            assertEquals("Origin not allowed", response.getErrorMessage());
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should treat every origin as disallowed when no allow-list is configured")
        void doFilter_shouldRejectAllOriginsWhenAllowListIsBlank() throws Exception {
            JwtUserIdValidationFilter unconfiguredFilter =
                    new JwtUserIdValidationFilter(jwtAuthenticationUtil, "  ");
            request.setMethod("OPTIONS");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("Origin", ALLOWED_ORIGIN);

            unconfiguredFilter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        }

        @Test
        @DisplayName("doFilter should not add CORS headers when the request carries no Origin header")
        void doFilter_shouldNotAddCorsHeadersWithoutOrigin() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/health");

            filter.doFilter(request, response, filterChain);

            assertNull(response.getHeader("Access-Control-Allow-Origin"));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Public endpoints that bypass token validation")
    class PublicEndpointTests {

        @Test
        @DisplayName("doFilter should pass /health straight through without validating a token")
        void doFilter_shouldSkipValidationForHealth() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/health");

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(jwtAuthenticationUtil, never()).validateUserIdAndJwtToken(anyString());
        }

        @Test
        @DisplayName("doFilter should pass /version straight through without validating a token")
        void doFilter_shouldSkipValidationForVersion() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/version");

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(jwtAuthenticationUtil, never()).validateUserIdAndJwtToken(anyString());
        }

        @Test
        @DisplayName("doFilter should pass the login endpoint straight through without validating a token")
        void doFilter_shouldSkipValidationForUserAuthenticate() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/user/userAuthenticate");

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(jwtAuthenticationUtil, never()).validateUserIdAndJwtToken(anyString());
        }

        @Test
        @DisplayName("doFilter should pass any /public path straight through without validating a token")
        void doFilter_shouldSkipValidationForPublicPaths() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/public/anything");

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(jwtAuthenticationUtil, never()).validateUserIdAndJwtToken(anyString());
        }

        @Test
        @DisplayName("doFilter should pass the concurrent-session logout endpoint straight through")
        void doFilter_shouldSkipValidationForConcurrentSessionLogout() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/user/logOutUserFromConcurrentSession");

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(jwtAuthenticationUtil, never()).validateUserIdAndJwtToken(anyString());
        }
    }

    @Nested
    @DisplayName("JWT token validation")
    class TokenValidationTests {

        @Test
        @DisplayName("doFilter should continue the chain when the cookie token is valid")
        void doFilter_shouldContinueChainWhenCookieTokenIsValid() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.setCookies(new Cookie("Jwttoken", "cookie-token"));
            when(jwtAuthenticationUtil.validateUserIdAndJwtToken("cookie-token")).thenReturn(true);

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(any(AuthorizationHeaderRequestWrapper.class), any(ServletResponse.class));
            assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        }

        @Test
        @DisplayName("doFilter should reject with 401 when the cookie token is rejected")
        void doFilter_shouldRejectWhenCookieTokenIsInvalid() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.setCookies(new Cookie("Jwttoken", "cookie-token"));
            when(jwtAuthenticationUtil.validateUserIdAndJwtToken("cookie-token")).thenReturn(false);

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertEquals("Unauthorized: Invalid or missing token", response.getErrorMessage());
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should continue the chain when the header token is valid")
        void doFilter_shouldContinueChainWhenHeaderTokenIsValid() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("JwtToken", "header-token");
            when(jwtAuthenticationUtil.validateUserIdAndJwtToken("header-token")).thenReturn(true);

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(any(AuthorizationHeaderRequestWrapper.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should reject with 401 when the header token is rejected")
        void doFilter_shouldRejectWhenHeaderTokenIsInvalid() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("JwtToken", "header-token");
            when(jwtAuthenticationUtil.validateUserIdAndJwtToken("header-token")).thenReturn(false);

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should reject with 401 when no token is present at all")
        void doFilter_shouldRejectWhenNoTokenPresent() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertEquals("Unauthorized: Invalid or missing token", response.getErrorMessage());
        }

        @Test
        @DisplayName("doFilter should surface a 401 carrying the message when validation throws")
        void doFilter_shouldRejectWithErrorMessageWhenValidationThrows() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("JwtToken", "header-token");
            when(jwtAuthenticationUtil.validateUserIdAndJwtToken("header-token"))
                    .thenThrow(new IllegalStateException("token expired"));

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            assertTrue(response.getErrorMessage().contains("token expired"),
                    "error message should carry the underlying cause");
        }
    }

    @Nested
    @DisplayName("Mobile client handling")
    class MobileClientTests {

        @Test
        @DisplayName("doFilter should let an okhttp client through on its Authorization header alone")
        void doFilter_shouldAllowOkHttpClientWithAuthorizationHeader() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("User-Agent", "okhttp/4.9.0");
            request.addHeader("Authorization", "some-session-key");

            filter.doFilter(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(jwtAuthenticationUtil, never()).validateUserIdAndJwtToken(anyString());
        }

        @Test
        @DisplayName("doFilter should reject a non-okhttp client that only carries an Authorization header")
        void doFilter_shouldRejectJavaClientWithAuthorizationHeaderOnly() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("User-Agent", "Java/17.0.2");
            request.addHeader("Authorization", "some-session-key");

            filter.doFilter(request, response, filterChain);

            verify(filterChain, never()).doFilter(any(), any());
            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }

        @Test
        @DisplayName("doFilter should clear the User-Agent context once the mobile request completes")
        void doFilter_shouldClearUserAgentContextAfterMobileRequest() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("User-Agent", "okhttp/4.9.0");
            request.addHeader("Authorization", "some-session-key");

            filter.doFilter(request, response, filterChain);

            assertNull(UserAgentContext.getUserAgent(),
                    "the thread-local User-Agent must not leak past the request");
        }

        @Test
        @DisplayName("doFilter should reject a browser client that has no token")
        void doFilter_shouldRejectBrowserClientWithoutToken() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("User-Agent", "Mozilla/5.0");
            request.addHeader("Authorization", "some-session-key");

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
            verify(filterChain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
        }

        @Test
        @DisplayName("doFilter should reject a mobile client that sends no Authorization header")
        void doFilter_shouldRejectMobileClientWithoutAuthorizationHeader() throws Exception {
            request.setMethod("POST");
            request.setRequestURI("/generateResource/createResource");
            request.addHeader("User-Agent", "okhttp/4.9.0");

            filter.doFilter(request, response, filterChain);

            assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        }
    }

    @Nested
    @DisplayName("userId cookie hygiene")
    class UserIdCookieTests {

        @Test
        @DisplayName("doFilter should expire any userId cookie the client sends")
        void doFilter_shouldExpireUserIdCookie() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/health");
            request.setCookies(new Cookie("userId", "1234"));

            filter.doFilter(request, response, filterChain);

            Cookie cleared = Arrays.stream(response.getCookies())
                    .filter(cookie -> "userId".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
            assertNotNull(cleared, "a userId cookie should have been sent back to expire it");
            assertEquals(0, cleared.getMaxAge());
            assertEquals("/", cleared.getPath());
            assertTrue(cleared.isHttpOnly());
            assertTrue(cleared.getSecure());
        }

        @Test
        @DisplayName("doFilter should leave unrelated cookies untouched")
        void doFilter_shouldLeaveUnrelatedCookiesUntouched() throws Exception {
            request.setMethod("GET");
            request.setRequestURI("/health");
            request.setCookies(new Cookie("theme", "dark"));

            filter.doFilter(request, response, filterChain);

            assertEquals(0, response.getCookies().length);
        }
    }
}
