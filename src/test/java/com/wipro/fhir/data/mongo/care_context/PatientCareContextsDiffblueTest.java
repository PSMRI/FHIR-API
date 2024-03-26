package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class PatientCareContextsDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientCareContexts#setCareContextsList(String)}
     *   <li>{@link PatientCareContexts#setCareContextsListTemp(ArrayList)}
     *   <li>{@link PatientCareContexts#setCaseReferenceNumber(String)}
     *   <li>{@link PatientCareContexts#setEmail(String)}
     *   <li>{@link PatientCareContexts#setGender(String)}
     *   <li>{@link PatientCareContexts#setHealthId(String)}
     *   <li>{@link PatientCareContexts#setHealthNumber(String)}
     *   <li>{@link PatientCareContexts#setId(String)}
     *   <li>{@link PatientCareContexts#setIdentifier(String)}
     *   <li>{@link PatientCareContexts#setName(String)}
     *   <li>{@link PatientCareContexts#setPhoneNumber(String)}
     *   <li>{@link PatientCareContexts#setYearOfBirth(String)}
     *   <li>{@link PatientCareContexts#getCareContextsList()}
     *   <li>{@link PatientCareContexts#getCareContextsListTemp()}
     *   <li>{@link PatientCareContexts#getCaseReferenceNumber()}
     *   <li>{@link PatientCareContexts#getEmail()}
     *   <li>{@link PatientCareContexts#getGender()}
     *   <li>{@link PatientCareContexts#getHealthId()}
     *   <li>{@link PatientCareContexts#getHealthNumber()}
     *   <li>{@link PatientCareContexts#getId()}
     *   <li>{@link PatientCareContexts#getIdentifier()}
     *   <li>{@link PatientCareContexts#getName()}
     *   <li>{@link PatientCareContexts#getPhoneNumber()}
     *   <li>{@link PatientCareContexts#getYearOfBirth()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientCareContexts patientCareContexts = new PatientCareContexts();

        // Act
        patientCareContexts.setCareContextsList("Care Contexts List");
        ArrayList<CareContexts> careContextsListTemp = new ArrayList<>();
        patientCareContexts.setCareContextsListTemp(careContextsListTemp);
        patientCareContexts.setCaseReferenceNumber("42");
        patientCareContexts.setEmail("jane.doe@example.org");
        patientCareContexts.setGender("Gender");
        patientCareContexts.setHealthId("42");
        patientCareContexts.setHealthNumber("42");
        patientCareContexts.setId("42");
        patientCareContexts.setIdentifier("42");
        patientCareContexts.setName("Name");
        patientCareContexts.setPhoneNumber("6625550144");
        patientCareContexts.setYearOfBirth("Year Of Birth");
        String actualCareContextsList = patientCareContexts.getCareContextsList();
        ArrayList<CareContexts> actualCareContextsListTemp = patientCareContexts.getCareContextsListTemp();
        String actualCaseReferenceNumber = patientCareContexts.getCaseReferenceNumber();
        String actualEmail = patientCareContexts.getEmail();
        String actualGender = patientCareContexts.getGender();
        String actualHealthId = patientCareContexts.getHealthId();
        String actualHealthNumber = patientCareContexts.getHealthNumber();
        String actualId = patientCareContexts.getId();
        String actualIdentifier = patientCareContexts.getIdentifier();
        String actualName = patientCareContexts.getName();
        String actualPhoneNumber = patientCareContexts.getPhoneNumber();

        // Assert that nothing has changed
        assertEquals("42", actualCaseReferenceNumber);
        assertEquals("42", actualHealthId);
        assertEquals("42", actualHealthNumber);
        assertEquals("42", actualId);
        assertEquals("42", actualIdentifier);
        assertEquals("6625550144", actualPhoneNumber);
        assertEquals("Care Contexts List", actualCareContextsList);
        assertEquals("Gender", actualGender);
        assertEquals("Name", actualName);
        assertEquals("Year Of Birth", patientCareContexts.getYearOfBirth());
        assertEquals("jane.doe@example.org", actualEmail);
        assertSame(careContextsListTemp, actualCareContextsListTemp);
    }
}
