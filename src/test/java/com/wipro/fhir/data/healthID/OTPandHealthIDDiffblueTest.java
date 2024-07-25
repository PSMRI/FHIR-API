package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OTPandHealthIDDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OTPandHealthID#setAuthMethod(String)}
     *   <li>{@link OTPandHealthID#setOtp(String)}
     *   <li>{@link OTPandHealthID#setTxnId(String)}
     *   <li>{@link OTPandHealthID#getAuthMethod()}
     *   <li>{@link OTPandHealthID#getOtp()}
     *   <li>{@link OTPandHealthID#getTxnId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OTPandHealthID otPandHealthID = new OTPandHealthID();

        // Act
        otPandHealthID.setAuthMethod("Auth Method");
        otPandHealthID.setOtp("Otp");
        otPandHealthID.setTxnId("42");
        String actualAuthMethod = otPandHealthID.getAuthMethod();
        String actualOtp = otPandHealthID.getOtp();

        // Assert that nothing has changed
        assertEquals("42", otPandHealthID.getTxnId());
        assertEquals("Auth Method", actualAuthMethod);
        assertEquals("Otp", actualOtp);
    }
}
