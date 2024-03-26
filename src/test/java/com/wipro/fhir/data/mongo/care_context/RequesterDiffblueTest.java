package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RequesterDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Requester#setId(String)}
     *   <li>{@link Requester#setType(String)}
     *   <li>{@link Requester#getId()}
     *   <li>{@link Requester#getType()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Requester requester = new Requester();

        // Act
        requester.setId("42");
        requester.setType("Type");
        String actualId = requester.getId();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Type", requester.getType());
    }
}
