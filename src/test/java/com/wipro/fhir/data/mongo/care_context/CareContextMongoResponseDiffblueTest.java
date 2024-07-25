package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CareContextMongoResponseDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CareContextMongoResponse#setId(String)}
     *   <li>{@link CareContextMongoResponse#setRequestid(String)}
     *   <li>{@link CareContextMongoResponse#setResponsedata(String)}
     *   <li>{@link CareContextMongoResponse#getId()}
     *   <li>{@link CareContextMongoResponse#getRequestid()}
     *   <li>{@link CareContextMongoResponse#getResponsedata()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        CareContextMongoResponse careContextMongoResponse = new CareContextMongoResponse();

        // Act
        careContextMongoResponse.setId("42");
        careContextMongoResponse.setRequestid("Requestid");
        careContextMongoResponse.setResponsedata("Responsedata");
        String actualId = careContextMongoResponse.getId();
        String actualRequestid = careContextMongoResponse.getRequestid();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Requestid", actualRequestid);
        assertEquals("Responsedata", careContextMongoResponse.getResponsedata());
    }
}
