package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SendOTPForCardDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SendOTPForCard#setAuthMethod(String)}
     *   <li>{@link SendOTPForCard#setHealthIdNumber(String)}
     *   <li>{@link SendOTPForCard#setHealthid(String)}
     *   <li>{@link SendOTPForCard#getAuthMethod()}
     *   <li>{@link SendOTPForCard#getHealthIdNumber()}
     *   <li>{@link SendOTPForCard#getHealthid()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        SendOTPForCard sendOTPForCard = new SendOTPForCard();

        // Act
        sendOTPForCard.setAuthMethod("Auth Method");
        sendOTPForCard.setHealthIdNumber("42");
        sendOTPForCard.setHealthid("Healthid");
        String actualAuthMethod = sendOTPForCard.getAuthMethod();
        String actualHealthIdNumber = sendOTPForCard.getHealthIdNumber();

        // Assert that nothing has changed
        assertEquals("42", actualHealthIdNumber);
        assertEquals("Auth Method", actualAuthMethod);
        assertEquals("Healthid", sendOTPForCard.getHealthid());
    }
}
