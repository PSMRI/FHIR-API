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

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("UserAgentContext Test Suite")
class UserAgentContextTest {

    @AfterEach
    @DisplayName("Clear the thread local after each test")
    void tearDown() {
        UserAgentContext.clear();
    }

    @Test
    @DisplayName("getUserAgent should be empty before anything is set")
    void getUserAgent_shouldBeEmptyByDefault() {
        assertNull(UserAgentContext.getUserAgent());
    }

    @Test
    @DisplayName("setUserAgent should make the value readable on the same thread")
    void setUserAgent_shouldBeReadableOnSameThread() {
        UserAgentContext.setUserAgent("okhttp/4.9.0");

        assertEquals("okhttp/4.9.0", UserAgentContext.getUserAgent());
    }

    @Test
    @DisplayName("setUserAgent should overwrite a previously stored value")
    void setUserAgent_shouldOverwritePreviousValue() {
        UserAgentContext.setUserAgent("okhttp/4.9.0");
        UserAgentContext.setUserAgent("Java/17.0.2");

        assertEquals("Java/17.0.2", UserAgentContext.getUserAgent());
    }

    @Test
    @DisplayName("clear should remove the stored value")
    void clear_shouldRemoveStoredValue() {
        UserAgentContext.setUserAgent("okhttp/4.9.0");

        UserAgentContext.clear();

        assertNull(UserAgentContext.getUserAgent());
    }

    @Test
    @DisplayName("the stored value should not leak into another thread")
    void storedValue_shouldNotLeakAcrossThreads() throws Exception {
        UserAgentContext.setUserAgent("okhttp/4.9.0");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> otherThreadValue = executor.submit(UserAgentContext::getUserAgent);

        assertNull(otherThreadValue.get(), "the User-Agent is per-request, so must stay thread-confined");
        executor.shutdown();
    }
}
