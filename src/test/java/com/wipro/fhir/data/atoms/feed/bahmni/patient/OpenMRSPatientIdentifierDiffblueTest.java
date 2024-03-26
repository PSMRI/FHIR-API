package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OpenMRSPatientIdentifierDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPatientIdentifier#setIdentifier(String)}
     *   <li>
     * {@link OpenMRSPatientIdentifier#setIdentifierType(OpenMRSPatientIdentifierType)}
     *   <li>{@link OpenMRSPatientIdentifier#setPreferred(boolean)}
     *   <li>{@link OpenMRSPatientIdentifier#setUuid(String)}
     *   <li>{@link OpenMRSPatientIdentifier#setVoided(boolean)}
     *   <li>{@link OpenMRSPatientIdentifier#getIdentifier()}
     *   <li>{@link OpenMRSPatientIdentifier#getIdentifierType()}
     *   <li>{@link OpenMRSPatientIdentifier#getUuid()}
     *   <li>{@link OpenMRSPatientIdentifier#isPreferred()}
     *   <li>{@link OpenMRSPatientIdentifier#isVoided()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPatientIdentifier openMRSPatientIdentifier = new OpenMRSPatientIdentifier();

        // Act
        openMRSPatientIdentifier.setIdentifier("42");
        OpenMRSPatientIdentifierType identifierType = new OpenMRSPatientIdentifierType();
        openMRSPatientIdentifier.setIdentifierType(identifierType);
        openMRSPatientIdentifier.setPreferred(true);
        openMRSPatientIdentifier.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        openMRSPatientIdentifier.setVoided(true);
        String actualIdentifier = openMRSPatientIdentifier.getIdentifier();
        OpenMRSPatientIdentifierType actualIdentifierType = openMRSPatientIdentifier.getIdentifierType();
        String actualUuid = openMRSPatientIdentifier.getUuid();
        boolean actualIsPreferredResult = openMRSPatientIdentifier.isPreferred();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
        assertEquals("42", actualIdentifier);
        assertTrue(actualIsPreferredResult);
        assertTrue(openMRSPatientIdentifier.isVoided());
        assertSame(identifierType, actualIdentifierType);
    }
}
