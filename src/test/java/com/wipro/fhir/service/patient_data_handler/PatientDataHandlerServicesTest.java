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
package com.wipro.fhir.service.patient_data_handler;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.EncounterFullRepresentation;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.GroupMembers;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.Observations;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.VisitWiseEncounterData;
import com.wipro.fhir.data.healthID.BenHealthIDMapping;
import com.wipro.fhir.data.patient.M_title;
import com.wipro.fhir.data.patient.PatientAddress;
import com.wipro.fhir.data.patient.PatientDemographicDetails;
import com.wipro.fhir.data.patient.PatientPhoneMaps;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.patient_data_handler.TRG_PatientResourceData;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.EncounterFullRepresentationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.repo.patient_data_handler.TRG_PatientResourceData_Repo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Patient data handler services Test Suite")
class PatientDataHandlerServicesTest {

    private static final Timestamp CREATED = new Timestamp(1_700_000_000_000L);

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("HigherHealthFacilityServiceImpl")
    class HigherHealthFacilityTests {

        @Mock
        private PatientDemographicModel_NDHM_Patient_Profile_Repo patientProfileRepo;

        @Mock
        private EncounterFullRepresentationRepo encounterFullRepresentationRepo;

        @InjectMocks
        private HigherHealthFacilityServiceImpl service;

        private ResourceRequestHandler request() {
            ResourceRequestHandler request = new ResourceRequestHandler();
            request.setAmritId("AM-1");
            request.setExternalId("EXT-1");
            return request;
        }

        private PatientDemographicModel_NDHM_Patient_Profile profileWithPatient() {
            PatientDemographicModel_NDHM_Patient_Profile profile =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            profile.setExternalId("EXT-1");
            PatientDemographicModel_NDHM_Patient_Profile.Profile inner = profile.new Profile();
            PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient patient = inner.new Patient();
            patient.setIdentifiers(new ArrayList<>());
            inner.setPatient(patient);
            profile.setProfile(inner);
            return profile;
        }

        @Test
        @DisplayName("updateBengenIDToHigherHealthFacilityBeneficiary should stamp the AMRIT id on every match")
        void updateBengenID_shouldStampAmritIdOnEveryMatch() throws Exception {
            PatientDemographicModel_NDHM_Patient_Profile profile = profileWithPatient();
            when(patientProfileRepo.findByExternalId("EXT-1")).thenReturn(List.of(profile));

            assertEquals("Beneficiary ID updated successfully",
                    service.updateBengenIDToHigherHealthFacilityBeneficiary(request()));

            assertEquals("AM-1", profile.getAmritId());
            assertEquals("AM-1", profile.getProfile().getPatient().getIdentifiers().get(0).get("amritId"));
            verify(patientProfileRepo).saveAll(List.of(profile));
        }

        @Test
        @DisplayName("updateBengenIDToHigherHealthFacilityBeneficiary should still succeed when nothing matched")
        void updateBengenID_shouldSucceedWithoutMatches() throws Exception {
            when(patientProfileRepo.findByExternalId("EXT-1")).thenReturn(List.of());

            assertEquals("Beneficiary ID updated successfully",
                    service.updateBengenIDToHigherHealthFacilityBeneficiary(request()));
        }

        private EncounterFullRepresentation encounter(String visitUuid, String encounterType,
                List<GroupMembers> groupMembers) {
            EncounterFullRepresentation encounter = new EncounterFullRepresentation();
            encounter.setVisitUuid(visitUuid);
            encounter.setEncounterType(encounterType);
            encounter.setPatientId("EXT-1");
            encounter.setVisitType("OPD");
            encounter.setVisitTypeUuid("visit-type-1");
            encounter.setEncounterDateTime(new java.util.Date(1_700_000_000_000L));
            if (groupMembers != null) {
                Observations observation = new Observations();
                observation.setGroupMembers(groupMembers);
                encounter.setObservations(List.of(observation));
            }
            return encounter;
        }

        private GroupMembers leaf(String uuid) {
            GroupMembers member = new GroupMembers();
            member.setUuid(uuid);
            return member;
        }

