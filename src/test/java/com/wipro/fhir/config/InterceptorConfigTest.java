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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.wipro.fhir.utils.http.HTTPRequestInterceptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterceptorConfig Test Suite")
class InterceptorConfigTest {

    @Mock
    private HTTPRequestInterceptor requestInterceptor;

    private InterceptorConfig interceptorConfig;

    @BeforeEach
    @DisplayName("Wire the configuration with a mocked interceptor before each test")
    void setUp() {
        interceptorConfig = new InterceptorConfig();
        ReflectionTestUtils.setField(interceptorConfig, "requestInterceptor", requestInterceptor);
    }

    @Test
    @DisplayName("addInterceptors should register the HTTP request interceptor exactly once")
    void addInterceptors_shouldRegisterRequestInterceptorOnce() {
        InterceptorRegistry registry = new InterceptorRegistry();

        interceptorConfig.addInterceptors(registry);

        List<Object> interceptors = (List<Object>) ReflectionTestUtils.invokeMethod(registry, "getInterceptors");
        assertEquals(1, interceptors.size());
        assertSame(requestInterceptor, interceptors.get(0));
    }
}
