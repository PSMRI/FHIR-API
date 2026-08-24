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
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.utils.gateway.email.EmailService;
import com.wipro.fhir.utils.gateway.email.GenericEmailServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("IEMRApplBeans Test Suite")
class IEMRApplBeansTest {

    private IEMRApplBeans beans;

    @BeforeEach
    @DisplayName("Create the bean configuration with Redis coordinates before each test")
    void setUp() {
        beans = new IEMRApplBeans();
        ReflectionTestUtils.setField(beans, "redisHost", "redis.example.org");
        ReflectionTestUtils.setField(beans, "redisPort", 6379);
    }

    @Test
    @DisplayName("getVaidator should supply a validator bean")
    void getVaidator_shouldSupplyValidatorBean() {
        assertNotNull(beans.getVaidator());
    }

    @Test
    @DisplayName("getEmailService should supply the generic email implementation")
    void getEmailService_shouldSupplyGenericImplementation() {
        EmailService emailService = beans.getEmailService();

        assertTrue(emailService instanceof GenericEmailServiceImpl);
    }

    @Test
    @DisplayName("getJavaMailSender should supply a JavaMailSender implementation")
    void getJavaMailSender_shouldSupplyMailSenderImplementation() {
        JavaMailSender mailSender = beans.getJavaMailSender();

        assertTrue(mailSender instanceof JavaMailSenderImpl);
    }

    @Test
    @DisplayName("configProperties should supply a fresh properties holder on each call")
    void configProperties_shouldSupplyFreshHolder() {
        assertNotSame(beans.configProperties(), beans.configProperties());
    }

    @Test
    @DisplayName("sessionObject should supply a session holder bean")
    void sessionObject_shouldSupplySessionHolder() {
        assertNotNull(beans.sessionObject());
    }

    @Test
    @DisplayName("redisStorage should supply a Redis store bean")
    void redisStorage_shouldSupplyRedisStore() {
        assertNotNull(beans.redisStorage());
    }

    @Test
    @DisplayName("connectionFactory should point Lettuce at the configured host and port")
    void connectionFactory_shouldPointAtConfiguredHostAndPort() {
        LettuceConnectionFactory factory = beans.connectionFactory();

        assertEquals("redis.example.org", factory.getHostName());
        assertEquals(6379, factory.getPort());
    }
}
