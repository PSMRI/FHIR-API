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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

@DisplayName("CommonMain Test Suite")
class CommonMainTest {

    private CommonMain commonMain;

    @BeforeEach
    @DisplayName("Create the bean configuration before each test")
    void setUp() {
        commonMain = new CommonMain();
    }

    @Test
    @DisplayName("configProperties should supply a fresh properties holder on each call")
    void configProperties_shouldSupplyFreshHolder() {
        assertNotNull(commonMain.configProperties());
        assertNotSame(commonMain.configProperties(), commonMain.configProperties());
    }

    @Test
    @DisplayName("redisSession should supply a Spring Session Redis configuration")
    void redisSession_shouldSupplySessionConfiguration() {
        assertNotNull(commonMain.redisSession());
    }

    @Test
    @DisplayName("redisStorage should supply a Redis store bean")
    void redisStorage_shouldSupplyRedisStore() {
        assertNotNull(commonMain.redisStorage());
    }
}
