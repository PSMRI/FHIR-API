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

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
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
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.AllergyIntoleranceDataModel;
import com.wipro.fhir.data.resource_model.AppointmentDataModel;
import com.wipro.fhir.data.resource_model.DiagnosticReportDataModel;
import com.wipro.fhir.data.resource_model.FamilyMemberHistoryDataModel;
import com.wipro.fhir.data.resource_model.ImmunizationDataModel;
import com.wipro.fhir.data.resource_model.MedicalHistoryDataModel;
import com.wipro.fhir.data.resource_model.OrganizationDataModel;
import com.wipro.fhir.data.resource_model.PractitionerDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.common.CommonServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * The single-resource FHIR builders: each pulls its rows from a stored procedure, maps
 * them through its data model, and assembles one HL7 R4 resource. These tests drive the
 * builders with typed models so the assertions are about the generated FHIR, not the SQL.
 */
@DisplayName("Single-resource FHIR builders Test Suite")
class SimpleResourceBuildersTest {

    private static final Timestamp CREATED = new Timestamp(1_700_000_000_000L);
    private static final String UUID = "uuid-1";

    static ResourceRequestHandler visitRequest() {
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setBeneficiaryRegID(BigInteger.valueOf(4321L));
        request.setVisitCode(BigInteger.valueOf(987654L));
        return request;
    }

    static Patient patient() {
        Patient patient = new Patient();
        patient.setId("Patient/patient-1");
        return patient;
    }

