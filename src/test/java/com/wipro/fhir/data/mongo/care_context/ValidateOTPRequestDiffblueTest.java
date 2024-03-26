package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class ValidateOTPRequestDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ValidateOTPRequest#setCredential(Credential)}
     *   <li>{@link ValidateOTPRequest#setRequestId(String)}
     *   <li>{@link ValidateOTPRequest#setTimestamp(String)}
     *   <li>{@link ValidateOTPRequest#setTransactionId(String)}
     *   <li>{@link ValidateOTPRequest#getCredential()}
     *   <li>{@link ValidateOTPRequest#getRequestId()}
     *   <li>{@link ValidateOTPRequest#getTimestamp()}
     *   <li>{@link ValidateOTPRequest#getTransactionId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ValidateOTPRequest validateOTPRequest = new ValidateOTPRequest();

        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        // Act
        validateOTPRequest.setCredential(credential);
        validateOTPRequest.setRequestId("42");
        validateOTPRequest.setTimestamp("Timestamp");
        validateOTPRequest.setTransactionId("42");
        Credential actualCredential = validateOTPRequest.getCredential();
        String actualRequestId = validateOTPRequest.getRequestId();
        String actualTimestamp = validateOTPRequest.getTimestamp();

        // Assert that nothing has changed
        assertEquals("42", actualRequestId);
        assertEquals("42", validateOTPRequest.getTransactionId());
        assertEquals("Timestamp", actualTimestamp);
        assertSame(credential, actualCredential);
    }
}
