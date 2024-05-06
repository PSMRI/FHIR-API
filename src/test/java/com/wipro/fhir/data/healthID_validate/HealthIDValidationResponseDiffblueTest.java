package com.wipro.fhir.data.healthID_validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class HealthIDValidationResponseDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link HealthIDValidationResponse#setAuth(AuthNDHM)}
     *   <li>{@link HealthIDValidationResponse#setRequestId(String)}
     *   <li>{@link HealthIDValidationResponse#setTimestamp(String)}
     *   <li>{@link HealthIDValidationResponse#getAuth()}
     *   <li>{@link HealthIDValidationResponse#getRequestId()}
     *   <li>{@link HealthIDValidationResponse#getTimestamp()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        HealthIDValidationResponse healthIDValidationResponse = new HealthIDValidationResponse();

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

        AuthNDHM auth = new AuthNDHM();
        auth.setAccessToken("ABC123");
        auth.setPatient(patient);

        // Act
        healthIDValidationResponse.setAuth(auth);
        healthIDValidationResponse.setRequestId("42");
        healthIDValidationResponse.setTimestamp("Timestamp");
        AuthNDHM actualAuth = healthIDValidationResponse.getAuth();
        String actualRequestId = healthIDValidationResponse.getRequestId();

        // Assert that nothing has changed
        assertEquals("42", actualRequestId);
        assertEquals("Timestamp", healthIDValidationResponse.getTimestamp());
        assertSame(auth, actualAuth);
    }
}
