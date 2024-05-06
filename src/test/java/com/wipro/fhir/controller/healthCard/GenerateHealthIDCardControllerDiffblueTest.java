package com.wipro.fhir.controller.healthCard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GenerateHealthIDCardControllerDiffblueTest {
    /**
     * Method under test:
     * {@link GenerateHealthIDCardController#mapHealthIDToBeneficiary(String, String)}
     */
    @Test
    void testMapHealthIDToBeneficiary() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new GenerateHealthIDCardController()).mapHealthIDToBeneficiary(null, "foo"));
    }

    /**
     * Method under test:
     * {@link GenerateHealthIDCardController#verifyOTPAndGenerateHealthCard(String, String)}
     */
    @Test
    void testVerifyOTPAndGenerateHealthCard() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new GenerateHealthIDCardController()).verifyOTPAndGenerateHealthCard(null, "foo"));
    }
}
