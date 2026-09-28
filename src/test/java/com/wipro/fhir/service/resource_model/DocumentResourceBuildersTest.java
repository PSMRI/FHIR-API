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
package com.wipro.fhir.service.resource_model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.wipro.fhir.data.patient.PatientDemographic;
import com.wipro.fhir.data.resource_model.LabTestAndComponentModel;
import com.wipro.fhir.data.resource_model.VitalsAnthropometryModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.common.CommonServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;

import static com.wipro.fhir.service.resource_model.SimpleResourceBuildersTest.encounter;
import static com.wipro.fhir.service.resource_model.SimpleResourceBuildersTest.patient;
import static com.wipro.fhir.service.resource_model.SimpleResourceBuildersTest.visitRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The observation, patient and composition builders: the vitals and lab observations that
 * carry the measurements, the patient the bundle is about, and the composition that indexes
 * every section of the finished document.
 */
@DisplayName("Observation, patient and composition builders Test Suite")
class DocumentResourceBuildersTest {

    private static final Timestamp CREATED = new Timestamp(1_700_000_000_000L);
    private static final String UUID = "uuid-1";

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("ObservationResource")
    class ObservationTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private LabTestAndComponentModel observationDataModel;

        @Mock
        private VitalsAnthropometryModel vitalsAnthropometryModel;

        @InjectMocks
        private ObservationResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private VitalsAnthropometryModel allVitals() {
            VitalsAnthropometryModel vitals = new VitalsAnthropometryModel();
            vitals.setTemperature(BigDecimal.valueOf(98.4d));
            vitals.setPulseRate((short) 72);
            vitals.setRespiratoryRate((short) 18);
            vitals.setSystolicBP_1stReading((short) 120);
            vitals.setDiastolicBP_1stReading((short) 80);
            vitals.setHeight_cm(BigDecimal.valueOf(170));
            vitals.setWeight_Kg(BigDecimal.valueOf(64));
            vitals.setBMI(BigDecimal.valueOf(22));
            vitals.setCreatedDate(CREATED);
            return vitals;
        }

        private List<Observation> buildVitals(VitalsAnthropometryModel vitals) {
            when(vitalsAnthropometryModel.getVitalsAndAnthropometryList(any()))
                    .thenReturn(vitals == null ? List.of() : java.util.Arrays.asList(vitals));
            return resource.getObservationVitals(patient(), visitRequest());
        }

