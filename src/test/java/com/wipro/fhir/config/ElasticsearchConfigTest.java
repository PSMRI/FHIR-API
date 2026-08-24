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

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ElasticsearchConfig Test Suite")
class ElasticsearchConfigTest {

    private ElasticsearchConfig config;

    @BeforeEach
    @DisplayName("Configure the Elasticsearch connection settings before each test")
    void setUp() {
        config = new ElasticsearchConfig();
        ReflectionTestUtils.setField(config, "esHost", "127.0.0.1");
        ReflectionTestUtils.setField(config, "esPort", 9200);
        ReflectionTestUtils.setField(config, "esUsername", "elastic");
        ReflectionTestUtils.setField(config, "esPassword", "elastic-secret");
        ReflectionTestUtils.setField(config, "connectionTimeout", 10_000);
        ReflectionTestUtils.setField(config, "socketTimeout", 120_000);
        ReflectionTestUtils.setField(config, "maxConnections", 200);
        ReflectionTestUtils.setField(config, "maxConnectionsPerRoute", 100);
    }

    @Test
    @DisplayName("elasticsearchClient should build a client against the configured host")
    void elasticsearchClient_shouldBuildClient() {
        ElasticsearchClient client = config.elasticsearchClient();

        assertNotNull(client, "the client is built lazily and does not connect until it is used");
        assertNotNull(client._transport());
    }

    @Test
    @DisplayName("asyncExecutor should size the sync pool and name its threads for the ABHA sync")
    void asyncExecutor_shouldSizeAndNameSyncPool() {
        Executor executor = config.asyncExecutor();

        ThreadPoolTaskExecutor pool = assertInstanceOf(ThreadPoolTaskExecutor.class, executor);
        assertEquals(5, pool.getCorePoolSize());
        assertEquals(20, pool.getMaxPoolSize());
        assertEquals("es-sync-", pool.getThreadNamePrefix());
        assertTrue(pool.getThreadPoolExecutor().getRejectedExecutionHandler()
                instanceof ThreadPoolExecutor.CallerRunsPolicy,
                "a rejected ABHA sync runs on the caller rather than being dropped");

        pool.shutdown();
    }
}
