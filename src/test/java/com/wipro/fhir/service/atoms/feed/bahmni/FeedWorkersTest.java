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
package com.wipro.fhir.service.atoms.feed.bahmni;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

import com.wipro.fhir.LocalHttpStub;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.ClinicalFeedDataLog;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.EncounterFullRepresentation;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.FeedDataLog;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPatientFullRepresentation;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPatientIdentifier;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPatientIdentifierType;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPerson;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPersonAddress;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPersonName;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.ClinicalFeedDataLogRepo;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.EncounterFullRepresentationRepo;
import com.wipro.fhir.repo.atoms.feed.bahmni.patient.FeedDataLogRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.http.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The Bahmni ATOM feed workers. The feed itself is served from a loopback stub so the
 * paging, entry-resume and per-entry error handling all run for real; the OpenMRS
 * full-representation fetch behind each entry goes through the mocked {@link HttpUtils}.
 */
@DisplayName("Bahmni ATOM feed workers Test Suite")
class FeedWorkersTest {

    private static final String FEED_PATH = "/openmrs/ws/atomfeed/patient/";
    private static final String ENCOUNTER_FEED_PATH = "/openmrs/ws/atomfeed/encounter/";

    /**
     * One ATOM page carrying a single entry whose content is the CDATA-wrapped URL of the
     * full representation, plus the self link the worker records and the next-archive link
     * it pages on.
     */
    private static String atomPage(String entryUri, String contentUrl, String selfHref, String nextArchiveHref) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
                + "<id>urn:uuid:feed-1</id>"
                + "<title>Patient AOP</title>"
                + "<author><name>OpenMRS</name></author>"
                + "<updated>2026-08-24T10:00:00Z</updated>"
                + "<link rel=\"self\" href=\"" + selfHref + "\"/>"
                + "<link rel=\"via\" href=\"" + selfHref + "\"/>"
                + (nextArchiveHref == null ? ""
                        : "<link rel=\"next-archive\" href=\"" + nextArchiveHref + "\"/>")
                + "<link rel=\"prev-archive\" href=\"http://example.org/feed/0\"/>"
                + "<entry>"
                + "<id>" + entryUri + "</id>"
                + "<title>Patient</title>"
                + "<updated>2026-08-24T10:00:00Z</updated>"
                + "<content type=\"application/vnd.atomfeed+xml\">"
                + "<![CDATA[" + contentUrl + "]]>"
                + "</content>"
                + "</entry>"
                + "</feed>";
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("PatientFeedWorker")
    class PatientFeedTests {

        @Mock
        private HttpUtils httpUtils;

        @Mock
        private FeedDataLogRepo feedDataLogRepo;

        @Mock
        private PatientDemographicModel_NDHM_Patient_Profile_Repo profileRepo;

        @Mock
        private CommonService commonService;

        @InjectMocks
        private PatientFeedWorker worker;

        private LocalHttpStub stub;

        @BeforeEach
        void setUp() throws Exception {
            stub = new LocalHttpStub();
            ReflectionTestUtils.setField(worker, "atomsFeedStartPage", 1);
            ReflectionTestUtils.setField(worker, "parentUrl", stub.url(""));
            ReflectionTestUtils.setField(worker, "atomFeedURLPatientDemographic", FEED_PATH);
            ReflectionTestUtils.setField(worker, "userName", "feed-user");
            ReflectionTestUtils.setField(worker, "password", "feed-secret");
            when(commonService.getUUID()).thenReturn("uuid-1");
        }

        @AfterEach
        void tearDown() {
            stub.close();
        }

