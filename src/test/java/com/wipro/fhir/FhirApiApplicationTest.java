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
package com.wipro.fhir;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FhirApiApplication Test Suite")
class FhirApiApplicationTest {

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    @DisplayName("main should hand the application class to Spring Boot")
    void main_shouldStartSpringBoot() {
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            FhirApiApplication.main(new String[] { "--server.port=0" });

            springApplication.verify(() ->
                    SpringApplication.run(FhirApiApplication.class, new String[] { "--server.port=0" }));
        }
    }

    @Test
    @DisplayName("redisTemplate should key on strings and serialise the user value as JSON")
    void redisTemplate_shouldSerialiseUserAsJson() {
        RedisTemplate<String, Object> template = new FhirApiApplication().redisTemplate(redisConnectionFactory);

        assertSame(redisConnectionFactory, template.getConnectionFactory());
        assertTrue(template.getKeySerializer() instanceof StringRedisSerializer);
        assertNotNull(template.getValueSerializer());
    }

    @Test
    @DisplayName("the servlet initializer should register the application class for a war deployment")
    void servletInitializer_shouldRegisterApplicationClass() {
        SpringApplicationBuilder builder = Mockito.mock(SpringApplicationBuilder.class);
        Mockito.when(builder.sources(any(Class[].class))).thenReturn(builder);

        assertSame(builder, new ServletInitializer() {
            SpringApplicationBuilder call() {
                return configure(builder);
            }
        }.call());

        verify(builder).sources(FhirApiApplication.class);
    }
}
