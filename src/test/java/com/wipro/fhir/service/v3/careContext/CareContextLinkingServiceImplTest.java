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
package com.wipro.fhir.service.v3.careContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.LocalHttpStub;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.mongo.care_context.GenerateTokenAbdmResponses;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.repo.mongo.generateToken_response.GenerateTokenAbdmResponsesRepo;
import com.wipro.fhir.repo.v3.careContext.CareContextRepo;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.v3.abha.GenerateAuthSessionService;
import com.wipro.fhir.utils.exception.FHIRException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("CareContextLinkingServiceImpl Test Suite")
class CareContextLinkingServiceImplTest {

    private static final String TOKEN_PATH = "/abdm/hip/token/generate-token";
    private static final String LINK_PATH = "/abdm/hip/link/carecontext";
    private static final String ABHA_ADDRESS = "abc@sbx";

    @Mock
    private GenerateAuthSessionService generateAuthSessionService;

    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private GenerateTokenAbdmResponsesRepo generateTokenAbdmResponsesRepo;

    @Mock
    private CareContextRepo careContextRepo;

    @Mock
    private HealthIDRepo healthIDRepo;

    @InjectMocks
    private CareContextLinkingServiceImpl service;

    private LocalHttpStub stub;

    @BeforeEach
    @DisplayName("Point the ABDM HIP URLs at a loopback stub before each test")
    void setUp() throws Exception {
        stub = new LocalHttpStub();
        ReflectionTestUtils.setField(service, "generateTokenForLinkCareContext", stub.url(TOKEN_PATH));
        ReflectionTestUtils.setField(service, "linkCareContext", stub.url(LINK_PATH));
        ReflectionTestUtils.setField(service, "abhaMode", "sbx");
        ReflectionTestUtils.setField(service, "abdmFacilityId", "IN-DEFAULT-1");

        when(generateAuthSessionService.getAbhaAuthToken()).thenReturn("Bearer abdm-token");
        when(common_NDHMService.getBody(any(ResponseEntity.class)))
                .thenAnswer(invocation -> ((ResponseEntity<String>) invocation.getArgument(0)).getBody());
    }

    @AfterEach
    void tearDown() {
        stub.close();
    }

    private JsonObject sentTo(String path) {
        return JsonParser.parseString(stub.request(path).body()).getAsJsonObject();
    }

    private JsonObject answer(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }

    private GenerateTokenAbdmResponses mongoRow(String response, Date createdDate) {
        GenerateTokenAbdmResponses row = new GenerateTokenAbdmResponses();
        row.setAbhaAddress(ABHA_ADDRESS);
        row.setResponse(response);
        row.setCreatedDate(createdDate);
        return row;
    }

