package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AttributeDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Attribute#setKey(String)}
     *   <li>{@link Attribute#setValue(String)}
     *   <li>{@link Attribute#getKey()}
     *   <li>{@link Attribute#getValue()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Attribute attribute = new Attribute();

        // Act
        attribute.setKey("Key");
        attribute.setValue("42");
        String actualKey = attribute.getKey();

        // Assert that nothing has changed
        assertEquals("42", attribute.getValue());
        assertEquals("Key", actualKey);
    }
}
