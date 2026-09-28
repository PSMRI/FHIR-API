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
package com.wipro.fhir.utils.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wipro.fhir.utils.redis.RedisSessionException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("AMRIT exception types Test Suite")
class ExceptionsTest {

    private static final String MESSAGE = "Invalid session key";

    private RuntimeException causeWithStackTrace() {
        RuntimeException cause = new RuntimeException("root cause");
        cause.setStackTrace(new StackTraceElement[] {
                new StackTraceElement("com.iemr.Origin", "failingMethod", "Origin.java", 42) });
        return cause;
    }

    @Nested
    @DisplayName("FHIRException")
    class FHIRExceptionTests {

        @Test
        @DisplayName("the message constructor should expose the message through both accessors")
        void messageConstructor_shouldExposeMessage() {
            FHIRException exception = new FHIRException(MESSAGE);

            assertEquals(MESSAGE, exception.getMessage());
            assertEquals(MESSAGE, exception.toString());
        }

        @Test
        @DisplayName("the cause constructor should adopt the stack trace of the cause")
        void causeConstructor_shouldAdoptCauseStackTrace() {
            RuntimeException cause = causeWithStackTrace();

            FHIRException exception = new FHIRException(MESSAGE, cause);

            assertEquals(MESSAGE, exception.getMessage());
            assertArrayEquals(cause.getStackTrace(), exception.getStackTrace());
        }

        @Test
        @DisplayName("the cause constructor should not chain the cause itself")
        void causeConstructor_shouldNotChainCause() {
            FHIRException exception = new FHIRException(MESSAGE, causeWithStackTrace());

            assertNull(exception.getCause(),
                    "only the stack trace is adopted; the cause is deliberately not chained");
        }

        @Test
        @DisplayName("toString should return null when constructed with a null message")
        void toString_shouldReturnNullForNullMessage() {
            assertNull(new FHIRException(null).toString());
        }
    }

    @Nested
    @DisplayName("RedisSessionException")
    class RedisSessionExceptionTests {

        @Test
        @DisplayName("the message constructor should expose the message through both accessors")
        void messageConstructor_shouldExposeMessage() {
            RedisSessionException exception = new RedisSessionException(MESSAGE);

            assertEquals(MESSAGE, exception.getMessage());
            assertEquals(MESSAGE, exception.toString());
        }

        @Test
        @DisplayName("the cause constructor should adopt the stack trace of the cause")
        void causeConstructor_shouldAdoptCauseStackTrace() {
            RuntimeException cause = causeWithStackTrace();

            RedisSessionException exception = new RedisSessionException(MESSAGE, cause);

            assertEquals(MESSAGE, exception.getMessage());
            assertArrayEquals(cause.getStackTrace(), exception.getStackTrace());
        }

        @Test
        @DisplayName("a RedisSessionException should also be a FHIRException")
        void redisSessionException_shouldExtendFhirException() {
            assertInstanceOf(FHIRException.class, new RedisSessionException(MESSAGE));
        }
    }
}
