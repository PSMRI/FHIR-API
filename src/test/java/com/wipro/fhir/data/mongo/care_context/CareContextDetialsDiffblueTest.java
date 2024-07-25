package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CareContextDetialsDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CareContextDetials#setDisplay(String)}
     *   <li>{@link CareContextDetials#setReferenceNumber(String)}
     *   <li>{@link CareContextDetials#getDisplay()}
     *   <li>{@link CareContextDetials#getReferenceNumber()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        CareContextDetials careContextDetials = new CareContextDetials();

        // Act
        careContextDetials.setDisplay("Display");
        careContextDetials.setReferenceNumber("42");
        String actualDisplay = careContextDetials.getDisplay();

        // Assert that nothing has changed
        assertEquals("42", careContextDetials.getReferenceNumber());
        assertEquals("Display", actualDisplay);
    }
}
