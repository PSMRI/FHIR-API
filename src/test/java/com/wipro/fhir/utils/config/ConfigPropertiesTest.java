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
package com.wipro.fhir.utils.config;

import java.util.Base64;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ConfigProperties Test Suite")
class ConfigPropertiesTest {

    private Properties originalProperties;

    @BeforeEach
    @DisplayName("Instantiate the holder so application.properties is loaded, keeping the original statics")
    void setUp() {
        new ConfigProperties();
        originalProperties = (Properties) ReflectionTestUtils.getField(ConfigProperties.class, "properties");
    }

    @AfterEach
    @DisplayName("Restore the shared static properties after each test")
    void tearDown() {
        ReflectionTestUtils.setField(ConfigProperties.class, "properties", originalProperties);
    }

    @Nested
    @DisplayName("Reading values from application.properties")
    class PropertyLookupTests {

        @Test
        @DisplayName("getPropertyByName should return the configured value for a known key")
        void getPropertyByName_shouldReturnConfiguredValue() {
            assertEquals("6379", ConfigProperties.getPropertyByName("spring.redis.port"));
        }

        @Test
        @DisplayName("getPropertyByName should return null for a key that is not configured")
        void getPropertyByName_shouldReturnNullForUnknownKey() {
            assertNull(ConfigProperties.getPropertyByName("no.such.key.configured"));
        }

        @Test
        @DisplayName("getBoolean should parse a boolean property")
        void getBoolean_shouldParseBooleanProperty() {
            assertTrue(ConfigProperties.getBoolean("iemr.extend.expiry.time"));
        }

        @Test
        @DisplayName("getBoolean should return false for a value that is not a boolean")
        void getBoolean_shouldReturnFalseForNonBooleanValue() {
            assertEquals(false, ConfigProperties.getBoolean("spring.redis.port"));
        }

        @Test
        @DisplayName("getInteger should parse an integer property")
        void getInteger_shouldParseIntegerProperty() {
            assertEquals(1800, ConfigProperties.getInteger("iemr.session.expiry.time"));
        }

        @Test
        @DisplayName("getInteger should fall back to zero when the value is not a number")
        void getInteger_shouldFallBackToZeroForNonNumericValue() {
            assertEquals(0, ConfigProperties.getInteger("spring.session.store-type"));
        }

        @Test
        @DisplayName("getLong should parse a long property")
        void getLong_shouldParseLongProperty() {
            assertEquals(1800L, ConfigProperties.getLong("iemr.session.expiry.time"));
        }

        @Test
        @DisplayName("getLong should fall back to zero when the value is not a number")
        void getLong_shouldFallBackToZeroForNonNumericValue() {
            assertEquals(0L, ConfigProperties.getLong("spring.session.store-type"));
        }

        @Test
        @DisplayName("getFloat should parse a numeric property")
        void getFloat_shouldParseNumericProperty() {
            assertEquals(6379F, ConfigProperties.getFloat("spring.redis.port"));
        }

        @Test
        @DisplayName("getFloat should fall back to zero when the value is not a number")
        void getFloat_shouldFallBackToZeroForNonNumericValue() {
            assertEquals(0F, ConfigProperties.getFloat("spring.session.store-type"));
        }
    }

    @Nested
    @DisplayName("Session and Redis accessors")
    class AccessorTests {

        @Test
        @DisplayName("getSessionExpiryTime should resolve the configured session expiry")
        void getSessionExpiryTime_shouldResolveConfiguredExpiry() {
            assertEquals(1800, ConfigProperties.getSessionExpiryTime());
        }

        @Test
        @DisplayName("getRedisPort should fall back to zero when no iemr.redis.port is configured")
        void getRedisPort_shouldFallBackToZeroWhenUnconfigured() {
            assertEquals(0, ConfigProperties.getRedisPort());
        }

        @Test
        @DisplayName("getRedisUrl should return null when no iemr.redis.url is configured")
        void getRedisUrl_shouldReturnNullWhenUnconfigured() {
            assertNull(ConfigProperties.getRedisUrl());
        }
    }

    @Nested
    @DisplayName("Password handling")
    class PasswordTests {

        @Test
        @DisplayName("getPassword should return a plain-text password unchanged")
        void getPassword_shouldReturnPlainTextUnchanged() {
            Properties stub = new Properties();
            stub.setProperty("db.password", "plainSecret");
            ReflectionTestUtils.setField(ConfigProperties.class, "properties", stub);

            assertEquals("plainSecret", ConfigProperties.getPassword("db.password"));
        }

        @Test
        @DisplayName("getPassword should Base64-decode a password tagged with the 0X10 prefix")
        void getPassword_shouldBase64DecodeTaggedPassword() {
            String encoded = Base64.getEncoder().encodeToString("s3cr3t".getBytes());
            Properties stub = new Properties();
            stub.setProperty("db.password", "0X10:" + encoded);
            ReflectionTestUtils.setField(ConfigProperties.class, "properties", stub);

            assertEquals("s3cr3t", ConfigProperties.getPassword("db.password"));
        }
    }
}
