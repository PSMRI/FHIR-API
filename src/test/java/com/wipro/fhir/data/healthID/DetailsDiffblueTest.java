package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class DetailsDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Details#setAttribute(Attribute)}
     *   <li>{@link Details#setCode(String)}
     *   <li>{@link Details#setMessage(String)}
     *   <li>{@link Details#getAttribute()}
     *   <li>{@link Details#getCode()}
     *   <li>{@link Details#getMessage()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Details details = new Details();

        Attribute attribute = new Attribute();
        attribute.setKey("Key");
        attribute.setValue("42");

        // Act
        details.setAttribute(attribute);
        details.setCode("Code");
        details.setMessage("Not all who wander are lost");
        Attribute actualAttribute = details.getAttribute();
        String actualCode = details.getCode();

        // Assert that nothing has changed
        assertEquals("Code", actualCode);
        assertEquals("Not all who wander are lost", details.getMessage());
        assertSame(attribute, actualAttribute);
    }
}
