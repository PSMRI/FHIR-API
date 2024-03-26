package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class HealthIDExceptionDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link HealthIDException#setCode(String)}
     *   <li>{@link HealthIDException#setDetails(Details[])}
     *   <li>{@link HealthIDException#setMessage(String)}
     *   <li>{@link HealthIDException#getCode()}
     *   <li>{@link HealthIDException#getDetails()}
     *   <li>{@link HealthIDException#getMessage()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        HealthIDException healthIDException = new HealthIDException();

        // Act
        healthIDException.setCode("Code");
        Attribute attribute = new Attribute();
        attribute.setKey("Key");
        attribute.setValue("42");
        Details details = new Details();
        details.setAttribute(attribute);
        details.setCode("Code");
        details.setMessage("Not all who wander are lost");
        Details[] details2 = new Details[]{details};
        healthIDException.setDetails(details2);
        healthIDException.setMessage("An error occurred");
        String actualCode = healthIDException.getCode();
        Details[] actualDetails = healthIDException.getDetails();

        // Assert that nothing has changed
        assertEquals("An error occurred", healthIDException.getMessage());
        assertEquals("Code", actualCode);
        assertSame(details2, actualDetails);
    }
}
