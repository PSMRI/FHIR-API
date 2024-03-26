package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SyncPatientDetailsRequestDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SyncPatientDetailsRequest#setAmrit_beneficiary_id(String)}
     *   <li>{@link SyncPatientDetailsRequest#setGnum_gender_code(String)}
     *   <li>{@link SyncPatientDetailsRequest#setHstdt_age(String)}
     *   <li>{@link SyncPatientDetailsRequest#setHststr_father_name(String)}
     *   <li>{@link SyncPatientDetailsRequest#setHststr_patient_id(String)}
     *   <li>{@link SyncPatientDetailsRequest#setHststr_patient_name(String)}
     *   <li>{@link SyncPatientDetailsRequest#setHststr_prescribed_by(String)}
     *   <li>{@link SyncPatientDetailsRequest#setNdhm_health_id(String)}
     *   <li>{@link SyncPatientDetailsRequest#setNdhm_health_id_no(String)}
     *   <li>{@link SyncPatientDetailsRequest#getAmrit_beneficiary_id()}
     *   <li>{@link SyncPatientDetailsRequest#getGnum_gender_code()}
     *   <li>{@link SyncPatientDetailsRequest#getHstdt_age()}
     *   <li>{@link SyncPatientDetailsRequest#getHststr_father_name()}
     *   <li>{@link SyncPatientDetailsRequest#getHststr_patient_id()}
     *   <li>{@link SyncPatientDetailsRequest#getHststr_patient_name()}
     *   <li>{@link SyncPatientDetailsRequest#getHststr_prescribed_by()}
     *   <li>{@link SyncPatientDetailsRequest#getNdhm_health_id()}
     *   <li>{@link SyncPatientDetailsRequest#getNdhm_health_id_no()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        SyncPatientDetailsRequest syncPatientDetailsRequest = new SyncPatientDetailsRequest();

        // Act
        syncPatientDetailsRequest.setAmrit_beneficiary_id("Amrit beneficiary id");
        syncPatientDetailsRequest.setGnum_gender_code("Gnum gender code");
        syncPatientDetailsRequest.setHstdt_age("Hstdt age");
        syncPatientDetailsRequest.setHststr_father_name("Hststr father name");
        syncPatientDetailsRequest.setHststr_patient_id("Hststr patient id");
        syncPatientDetailsRequest.setHststr_patient_name("Hststr patient name");
        syncPatientDetailsRequest.setHststr_prescribed_by("Hststr prescribed by");
        syncPatientDetailsRequest.setNdhm_health_id("Ndhm health id");
        syncPatientDetailsRequest.setNdhm_health_id_no("Ndhm health id no");
        String actualAmrit_beneficiary_id = syncPatientDetailsRequest.getAmrit_beneficiary_id();
        String actualGnum_gender_code = syncPatientDetailsRequest.getGnum_gender_code();
        String actualHstdt_age = syncPatientDetailsRequest.getHstdt_age();
        String actualHststr_father_name = syncPatientDetailsRequest.getHststr_father_name();
        String actualHststr_patient_id = syncPatientDetailsRequest.getHststr_patient_id();
        String actualHststr_patient_name = syncPatientDetailsRequest.getHststr_patient_name();
        String actualHststr_prescribed_by = syncPatientDetailsRequest.getHststr_prescribed_by();
        String actualNdhm_health_id = syncPatientDetailsRequest.getNdhm_health_id();

        // Assert that nothing has changed
        assertEquals("Amrit beneficiary id", actualAmrit_beneficiary_id);
        assertEquals("Gnum gender code", actualGnum_gender_code);
        assertEquals("Hstdt age", actualHstdt_age);
        assertEquals("Hststr father name", actualHststr_father_name);
        assertEquals("Hststr patient id", actualHststr_patient_id);
        assertEquals("Hststr patient name", actualHststr_patient_name);
        assertEquals("Hststr prescribed by", actualHststr_prescribed_by);
        assertEquals("Ndhm health id no", syncPatientDetailsRequest.getNdhm_health_id_no());
        assertEquals("Ndhm health id", actualNdhm_health_id);
    }
}
