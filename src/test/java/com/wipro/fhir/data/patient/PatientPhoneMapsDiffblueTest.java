package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PatientPhoneMapsDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientPhoneMaps#setBenRelationshipID(Integer)}
     *   <li>{@link PatientPhoneMaps#setPhoneNo(String)}
     *   <li>{@link PatientPhoneMaps#getBenRelationshipID()}
     *   <li>{@link PatientPhoneMaps#getPhoneNo()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientPhoneMaps patientPhoneMaps = new PatientPhoneMaps();

        // Act
        patientPhoneMaps.setBenRelationshipID(1);
        patientPhoneMaps.setPhoneNo("6625550144");
        Integer actualBenRelationshipID = patientPhoneMaps.getBenRelationshipID();

        // Assert that nothing has changed
        assertEquals("6625550144", patientPhoneMaps.getPhoneNo());
        assertEquals(1, actualBenRelationshipID.intValue());
    }
}
