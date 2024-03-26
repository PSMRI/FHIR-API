package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class QueryDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Query#setAuthMode(String)}
     *   <li>{@link Query#setId(String)}
     *   <li>{@link Query#setPurpose(String)}
     *   <li>{@link Query#setRequester(Requester)}
     *   <li>{@link Query#getAuthMode()}
     *   <li>{@link Query#getId()}
     *   <li>{@link Query#getPurpose()}
     *   <li>{@link Query#getRequester()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Query query = new Query();

        // Act
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");
        query.setRequester(requester);
        String actualAuthMode = query.getAuthMode();
        String actualId = query.getId();
        String actualPurpose = query.getPurpose();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Auth Mode", actualAuthMode);
        assertEquals("Purpose", actualPurpose);
        assertSame(requester, query.getRequester());
    }
}
