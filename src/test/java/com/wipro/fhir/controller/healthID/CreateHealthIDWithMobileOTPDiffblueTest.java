package com.wipro.fhir.controller.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CreateHealthIDWithMobileOTPDiffblueTest {
    /**
     * Method under test:
     * {@link CreateHealthIDWithMobileOTP#generateOTP(String, String)}
     */
    @Test
    void testGenerateOTP() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithMobileOTP()).generateOTP(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithMobileOTP#verifyOTPAndGenerateHealthID(String, String)}
     */
    @Test
    void testVerifyOTPAndGenerateHealthID() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithMobileOTP()).verifyOTPAndGenerateHealthID(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithMobileOTP#mapHealthIDToBeneficiary(String, String)}
     */
    @Test
    void testMapHealthIDToBeneficiary() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithMobileOTP()).mapHealthIDToBeneficiary(null, "foo"));
    }

    /**
     * Method under test: {@link CreateHealthIDWithMobileOTP#getBenhealthID(String)}
     */
    @Test
    void testGetBenhealthID() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Value Coming of type java.lang.String cannot be converted to"
                        + " JSONObject\",\"status\":\"Value Coming of type java.lang.String cannot be converted to JSONObject\"}",
                (new CreateHealthIDWithMobileOTP()).getBenhealthID("Coming Request"));
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Cannot invoke \\\"String.length()\\\" because \\\"this.in\\\" is"
                        + " null\",\"status\":\"Cannot invoke \\\"String.length()\\\" because \\\"this.in\\\" is null\"}",
                (new CreateHealthIDWithMobileOTP()).getBenhealthID(null));
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"End of input at character 0 of \",\"status\":\"End of input at character"
                        + " 0 of \"}",
                (new CreateHealthIDWithMobileOTP()).getBenhealthID("﻿"));
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Value foo of type java.lang.String cannot be converted to"
                        + " JSONObject\",\"status\":\"Value foo of type java.lang.String cannot be converted to JSONObject\"}",
                (new CreateHealthIDWithMobileOTP()).getBenhealthID("foo"));
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Value 42 of type java.lang.Integer cannot be converted to"
                        + " JSONObject\",\"status\":\"Value 42 of type java.lang.Integer cannot be converted to JSONObject\"}",
                (new CreateHealthIDWithMobileOTP()).getBenhealthID("42"));
    }
}
