package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AddCareContextRequestDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link AddCareContextRequest#setBeneficiaryID(String)}
     *   <li>{@link AddCareContextRequest#setHealthID(String)}
     *   <li>{@link AddCareContextRequest#setHealthIdNumber(String)}
     *   <li>{@link AddCareContextRequest#setVisitCategory(String)}
     *   <li>{@link AddCareContextRequest#setVisitCode(String)}
     *   <li>{@link AddCareContextRequest#getBeneficiaryID()}
     *   <li>{@link AddCareContextRequest#getHealthID()}
     *   <li>{@link AddCareContextRequest#getHealthIdNumber()}
     *   <li>{@link AddCareContextRequest#getVisitCategory()}
     *   <li>{@link AddCareContextRequest#getVisitCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        AddCareContextRequest addCareContextRequest = new AddCareContextRequest();

        // Act
        addCareContextRequest.setBeneficiaryID("Beneficiary ID");
        addCareContextRequest.setHealthID("Health ID");
        addCareContextRequest.setHealthIdNumber("42");
        addCareContextRequest.setVisitCategory("Visit Category");
        addCareContextRequest.setVisitCode("Visit Code");
        String actualBeneficiaryID = addCareContextRequest.getBeneficiaryID();
        String actualHealthID = addCareContextRequest.getHealthID();
        String actualHealthIdNumber = addCareContextRequest.getHealthIdNumber();
        String actualVisitCategory = addCareContextRequest.getVisitCategory();

        // Assert that nothing has changed
        assertEquals("42", actualHealthIdNumber);
        assertEquals("Beneficiary ID", actualBeneficiaryID);
        assertEquals("Health ID", actualHealthID);
        assertEquals("Visit Category", actualVisitCategory);
        assertEquals("Visit Code", addCareContextRequest.getVisitCode());
    }
}