        @Test
        @DisplayName("getCLinicalDataHigherhealthFacility should group the encounters of one visit together")
        void getClinicalData_shouldGroupEncountersByVisit() throws Exception {
            PatientDemographicModel_NDHM_Patient_Profile profile = profileWithPatient();
            when(patientProfileRepo.findByAmritId("AM-1")).thenReturn(List.of(profile));
            when(encounterFullRepresentationRepo.findByPatientId("EXT-1")).thenReturn(List.of(
                    encounter("visit-1", "reg", List.of(leaf("gm-1"))),
                    encounter("visit-1", "consultation", List.of(leaf("gm-2"))),
                    encounter("visit-2", "reg", List.of(leaf("gm-3")))));

            String result = service.getCLinicalDataHigherhealthFacility(request());

            assertEquals(2, JsonParser.parseString(result).getAsJsonArray().size());
            assertTrue(result.contains("visit-1"), result);
            assertTrue(result.contains("visit-2"), result);
        }

        @Test
        @DisplayName("getCLinicalDataHigherhealthFacility should skip an encounter type it already processed")
        void getClinicalData_shouldSkipRepeatedEncounterType() throws Exception {
            when(patientProfileRepo.findByAmritId("AM-1")).thenReturn(List.of(profileWithPatient()));
            when(encounterFullRepresentationRepo.findByPatientId("EXT-1")).thenReturn(List.of(
                    encounter("visit-1", "reg", List.of(leaf("gm-1"))),
                    encounter("visit-1", "reg", List.of(leaf("gm-2")))));

            String result = service.getCLinicalDataHigherhealthFacility(request());

            assertEquals(1, JsonParser.parseString(result).getAsJsonArray().size());
        }

        @Test
        @DisplayName("getCLinicalDataHigherhealthFacility should answer with nothing when nobody matches")
        void getClinicalData_shouldAnswerEmptyWithoutProfile() throws Exception {
            when(patientProfileRepo.findByAmritId("AM-1")).thenReturn(List.of());

            assertEquals("[]", service.getCLinicalDataHigherhealthFacility(request()));
            verify(encounterFullRepresentationRepo, never()).findByPatientId(anyString());
        }

        @Test
        @DisplayName("getCLinicalDataHigherhealthFacility should answer with nothing when the visit has no encounter")
        void getClinicalData_shouldAnswerEmptyWithoutEncounters() throws Exception {
            when(patientProfileRepo.findByAmritId("AM-1")).thenReturn(List.of(profileWithPatient()));
            when(encounterFullRepresentationRepo.findByPatientId("EXT-1")).thenReturn(List.of());

            assertEquals("[]", service.getCLinicalDataHigherhealthFacility(request()));
        }

