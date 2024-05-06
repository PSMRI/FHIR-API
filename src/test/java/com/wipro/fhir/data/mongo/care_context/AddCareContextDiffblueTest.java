package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class AddCareContextDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link AddCareContext#setAccessToken(String)}
     *   <li>{@link AddCareContext#setPatient(CareContextPatient)}
     *   <li>{@link AddCareContext#getAccessToken()}
     *   <li>{@link AddCareContext#getPatient()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        AddCareContext addCareContext = new AddCareContext();

        // Act
        addCareContext.setAccessToken("ABC123");
        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");
        addCareContext.setPatient(patient);
        String actualAccessToken = addCareContext.getAccessToken();

        // Assert that nothing has changed
        assertEquals("ABC123", actualAccessToken);
        assertSame(patient, addCareContext.getPatient());
    }
}
