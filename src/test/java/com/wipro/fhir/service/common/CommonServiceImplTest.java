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
package com.wipro.fhir.service.common;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.mongo.amrit_resource.TempCollection;
import com.wipro.fhir.data.mongo.care_context.CareContexts;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.NDHMResponse;
import com.wipro.fhir.data.mongo.care_context.PatientCareContexts;
import com.wipro.fhir.data.patient.PatientDemographic;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.mongo.amrit_resource.AMRIT_ResourceMongoRepo;
import com.wipro.fhir.repo.mongo.amrit_resource.PatientCareContextsMongoRepo;
import com.wipro.fhir.repo.mongo.amrit_resource.TempCollectionRepo;
import com.wipro.fhir.repo.mongo.ndhm_response.NDHMResponseRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.repo.v3.careContext.CareContextRepo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.bundle_creation.DiagnosticRecordResourceBundle;
import com.wipro.fhir.service.bundle_creation.DischargeSummaryResourceBundle;
import com.wipro.fhir.service.bundle_creation.ImmunizationRecordResourceBundle;
import com.wipro.fhir.service.bundle_creation.OPConsultResourceBundle;
import com.wipro.fhir.service.bundle_creation.PrescriptionResourceBundleImpl;
import com.wipro.fhir.service.bundle_creation.WellnessRecordResourceBundle;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService;
import com.wipro.fhir.service.v3.abha.GenerateAuthSessionService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CommonServiceImpl Test Suite")
class CommonServiceImplTest {

    private static final Timestamp CREATED = new Timestamp(1_700_000_000_000L);

    @Mock
    private HttpUtils httpUtils;

    @Mock
    private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;

    @Mock
    private APIChannel aPIChannel;

    @Mock
    private AMRIT_ResourceMongoRepo aMRIT_ResourceMongoRepo;

    @Mock
    private PatientCareContextsMongoRepo patientCareContextsMongoRepo;

    @Mock
    private TempCollectionRepo tempCollectionRepo;

    @Mock
    private NDHMResponseRepo nDHMResponseRepo;

    @Mock
    private PatientDemographicModel_NDHM_Patient_Profile_Repo profileRepo;

    @Mock
    private PatientDataGatewayService patientDataGatewayService;

    @Mock
    private GenerateAuthSessionService generateAuthSessionService;

    @Mock
    private PatientDemographic patientDemographic;

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private BenHealthIDMappingRepo benHealthIDMappingRepo;

    @Mock
    private PrescriptionResourceBundleImpl prescriptionResourceBundle;

    @Mock
    private OPConsultResourceBundle oPConsultResourceBundle;

    @Mock
    private DiagnosticRecordResourceBundle diagnosticReportResourceBundle;

    @Mock
    private WellnessRecordResourceBundle wellnessRecordResourceBundle;

    @Mock
    private ImmunizationRecordResourceBundle immunizationRecordResourceBundle;

    @Mock
    private DischargeSummaryResourceBundle dischargeSummaryResourceBundle;

    @Mock
    private CareContextRepo careContextRepo;

    @InjectMocks
    private CommonServiceImpl service;

    @BeforeEach
    @DisplayName("Configure the properties the service reads before each test")
    void setUp() {
        ReflectionTestUtils.setField(service, "patient_search_page_size", "10");
        ReflectionTestUtils.setField(service, "abhaMode", "sbx");
        ReflectionTestUtils.setField(service, "clientID", "client-1");
        ReflectionTestUtils.setField(service, "clientSecret", "secret-1");
        ReflectionTestUtils.setField(service, "ndhmUserAuthenticate", "http://abdm.example.org/sessions");
        ReflectionTestUtils.setField(service, "generateABDM_NotifySMS", "http://abdm.example.org/sms");
    }

    private PatientEligibleForResourceCreation eligibleVisit(String visitCategory) {
        PatientEligibleForResourceCreation visit = new PatientEligibleForResourceCreation();
        visit.setId(BigInteger.ONE);
        visit.setBeneficiaryId(BigInteger.valueOf(9999L));
        visit.setBeneficiaryRegID(BigInteger.valueOf(4321L));
        visit.setVisitCode(BigInteger.valueOf(987654L));
        visit.setVisitCategory(visitCategory);
        return visit;
    }

