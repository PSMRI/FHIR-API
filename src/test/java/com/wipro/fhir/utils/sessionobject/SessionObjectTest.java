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
package com.wipro.fhir.utils.sessionobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wipro.fhir.utils.config.ConfigProperties;
import com.wipro.fhir.utils.redis.RedisSessionException;
import com.wipro.fhir.utils.redis.RedisStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionObject Test Suite")
class SessionObjectTest {

    private static final String KEY = "session-key";
    private static final String VALUE = "{\"userName\":\"amrit-user\"}";

    @Mock
    private RedisStorage objectStore;

    private SessionObject sessionObject;
    private int expectedExpiry;
    private boolean expectedExtend;

    @BeforeEach
    @DisplayName("Wire the session holder with a mocked Redis store before each test")
    void setUp() {
        sessionObject = new SessionObject();
        sessionObject.setObjectStore(objectStore);
        expectedExpiry = ConfigProperties.getSessionExpiryTime();
        expectedExtend = ConfigProperties.getExtendExpiryTime();
    }

    @Nested
    @DisplayName("Reading and writing the session")
    class ReadWriteTests {

        @Test
        @DisplayName("getSessionObject should delegate to the store with the configured expiry settings")
        void getSessionObject_shouldDelegateWithConfiguredExpiry() throws RedisSessionException {
            when(objectStore.getObject(KEY, expectedExtend, expectedExpiry)).thenReturn(VALUE);

            assertEquals(VALUE, sessionObject.getSessionObject(KEY));
            verify(objectStore).getObject(KEY, expectedExtend, expectedExpiry);
        }

        @Test
        @DisplayName("setSessionObject should delegate to the store with the configured expiry")
        void setSessionObject_shouldDelegateWithConfiguredExpiry() throws RedisSessionException {
            when(objectStore.setObject(KEY, VALUE, expectedExpiry)).thenReturn(KEY);

            assertEquals(KEY, sessionObject.setSessionObject(KEY, VALUE));
            verify(objectStore).setObject(KEY, VALUE, expectedExpiry);
        }

        @Test
        @DisplayName("updateSessionObject should delegate to the store with the configured expiry settings")
        void updateSessionObject_shouldDelegateWithConfiguredExpiry() throws RedisSessionException {
            when(objectStore.updateObject(KEY, VALUE, expectedExtend, expectedExpiry)).thenReturn(KEY);

            assertEquals(KEY, sessionObject.updateSessionObject(KEY, VALUE));
            verify(objectStore).updateObject(KEY, VALUE, expectedExtend, expectedExpiry);
        }

        @Test
        @DisplayName("deleteSessionObject should delegate the removal to the store")
        void deleteSessionObject_shouldDelegateRemoval() throws RedisSessionException {
            when(objectStore.deleteObject(KEY)).thenReturn(1L);

            sessionObject.deleteSessionObject(KEY);

            verify(objectStore).deleteObject(KEY);
        }
    }

    @Nested
    @DisplayName("Propagating store failures")
    class FailureTests {

        @Test
        @DisplayName("getSessionObject should propagate a missing-session failure")
        void getSessionObject_shouldPropagateMissingSessionFailure() throws RedisSessionException {
            when(objectStore.getObject(anyString(), anyBoolean(), anyInt()))
                    .thenThrow(new RedisSessionException("Unable to fetch session object from Redis server"));

            RedisSessionException thrown = assertThrows(RedisSessionException.class,
                    () -> sessionObject.getSessionObject(KEY));

            assertEquals("Unable to fetch session object from Redis server", thrown.getMessage());
        }

        @Test
        @DisplayName("updateSessionObject should propagate a missing-session failure")
        void updateSessionObject_shouldPropagateMissingSessionFailure() throws RedisSessionException {
            when(objectStore.updateObject(eq(KEY), eq(VALUE), anyBoolean(), anyInt()))
                    .thenThrow(new RedisSessionException("Unable to fetch session object from Redis server"));

            assertThrows(RedisSessionException.class, () -> sessionObject.updateSessionObject(KEY, VALUE));
        }

        @Test
        @DisplayName("deleteSessionObject should propagate a store failure")
        void deleteSessionObject_shouldPropagateStoreFailure() throws RedisSessionException {
            when(objectStore.deleteObject(KEY)).thenThrow(new RedisSessionException("redis down"));

            assertThrows(RedisSessionException.class, () -> sessionObject.deleteSessionObject(KEY));
        }
    }
}
