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
package com.wipro.fhir.service.healthID;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
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

import com.wipro.fhir.data.healthID.BenHealthIDMapping;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.elasticsearch.AbhaElasticsearchSyncService;
import com.wipro.fhir.service.ndhm.CreateHealthID_Aadhaar_NDHMService;
import com.wipro.fhir.service.ndhm.CreateHealthID_MobileOTP_NDHMService;
import com.wipro.fhir.service.ndhm.GenerateHealthID_CardService;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The ABHA (health ID) services: thin façades over the NDHM gateway calls, plus the
 * mapping service that ties a created ABHA to an AMRIT beneficiary.
 */
@DisplayName("ABHA services Test Suite")
class HealthIDServicesTest {

    private static final String REQUEST = "{\"txnId\":\"txn-1\"}";

    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("HealthIDWithBioServiceImpl")
    class WithBioTests {

        @Mock
        private HealthIDRepo healthIDRepo;

        @Mock
        private CreateHealthID_Aadhaar_NDHMService ndhmService;

        @InjectMocks
        private HealthIDWithBioServiceImpl service;

        @Test
        @DisplayName("verifyBio should pass the ABDM answer straight through")
        void verifyBio_shouldReturnNdhmAnswer() throws Exception {
            when(ndhmService.verifyBio(REQUEST)).thenReturn("{\"verified\":true}");

            assertEquals("{\"verified\":true}", service.verifyBio(REQUEST));
        }

        @Test
        @DisplayName("verifyBio should rewrap a downstream failure as a FHIRException")
        void verifyBio_shouldRewrapFailure() throws Exception {
            when(ndhmService.verifyBio(REQUEST)).thenThrow(new FHIRException("bio mismatch"));

            assertEquals("bio mismatch",
                    assertThrows(FHIRException.class, () -> service.verifyBio(REQUEST)).getMessage());
        }

        @Test
        @DisplayName("verifyBio should refuse an empty request without calling ABDM")
        void verifyBio_shouldRefuseNullRequest() throws Exception {
            assertEquals("NDHM_FHIR Error while Verifying Bio",
                    assertThrows(FHIRException.class, () -> service.verifyBio(null)).getMessage());
            verify(ndhmService, never()).verifyBio(anyString());
        }

        @Test
        @DisplayName("generateMobileOTP should pass the ABDM answer straight through")
        void generateMobileOTP_shouldReturnNdhmAnswer() throws Exception {
            when(ndhmService.generateMobileOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-2\"}");

            assertEquals("{\"txnId\":\"txn-2\"}", service.generateMobileOTP(REQUEST));
        }

        @Test
        @DisplayName("generateMobileOTP should refuse an empty request without calling ABDM")
        void generateMobileOTP_shouldRefuseNullRequest() {
            assertEquals("NDHM_FHIR Error Entered OTP is incorrect",
                    assertThrows(FHIRException.class, () -> service.generateMobileOTP(null)).getMessage());
        }

        @Test
        @DisplayName("confirmWithAadhaarBio should pass the ABDM answer straight through")
        void confirmWithAadhaarBio_shouldReturnNdhmAnswer() throws Exception {
            when(ndhmService.confirmWithAadhaarBio(REQUEST)).thenReturn("{\"confirmed\":true}");

            assertEquals("{\"confirmed\":true}", service.confirmWithAadhaarBio(REQUEST));
        }

