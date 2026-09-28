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
package com.wipro.fhir.utils.response;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OutputResponse (response package) Test Suite")
class OutputResponseTest {

    private OutputResponse outputResponse;

    @BeforeEach
    @DisplayName("Create a fresh response object before each test")
    void setUp() {
        outputResponse = new OutputResponse();
    }

    @Nested
    @DisplayName("Default state")
    class DefaultStateTests {

        @Test
        @DisplayName("a new response should default to a generic failure")
        void newResponse_shouldDefaultToGenericFailure() {
            assertEquals(OutputResponse.GENERIC_FAILURE, outputResponse.getStatusCode());
            assertEquals("Failed with generic error", outputResponse.getErrorMessage());
            assertEquals("FAILURE", outputResponse.getStatus());
            assertFalse(outputResponse.isSuccess());
        }

        @Test
        @DisplayName("getData should return null when no data has been set")
        void getData_shouldReturnNullWhenNoDataSet() {
            assertNull(outputResponse.getData());
        }
    }

    @Nested
    @DisplayName("setResponse")
    class SetResponseTests {

        @Test
        @DisplayName("setResponse should mark the response successful")
        void setResponse_shouldMarkResponseSuccessful() {
            outputResponse.setResponse("done");

            assertEquals(OutputResponse.SUCCESS, outputResponse.getStatusCode());
            assertEquals("Success", outputResponse.getErrorMessage());
            assertEquals("Success", outputResponse.getStatus());
            assertTrue(outputResponse.isSuccess());
        }

        @Test
        @DisplayName("setResponse should keep a JSON object payload as an object")
        void setResponse_shouldKeepJsonObjectPayload() {
            outputResponse.setResponse("{\"beneficiaryId\":12345}");

            assertTrue(outputResponse.getData().contains("\"beneficiaryId\""));
            assertTrue(outputResponse.getData().startsWith("{"));
        }

        @Test
        @DisplayName("setResponse should keep a JSON array payload as an array")
        void setResponse_shouldKeepJsonArrayPayload() {
            outputResponse.setResponse("[1,2,3]");

            assertTrue(outputResponse.getData().startsWith("["));
            assertTrue(outputResponse.getData().contains("1"));
        }

        @Test
        @DisplayName("setResponse should wrap a plain string payload under a response key")
        void setResponse_shouldWrapPlainStringPayload() {
            outputResponse.setResponse("plain text");

            assertTrue(outputResponse.getData().contains("response"));
            assertTrue(outputResponse.getData().contains("plain text"));
        }

        @Test
        @DisplayName("toString should serialise the exposed fields as JSON")
        void toString_shouldSerialiseExposedFields() {
            outputResponse.setResponse("done");

            String json = outputResponse.toString();

            assertTrue(json.contains("\"statusCode\":200"));
            assertTrue(json.contains("\"status\":\"Success\""));
            assertTrue(json.contains("\"errorMessage\":\"Success\""));
            assertTrue(json.contains("\"data\""));
        }

        @Test
        @DisplayName("toString should omit null fields while toStringWithSerialization keeps them")
        void toString_shouldOmitNullsUnlikeToStringWithSerialization() {
            assertFalse(outputResponse.toString().contains("\"data\""));
            assertTrue(outputResponse.toStringWithSerialization().contains("\"data\":null"));
        }
    }

    @Nested
    @DisplayName("setError with an explicit code")
    class SetErrorWithCodeTests {

        @Test
        @DisplayName("setError should apply the supplied code, message and status")
        void setError_shouldApplySuppliedCodeMessageAndStatus() {
            outputResponse.setError(OutputResponse.PREVILAGE_FAILURE, "not permitted", "PRIVILEGE");

            assertEquals(OutputResponse.PREVILAGE_FAILURE, outputResponse.getStatusCode());
            assertEquals("not permitted", outputResponse.getErrorMessage());
            assertEquals("PRIVILEGE", outputResponse.getStatus());
            assertFalse(outputResponse.isSuccess());
        }

        @Test
        @DisplayName("setError should reuse the message as the status when only a message is supplied")
        void setError_shouldReuseMessageAsStatus() {
            outputResponse.setError(OutputResponse.PASSWORD_FAILURE, "bad password");

            assertEquals(OutputResponse.PASSWORD_FAILURE, outputResponse.getStatusCode());
            assertEquals("bad password", outputResponse.getErrorMessage());
            assertEquals("bad password", outputResponse.getStatus());
        }
    }

    @Nested
    @DisplayName("setError mapped from a throwable")
    class SetErrorFromThrowableTests {

        @Test
        @DisplayName("setError should map an unmapped FHIRException to the generic failure")
        void setError_shouldMapFhirExceptionToGenericFailure() {
            outputResponse.setError(new FHIRException("invalid credentials"));

            assertEquals(OutputResponse.GENERIC_FAILURE, outputResponse.getStatusCode());
            assertTrue(outputResponse.getStatus().startsWith("Failed with invalid credentials at "));
            assertEquals("invalid credentials", outputResponse.getErrorMessage());
        }

