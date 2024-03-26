package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GenerateOTPForCareContextAndValidateDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>
     * {@link GenerateOTPForCareContextAndValidate#setAuthenticationMode(String)}
     *   <li>{@link GenerateOTPForCareContextAndValidate#setHealthID(String)}
     *   <li>{@link GenerateOTPForCareContextAndValidate#setHealthIdNumber(String)}
     *   <li>{@link GenerateOTPForCareContextAndValidate#getAuthenticationMode()}
     *   <li>{@link GenerateOTPForCareContextAndValidate#getHealthID()}
     *   <li>{@link GenerateOTPForCareContextAndValidate#getHealthIdNumber()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        GenerateOTPForCareContextAndValidate generateOTPForCareContextAndValidate = new GenerateOTPForCareContextAndValidate();

        // Act
        generateOTPForCareContextAndValidate.setAuthenticationMode("Authentication Mode");
        generateOTPForCareContextAndValidate.setHealthID("Health ID");
        generateOTPForCareContextAndValidate.setHealthIdNumber("42");
        String actualAuthenticationMode = generateOTPForCareContextAndValidate.getAuthenticationMode();
        String actualHealthID = generateOTPForCareContextAndValidate.getHealthID();

        // Assert that nothing has changed
        assertEquals("42", generateOTPForCareContextAndValidate.getHealthIdNumber());
        assertEquals("Authentication Mode", actualAuthenticationMode);
        assertEquals("Health ID", actualHealthID);
    }
}
