package com.wipro.fhir.data.request_handler;

import static org.junit.jupiter.api.Assertions.assertSame;

import com.wipro.fhir.data.patient.PatientDemographicDetails;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class PatientSearchAPIResponseDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientSearchAPIResponse#setData(ArrayList)}
     *   <li>{@link PatientSearchAPIResponse#getData()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientSearchAPIResponse patientSearchAPIResponse = new PatientSearchAPIResponse();
        ArrayList<PatientDemographicDetails> data = new ArrayList<>();

        // Act
        patientSearchAPIResponse.setData(data);

        // Assert that nothing has changed
        assertSame(data, patientSearchAPIResponse.getData());
    }
}
