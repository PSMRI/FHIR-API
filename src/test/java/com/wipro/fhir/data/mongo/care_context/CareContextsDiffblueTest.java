package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CareContextsDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CareContexts#setDisplay(String)}
     *   <li>{@link CareContexts#setReferenceNumber(String)}
     *   <li>{@link CareContexts#getDisplay()}
     *   <li>{@link CareContexts#getReferenceNumber()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        CareContexts careContexts = new CareContexts();

        // Act
        careContexts.setDisplay("Display");
        careContexts.setReferenceNumber("42");
        String actualDisplay = careContexts.getDisplay();

        // Assert that nothing has changed
        assertEquals("42", careContexts.getReferenceNumber());
        assertEquals("Display", actualDisplay);
    }
}
