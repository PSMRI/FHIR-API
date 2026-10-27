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
package com.wipro.fhir.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("CorsConfig Test Suite")
class CorsConfigTest {

    private static final String ALLOWED_ORIGINS = "https://amrit.example.org, http://localhost:*";

    private CorsConfig corsConfig;

    @BeforeEach
    @DisplayName("Configure the allow-list before each test")
    void setUp() {
        corsConfig = new CorsConfig();
        ReflectionTestUtils.setField(corsConfig, "allowedOrigins", ALLOWED_ORIGINS);
    }

    // CorsRegistry.getCorsConfigurations() is protected, so read the registrations
    // back reflectively — the same map Spring MVC itself consumes.
    @SuppressWarnings("unchecked")
    private Map<String, CorsConfiguration> corsConfigurations(CorsRegistry registry) {
        return (Map<String, CorsConfiguration>)
                ReflectionTestUtils.invokeMethod(registry, "getCorsConfigurations");
    }

    @Test
    @DisplayName("addCorsMappings should register a mapping for every path")
    void addCorsMappings_shouldRegisterMappingForEveryPath() {
        CorsRegistry registry = new CorsRegistry();

        corsConfig.addCorsMappings(registry);

        Map<String, CorsConfiguration> configurations = corsConfigurations(registry);
        assertEquals(1, configurations.size());
        assertNotNull(configurations.get("/**"));
    }

    @Test
    @DisplayName("addCorsMappings should trim and register each configured origin pattern")
    void addCorsMappings_shouldTrimAndRegisterOriginPatterns() {
        CorsRegistry registry = new CorsRegistry();

        corsConfig.addCorsMappings(registry);

        CorsConfiguration configuration = corsConfigurations(registry).get("/**");
        assertEquals(java.util.List.of("https://amrit.example.org", "http://localhost:*"),
                configuration.getAllowedOriginPatterns());
    }

    @Test
    @DisplayName("addCorsMappings should permit the documented methods and headers with credentials")
    void addCorsMappings_shouldPermitDocumentedMethodsAndHeaders() {
        CorsRegistry registry = new CorsRegistry();

        corsConfig.addCorsMappings(registry);

        CorsConfiguration configuration = corsConfigurations(registry).get("/**");
        assertEquals(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"),
                configuration.getAllowedMethods());
        assertTrue(configuration.getAllowedHeaders().contains("Jwttoken"));
        assertTrue(configuration.getExposedHeaders().contains("Authorization"));
        assertEquals(Boolean.TRUE, configuration.getAllowCredentials());
        assertEquals(3600L, configuration.getMaxAge());
    }

    @Test
    @DisplayName("addCorsMappings should fail fast when no origin allow-list is configured")
    void addCorsMappings_shouldFailFastWithoutAllowList() {
        ReflectionTestUtils.setField(corsConfig, "allowedOrigins", null);
        CorsRegistry registry = new CorsRegistry();

        assertThrows(NullPointerException.class, () -> corsConfig.addCorsMappings(registry));
    }
}
