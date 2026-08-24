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

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenDenylist Test Suite")
class TokenDenylistTest {

    private static final String JTI = "jti-1";
    private static final String KEY = "denied_jti-1";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private TokenDenylist tokenDenylist;

    @BeforeEach
    @DisplayName("Wire the denylist with a mocked Redis template before each test")
    void setUp() {
        tokenDenylist = new TokenDenylist();
        ReflectionTestUtils.setField(tokenDenylist, "redisTemplate", redisTemplate);
    }

    @Nested
    @DisplayName("addTokenToDenylist")
    class AddTokenTests {

        @Test
        @DisplayName("addTokenToDenylist should store the prefixed key with the supplied expiry")
        void addTokenToDenylist_shouldStorePrefixedKeyWithExpiry() {
            when(redisTemplate.opsForValue()).thenReturn(valueOperations);

            tokenDenylist.addTokenToDenylist(JTI, 60_000L);

            verify(valueOperations).set(KEY, " ", 60_000L, TimeUnit.MILLISECONDS);
        }

        @Test
        @DisplayName("addTokenToDenylist should ignore a null jti")
        void addTokenToDenylist_shouldIgnoreNullJti() {
            tokenDenylist.addTokenToDenylist(null, 60_000L);

            verifyNoInteractions(redisTemplate);
        }

        @Test
        @DisplayName("addTokenToDenylist should ignore a blank jti")
        void addTokenToDenylist_shouldIgnoreBlankJti() {
            tokenDenylist.addTokenToDenylist("   ", 60_000L);

            verifyNoInteractions(redisTemplate);
        }

        @Test
        @DisplayName("addTokenToDenylist should reject a null expiry")
        void addTokenToDenylist_shouldRejectNullExpiry() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                    () -> tokenDenylist.addTokenToDenylist(JTI, null));

            assertTrue(thrown.getMessage().contains("Expiration time must be positive"));
            verifyNoInteractions(redisTemplate);
        }

        @Test
        @DisplayName("addTokenToDenylist should reject a non-positive expiry")
        void addTokenToDenylist_shouldRejectNonPositiveExpiry() {
            assertThrows(IllegalArgumentException.class, () -> tokenDenylist.addTokenToDenylist(JTI, 0L));
            assertThrows(IllegalArgumentException.class, () -> tokenDenylist.addTokenToDenylist(JTI, -5L));
            verifyNoInteractions(redisTemplate);
        }

        @Test
        @DisplayName("addTokenToDenylist should surface a Redis failure as a runtime exception")
        void addTokenToDenylist_shouldSurfaceRedisFailure() {
            when(redisTemplate.opsForValue()).thenReturn(valueOperations);
            doThrow(new IllegalStateException("redis down"))
                    .when(valueOperations).set(anyString(), any(), anyLong(), any(TimeUnit.class));

            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> tokenDenylist.addTokenToDenylist(JTI, 60_000L));

            assertTrue(thrown.getMessage().contains("Failed to denylist token"));
        }
    }

    @Nested
    @DisplayName("isTokenDenylisted")
    class IsTokenDenylistedTests {

        @Test
        @DisplayName("isTokenDenylisted should report true when the prefixed key exists")
        void isTokenDenylisted_shouldReportTrueWhenKeyExists() {
            when(redisTemplate.hasKey(KEY)).thenReturn(true);

            assertTrue(tokenDenylist.isTokenDenylisted(JTI));
        }

        @Test
        @DisplayName("isTokenDenylisted should report false when the key does not exist")
        void isTokenDenylisted_shouldReportFalseWhenKeyAbsent() {
            when(redisTemplate.hasKey(KEY)).thenReturn(false);

            assertFalse(tokenDenylist.isTokenDenylisted(JTI));
        }

        @Test
        @DisplayName("isTokenDenylisted should report false when Redis answers null")
        void isTokenDenylisted_shouldReportFalseWhenRedisAnswersNull() {
            when(redisTemplate.hasKey(KEY)).thenReturn(null);

            assertFalse(tokenDenylist.isTokenDenylisted(JTI));
        }

        @Test
        @DisplayName("isTokenDenylisted should report false for a null jti without touching Redis")
        void isTokenDenylisted_shouldReportFalseForNullJti() {
            assertFalse(tokenDenylist.isTokenDenylisted(null));

            verify(redisTemplate, never()).hasKey(anyString());
        }

        @Test
        @DisplayName("isTokenDenylisted should report false for a blank jti without touching Redis")
        void isTokenDenylisted_shouldReportFalseForBlankJti() {
            assertFalse(tokenDenylist.isTokenDenylisted("  "));

            verify(redisTemplate, never()).hasKey(anyString());
        }

        @Test
        @DisplayName("isTokenDenylisted should fail open rather than block requests when Redis is down")
        void isTokenDenylisted_shouldFailOpenWhenRedisIsDown() {
            when(redisTemplate.hasKey(KEY)).thenThrow(new IllegalStateException("redis down"));

            assertFalse(tokenDenylist.isTokenDenylisted(JTI));
        }
    }
}
