package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MaritalStatusDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link MaritalStatus#setMaritalStatusID(Integer)}
     *   <li>{@link MaritalStatus#setStatus(String)}
     *   <li>{@link MaritalStatus#getMaritalStatusID()}
     *   <li>{@link MaritalStatus#getStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        MaritalStatus maritalStatus = new MaritalStatus();

        // Act
        maritalStatus.setMaritalStatusID(1);
        maritalStatus.setStatus("Status");
        Integer actualMaritalStatusID = maritalStatus.getMaritalStatusID();

        // Assert that nothing has changed
        assertEquals("Status", maritalStatus.getStatus());
        assertEquals(1, actualMaritalStatusID.intValue());
    }
}
