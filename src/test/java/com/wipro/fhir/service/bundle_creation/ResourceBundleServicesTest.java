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
package com.wipro.fhir.service.bundle_creation;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationRequest;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.resource_model.AllergyIntoleranceResource;
import com.wipro.fhir.service.resource_model.ConditionResource;
import com.wipro.fhir.service.resource_model.DiagnosticReportResource;
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.FamilyMemberHistoryResource;
import com.wipro.fhir.service.resource_model.ImmunizationResource;
import com.wipro.fhir.service.resource_model.MedicalHistoryResource;
import com.wipro.fhir.service.resource_model.MedicationRequestResource;
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The document-bundle services stitch the individual FHIR resources into one NRCeS
 * document bundle and file it in Mongo. These tests drive each one with stubbed resource
 * builders, then parse the serialised bundle back to check what actually went into it.
 */
@DisplayName("FHIR document bundle services Test Suite")
class ResourceBundleServicesTest {

    private static final FhirContext FHIR = FhirContext.forR4();
    private static final String UUID = "uuid-1";
    private static final String SYSTEM_URL = "https://hip.example.org";

    static ResourceRequestHandler visitRequest() {
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setBeneficiaryRegID(BigInteger.valueOf(4321L));
        request.setVisitCode(BigInteger.valueOf(987654L));
        return request;
    }

    static PatientEligibleForResourceCreation eligibleVisit() {
        PatientEligibleForResourceCreation visit = new PatientEligibleForResourceCreation();
        visit.setBeneficiaryId(BigInteger.valueOf(9999L));
        visit.setBeneficiaryRegID(BigInteger.valueOf(4321L));
        visit.setVisitCode(BigInteger.valueOf(987654L));
        visit.setVisitCategory("General OPD");
        return visit;
    }

    static Patient patient() {
        Patient patient = new Patient();
        patient.setId("Patient/9999");
        return patient;
    }

