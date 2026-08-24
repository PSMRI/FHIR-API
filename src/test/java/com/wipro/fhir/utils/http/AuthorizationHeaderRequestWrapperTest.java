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

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AuthorizationHeaderRequestWrapper Test Suite")
class AuthorizationHeaderRequestWrapperTest {

    private MockHttpServletRequest request;

    @BeforeEach
    @DisplayName("Create a request carrying an inbound Authorization header before each test")
    void setUp() {
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "inbound-key");
        request.addHeader("JwtToken", "header-token");
    }

    @Test
    @DisplayName("getHeader should return the overridden value for Authorization")
    void getHeader_shouldReturnOverriddenAuthorization() {
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(request, "overridden-key");

        assertEquals("overridden-key", wrapper.getHeader("Authorization"));
    }

    @Test
    @DisplayName("getHeader should match the Authorization name case-insensitively")
    void getHeader_shouldMatchAuthorizationCaseInsensitively() {
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(request, "overridden-key");

        assertEquals("overridden-key", wrapper.getHeader("authorization"));
        assertEquals("overridden-key", wrapper.getHeader("AUTHORIZATION"));
    }

    @Test
    @DisplayName("getHeader should pass every other header through to the wrapped request")
    void getHeader_shouldPassOtherHeadersThrough() {
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(request, "overridden-key");

        assertEquals("header-token", wrapper.getHeader("JwtToken"));
        assertNull(wrapper.getHeader("X-Not-Present"));
    }

    @Test
    @DisplayName("getHeader should return the blank override the JWT filter installs")
    void getHeader_shouldReturnBlankOverride() {
        AuthorizationHeaderRequestWrapper wrapper = new AuthorizationHeaderRequestWrapper(request, "");

        assertEquals("", wrapper.getHeader("Authorization"),
                "the filter blanks Authorization once the JWT has been validated");
    }

    @Test
    @DisplayName("getHeaders should return the overridden Authorization as a single-valued enumeration")
    void getHeaders_shouldReturnOverriddenAuthorizationAsSingleValue() {
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(request, "overridden-key");

        assertEquals(List.of("overridden-key"), Collections.list(wrapper.getHeaders("Authorization")));
    }

    @Test
    @DisplayName("getHeaders should pass every other header through to the wrapped request")
    void getHeaders_shouldPassOtherHeadersThrough() {
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(request, "overridden-key");

        assertEquals(List.of("header-token"), Collections.list(wrapper.getHeaders("JwtToken")));
    }

    @Test
    @DisplayName("getHeaderNames should still list Authorization alongside the wrapped names")
    void getHeaderNames_shouldListAuthorizationAlongsideWrappedNames() {
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(request, "overridden-key");

        List<String> names = Collections.list(wrapper.getHeaderNames());
        assertTrue(names.contains("Authorization"));
        assertTrue(names.contains("JwtToken"));
        assertEquals(1, names.stream().filter("Authorization"::equals).count(),
                "Authorization must not be duplicated when the wrapped request already carries it");
    }

    @Test
    @DisplayName("getHeaderNames should add Authorization when the wrapped request lacks it")
    void getHeaderNames_shouldAddAuthorizationWhenWrappedRequestLacksIt() {
        MockHttpServletRequest bare = new MockHttpServletRequest();
        bare.addHeader("JwtToken", "header-token");
        AuthorizationHeaderRequestWrapper wrapper =
                new AuthorizationHeaderRequestWrapper(bare, "overridden-key");

        assertTrue(Collections.list(wrapper.getHeaderNames()).contains("Authorization"));
    }
}
