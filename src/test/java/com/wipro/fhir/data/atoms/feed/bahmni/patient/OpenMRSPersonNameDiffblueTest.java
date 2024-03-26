package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OpenMRSPersonNameDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonName#setDisplay(String)}
     *   <li>{@link OpenMRSPersonName#setFamilyName2(String)}
     *   <li>{@link OpenMRSPersonName#setFamilyName(String)}
     *   <li>{@link OpenMRSPersonName#setGivenName(String)}
     *   <li>{@link OpenMRSPersonName#setMiddleName(String)}
     *   <li>{@link OpenMRSPersonName#setUuid(String)}
     *   <li>{@link OpenMRSPersonName#setVoided(boolean)}
     *   <li>{@link OpenMRSPersonName#getDisplay()}
     *   <li>{@link OpenMRSPersonName#getFamilyName()}
     *   <li>{@link OpenMRSPersonName#getFamilyName2()}
     *   <li>{@link OpenMRSPersonName#getGivenName()}
     *   <li>{@link OpenMRSPersonName#getMiddleName()}
     *   <li>{@link OpenMRSPersonName#getUuid()}
     *   <li>{@link OpenMRSPersonName#isVoided()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPersonName openMRSPersonName = new OpenMRSPersonName();

        // Act
        openMRSPersonName.setDisplay("Display");
        openMRSPersonName.setFamilyName2("Family Name2");
        openMRSPersonName.setFamilyName("Family Name");
        openMRSPersonName.setGivenName("Given Name");
        openMRSPersonName.setMiddleName("Middle Name");
        openMRSPersonName.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        openMRSPersonName.setVoided(true);
        String actualDisplay = openMRSPersonName.getDisplay();
        String actualFamilyName = openMRSPersonName.getFamilyName();
        String actualFamilyName2 = openMRSPersonName.getFamilyName2();
        String actualGivenName = openMRSPersonName.getGivenName();
        String actualMiddleName = openMRSPersonName.getMiddleName();
        String actualUuid = openMRSPersonName.getUuid();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
        assertEquals("Display", actualDisplay);
        assertEquals("Family Name", actualFamilyName);
        assertEquals("Family Name2", actualFamilyName2);
        assertEquals("Given Name", actualGivenName);
        assertEquals("Middle Name", actualMiddleName);
        assertTrue(openMRSPersonName.isVoided());
    }
}