    static Practitioner practitioner() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("Practitioner/55");
        practitioner.addName(new HumanName().setText("Dr Rao"));
        return practitioner;
    }

    static Organization organization() {
        Organization organization = new Organization();
        organization.setId("Organization/7");
        return organization;
    }

    static Encounter encounter() {
        Encounter encounter = new Encounter();
        encounter.setId("Encounter/encounter-1");
        return encounter;
    }

    static Condition condition(String id) {
        Condition condition = new Condition();
        condition.setId(id);
        condition.setCode(new CodeableConcept().setText("Fever"));
        return condition;
    }

    static Observation observation(String id) {
        Observation observation = new Observation();
        observation.setId(id);
        return observation;
    }

    static Bundle parse(String serialised) {
        return (Bundle) FHIR.newJsonParser().parseResource(serialised);
    }

    static Composition compositionOf(Bundle bundle) {
        return (Composition) bundle.getEntryFirstRep().getResource();
    }

    static void assertNrcesDocumentBundle(Bundle bundle) {
        assertEquals(Bundle.BundleType.DOCUMENT, bundle.getType());
        assertEquals("1", bundle.getMeta().getVersionId());
        assertTrue(bundle.getMeta().getProfile().get(0).getValue()
                .endsWith("StructureDefinition/DocumentBundle"));
        assertEquals("http://terminology.hl7.org/CodeSystem/v3-Confidentiality",
                bundle.getMeta().getSecurityFirstRep().getSystem());
        assertEquals(SYSTEM_URL, bundle.getIdentifier().getSystem());
        assertNotNull(bundle.getTimestamp());
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("OPConsultResourceBundleImpl")
    class OpConsultTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PractitionerResource practitionerResource;

        @Mock
        private OrganizationResource organizationResource;

        @Mock
        private PatientResource patientResource;

        @Mock
        private ConditionResource conditionResource;

        @Mock
        private EncounterResource encounterResource;

        @Mock
        private AllergyIntoleranceResource allergyIntoleranceResource;

        @Mock
        private FamilyMemberHistoryResource familyMemberHistoryResource;

        @Mock
        private MedicalHistoryResource medicalHistoryResource;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private OPConsultResourceBundleImpl service;

        @BeforeEach
        void stubResourceBuilders() throws Exception {
            ReflectionTestUtils.setField(service, "systemUrl", SYSTEM_URL);
            when(commonService.getUUID()).thenReturn(UUID);
            when(practitionerResource.getPractitionerResource(any())).thenReturn(practitioner());
            when(organizationResource.getOrganizationResource(any())).thenReturn(organization());
            when(patientResource.getPatientResource(any())).thenReturn(patient());
            when(conditionResource.getCondition(any(), any(), eq("chiefcomplaints")))
                    .thenReturn(List.of(condition("Condition/cc-1")));
            when(conditionResource.getCondition(any(), any(), eq("diagnosis")))
                    .thenReturn(List.of(condition("Condition/dx-1")));
            when(encounterResource.getEncounterResource(any(), any(), any(), any())).thenReturn(encounter());
            AllergyIntolerance allergy = new AllergyIntolerance();
            allergy.setId("AllergyIntolerance/food");
            when(allergyIntoleranceResource.getAllergyIntolerance(any(), any(), any(), any()))
                    .thenReturn(List.of(allergy));
            FamilyMemberHistory history = new FamilyMemberHistory();
            history.setId("FamilyMemberHistory/fmh-1");
            when(familyMemberHistoryResource.getFamilyMemberHistory(any(), any())).thenReturn(history);
            MedicationStatement statement = new MedicationStatement();
            statement.setId("MedicationStatement/ms-1");
            when(medicalHistoryResource.getMedicalHistory(any(), any())).thenReturn(List.of(statement));
        }

        @Test
        @DisplayName("populateOPConsultRecordResourceBundle should stitch every resource into the document bundle")
        void populate_shouldStitchEveryResource() throws Exception {
            Bundle bundle = parse(service.populateOPConsultRecordResourceBundle(visitRequest(), eligibleVisit()));

            assertNrcesDocumentBundle(bundle);
            assertEquals(9, bundle.getEntry().size(),
                    "composition, practitioner, organization, patient, 1 complaint, 1 diagnosis, "
                            + "1 allergy, 1 family history and 1 medication statement");
            assertTrue(bundle.getId().contains("987654"), bundle.getId());
        }

        @Test
        @DisplayName("populateOPConsultRecordResourceBundle should index each section of the consultation report")
        void populate_shouldIndexEverySection() throws Exception {
            Composition composition =
                    compositionOf(parse(service.populateOPConsultRecordResourceBundle(visitRequest(),
                            eligibleVisit())));

            assertEquals(Composition.CompositionStatus.FINAL, composition.getStatus());
            assertEquals("Consultation Report", composition.getTitle());
            assertEquals("Patient/9999", composition.getSubject().getReference());
            assertEquals("Practitioner/55", composition.getAuthorFirstRep().getReference());
            assertEquals("Organization/7", composition.getCustodian().getReference());
            assertTrue(composition.getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/OPConsultRecord"));
            assertEquals(List.of("Chief complaints", "Physical diagnosis", "Allergies", "Medical History",
                    "Family history"),
                    composition.getSection().stream().map(Composition.SectionComponent::getTitle).toList());
        }

        @Test
        @DisplayName("populateOPConsultRecordResourceBundle should leave out a family history that was never built")
        void populate_shouldOmitAbsentFamilyHistory() throws Exception {
            when(familyMemberHistoryResource.getFamilyMemberHistory(any(), any()))
                    .thenReturn(new FamilyMemberHistory());

            Bundle bundle = parse(service.populateOPConsultRecordResourceBundle(visitRequest(), eligibleVisit()));

            assertEquals(8, bundle.getEntry().size());
            assertEquals(4, compositionOf(bundle).getSection().size());
        }

        @Test
        @DisplayName("populateOPConsultRecordResourceBundle should wrap a failing resource builder")
        void populate_shouldWrapBuilderFailure() throws Exception {
            when(patientResource.getPatientResource(any())).thenThrow(new FHIRException("patient not found"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.populateOPConsultRecordResourceBundle(visitRequest(), eligibleVisit()))
                    .getMessage().startsWith("Op Consult FHIR Resource Bundle failed with error - "));
        }

        @Test
        @DisplayName("processOpConsultRecordBundle should file the bundle in Mongo under its ABHA")
        void process_shouldFileBundleInMongo() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(BigInteger.valueOf(987654L)))
                    .thenReturn(List.of("11-1111-1111-1111"));
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processOpConsultRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            AMRIT_ResourceMongo filed = captor.getValue();
            assertEquals("OPConsultation", filed.getResourceType());
            assertEquals(BigInteger.valueOf(9999L), filed.getBeneficiaryID());
            assertEquals(BigInteger.valueOf(987654L), filed.getVisitCode());
            assertEquals("11-1111-1111-1111", filed.getNationalHealthID());
            assertTrue(filed.getResourceJson().contains("\"resourceType\":\"Bundle\""));
        }

        @Test
        @DisplayName("processOpConsultRecordBundle should file the bundle even when no ABHA is linked")
        void process_shouldFileBundleWithoutLinkedAbha() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(any())).thenReturn(List.of());
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processOpConsultRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            assertEquals(null, captor.getValue().getNationalHealthID());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("PrescriptionResourceBundleImpl")
    class PrescriptionTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PractitionerResource practitionerResource;

        @Mock
        private PatientResource patientResource;

        @Mock
        private MedicationRequestResource medicationRequestResource;

        @Mock
        private OrganizationResource organizationResource;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private PrescriptionResourceBundleImpl service;

        @BeforeEach
        void stubResourceBuilders() throws Exception {
            ReflectionTestUtils.setField(service, "systemUrl", SYSTEM_URL);
            when(commonService.getUUID()).thenReturn(UUID);
            when(practitionerResource.getPractitionerResource(any())).thenReturn(practitioner());
            when(organizationResource.getOrganizationResource(any())).thenReturn(organization());
            when(patientResource.getPatientResource(any())).thenReturn(patient());
            MedicationRequest request = new MedicationRequest();
            request.setId("MedicationRequest/mr-1");
            when(medicationRequestResource.getMedicationRequest(any(), any(), any(), any()))
                    .thenReturn(List.of(request));
        }

        @Test
        @DisplayName("populatePrescriptionResourceBundle should carry every prescribed drug into the bundle")
        void populate_shouldCarryEveryDrug() throws Exception {
            Bundle bundle = parse(service.populatePrescriptionResourceBundle(visitRequest(), eligibleVisit()));

            assertNrcesDocumentBundle(bundle);
            assertTrue(bundle.getEntry().size() >= 5);
            Composition composition = compositionOf(bundle);
            assertTrue(composition.getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/PrescriptionRecord"));
            assertEquals(1, composition.getSection().size());
        }

        @Test
        @DisplayName("populatePrescriptionResourceBundle should wrap a failing resource builder")
        void populate_shouldWrapBuilderFailure() throws Exception {
            when(patientResource.getPatientResource(any())).thenThrow(new FHIRException("patient not found"));

            assertThrows(FHIRException.class,
                    () -> service.populatePrescriptionResourceBundle(visitRequest(), eligibleVisit()));
        }

        @Test
        @DisplayName("processPrescriptionRecordBundle should file the bundle in Mongo as a prescription")
        void process_shouldFileBundleInMongo() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(any())).thenReturn(List.of("11-1111"));
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processPrescriptionRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            assertEquals("Prescription", captor.getValue().getResourceType());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("DiagnosticRecordResourceBundleImpl")
    class DiagnosticReportTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PractitionerResource practitionerResource;

        @Mock
        private OrganizationResource organizationResource;

        @Mock
        private PatientResource patientResource;

        @Mock
        private ObservationResource observationResource;

        @Mock
        private DiagnosticReportResource diagnosticReportResource;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private DiagnosticRecordResourceBundleImpl service;

        @BeforeEach
        void stubResourceBuilders() throws Exception {
            ReflectionTestUtils.setField(service, "systemUrl", SYSTEM_URL);
            when(commonService.getUUID()).thenReturn(UUID);
            when(practitionerResource.getPractitionerResource(any())).thenReturn(practitioner());
            when(organizationResource.getOrganizationResource(any())).thenReturn(organization());
            when(patientResource.getPatientResource(any())).thenReturn(patient());
            when(observationResource.getObservationLab(any(), any()))
                    .thenReturn(Map.of(11, List.of(observation("Observation/1"))));
            DiagnosticReport report = new DiagnosticReport();
            report.setId("DiagnosticReport/dr-1");
            when(diagnosticReportResource.getDiagnosticReport(any(), any(), any(), any()))
                    .thenReturn(List.of(report));
        }

        @Test
        @DisplayName("populateDiagnosticReportResourceBundle should carry the report and its observations")
        void populate_shouldCarryReportAndObservations() throws Exception {
            Bundle bundle = parse(service.populateDiagnosticReportResourceBundle(visitRequest(), eligibleVisit()));

            assertNrcesDocumentBundle(bundle);
            assertTrue(bundle.getEntry().size() >= 5);
            assertTrue(compositionOf(bundle).getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/DiagnosticReportRecord"));
        }

        @Test
        @DisplayName("populateDiagnosticReportResourceBundle should wrap a failing resource builder")
        void populate_shouldWrapBuilderFailure() throws Exception {
            when(patientResource.getPatientResource(any())).thenThrow(new FHIRException("patient not found"));

            assertThrows(FHIRException.class,
                    () -> service.populateDiagnosticReportResourceBundle(visitRequest(), eligibleVisit()));
        }

        @Test
        @DisplayName("processDiagnosticReportRecordBundle should file the bundle in Mongo as a diagnostic report")
        void process_shouldFileBundleInMongo() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(any())).thenReturn(List.of("11-1111"));
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processDiagnosticReportRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            assertEquals("DiagnosticReport", captor.getValue().getResourceType());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("WellnessRecordResourceBundleImpl")
    class WellnessTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PractitionerResource practitionerResource;

        @Mock
        private PatientResource patientResource;

        @Mock
        private OrganizationResource organizationResource;

        @Mock
        private ObservationResource observationResource;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private WellnessRecordResourceBundleImpl service;

        @BeforeEach
        void stubResourceBuilders() throws Exception {
            ReflectionTestUtils.setField(service, "systemUrl", SYSTEM_URL);
            when(commonService.getUUID()).thenReturn(UUID);
            when(practitionerResource.getPractitionerResource(any())).thenReturn(practitioner());
            when(organizationResource.getOrganizationResource(any())).thenReturn(organization());
            when(patientResource.getPatientResource(any())).thenReturn(patient());
            when(observationResource.getObservationVitals(any(), any()))
                    .thenReturn(List.of(observation("Observation/1"), observation("Observation/2")));
        }

        @Test
        @DisplayName("populateWellnessRecordResourceBundle should carry every recorded vital")
        void populate_shouldCarryEveryVital() throws Exception {
            Bundle bundle = parse(service.populateWellnessRecordResourceBundle(visitRequest(), eligibleVisit()));

            assertNrcesDocumentBundle(bundle);
            assertTrue(bundle.getEntry().size() >= 6);
            assertTrue(compositionOf(bundle).getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/WellnessRecord"));
        }

        @Test
        @DisplayName("populateWellnessRecordResourceBundle should wrap a failing resource builder")
        void populate_shouldWrapBuilderFailure() throws Exception {
            when(patientResource.getPatientResource(any())).thenThrow(new FHIRException("patient not found"));

            assertThrows(Exception.class,
                    () -> service.populateWellnessRecordResourceBundle(visitRequest(), eligibleVisit()),
                    "the wellness bundle rethrows through the HAPI FHIRException type");
        }

        @Test
        @DisplayName("processWellnessRecordBundle should file the bundle in Mongo as a wellness record")
        void process_shouldFileBundleInMongo() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(any())).thenReturn(List.of("11-1111"));
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processWellnessRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            assertEquals("WellnessRecord", captor.getValue().getResourceType());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("ImmunizationRecordResourceBundleImpl")
    class ImmunizationTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PractitionerResource practitionerResource;

        @Mock
        private OrganizationResource organizationResource;

        @Mock
        private PatientResource patientResource;

        @Mock
        private ImmunizationResource immunizationResource;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private ImmunizationRecordResourceBundleImpl service;

        @BeforeEach
        void stubResourceBuilders() throws Exception {
            ReflectionTestUtils.setField(service, "systemUrl", SYSTEM_URL);
            when(commonService.getUUID()).thenReturn(UUID);
            when(practitionerResource.getPractitionerResource(any())).thenReturn(practitioner());
            when(organizationResource.getOrganizationResource(any())).thenReturn(organization());
            when(patientResource.getPatientResource(any())).thenReturn(patient());
            Immunization immunization = new Immunization();
            immunization.setId("Immunization/im-1");
            when(immunizationResource.getImmunizations(any(), any())).thenReturn(List.of(immunization));
        }

        @Test
        @DisplayName("populateImmunizationResourceBundle should carry every administered vaccine")
        void populate_shouldCarryEveryVaccine() throws Exception {
            Bundle bundle = parse(service.populateImmunizationResourceBundle(visitRequest(), eligibleVisit()));

            assertNrcesDocumentBundle(bundle);
            assertTrue(bundle.getEntry().size() >= 5);
            Composition composition = compositionOf(bundle);
            assertEquals("Immunization Record", composition.getTitle());
            assertTrue(composition.getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/ImmunizationRecord"));
            assertEquals("Administered Immunizations", composition.getSectionFirstRep().getTitle());
        }

        @Test
        @DisplayName("populateImmunizationResourceBundle should wrap a failing resource builder")
        void populate_shouldWrapBuilderFailure() throws Exception {
            when(patientResource.getPatientResource(any())).thenThrow(new FHIRException("patient not found"));

            assertThrows(FHIRException.class,
                    () -> service.populateImmunizationResourceBundle(visitRequest(), eligibleVisit()));
        }

        @Test
        @DisplayName("processImmunizationRecordBundle should file the bundle in Mongo as an immunization record")
        void process_shouldFileBundleInMongo() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(any())).thenReturn(List.of("11-1111"));
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processImmunizationRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            assertEquals("ImmunizationRecord", captor.getValue().getResourceType());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("DischargeSummaryResourceBundleImpl")
    class DischargeSummaryTests {

        @Mock
        private CommonService commonService;

        @Mock
        private PractitionerResource practitionerResource;

        @Mock
        private OrganizationResource organizationResource;

        @Mock
        private PatientResource patientResource;

        @Mock
        private ConditionResource conditionResource;

        @Mock
        private EncounterResource encounterResource;

        @Mock
        private AllergyIntoleranceResource allergyIntoleranceResource;

        @Mock
        private FamilyMemberHistoryResource familyMemberHistoryResource;

        @Mock
        private MedicalHistoryResource medicalHistoryResource;

        @Mock
        private ObservationResource observationResource;

        @Mock
        private DiagnosticReportResource diagnosticReportResource;

        @Mock
        private MedicationRequestResource medicationRequestResource;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private DischargeSummaryResourceBundleImpl service;

        @BeforeEach
        void stubResourceBuilders() throws Exception {
            ReflectionTestUtils.setField(service, "systemUrl", SYSTEM_URL);
            when(commonService.getUUID()).thenReturn(UUID);
            when(practitionerResource.getPractitionerResource(any())).thenReturn(practitioner());
            when(organizationResource.getOrganizationResource(any())).thenReturn(organization());
            when(patientResource.getPatientResource(any())).thenReturn(patient());
            when(conditionResource.getCondition(any(), any(), eq("chiefcomplaints")))
                    .thenReturn(List.of(condition("Condition/cc-1")));
            when(conditionResource.getCondition(any(), any(), eq("diagnosis")))
                    .thenReturn(List.of(condition("Condition/dx-1")));
            when(encounterResource.getEncounterResource(any(), any(), any(), any())).thenReturn(encounter());
            AllergyIntolerance allergy = new AllergyIntolerance();
            allergy.setId("AllergyIntolerance/food");
            when(allergyIntoleranceResource.getAllergyIntolerance(any(), any(), any(), any()))
                    .thenReturn(List.of(allergy));
            FamilyMemberHistory history = new FamilyMemberHistory();
            history.setId("FamilyMemberHistory/fmh-1");
            when(familyMemberHistoryResource.getFamilyMemberHistory(any(), any())).thenReturn(history);
            MedicationStatement statement = new MedicationStatement();
            statement.setId("MedicationStatement/ms-1");
            when(medicalHistoryResource.getMedicalHistory(any(), any())).thenReturn(List.of(statement));
            MedicationRequest request = new MedicationRequest();
            request.setId("MedicationRequest/mr-1");
            when(medicationRequestResource.getMedicationRequest(any(), any(), any(), any()))
                    .thenReturn(List.of(request));
            when(observationResource.getObservationLab(any(), any()))
                    .thenReturn(Map.of(11, List.of(observation("Observation/1"))));
            DiagnosticReport report = new DiagnosticReport();
            report.setId("DiagnosticReport/dr-1");
            when(diagnosticReportResource.getDiagnosticReport(any(), any(), any(), any()))
                    .thenReturn(List.of(report));
        }

        @Test
        @DisplayName("populateDischargeSummaryResourceBundle should gather the whole visit into one bundle")
        void populate_shouldGatherWholeVisit() throws Exception {
            Bundle bundle = parse(service.populateDischargeSummaryResourceBundle(visitRequest(), eligibleVisit()));

            assertNrcesDocumentBundle(bundle);
            assertTrue(bundle.getEntry().size() >= 10, "entries: " + bundle.getEntry().size());
            Composition composition = compositionOf(bundle);
            assertTrue(composition.getMeta().getProfile().get(0).getValue()
                    .endsWith("StructureDefinition/DischargeSummaryRecord"));
            assertTrue(composition.getSection().size() >= 5);
        }

        @Test
        @DisplayName("populateDischargeSummaryResourceBundle should wrap a failing resource builder")
        void populate_shouldWrapBuilderFailure() throws Exception {
            when(patientResource.getPatientResource(any())).thenThrow(new FHIRException("patient not found"));

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.populateDischargeSummaryResourceBundle(visitRequest(), eligibleVisit()))
                    .getMessage().startsWith("Discharge summary FHIR Resource Bundle failed with error - "));
        }

        @Test
        @DisplayName("processDischargeSummaryRecordBundle should file the bundle in Mongo as a discharge summary")
        void process_shouldFileBundleInMongo() throws Exception {
            when(benHealthIDMappingRepo.getLinkedHealthIDForVisit(any())).thenReturn(List.of("11-1111"));
            when(commonService.saveResourceToMongo(any())).thenReturn(1);

            assertEquals(1, service.processDischargeSummaryRecordBundle(visitRequest(), eligibleVisit()));

            ArgumentCaptor<AMRIT_ResourceMongo> captor = ArgumentCaptor.forClass(AMRIT_ResourceMongo.class);
            verify(commonService).saveResourceToMongo(captor.capture());
            assertEquals("DischargeSummary", captor.getValue().getResourceType());
        }
    }
}
