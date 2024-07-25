package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HIPDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link HIP#HIP(String, String)}
     *   <li>{@link HIP#setId(String)}
     *   <li>{@link HIP#setName(String)}
     *   <li>{@link HIP#getId()}
     *   <li>{@link HIP#getName()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        HIP actualHip = new HIP("Name", "42");
        actualHip.setId("42");
        actualHip.setName("Name");
        String actualId = actualHip.getId();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Name", actualHip.getName());
    }
}
