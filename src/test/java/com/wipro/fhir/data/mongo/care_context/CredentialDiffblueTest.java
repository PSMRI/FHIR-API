package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CredentialDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Credential#setAuthCode(String)}
     *   <li>{@link Credential#getAuthCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Credential credential = new Credential();

        // Act
        credential.setAuthCode("Auth Code");

        // Assert that nothing has changed
        assertEquals("Auth Code", credential.getAuthCode());
    }
}
