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

import java.sql.Timestamp;
import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.MedicationRequest;
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

import com.wipro.fhir.data.resource_model.ConditionChiefComplaintsDataModel;
import com.wipro.fhir.data.resource_model.ConditionDiagnosisDataModel;
import com.wipro.fhir.data.resource_model.EncounterDataModel;
import com.wipro.fhir.data.resource_model.MedicationRequestDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;

import static com.wipro.fhir.service.resource_model.SimpleResourceBuildersTest.patient;
import static com.wipro.fhir.service.resource_model.SimpleResourceBuildersTest.practitioner;
import static com.wipro.fhir.service.resource_model.SimpleResourceBuildersTest.visitRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The clinical FHIR builders that hang off a visit: conditions, the encounter that ties
 * them together, and the medication requests raised against a diagnosis.
 */
@DisplayName("Clinical FHIR builders Test Suite")
class ClinicalResourceBuildersTest {

    private static final Timestamp CREATED = new Timestamp(1_700_000_000_000L);
    private static final String UUID = "uuid-1";

    private static Condition condition(String id, String text) {
        Condition condition = new Condition();
        condition.setId(id);
        condition.setCode(new CodeableConcept().setText(text));
        return condition;
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("ConditionResource")
    class ConditionTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private ConditionDiagnosisDataModel conditionDataModel;

        @Mock
        private ConditionChiefComplaintsDataModel conditionChiefComplaintsDataModel;

        @InjectMocks
        private ConditionResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private ConditionChiefComplaintsDataModel chiefComplaint(String code) {
            ConditionChiefComplaintsDataModel model = new ConditionChiefComplaintsDataModel();
            model.setChiefComplaint("Fever");
            model.setSCTCode(code);
            model.setCreatedDate(CREATED);
            return model;
        }

        private ConditionDiagnosisDataModel diagnosis(String code, String term) {
            ConditionDiagnosisDataModel model = new ConditionDiagnosisDataModel();
            model.setSctcode(code);
            model.setSctTerm(term);
            model.setCreatedDate(CREATED);
            return model;
        }

        @Test
        @DisplayName("should build a chief complaint as an active problem-list item")
        void getCondition_shouldBuildChiefComplaint() {
            when(conditionChiefComplaintsDataModel.getConditionChiefComplaintList(any()))
                    .thenReturn(List.of(chiefComplaint("SCT-2")));

            List<Condition> conditions = resource.getCondition(patient(), visitRequest(), "chiefcomplaints");

            assertEquals(1, conditions.size());
            Condition condition = conditions.get(0);
            assertEquals("Condition/" + UUID, condition.getId());
            assertEquals("active", condition.getClinicalStatus().getCodingFirstRep().getCode());
            assertEquals("problem-list-item", condition.getCategoryFirstRep().getCodingFirstRep().getCode());
            assertEquals("SCT-2", condition.getCode().getCodingFirstRep().getCode());
            assertEquals("http://snomed.info/sct", condition.getCode().getCodingFirstRep().getSystem());
            assertEquals("Fever", condition.getCode().getText());
            assertEquals("Patient/patient-1", condition.getSubject().getReference());
        }

        @Test
        @DisplayName("should leave an uncoded chief complaint without a SNOMED system")
        void getCondition_shouldLeaveChiefComplaintUncoded() {
            when(conditionChiefComplaintsDataModel.getConditionChiefComplaintList(any()))
                    .thenReturn(List.of(chiefComplaint(null)));

            Condition condition = resource.getCondition(patient(), visitRequest(), "chiefcomplaints").get(0);

            assertFalse(condition.getCode().getCodingFirstRep().hasSystem());
            assertEquals("Fever", condition.getCode().getCodingFirstRep().getDisplay());
        }

        @Test
        @DisplayName("should build a diagnosis as an active encounter-diagnosis")
        void getCondition_shouldBuildDiagnosis() {
            when(conditionDataModel.getConditionList(any()))
                    .thenReturn(List.of(diagnosis("SCT-3", "Malaria")));

            List<Condition> conditions = resource.getCondition(patient(), visitRequest(), "diagnosis");

            assertEquals(1, conditions.size());
            Condition condition = conditions.get(0);
            assertEquals("encounter-diagnosis", condition.getCategoryFirstRep().getCodingFirstRep().getCode());
            assertEquals("SCT-3", condition.getCode().getCodingFirstRep().getCode());
            assertEquals("Malaria", condition.getCode().getText());
        }

        @Test
        @DisplayName("should leave an uncoded, untermed diagnosis without a code or text")
        void getCondition_shouldLeaveDiagnosisUncoded() {
            when(conditionDataModel.getConditionList(any())).thenReturn(List.of(diagnosis(null, null)));

            Condition condition = resource.getCondition(patient(), visitRequest(), "diagnosis").get(0);

            assertFalse(condition.getCode().getCodingFirstRep().hasCode());
            assertFalse(condition.getCode().hasText());
        }

        @Test
        @DisplayName("should build nothing for a condition type it does not recognise")
        void getCondition_shouldAnswerNullForUnknownType() {
            assertNull(resource.getCondition(patient(), visitRequest(), "allergies"));
            assertNull(resource.getCondition(patient(), visitRequest(), null));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("EncounterResource")
    class EncounterTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private EncounterDataModel encounterDataModel;

        @InjectMocks
        private EncounterResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private EncounterDataModel model(Integer nurseFlag, Integer docFlag, Integer specialistFlag) {
            EncounterDataModel model = new EncounterDataModel();
            model.setNurseFlag(nurseFlag);
            model.setDocFlag(docFlag);
            model.setSpecialistFlag(specialistFlag);
            model.setCreatedDate(CREATED);
            return model;
        }

        private Encounter build(EncounterDataModel model) {
            when(encounterDataModel.getEncounterList(any()))
                    .thenReturn(model == null ? List.of() : List.of(model));
            return resource.getEncounterResource(patient(), visitRequest(), List.of(), List.of());
        }

        /**
         * The specialist flag wins over the doctor flag, which wins over the nurse flag;
         * within a flag, 1 is planned, 2 and 3 are in progress and 9 is finished.
         */
        @ParameterizedTest(name = "nurse={0} doc={1} specialist={2} -> {3}")
        @CsvSource({
                "0, 0, 1, PLANNED",
                "0, 0, 2, INPROGRESS",
                "0, 0, 3, INPROGRESS",
                "0, 0, 9, FINISHED",
                "0, 1, 0, PLANNED",
                "0, 2, 0, INPROGRESS",
                "0, 3, 0, INPROGRESS",
                "0, 9, 0, FINISHED",
                "1, 0, 0, PLANNED",
                "2, 0, 0, INPROGRESS",
                "3, 0, 0, INPROGRESS",
                "9, 0, 0, INPROGRESS",
                "0, 0, 0, UNKNOWN",
                "0, 0, 7, UNKNOWN",
        })
        @DisplayName("should derive the encounter status from the most senior workflow flag set")
        void getEncounterResource_shouldDeriveStatusFromFlags(Integer nurseFlag, Integer docFlag,
                Integer specialistFlag, Encounter.EncounterStatus expected) {
            assertEquals(expected, build(model(nurseFlag, docFlag, specialistFlag)).getStatus());
        }

        @Test
        @DisplayName("should record the encounter as ambulatory and date its period from the row")
        void getEncounterResource_shouldRecordAmbulatoryClassAndPeriod() {
            Encounter encounter = build(model(1, 0, 0));

            assertEquals("Encounter/" + UUID, encounter.getId());
            assertEquals("AMB", encounter.getClass_().getCode());
            assertEquals("ambulatory", encounter.getClass_().getDisplay());
            assertEquals("Patient/patient-1", encounter.getSubject().getReference());
            assertEquals(CREATED.getTime(), encounter.getPeriod().getStart().getTime());
        }

        @Test
        @DisplayName("should leave the status and period unset when the visit has no encounter row")
        void getEncounterResource_shouldLeaveStatusUnsetWithoutRows() {
            Encounter encounter = build(null);

            assertFalse(encounter.hasStatus());
            assertFalse(encounter.hasPeriod());
            assertEquals("AMB", encounter.getClass_().getCode(), "the class is set regardless");
        }

        @Test
        @DisplayName("should reference each chief complaint and diagnosis with its own SNOMED use code")
        void getEncounterResource_shouldReferenceConditions() {
            when(encounterDataModel.getEncounterList(any())).thenReturn(List.of(model(1, 0, 0)));

            Encounter encounter = resource.getEncounterResource(patient(), visitRequest(),
                    List.of(condition("Condition/cc-1", "Fever")),
                    List.of(condition("Condition/dx-1", "Malaria"), condition("Condition/dx-2", "Anaemia")));

            assertEquals(3, encounter.getDiagnosis().size());
            assertEquals("Condition/cc-1", encounter.getDiagnosis().get(0).getCondition().getReference());
            assertEquals("33962009",
                    encounter.getDiagnosis().get(0).getUse().getCodingFirstRep().getCode());
            assertEquals("Chief complaint",
                    encounter.getDiagnosis().get(0).getUse().getCodingFirstRep().getDisplay());
            assertEquals("Condition/dx-1", encounter.getDiagnosis().get(1).getCondition().getReference());
            assertEquals("148006", encounter.getDiagnosis().get(1).getUse().getCodingFirstRep().getCode());
            assertEquals("Provisional diagnosis",
                    encounter.getDiagnosis().get(2).getUse().getCodingFirstRep().getDisplay());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("MedicationRequestResource")
    class MedicationRequestTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private MedicationRequestDataModel medicationRequestDataModel;

        @InjectMocks
        private MedicationRequestResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private MedicationRequestDataModel model() {
            MedicationRequestDataModel model = new MedicationRequestDataModel();
            model.setGenericDrugName("Paracetamol");
            model.setSnomedCTCodeDrug("SCT-6");
            model.setSnomedCTTermDrug("Paracetamol 500mg tablet");
            model.setDrugDose("1");
            model.setDrugFrequency("TID");
            model.setDuration("5");
            model.setDurationUnit("Days");
            model.setDrugRoute("Oral");
            model.setInstructions("after food");
            model.setCreatedDate(CREATED);
            return model;
        }

        private List<MedicationRequest> build(MedicationRequestDataModel model, List<Condition> diagnoses) {
            when(medicationRequestDataModel.getMedicationRequestList(any())).thenReturn(List.of(model));
            return resource.getMedicationRequest(patient(), visitRequest(), practitioner(), diagnoses);
        }

        @Test
        @DisplayName("should raise an active medication order coded from SNOMED")
        void getMedicationRequest_shouldRaiseActiveOrder() {
            MedicationRequest request = build(model(), List.of()).get(0);

            assertEquals("MedicationRequest-1/" + UUID, request.getId());
            assertEquals(MedicationRequest.MedicationRequestStatus.ACTIVE, request.getStatus());
            assertEquals(MedicationRequest.MedicationRequestIntent.ORDER, request.getIntent());
            assertEquals("Patient/patient-1", request.getSubject().getReference());
            assertEquals("Practitioner/55", request.getRequester().getReference());
            assertEquals("SCT-6", request.getMedicationCodeableConcept().getCodingFirstRep().getCode());
            assertEquals("Paracetamol", request.getMedicationCodeableConcept().getText());
        }

        @Test
        @DisplayName("should spell the dosage out as dose, frequency, duration and remarks")
        void getMedicationRequest_shouldSpellOutDosage() {
            MedicationRequest request = build(model(), List.of()).get(0);

            assertEquals("1 TID for 5 Days. Remarks : after food",
                    request.getDosageInstructionFirstRep().getText());
            assertEquals("Remarks : after food", request.getDosageInstructionFirstRep()
                    .getAdditionalInstructionFirstRep().getText());
            assertEquals("Oral", request.getDosageInstructionFirstRep().getRoute().getText());
        }

        @Test
        @DisplayName("should fill the gaps in the dosage text when the row is sparse")
        void getMedicationRequest_shouldFillDosageGaps() {
            MedicationRequestDataModel sparse = model();
            sparse.setDrugDose(null);
            sparse.setDrugFrequency(null);
            sparse.setDuration(null);
            sparse.setDurationUnit(null);
            sparse.setDrugRoute(null);
            sparse.setInstructions(null);

            MedicationRequest request = build(sparse, List.of()).get(0);

            assertEquals(" for  . Remarks : ", request.getDosageInstructionFirstRep().getText());
            assertEquals("Remarks : N/A", request.getDosageInstructionFirstRep()
                    .getAdditionalInstructionFirstRep().getText());
            assertEquals("route not available", request.getDosageInstructionFirstRep().getRoute().getText());
        }

        @Test
        @DisplayName("should cite every diagnosis as the reason for the order")
        void getMedicationRequest_shouldCiteDiagnoses() {
            MedicationRequest request = build(model(),
                    List.of(condition("Condition/dx-1", "Malaria"), condition("Condition/dx-2", "Anaemia")))
                    .get(0);

            assertEquals(2, request.getReasonReference().size());
            assertEquals("Condition/dx-1", request.getReasonReference().get(0).getReference());
            assertEquals(2, request.getReasonCode().size());
            assertEquals("Malaria", request.getReasonCode().get(0).getText());
        }

        @Test
        @DisplayName("should cite no reason when the visit reached no diagnosis")
        void getMedicationRequest_shouldCiteNoReasonWithoutDiagnosis() {
            assertTrue(build(model(), null).get(0).getReasonReference().isEmpty());
            assertTrue(build(model(), List.of()).get(0).getReasonCode().isEmpty());
        }

        @Test
        @DisplayName("should number each order in row order")
        void getMedicationRequest_shouldNumberOrders() {
            when(medicationRequestDataModel.getMedicationRequestList(any()))
                    .thenReturn(List.of(model(), model()));

            List<MedicationRequest> requests =
                    resource.getMedicationRequest(patient(), visitRequest(), practitioner(), List.of());

            assertEquals("MedicationRequest-1/" + UUID, requests.get(0).getId());
            assertEquals("MedicationRequest-2/" + UUID, requests.get(1).getId());
        }
    }
}