    private Date monthsAgo(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -months);
        return calendar.getTime();
    }

    @Nested
    @DisplayName("generateTokenForCareContext")
    class GenerateTokenTests {

        @Test
        @DisplayName("should reuse a link token that is still inside the three-month window")
        void generateToken_shouldReuseFreshStoredToken() throws Exception {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS))
                    .thenReturn(mongoRow("{\"LinkToken\":\"stored-token\"}", monthsAgo(1)));

            JsonObject result = answer(service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\"}"));

            assertEquals("stored-token", result.get("linkToken").getAsString());
            assertTrue(stub.requests(TOKEN_PATH).isEmpty(), "a reusable token must not trigger an ABDM call");
        }

        @Test
        @DisplayName("should ask ABDM for a token and answer with the one the callback stored")
        void generateToken_shouldRequestTokenFromAbdm() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"LinkToken\":\"fresh-token\"}", new Date()));

            JsonObject result = answer(service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\",\"abhaNumber\":\"11-1111-1111-1111\","
                            + "\"name\":\"Asha\",\"gender\":\"female\",\"yearOfBirth\":1990}"));

            assertEquals("fresh-token", result.get("X-LINK-TOKEN").getAsString());
            assertTrue(result.has("requestId"));
            JsonObject sent = sentTo(TOKEN_PATH);
            assertEquals("11111111111111", sent.get("abhaNumber").getAsString(),
                    "ABDM wants the ABHA number without its separators");
            assertEquals(ABHA_ADDRESS, sent.get("abhaAddress").getAsString());
            assertEquals("Asha", sent.get("name").getAsString());
            assertEquals(1990, sent.get("yearOfBirth").getAsInt());
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @CsvSource({ "female, F", "F, F", "male, M", "M, M", "transgender, O", "'', O" })
        @DisplayName("should normalise the gender to the single letter ABDM expects")
        void generateToken_shouldNormaliseGender(String gender, String expected) throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"LinkToken\":\"fresh-token\"}", new Date()));

            service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\",\"gender\":\"" + gender + "\"}");

            assertEquals(expected, sentTo(TOKEN_PATH).get("gender").getAsString());
        }

        @Test
        @DisplayName("should fall back to the stored year of birth when the request carries an implausible one")
        void generateToken_shouldFallBackToStoredYearOfBirth() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"LinkToken\":\"fresh-token\"}", new Date()));
            HealthIDResponse stored = new HealthIDResponse();
            stored.setYearOfBirth("1985");
            when(healthIDRepo.getHealthIDDetails(ABHA_ADDRESS)).thenReturn(new ArrayList<>(List.of(stored)));

            service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\",\"yearOfBirth\":1800}");

            assertEquals(1985, sentTo(TOKEN_PATH).get("yearOfBirth").getAsInt());
        }

        @Test
        @DisplayName("should send no year of birth when neither the request nor the record has a usable one")
        void generateToken_shouldOmitUnresolvableYearOfBirth() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"LinkToken\":\"fresh-token\"}", new Date()));
            when(healthIDRepo.getHealthIDDetails(ABHA_ADDRESS)).thenReturn(new ArrayList<>());

            service.generateTokenForCareContext("{\"abhaAddress\":\"" + ABHA_ADDRESS + "\"}");

            assertFalse(sentTo(TOKEN_PATH).has("yearOfBirth"));
        }

        @Test
        @DisplayName("should route the call to the facility the request names, not the configured default")
        void generateToken_shouldPreferRequestFacility() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"LinkToken\":\"fresh-token\"}", new Date()));

            service.generateTokenForCareContext("{\"abhaAddress\":\"" + ABHA_ADDRESS
                    + "\",\"abdmFacilityId\":\"IN0710000001\"}");

            assertEquals("IN0710000001", stub.request(TOKEN_PATH).header("X-Hip-Id"));
        }

        @Test
        @DisplayName("should fall back to the configured facility when the request names none")
        void generateToken_shouldFallBackToConfiguredFacility() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"LinkToken\":\"fresh-token\"}", new Date()));

            service.generateTokenForCareContext("{\"abhaAddress\":\"" + ABHA_ADDRESS + "\"}");

            assertEquals("IN-DEFAULT-1", stub.request(TOKEN_PATH).header("X-Hip-Id"));
        }

        @Test
        @DisplayName("should report the ABDM error the callback stored instead of a link token")
        void generateToken_shouldReportStoredError() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"Error\":{\"message\":\"abha not found\"}}", new Date()));

            JsonObject result = answer(service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\"}"));

            assertTrue(result.get("error").getAsString().contains("abha not found"));
        }

        @Test
        @DisplayName("should report an unknown error when the callback stored neither a token nor an error")
        void generateToken_shouldReportUnknownError() throws Exception {
            stub.stub(TOKEN_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken(anyString()))
                    .thenReturn(mongoRow("{\"somethingElse\":true}", new Date()));

            assertEquals("Unknown error", answer(service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\"}")).get("error").getAsString());
        }

        @Test
        @DisplayName("should surface the ABDM error body on any status other than the 202 it accepts")
        void generateToken_shouldSurfaceAbdmRejection() {
            stub.stubOk(TOKEN_PATH, "{\"error\":\"bad request\"}");

            assertThrows(FHIRException.class, () -> service.generateTokenForCareContext(
                    "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\"}"));
        }
    }

    @Nested
    @DisplayName("linkCareContext")
    class LinkCareContextTests {

        private String linkRequest(String extra) {
            return "{\"abhaAddress\":\"" + ABHA_ADDRESS + "\",\"abhaNumber\":\"11-1111-1111-1111\","
                    + "\"visitCode\":\"987654\",\"visitCategory\":\"General OPD\"" + extra + "}";
        }

        @Test
        @DisplayName("should link the care context with the link token the caller supplied")
        void linkCareContext_shouldLinkWithSuppliedToken() throws Exception {
            stub.stub(LINK_PATH, LocalHttpStub.Response.of(202, ""));

            JsonObject result = answer(service.linkCareContext(linkRequest(",\"linkToken\":\"caller-token\"")));

            assertEquals("Care Context added successfully", result.get("message").getAsString());
            assertEquals("caller-token", stub.request(LINK_PATH).header("X-Link-Token"));
            JsonObject sent = sentTo(LINK_PATH);
            assertEquals("11111111111111", sent.get("abhaNumber").getAsString());
            assertEquals(ABHA_ADDRESS, sent.get("abhaAddress").getAsString());
        }

        @Test
        @DisplayName("should fetch the link token from the stored callback when the caller supplies none")
        void linkCareContext_shouldFetchStoredToken() throws Exception {
            stub.stub(LINK_PATH, LocalHttpStub.Response.of(202, ""));
            when(common_NDHMService.getLinkToken("req-1"))
                    .thenReturn(mongoRow("{\"LinkToken\":\"stored-token\"}", new Date()));

            service.linkCareContext(linkRequest(",\"requestId\":\"req-1\""));

            assertEquals("stored-token", stub.request(LINK_PATH).header("X-Link-Token"));
        }

        @Test
        @DisplayName("should build one care-context entry per HI type the visit actually has data for")
        void linkCareContext_shouldBuildOneEntryPerHiType() throws Exception {
            stub.stub(LINK_PATH, LocalHttpStub.Response.of(202, ""));
            when(careContextRepo.hasPhyVitals("987654")).thenReturn(1);
            when(careContextRepo.hasPrescribedDrugs("987654")).thenReturn(1);
            when(careContextRepo.hasLabtestsDone("987654")).thenReturn(1);
            when(careContextRepo.hasVaccineDetails("987654")).thenReturn(1);

            service.linkCareContext(linkRequest(",\"linkToken\":\"caller-token\""));

            assertEquals(6, sentTo(LINK_PATH).getAsJsonArray("patient").size(),
                    "OPConsultation, DischargeSummary and the four data-driven HI types");
        }

        @Test
        @DisplayName("should link only the always-present HI types when the visit carries no clinical data")
        void linkCareContext_shouldLinkOnlyBaselineHiTypes() throws Exception {
            stub.stub(LINK_PATH, LocalHttpStub.Response.of(202, ""));

            service.linkCareContext(linkRequest(",\"linkToken\":\"caller-token\""));

            assertEquals(2, sentTo(LINK_PATH).getAsJsonArray("patient").size());
        }

        @Test
        @DisplayName("should report the ABDM error message when ABDM rejects the link")
        void linkCareContext_shouldReportAbdmError() throws Exception {
            stub.stubOk(LINK_PATH, "{\"error\":{\"message\":\"link token expired\"}}");

            assertEquals("link token expired", answer(service.linkCareContext(
                    linkRequest(",\"linkToken\":\"caller-token\""))).get("error").getAsString());
        }

        @Test
        @DisplayName("should report an unknown error when ABDM's rejection carries no error object")
        void linkCareContext_shouldReportUnknownAbdmError() throws Exception {
            stub.stubOk(LINK_PATH, "{\"status\":\"rejected\"}");

            assertEquals("Unknown error", answer(service.linkCareContext(
                    linkRequest(",\"linkToken\":\"caller-token\""))).get("error").getAsString());
        }

        @Test
        @DisplayName("should report the stored ABDM error instead of attempting the link")
        void linkCareContext_shouldReportStoredError() throws Exception {
            when(common_NDHMService.getLinkToken("req-1"))
                    .thenReturn(mongoRow("{\"Error\":{\"message\":\"abha not found\"}}", new Date()));

            JsonObject result = answer(service.linkCareContext(linkRequest(",\"requestId\":\"req-1\"")));

            assertTrue(result.get("error").getAsString().contains("abha not found"));
            assertTrue(stub.requests(LINK_PATH).isEmpty(), "without a link token there is nothing to send");
        }

        @Test
        @DisplayName("should answer with an empty map when there is no link token to be had at all")
        void linkCareContext_shouldAnswerEmptyWithoutToken() throws Exception {
            when(common_NDHMService.getLinkToken("req-1")).thenReturn(null);

            assertEquals("{}", service.linkCareContext(linkRequest(",\"requestId\":\"req-1\"")));
        }

        @Test
        @DisplayName("should unwrap the ABDM error message out of a transport failure body")
        void linkCareContext_shouldUnwrapErrorFromTransportFailure() {
            stub.stub(LINK_PATH, LocalHttpStub.Response.of(500,
                    "{\"error\":{\"message\":\"ABDM gateway unavailable\"}}"));

            assertEquals("ABDM gateway unavailable", assertThrows(FHIRException.class, () ->
                    service.linkCareContext(linkRequest(",\"linkToken\":\"caller-token\""))).getMessage());
        }
    }

    @Nested
    @DisplayName("checkRecordExisits")
    class CheckRecordExistsTests {

        @Test
        @DisplayName("should answer with the stored link token when the record is fresh")
        void checkRecordExisits_shouldReturnFreshToken() {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS))
                    .thenReturn(mongoRow("{\"LinkToken\":\"stored-token\"}", monthsAgo(1)));

            assertEquals("stored-token", service.checkRecordExisits(ABHA_ADDRESS));
        }

        @Test
        @DisplayName("should answer with the raw stored response once the record is older than three months")
        void checkRecordExisits_shouldNotReuseStaleToken() {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS))
                    .thenReturn(mongoRow("{\"LinkToken\":\"stored-token\"}", monthsAgo(6)));

            assertEquals("{\"LinkToken\":\"stored-token\"}", service.checkRecordExisits(ABHA_ADDRESS));
        }

        @Test
        @DisplayName("should answer with the raw stored response when it carries no link token")
        void checkRecordExisits_shouldReturnResponseWithoutToken() {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS))
                    .thenReturn(mongoRow("{\"Error\":{\"message\":\"abha not found\"}}", monthsAgo(1)));

            assertEquals("{\"Error\":{\"message\":\"abha not found\"}}", service.checkRecordExisits(ABHA_ADDRESS));
        }

        @Test
        @DisplayName("should answer with nothing when the stored response cannot be parsed")
        void checkRecordExisits_shouldAnswerNullForUnparseableResponse() {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS))
                    .thenReturn(mongoRow("not-json", monthsAgo(1)));

            assertNull(service.checkRecordExisits(ABHA_ADDRESS));
        }

        @Test
        @DisplayName("should answer with nothing when no record exists for the ABHA address")
        void checkRecordExisits_shouldAnswerNullWithoutRecord() {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS)).thenReturn(null);

            assertNull(service.checkRecordExisits(ABHA_ADDRESS));
        }

        @Test
        @DisplayName("should answer with nothing when the record has no creation date to age")
        void checkRecordExisits_shouldAnswerNullWithoutCreatedDate() {
            when(generateTokenAbdmResponsesRepo.findByAbhaAddress(ABHA_ADDRESS))
                    .thenReturn(mongoRow("{\"LinkToken\":\"stored-token\"}", null));

            assertNull(service.checkRecordExisits(ABHA_ADDRESS));
        }
    }

    @Nested
    @DisplayName("findHiTypes")
    class FindHiTypesTests {

        @ParameterizedTest(name = "{0}")
        @CsvSource({ "General OPD", "General OPD (QC)" })
        @DisplayName("should add an OP consultation for the OPD visit categories")
        void findHiTypes_shouldAddOpConsultationForOpd(String visitCategory) {
            String[] hiTypes = service.findHiTypes("987654", visitCategory);

            assertEquals(List.of("OPConsultation", "DischargeSummary"), List.of(hiTypes));
        }

        @Test
        @DisplayName("should always add a discharge summary, whatever the visit category")
        void findHiTypes_shouldAlwaysAddDischargeSummary() {
            assertEquals(List.of("DischargeSummary"), List.of(service.findHiTypes("987654", "IPD")));
        }

        @Test
        @DisplayName("should add a wellness record only when the visit recorded vitals")
        void findHiTypes_shouldAddWellnessRecordForVitals() {
            when(careContextRepo.hasPhyVitals("987654")).thenReturn(2);

            assertTrue(List.of(service.findHiTypes("987654", "IPD")).contains("WellnessRecord"));
        }

        @Test
        @DisplayName("should add a prescription only when the visit prescribed drugs")
        void findHiTypes_shouldAddPrescriptionForDrugs() {
            when(careContextRepo.hasPrescribedDrugs("987654")).thenReturn(3);

            assertTrue(List.of(service.findHiTypes("987654", "IPD")).contains("Prescription"));
        }

        @Test
        @DisplayName("should add a diagnostic report only when the visit ran lab tests")
        void findHiTypes_shouldAddDiagnosticReportForLabTests() {
            when(careContextRepo.hasLabtestsDone("987654")).thenReturn(1);

            assertTrue(List.of(service.findHiTypes("987654", "IPD")).contains("DiagnosticReport"));
        }

        @Test
        @DisplayName("should add an immunization record only when the visit gave a vaccine")
        void findHiTypes_shouldAddImmunizationRecordForVaccines() {
            when(careContextRepo.hasVaccineDetails("987654")).thenReturn(1);

            assertTrue(List.of(service.findHiTypes("987654", "IPD")).contains("ImmunizationRecord"));
        }
    }
}
