package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PatientAddressDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientAddress#setAddressLine1(String)}
     *   <li>{@link PatientAddress#setAddressLine2(String)}
     *   <li>{@link PatientAddress#setAddressLine3(String)}
     *   <li>{@link PatientAddress#setBlockID(Integer)}
     *   <li>{@link PatientAddress#setBlockName(String)}
     *   <li>{@link PatientAddress#setDistrictBranchID(Integer)}
     *   <li>{@link PatientAddress#setDistrictBranchName(String)}
     *   <li>{@link PatientAddress#setDistrictID(Integer)}
     *   <li>{@link PatientAddress#setDistrictName(String)}
     *   <li>{@link PatientAddress#setPinCode(String)}
     *   <li>{@link PatientAddress#setStateID(Integer)}
     *   <li>{@link PatientAddress#setStateName(String)}
     *   <li>{@link PatientAddress#getAddressLine1()}
     *   <li>{@link PatientAddress#getAddressLine2()}
     *   <li>{@link PatientAddress#getAddressLine3()}
     *   <li>{@link PatientAddress#getBlockID()}
     *   <li>{@link PatientAddress#getBlockName()}
     *   <li>{@link PatientAddress#getDistrictBranchID()}
     *   <li>{@link PatientAddress#getDistrictBranchName()}
     *   <li>{@link PatientAddress#getDistrictID()}
     *   <li>{@link PatientAddress#getDistrictName()}
     *   <li>{@link PatientAddress#getPinCode()}
     *   <li>{@link PatientAddress#getStateID()}
     *   <li>{@link PatientAddress#getStateName()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientAddress patientAddress = new PatientAddress();

        // Act
        patientAddress.setAddressLine1("42 Main St");
        patientAddress.setAddressLine2("42 Main St");
        patientAddress.setAddressLine3("42 Main St");
        patientAddress.setBlockID(1);
        patientAddress.setBlockName("Block Name");
        patientAddress.setDistrictBranchID(1);
        patientAddress.setDistrictBranchName("janedoe/featurebranch");
        patientAddress.setDistrictID(1);
        patientAddress.setDistrictName("District Name");
        patientAddress.setPinCode("Pin Code");
        patientAddress.setStateID(1);
        patientAddress.setStateName("MD");
        String actualAddressLine1 = patientAddress.getAddressLine1();
        String actualAddressLine2 = patientAddress.getAddressLine2();
        String actualAddressLine3 = patientAddress.getAddressLine3();
        Integer actualBlockID = patientAddress.getBlockID();
        String actualBlockName = patientAddress.getBlockName();
        Integer actualDistrictBranchID = patientAddress.getDistrictBranchID();
        String actualDistrictBranchName = patientAddress.getDistrictBranchName();
        Integer actualDistrictID = patientAddress.getDistrictID();
        String actualDistrictName = patientAddress.getDistrictName();
        String actualPinCode = patientAddress.getPinCode();
        Integer actualStateID = patientAddress.getStateID();

        // Assert that nothing has changed
        assertEquals("42 Main St", actualAddressLine1);
        assertEquals("42 Main St", actualAddressLine2);
        assertEquals("42 Main St", actualAddressLine3);
        assertEquals("Block Name", actualBlockName);
        assertEquals("District Name", actualDistrictName);
        assertEquals("MD", patientAddress.getStateName());
        assertEquals("Pin Code", actualPinCode);
        assertEquals("janedoe/featurebranch", actualDistrictBranchName);
        assertEquals(1, actualBlockID.intValue());
        assertEquals(1, actualDistrictBranchID.intValue());
        assertEquals(1, actualDistrictID.intValue());
        assertEquals(1, actualStateID.intValue());
    }
}
