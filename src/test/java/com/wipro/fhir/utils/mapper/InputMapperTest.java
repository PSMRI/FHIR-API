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
package com.wipro.fhir.utils.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonSyntaxException;

@DisplayName("InputMapper Test Suite")
class InputMapperTest {

    static class TestPojo {
        String name;
        int value;
        Date date;

        public String getName() { return name; }
        public int getValue() { return value; }
        public Date getDate() { return date; }
    }

    @Test
    @DisplayName("Should return valid InputMapper instance from gson factory method")
    void testGsonStaticFactoryMethod() throws Exception {
        InputMapper mapper = InputMapper.gson();
        assertNotNull(mapper);
        assertTrue(mapper instanceof InputMapper);
    }

    @Test
    @DisplayName("Should successfully parse valid JSON to object")
    void testFromJson_validJson() throws Exception {
        InputMapper mapper = InputMapper.gson();
        String json = "{\"name\":\"testName\", \"value\":100}";
        
        TestPojo result = mapper.fromJson(json, TestPojo.class);

        assertNotNull(result);
        assertEquals("testName", result.getName());
        assertEquals(100, result.getValue());
        assertNull(result.getDate());
    }

    @Test
    @DisplayName("Should successfully parse JSON with date field")
    void testFromJson_validJsonWithDate() throws Exception {
        InputMapper mapper = InputMapper.gson();
        String json = "{\"name\":\"itemWithDate\", \"value\":200, \"date\":\"2023-10-26T10:30:45.123\"}";
        
        TestPojo result = mapper.fromJson(json, TestPojo.class);

        assertNotNull(result);
        assertEquals("itemWithDate", result.getName());
        assertEquals(200, result.getValue());
        assertNotNull(result.getDate());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date expectedDate = sdf.parse("2023-10-26T10:30:45.123");
        
        assertEquals(expectedDate.getTime(), result.getDate().getTime());
    }

    @Test
    @DisplayName("Should return null when JSON input is null")
    void testFromJson_nullJson() throws Exception {
        InputMapper mapper = InputMapper.gson();
        String json = null;
        
        TestPojo result = mapper.fromJson(json, TestPojo.class);
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null when JSON input is empty string")
    void testFromJson_emptyJsonString() throws Exception {
        InputMapper mapper = InputMapper.gson();
        String json = "";
        
        // InputMapper's fromJson method (likely catching JsonSyntaxException internally)
        // returns null when given an empty string.
        TestPojo result = mapper.fromJson(json, TestPojo.class);
        assertNull(result);
    }

    @Test
    @DisplayName("Should create object with default values when JSON is empty object")
    void testFromJson_emptyJsonObject() throws Exception {
        InputMapper mapper = InputMapper.gson();
        String json = "{}";
        
        TestPojo result = mapper.fromJson(json, TestPojo.class);
        
        assertNotNull(result);
        assertNull(result.getName());
        assertEquals(0, result.getValue());
        assertNull(result.getDate());
    }

    @Test
    @DisplayName("Should throw JsonSyntaxException when JSON is malformed")
    void testFromJson_malformedJson() throws Exception {
        InputMapper mapper = InputMapper.gson();
        String json = "{\"name\":\"testName\", \"value\":,}";
        
        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> mapper.fromJson(json, TestPojo.class));
        assertNotNull(thrown);
    }
}
