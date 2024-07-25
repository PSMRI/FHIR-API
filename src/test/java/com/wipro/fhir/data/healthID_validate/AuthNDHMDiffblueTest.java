package com.wipro.fhir.data.healthID_validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class AuthNDHMDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link AuthNDHM#setAccessToken(String)}
     *   <li>{@link AuthNDHM#setPatient(Patient)}
     *   <li>{@link AuthNDHM#getAccessToken()}
     *   <li>{@link AuthNDHM#getPatient()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        AuthNDHM authNDHM = new AuthNDHM();

        // Act
        authNDHM.setAccessToken("ABC123");
        Address address = new Address();
        address.setDistrict("District");
        address.setLine("Line");
        address.setPinCode("Pin Code");
        address.setState("MD");
        Patient patient = new Patient();
        patient.setAddress(address);
        patient.setGender("Gender");
        patient.setId("42");
        patient.setName("Name");
        patient.setYearOfBirth("Year Of Birth");
        authNDHM.setPatient(patient);
        String actualAccessToken = authNDHM.getAccessToken();

        // Assert that nothing has changed
        assertEquals("ABC123", actualAccessToken);
        assertSame(patient, authNDHM.getPatient());
    }
}
