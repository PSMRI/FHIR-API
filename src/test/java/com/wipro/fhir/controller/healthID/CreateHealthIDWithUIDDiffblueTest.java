package com.wipro.fhir.controller.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CreateHealthIDWithUIDDiffblueTest {
    /**
     * Method under test: {@link CreateHealthIDWithUID#generateOTP(String, String)}
     */
    @Test
    void testGenerateOTP() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithUID()).generateOTP(null, "foo"));
    }

    /**
     * Method under test: {@link CreateHealthIDWithUID#verifyOTP(String, String)}
     */
    @Test
    void testVerifyOTP() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithUID()).verifyOTP(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithUID#checkAndGenerateMobileOTP(String, String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithUID()).checkAndGenerateMobileOTP(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithUID#verifyMobileOTP(String, String)}
     */
    @Test
    void testVerifyMobileOTP() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithUID()).verifyMobileOTP(null, "foo"));
    }

    /**
     * Method under test:
     * {@link CreateHealthIDWithUID#createHealthIDWithUID(String, String)}
     */
    @Test
    void testCreateHealthIDWithUID() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request"
                        + " object\"}",
                (new CreateHealthIDWithUID()).createHealthIDWithUID(null, "foo"));
    }
}