        private String patientJson() {
            return "{\"uuid\":\"patient-uuid\",\"identifiers\":[{\"identifier\":\"EXT-1\","
                    + "\"identifierType\":{\"display\":\"Patient Identifier\"}}],"
                    + "\"person\":{\"gender\":\"F\",\"birthdate\":\"1990-08-15T00:00:00.000\","
                    + "\"preferredName\":{\"givenName\":\"Asha\",\"middleName\":\"Kumari\","
                    + "\"familyName\":\"Devi\",\"display\":\"Asha Kumari Devi\"},"
                    + "\"preferredAddress\":{\"stateProvince\":\"Jharkhand\",\"postalCode\":\"834006\","
                    + "\"countyDistrict\":\"Ranchi\",\"cityVillage\":\"Kanke\"}}}";
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should store the patient behind each feed entry")
        void readFeeds_shouldStoreEachPatient() throws Exception {
            stub.stubOk(FEED_PATH + "1", atomPage("urn:uuid:entry-1", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "1"), null));
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class))).thenReturn(patientJson());
            when(profileRepo.findByExternalId("EXT-1")).thenReturn(List.of());
            PatientDemographicModel_NDHM_Patient_Profile saved =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            saved.setId("doc-1");
            when(profileRepo.save(any())).thenReturn(saved);

            List<OpenMRSPatientFullRepresentation> patients =
                    worker.readPatientDemographicFeeds(stub.url(""), FEED_PATH, 1, null);

            assertEquals(1, patients.size());
            assertEquals("patient-uuid", patients.get(0).getUuid());
            verify(profileRepo).save(any());
            verify(feedDataLogRepo).save(any(FeedDataLog.class));
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should update the document a patient already has")
        void readFeeds_shouldUpdateExistingDocument() throws Exception {
            stub.stubOk(FEED_PATH + "1", atomPage("urn:uuid:entry-1", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "1"), null));
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class))).thenReturn(patientJson());
            PatientDemographicModel_NDHM_Patient_Profile existing =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            existing.setId("doc-1");
            existing.setAmritId("AM-1");
            when(profileRepo.findByExternalId("EXT-1")).thenReturn(List.of(existing));
            when(profileRepo.save(any())).thenAnswer(i -> i.getArgument(0));

            worker.readPatientDemographicFeeds(stub.url(""), FEED_PATH, 1, null);

            org.mockito.ArgumentCaptor<PatientDemographicModel_NDHM_Patient_Profile> captor =
                    org.mockito.ArgumentCaptor.forClass(PatientDemographicModel_NDHM_Patient_Profile.class);
            verify(profileRepo).save(captor.capture());
            assertEquals("doc-1", captor.getValue().getId());
            assertEquals("AM-1", captor.getValue().getAmritId());
            assertNotNull(captor.getValue().getLastModDate());
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should page on to the next archive the feed points at")
        void readFeeds_shouldPageToNextArchive() throws Exception {
            stub.stubOk(FEED_PATH + "1", atomPage("urn:uuid:entry-1", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "1"), stub.url(FEED_PATH + "2")));
            stub.stubOk(FEED_PATH + "2", atomPage("urn:uuid:entry-2", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "2"), null));
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class))).thenReturn(patientJson());
            when(profileRepo.findByExternalId(anyString())).thenReturn(List.of());
            when(profileRepo.save(any())).thenAnswer(i -> i.getArgument(0));

            assertEquals(2, worker.readPatientDemographicFeeds(stub.url(""), FEED_PATH, 1, null).size(),
                    "both feed pages are walked");
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should resume from the entry the log last completed")
        void readFeeds_shouldResumeFromLoggedEntry() throws Exception {
            stub.stubOk(FEED_PATH + "1", atomPage("urn:uuid:entry-1", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "1"), null));
            FeedDataLog log = new FeedDataLog();
            log.setEntryID("urn:uuid:entry-1");

            assertTrue(worker.readPatientDemographicFeeds(stub.url(""), FEED_PATH, 1, log).isEmpty(),
                    "the already-processed entry is skipped, leaving nothing on this page");
            verify(profileRepo, never()).save(any());
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should log the entry and carry on when the fetch fails")
        void readFeeds_shouldLogFailedEntry() throws Exception {
            stub.stubOk(FEED_PATH + "1", atomPage("urn:uuid:entry-1", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "1"), null));
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class)))
                    .thenThrow(org.springframework.web.client.HttpClientErrorException.create(
                            org.springframework.http.HttpStatus.NOT_FOUND, "Not Found", new HttpHeaders(),
                            new byte[0], StandardCharsets.UTF_8));

            assertTrue(worker.readPatientDemographicFeeds(stub.url(""), FEED_PATH, 1, null).isEmpty());
            verify(feedDataLogRepo).save(any(FeedDataLog.class));
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should stop when the feed cannot be reached")
        void readFeeds_shouldStopOnUnreachableFeed() {
            assertTrue(worker.readPatientDemographicFeeds("http://127.0.0.1:1", FEED_PATH, 1, null).isEmpty());
        }

        @Test
        @DisplayName("readPatientDemographicFeeds should do nothing for a non-positive page pointer")
        void readFeeds_shouldDoNothingForNonPositivePointer() {
            assertTrue(worker.readPatientDemographicFeeds(stub.url(""), FEED_PATH, 0, null).isEmpty());
        }

        @Test
        @DisplayName("patientFeedManager should resume from the page the last successful log entry names")
        void patientFeedManager_shouldResumeFromLoggedPage() throws Exception {
            FeedDataLog log = new FeedDataLog();
            log.setId("log-1");
            log.setLinkSelf(stub.url(FEED_PATH + "5"));
            log.setEntryID("urn:uuid:entry-0");
            when(feedDataLogRepo.findOneByEntrySuccessOrderByIdDesc(true)).thenReturn(log);
            stub.stubOk(FEED_PATH + "5", atomPage("urn:uuid:entry-5", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "5"), null));

            assertEquals("0patient entry processed successfully", worker.patientFeedManager(),
                    "the page is walked but its only entry precedes the resume point");
        }

        @Test
        @DisplayName("patientFeedManager should start at the configured page when nothing was ever logged")
        void patientFeedManager_shouldStartAtConfiguredPage() throws Exception {
            when(feedDataLogRepo.findOneByEntrySuccessOrderByIdDesc(true)).thenReturn(null);
            stub.stubOk(FEED_PATH + "1", atomPage("urn:uuid:entry-1", "/openmrs/ws/rest/patient/patient-uuid",
                    stub.url(FEED_PATH + "1"), null));
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class))).thenReturn(patientJson());
            when(profileRepo.findByExternalId(anyString())).thenReturn(List.of());
            PatientDemographicModel_NDHM_Patient_Profile saved =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            saved.setId("doc-1");
            when(profileRepo.save(any())).thenReturn(saved);

            assertEquals("1patient entry processed successfully", worker.patientFeedManager());
        }

        @Test
        @DisplayName("getPatientURL_CData should unwrap the URL out of the entry's CDATA section")
        void getPatientUrlCData_shouldUnwrapUrl() {
            assertEquals("/openmrs/ws/rest/patient/patient-uuid",
                    worker.getPatientURL_CData("<![CDATA[/openmrs/ws/rest/patient/patient-uuid]]>"));
        }

        @Test
        @DisplayName("getHeaders should send basic credentials and reuse the same header set")
        void getHeaders_shouldSendBasicCredentialsOnce() {
            HttpHeaders first = worker.getHeaders();

            assertEquals("Basic " + Base64.getEncoder()
                    .encodeToString("feed-user:feed-secret".getBytes(StandardCharsets.UTF_8)),
                    first.getFirst("Authorization"));
            assertSame(first, worker.getHeaders(), "the header set is built once and cached");
        }

        @Test
        @DisplayName("mapFeedDataToModel should map the OpenMRS patient onto the ABDM profile document")
        void mapFeedDataToModel_shouldMapPatient() {
            OpenMRSPatientFullRepresentation source = new OpenMRSPatientFullRepresentation();
            OpenMRSPatientIdentifierType type = new OpenMRSPatientIdentifierType();
            type.setDisplay("Patient Identifier");
            OpenMRSPatientIdentifier identifier = new OpenMRSPatientIdentifier();
            identifier.setIdentifier("EXT-1");
            identifier.setIdentifierType(type);
            source.setIdentifiers(List.of(identifier));
            OpenMRSPerson person = new OpenMRSPerson();
            person.setGender("F");
            person.setBirthdate(new Date(650_000_000_000L));
            OpenMRSPersonName name = new OpenMRSPersonName();
            name.setGivenName("Asha");
            name.setMiddleName("Kumari");
            name.setFamilyName("Devi");
            name.setDisplay("Asha Kumari Devi");
            person.setPreferredName(name);
            OpenMRSPersonAddress address = new OpenMRSPersonAddress();
            address.setStateProvince("Jharkhand");
            address.setPostalCode("834006");
            address.setCountyDistrict("Ranchi");
            address.setCityVillage("Kanke");
            person.setPreferredAddress(address);
            source.setPerson(person);

            PatientDemographicModel_NDHM_Patient_Profile mapped = worker.mapFeedDataToModel(source);

            assertEquals("uuid-1", mapped.getRequestId());
            assertEquals("EXT-1", mapped.getExternalId());
            assertEquals("F", mapped.getProfile().getPatient().getGender());
            assertEquals("Asha", mapped.getProfile().getPatient().getFirstName());
            assertEquals("KumariDevi", mapped.getProfile().getPatient().getLastName());
            assertEquals("Asha Kumari Devi", mapped.getProfile().getPatient().getName());
            assertNotNull(mapped.getProfile().getPatient().getYearOfBirth());
            assertEquals("Jharkhand", mapped.getProfile().getPatient().getAddress().getState());
            assertEquals("Kanke", mapped.getProfile().getPatient().getAddress().getVillage());
            assertEquals("EXT-1",
                    mapped.getProfile().getPatient().getIdentifiers().get(0).get("Patient Identifier"));
        }

        @Test
        @DisplayName("mapFeedDataToModel should leave the surname unset when OpenMRS carries none")
        void mapFeedDataToModel_shouldLeaveSurnameUnset() {
            OpenMRSPatientFullRepresentation source = new OpenMRSPatientFullRepresentation();
            OpenMRSPatientIdentifierType type = new OpenMRSPatientIdentifierType();
            type.setDisplay("Bahmni Id");
            OpenMRSPatientIdentifier identifier = new OpenMRSPatientIdentifier();
            identifier.setIdentifier("BAH-1");
            identifier.setIdentifierType(type);
            source.setIdentifiers(List.of(identifier));
            OpenMRSPerson person = new OpenMRSPerson();
            OpenMRSPersonName name = new OpenMRSPersonName();
            name.setGivenName("Asha");
            person.setPreferredName(name);
            source.setPerson(person);

            PatientDemographicModel_NDHM_Patient_Profile mapped = worker.mapFeedDataToModel(source);

            assertNull(mapped.getProfile().getPatient().getLastName());
            assertNull(mapped.getExternalId(), "only a patient identifier becomes the external id");
        }

        @Test
        @DisplayName("mapFeedDataToModel should answer with a bare document when there is nothing to map")
        void mapFeedDataToModel_shouldAnswerBareDocument() {
            assertNotNull(worker.mapFeedDataToModel(null).getRequestId());
            assertNull(worker.mapFeedDataToModel(null).getProfile());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("ClinicalFeedWorker")
    class ClinicalFeedTests {

        @Mock
        private HttpUtils httpUtils;

        @Mock
        private ClinicalFeedDataLogRepo clinicalFeedDataLogRepo;

        @Mock
        private EncounterFullRepresentationRepo encounterFullRepresentationRepo;

        @InjectMocks
        private ClinicalFeedWorker worker;

        private LocalHttpStub stub;

        @BeforeEach
        void setUp() throws Exception {
            stub = new LocalHttpStub();
            ReflectionTestUtils.setField(worker, "atomsFeedStartPage", 1);
            ReflectionTestUtils.setField(worker, "parentUrl", stub.url(""));
            ReflectionTestUtils.setField(worker, "atomFeedURLPatientEncounter", ENCOUNTER_FEED_PATH);
            ReflectionTestUtils.setField(worker, "userName", "feed-user");
            ReflectionTestUtils.setField(worker, "password", "feed-secret");
        }

        @AfterEach
        void tearDown() {
            stub.close();
        }

        private String encounterJson(String patientId) {
            return "{\"encounterType\":\"consultation\",\"visitUuid\":\"visit-1\","
                    + (patientId == null ? "" : "\"patientId\":\"" + patientId + "\",")
                    + "\"visitType\":\"OPD\"}";
        }

        private void stubFeedPage(String nextArchive) {
            stub.stubOk(ENCOUNTER_FEED_PATH + "1", atomPage("urn:uuid:encounter-1",
                    "/openmrs/ws/rest/encounter/encounter-uuid", stub.url(ENCOUNTER_FEED_PATH + "1"), nextArchive));
        }

        @Test
        @DisplayName("readPatientEncounterFeeds should store the encounter behind each feed entry")
        void readFeeds_shouldStoreEachEncounter() throws Exception {
            stubFeedPage(null);
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class)))
                    .thenReturn(encounterJson("EXT-1"));
            when(encounterFullRepresentationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

            List<EncounterFullRepresentation> encounters =
                    worker.readPatientEncounterFeeds(stub.url(""), ENCOUNTER_FEED_PATH, 1, null);

            assertEquals(1, encounters.size());
            assertEquals("EXT-1", encounters.get(0).getPatientId());
            verify(clinicalFeedDataLogRepo).save(any(ClinicalFeedDataLog.class));
        }

        @Test
        @DisplayName("readPatientEncounterFeeds should log an encounter with no patient and carry on")
        void readFeeds_shouldLogEncounterWithoutPatient() throws Exception {
            stubFeedPage(null);
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class)))
                    .thenReturn(encounterJson(null));

            assertTrue(worker.readPatientEncounterFeeds(stub.url(""), ENCOUNTER_FEED_PATH, 1, null).isEmpty());
            verify(encounterFullRepresentationRepo, never()).save(any());
            verify(clinicalFeedDataLogRepo).save(any(ClinicalFeedDataLog.class));
        }

        @Test
        @DisplayName("readPatientEncounterFeeds should log a failed save and carry on")
        void readFeeds_shouldLogFailedSave() throws Exception {
            stubFeedPage(null);
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class)))
                    .thenReturn(encounterJson("EXT-1"));
            when(encounterFullRepresentationRepo.save(any())).thenReturn(null);

            assertTrue(worker.readPatientEncounterFeeds(stub.url(""), ENCOUNTER_FEED_PATH, 1, null).isEmpty());
            verify(clinicalFeedDataLogRepo).save(any(ClinicalFeedDataLog.class));
        }

        @Test
        @DisplayName("readPatientEncounterFeeds should resume from the entry the log last completed")
        void readFeeds_shouldResumeFromLoggedEntry() {
            stubFeedPage(null);
            ClinicalFeedDataLog log = new ClinicalFeedDataLog();
            log.setEntryID("urn:uuid:encounter-1");

            assertTrue(worker.readPatientEncounterFeeds(stub.url(""), ENCOUNTER_FEED_PATH, 1, log).isEmpty());
            verify(encounterFullRepresentationRepo, never()).save(any());
        }

        @Test
        @DisplayName("readPatientEncounterFeeds should page on to the next archive the feed points at")
        void readFeeds_shouldPageToNextArchive() throws Exception {
            stubFeedPage(stub.url(ENCOUNTER_FEED_PATH + "2"));
            stub.stubOk(ENCOUNTER_FEED_PATH + "2", atomPage("urn:uuid:encounter-2",
                    "/openmrs/ws/rest/encounter/encounter-uuid", stub.url(ENCOUNTER_FEED_PATH + "2"), null));
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class)))
                    .thenReturn(encounterJson("EXT-1"));
            when(encounterFullRepresentationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

            assertEquals(2,
                    worker.readPatientEncounterFeeds(stub.url(""), ENCOUNTER_FEED_PATH, 1, null).size());
        }

        @Test
        @DisplayName("readPatientEncounterFeeds should stop when the feed cannot be reached")
        void readFeeds_shouldStopOnUnreachableFeed() {
            ReflectionTestUtils.setField(worker, "parentUrl", "http://127.0.0.1:1");

            assertTrue(worker.readPatientEncounterFeeds("http://127.0.0.1:1", ENCOUNTER_FEED_PATH, 1, null)
                    .isEmpty());
        }

        @Test
        @DisplayName("encounterFeedManager should resume from the page the last successful log entry names")
        void encounterFeedManager_shouldResumeFromLoggedPage() throws Exception {
            ClinicalFeedDataLog log = new ClinicalFeedDataLog();
            log.setId("log-1");
            log.setLinkSelf(stub.url(ENCOUNTER_FEED_PATH + "1"));
            log.setEntryID("urn:uuid:encounter-1");
            when(clinicalFeedDataLogRepo.findOneByEntrySuccessOrderByIdDesc(true)).thenReturn(log);
            stubFeedPage(null);

            assertEquals("[]", worker.encounterFeedManager(),
                    "the page is walked but its only entry precedes the resume point");
        }

        @Test
        @DisplayName("encounterFeedManager should start at the configured page when nothing was ever logged")
        void encounterFeedManager_shouldStartAtConfiguredPage() throws Exception {
            when(clinicalFeedDataLogRepo.findOneByEntrySuccessOrderByIdDesc(true)).thenReturn(null);
            stubFeedPage(null);
            when(httpUtils.getPatientDataFromFeed(anyString(), any(HttpHeaders.class)))
                    .thenReturn(encounterJson("EXT-1"));
            when(encounterFullRepresentationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

            assertTrue(worker.encounterFeedManager().contains("EXT-1"));
        }

        @Test
        @DisplayName("getPatientEncounterURL_CData should unwrap the URL out of the entry's CDATA section")
        void getEncounterUrlCData_shouldUnwrapUrl() {
            assertEquals("/openmrs/ws/rest/encounter/encounter-uuid", worker.getPatientEncounterURL_CData(
                    "<![CDATA[/openmrs/ws/rest/encounter/encounter-uuid]]>"));
        }

        @Test
        @DisplayName("getHeaders should send basic credentials and reuse the same header set")
        void getHeaders_shouldSendBasicCredentialsOnce() {
            HttpHeaders first = worker.getHeaders();

            assertEquals("Basic " + Base64.getEncoder()
                    .encodeToString("feed-user:feed-secret".getBytes(StandardCharsets.UTF_8)),
                    first.getFirst("Authorization"));
            assertSame(first, worker.getHeaders());
        }
    }
}
