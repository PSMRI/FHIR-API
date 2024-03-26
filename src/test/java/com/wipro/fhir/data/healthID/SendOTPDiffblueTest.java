package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SendOTPDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SendOTP#setAadhaar(String)}
     *   <li>{@link SendOTP#setMobile(String)}
     *   <li>{@link SendOTP#getAadhaar()}
     *   <li>{@link SendOTP#getMobile()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        SendOTP sendOTP = new SendOTP();

        // Act
        sendOTP.setAadhaar("Aadhaar");
        sendOTP.setMobile("Mobile");
        String actualAadhaar = sendOTP.getAadhaar();

        // Assert that nothing has changed
        assertEquals("Aadhaar", actualAadhaar);
        assertEquals("Mobile", sendOTP.getMobile());
    }
}