        @Test
        @DisplayName("getVisitWiseEncounterData should flatten a nested observation group down to its leaves")
        void getVisitWiseEncounterData_shouldFlattenNestedGroups() {
            GroupMembers parent = new GroupMembers();
            parent.setUuid("gm-parent");
            parent.setGroupMembers(List.of(leaf("gm-child-1"), leaf("gm-child-2")));

            List<VisitWiseEncounterData> visits = service.getVisitWiseEncounterData(
                    List.of(encounter("visit-1", "reg", List.of(parent))));

            assertEquals(1, visits.size());
            assertEquals(2, visits.get(0).getObservationsGMReg().size(),
                    "only the leaf group members are carried forward");
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("PatientDataGatewayServiceImpl")
    class PatientDataGatewayTests {

        @Mock
        private PatientDemographicModel_NDHM_Patient_Profile_Repo profileRepo;

        @Mock
        private CommonService commonService;

        @Mock
        private TRG_PatientResourceData_Repo trgRepo;

        @Mock
        private APIChannel aPIChannel;

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @InjectMocks
        private PatientDataGatewayServiceImpl service;

        private static final String AUTHORIZATION = "session-key-123";

        private void configurePageSize() {
            ReflectionTestUtils.setField(service, "patient_search_page_size", "10");
        }

        private TRG_PatientResourceData trigger() {
            TRG_PatientResourceData trigger = new TRG_PatientResourceData();
            trigger.setId(1L);
            trigger.setBeneficiaryID(BigInteger.valueOf(9999L));
            trigger.setCreatedDate(CREATED);
            return trigger;
        }

        private PatientDemographicDetails demographics(Integer genderID) {
            PatientDemographicDetails details = new PatientDemographicDetails();
            details.setBeneficiaryID(9999L);
            details.setBeneficiaryRegID(4321L);
            details.setFirstName("Asha");
            details.setLastName("Devi");
            details.setGenderID(genderID);
            details.setdOB(CREATED);
            M_title title = new M_title();
            title.setTitleName("Smt");
            details.setM_title(title);
            PatientAddress address = new PatientAddress();
            address.setPinCode("834006");
            address.setDistrictName("Ranchi");
            address.setStateName("Jharkhand");
            address.setAddressLine1("Kanke Road");
            address.setAddressLine2("Near PHC");
            details.setI_bendemographics(address);
            PatientPhoneMaps phone = new PatientPhoneMaps();
            phone.setPhoneNo("9999999999");
            details.setBenPhoneMaps(new ArrayList<>(List.of(phone)));
            return details;
        }

        private String searchAnswer(Integer genderID) {
            return "{\"data\":[{\"beneficiaryID\":9999,\"beneficiaryRegID\":4321,\"firstName\":\"Asha\","
                    + "\"lastName\":\"Devi\",\"genderID\":" + genderID + ",\"dOB\":\"2023-11-14T22:13:20.000\","
                    + "\"m_title\":{\"titleName\":\"Smt\"},"
                    + "\"i_bendemographics\":{\"pinCode\":\"834006\",\"districtName\":\"Ranchi\","
                    + "\"stateName\":\"Jharkhand\",\"addressLine1\":\"Kanke Road\","
                    + "\"addressLine2\":\"Near PHC\"},"
                    + "\"benPhoneMaps\":[{\"phoneNo\":\"9999999999\"}]}]}";
        }

        @Test
        @DisplayName("feedPatientProfileToMongoDB should lift the ABHA identifiers onto the profile document")
        void feedPatientProfileToMongoDB_shouldLiftAbhaIdentifiers() throws Exception {
            PatientDemographicModel_NDHM_Patient_Profile profile =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            PatientDemographicModel_NDHM_Patient_Profile.Profile inner = profile.new Profile();
            PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient patient = inner.new Patient();
            patient.setHealthId("abc@sbx");
            patient.setHealthIdNumber("11-1111-1111-1111");
            inner.setPatient(patient);
            profile.setProfile(inner);
            when(commonService.savePatientProfileDataToMongo(any())).thenAnswer(i -> i.getArgument(0));

            List<PatientDemographicModel_NDHM_Patient_Profile> saved =
                    service.feedPatientProfileToMongoDB(new ArrayList<>(List.of(profile)));

            assertEquals("abc@sbx", saved.get(0).getHealthId());
            assertEquals("11-1111-1111-1111", saved.get(0).getHealthIdNumber());
        }

        @Test
        @DisplayName("feedPatientProfileToMongoDB should save nothing when given an empty batch")
        void feedPatientProfileToMongoDB_shouldSaveNothingForEmptyBatch() throws Exception {
            assertNull(service.feedPatientProfileToMongoDB(new ArrayList<>()));

            verify(commonService, never()).savePatientProfileDataToMongo(any());
        }

        @Test
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should build a profile per pending trigger row")
        void generateProfile_shouldBuildProfilePerTrigger() throws Exception {
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20())
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId("9999")).thenReturn(List.of());
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(searchAnswer(2));
            when(commonService.getUUID()).thenReturn("uuid-1");
            when(commonService.savePatientProfileDataToMongo(any())).thenAnswer(i -> i.getArgument(0));

            String result = service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION);

            JsonObject profile = JsonParser.parseString(result).getAsJsonArray().get(0).getAsJsonObject();
            assertEquals("uuid-1", profile.get("requestId").getAsString());
            assertEquals("9999", profile.get("amritId").getAsString());
            JsonObject patient = profile.getAsJsonObject("profile").getAsJsonObject("patient");
            assertEquals("Smt Asha Devi", patient.get("name").getAsString());
            assertEquals("F", patient.get("gender").getAsString());
            assertEquals("Ranchi", patient.getAsJsonObject("address").get("district").getAsString());
            assertEquals("Kanke Road Near PHC ", patient.getAsJsonObject("address").get("line").getAsString());
            assertEquals("9999999999",
                    patient.getAsJsonArray("identifiers").get(0).getAsJsonObject().get("MOBILE").getAsString());
            verify(trgRepo).updateProcessedFlagForProfileCreated(List.of(0L, 1L));
        }