        @Test
        @DisplayName("setError should map JSONException to an object conversion failure")
        void setError_shouldMapJsonExceptionToObjectFailure() {
            outputResponse.setError(new JSONException("bad json"));

            assertEquals(OutputResponse.OBJECT_FAILURE, outputResponse.getStatusCode());
            assertEquals("Invalid object conversion", outputResponse.getStatus());
            assertEquals("Invalid object conversion", outputResponse.getErrorMessage());
        }

        @Test
        @DisplayName("setError should map SQLException to a code exception")
        void setError_shouldMapSqlExceptionToCodeException() {
            outputResponse.setError(new SQLException("deadlock"));

            assertEquals(OutputResponse.CODE_EXCEPTION, outputResponse.getStatusCode());
            assertTrue(outputResponse.getStatus().startsWith("Failed with internal errors at "));
            assertEquals("deadlock", outputResponse.getErrorMessage());
        }

        @Test
        @DisplayName("setError should map NullPointerException to a code exception")
        void setError_shouldMapNullPointerExceptionToCodeException() {
            outputResponse.setError(new NullPointerException("npe"));

            assertEquals(OutputResponse.CODE_EXCEPTION, outputResponse.getStatusCode());
        }

        @Test
        @DisplayName("setError should map ParseException to a code exception")
        void setError_shouldMapParseExceptionToCodeException() {
            outputResponse.setError(new ParseException("bad date", 0));

            assertEquals(OutputResponse.CODE_EXCEPTION, outputResponse.getStatusCode());
        }

        @Test
        @DisplayName("setError should map an unlisted runtime exception to the generic failure")
        void setError_shouldMapArrayIndexExceptionToGenericFailure() {
            outputResponse.setError(new ArrayIndexOutOfBoundsException("index 5"));

            assertEquals(OutputResponse.GENERIC_FAILURE, outputResponse.getStatusCode());
            assertEquals("index 5", outputResponse.getErrorMessage());
        }

        @Test
        @DisplayName("setError should map IOException to an environment exception")
        void setError_shouldMapIoExceptionToEnvironmentException() {
            outputResponse.setError(new IOException("disk full"));

            assertEquals(OutputResponse.ENVIRONMENT_EXCEPTION, outputResponse.getStatusCode());
            assertTrue(outputResponse.getStatus().startsWith("Failed with connection issues at "));
            assertEquals("disk full", outputResponse.getErrorMessage());
        }

        @Test
        @DisplayName("setError should map ConnectException to an environment exception")
        void setError_shouldMapConnectExceptionToEnvironmentException() {
            outputResponse.setError(new ConnectException("refused"));

            assertEquals(OutputResponse.ENVIRONMENT_EXCEPTION, outputResponse.getStatusCode());
        }

        @Test
        @DisplayName("setError should fall back to a generic failure for an unmapped exception")
        void setError_shouldFallBackToGenericFailureForUnmappedException() {
            outputResponse.setError(new IllegalStateException("something odd"));

            assertEquals(OutputResponse.GENERIC_FAILURE, outputResponse.getStatusCode());
            assertTrue(outputResponse.getStatus().startsWith("Failed with something odd at "));
            assertEquals("something odd", outputResponse.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("Error mapping for exception types raised by other AMRIT modules")
    class ExternalExceptionMappingTests {

        // setError switches on getClass().getSimpleName(), so locally declared types with
        // the same simple names reach the arms meant for Hibernate/JDBC exceptions.
        private static class JDBCException extends Exception {
            JDBCException(String message) {
                super(message);
            }
        }

        private static class SQLGrammarException extends Exception {
            SQLGrammarException(String message) {
                super(message);
            }
        }

        private static class ConstraintViolationException extends Exception {
            ConstraintViolationException(String message) {
                super(message);
            }
        }

        @Test
        @DisplayName("setError should map a JDBC failure to a DB connection environment error")
        void setError_shouldMapJdbcFailure() {
            outputResponse.setError(new JDBCException("pool exhausted"));

            assertEquals(OutputResponse.ENVIRONMENT_EXCEPTION, outputResponse.getStatusCode());
            assertTrue(outputResponse.getStatus().startsWith("Failed with DB connection issues at "));
            assertEquals("pool exhausted", outputResponse.getErrorMessage());
        }

        @Test
        @DisplayName("setError should map a SQL grammar failure to a code exception")
        void setError_shouldMapSqlGrammarFailure() {
            outputResponse.setError(new SQLGrammarException("bad column"));

            assertEquals(OutputResponse.CODE_EXCEPTION, outputResponse.getStatusCode());
        }

        @Test
        @DisplayName("setError should map an unlisted constraint violation to the generic failure")
        void setError_shouldMapConstraintViolation() {
            outputResponse.setError(new ConstraintViolationException("duplicate key"));

            assertEquals(OutputResponse.GENERIC_FAILURE, outputResponse.getStatusCode());
        }
    }
}
