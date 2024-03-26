package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class OpenMRSPatientFullRepresentationDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPatientFullRepresentation#setIdentifiers(List)}
     *   <li>{@link OpenMRSPatientFullRepresentation#setPerson(OpenMRSPerson)}
     *   <li>{@link OpenMRSPatientFullRepresentation#setUuid(String)}
     *   <li>{@link OpenMRSPatientFullRepresentation#getIdentifiers()}
     *   <li>{@link OpenMRSPatientFullRepresentation#getPerson()}
     *   <li>{@link OpenMRSPatientFullRepresentation#getUuid()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPatientFullRepresentation openMRSPatientFullRepresentation = new OpenMRSPatientFullRepresentation();
        ArrayList<OpenMRSPatientIdentifier> identifiers = new ArrayList<>();

        // Act
        openMRSPatientFullRepresentation.setIdentifiers(identifiers);
        OpenMRSPerson person = new OpenMRSPerson();
        openMRSPatientFullRepresentation.setPerson(person);
        openMRSPatientFullRepresentation.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        List<OpenMRSPatientIdentifier> actualIdentifiers = openMRSPatientFullRepresentation.getIdentifiers();
        OpenMRSPerson actualPerson = openMRSPatientFullRepresentation.getPerson();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", openMRSPatientFullRepresentation.getUuid());
        assertSame(person, actualPerson);
        assertSame(identifiers, actualIdentifiers);
    }
}
