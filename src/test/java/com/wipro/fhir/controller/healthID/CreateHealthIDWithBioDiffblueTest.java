package com.wipro.fhir.controller.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CreateHealthIDWithBioDiffblueTest {
    /**
     * Method under test: {@link CreateHealthIDWithBio#verifyBio(String, String)}
     */
    @Test
    void testVerifyBio() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithBio()).verifyBio(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithBio#checkAndGenerateMobileOTP(String, String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithBio()).checkAndGenerateMobileOTP(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithBio#confirmWithAadhaarBio(String, String)}
     */
    @Test
    void testConfirmWithAadhaarBio() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithBio()).confirmWithAadhaarBio(null, "foo"));
    }
}
