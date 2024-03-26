package com.wipro.fhir.controller.healthIDvalidate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HealthIDValidateControllerDiffblueTest {
    /**
     * Method under test:
     * {@link HealthIDValidateController#generateOTPForHealthIDValidation(String, String)}
     */
    @Test
    void testGenerateOTPForHealthIDValidation() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new HealthIDValidateController()).generateOTPForHealthIDValidation(null, "foo"));
    }

    /**
     * Method under test:
     * {@link HealthIDValidateController#verifyOTPForHealthIDValidation(String, String)}
     */
    @Test
    void testVerifyOTPForHealthIDValidation() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new HealthIDValidateController()).verifyOTPForHealthIDValidation(null, "foo"));
    }
}