        @Test
        @DisplayName("confirmWithAadhaarBio should refuse an empty request without calling ABDM")
        void confirmWithAadhaarBio_shouldRefuseNullRequest() {
            assertEquals("NDHM_FHIR Error while Verifying Bio",
                    assertThrows(FHIRException.class, () -> service.confirmWithAadhaarBio(null)).getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("HealthIDWithUIDServiceImpl")
    class WithUidTests {

        @Mock
        private HealthIDRepo healthIDRepo;

        @Mock
        private CreateHealthID_Aadhaar_NDHMService ndhmService;

        @InjectMocks
        private HealthIDWithUIDServiceImpl service;

        @Test
        @DisplayName("generateOTP should pass the ABDM answer straight through")
        void generateOTP_shouldReturnNdhmAnswer() throws Exception {
            when(ndhmService.generateOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

            assertEquals("{\"txnId\":\"txn-1\"}", service.generateOTP(REQUEST));
        }

        @Test
        @DisplayName("generateOTP should fail when ABDM answers with nothing")
        void generateOTP_shouldFailOnNullAnswer() throws Exception {
            when(ndhmService.generateOTP(REQUEST)).thenReturn(null);

            assertEquals("NDHM_FHIR Error while generating OTP",
                    assertThrows(FHIRException.class, () -> service.generateOTP(REQUEST)).getMessage());
        }

        @Test
        @DisplayName("verifyOTP, checkAndGenerateOTP and verifyMobileOTP each relay their ABDM call")
        void otpSteps_shouldRelayNdhmAnswers() throws Exception {
            when(ndhmService.verifyOTP(REQUEST)).thenReturn("verified");
            when(ndhmService.checkAndGenerateMobileOTP(REQUEST)).thenReturn("mobile-otp-sent");
            when(ndhmService.verifyMobileOTP(REQUEST)).thenReturn("mobile-verified");

            assertEquals("verified", service.verifyOTP(REQUEST));
            assertEquals("mobile-otp-sent", service.checkAndGenerateOTP(REQUEST));
            assertEquals("mobile-verified", service.verifyMobileOTP(REQUEST));
        }

        @Test
        @DisplayName("each OTP step should refuse an empty request without calling ABDM")
        void otpSteps_shouldRefuseNullRequest() {
            String expected = "NDHM_FHIR Error Entered OTP is incorrect";

            assertEquals(expected, assertThrows(FHIRException.class, () -> service.verifyOTP(null)).getMessage());
            assertEquals(expected,
                    assertThrows(FHIRException.class, () -> service.checkAndGenerateOTP(null)).getMessage());
            assertEquals(expected,
                    assertThrows(FHIRException.class, () -> service.verifyMobileOTP(null)).getMessage());
        }

        @Test
        @DisplayName("createHealthIDWithUID should flatten the ABDM auth methods and save the ABHA")
        void createHealthIDWithUID_shouldSaveAbha() throws Exception {
            HealthIDResponse created = new HealthIDResponse();
            created.setAuthMethods(List.of("AADHAAR_BIO", "MOBILE_OTP"));
            when(ndhmService.createHealthIDWithUID(anyString())).thenReturn(created);
            when(healthIDRepo.save(any(HealthIDResponse.class))).thenAnswer(i -> i.getArgument(0));

            String result = service.createHealthIDWithUID(
                    "{\"txnId\":\"txn-1\",\"createdBy\":\"admin\",\"providerServiceMapID\":7}");

            ArgumentCaptor<HealthIDResponse> captor = ArgumentCaptor.forClass(HealthIDResponse.class);
            verify(healthIDRepo).save(captor.capture());
            HealthIDResponse saved = captor.getValue();
            assertEquals("AADHAAR_BIO||MOBILE_OTP", saved.getAuthMethod());
            assertEquals("admin", saved.getCreatedBy());
            assertEquals("txn-1", saved.getTxnId());
            assertEquals(7, saved.getProviderServiceMapID());
            assertTrue(result.contains("AADHAAR_BIO||MOBILE_OTP"), result);
        }

        @Test
        @DisplayName("createHealthIDWithUID should leave the auth method unset when ABDM lists none")
        void createHealthIDWithUID_shouldLeaveAuthMethodUnset() throws Exception {
            when(ndhmService.createHealthIDWithUID(anyString())).thenReturn(new HealthIDResponse());
            when(healthIDRepo.save(any(HealthIDResponse.class))).thenAnswer(i -> i.getArgument(0));

            service.createHealthIDWithUID("{\"txnId\":\"txn-1\"}");

            ArgumentCaptor<HealthIDResponse> captor = ArgumentCaptor.forClass(HealthIDResponse.class);
            verify(healthIDRepo).save(captor.capture());
            assertNull(captor.getValue().getAuthMethod());
        }

        @Test
        @DisplayName("createHealthIDWithUID should fail when ABDM creates no ABHA")
        void createHealthIDWithUID_shouldFailWithoutAbha() throws Exception {
            when(ndhmService.createHealthIDWithUID(anyString())).thenReturn(null);

            assertEquals("NDHM_FHIR Error while creating ABHA", assertThrows(FHIRException.class,
                    () -> service.createHealthIDWithUID(REQUEST)).getMessage());
            verify(healthIDRepo, never()).save(any(HealthIDResponse.class));
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("HealthID_WithMobileOTPServiceImpl")
    class WithMobileOtpTests {

        @Mock
        private HealthIDRepo healthIDRepo;

        @Mock
        private CreateHealthID_MobileOTP_NDHMService ndhmService;

        @InjectMocks
        private HealthID_WithMobileOTPServiceImpl service;

        @Test
        @DisplayName("generateOTP should pass the ABDM answer straight through")
        void generateOTP_shouldReturnNdhmAnswer() throws Exception {
            when(ndhmService.generateOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

            assertEquals("{\"txnId\":\"txn-1\"}", service.generateOTP(REQUEST));
        }

        @Test
        @DisplayName("generateOTP should fail when ABDM answers with nothing")
        void generateOTP_shouldFailOnNullAnswer() throws Exception {
            when(ndhmService.generateOTP(REQUEST)).thenReturn(null);

            assertEquals("NDHM_FHIR Error while generating OTP",
                    assertThrows(FHIRException.class, () -> service.generateOTP(REQUEST)).getMessage());
        }

        @Test
        @DisplayName("verifyOTPandGenerateHealthID should save the created ABHA with the caller's metadata")
        void verifyOTPandGenerateHealthID_shouldSaveAbha() throws Exception {
            HealthIDResponse created = new HealthIDResponse();
            created.setAuthMethods(List.of("MOBILE_OTP"));
            when(ndhmService.validateOTP(anyString())).thenReturn("otp-token");
            when(ndhmService.createHealthID(anyString(), eq("otp-token"))).thenReturn(created);
            when(healthIDRepo.save(any(HealthIDResponse.class))).thenAnswer(i -> i.getArgument(0));

            String result = service.verifyOTPandGenerateHealthID(
                    "{\"txnId\":\"txn-1\",\"createdBy\":\"admin\",\"providerServiceMapID\":7}");

            ArgumentCaptor<HealthIDResponse> captor = ArgumentCaptor.forClass(HealthIDResponse.class);
            verify(healthIDRepo).save(captor.capture());
            assertEquals("MOBILE_OTP", captor.getValue().getAuthMethod());
            assertEquals("admin", captor.getValue().getCreatedBy());
            assertTrue(result.contains("MOBILE_OTP"), result);
        }

        @Test
        @DisplayName("verifyOTPandGenerateHealthID should fail when the OTP does not validate")
        void verifyOTPandGenerateHealthID_shouldFailOnInvalidOtp() throws Exception {
            when(ndhmService.validateOTP(anyString())).thenReturn(null);

            assertEquals("NDHM_FHIR Error while validating OTP", assertThrows(FHIRException.class,
                    () -> service.verifyOTPandGenerateHealthID(REQUEST)).getMessage());
        }

        @Test
        @DisplayName("verifyOTPandGenerateHealthID should fail when ABDM creates no ABHA")
        void verifyOTPandGenerateHealthID_shouldFailWithoutAbha() throws Exception {
            when(ndhmService.validateOTP(anyString())).thenReturn("otp-token");
            when(ndhmService.createHealthID(anyString(), anyString())).thenReturn(null);

            assertEquals("NDHM_FHIR Error while creating ABHA", assertThrows(FHIRException.class,
                    () -> service.verifyOTPandGenerateHealthID(REQUEST)).getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("HealthID_CardServiceImpl")
    class CardTests {

        @Mock
        private GenerateHealthID_CardService cardService;

        @InjectMocks
        private HealthID_CardServiceImpl service;

        @Test
        @DisplayName("generateOTP should pass the ABDM answer straight through")
        void generateOTP_shouldReturnNdhmAnswer() throws Exception {
            when(cardService.generateOTP(REQUEST)).thenReturn("{\"txnId\":\"txn-1\"}");

            assertEquals("{\"txnId\":\"txn-1\"}", service.generateOTP(REQUEST));
        }

        @Test
        @DisplayName("generateOTP should fail when ABDM answers with nothing")
        void generateOTP_shouldFailOnNullAnswer() throws Exception {
            when(cardService.generateOTP(REQUEST)).thenReturn(null);

            assertEquals("NDHM_FHIR Error while generating OTP",
                    assertThrows(FHIRException.class, () -> service.generateOTP(REQUEST)).getMessage());
        }

        @Test
        @DisplayName("verifyOTPAndGenerateCard should wrap the generated card under a data key")
        void verifyOTPAndGenerateCard_shouldWrapCard() throws Exception {
            when(cardService.validateOTP(REQUEST)).thenReturn("x-token");
            when(cardService.generateCard(REQUEST, "x-token")).thenReturn("card-png");

            assertEquals("card-png",
                    new JSONObject(service.verifyOTPAndGenerateCard(REQUEST)).getString("data"));
        }

        @Test
        @DisplayName("verifyOTPAndGenerateCard should fail when the OTP does not validate")
        void verifyOTPAndGenerateCard_shouldFailOnInvalidOtp() throws Exception {
            when(cardService.validateOTP(REQUEST)).thenReturn(null);

            assertEquals("NDHM_FHIR Error while validating OTP", assertThrows(FHIRException.class,
                    () -> service.verifyOTPAndGenerateCard(REQUEST)).getMessage());
        }

        @Test
        @DisplayName("verifyOTPAndGenerateCard should fail when ABDM returns no card")
        void verifyOTPAndGenerateCard_shouldFailWithoutCard() throws Exception {
            when(cardService.validateOTP(REQUEST)).thenReturn("x-token");
            when(cardService.generateCard(REQUEST, "x-token")).thenReturn(null);

            assertEquals("NDHM_FHIR Error while generating ABHA card", assertThrows(FHIRException.class,
                    () -> service.verifyOTPAndGenerateCard(REQUEST)).getMessage());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("HealthIDServiceImpl")
    class MappingTests {

        @Mock
        private BenHealthIDMappingRepo benHealthIDMappingRepo;

        @Mock
        private HealthIDRepo healthIDRepo;

        @Mock
        private AbhaElasticsearchSyncService abhaEsSyncService;

        @InjectMocks
        private HealthIDServiceImpl service;

        private String mappingRequest(String identifier) {
            return "{" + identifier + ",\"healthIdNumber\":\"11-1111-1111-1111\",\"createdBy\":\"admin\","
                    + "\"providerServiceMapId\":7,\"isNew\":true,"
                    + "\"ABHAProfile\":{\"ABHANumber\":\"11-1111-1111-1111\","
                    + "\"phrAddress\":[\"abc@sbx\",\"def@sbx\"],\"firstName\":\"Asha\","
                    + "\"middleName\":\"Kumari\",\"lastName\":\"Devi\",\"dob\":\"15-08-1990\"}}";
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should link the ABHA to the registration id it was given")
        void mapHealthID_shouldLinkByRegistrationId() throws Exception {
            when(benHealthIDMappingRepo.existsByHealthIdNumber("11-1111-1111-1111")).thenReturn(false);
            when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenAnswer(i -> i.getArgument(0));
            when(healthIDRepo.existsByHealthIdNumber("11-1111-1111-1111")).thenReturn(false);

            String result = service.mapHealthIDToBeneficiary(mappingRequest("\"beneficiaryRegID\":4321"));

            assertTrue(result.contains("11-1111-1111-1111"), result);
            verify(benHealthIDMappingRepo).save(any(BenHealthIDMapping.class));
            verify(benHealthIDMappingRepo, never()).getBenRegID(anyLong());
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should resolve the registration id from the beneficiary id")
        void mapHealthID_shouldResolveRegistrationIdFromBeneficiaryId() throws Exception {
            when(benHealthIDMappingRepo.existsByHealthIdNumber(anyString())).thenReturn(false);
            when(benHealthIDMappingRepo.getBenRegID(9999L)).thenReturn(4321L);
            when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenAnswer(i -> i.getArgument(0));

            service.mapHealthIDToBeneficiary(mappingRequest("\"beneficiaryID\":9999"));

            ArgumentCaptor<BenHealthIDMapping> captor = ArgumentCaptor.forClass(BenHealthIDMapping.class);
            verify(benHealthIDMappingRepo).save(captor.capture());
            assertEquals(4321L, captor.getValue().getBeneficiaryRegID());
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should save the full ABHA profile the first time it is seen")
        void mapHealthID_shouldSaveAbhaProfileOnce() throws Exception {
            when(benHealthIDMappingRepo.existsByHealthIdNumber(anyString())).thenReturn(false);
            when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenAnswer(i -> i.getArgument(0));
            when(healthIDRepo.existsByHealthIdNumber("11-1111-1111-1111")).thenReturn(false);

            service.mapHealthIDToBeneficiary(mappingRequest("\"beneficiaryRegID\":4321"));

            ArgumentCaptor<HealthIDResponse> captor = ArgumentCaptor.forClass(HealthIDResponse.class);
            verify(healthIDRepo).save(captor.capture());
            HealthIDResponse saved = captor.getValue();
            assertEquals("abc@sbx, def@sbx", saved.getHealthId());
            assertEquals("Asha Kumari Devi", saved.getName());
            assertEquals("1990", saved.getYearOfBirth());
            assertEquals("08", saved.getMonthOfBirth());
            assertEquals("15", saved.getDayOfBirth());
            assertEquals("admin", saved.getCreatedBy());
            assertEquals(7, saved.getProviderServiceMapID());
            assertTrue(saved.getIsNewAbha());
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should not save the ABHA profile again once it is on file")
        void mapHealthID_shouldNotResaveKnownProfile() throws Exception {
            when(benHealthIDMappingRepo.existsByHealthIdNumber(anyString())).thenReturn(false);
            when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenAnswer(i -> i.getArgument(0));
            when(healthIDRepo.existsByHealthIdNumber("11-1111-1111-1111")).thenReturn(true);

            service.mapHealthIDToBeneficiary(mappingRequest("\"beneficiaryRegID\":4321"));

            verify(healthIDRepo, never()).save(any(HealthIDResponse.class));
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should refuse to link an ABHA already held by someone else")
        void mapHealthID_shouldRefuseAlreadyLinkedAbha() throws Exception {
            when(benHealthIDMappingRepo.existsByHealthIdNumber("11-1111-1111-1111")).thenReturn(true);

            assertEquals("HealthId is already linked to another beneficiary ID",
                    service.mapHealthIDToBeneficiary(mappingRequest("\"beneficiaryRegID\":4321")));
            verify(benHealthIDMappingRepo, never()).save(any(BenHealthIDMapping.class));
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should insist on a beneficiary identifier")
        void mapHealthID_shouldInsistOnIdentifier() {
            assertEquals("BeneficiaryRegID or BeneficiaryID must be provided",
                    assertThrows(FHIRException.class,
                            () -> service.mapHealthIDToBeneficiary("{\"healthIdNumber\":\"11-1111\"}"))
                            .getMessage());
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should report an unusable request as an unexpected error")
        void mapHealthID_shouldReportUnusableRequest() {
            assertTrue(assertThrows(FHIRException.class,
                    () -> service.mapHealthIDToBeneficiary("not-json")).getMessage()
                    .startsWith("Unexpected error: "));
        }

        @Test
        @DisplayName("mapHealthIDToBeneficiary should carry on when the Elasticsearch sync fails")
        void mapHealthID_shouldCarryOnAfterEsFailure() throws Exception {
            when(benHealthIDMappingRepo.existsByHealthIdNumber(anyString())).thenReturn(false);
            when(benHealthIDMappingRepo.save(any(BenHealthIDMapping.class))).thenAnswer(i -> i.getArgument(0));
            when(healthIDRepo.existsByHealthIdNumber(anyString())).thenReturn(true);
            org.mockito.Mockito.doThrow(new IllegalStateException("ES down"))
                    .when(abhaEsSyncService).updateAbhaInElasticsearch(any(), any(), any(), any());

            assertTrue(service.mapHealthIDToBeneficiary(mappingRequest("\"beneficiaryRegID\":4321"))
                    .contains("11-1111-1111-1111"));
        }

        private BenHealthIDMapping mapping(String healthIdNumber) {
            BenHealthIDMapping mapping = new BenHealthIDMapping();
            mapping.setHealthIdNumber(healthIdNumber);
            return mapping;
        }

        @Test
        @DisplayName("getBenHealthID should enrich each linked ABHA with its profile and new-ABHA flag")
        void getBenHealthID_shouldEnrichEachMapping() {
            when(benHealthIDMappingRepo.getHealthDetails(4321L))
                    .thenReturn(new ArrayList<>(List.of(mapping("11-1111-1111-1111"))));
            when(benHealthIDMappingRepo.getIsNewAbhaBatch(List.of("11-1111-1111-1111")))
                    .thenReturn(List.<Object[]>of(new Object[] { "11-1111-1111-1111", Boolean.TRUE }));
            HealthIDResponse profile = new HealthIDResponse();
            profile.setName("Asha Devi");
            profile.setGender("Female");
            profile.setYearOfBirth("1990");
            profile.setHealthId("abc@sbx");
            when(healthIDRepo.getHealthIDDetailsUsingHealthNumber("11-1111-1111-1111"))
                    .thenReturn(new ArrayList<>(List.of(profile)));

            String result = service.getBenHealthID(4321L);

            assertTrue(result.contains("Asha Devi"), result);
            assertTrue(result.contains("abc@sbx"), result);
            assertTrue(result.contains("BenHealthDetails"), result);
        }

        @Test
        @DisplayName("getBenHealthID should answer with an empty list when nothing is linked")
        void getBenHealthID_shouldAnswerEmptyWithoutMappings() {
            when(benHealthIDMappingRepo.getHealthDetails(4321L)).thenReturn(new ArrayList<>());

            assertEquals("{\"BenHealthDetails\":[]}", service.getBenHealthID(4321L));
            verify(benHealthIDMappingRepo, never()).getIsNewAbhaBatch(any());
        }

        @Test
        @DisplayName("getBenHealthID should leave a mapping unenriched when no profile is on file")
        void getBenHealthID_shouldLeaveMappingUnenriched() {
            when(benHealthIDMappingRepo.getHealthDetails(4321L))
                    .thenReturn(new ArrayList<>(List.of(mapping("11-1111-1111-1111"))));
            when(benHealthIDMappingRepo.getIsNewAbhaBatch(any())).thenReturn(List.of());
            when(healthIDRepo.getHealthIDDetailsUsingHealthNumber(anyString())).thenReturn(new ArrayList<>());

            assertTrue(service.getBenHealthID(4321L).contains("BenHealthDetails"));
        }

        @Test
        @DisplayName("addRecordToHealthIdTable should save a profile the table has not seen before")
        void addRecordToHealthIdTable_shouldSaveNewProfile() throws Exception {
            when(healthIDRepo.getCountOfHealthIdNumber(any())).thenReturn(0);

            assertEquals("Data Saved Successfully",
                    service.addRecordToHealthIdTable(mappingRequest("\"beneficiaryRegID\":4321")));

            ArgumentCaptor<HealthIDResponse> captor = ArgumentCaptor.forClass(HealthIDResponse.class);
            verify(healthIDRepo).save(captor.capture());
            assertEquals("Asha Kumari Devi", captor.getValue().getName());
            assertEquals("abc@sbx, def@sbx", captor.getValue().getHealthId());
        }

        @Test
        @DisplayName("addRecordToHealthIdTable should leave an existing profile untouched")
        void addRecordToHealthIdTable_shouldLeaveExistingProfile() throws Exception {
            when(healthIDRepo.getCountOfHealthIdNumber(any())).thenReturn(1);

            assertEquals("Data already exists",
                    service.addRecordToHealthIdTable(mappingRequest("\"beneficiaryRegID\":4321")));
            verify(healthIDRepo, never()).save(any(HealthIDResponse.class));
        }

        @Test
        @DisplayName("addRecordToHealthIdTable should report a save failure plainly")
        void addRecordToHealthIdTable_shouldReportSaveFailure() {
            when(healthIDRepo.getCountOfHealthIdNumber(any())).thenThrow(new IllegalStateException("deadlock"));

            assertEquals("Error in saving data", assertThrows(FHIRException.class,
                    () -> service.addRecordToHealthIdTable(mappingRequest("\"beneficiaryRegID\":4321")))
                    .getMessage());
        }

        @Test
        @DisplayName("getMappedBenIdForHealthId should answer with the beneficiary ids behind the ABHA")
        void getMappedBenIdForHealthId_shouldAnswerWithBeneficiaryIds() {
            when(benHealthIDMappingRepo.getBenIdForHealthId("11-1111-1111-1111"))
                    .thenReturn(new String[] { "4321" });
            when(benHealthIDMappingRepo.getBeneficiaryIds(new String[] { "4321" }))
                    .thenReturn(new String[] { "9999" });

            assertEquals("[9999]", service.getMappedBenIdForHealthId("11-1111-1111-1111"));
        }

        @Test
        @DisplayName("getMappedBenIdForHealthId should say so plainly when the ABHA is linked to nobody")
        void getMappedBenIdForHealthId_shouldReportNoBeneficiary() {
            when(benHealthIDMappingRepo.getBenIdForHealthId(anyString())).thenReturn(new String[0]);

            assertEquals("No Beneficiary Found", service.getMappedBenIdForHealthId("11-1111-1111-1111"));
        }
    }
}
