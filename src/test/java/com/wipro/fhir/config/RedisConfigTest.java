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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

import com.wipro.fhir.data.users.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisConfig Test Suite")
class RedisConfigTest {

    @Mock
    private RedisConnectionFactory connectionFactory;

    private RedisConfig redisConfig;

    @BeforeEach
    @DisplayName("Create the configuration before each test")
    void setUp() {
        redisConfig = new RedisConfig();
    }

    @Test
    @DisplayName("configureRedisAction should disable Spring Session's CONFIG command probing")
    void configureRedisAction_shouldDisableConfigCommandProbing() {
        assertSame(ConfigureRedisAction.NO_OP, redisConfig.configureRedisAction());
    }

    @Test
    @DisplayName("redisTemplate should bind the supplied connection factory")
    void redisTemplate_shouldBindSuppliedConnectionFactory() {
        RedisTemplate<String, User> template = redisConfig.redisTemplate(connectionFactory);

        assertNotNull(template);
        assertSame(connectionFactory, template.getConnectionFactory());
    }

    @Test
    @DisplayName("redisTemplate should serialise keys as plain strings and values as User JSON")
    void redisTemplate_shouldSerialiseKeysAsStringsAndValuesAsJson() {
        RedisTemplate<String, User> template = redisConfig.redisTemplate(connectionFactory);

        assertTrue(template.getKeySerializer() instanceof StringRedisSerializer);
        assertTrue(template.getValueSerializer() instanceof Jackson2JsonRedisSerializer);
    }
}
