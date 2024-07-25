package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MobileOTPDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link MobileOTP#setMobileNumber(String)}
     *   <li>{@link MobileOTP#setTxnId(String)}
     *   <li>{@link MobileOTP#getMobileNumber()}
     *   <li>{@link MobileOTP#getTxnId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        MobileOTP mobileOTP = new MobileOTP();

        // Act
        mobileOTP.setMobileNumber("Mobile");
        mobileOTP.setTxnId("42");
        String actualMobileNumber = mobileOTP.getMobileNumber();

        // Assert that nothing has changed
        assertEquals("42", mobileOTP.getTxnId());
        assertEquals("Mobile", actualMobileNumber);
    }
}