    private PatientDemographic demographic() {
        PatientDemographic demographic = new PatientDemographic();
        demographic.setBeneficiaryID(BigInteger.valueOf(9999L));
        demographic.setBeneficiaryRegID(BigInteger.valueOf(4321L));
        demographic.setName("Asha Devi");
        demographic.setGenderID(2);
        demographic.setDOB(CREATED);
        demographic.setPreferredPhoneNo("9999999999");
        demographic.setHealthID("abc@sbx");
        demographic.setHealthIdNo("11-1111-1111-1111");
        return demographic;
    }

    @Nested
    @DisplayName("processResourceOperation")
    class ProcessResourceOperationTests {

        private void stubEligibleVisit(String visitCategory) throws Exception {
            when(patientEligibleForResourceCreationRepo.getPatientListForResourceCreation(any(Timestamp.class)))
                    .thenReturn(new ArrayList<>(List.of(eligibleVisit(visitCategory))));
            when(patientEligibleForResourceCreationRepo.callPatientDemographicSP(any()))
                    .thenReturn(List.<Object[]>of(new Object[] { "row" }));
            when(patientDemographic.getPatientDemographic(any())).thenReturn(demographic());
            when(benHealthIDMappingRepo.getAbdmFacilityAndlinkedDate(any()))
                    .thenReturn(List.<Object[]>of(new Object[] { "IN0710000001", "2026-08-24" }));
            when(dischargeSummaryResourceBundle.processDischargeSummaryRecordBundle(any(), any())).thenReturn(1);
            when(patientEligibleForResourceCreationRepo.save(any())).thenAnswer(i -> i.getArgument(0));
        }

        @Test
        @DisplayName("should build the OP consult bundle for an OPD visit and mark the row processed")
        void processResourceOperation_shouldBuildOpConsultForOpd() throws Exception {
            stubEligibleVisit("General OPD");
            when(oPConsultResourceBundle.processOpConsultRecordBundle(any(), any())).thenReturn(1);

            String result = service.processResourceOperation();

            assertTrue(result.startsWith("Bundle creation is success for BenID : 9999"), result);
            verify(oPConsultResourceBundle).processOpConsultRecordBundle(any(), any());
            ArgumentCaptor<PatientEligibleForResourceCreation> captor =
                    ArgumentCaptor.forClass(PatientEligibleForResourceCreation.class);
            verify(patientEligibleForResourceCreationRepo).save(captor.capture());
            assertTrue(captor.getValue().getProcessed());
        }

        @Test
        @DisplayName("should build only the discharge summary for a visit category with no OP consult")
        void processResourceOperation_shouldBuildOnlyDischargeSummary() throws Exception {
            stubEligibleVisit("IPD");

            assertNotNull(service.processResourceOperation());

            verify(oPConsultResourceBundle, never()).processOpConsultRecordBundle(any(), any());
            verify(dischargeSummaryResourceBundle).processDischargeSummaryRecordBundle(any(), any());
        }

        @Test
        @DisplayName("should build each data-driven bundle the visit has data for")
        void processResourceOperation_shouldBuildDataDrivenBundles() throws Exception {
            stubEligibleVisit("IPD");
            when(careContextRepo.hasLabtestsDone("987654")).thenReturn(1);
            when(careContextRepo.hasPrescribedDrugs("987654")).thenReturn(1);
            when(careContextRepo.hasPhyVitals("987654")).thenReturn(1);
            when(careContextRepo.hasVaccineDetails("987654")).thenReturn(1);
            when(diagnosticReportResourceBundle.processDiagnosticReportRecordBundle(any(), any())).thenReturn(1);
            when(prescriptionResourceBundle.processPrescriptionRecordBundle(any(), any())).thenReturn(1);
            when(wellnessRecordResourceBundle.processWellnessRecordBundle(any(), any())).thenReturn(1);
            when(immunizationRecordResourceBundle.processImmunizationRecordBundle(any(), any())).thenReturn(1);

            assertNotNull(service.processResourceOperation());

            verify(diagnosticReportResourceBundle).processDiagnosticReportRecordBundle(any(), any());
            verify(prescriptionResourceBundle).processPrescriptionRecordBundle(any(), any());
            verify(wellnessRecordResourceBundle).processWellnessRecordBundle(any(), any());
            verify(immunizationRecordResourceBundle).processImmunizationRecordBundle(any(), any());
        }