        private String valueOf(List<Observation> observations, String code) {
            return observations.stream()
                    .filter(observation -> code.equals(observation.getCode().getText()))
                    .map(observation -> observation.getValueStringType().getValue())
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("no observation for " + code));
        }

        @Test
        @DisplayName("should build one final observation per recorded vital, each with its UCUM unit")
        void getObservationVitals_shouldBuildOnePerVital() {
            List<Observation> observations = buildVitals(allVitals());

            assertEquals(8, observations.size());
            assertEquals("98.4 [degF]", valueOf(observations, "Body temperature"));
            assertEquals("72 / min", valueOf(observations, "Pulse Rate"));
            assertEquals("18 / min", valueOf(observations, "Respiratory Rate"));
            assertEquals("120 mm[Hg]", valueOf(observations, "Systolic blood pressure"));
            assertEquals("80 mm[Hg]", valueOf(observations, "Diastolic blood pressure"));
            assertEquals("170 cm", valueOf(observations, "Body height"));
            assertEquals("64 kg", valueOf(observations, "Body weight"));
            assertEquals("22 kg/m2", valueOf(observations, "Body mass index"));
            Observation first = observations.get(0);
            assertEquals("Observation/" + UUID, first.getId());
            assertEquals(Observation.ObservationStatus.FINAL, first.getStatus());
            assertEquals(CREATED, first.getIssued());
            assertNotNull(first.getEffective());
        }

        @Test
        @DisplayName("should build an observation only for the vitals the visit actually recorded")
        void getObservationVitals_shouldSkipUnrecordedVitals() {
            VitalsAnthropometryModel sparse = new VitalsAnthropometryModel();
            sparse.setPulseRate((short) 72);
            sparse.setCreatedDate(CREATED);

            List<Observation> observations = buildVitals(sparse);

            assertEquals(1, observations.size());
            assertEquals("Pulse Rate", observations.get(0).getCode().getText());
        }

        @Test
        @DisplayName("should build nothing when the visit recorded no vitals at all")
        void getObservationVitals_shouldBuildNothingWithoutRows() {
            assertTrue(buildVitals(null).isEmpty());
            assertTrue(buildVitals(new VitalsAnthropometryModel()).isEmpty());
        }

        private LabTestAndComponentModel labRow(Integer procedureId, String component, String value, String unit) {
            LabTestAndComponentModel model = new LabTestAndComponentModel();
            model.setProcedureID(procedureId);
            model.setTestComponentName(component);
            model.setTestResultValue(value);
            model.setTestResultUnit(unit);
            model.setLoincCode("LOINC-1");
            model.setLoincValue("Hb");
            model.setCreatedDate(CREATED);
            return model;
        }

        @Test
        @DisplayName("should group the lab observations by the procedure they belong to")
        void getObservationLab_shouldGroupByProcedure() {
            when(observationDataModel.getlabTestAndComponentList(any())).thenReturn(List.of(
                    labRow(11, "Haemoglobin", "13.2", "g/dL"),
                    labRow(11, "Platelets", "250", "10*3/uL"),
                    labRow(12, "Glucose", "90", "mg/dL")));

            Map<Integer, List<Observation>> observations = resource.getObservationLab(patient(), visitRequest());

            assertEquals(2, observations.size());
            assertEquals(2, observations.get(11).size());
            assertEquals(1, observations.get(12).size());
        }

        @Test
        @DisplayName("should code each lab observation from LOINC and read its result with the unit")
        void getObservationLab_shouldCodeFromLoinc() {
            when(observationDataModel.getlabTestAndComponentList(any()))
                    .thenReturn(List.of(labRow(11, "Haemoglobin", "13.2", "g/dL")));

            Observation observation = resource.getObservationLab(patient(), visitRequest()).get(11).get(0);

            assertEquals("Observation/" + UUID, observation.getId());
            assertEquals(Observation.ObservationStatus.FINAL, observation.getStatus());
            assertEquals("LOINC-1", observation.getCode().getCodingFirstRep().getCode());
            assertEquals("Hb", observation.getCode().getCodingFirstRep().getDisplay());
            assertEquals("Haemoglobin", observation.getCode().getText());
            assertEquals("13.2 g/dL", observation.getValueCodeableConcept().getText());
            assertEquals("Patient/patient-1", observation.getSubject().getReference());
            assertEquals("Patient/patient-1", observation.getPerformerFirstRep().getReference());
        }

        @Test
        @DisplayName("should read a result with no value or unit as an empty result")
        void getObservationLab_shouldReadEmptyResult() {
            when(observationDataModel.getlabTestAndComponentList(any()))
                    .thenReturn(List.of(labRow(11, "Haemoglobin", null, null)));

            assertNull(resource.getObservationLab(patient(), visitRequest())
                    .get(11).get(0).getValueCodeableConcept().getText(),
                    "an empty result string is not carried as text at all");
        }

        @Test
        @DisplayName("should answer with no groups when the visit ran no lab test")
        void getObservationLab_shouldAnswerEmptyWithoutRows() {
            when(observationDataModel.getlabTestAndComponentList(any())).thenReturn(List.of());

            assertTrue(resource.getObservationLab(patient(), visitRequest()).isEmpty());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("PatientResource")
    class PatientTests {

        @Mock
        private CommonServiceImpl commonServiceImpl;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private PatientDemographic patientDemographic;

        @InjectMocks
        private PatientResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonServiceImpl.getUUID()).thenReturn(UUID);
        }

        private PatientDemographic demographic(String gender, Integer genderID) {
            PatientDemographic demographic = new PatientDemographic();
            demographic.setBeneficiaryRegID(BigInteger.valueOf(4321L));
            demographic.setBeneficiaryID(BigInteger.valueOf(9999L));
            demographic.setName("Asha Devi");
            demographic.setHealthIdNo("11-1111-1111-1111");
            demographic.setGender(gender);
            demographic.setGenderID(genderID);
            demographic.setDOB(CREATED);
            return demographic;
        }

        private Patient build(PatientDemographic demographic) throws FHIRException {
            when(patientDemographic.getPatientDemographic(any())).thenReturn(demographic);
            return resource.getPatientResource(visitRequest());
        }

        @Test
        @DisplayName("should build the patient keyed on the beneficiary id with an ABHA identifier")
        void getPatientResource_shouldBuildPatient() throws Exception {
            Patient built = build(demographic("Male", null));

            assertEquals("Patient/9999", built.getId());
            assertEquals("Asha Devi", built.getNameFirstRep().getText());
            assertEquals("MR", built.getIdentifierFirstRep().getType().getCodingFirstRep().getCode());
            assertEquals("11-1111-1111-1111",
                    built.getIdentifierFirstRep().getType().getCodingFirstRep().getDisplay());
            assertEquals(AdministrativeGender.MALE, built.getGender());
            assertNotNull(built.getBirthDate());
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @CsvSource({ "Male, MALE", "Female, FEMALE", "Transgender, OTHER", "male, UNKNOWN" })
        @DisplayName("should map the recorded gender name onto the FHIR administrative gender")
        void getPatientResource_shouldMapGenderName(String gender, AdministrativeGender expected) throws Exception {
            assertEquals(expected, build(demographic(gender, null)).getGender());
        }

        @ParameterizedTest(name = "genderID {0} -> {1}")
        @CsvSource({ "1, MALE", "2, FEMALE", "3, OTHER", "4, UNKNOWN" })
        @DisplayName("should fall back to the gender id when no gender name was recorded")
        void getPatientResource_shouldFallBackToGenderId(Integer genderID, AdministrativeGender expected)
                throws Exception {
            assertEquals(expected, build(demographic(null, genderID)).getGender());
        }

        @Test
        @DisplayName("should omit every optional detail the demographic record leaves blank")
        void getPatientResource_shouldOmitAbsentDetails() throws Exception {
            PatientDemographic sparse = new PatientDemographic();
            sparse.setBeneficiaryRegID(BigInteger.valueOf(4321L));
            sparse.setBeneficiaryID(BigInteger.valueOf(9999L));

            Patient built = build(sparse);

            assertTrue(built.getIdentifier().isEmpty());
            assertFalse(built.getNameFirstRep().hasText());
            assertFalse(built.hasGender());
            assertFalse(built.hasBirthDate());
        }

        @Test
        @DisplayName("should fail when the beneficiary matches no single demographic record")
        void getPatientResource_shouldFailForAmbiguousMatch() throws Exception {
            when(patientDemographic.getPatientDemographic(any())).thenReturn(new PatientDemographic());

            assertEquals("multiple patient found with given identifier / beneficiaryID",
                    assertThrows(FHIRException.class, () -> resource.getPatientResource(visitRequest()))
                            .getMessage());
        }

        @Test
        @DisplayName("should fail when the beneficiary has no demographic record at all")
        void getPatientResource_shouldFailWithoutRecord() throws Exception {
            when(patientDemographic.getPatientDemographic(any())).thenReturn(null);

            assertEquals("patient not found",
                    assertThrows(FHIRException.class, () -> resource.getPatientResource(visitRequest()))
                            .getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("CompositionResource")
    class CompositionTests {

        @Mock
        private CommonService commonService;

        @InjectMocks
        private CompositionResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private AllergyIntolerance allergy(String id) {
            AllergyIntolerance allergy = new AllergyIntolerance();
            allergy.setId(id);
            return allergy;
        }

        private Condition condition(String id) {
            Condition condition = new Condition();
            condition.setId(id);
            condition.setCode(new CodeableConcept().setText("Fever"));
            return condition;
        }

        private Observation observation(String id) {
            Observation observation = new Observation();
            observation.setId(id);
            return observation;
        }

        private Appointment appointment() {
            Appointment appointment = new Appointment();
            appointment.setId("Appointment/appointment-1");
            return appointment;
        }

        @Test
        @DisplayName("should head the composition as a final clinical consultation report")
        void getComposition_shouldHeadAsConsultationReport() {
            Composition composition = resource.getComposition(patient(), encounter(), null, null, null, null, null);

            assertEquals("Composition/" + UUID, composition.getId());
            assertEquals(Composition.CompositionStatus.FINAL, composition.getStatus());
            assertEquals("371530004", composition.getType().getCodingFirstRep().getCode());
            assertEquals("Clinical consultation report", composition.getType().getCodingFirstRep().getDisplay());
            assertEquals("OP Consultation Document", composition.getTitle());
            assertEquals("Patient/patient-1", composition.getSubject().getReference());
            assertEquals("Encounter/encounter-1", composition.getEncounter().getReference());
            assertEquals("Practitioner/MAX1456", composition.getAuthorFirstRep().getReference());
            assertNotNull(composition.getDate());
        }

        @Test
        @DisplayName("should index the allergy, follow-up, chief-complaint and examination sections in order")
        void getComposition_shouldIndexEverySection() {
            Composition composition = resource.getComposition(patient(), encounter(),
                    List.of(allergy("AllergyIntolerance/food")), appointment(),
                    List.of(condition("Condition/cc-1")), List.of(condition("Condition/dx-1")),
                    List.of(observation("Observation/1"), observation("Observation/2")));

            assertEquals(4, composition.getSection().size());
            assertEquals("Allergy Section", composition.getSection().get(0).getTitle());
            assertEquals("AllergyIntolerance/food",
                    composition.getSection().get(0).getEntryFirstRep().getReference());
            assertEquals("Follow up", composition.getSection().get(1).getTitle());
            assertEquals("736271009", composition.getSection().get(1).getCode().getCodingFirstRep().getCode());
            assertEquals("Chief Complaints", composition.getSection().get(2).getTitle());
            assertEquals("422843007", composition.getSection().get(2).getCode().getCodingFirstRep().getCode());
            assertEquals("Physical Examination", composition.getSection().get(3).getTitle());
            assertEquals("425044008", composition.getSection().get(3).getCode().getCodingFirstRep().getCode());
            assertEquals(2, composition.getSection().get(3).getEntry().size());
        }

        @Test
        @DisplayName("should leave the composition unsectioned when the visit recorded no vitals")
        void getComposition_shouldLeaveUnsectionedWithoutVitals() {
            Composition composition = resource.getComposition(patient(), encounter(),
                    List.of(allergy("AllergyIntolerance/food")), appointment(),
                    List.of(condition("Condition/cc-1")), List.of(condition("Condition/dx-1")), List.of());

            assertTrue(composition.getSection().isEmpty(),
                    "the section list is only attached alongside the physical examination section");
        }

        @Test
        @DisplayName("should index only the examination section when nothing else was recorded")
        void getComposition_shouldIndexOnlyExamination() {
            Composition composition = resource.getComposition(patient(), encounter(), List.of(), null,
                    List.of(), List.of(), List.of(observation("Observation/1")));

            assertEquals(1, composition.getSection().size());
            assertEquals("Physical Examination", composition.getSectionFirstRep().getTitle());
        }
    }
}
