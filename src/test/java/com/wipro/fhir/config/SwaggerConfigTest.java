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
import org.springframework.mock.env.MockEnvironment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SwaggerConfig Test Suite")
class SwaggerConfigTest {

    private static final String SECURITY_SCHEME_NAME = "my security";
    private static final String DEFAULT_URL = "http://localhost:9090";

    private SwaggerConfig swaggerConfig;
    private MockEnvironment environment;

    @BeforeEach
    @DisplayName("Create the configuration and an empty environment before each test")
    void setUp() {
        swaggerConfig = new SwaggerConfig();
        environment = new MockEnvironment();
    }

    @Test
    @DisplayName("customOpenAPI should describe the FHIR API")
    void customOpenAPI_shouldDescribeTheApi() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI(environment);

        assertNotNull(openAPI.getInfo());
        assertEquals("FHIR API", openAPI.getInfo().getTitle());
        assertEquals("version", openAPI.getInfo().getVersion());
        assertTrue(openAPI.getInfo().getDescription().contains("creation and management of beneficiaries"));
    }

    @Test
    @DisplayName("customOpenAPI should declare a bearer security scheme and require it")
    void customOpenAPI_shouldDeclareBearerSecurityScheme() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI(environment);

        SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get(SECURITY_SCHEME_NAME);
        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals(1, openAPI.getSecurity().size());
        assertTrue(openAPI.getSecurity().get(0).containsKey(SECURITY_SCHEME_NAME));
    }

    @Test
    @DisplayName("customOpenAPI should fall back to localhost for every unset server url")
    void customOpenAPI_shouldFallBackToLocalhostForUnsetUrls() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI(environment);

        assertEquals(3, openAPI.getServers().size());
        openAPI.getServers().forEach(server -> assertEquals(DEFAULT_URL, server.getUrl()));
        assertEquals("Dev", openAPI.getServers().get(0).getDescription());
        assertEquals("UAT", openAPI.getServers().get(1).getDescription());
        assertEquals("Demo", openAPI.getServers().get(2).getDescription());
    }

    @Test
    @DisplayName("customOpenAPI should use the configured dev, UAT and demo urls when present")
    void customOpenAPI_shouldUseConfiguredUrls() {
        environment.setProperty("api.dev.url", "https://dev.amrit.example.org");
        environment.setProperty("api.uat.url", "https://uat.amrit.example.org");
        environment.setProperty("api.demo.url", "https://demo.amrit.example.org");

        OpenAPI openAPI = swaggerConfig.customOpenAPI(environment);

        assertEquals("https://dev.amrit.example.org", openAPI.getServers().get(0).getUrl());
        assertEquals("https://uat.amrit.example.org", openAPI.getServers().get(1).getUrl());
        assertEquals("https://demo.amrit.example.org", openAPI.getServers().get(2).getUrl());
    }
}
