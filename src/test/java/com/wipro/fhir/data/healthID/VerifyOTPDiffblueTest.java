package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VerifyOTPDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link VerifyOTP#setOtp(String)}
     *   <li>{@link VerifyOTP#setTxnId(String)}
     *   <li>{@link VerifyOTP#getOtp()}
     *   <li>{@link VerifyOTP#getTxnId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        VerifyOTP verifyOTP = new VerifyOTP();

        // Act
        verifyOTP.setOtp("Otp");
        verifyOTP.setTxnId("42");
        String actualOtp = verifyOTP.getOtp();

        // Assert that nothing has changed
        assertEquals("42", verifyOTP.getTxnId());
        assertEquals("Otp", actualOtp);
    }
}
