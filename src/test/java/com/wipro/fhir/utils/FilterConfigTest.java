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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
@DisplayName("FilterConfig Test Suite")
class FilterConfigTest {

    private static final String ALLOWED_ORIGINS = "https://amrit.example.org,http://localhost:*";

    @Mock
    private JwtAuthenticationUtil jwtAuthenticationUtil;

    private FilterConfig filterConfig;

    @BeforeEach
    @DisplayName("Configure the allow-list before each test")
    void setUp() {
        filterConfig = new FilterConfig();
        ReflectionTestUtils.setField(filterConfig, "allowedOrigins", ALLOWED_ORIGINS);
    }

    @Test
    @DisplayName("jwtUserIdValidationFilter should register the JWT filter across every url pattern")
    void jwtUserIdValidationFilter_shouldRegisterFilterForEveryUrlPattern() {
        FilterRegistrationBean<JwtUserIdValidationFilter> registration =
                filterConfig.jwtUserIdValidationFilter(jwtAuthenticationUtil);

        assertNotNull(registration.getFilter());
        assertEquals(java.util.Set.of("/*"), registration.getUrlPatterns());
    }

    @Test
    @DisplayName("jwtUserIdValidationFilter should run at the highest precedence so auth happens first")
    void jwtUserIdValidationFilter_shouldRunAtHighestPrecedence() {
        FilterRegistrationBean<JwtUserIdValidationFilter> registration =
                filterConfig.jwtUserIdValidationFilter(jwtAuthenticationUtil);

        assertEquals(Ordered.HIGHEST_PRECEDENCE, registration.getOrder());
    }

    @Test
    @DisplayName("jwtUserIdValidationFilter should hand the filter the configured origins and auth util")
    void jwtUserIdValidationFilter_shouldPassOriginsAndAuthUtilToFilter() {
        JwtUserIdValidationFilter filter =
                filterConfig.jwtUserIdValidationFilter(jwtAuthenticationUtil).getFilter();

        assertEquals(ALLOWED_ORIGINS, ReflectionTestUtils.getField(filter, "allowedOrigins"));
        assertSame(jwtAuthenticationUtil, ReflectionTestUtils.getField(filter, "jwtAuthenticationUtil"));
    }
}
