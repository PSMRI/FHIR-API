package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NDHMResponseDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link NDHMResponse#setId(String)}
     *   <li>{@link NDHMResponse#setRequestID(String)}
     *   <li>{@link NDHMResponse#setResponseData(String)}
     *   <li>{@link NDHMResponse#getId()}
     *   <li>{@link NDHMResponse#getRequestID()}
     *   <li>{@link NDHMResponse#getResponseData()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        NDHMResponse ndhmResponse = new NDHMResponse();

        // Act
        ndhmResponse.setId("42");
        ndhmResponse.setRequestID("Request ID");
        ndhmResponse.setResponseData("Response Data");
        String actualId = ndhmResponse.getId();
        String actualRequestID = ndhmResponse.getRequestID();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Request ID", actualRequestID);
        assertEquals("Response Data", ndhmResponse.getResponseData());
    }
}