        @Test
        @DisplayName("should leave the row unprocessed when a bundle reports it wrote nothing")
        void processResourceOperation_shouldLeaveRowUnprocessedOnEmptyBundle() throws Exception {
            stubEligibleVisit("General OPD");
            when(oPConsultResourceBundle.processOpConsultRecordBundle(any(), any())).thenReturn(0);

            assertNull(service.processResourceOperation());

            verify(patientEligibleForResourceCreationRepo, never()).save(any());
        }

        @Test
        @DisplayName("should leave the row unprocessed when a bundle fails outright")
        void processResourceOperation_shouldLeaveRowUnprocessedOnBundleFailure() throws Exception {
            stubEligibleVisit("General OPD");
            when(oPConsultResourceBundle.processOpConsultRecordBundle(any(), any()))
                    .thenThrow(new FHIRException("bundle failed"));

            assertNull(service.processResourceOperation());

            verify(patientEligibleForResourceCreationRepo, never()).save(any());
        }

        @Test
        @DisplayName("should carry on building the bundles when the beneficiary cannot be found")
        void processResourceOperation_shouldCarryOnWithoutDemographics() throws Exception {
            stubEligibleVisit("IPD");
            when(patientDemographic.getPatientDemographic(any())).thenReturn(null);

            assertNotNull(service.processResourceOperation());

            verify(patientCareContextsMongoRepo, never()).save(any());
        }

        @Test
        @DisplayName("should answer with nothing when no visit is eligible")
        void processResourceOperation_shouldAnswerNullWithoutEligibleVisits() throws Exception {
            when(patientEligibleForResourceCreationRepo.getPatientListForResourceCreation(any(Timestamp.class)))
                    .thenReturn(new ArrayList<>());

            assertNull(service.processResourceOperation());
        }
    }

    @Nested
    @DisplayName("addCareContextToMongo")
    class AddCareContextTests {

        @Test
        @DisplayName("should open a new care-context document for a beneficiary Mongo has not seen")
        void addCareContextToMongo_shouldOpenNewDocument() throws Exception {
            when(benHealthIDMappingRepo.getAbdmFacilityAndlinkedDate(any()))
                    .thenReturn(List.<Object[]>of(new Object[] { "IN0710000001", "2026-08-24" }));
            when(patientCareContextsMongoRepo.findByIdentifier("9999")).thenReturn(null);

            service.addCareContextToMongo(demographic(), eligibleVisit("General OPD"));

            ArgumentCaptor<PatientCareContexts> captor = ArgumentCaptor.forClass(PatientCareContexts.class);
            verify(patientCareContextsMongoRepo).save(captor.capture());
            PatientCareContexts saved = captor.getValue();
            assertEquals("9999", saved.getIdentifier());
            assertEquals("F", saved.getGender());
            assertEquals("Asha Devi", saved.getName());
            assertEquals("abc@sbx", saved.getHealthId());
            assertEquals("11-1111-1111-1111", saved.getHealthNumber());
            assertEquals("9999999999", saved.getPhoneNumber());
            assertEquals(1, saved.getCareContextsList().size());
            assertEquals("987654", saved.getCareContextsList().get(0).getReferenceNumber());
            assertEquals("IN0710000001", saved.getCareContextsList().get(0).getAbdmFacilityId());
        }

        @ParameterizedTest(name = "genderID {0} -> {1}")
        @CsvSource({ "1, M", "2, F", "3, O" })
        @DisplayName("should map the gender id onto the ABDM letter on a new document")
        void addCareContextToMongo_shouldMapGender(Integer genderID, String expected) throws Exception {
            when(benHealthIDMappingRepo.getAbdmFacilityAndlinkedDate(any()))
                    .thenReturn(List.<Object[]>of(new Object[] { null, null }));
            when(patientCareContextsMongoRepo.findByIdentifier(anyString())).thenReturn(null);
            PatientDemographic demographic = demographic();
            demographic.setGenderID(genderID);

            service.addCareContextToMongo(demographic, eligibleVisit("General OPD"));

            ArgumentCaptor<PatientCareContexts> captor = ArgumentCaptor.forClass(PatientCareContexts.class);
            verify(patientCareContextsMongoRepo).save(captor.capture());
            assertEquals(expected, captor.getValue().getGender());
        }

