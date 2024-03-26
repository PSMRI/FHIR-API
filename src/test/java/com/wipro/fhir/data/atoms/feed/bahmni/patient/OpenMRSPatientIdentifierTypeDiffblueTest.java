package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OpenMRSPatientIdentifierTypeDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPatientIdentifierType#setDisplay(String)}
     *   <li>{@link OpenMRSPatientIdentifierType#setUuid(String)}
     *   <li>{@link OpenMRSPatientIdentifierType#getDisplay()}
     *   <li>{@link OpenMRSPatientIdentifierType#getUuid()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPatientIdentifierType openMRSPatientIdentifierType = new OpenMRSPatientIdentifierType();

        // Act
        openMRSPatientIdentifierType.setDisplay("Display");
        openMRSPatientIdentifierType.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        String actualDisplay = openMRSPatientIdentifierType.getDisplay();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", openMRSPatientIdentifierType.getUuid());
        assertEquals("Display", actualDisplay);
    }
}
