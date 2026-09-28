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
package com.wipro.fhir.controller.version;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("VersionController Test Suite")
class VersionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        VersionController versionController = new VersionController();

        mockMvc = MockMvcBuilders.standaloneSetup(versionController).build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return version details sourced from git.properties on the classpath")
    void versionInformation_shouldReturnGitPropertiesContent() throws Exception {
        Properties gitProperties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("git.properties")) {
            if (inputStream == null) {
                throw new IOException("git.properties file not found in test resources.");
            }
            gitProperties.load(inputStream);
        }

        mockMvc.perform(get("/version"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buildTimestamp").value(gitProperties.getProperty("git.build.time", "unknown")))
                .andExpect(jsonPath("$.version").value(gitProperties.getProperty("git.build.version", "unknown")))
                .andExpect(jsonPath("$.branch").value(gitProperties.getProperty("git.branch", "unknown")))
                .andExpect(jsonPath("$.commitHash").value(gitProperties.getProperty("git.commit.id.abbrev", "unknown")));
    }
}
