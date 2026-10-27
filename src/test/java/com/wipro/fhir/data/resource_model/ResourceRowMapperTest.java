/*
* AMRIT - Accessible Medical Records via Integrated Technologies
* Integrated EHR (Electronic Health Records) Solution
*
* Copyright (C) "Piramal Swasthya Management and Research Institute"
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.data.resource_model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The resource models double as JPA row mappers: each takes the {@code Object[]} a native
 * query hands back and turns it into a typed model. These tests pin the column order down,
 * check that a null column leaves the corresponding property null, and drive the list
 * helpers with a populated list, an empty list and a null list.
 */
@DisplayName("Resource model row mappers Test Suite")
class ResourceRowMapperTest {

    private static final Timestamp CREATED = new Timestamp(1_700_000_000_000L);
    private static final Timestamp RECEIVED = new Timestamp(1_600_000_000_000L);

    private Object[] nulls(int length) {
        return new Object[length];
    }

    /** {@code List.of} would infer a {@code List<Object>} from a single array argument. */
    private static List<Object[]> rows(Object[]... rows) {
        return Arrays.asList(rows);
    }

    @Nested
    @DisplayName("AllergyIntoleranceDataModel")
    class AllergyIntoleranceTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, "active", "food", "SCT-1", "Peanut butter",
                    CREATED, "admin", "rash" };
        }

        @Test
        @DisplayName("maps every allergy column in order")
        void constructor_shouldMapEveryColumn() {
            AllergyIntoleranceDataModel model = new AllergyIntoleranceDataModel(row());

            assertEquals(BigInteger.ONE, model.getId());
            assertEquals(BigInteger.valueOf(4321L), model.getBeneficiaryRegID());
            assertEquals(BigInteger.valueOf(987654L), model.getVisitCode());
            assertEquals(7, model.getProviderServiceMapID());
            assertEquals(3, model.getVanID());
            assertEquals("active", model.getAllergyStatus());
            assertEquals("food", model.getAllergyType());
            assertEquals("SCT-1", model.getSctcode());
            assertEquals("Peanut butter", model.getSctTerm());
            assertEquals(CREATED, model.getCreatedDate());
            assertEquals("admin", model.getCreatedBy());
            assertEquals("rash", model.getAllergicReactionType());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            AllergyIntoleranceDataModel model = new AllergyIntoleranceDataModel(nulls(12));

            assertNull(model.getId());
            assertNull(model.getSctTerm());
            assertNull(model.getCreatedDate());
        }

        @Test
        @DisplayName("getAllergyList maps each row, and answers empty for an empty or null result set")
        void getAllergyList_shouldMapEveryRow() {
            AllergyIntoleranceDataModel mapper = new AllergyIntoleranceDataModel();

            List<AllergyIntoleranceDataModel> mapped = mapper.getAllergyList(rows(row(), row()));

            assertEquals(2, mapped.size());
            assertEquals("Peanut butter", mapped.get(1).getSctTerm());
            assertTrue(mapper.getAllergyList(rows()).isEmpty());
            assertTrue(mapper.getAllergyList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("AppointmentDataModel")
    class AppointmentTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, "booked", CREATED, 55, "Dr Rao", 9,
                    "Medical Officer", CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every appointment column in order")
        void constructor_shouldMapEveryColumn() {
            AppointmentDataModel model = new AppointmentDataModel(row());

            assertEquals(BigInteger.valueOf(4321L), model.getBeneficiaryRegID());
            assertEquals("booked", model.getStatus());
            assertEquals(CREATED, model.getRequestDate());
            assertEquals(55, model.getUserID());
            assertEquals("Dr Rao", model.getSName());
            assertEquals(9, model.getDesignationID());
            assertEquals("Medical Officer", model.getDesignationName());
            assertEquals("admin", model.getCreatedBy());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new AppointmentDataModel(nulls(13)).getStatus());
        }

        @Test
        @DisplayName("getAppointmentList maps each row and tolerates an absent result set")
        void getAppointmentList_shouldMapEveryRow() {
            AppointmentDataModel mapper = new AppointmentDataModel();

            assertEquals(1, mapper.getAppointmentList(rows(row())).size());
            assertTrue(mapper.getAppointmentList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("ConditionChiefComplaintsDataModel")
    class ChiefComplaintTests {

        private Object[] row() {
            return new Object[] { "Fever", "SCT-2", 3, "Days", "since Monday", CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every chief-complaint column in order")
        void constructor_shouldMapEveryColumn() {
            ConditionChiefComplaintsDataModel model = new ConditionChiefComplaintsDataModel(row());

            assertEquals("Fever", model.getChiefComplaint());
            assertEquals("SCT-2", model.getSCTCode());
            assertEquals(3, model.getDuration());
            assertEquals("Days", model.getUnitOfDuration());
            assertEquals("since Monday", model.getDescription());
            assertEquals(CREATED, model.getCreatedDate());
            assertEquals("admin", model.getCreatedBy());
        }

        @Test
        @DisplayName("getConditionChiefComplaintList maps each row and tolerates an absent result set")
        void getConditionChiefComplaintList_shouldMapEveryRow() {
            ConditionChiefComplaintsDataModel mapper = new ConditionChiefComplaintsDataModel();

            assertEquals(2, mapper.getConditionChiefComplaintList(rows(row(), row())).size());
            assertTrue(mapper.getConditionChiefComplaintList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("ConditionDiagnosisDataModel")
    class DiagnosisTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, "Malaria", "SCT-3", CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every diagnosis column in order")
        void constructor_shouldMapEveryColumn() {
            ConditionDiagnosisDataModel model = new ConditionDiagnosisDataModel(row());

            assertEquals("Malaria", model.getSctTerm());
            assertEquals("SCT-3", model.getSctcode());
            assertEquals(BigInteger.valueOf(987654L), model.getVisitCode());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new ConditionDiagnosisDataModel(nulls(9)).getSctTerm());
        }

        @Test
        @DisplayName("getConditionList maps each row and tolerates an absent result set")
        void getConditionList_shouldMapEveryRow() {
            ConditionDiagnosisDataModel mapper = new ConditionDiagnosisDataModel();

            assertEquals(1, mapper.getConditionList(rows(row())).size());
            assertTrue(mapper.getConditionList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("DiagnosticReportDataModel")
    class DiagnosticReportTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, 11, 12, "CBC", "Haemoglobin", "13.2",
                    BigDecimal.valueOf(12), BigDecimal.valueOf(16), "LOINC-1", "Hb", CREATED, "admin",
                    "fasting sample", "g/dL" };
        }

        @Test
        @DisplayName("maps every diagnostic-report column in order")
        void constructor_shouldMapEveryColumn() {
            DiagnosticReportDataModel model = new DiagnosticReportDataModel(row());

            assertEquals(11, model.getProcedureID());
            assertEquals(12, model.getComponentID());
            assertEquals("CBC", model.getProcedureName());
            assertEquals("Haemoglobin", model.getTestComponentName());
            assertEquals("13.2", model.getTestResultValue());
            assertEquals(BigDecimal.valueOf(12), model.getRangeMin());
            assertEquals(BigDecimal.valueOf(16), model.getRangeMax());
            assertEquals("LOINC-1", model.getLoincCode());
            assertEquals("Hb", model.getLoincValue());
            assertEquals("fasting sample", model.getRemarks());
            assertEquals("g/dL", model.getTestResultUnit());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new DiagnosticReportDataModel(nulls(18)).getProcedureName());
        }

        @Test
        @DisplayName("getDiagnosticReportList maps each row and tolerates an absent result set")
        void getDiagnosticReportList_shouldMapEveryRow() {
            DiagnosticReportDataModel mapper = new DiagnosticReportDataModel();

            assertEquals(1, mapper.getDiagnosticReportList(rows(row())).size());
            assertTrue(mapper.getDiagnosticReportList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("EncounterDataModel")
    class EncounterTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, 1, 1, 0, CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every encounter column in order, narrowing the flag columns to ints")
        void constructor_shouldMapEveryColumn() {
            EncounterDataModel model = new EncounterDataModel(row());

            assertEquals(1, model.getNurseFlag());
            assertEquals(1, model.getDocFlag());
            assertEquals(0, model.getSpecialistFlag());
            assertEquals("admin", model.getCreatedBy());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new EncounterDataModel(nulls(10)).getNurseFlag());
        }

        @Test
        @DisplayName("getEncounterList maps each row and tolerates an absent result set")
        void getEncounterList_shouldMapEveryRow() {
            EncounterDataModel mapper = new EncounterDataModel();

            assertEquals(1, mapper.getEncounterList(rows(row())).size());
            assertTrue(mapper.getEncounterList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("FamilyMemberHistoryDataModel")
    class FamilyHistoryTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, "Mother", "SCT-4", "Diabetes mellitus",
                    CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every family-history column in order")
        void constructor_shouldMapEveryColumn() {
            FamilyMemberHistoryDataModel model = new FamilyMemberHistoryDataModel(row());

            assertEquals("Mother", model.getFamilyMembers());
            assertEquals("SCT-4", model.getSctcode());
            assertEquals("Diabetes mellitus", model.getSctTerm());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new FamilyMemberHistoryDataModel(nulls(10)).getFamilyMembers());
        }

        @Test
        @DisplayName("getFamilyMemberHistoryList maps each row and tolerates an absent result set")
        void getFamilyMemberHistoryList_shouldMapEveryRow() {
            FamilyMemberHistoryDataModel mapper = new FamilyMemberHistoryDataModel();

            assertEquals(1, mapper.getFamilyMemberHistoryList(rows(row())).size());
            assertTrue(mapper.getFamilyMemberHistoryList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("ImmunizationDataModel")
    class ImmunizationTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, "at birth", "BCG", RECEIVED, "PHC Kanke",
                    "SCT-5", "BCG vaccination", CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every immunization column in order")
        void constructor_shouldMapEveryColumn() {
            ImmunizationDataModel model = new ImmunizationDataModel(row());

            assertEquals("at birth", model.getDefaultReceivingAge());
            assertEquals("BCG", model.getVaccineName());
            assertEquals(RECEIVED, model.getReceivedDate());
            assertEquals("PHC Kanke", model.getReceivedFacilityName());
            assertEquals("BCG vaccination", model.getSctTerm());
            assertEquals(7, model.getProviderServiceMapID());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new ImmunizationDataModel(nulls(13)).getVaccineName());
        }

        @Test
        @DisplayName("getImmunizationList maps each row and tolerates an absent result set")
        void getImmunizationList_shouldMapEveryRow() {
            ImmunizationDataModel mapper = new ImmunizationDataModel();

            assertEquals(1, mapper.getImmunizationList(rows(row())).size());
            assertTrue(mapper.getImmunizationList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("LabTestAndComponentModel")
    class LabTestTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, 11, 12, "CBC", "Haemoglobin", "13.2",
                    BigDecimal.valueOf(12), BigDecimal.valueOf(16), "LOINC-1", "Hb", CREATED, "admin",
                    "fasting sample", "g/dL" };
        }

        @Test
        @DisplayName("maps every lab-test column in order")
        void constructor_shouldMapEveryColumn() {
            LabTestAndComponentModel model = new LabTestAndComponentModel(row());

            assertEquals("CBC", model.getProcedureName());
            assertEquals("g/dL", model.getTestResultUnit());
            assertEquals(BigDecimal.valueOf(16), model.getRangeMax());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new LabTestAndComponentModel(nulls(18)).getProcedureName());
        }

        @Test
        @DisplayName("getlabTestAndComponentList maps each row and tolerates an absent result set")
        void getLabTestList_shouldMapEveryRow() {
            LabTestAndComponentModel mapper = new LabTestAndComponentModel();

            assertEquals(1, mapper.getlabTestAndComponentList(rows(row())).size());
            assertTrue(mapper.getlabTestAndComponentList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("MedicationRequestDataModel")
    class MedicationRequestTests {

        private Object[] row() {
            return new Object[] { 1L, 4321L, 987654L, 7, 3, "Tablet", "Paracetamol", "500 mg", "1",
                    "Oral", "TID", "5", "Days", "after food", 15, "SCT-6", "Paracetamol 500mg tablet",
                    CREATED, "admin" };
        }

        @Test
        @DisplayName("maps every medication-request column in order")
        void constructor_shouldMapEveryColumn() {
            MedicationRequestDataModel model = new MedicationRequestDataModel(row());

            assertEquals("Tablet", model.getDrugForm());
            assertEquals("Paracetamol", model.getGenericDrugName());
            assertEquals("500 mg", model.getDrugStrength());
            assertEquals("Oral", model.getDrugRoute());
            assertEquals("TID", model.getDrugFrequency());
            assertEquals("5", model.getDuration());
            assertEquals("Days", model.getDurationUnit());
            assertEquals("after food", model.getInstructions());
            assertEquals(15, model.getQtyPrescribed());
            assertEquals("SCT-6", model.getSnomedCTCodeDrug());
            assertEquals("Paracetamol 500mg tablet", model.getSnomedCTTermDrug());
        }

        @Test
        @DisplayName("leaves every property null for an all-null row")
        void constructor_shouldTolerateNullColumns() {
            assertNull(new MedicationRequestDataModel(nulls(19)).getGenericDrugName());
        }

        @Test
        @DisplayName("getMedicationRequestList maps each row and tolerates an absent result set")
        void getMedicationRequestList_shouldMapEveryRow() {
            MedicationRequestDataModel mapper = new MedicationRequestDataModel();

            assertEquals(1, mapper.getMedicationRequestList(rows(row())).size());
            assertTrue(mapper.getMedicationRequestList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("VitalsAnthropometryModel")
    class VitalsTests {

        private Object[] row() {
            Object[] row = new Object[19];
            row[0] = 4321L;
            row[1] = 7;
            row[2] = 987654L;
            row[3] = BigDecimal.valueOf(98.4d);
            row[4] = (short) 72;
            row[5] = (short) 18;
            row[6] = (short) 120;
            row[7] = (short) 80;
            row[11] = "Normal";
            row[12] = (short) 90;
            row[13] = (short) 130;
            row[14] = BigDecimal.valueOf(64);
            row[15] = BigDecimal.valueOf(170);
            row[16] = BigDecimal.valueOf(22);
            row[17] = CREATED;
            row[18] = "admin";
            return row;
        }

        @Test
        @DisplayName("maps every vitals column in order, skipping the three unused columns")
        void constructor_shouldMapEveryColumn() {
            VitalsAnthropometryModel model = new VitalsAnthropometryModel(row());

            assertEquals(BigInteger.valueOf(4321L), model.getBeneficiaryRegID());
            assertEquals(BigInteger.valueOf(987654L), model.getVisitCode());
            assertEquals(BigDecimal.valueOf(98.4d), model.getTemperature());
            assertEquals((short) 72, model.getPulseRate());
            assertEquals((short) 18, model.getRespiratoryRate());
            assertEquals((short) 120, model.getSystolicBP_1stReading());
            assertEquals((short) 80, model.getDiastolicBP_1stReading());
            assertEquals("Normal", model.getBloodPressureStatus());
            assertEquals((short) 90, model.getBloodGlucose_Fasting());
            assertEquals((short) 130, model.getBloodGlucose_Random());
            assertEquals(BigDecimal.valueOf(64), model.getWeight_Kg());
            assertEquals(BigDecimal.valueOf(170), model.getHeight_cm());
            assertEquals(BigDecimal.valueOf(22), model.getBMI());
            assertEquals(CREATED, model.getCreatedDate());
            assertEquals("admin", model.getCreatedBy());
        }

        @Test
        @DisplayName("getVitalsAndAnthropometryList maps each row and tolerates an absent result set")
        void getVitalsList_shouldMapEveryRow() {
            VitalsAnthropometryModel mapper = new VitalsAnthropometryModel();

            assertEquals(2, mapper.getVitalsAndAnthropometryList(rows(row(), row())).size());
            assertTrue(mapper.getVitalsAndAnthropometryList(rows()).isEmpty());
            assertTrue(mapper.getVitalsAndAnthropometryList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("MedicalHistoryDataModel")
    class MedicalHistoryTests {

        private Object[] row() {
            return new Object[] { 1L, 7L, "Metformin", CREATED, "admin", CREATED };
        }

        @Test
        @DisplayName("maps every medical-history column, parsing the id columns from their text form")
        void constructor_shouldMapEveryColumn() throws Exception {
            MedicalHistoryDataModel model = new MedicalHistoryDataModel(row());

            assertEquals(BigInteger.ONE, model.getId());
            assertEquals(BigInteger.valueOf(7L), model.getProviderServiceMapID());
            assertEquals("Metformin", model.getCurrentMedication());
            assertEquals(CREATED, model.getCurrentMedYear());
            assertEquals("admin", model.getCreatedBy());
            assertEquals(CREATED, model.getCreatedDate());
        }

        @Test
        @DisplayName("leaves the timestamp columns null when they are not timestamps")
        void constructor_shouldIgnoreNonTimestampColumns() throws Exception {
            MedicalHistoryDataModel model =
                    new MedicalHistoryDataModel(new Object[] { 1L, 7L, "Metformin", "2023", "admin", "2023" });

            assertNull(model.getCurrentMedYear());
            assertNull(model.getCreatedDate());
        }

        @Test
        @DisplayName("wraps an unparseable id column in a descriptive failure")
        void constructor_shouldWrapParseFailure() {
            Exception failure = assertThrows(Exception.class, () ->
                    new MedicalHistoryDataModel(new Object[] { "not-a-number", 7L, null, null, null, null }));

            assertTrue(failure.getMessage().startsWith("Medical History resource model failed with error - "));
        }

        @Test
        @DisplayName("getMedicalList maps each row and tolerates an absent result set")
        void getMedicalList_shouldMapEveryRow() throws Exception {
            MedicalHistoryDataModel mapper = new MedicalHistoryDataModel();

            assertEquals(1, mapper.getMedicalList(rows(row())).size());
            assertTrue(mapper.getMedicalList(null).isEmpty());
        }
    }

    @Nested
    @DisplayName("OrganizationDataModel")
    class OrganizationTests {

        private Object[] row(Object isNational) {
            return new Object[] { 91L, 7, "PHC Kanke", 21, "Jharkhand", 301, "Ranchi", "Kanke",
                    "Kanke Road", 5, "MMU", isNational, "IN0710000001", "PHC Kanke ABDM", 15, 7 };
        }

        @Test
        @DisplayName("maps every organization column, parsing each from its text form")
        void constructor_shouldMapEveryColumn() throws FHIRException {
            OrganizationDataModel model = new OrganizationDataModel(row("true"));

            assertEquals(91L, model.getBenVisitID());
            assertEquals((short) 7, model.getServiceProviderID());
            assertEquals("PHC Kanke", model.getServiceProviderName());
            assertEquals(21, model.getStateID());
            assertEquals("Jharkhand", model.getStateName());
            assertEquals(301, model.getDistrictID());
            assertEquals("Ranchi", model.getDistrictName());
            assertEquals("Kanke", model.getLocationName());
            assertEquals("Kanke Road", model.getAddress());
            assertEquals((short) 5, model.getServiceID());
            assertEquals("MMU", model.getServiceName());
            assertTrue(model.getIsNational());
            assertEquals("IN0710000001", model.getAbdmFacilityId());
            assertEquals("PHC Kanke ABDM", model.getAbdmFacilityName());
            assertEquals(15, model.getPsAddMapID());
            assertEquals(7, model.getProviderServiceMapID());
        }

        @Test
        @DisplayName("reads the national flag from either the text \"true\" or the digit 1")
        void constructor_shouldReadNationalFlagFromEitherForm() throws FHIRException {
            assertTrue(new OrganizationDataModel(row(1)).getIsNational());
            assertTrue(new OrganizationDataModel(row("TRUE")).getIsNational());
            assertFalse(new OrganizationDataModel(row("0")).getIsNational());
            assertNull(new OrganizationDataModel(row(null)).getIsNational());
        }

        @Test
        @DisplayName("wraps an unparseable column in a FHIRException")
        void constructor_shouldWrapParseFailure() {
            FHIRException failure = assertThrows(FHIRException.class, () ->
                    new OrganizationDataModel(new Object[] { "not-a-number", null, null, null, null, null,
                            null, null, null, null, null, null, null, null, null, null }));

            assertTrue(failure.getMessage().startsWith("Organization resource failed with error - "));
        }

        @Test
        @DisplayName("getOrganization maps a single result row")
        void getOrganization_shouldMapSingleRow() throws FHIRException {
            OrganizationDataModel mapper = new OrganizationDataModel();

            assertEquals("PHC Kanke", mapper.getOrganization(row("true")).getServiceProviderName());
        }
    }

    @Nested
    @DisplayName("PractitionerDataModel")
    class PractitionerTests {

        private static final Date DOB = new Date(1_000_000_000_000L);

        private Object[] row(Object dob, Object created) {
            return new Object[] { 91, 55, "Dr Rao", dob, "EMP-1", "9999999999", "rao@example.org",
                    "MBBS", "Medical Officer", "Male", 1, 7, 987654L, "admin", created };
        }

        @Test
        @DisplayName("maps every practitioner column, parsing each from its text form")
        void constructor_shouldMapEveryColumn() throws FHIRException {
            PractitionerDataModel model = new PractitionerDataModel(row(DOB, CREATED));

            assertEquals(91, model.getBenVisitID());
            assertEquals(55, model.getUserID());
            assertEquals("Dr Rao", model.getFullName());
            assertEquals(DOB, model.getDob());
            assertEquals("EMP-1", model.getEmployeeID());
            assertEquals("9999999999", model.getContactNo());
            assertEquals("rao@example.org", model.getEmailID());
            assertEquals("MBBS", model.getQualificationName());
            assertEquals("Medical Officer", model.getDesignationName());
            assertEquals("Male", model.getGenderName());
            assertEquals(1, model.getGenderID());
            assertEquals(7, model.getServiceProviderID());
            assertEquals(987654L, model.getVisitCode());
            assertEquals("admin", model.getCreatedBy());
            assertEquals(CREATED, model.getCreatedDate());
        }

        @Test
        @DisplayName("leaves the date columns null when they carry the wrong type")
        void constructor_shouldIgnoreWronglyTypedDateColumns() throws FHIRException {
            PractitionerDataModel model = new PractitionerDataModel(row("1970-01-01", "1970-01-01"));

            assertNull(model.getDob());
            assertNull(model.getCreatedDate());
        }

        @Test
        @DisplayName("wraps an unparseable column in a FHIRException")
        void constructor_shouldWrapParseFailure() {
            FHIRException failure = assertThrows(FHIRException.class, () ->
                    new PractitionerDataModel(new Object[] { "not-a-number", null, null, null, null, null,
                            null, null, null, null, null, null, null, null, null }));

            assertTrue(failure.getMessage().startsWith("Practitioner resource failed with error - "));
        }

        @Test
        @DisplayName("getPractitioner maps a single result row")
        void getPractitioner_shouldMapSingleRow() throws FHIRException {
            PractitionerDataModel mapper = new PractitionerDataModel();

            assertEquals("Dr Rao", mapper.getPractitioner(row(DOB, CREATED)).getFullName());
        }
    }

    @Test
    @DisplayName("every row mapper leaves its own no-arg instance blank")
    void noArgConstructors_shouldLeaveModelsBlank() {
        assertTrue(Arrays.asList(new AllergyIntoleranceDataModel(), new AppointmentDataModel(),
                new ConditionChiefComplaintsDataModel(), new ConditionDiagnosisDataModel(),
                new DiagnosticReportDataModel(), new EncounterDataModel(),
                new FamilyMemberHistoryDataModel(), new ImmunizationDataModel(),
                new LabTestAndComponentModel(), new MedicationRequestDataModel(),
                new VitalsAnthropometryModel()).stream().allMatch(java.util.Objects::nonNull));
        assertNull(new AllergyIntoleranceDataModel().getId());
        assertNull(new EncounterDataModel().getCreatedBy());
    }
}
