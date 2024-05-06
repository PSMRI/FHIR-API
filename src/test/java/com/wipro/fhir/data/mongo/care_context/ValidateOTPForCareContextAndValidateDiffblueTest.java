package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ValidateOTPForCareContextAndValidateDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ValidateOTPForCareContextAndValidate#setHealthID(String)}
     *   <li>{@link ValidateOTPForCareContextAndValidate#setOtp(String)}
     *   <li>{@link ValidateOTPForCareContextAndValidate#setTxnId(String)}
     *   <li>{@link ValidateOTPForCareContextAndValidate#getHealthID()}
     *   <li>{@link ValidateOTPForCareContextAndValidate#getOtp()}
     *   <li>{@link ValidateOTPForCareContextAndValidate#getTxnId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ValidateOTPForCareContextAndValidate validateOTPForCareContextAndValidate = new ValidateOTPForCareContextAndValidate();

        // Act
        validateOTPForCareContextAndValidate.setHealthID("Health ID");
        validateOTPForCareContextAndValidate.setOtp("Otp");
        validateOTPForCareContextAndValidate.setTxnId("42");
        String actualHealthID = validateOTPForCareContextAndValidate.getHealthID();
        String actualOtp = validateOTPForCareContextAndValidate.getOtp();

        // Assert that nothing has changed
        assertEquals("42", validateOTPForCareContextAndValidate.getTxnId());
        assertEquals("Health ID", actualHealthID);
        assertEquals("Otp", actualOtp);
    }
}
