package com.wipro.fhir.data.healthID_validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class PatientDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Patient#setAddress(Address)}
     *   <li>{@link Patient#setGender(String)}
     *   <li>{@link Patient#setId(String)}
     *   <li>{@link Patient#setName(String)}
     *   <li>{@link Patient#setYearOfBirth(String)}
     *   <li>{@link Patient#getAddress()}
     *   <li>{@link Patient#getGender()}
     *   <li>{@link Patient#getId()}
     *   <li>{@link Patient#getName()}
     *   <li>{@link Patient#getYearOfBirth()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Patient patient = new Patient();

        Address address = new Address();
        address.setDistrict("District");
        address.setLine("Line");
        address.setPinCode("Pin Code");
        address.setState("MD");

        // Act
        patient.setAddress(address);
        patient.setGender("Gender");
        patient.setId("42");
        patient.setName("Name");
        patient.setYearOfBirth("Year Of Birth");
        Address actualAddress = patient.getAddress();
        String actualGender = patient.getGender();
        String actualId = patient.getId();
        String actualName = patient.getName();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Gender", actualGender);
        assertEquals("Name", actualName);
        assertEquals("Year Of Birth", patient.getYearOfBirth());
        assertSame(address, actualAddress);
    }
}