        @ParameterizedTest(name = "genderID {0} -> {1}")
        @CsvSource({ "1, M", "2, F", "3, O", "4, UNKNOWN" })
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should map the gender id onto the ABDM letter")
        void generateProfile_shouldMapGender(Integer genderID, String expected) throws Exception {
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20())
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId(anyString())).thenReturn(List.of());
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(searchAnswer(genderID));
            when(commonService.getUUID()).thenReturn("uuid-1");
            when(commonService.savePatientProfileDataToMongo(any())).thenAnswer(i -> i.getArgument(0));

            String result = service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION);

            assertEquals(expected, JsonParser.parseString(result).getAsJsonArray().get(0).getAsJsonObject()
                    .getAsJsonObject("profile").getAsJsonObject("patient").get("gender").getAsString());
        }

        @Test
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should carry the latest linked ABHA onto the profile")
        void generateProfile_shouldCarryLatestAbha() throws Exception {
            BenHealthIDMapping older = new BenHealthIDMapping();
            older.setHealthId("old@sbx");
            older.setHealthIdNumber("11-0000-0000-0000");
            BenHealthIDMapping latest = new BenHealthIDMapping();
            latest.setHealthId("abc@sbx");
            latest.setHealthIdNumber("11-1111-1111-1111");
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20())
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId(anyString())).thenReturn(List.of());
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(searchAnswer(2));
            when(commonService.getUUID()).thenReturn("uuid-1");
            when(benHealthIDMappingRepo.getHealthDetails(4321L))
                    .thenReturn(new ArrayList<>(List.of(older, latest)));
            when(commonService.savePatientProfileDataToMongo(any())).thenAnswer(i -> i.getArgument(0));

            String result = service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION);

            JsonObject profile = JsonParser.parseString(result).getAsJsonArray().get(0).getAsJsonObject();
            assertEquals("abc@sbx", profile.get("healthId").getAsString());
            assertEquals("11-1111-1111-1111", profile.get("healthIdNumber").getAsString());
        }

        @Test
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should skip a beneficiary that already has a profile")
        void generateProfile_shouldSkipKnownBeneficiary() throws Exception {
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20())
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId("9999"))
                    .thenReturn(List.of(new PatientDemographicModel_NDHM_Patient_Profile()));

            assertEquals("null", service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION),
                    "nothing was fed to Mongo, so there is no profile list to serialise");
            verify(aPIChannel, never()).benSearchByBenID(anyString(), any());
        }

        @Test
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should skip a beneficiary the search cannot pin down")
        void generateProfile_shouldSkipAmbiguousSearch() throws Exception {
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20())
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId(anyString())).thenReturn(List.of());
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn("{\"data\":[]}");

            assertEquals("null", service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION));
        }

        @Test
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should log a failing beneficiary and carry on")
        void generateProfile_shouldCarryOnAfterSearchFailure() throws Exception {
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20())
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId(anyString())).thenReturn(List.of());
            when(aPIChannel.benSearchByBenID(anyString(), any()))
                    .thenThrow(new FHIRException("error in patient search"));

            assertEquals("null", service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION));
        }

        @Test
        @DisplayName("generatePatientProfileAMRIT_SaveTo_Mongo should answer with nothing when no trigger is pending")
        void generateProfile_shouldAnswerEmptyWithoutTriggers() throws Exception {
            when(trgRepo.getByProcessedOrderByCreatedDateLimit20()).thenReturn(new ArrayList<>());

            assertEquals("null", service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION));
            verify(trgRepo, never()).updateProcessedFlagForProfileCreated(any());
        }

        @Test
        @DisplayName("the beneficiary-scoped overload should read the latest trigger row for that beneficiary")
        void generateProfileForBeneficiary_shouldReadLatestTrigger() throws Exception {
            ResourceRequestHandler request = new ResourceRequestHandler();
            request.setBeneficiaryID(BigInteger.valueOf(9999L));
            when(trgRepo.getByBenIdLatestRecord(BigInteger.valueOf(9999L)))
                    .thenReturn(new ArrayList<>(List.of(trigger())));
            when(profileRepo.findByAmritId(anyString())).thenReturn(List.of());
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(searchAnswer(2));
            when(commonService.getUUID()).thenReturn("uuid-1");
            when(commonService.savePatientProfileDataToMongo(any())).thenAnswer(i -> i.getArgument(0));

            String result = service.generatePatientProfileAMRIT_SaveTo_Mongo(AUTHORIZATION, request);

            assertTrue(result.contains("uuid-1"), result);
            verify(trgRepo).getByBenIdLatestRecord(BigInteger.valueOf(9999L));
        }

        @Test
        @DisplayName("searchPatientProfileMongo should page the matches with the configured page size")
        void searchPatientProfileMongo_shouldPageMatches() throws Exception {
            configurePageSize();
            PatientDemographicModel_NDHM_Patient_Profile match =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            match.setAmritId("AM-1");
            Page<PatientDemographicModel_NDHM_Patient_Profile> page =
                    new PageImpl<>(List.of(match), PageRequest.of(0, 10), 1);
            when(commonService.searchPatientProfileFromMongo(any())).thenReturn(page);

            JsonObject result = JsonParser
                    .parseString(service.searchPatientProfileMongo(AUTHORIZATION, new ResourceRequestHandler()))
                    .getAsJsonObject();

            assertEquals(10, result.get("pageSize").getAsInt());
            assertEquals(1, result.get("totalPage").getAsInt());
            assertEquals("AM-1", result.getAsJsonArray("data").get(0).getAsJsonObject().get("amritId").getAsString());
        }

        @Test
        @DisplayName("searchPatientProfileMongo should answer with an empty data page when nothing matched")
        void searchPatientProfileMongo_shouldAnswerEmptyPageWithoutMatches() throws Exception {
            configurePageSize();
            when(commonService.searchPatientProfileFromMongo(any()))
                    .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

            JsonObject result = JsonParser
                    .parseString(service.searchPatientProfileMongo(AUTHORIZATION, new ResourceRequestHandler()))
                    .getAsJsonObject();

            assertTrue(result.getAsJsonArray("data").isEmpty());
            assertEquals(0, result.get("totalPage").getAsInt());
        }

        @Test
        @DisplayName("searchPatientProfileMongo should answer with nothing when the search returns no page at all")
        void searchPatientProfileMongo_shouldAnswerNullWithoutPage() throws Exception {
            when(commonService.searchPatientProfileFromMongo(any())).thenReturn(null);

            assertNull(service.searchPatientProfileMongo(AUTHORIZATION, new ResourceRequestHandler()));
        }

        @Test
        @DisplayName("searchPatientProfileMongo should insist on a search request")
        void searchPatientProfileMongo_shouldInsistOnRequest() {
            assertTrue(assertThrows(FHIRException.class,
                    () -> service.searchPatientProfileMongo(AUTHORIZATION, null)).getMessage()
                    .startsWith("request is null."));
        }

        @Test
        @DisplayName("searchPatientProfileMongoPagination should read the requested page of profiles")
        void searchPaginated_shouldReadRequestedPage() throws Exception {
            configurePageSize();
            PatientDemographicModel_NDHM_Patient_Profile match =
                    new PatientDemographicModel_NDHM_Patient_Profile();
            match.setAmritId("AM-1");
            when(profileRepo.findAll(PageRequest.of(2, 10)))
                    .thenReturn(new PageImpl<>(List.of(match), PageRequest.of(2, 10), 25));

            JsonObject result = JsonParser.parseString(service.searchPatientProfileMongoPagination(2))
                    .getAsJsonObject();

            assertEquals(10, result.get("pageSize").getAsInt());
            assertEquals(3, result.get("totalPage").getAsInt());
        }

        @Test
        @DisplayName("searchPatientProfileMongoPagination should answer with nothing for a page beyond the data")
        void searchPaginated_shouldAnswerNullBeyondData() throws Exception {
            configurePageSize();
            when(profileRepo.findAll(any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(), PageRequest.of(99, 10), 0));

            assertNull(service.searchPatientProfileMongoPagination(99));
        }

        @Test
        @DisplayName("searchPatientProfileMongoPagination should insist on a page number")
        void searchPaginated_shouldInsistOnPageNumber() {
            configurePageSize();

            assertEquals("Invalid page no", assertThrows(FHIRException.class,
                    () -> service.searchPatientProfileMongoPagination(null)).getMessage());
        }
    }
}
