package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthorizeDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Authorize#setClientId(String)}
     *   <li>{@link Authorize#setClientSecret(String)}
     *   <li>{@link Authorize#getClientId()}
     *   <li>{@link Authorize#getClientSecret()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Authorize authorize = new Authorize();

        // Act
        authorize.setClientId("42");
        authorize.setClientSecret("Client Secret");
        String actualClientId = authorize.getClientId();

        // Assert that nothing has changed
        assertEquals("42", actualClientId);
        assertEquals("Client Secret", authorize.getClientSecret());
    }
}
