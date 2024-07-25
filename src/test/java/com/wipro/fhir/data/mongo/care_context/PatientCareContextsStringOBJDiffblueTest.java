package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class PatientCareContextsStringOBJDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientCareContextsStringOBJ#setCareContexts(ArrayList)}
     *   <li>{@link PatientCareContextsStringOBJ#getCareContexts()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientCareContextsStringOBJ patientCareContextsStringOBJ = new PatientCareContextsStringOBJ();
        ArrayList<CareContexts> careContexts = new ArrayList<>();

        // Act
        patientCareContextsStringOBJ.setCareContexts(careContexts);

        // Assert that nothing has changed
        assertSame(careContexts, patientCareContextsStringOBJ.getCareContexts());
    }
}
