package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class CareContextPatientDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link CareContextPatient#setCarecontexts(ArrayList)}
     *   <li>{@link CareContextPatient#setDisplay(String)}
     *   <li>{@link CareContextPatient#setReferencenumber(String)}
     *   <li>{@link CareContextPatient#getCarecontexts()}
     *   <li>{@link CareContextPatient#getDisplay()}
     *   <li>{@link CareContextPatient#getReferencenumber()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        CareContextPatient careContextPatient = new CareContextPatient();
        ArrayList<CareContextDetials> carecontexts = new ArrayList<>();

        // Act
        careContextPatient.setCarecontexts(carecontexts);
        careContextPatient.setDisplay("Display");
        careContextPatient.setReferencenumber("42");
        ArrayList<CareContextDetials> actualCarecontexts = careContextPatient.getCarecontexts();
        String actualDisplay = careContextPatient.getDisplay();

        // Assert that nothing has changed
        assertEquals("42", careContextPatient.getReferencenumber());
        assertEquals("Display", actualDisplay);
        assertSame(carecontexts, actualCarecontexts);
    }
}
