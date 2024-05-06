package com.wipro.fhir.data.request_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserAuthDataDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserAuthData#setIsAuthenticated(Boolean)}
     *   <li>{@link UserAuthData#setKey(String)}
     *   <li>{@link UserAuthData#setStatus(String)}
     *   <li>{@link UserAuthData#getIsAuthenticated()}
     *   <li>{@link UserAuthData#getKey()}
     *   <li>{@link UserAuthData#getStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        UserAuthData userAuthData = new UserAuthData();

        // Act
        userAuthData.setIsAuthenticated(true);
        userAuthData.setKey("Key");
        userAuthData.setStatus("Status");
        Boolean actualIsAuthenticated = userAuthData.getIsAuthenticated();
        String actualKey = userAuthData.getKey();

        // Assert that nothing has changed
        assertEquals("Key", actualKey);
        assertEquals("Status", userAuthData.getStatus());
        assertTrue(actualIsAuthenticated);
    }
}