    static Practitioner practitioner() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("Practitioner/55");
        practitioner.addName(new HumanName().setText("Dr Rao"));
        return practitioner;
    }

    static Encounter encounter() {
        Encounter encounter = new Encounter();
        encounter.setId("Encounter/encounter-1");
        return encounter;
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("MedicalHistoryResource")
    class MedicalHistoryTests {

        @Mock
        private CommonService commonService;

        @Mock
        private MedicalHistoryDataModel medicalHistoryDataModel;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @InjectMocks
        private MedicalHistoryResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private MedicalHistoryDataModel model(String medication) {
            MedicalHistoryDataModel model = new MedicalHistoryDataModel();
            model.setCurrentMedication(medication);
            model.setCreatedDate(CREATED);
            return model;
        }

        @Test
        @DisplayName("should build one completed medication statement per row of current medication")
        void getMedicalHistory_shouldBuildOneStatementPerRow() throws Exception {
            when(medicalHistoryDataModel.getMedicalList(any()))
                    .thenReturn(List.of(model("Metformin"), model("Amlodipine")));

            List<MedicationStatement> statements = resource.getMedicalHistory(patient(), visitRequest());

            assertEquals(2, statements.size());
            MedicationStatement first = statements.get(0);
            assertEquals("MedicationRequest-1/" + UUID, first.getId());
            assertEquals(MedicationStatement.MedicationStatementStatus.COMPLETED, first.getStatus());
            assertEquals("Patient/patient-1", first.getSubject().getReference());
            assertEquals("Metformin", first.getMedicationCodeableConcept().getText());
            assertEquals("http://snomed.info/sct",
                    first.getMedicationCodeableConcept().getCodingFirstRep().getSystem());
            assertEquals(CREATED, first.getDateAsserted());
            assertTrue(first.getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/MedicationStatement"));
            assertEquals("MedicationRequest-2/" + UUID, statements.get(1).getId(),
                    "each statement is numbered in row order");
        }

        @Test
        @DisplayName("should build nothing when the visit recorded no medication")
        void getMedicalHistory_shouldBuildNothingWithoutRows() throws Exception {
            when(medicalHistoryDataModel.getMedicalList(any())).thenReturn(List.of());

            assertTrue(resource.getMedicalHistory(patient(), visitRequest()).isEmpty());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("ImmunizationResource")
    class ImmunizationTests {

        @Mock
        private CommonService commonService;

        @Mock
        private ImmunizationDataModel immunizationDataModel;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @InjectMocks
        private ImmunizationResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private ImmunizationDataModel model() {
            ImmunizationDataModel model = new ImmunizationDataModel();
            model.setVaccineName("BCG");
            model.setSctcode("SCT-5");
            model.setSctTerm("BCG vaccination");
            model.setReceivedDate(CREATED);
            model.setCreatedDate(CREATED);
            model.setDefaultReceivingAge("at birth");
            model.setReceivedFacilityName("PHC Kanke");
            return model;
        }

        @Test
        @DisplayName("should mark an administered vaccine completed and carry its schedule and facility notes")
        void getImmunizations_shouldBuildCompletedImmunization() throws Exception {
            when(immunizationDataModel.getImmunizationList(any())).thenReturn(List.of(model()));

            List<Immunization> immunizations = resource.getImmunizations(patient(), visitRequest());

            assertEquals(1, immunizations.size());
            Immunization immunization = immunizations.get(0);
            assertEquals("Immunization-1/" + UUID, immunization.getId());
            assertEquals(Immunization.ImmunizationStatus.COMPLETED, immunization.getStatus());
            assertEquals("Patient/patient-1", immunization.getPatient().getReference());
            assertEquals("SCT-5", immunization.getVaccineCode().getCodingFirstRep().getCode());
            assertEquals("BCG vaccination", immunization.getVaccineCode().getCodingFirstRep().getDisplay());
            assertEquals("BCG", immunization.getVaccineCode().getText());
            assertEquals(2, immunization.getNote().size());
            assertEquals("Schedule: at birth", immunization.getNote().get(0).getText());
            assertEquals("Facility: PHC Kanke", immunization.getNote().get(1).getText());
            assertNotNull(immunization.getRecorded());
        }

        @Test
        @DisplayName("should mark a vaccine with no administration date as not done")
        void getImmunizations_shouldMarkMissingDateAsNotDone() throws Exception {
            ImmunizationDataModel model = model();
            model.setReceivedDate(null);
            when(immunizationDataModel.getImmunizationList(any())).thenReturn(List.of(model));

            Immunization immunization = resource.getImmunizations(patient(), visitRequest()).get(0);

            assertEquals(Immunization.ImmunizationStatus.NOTDONE, immunization.getStatus());
            assertFalse(immunization.hasOccurrence());
        }

        @Test
        @DisplayName("should fall back to the vaccine name when the row carries no SNOMED term")
        void getImmunizations_shouldFallBackToVaccineName() throws Exception {
            ImmunizationDataModel model = model();
            model.setSctTerm(null);
            when(immunizationDataModel.getImmunizationList(any())).thenReturn(List.of(model));

            assertEquals("BCG", resource.getImmunizations(patient(), visitRequest()).get(0)
                    .getVaccineCode().getCodingFirstRep().getDisplay());
        }

        @Test
        @DisplayName("should leave the vaccine uncoded when the row carries no SNOMED code")
        void getImmunizations_shouldLeaveVaccineUncoded() throws Exception {
            ImmunizationDataModel model = model();
            model.setSctcode("");
            model.setDefaultReceivingAge("");
            model.setReceivedFacilityName("");
            when(immunizationDataModel.getImmunizationList(any())).thenReturn(List.of(model));

            Immunization immunization = resource.getImmunizations(patient(), visitRequest()).get(0);

            assertTrue(immunization.getVaccineCode().getCoding().isEmpty());
            assertTrue(immunization.getNote().isEmpty(), "blank schedule and facility add no notes");
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("OrganizationResource")
    class OrganizationTests {

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private OrganizationDataModel organizationDataModel;

        @InjectMocks
        private OrganizationResource resource;

        private OrganizationDataModel model() {
            OrganizationDataModel model = new OrganizationDataModel();
            model.setServiceProviderID((short) 7);
            model.setServiceProviderName("PHC Kanke");
            model.setLocationName("Kanke");
            model.setAbdmFacilityId("IN0710000001");
            model.setAddress("Kanke Road");
            model.setDistrictName("Ranchi");
            model.setStateName("Jharkhand");
            return model;
        }

        @Test
        @DisplayName("should build the organization with its ABDM identifier and Indian address")
        void getOrganizationResource_shouldBuildOrganization() throws Exception {
            when(repo.callOrganizationSp(BigInteger.valueOf(987654L)))
                    .thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(organizationDataModel.getOrganization(any())).thenReturn(model());

            Organization organization = resource.getOrganizationResource(visitRequest());

            assertEquals("Organization/7", organization.getId());
            assertEquals("PHC Kanke", organization.getName());
            assertEquals("Kanke", organization.getAlias().get(0).getValue());
            assertEquals("IN0710000001", organization.getIdentifierFirstRep().getValue());
            assertEquals("https://facilitysbx.ndhm.gov.in", organization.getIdentifierFirstRep().getSystem());
            assertEquals("Kanke Road", organization.getAddressFirstRep().getLine().get(0).getValue());
            assertEquals("Ranchi", organization.getAddressFirstRep().getDistrict());
            assertEquals("Jharkhand", organization.getAddressFirstRep().getState());
            assertEquals("India", organization.getAddressFirstRep().getCountry());
        }

        @Test
        @DisplayName("should omit every optional detail the row leaves blank")
        void getOrganizationResource_shouldOmitAbsentDetails() throws Exception {
            when(repo.callOrganizationSp(any())).thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(organizationDataModel.getOrganization(any())).thenReturn(new OrganizationDataModel());

            Organization organization = resource.getOrganizationResource(visitRequest());

            assertFalse(organization.hasName());
            assertTrue(organization.getAlias().isEmpty());
            assertTrue(organization.getIdentifier().isEmpty());
            assertEquals("India", organization.getAddressFirstRep().getCountry());
        }

        @Test
        @DisplayName("should fail when the visit maps to no organization row")
        void getOrganizationResource_shouldFailWithoutRows() {
            when(repo.callOrganizationSp(any())).thenReturn(List.of());

            assertEquals("Organization not found",
                    assertThrows(FHIRException.class, () -> resource.getOrganizationResource(visitRequest()))
                            .getMessage());
        }

        @Test
        @DisplayName("should fail when the organization row cannot be mapped")
        void getOrganizationResource_shouldFailWhenRowUnmappable() throws Exception {
            when(repo.callOrganizationSp(any())).thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(organizationDataModel.getOrganization(any())).thenReturn(null);

            assertEquals("Organization data not found",
                    assertThrows(FHIRException.class, () -> resource.getOrganizationResource(visitRequest()))
                            .getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("PractitionerResource")
    class PractitionerTests {

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private PractitionerDataModel practitionerDataModel;

        @InjectMocks
        private PractitionerResource resource;

        @BeforeEach
        void configureSystemUrl() {
            ReflectionTestUtils.setField(resource, "systemUrl", "https://hip.example.org");
        }

        private PractitionerDataModel model(String gender) {
            PractitionerDataModel model = new PractitionerDataModel();
            model.setUserID(55);
            model.setEmployeeID("EMP-1");
            model.setFullName("Dr Rao");
            model.setDesignationName("Medical Officer");
            model.setQualificationName("MBBS");
            model.setGenderName(gender);
            model.setDob(new java.util.Date(1_000_000_000_000L));
            model.setContactNo("9999999999");
            model.setEmailID("rao@example.org");
            return model;
        }

        @Test
        @DisplayName("should build the practitioner with the HIP identifier, name parts and both contacts")
        void getPractitionerResource_shouldBuildPractitioner() throws Exception {
            when(repo.callPractitionerSP(BigInteger.valueOf(987654L)))
                    .thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(practitionerDataModel.getPractitioner(any())).thenReturn(model("Male"));

            Practitioner practitioner = resource.getPractitionerResource(visitRequest());

            assertEquals("Practitioner/55", practitioner.getId());
            assertEquals("https://hip.example.org", practitioner.getIdentifierFirstRep().getSystem());
            assertEquals("EMP-1", practitioner.getIdentifierFirstRep().getValue());
            HumanName name = practitioner.getNameFirstRep();
            assertEquals("Dr Rao", name.getText());
            assertEquals("Medical Officer", name.getPrefix().get(0).getValue());
            assertEquals("MBBS", name.getSuffix().get(0).getValue());
            assertEquals(AdministrativeGender.MALE, practitioner.getGender());
            assertNotNull(practitioner.getBirthDate());
            assertEquals(2, practitioner.getTelecom().size());
            assertEquals("9999999999", practitioner.getTelecom().get(0).getValue());
            assertEquals("rao@example.org", practitioner.getTelecom().get(1).getValue());
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @CsvSource({ "Male, MALE", "Female, FEMALE", "Transgender, UNKNOWN", "male, UNKNOWN" })
        @DisplayName("should map the recorded gender onto the FHIR administrative gender")
        void getPractitionerResource_shouldMapGender(String genderName, AdministrativeGender expected)
                throws Exception {
            when(repo.callPractitionerSP(any())).thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(practitionerDataModel.getPractitioner(any())).thenReturn(model(genderName));

            assertEquals(expected, resource.getPractitionerResource(visitRequest()).getGender());
        }

        @Test
        @DisplayName("should omit every optional detail the row leaves blank")
        void getPractitionerResource_shouldOmitAbsentDetails() throws Exception {
            when(repo.callPractitionerSP(any())).thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(practitionerDataModel.getPractitioner(any())).thenReturn(new PractitionerDataModel());

            Practitioner practitioner = resource.getPractitionerResource(visitRequest());

            assertTrue(practitioner.getIdentifier().isEmpty());
            assertTrue(practitioner.getTelecom().isEmpty());
            assertFalse(practitioner.hasGender());
            assertFalse(practitioner.hasBirthDate());
        }

        @Test
        @DisplayName("should fail when the visit maps to no practitioner row")
        void getPractitionerResource_shouldFailWithoutRows() {
            when(repo.callPractitionerSP(any())).thenReturn(List.of());

            assertEquals("invalid practitioner data",
                    assertThrows(FHIRException.class, () -> resource.getPractitionerResource(visitRequest()))
                            .getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("AppointmentResource")
    class AppointmentTests {

        @Mock
        private CommonServiceImpl commonServiceImpl;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private AppointmentDataModel appointmentDataModel;

        @InjectMocks
        private AppointmentResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonServiceImpl.getUUID()).thenReturn(UUID);
        }

        private AppointmentDataModel model(String status) {
            AppointmentDataModel model = new AppointmentDataModel();
            model.setStatus(status);
            return model;
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @CsvSource({ "A, ARRIVED", "C, CANCELLED", "D, FULFILLED", "N, BOOKED", "X, PROPOSED" })
        @DisplayName("should map the stored status letter onto the FHIR appointment status")
        void getAppointmentResource_shouldMapStatus(String stored,
                org.hl7.fhir.r4.model.Appointment.AppointmentStatus expected) {
            when(appointmentDataModel.getAppointmentList(any())).thenReturn(List.of(model(stored)));

            assertEquals(expected,
                    resource.getAppointmentResource(visitRequest(), practitioner()).getStatus());
        }

        @Test
        @DisplayName("should propose the appointment when the visit has no appointment row")
        void getAppointmentResource_shouldProposeWithoutRows() {
            when(appointmentDataModel.getAppointmentList(any())).thenReturn(List.of());

            assertEquals(org.hl7.fhir.r4.model.Appointment.AppointmentStatus.PROPOSED,
                    resource.getAppointmentResource(visitRequest(), practitioner()).getStatus());
        }

        @Test
        @DisplayName("should record the practitioner as an accepted participant")
        void getAppointmentResource_shouldRecordPractitionerParticipant() {
            when(appointmentDataModel.getAppointmentList(any())).thenReturn(List.of(model("A")));

            org.hl7.fhir.r4.model.Appointment appointment =
                    resource.getAppointmentResource(visitRequest(), practitioner());

            assertEquals("Appointment/" + UUID, appointment.getId());
            assertEquals(1, appointment.getParticipant().size());
            assertEquals(org.hl7.fhir.r4.model.Appointment.ParticipationStatus.ACCEPTED,
                    appointment.getParticipantFirstRep().getStatus());
            assertEquals("Practitioner/55", appointment.getParticipantFirstRep().getActor().getReference());
            assertEquals("Dr Rao", appointment.getParticipantFirstRep().getActor().getDisplay());
            assertTrue(appointment.getEnd().after(appointment.getStart()));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("FamilyMemberHistoryResource")
    class FamilyMemberHistoryTests {

        @Mock
        private CommonServiceImpl commonServiceImpl;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private FamilyMemberHistoryDataModel familyMemberHistoryDataModel;

        @InjectMocks
        private FamilyMemberHistoryResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonServiceImpl.getUUID()).thenReturn(UUID);
        }

        private FamilyMemberHistoryDataModel model(String members, String code, String term) {
            FamilyMemberHistoryDataModel model = new FamilyMemberHistoryDataModel();
            model.setFamilyMembers(members);
            model.setSctcode(code);
            model.setSctTerm(term);
            return model;
        }

        @Test
        @DisplayName("should record one relationship coding per named family member")
        void getFamilyMemberHistory_shouldRecordOneCodingPerMember() {
            when(familyMemberHistoryDataModel.getFamilyMemberHistoryList(any()))
                    .thenReturn(List.of(model("Mother, Father", "SCT-4", "Diabetes mellitus")));

            org.hl7.fhir.r4.model.FamilyMemberHistory history =
                    resource.getFamilyMemberHistory(patient(), visitRequest());

            assertEquals(UUID, history.getId());
            assertEquals(org.hl7.fhir.r4.model.FamilyMemberHistory.FamilyHistoryStatus.HEALTHUNKNOWN,
                    history.getStatus());
            assertEquals(2, history.getRelationship().getCoding().size());
            assertEquals(2, history.getCondition().size());
            assertEquals("Patient/patient-1", history.getPatient().getReference());
        }

        @Test
        @DisplayName("should gather every disease a single relation carries under that one relation")
        void getFamilyMemberHistory_shouldGroupDiseasesByRelation() {
            when(familyMemberHistoryDataModel.getFamilyMemberHistoryList(any()))
                    .thenReturn(List.of(model("Mother", "SCT-4", "Diabetes mellitus"),
                            model("Mother", "SCT-9", "Hypertension")));

            org.hl7.fhir.r4.model.FamilyMemberHistory history =
                    resource.getFamilyMemberHistory(patient(), visitRequest());

            assertEquals(1, history.getRelationship().getCoding().size());
            assertEquals("Mother", history.getRelationship().getCodingFirstRep().getCode());
            assertEquals(2, history.getConditionFirstRep().getCode().getCoding().size());
            assertEquals("http://snomed.info/sct",
                    history.getConditionFirstRep().getCode().getCodingFirstRep().getSystem());
        }

        @Test
        @DisplayName("should skip rows that name no family member at all")
        void getFamilyMemberHistory_shouldSkipRowsWithoutMembers() {
            List<FamilyMemberHistoryDataModel> models = new ArrayList<>();
            models.add(model(null, "SCT-4", "Diabetes mellitus"));
            models.add(model("", "SCT-4", "Diabetes mellitus"));
            models.add(null);
            when(familyMemberHistoryDataModel.getFamilyMemberHistoryList(any())).thenReturn(models);

            org.hl7.fhir.r4.model.FamilyMemberHistory history =
                    resource.getFamilyMemberHistory(patient(), visitRequest());

            assertTrue(history.getRelationship().getCoding().isEmpty());
            assertTrue(history.getCondition().isEmpty());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("AllergyIntoleranceResource")
    class AllergyIntoleranceTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private AllergyIntoleranceDataModel allergyIntoleranceDataModel;

        @InjectMocks
        private AllergyIntoleranceResource resource;

        private AllergyIntoleranceDataModel model(String allergyType, String reaction) {
            AllergyIntoleranceDataModel model = new AllergyIntoleranceDataModel();
            model.setAllergyType(allergyType);
            model.setSctcode("SCT-1");
            model.setSctTerm("Peanut butter");
            model.setAllergicReactionType(reaction);
            model.setCreatedDate(CREATED);
            return model;
        }

        private List<org.hl7.fhir.r4.model.AllergyIntolerance> build(
                List<AllergyIntoleranceDataModel> models) {
            when(allergyIntoleranceDataModel.getAllergyList(any())).thenReturn(models);
            return resource.getAllergyIntolerance(patient(), encounter(), visitRequest(), practitioner());
        }

        @Test
        @DisplayName("should mark the allergy active and unconfirmed, and code it from SNOMED")
        void getAllergyIntolerance_shouldBuildActiveUnconfirmedAllergy() {
            org.hl7.fhir.r4.model.AllergyIntolerance allergy = build(List.of(model("food", "rash"))).get(0);

            assertEquals("active", allergy.getClinicalStatus().getCodingFirstRep().getCode());
            assertEquals("unconfirmed", allergy.getVerificationStatus().getCodingFirstRep().getCode());
            assertEquals(org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceType.ALLERGY,
                    allergy.getType());
            assertEquals("SCT-1", allergy.getCode().getCodingFirstRep().getCode());
            assertEquals("Peanut butter", allergy.getCode().getCodingFirstRep().getDisplay());
            assertEquals("Having these side effects : rash", allergy.getNote().get(0).getText());
            assertEquals(CREATED, allergy.getRecordedDate());
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @CsvSource({ "drugs, MEDICATION", "food, FOOD", "environmental, ENVIRONMENT" })
        @DisplayName("should map the recorded allergy type onto the FHIR category")
        void getAllergyIntolerance_shouldMapCategory(String allergyType,
                org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory expected) {
            org.hl7.fhir.r4.model.AllergyIntolerance allergy = build(List.of(model(allergyType, "rash"))).get(0);

            assertEquals(expected, allergy.getCategory().get(0).getValue());
        }

        @Test
        @DisplayName("should leave an unrecognised allergy type uncategorised")
        void getAllergyIntolerance_shouldLeaveUnknownTypeUncategorised() {
            org.hl7.fhir.r4.model.AllergyIntolerance allergy = build(List.of(model("unknown", null))).get(0);

            assertTrue(allergy.getCategory().isEmpty());
            assertTrue(allergy.getNote().isEmpty(), "no reaction recorded means no note");
        }

        @Test
        @DisplayName("should reference the patient, encounter and asserting practitioner on the last allergy")
        void getAllergyIntolerance_shouldReferenceContext() {
            List<org.hl7.fhir.r4.model.AllergyIntolerance> allergies =
                    build(List.of(model("food", "rash"), model("drugs", "hives")));

            org.hl7.fhir.r4.model.AllergyIntolerance last = allergies.get(1);
            assertEquals("Patient/patient-1", last.getPatient().getReference());
            assertEquals("Encounter/encounter-1", last.getEncounter().getReference());
            assertEquals("Practitioner/55", last.getAsserter().getReference());
            assertEquals("Dr Rao", last.getAsserter().getDisplay());
        }

        @Test
        @DisplayName("should build nothing when the visit recorded no allergy")
        void getAllergyIntolerance_shouldBuildNothingWithoutRows() {
            assertTrue(build(List.of()).isEmpty());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("DiagnosticReportResource")
    class DiagnosticReportTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PatientEligibleForResourceCreationRepo repo;

        @Mock
        private DiagnosticReportDataModel diagnosticReportDataModel;

        @InjectMocks
        private DiagnosticReportResource resource;

        @BeforeEach
        void stubUuid() {
            when(commonService.getUUID()).thenReturn(UUID);
        }

        private Observation observation(String id) {
            Observation observation = new Observation();
            observation.setId(id);
            return observation;
        }

        private DiagnosticReportDataModel model() {
            DiagnosticReportDataModel model = new DiagnosticReportDataModel();
            model.setCreatedDate(CREATED);
            return model;
        }

        @Test
        @DisplayName("should gather every observation across the map into one final laboratory report")
        void getDiagnosticReport_shouldGatherObservations() throws Exception {
            when(diagnosticReportDataModel.getDiagnosticReportList(any())).thenReturn(List.of(model()));
            Map<Integer, List<Observation>> observations = Map.of(
                    11, List.of(observation("Observation/1"), observation("Observation/2")),
                    12, List.of(observation("Observation/3")));

            List<org.hl7.fhir.r4.model.DiagnosticReport> reports =
                    resource.getDiagnosticReport(patient(), encounter(), visitRequest(), observations);

            assertEquals(1, reports.size());
            org.hl7.fhir.r4.model.DiagnosticReport report = reports.get(0);
            assertEquals("DiagnosticReport/" + UUID, report.getId());
            assertEquals(org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportStatus.FINAL,
                    report.getStatus());
            assertEquals("Laboratory Report", report.getCode().getText());
            assertEquals("Patient/patient-1", report.getSubject().getReference());
            assertEquals(3, report.getResult().size());
            assertNotNull(report.getEffective());
        }

        @Test
        @DisplayName("should build no report at all when there is no observation to report on")
        void getDiagnosticReport_shouldBuildNothingWithoutObservations() throws Exception {
            when(diagnosticReportDataModel.getDiagnosticReportList(any())).thenReturn(List.of(model()));

            assertTrue(resource.getDiagnosticReport(patient(), encounter(), visitRequest(), Map.of()).isEmpty());
            assertTrue(resource.getDiagnosticReport(patient(), encounter(), visitRequest(), null).isEmpty());
        }

        @Test
        @DisplayName("should leave the report undated when no lab row carries a creation date")
        void getDiagnosticReport_shouldLeaveReportUndated() throws Exception {
            when(diagnosticReportDataModel.getDiagnosticReportList(any())).thenReturn(List.of());

            org.hl7.fhir.r4.model.DiagnosticReport report = resource.getDiagnosticReport(patient(), encounter(),
                    visitRequest(), Map.of(11, List.of(observation("Observation/1")))).get(0);

            assertFalse(report.hasEffective());
        }
    }
}