        @Test
        @DisplayName("should append the visit to an existing care-context document")
        void addCareContextToMongo_shouldAppendToExistingDocument() throws Exception {
            when(benHealthIDMappingRepo.getAbdmFacilityAndlinkedDate(any()))
                    .thenReturn(List.<Object[]>of(new Object[] { "IN0710000001", "2026-08-24" }));
            PatientCareContexts existing = new PatientCareContexts();
            existing.setIdentifier("9999");
            CareContexts earlier = new CareContexts();
            earlier.setReferenceNumber("111111");
            existing.setCareContextsList(new ArrayList<>(List.of(earlier)));
            when(patientCareContextsMongoRepo.findByIdentifier("9999")).thenReturn(existing);

            service.addCareContextToMongo(demographic(), eligibleVisit("General OPD"));

            verify(patientCareContextsMongoRepo).save(existing);
            assertEquals(2, existing.getCareContextsList().size());
        }

        @Test
        @DisplayName("should leave an already-recorded visit alone")
        void addCareContextToMongo_shouldLeaveKnownVisitAlone() throws Exception {
            when(benHealthIDMappingRepo.getAbdmFacilityAndlinkedDate(any()))
                    .thenReturn(List.<Object[]>of(new Object[] { "IN0710000001", "2026-08-24" }));
            PatientCareContexts existing = new PatientCareContexts();
            existing.setIdentifier("9999");
            CareContexts sameVisit = new CareContexts();
            sameVisit.setReferenceNumber("987654");
            existing.setCareContextsList(new ArrayList<>(List.of(sameVisit)));
            when(patientCareContextsMongoRepo.findByIdentifier("9999")).thenReturn(existing);

            service.addCareContextToMongo(demographic(), eligibleVisit("General OPD"));

            verify(patientCareContextsMongoRepo, never()).save(any());
        }

