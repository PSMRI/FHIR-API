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
package com.wipro.fhir.utils.redis;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RedisStorage Test Suite")
class RedisStorageTest {

    private static final String KEY = "session-key";
    private static final int EXPIRY_SECONDS = 7200;

    @Mock
    private LettuceConnectionFactory connectionFactory;

    @Mock
    private RedisConnection redisConnection;

    private RedisStorage redisStorage;

    @BeforeEach
    @DisplayName("Wire the store with a mocked Lettuce connection factory before each test")
    void setUp() {
        redisStorage = new RedisStorage();
        ReflectionTestUtils.setField(redisStorage, "connection", connectionFactory);
        when(connectionFactory.getConnection()).thenReturn(redisConnection);
    }

    private byte[] bytes(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @Nested
    @DisplayName("setObject")
    class SetObjectTests {

        @Test
        @DisplayName("setObject should write the value when no session is stored yet")
        void setObject_shouldWriteValueWhenKeyIsAbsent() throws RedisSessionException {
            when(redisConnection.get(bytes(KEY))).thenReturn(null);

            assertEquals(KEY, redisStorage.setObject(KEY, "payload", EXPIRY_SECONDS));
            verify(redisConnection).set(eq(bytes(KEY)), eq(bytes("payload")),
                    eq(Expiration.seconds(EXPIRY_SECONDS)), eq(SetOption.UPSERT));
        }

        @Test
        @DisplayName("setObject should write the value when the stored session is empty")
        void setObject_shouldWriteValueWhenStoredSessionIsEmpty() throws RedisSessionException {
            when(redisConnection.get(bytes(KEY))).thenReturn(bytes(""));

            assertEquals(KEY, redisStorage.setObject(KEY, "payload", EXPIRY_SECONDS));
            verify(redisConnection).set(any(byte[].class), any(byte[].class), any(Expiration.class), any(SetOption.class));
        }

        @Test
        @DisplayName("setObject should leave an existing session untouched")
        void setObject_shouldLeaveExistingSessionUntouched() throws RedisSessionException {
            when(redisConnection.get(bytes(KEY))).thenReturn(bytes("existing"));

            assertEquals(KEY, redisStorage.setObject(KEY, "payload", EXPIRY_SECONDS));
            verify(redisConnection, never()).set(any(byte[].class), any(byte[].class),
                    any(Expiration.class), any(SetOption.class));
        }
    }

    @Nested
    @DisplayName("getObject")
    class GetObjectTests {

        @Test
        @DisplayName("getObject should return the stored session and extend its expiry")
        void getObject_shouldReturnStoredSessionAndExtendExpiry() throws RedisSessionException {
            when(redisConnection.get(bytes(KEY))).thenReturn(bytes("payload"));

            assertEquals("payload", redisStorage.getObject(KEY, true, EXPIRY_SECONDS));
            verify(redisConnection).expire(bytes(KEY), EXPIRY_SECONDS);
        }

        @Test
        @DisplayName("getObject should raise a session exception when the key is absent")
        void getObject_shouldRaiseWhenKeyIsAbsent() {
            when(redisConnection.get(bytes(KEY))).thenReturn(null);

            RedisSessionException thrown = assertThrows(RedisSessionException.class,
                    () -> redisStorage.getObject(KEY, true, EXPIRY_SECONDS));

            assertEquals("Unable to fetch session object from Redis server", thrown.getMessage());
        }

        @Test
        @DisplayName("getObject should raise a session exception when the stored value is blank")
        void getObject_shouldRaiseWhenStoredValueIsBlank() {
            when(redisConnection.get(bytes(KEY))).thenReturn(bytes("   "));

            assertThrows(RedisSessionException.class,
                    () -> redisStorage.getObject(KEY, true, EXPIRY_SECONDS));
            verify(redisConnection, never()).expire(any(byte[].class), any(Long.class));
        }
    }

    @Nested
    @DisplayName("updateObject")
    class UpdateObjectTests {

        @Test
        @DisplayName("updateObject should overwrite an existing session")
        void updateObject_shouldOverwriteExistingSession() throws RedisSessionException {
            when(redisConnection.get(bytes(KEY))).thenReturn(bytes("old"));

            assertEquals(KEY, redisStorage.updateObject(KEY, "new", true, EXPIRY_SECONDS));
            verify(redisConnection).set(eq(bytes(KEY)), eq(bytes("new")),
                    eq(Expiration.seconds(EXPIRY_SECONDS)), eq(SetOption.UPSERT));
        }

        @Test
        @DisplayName("updateObject should raise a session exception when there is nothing to update")
        void updateObject_shouldRaiseWhenKeyIsAbsent() {
            when(redisConnection.get(bytes(KEY))).thenReturn(null);

            RedisSessionException thrown = assertThrows(RedisSessionException.class,
                    () -> redisStorage.updateObject(KEY, "new", true, EXPIRY_SECONDS));

            assertEquals("Unable to fetch session object from Redis server", thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteObject")
    class DeleteObjectTests {

        @Test
        @DisplayName("deleteObject should return the number of keys Redis removed")
        void deleteObject_shouldReturnNumberOfKeysRemoved() throws RedisSessionException {
            when(redisConnection.del(bytes(KEY))).thenReturn(1L);

            assertEquals(1L, redisStorage.deleteObject(KEY));
        }

        @Test
        @DisplayName("deleteObject should return zero when the key was not present")
        void deleteObject_shouldReturnZeroWhenKeyAbsent() throws RedisSessionException {
            when(redisConnection.del(bytes(KEY))).thenReturn(0L);

            assertEquals(0L, redisStorage.deleteObject(KEY));
        }
    }
}
