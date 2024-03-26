package com.wipro.fhir.data.request_handler;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class UserAuthAPIResponseDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link UserAuthAPIResponse#setData(UserAuthData)}
     *   <li>{@link UserAuthAPIResponse#getData()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        UserAuthAPIResponse userAuthAPIResponse = new UserAuthAPIResponse();

        UserAuthData data = new UserAuthData();
        data.setIsAuthenticated(true);
        data.setKey("Key");
        data.setStatus("Status");

        // Act
        userAuthAPIResponse.setData(data);

        // Assert that nothing has changed
        assertSame(data, userAuthAPIResponse.getData());
    }
}