        @Test
        @DisplayName("should do nothing at all without both a beneficiary and a visit")
        void addCareContextToMongo_shouldDoNothingWithoutBoth() throws Exception {
            service.addCareContextToMongo(null, eligibleVisit("General OPD"));
            service.addCareContextToMongo(demographic(), null);

            verify(patientCareContextsMongoRepo, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Mongo reads and writes")
    class MongoTests

    {
        @Test
        @DisplayName("saveResourceToMongo should report success once the document has an id")
        void saveResourceToMongo_shouldReportSuccess() throws Exception {
            AMRIT_ResourceMongo saved = new AMRIT_ResourceMongo();
            saved.setId("doc-1");
            when(aMRIT_ResourceMongoRepo.save(any())).thenReturn(saved);

            assertEquals(1, service.saveResourceToMongo(new AMRIT_ResourceMongo()));
        }

        @Test
        @DisplayName("saveResourceToMongo should report failure when the write left no id behind")
        void saveResourceToMongo_shouldReportFailure() throws Exception {
            when(aMRIT_ResourceMongoRepo.save(any())).thenReturn(new AMRIT_ResourceMongo());

            assertEquals(0, service.saveResourceToMongo(new AMRIT_ResourceMongo()));
        }

        private TempCollection tempCollection() {
            TempCollection temp = new TempCollection();
            temp.setBeneficiaryRegID(BigInteger.valueOf(4321L));
            temp.setVisitCode(BigInteger.valueOf(987654L));
            return temp;
        }

        @Test
        @DisplayName("saveTempResourceToMongo should overwrite the existing draft for the same visit")
        void saveTempResourceToMongo_shouldOverwriteExistingDraft() throws Exception {
            TempCollection existing = tempCollection();
            existing.setId("draft-1");
            when(tempCollectionRepo.findByBeneficiaryRegIDAndVisitCode(any(), any()))
                    .thenReturn(List.of(existing));
            when(tempCollectionRepo.save(any())).thenAnswer(i -> i.getArgument(0));

            TempCollection incoming = tempCollection();
            assertEquals(1, service.saveTempResourceToMongo(incoming));
            assertEquals("draft-1", incoming.getId(), "the draft is replaced in place, not duplicated");
        }

        @Test
        @DisplayName("saveTempResourceToMongo should report failure when the write left no id behind")
        void saveTempResourceToMongo_shouldReportFailure() throws Exception {
            when(tempCollectionRepo.findByBeneficiaryRegIDAndVisitCode(any(), any())).thenReturn(List.of());
            when(tempCollectionRepo.save(any())).thenReturn(new TempCollection());

            assertEquals(0, service.saveTempResourceToMongo(tempCollection()));
        }

        @Test
        @DisplayName("saveTempResourceToMongo should refuse an absent draft")
        void saveTempResourceToMongo_shouldRefuseNull() {
            assertEquals("Null data",
                    assertThrows(FHIRException.class, () -> service.saveTempResourceToMongo(null)).getMessage());
        }

        @Test
        @DisplayName("fetchTempResourceFromMongo should read the drafts for the requested visit")
        void fetchTempResourceFromMongo_shouldReadDrafts() throws Exception {
            when(tempCollectionRepo.findByBeneficiaryRegIDAndVisitCode(
                    BigInteger.valueOf(4321L), BigInteger.valueOf(987654L)))
                    .thenReturn(List.of(tempCollection()));
            ResourceRequestHandler request = new ResourceRequestHandler();
            request.setBeneficiaryRegID(BigInteger.valueOf(4321L));
            request.setVisitCode(BigInteger.valueOf(987654L));

            assertEquals(1, service.fetchTempResourceFromMongo(request).size());
        }

        @Test
        @DisplayName("savePatientProfileDataToMongo should hand the whole batch to the repository")
        void savePatientProfileDataToMongo_shouldSaveBatch() throws Exception {
            List<PatientDemographicModel_NDHM_Patient_Profile> batch =
                    List.of(new PatientDemographicModel_NDHM_Patient_Profile());
            when(profileRepo.saveAll(batch)).thenReturn(batch);

            assertEquals(batch, service.savePatientProfileDataToMongo(batch));
        }

        @Test
        @DisplayName("getMongoNDHMResponse should answer with the stored callback payload")
        void getMongoNDHMResponse_shouldAnswerWithStoredPayload() throws Exception {
            NDHMResponse stored = new NDHMResponse();
            stored.setResponseData("{\"linked\":true}");
            when(nDHMResponseRepo.findByRequestID("req-1")).thenReturn(stored);

            assertEquals("{\"linked\":true}", service.getMongoNDHMResponse("req-1"));
        }

        @Test
        @DisplayName("getResponseMongo should answer with nothing when no callback was stored")
        void getResponseMongo_shouldAnswerNullWithoutPayload() {
            when(nDHMResponseRepo.findByRequestID("req-1")).thenReturn(null);

            assertNull(service.getResponseMongo("req-1"));
        }
    }

    @Nested
    @DisplayName("searchPatientProfileFromMongo")
    class SearchProfileTests {

        private ResourceRequestHandler pagedRequest() {
            ResourceRequestHandler request = new ResourceRequestHandler();
            request.setPageNo(0);
            return request;
        }

        private PageImpl<PatientDemographicModel_NDHM_Patient_Profile> page() {
            return new PageImpl<>(List.of(new PatientDemographicModel_NDHM_Patient_Profile()),
                    PageRequest.of(0, 10), 1);
        }

        @Test
        @DisplayName("should search by ABHA address first when the request carries one")
        void search_shouldPreferHealthId() throws Exception {
            ResourceRequestHandler request = pagedRequest();
            request.setHealthId("abc@sbx");
            request.setAmritId("AM-1");
            when(profileRepo.findByHealthId(eq("abc@sbx"), any(PageRequest.class))).thenReturn(page());

            assertEquals(1, service.searchPatientProfileFromMongo(request).getContent().size());
            verify(profileRepo, never()).findByAmritId(anyString(), any(PageRequest.class));
        }

        @Test
        @DisplayName("should search by ABHA number when there is no ABHA address")
        void search_shouldSearchByHealthIdNumber() throws Exception {
            ResourceRequestHandler request = pagedRequest();
            request.setHealthIdNumber("11-1111-1111-1111");
            when(profileRepo.findByHealthIdNumber(eq("11-1111-1111-1111"), any(PageRequest.class)))
                    .thenReturn(page());

            assertNotNull(service.searchPatientProfileFromMongo(request));
        }

        @Test
        @DisplayName("should search by AMRIT id, and by the beneficiary id when that is all there is")
        void search_shouldSearchByAmritAndBeneficiaryId() throws Exception {
            ResourceRequestHandler byAmrit = pagedRequest();
            byAmrit.setAmritId("AM-1");
            when(profileRepo.findByAmritId(eq("AM-1"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byAmrit));

            ResourceRequestHandler byBeneficiary = pagedRequest();
            byBeneficiary.setBeneficiaryID(BigInteger.valueOf(9999L));
            when(profileRepo.findByAmritId(eq("9999"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byBeneficiary));
        }

        @Test
        @DisplayName("should search by external id, phone number and each address level in turn")
        void search_shouldSearchByRemainingCriteria() throws Exception {
            ResourceRequestHandler byExternal = pagedRequest();
            byExternal.setExternalId("EXT-1");
            when(profileRepo.findByExternalId(eq("EXT-1"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byExternal));

            ResourceRequestHandler byPhone = pagedRequest();
            byPhone.setPhoneNo("9999999999");
            when(profileRepo.searchPatientByPhoneNo(eq("9999999999"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byPhone));

            ResourceRequestHandler byVillage = pagedRequest();
            byVillage.setVillage("Kanke");
            when(profileRepo.searchPatientByAddressVillage(eq("Kanke"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byVillage));

            ResourceRequestHandler byDistrict = pagedRequest();
            byDistrict.setDistrict("Ranchi");
            when(profileRepo.searchPatientByAddressDistrict(eq("Ranchi"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byDistrict));

            ResourceRequestHandler byState = pagedRequest();
            byState.setState("Jharkhand");
            when(profileRepo.searchPatientByAddressState(eq("Jharkhand"), any(PageRequest.class))).thenReturn(page());
            assertNotNull(service.searchPatientProfileFromMongo(byState));
        }

        @Test
        @DisplayName("should answer with nothing when the request names no search criterion")
        void search_shouldAnswerNullWithoutCriteria() throws Exception {
            assertNull(service.searchPatientProfileFromMongo(pagedRequest()));
        }

        @Test
        @DisplayName("should fall back to a page size of ten when none is configured")
        void search_shouldFallBackToDefaultPageSize() throws Exception {
            ReflectionTestUtils.setField(service, "patient_search_page_size", null);
            ResourceRequestHandler request = pagedRequest();
            request.setHealthId("abc@sbx");
            when(profileRepo.findByHealthId(anyString(), eq(PageRequest.of(0, 10)))).thenReturn(page());

            assertNotNull(service.searchPatientProfileFromMongo(request));
        }

        @Test
        @DisplayName("should insist on a page number")
        void search_shouldInsistOnPageNumber() {
            assertEquals("page no is required", assertThrows(FHIRException.class,
                    () -> service.searchPatientProfileFromMongo(new ResourceRequestHandler())).getMessage());
        }
    }

    @Nested
    @DisplayName("Session, SMS and helpers")
    class HelperTests {

        @Test
        @DisplayName("processPatientProfileCreationAMRIT should run the profile pass with a fresh session key")
        void processPatientProfileCreationAMRIT_shouldRunWithFreshKey() throws Exception {
            when(aPIChannel.userAuthentication()).thenReturn("session-key-123");
            when(patientDataGatewayService.generatePatientProfileAMRIT_SaveTo_Mongo("session-key-123"))
                    .thenReturn("[{\"amritId\":\"AM-1\"}]");

            assertEquals("[{\"amritId\":\"AM-1\"}]", service.processPatientProfileCreationAMRIT());
        }

        @Test
        @DisplayName("processPatientProfileCreationAMRIT should tolerate a pass that created nothing")
        void processPatientProfileCreationAMRIT_shouldTolerateEmptyPass() throws Exception {
            when(aPIChannel.userAuthentication()).thenReturn("session-key-123");
            when(patientDataGatewayService.generatePatientProfileAMRIT_SaveTo_Mongo(anyString())).thenReturn(null);

            assertNull(service.processPatientProfileCreationAMRIT());
        }

        @Test
        @DisplayName("getPatientListForResourceEligible should look back two days for eligible visits")
        void getPatientListForResourceEligible_shouldLookBackTwoDays() throws Exception {
            when(patientEligibleForResourceCreationRepo.getPatientListForResourceCreation(any(Timestamp.class)))
                    .thenReturn(List.of(eligibleVisit("General OPD")));

            assertEquals(1, service.getPatientListForResourceEligible().size());

            ArgumentCaptor<Timestamp> captor = ArgumentCaptor.forClass(Timestamp.class);
            verify(patientEligibleForResourceCreationRepo).getPatientListForResourceCreation(captor.capture());
            assertTrue(captor.getValue().before(new Timestamp(System.currentTimeMillis())));
        }

        @Test
        @DisplayName("getAuthKey should mint a session key through the AMRIT auth channel")
        void getAuthKey_shouldMintSessionKey() throws Exception {
            when(aPIChannel.userAuthentication()).thenReturn("session-key-123");

            assertEquals("session-key-123", service.getAuthKey());
        }

        @Test
        @DisplayName("getUUID should answer with a fresh identifier each time")
        void getUUID_shouldAnswerFreshIdentifier() {
            assertNotEquals(service.getUUID(), service.getUUID());
        }

        @Test
        @DisplayName("getRequestIDAndTimeStamp should stamp a UTC timestamp and a fresh request id")
        void getRequestIDAndTimeStamp_shouldStampRequest() {
            NDHMRequest request = service.getRequestIDAndTimeStamp();

            assertNotNull(request.getRequestId());
            assertTrue(request.getTimestamp().endsWith("Z"), request.getTimestamp());
        }

        @Test
        @DisplayName("ndhmUserAuthenticate should report success when ABDM answers with a session")
        void ndhmUserAuthenticate_shouldReportSuccess() throws Exception {
            when(httpUtils.post(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn("{\"accessToken\":\"abdm-access\",\"expiresIn\":1800}");

            assertEquals("success", service.ndhmUserAuthenticate());
        }

        @Test
        @DisplayName("ndhmUserAuthenticate should report the failure when ABDM answers with nothing")
        void ndhmUserAuthenticate_shouldReportEmptyAnswer() throws Exception {
            when(httpUtils.post(anyString(), anyString(), any(HttpHeaders.class))).thenReturn(null);

            assertEquals("Error while accessing authenticate API", service.ndhmUserAuthenticate());
        }

        @Test
        @DisplayName("ndhmUserAuthenticate should wrap an ABDM transport failure")
        void ndhmUserAuthenticate_shouldWrapTransportFailure() {
            when(httpUtils.post(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertTrue(assertThrows(FHIRException.class, () -> service.ndhmUserAuthenticate()).getMessage()
                    .startsWith("Error while accessing authenticate API"));
        }

        @Test
        @DisplayName("sendAbdmAdvSMS should notify ABDM and accept its 202")
        void sendAbdmAdvSMS_shouldNotifyAbdm() throws Exception {
            when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
            when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(service.getRequestIDAndTimeStamp());
            when(common_NDHMService.getHeaders(anyString(), anyString())).thenReturn(new HttpHeaders());
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>("", HttpStatus.ACCEPTED));
            when(common_NDHMService.getStatusCode(any())).thenReturn("202");

            service.sendAbdmAdvSMS("9999999999");

            verify(httpUtils).postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class));
        }

        @Test
        @DisplayName("sendAbdmAdvSMS should fall back to the sandbox mode for an unrecognised configuration")
        void sendAbdmAdvSMS_shouldFallBackToSandboxMode() throws Exception {
            ReflectionTestUtils.setField(service, "abhaMode", "staging");
            when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
            when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(service.getRequestIDAndTimeStamp());
            when(common_NDHMService.getHeaders(anyString(), anyString())).thenReturn(new HttpHeaders());
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>("", HttpStatus.ACCEPTED));
            when(common_NDHMService.getStatusCode(any())).thenReturn("202");

            service.sendAbdmAdvSMS("9999999999");

            verify(common_NDHMService).getHeaders("Bearer abdm-token", "sbx");
        }

        @Test
        @DisplayName("sendAbdmAdvSMS should log a rejected notification rather than fail the caller")
        void sendAbdmAdvSMS_shouldLogRejectedNotification() throws Exception {
            when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
            when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(service.getRequestIDAndTimeStamp());
            when(common_NDHMService.getHeaders(anyString(), anyString())).thenReturn(new HttpHeaders());
            when(httpUtils.postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));
            when(common_NDHMService.getStatusCode(any())).thenReturn("400");

            service.sendAbdmAdvSMS("9999999999");
        }

        @Test
        @DisplayName("sendAbdmAdvSMS should swallow a failure to reach ABDM")
        void sendAbdmAdvSMS_shouldSwallowTransportFailure() throws Exception {
            when(generateAuthSessionService.getAbhaAuthToken()).thenThrow(new FHIRException("no ABDM session"));

            service.sendAbdmAdvSMS("9999999999");

            verify(httpUtils, never()).postWithResponseEntity(anyString(), anyString(), any(HttpHeaders.class));
        }
    }
}
