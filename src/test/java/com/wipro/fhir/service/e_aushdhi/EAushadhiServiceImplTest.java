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
package com.wipro.fhir.service.e_aushdhi;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.e_aushdhi.E_AusdhFacilityProcessLog;
import com.wipro.fhir.data.e_aushdhi.EAushadhiResponse;
import com.wipro.fhir.data.e_aushdhi.ItemMaster;
import com.wipro.fhir.data.e_aushdhi.ItemStockEntry;
import com.wipro.fhir.data.e_aushdhi.M_Facility;
import com.wipro.fhir.data.e_aushdhi.M_ItemCategory;
import com.wipro.fhir.data.e_aushdhi.M_ItemForm;
import com.wipro.fhir.data.e_aushdhi.M_Route;
import com.wipro.fhir.data.e_aushdhi.M_itemfacilitymapping;
import com.wipro.fhir.data.e_aushdhi.SyncDispenseDetailsRequest;
import com.wipro.fhir.data.e_aushdhi.T_PatientIssue;
import com.wipro.fhir.repo.e_aushdhi.E_AusdhFacilityProcessLogRepo;
import com.wipro.fhir.repo.e_aushdhi.FacilityRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemCategoryRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemFormRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemStockEntryRepo;
import com.wipro.fhir.repo.e_aushdhi.M_itemfacilitymappingRepo;
import com.wipro.fhir.repo.e_aushdhi.ParkingPlaceRepo;
import com.wipro.fhir.repo.e_aushdhi.PatientIssueRepo;
import com.wipro.fhir.repo.e_aushdhi.RouteRepo;
import com.wipro.fhir.repo.e_aushdhi.VanMasterRepo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The e-aushadhi bridge: it pulls stock from the state drug-inventory system into AMRIT's
 * item master, acknowledges what it took, and pushes AMRIT's dispense records back.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("EAushadhiServiceImpl Test Suite")
class EAushadhiServiceImplTest {

    private static final String STOCK_URL = "http://eaushadhi.example.org/stock/";
    private static final String ACK_URL = "http://eaushadhi.example.org/ack/";
    private static final int AMRIT_FACILITY_ID = 7;
    private static final int MAIN_FACILITY_ID = 1;
    private static final int EAUSHADHI_FACILITY_ID = 99927641;

    @Mock
    private HttpUtils httpUtils;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private M_itemfacilitymappingRepo mItemFacilityMappingRepo;

    @Mock
    private FacilityRepo facilityRepo;

    @Mock
    private ItemStockEntryRepo itemStockEntryRepo;

    @Mock
    private VanMasterRepo vanMasterRepo;

    @Mock
    private ParkingPlaceRepo parkingPlaceRepo;

    @Mock
    private ItemFormRepo itemFormRepo;

    @Mock
    private ItemCategoryRepo itemCategoryRepo;

    @Mock
    private RouteRepo routeRepo;

    @Mock
    private PatientIssueRepo patientIssueRepo;

    @Mock
    private E_AusdhFacilityProcessLogRepo e_AusdhFacilityProcessLogRepo;

    @Mock
    private APIChannel aPIChannel;

    @InjectMocks
    private EAushadhiServiceImpl service;

    @BeforeEach
    @DisplayName("Configure the e-aushadhi endpoints before each test")
    void setUp() {
        ReflectionTestUtils.setField(service, "eaushadhiStoreStockDetailsUrl", STOCK_URL);
        ReflectionTestUtils.setField(service, "eaushadhiStoreStockAckUrl", ACK_URL);
        ReflectionTestUtils.setField(service, "eAushadhiDispensePageSize", "10");
        ReflectionTestUtils.setField(service, "eAushadhiDummy", "false");
    }

    private M_Facility mappedFacility() {
        M_Facility facility = new M_Facility();
        facility.setFacilityID(AMRIT_FACILITY_ID);
        facility.setMainFacilityID(MAIN_FACILITY_ID);
        facility.seteAushadhiFacilityId(EAUSHADHI_FACILITY_ID);
        facility.setProviderServiceMapID(3);
        facility.setCreatedBy("cron_job");
        return facility;
    }

    private EAushadhiResponse stockRow(String stockStatus, String category) {
        EAushadhiResponse row = new EAushadhiResponse();
        row.setStockstatus(stockStatus);
        row.setCategory(category);
        row.setBrandid("BRAND-1");
        row.setDrugname("Paracetamol");
        row.setInhandqty("50");
        row.setBatchno("BATCH-1");
        row.setExpdate("01-Jan-2030");
        row.setMfgdate("01-Jan-2024");
        row.setItemtypename("Tablet");
        row.setSpecification("500 mg");
        row.setEdl("EDL");
        return row;
    }

    private void stubStockLookups() {
        M_ItemForm itemForm = new M_ItemForm();
        itemForm.setItemFormID(11);
        when(itemFormRepo.getItemFormID("Tablet")).thenReturn(itemForm);
        M_Route route = new M_Route();
        route.setRouteID(5);
        when(routeRepo.getItemRouteID("Oral")).thenReturn(route);
        M_ItemCategory category = new M_ItemCategory();
        category.setItemCategoryID(2);
        when(itemCategoryRepo.getItemCategoryID(anyString(), anyInt())).thenReturn(category);
        when(vanMasterRepo.getvanID(AMRIT_FACILITY_ID)).thenReturn(21);
        when(parkingPlaceRepo.getParkingPlaceID(AMRIT_FACILITY_ID)).thenReturn(31);
    }

    private ItemMaster savedItem() {
        ItemMaster item = new ItemMaster();
        item.setItemID(101);
        item.setProviderServiceMapID(3);
        item.setCreatedBy("cron_job");
        item.setIsEaushadi(true);
        return item;
    }

    private void stubStockEntrySaves() {
        ItemStockEntry entry = new ItemStockEntry();
        entry.setItemStockEntryID(501L);
        when(itemStockEntryRepo.save(any(ItemStockEntry.class))).thenReturn(entry);
        when(itemStockEntryRepo.updateVanSerialNo(501L)).thenReturn(1);
        when(mItemFacilityMappingRepo.save(any(M_itemfacilitymapping.class)))
                .thenReturn(new M_itemfacilitymapping());
    }

    private void stubStockFeed(String body, HttpStatus status) {
        when(httpUtils.getWithResponseEntity(anyString(), any(HttpHeaders.class)))
                .thenReturn(new ResponseEntity<>(body, status));
    }

    private void stubAck(String result) {
        when(httpUtils.getWithResponseEntity(org.mockito.ArgumentMatchers.startsWith(ACK_URL),
                any(HttpHeaders.class)))
                .thenReturn(new ResponseEntity<>("[{\"trans_RESULT\":\"" + result + "\"}]", HttpStatus.OK));
    }

    @Nested
    @DisplayName("getStoreStockDetailsService")
    class GetStoreStockDetailsTests {

        @Test
        @DisplayName("should add a new e-aushadhi item to the item master and stock it at both facilities")
        void getStoreStockDetails_shouldAddNewItem() throws Exception {
            stubStockFeed("[" + new com.google.gson.Gson().toJson(stockRow("10", "10")) + "]", HttpStatus.OK);
            stubStockLookups();
            when(itemRepo.checkItemExists("Paracetamol", 3, "BRAND-1_EAushadhi")).thenReturn(null);
            when(itemRepo.save(any(ItemMaster.class))).thenReturn(savedItem());
            stubStockEntrySaves();

            assertEquals("success", service.getStoreStockDetailsService(mappedFacility()));

            verify(itemRepo).save(any(ItemMaster.class));
            verify(mItemFacilityMappingRepo, times(2)).save(any(M_itemfacilitymapping.class));
            verify(itemStockEntryRepo).save(any(ItemStockEntry.class));
        }

        @Test
        @DisplayName("should stock an item that already exists and is already mapped to both facilities")
        void getStoreStockDetails_shouldStockKnownMappedItem() throws Exception {
            stubStockFeed("[" + new com.google.gson.Gson().toJson(stockRow("10", "11")) + "]", HttpStatus.OK);
            stubStockLookups();
            when(itemRepo.checkItemExists(anyString(), anyInt(), anyString())).thenReturn(savedItem());
            when(mItemFacilityMappingRepo.checkItemFacilityMapping(eq(101), anyInt(), eq(3)))
                    .thenReturn(new M_itemfacilitymapping());
            stubStockEntrySaves();

            assertEquals("success", service.getStoreStockDetailsService(mappedFacility()));

            verify(itemRepo, never()).save(any(ItemMaster.class));
            verify(itemStockEntryRepo).save(any(ItemStockEntry.class));
        }

        @Test
        @DisplayName("should map a known item to the sub-facility when only the main mapping exists")
        void getStoreStockDetails_shouldMapKnownItemToSubFacility() throws Exception {
            stubStockFeed("[" + new com.google.gson.Gson().toJson(stockRow("10", "12")) + "]", HttpStatus.OK);
            stubStockLookups();
            when(itemRepo.checkItemExists(anyString(), anyInt(), anyString())).thenReturn(savedItem());
            when(mItemFacilityMappingRepo.checkItemFacilityMapping(101, MAIN_FACILITY_ID, 3))
                    .thenReturn(new M_itemfacilitymapping());
            when(mItemFacilityMappingRepo.checkItemFacilityMapping(101, AMRIT_FACILITY_ID, 3)).thenReturn(null);
            stubStockEntrySaves();

            assertEquals("success", service.getStoreStockDetailsService(mappedFacility()));

            verify(mItemFacilityMappingRepo).save(any(M_itemfacilitymapping.class));
        }

        @Test
        @DisplayName("should skip a stock row e-aushadhi has not approved")
        void getStoreStockDetails_shouldSkipUnapprovedStock() throws Exception {
            stubStockFeed("[" + new com.google.gson.Gson().toJson(stockRow("20", "10")) + "]", HttpStatus.OK);
            when(vanMasterRepo.getvanID(anyInt())).thenReturn(21);
            when(parkingPlaceRepo.getParkingPlaceID(anyInt())).thenReturn(31);

            assertEquals("success", service.getStoreStockDetailsService(mappedFacility()),
                    "one rejected row is logged and skipped, the pass itself still succeeds");
            verify(itemRepo, never()).save(any(ItemMaster.class));
        }

        @Test
        @DisplayName("should fail when e-aushadhi has no new stock for the facility")
        void getStoreStockDetails_shouldFailWithoutNewStock() {
            stubStockFeed("[]", HttpStatus.OK);

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.getStoreStockDetailsService(mappedFacility())).getMessage()
                    .contains("No new stock from E-Aushadhi"));
        }

        @Test
        @DisplayName("should fail when e-aushadhi does not answer 200")
        void getStoreStockDetails_shouldFailOnNonOkStatus() {
            stubStockFeed("[]", HttpStatus.BAD_GATEWAY);

            assertTrue(assertThrows(FHIRException.class,
                    () -> service.getStoreStockDetailsService(mappedFacility())).getMessage()
                    .contains("Error while accessing store stock details"));
        }
    }

    @Nested
    @DisplayName("getEaushadhiStoreDetailsByFacilityID")
    class ByFacilityIdTests

    {
        private String request() {
            return "{\"facilityID\":" + AMRIT_FACILITY_ID + "}";
        }

        private void stubSuccessfulStockPass() throws Exception {
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(mappedFacility());
            stubStockFeed("[" + new com.google.gson.Gson().toJson(stockRow("10", "10")) + "]", HttpStatus.OK);
            stubStockLookups();
            when(itemRepo.checkItemExists(anyString(), anyInt(), anyString())).thenReturn(null);
            when(itemRepo.save(any(ItemMaster.class))).thenReturn(savedItem());
            stubStockEntrySaves();
        }

        @Test
        @DisplayName("should pull the stock, acknowledge it and record the success in the process log")
        void byFacilityId_shouldPullStockAndAcknowledge() throws Exception {
            stubSuccessfulStockPass();
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(
                    AMRIT_FACILITY_ID, EAUSHADHI_FACILITY_ID)).thenReturn(new ArrayList<>());
            stubAck("1");

            assertEquals("success", service.getEaushadhiStoreDetailsByFacilityID(request()));

            ArgumentCaptor<E_AusdhFacilityProcessLog> captor =
                    ArgumentCaptor.forClass(E_AusdhFacilityProcessLog.class);
            verify(e_AusdhFacilityProcessLogRepo, atLeastOnce()).save(captor.capture());
            E_AusdhFacilityProcessLog logged = captor.getValue();
            assertEquals("manual_job", logged.getCreatedBy());
            assertTrue(logged.getStockUpdateAmrit());
            assertTrue(logged.getAcknowledge());
            assertNotNull(logged.getLastSuccessDate());
        }

        @Test
        @DisplayName("should reuse the newest process-log row when the facility has more than one")
        void byFacilityId_shouldReuseNewestLogRow() throws Exception {
            stubSuccessfulStockPass();
            E_AusdhFacilityProcessLog older = new E_AusdhFacilityProcessLog();
            older.seteLID(1);
            E_AusdhFacilityProcessLog newest = new E_AusdhFacilityProcessLog();
            newest.seteLID(2);
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(anyInt(), anyInt()))
                    .thenReturn(new ArrayList<>(List.of(older, newest)));
            stubAck("1");

            service.getEaushadhiStoreDetailsByFacilityID(request());

            ArgumentCaptor<E_AusdhFacilityProcessLog> captor =
                    ArgumentCaptor.forClass(E_AusdhFacilityProcessLog.class);
            verify(e_AusdhFacilityProcessLogRepo, atLeastOnce()).save(captor.capture());
            assertEquals(2, captor.getValue().geteLID());
        }

        @Test
        @DisplayName("should only re-acknowledge a pass whose stock already landed in AMRIT")
        void byFacilityId_shouldOnlyReAcknowledge() throws Exception {
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(mappedFacility());
            E_AusdhFacilityProcessLog pending = new E_AusdhFacilityProcessLog();
            pending.setStockUpdateAmrit(true);
            pending.setAcknowledge(false);
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(anyInt(), anyInt()))
                    .thenReturn(new ArrayList<>(List.of(pending)));
            stubAck("1");

            assertEquals("success", service.getEaushadhiStoreDetailsByFacilityID(request()));

            verify(itemRepo, never()).save(any(ItemMaster.class));
        }

        @Test
        @DisplayName("should record the failure when the re-acknowledgement is refused")
        void byFacilityId_shouldRecordRefusedAcknowledgement() throws Exception {
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(mappedFacility());
            E_AusdhFacilityProcessLog pending = new E_AusdhFacilityProcessLog();
            pending.setStockUpdateAmrit(true);
            pending.setAcknowledge(false);
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(anyInt(), anyInt()))
                    .thenReturn(new ArrayList<>(List.of(pending)));
            stubAck("2");

            assertEquals("failure", service.getEaushadhiStoreDetailsByFacilityID(request()));

            ArgumentCaptor<E_AusdhFacilityProcessLog> captor =
                    ArgumentCaptor.forClass(E_AusdhFacilityProcessLog.class);
            verify(e_AusdhFacilityProcessLogRepo).save(captor.capture());
            assertNotNull(captor.getValue().getLastFailureDate());
        }

        @Test
        @DisplayName("should refuse a facility that is not mapped to e-aushadhi")
        void byFacilityId_shouldRefuseUnmappedFacility() {
            M_Facility unmapped = new M_Facility();
            unmapped.setFacilityID(AMRIT_FACILITY_ID);
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(unmapped);

            assertEquals("Facility is not mapped with E-aushadhi", assertThrows(FHIRException.class,
                    () -> service.getEaushadhiStoreDetailsByFacilityID(request())).getMessage());
        }

        @Test
        @DisplayName("should refuse a facility AMRIT does not know")
        void byFacilityId_shouldRefuseUnknownFacility() {
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(null);

            assertEquals("Error while getting facility details", assertThrows(FHIRException.class,
                    () -> service.getEaushadhiStoreDetailsByFacilityID(request())).getMessage());
        }
    }

    @Nested
    @DisplayName("getStockDetailsFromEAushadhi")
    class ScheduledPassTests {

        @Test
        @DisplayName("should walk every mapped facility and record each pass as a cron job")
        void scheduledPass_shouldWalkEveryMappedFacility() throws Exception {
            when(facilityRepo.getFacilityDetails()).thenReturn(new ArrayList<>(List.of(mappedFacility())));
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(anyInt(), anyInt()))
                    .thenReturn(new ArrayList<>());
            stubStockFeed("[" + new com.google.gson.Gson().toJson(stockRow("10", "10")) + "]", HttpStatus.OK);
            stubStockLookups();
            when(itemRepo.checkItemExists(anyString(), anyInt(), anyString())).thenReturn(null);
            when(itemRepo.save(any(ItemMaster.class))).thenReturn(savedItem());
            stubStockEntrySaves();
            stubAck("1");

            service.getStockDetailsFromEAushadhi();

            ArgumentCaptor<E_AusdhFacilityProcessLog> captor =
                    ArgumentCaptor.forClass(E_AusdhFacilityProcessLog.class);
            verify(e_AusdhFacilityProcessLogRepo, atLeastOnce()).save(captor.capture());
            assertEquals("cron_job", captor.getValue().getCreatedBy());
        }

        @Test
        @DisplayName("should only re-acknowledge a facility whose stock already landed in AMRIT")
        void scheduledPass_shouldOnlyReAcknowledge() {
            when(facilityRepo.getFacilityDetails()).thenReturn(new ArrayList<>(List.of(mappedFacility())));
            E_AusdhFacilityProcessLog pending = new E_AusdhFacilityProcessLog();
            pending.setStockUpdateAmrit(true);
            pending.setAcknowledge(false);
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityIdAndEaushadiFacilityId(anyInt(), anyInt()))
                    .thenReturn(new ArrayList<>(List.of(pending)));
            stubAck("1");

            service.getStockDetailsFromEAushadhi();

            verify(itemRepo, never()).save(any(ItemMaster.class));
        }

        @Test
        @DisplayName("should do nothing when no facility is mapped to e-aushadhi")
        void scheduledPass_shouldDoNothingWithoutMappedFacilities() {
            when(facilityRepo.getFacilityDetails()).thenReturn(new ArrayList<>());

            assertDoesNotThrow(() -> service.getStockDetailsFromEAushadhi());

            verify(e_AusdhFacilityProcessLogRepo, never()).save(any());
        }

        @Test
        @DisplayName("should swallow a failure so the scheduler keeps running")
        void scheduledPass_shouldSwallowFailure() {
            when(facilityRepo.getFacilityDetails()).thenThrow(new IllegalStateException("db down"));

            assertDoesNotThrow(() -> service.getStockDetailsFromEAushadhi());
        }
    }

    @Nested
    @DisplayName("sendStockAdditionAckToEAushadhi")
    class AcknowledgementTests {

        @Test
        @DisplayName("should read the acknowledgement verdict e-aushadhi returned")
        void ack_shouldReadVerdict() throws Exception {
            stubAck("1");
            assertEquals("1", service.sendStockAdditionAckToEAushadhi(1, EAUSHADHI_FACILITY_ID));

            stubAck("2");
            assertEquals("2", service.sendStockAdditionAckToEAushadhi(1, EAUSHADHI_FACILITY_ID));
        }

        @Test
        @DisplayName("should read an unrecognised verdict as a failure")
        void ack_shouldReadUnknownVerdictAsFailure() throws Exception {
            stubAck("9");

            assertEquals("0", service.sendStockAdditionAckToEAushadhi(1, EAUSHADHI_FACILITY_ID));
        }

        @Test
        @DisplayName("should read an empty answer as a failure")
        void ack_shouldReadEmptyAnswerAsFailure() throws Exception {
            when(httpUtils.getWithResponseEntity(anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>("[]", HttpStatus.OK));

            assertEquals("0", service.sendStockAdditionAckToEAushadhi(1, EAUSHADHI_FACILITY_ID));
        }

        @Test
        @DisplayName("should read a non-200 answer as a failure")
        void ack_shouldReadNonOkAnswerAsFailure() throws Exception {
            when(httpUtils.getWithResponseEntity(anyString(), any(HttpHeaders.class)))
                    .thenReturn(new ResponseEntity<>("[]", HttpStatus.BAD_GATEWAY));

            assertEquals("0", service.sendStockAdditionAckToEAushadhi(1, EAUSHADHI_FACILITY_ID));
        }

        @Test
        @DisplayName("should read a transport failure as a failure")
        void ack_shouldReadTransportFailureAsFailure() throws Exception {
            when(httpUtils.getWithResponseEntity(anyString(), any(HttpHeaders.class)))
                    .thenThrow(new IllegalStateException("connection refused"));

            assertEquals("0", service.sendStockAdditionAckToEAushadhi(1, EAUSHADHI_FACILITY_ID));
        }
    }

    @Nested
    @DisplayName("Item and stock writes")
    class ItemWriteTests {

        @Test
        @DisplayName("insertItemsInAmrit should build the item from the e-aushadhi row and its lookups")
        void insertItemsInAmrit_shouldBuildItem() throws Exception {
            stubStockLookups();
            when(itemRepo.save(any(ItemMaster.class))).thenAnswer(i -> i.getArgument(0));

            ItemMaster added = service.insertItemsInAmrit(stockRow("10", "10"), mappedFacility());

            assertEquals("Paracetamol", added.getItemName());
            assertEquals("BRAND-1_EAushadhi", added.getItemCode());
            assertEquals("500 mg", added.getComposition());
            assertEquals(11, added.getItemFormID());
            assertEquals(5, added.getRouteID());
            assertEquals(2, added.getItemCategoryID());
            assertEquals("active", added.getStatus());
            assertTrue(added.getIsEDL());
            assertTrue(added.getIsEaushadi());
        }

        @Test
        @DisplayName("insertItemsInAmrit should mark a non-EDL row as such")
        void insertItemsInAmrit_shouldMarkNonEdl() throws Exception {
            stubStockLookups();
            when(itemRepo.save(any(ItemMaster.class))).thenAnswer(i -> i.getArgument(0));
            EAushadhiResponse row = stockRow("10", "10");
            row.setEdl("NON-EDL");

            assertTrue(!service.insertItemsInAmrit(row, mappedFacility()).getIsEDL());
        }

        @Test
        @DisplayName("insertItemsInAmrit should wrap a failing lookup")
        void insertItemsInAmrit_shouldWrapFailingLookup() {
            when(itemFormRepo.getItemFormID(anyString())).thenReturn(null);

            assertThrows(Exception.class, () -> service.insertItemsInAmrit(stockRow("10", "10"), mappedFacility()));
        }

        @Test
        @DisplayName("insertItemStockEntry should stock the batch with its converted expiry date")
        void insertItemStockEntry_shouldStockBatch() throws Exception {
            ItemStockEntry saved = new ItemStockEntry();
            saved.setItemStockEntryID(501L);
            when(itemStockEntryRepo.save(any(ItemStockEntry.class))).thenReturn(saved);
            when(itemStockEntryRepo.updateVanSerialNo(501L)).thenReturn(1);

            assertEquals(1, service.insertItemStockEntry(AMRIT_FACILITY_ID, savedItem(),
                    stockRow("10", "10"), 21, 31));

            ArgumentCaptor<ItemStockEntry> captor = ArgumentCaptor.forClass(ItemStockEntry.class);
            verify(itemStockEntryRepo).save(captor.capture());
            ItemStockEntry entry = captor.getValue();
            assertEquals("BATCH-1", entry.getBatchNo());
            assertEquals(50, entry.getQuantity());
            assertEquals(50, entry.getQuantityInHand());
            assertEquals(21, entry.getVanID());
            assertEquals(31, entry.getParkingPlaceID());
            assertEquals(110001, entry.getEntryTypeID());
            assertEquals("E-Aushadhi Stock Entry", entry.getEntryType());
            assertNotNull(entry.getExpiryDate());
        }

        @Test
        @DisplayName("insertItemStockEntry should report failure when the write left no row behind")
        void insertItemStockEntry_shouldReportFailure() throws Exception {
            when(itemStockEntryRepo.save(any(ItemStockEntry.class))).thenReturn(null);

            assertEquals(0, service.insertItemStockEntry(AMRIT_FACILITY_ID, savedItem(),
                    stockRow("10", "10"), 21, 31));
        }

        @Test
        @DisplayName("insertItemFacilityMapping should map the item to the facility as active")
        void insertItemFacilityMapping_shouldMapItem() throws Exception {
            when(mItemFacilityMappingRepo.save(any(M_itemfacilitymapping.class)))
                    .thenReturn(new M_itemfacilitymapping());

            assertEquals(1, service.insertItemFacilityMapping(AMRIT_FACILITY_ID, savedItem()));

            ArgumentCaptor<M_itemfacilitymapping> captor =
                    ArgumentCaptor.forClass(M_itemfacilitymapping.class);
            verify(mItemFacilityMappingRepo).save(captor.capture());
            assertEquals(AMRIT_FACILITY_ID, captor.getValue().getFacilityID());
            assertEquals(101, captor.getValue().getItemID());
            assertEquals("Active", captor.getValue().getStatus());
        }

        @Test
        @DisplayName("insertItemFacilityMapping should report failure when the write left no row behind")
        void insertItemFacilityMapping_shouldReportFailure() throws Exception {
            when(mItemFacilityMappingRepo.save(any(M_itemfacilitymapping.class))).thenReturn(null);

            assertEquals(0, service.insertItemFacilityMapping(AMRIT_FACILITY_ID, savedItem()));
        }

        @Test
        @DisplayName("mapItemDetailsAndInsertStockToSubFacility should map then stock the sub-facility")
        void mapAndStockSubFacility_shouldMapThenStock() throws Exception {
            stubStockEntrySaves();

            service.mapItemDetailsAndInsertStockToSubFacility(stockRow("10", "10"), mappedFacility(), 21, 31,
                    savedItem());

            verify(mItemFacilityMappingRepo).save(any(M_itemfacilitymapping.class));
            verify(itemStockEntryRepo).save(any(ItemStockEntry.class));
        }

        @Test
        @DisplayName("mapItemDetailsAndInsertStockToSubFacility should swallow a failed mapping")
        void mapAndStockSubFacility_shouldSwallowFailedMapping() {
            when(mItemFacilityMappingRepo.save(any(M_itemfacilitymapping.class))).thenReturn(null);

            assertDoesNotThrow(() -> service.mapItemDetailsAndInsertStockToSubFacility(stockRow("10", "10"),
                    mappedFacility(), 21, 31, savedItem()));

            verify(itemStockEntryRepo, never()).save(any(ItemStockEntry.class));
        }
    }

    @Nested
    @DisplayName("The dispense sync")
    class DispenseSyncTests {

        private T_PatientIssue patientIssue() {
            T_PatientIssue issue = new T_PatientIssue();
            issue.setPatientIssueID(901L);
            issue.setBenRegID(BigInteger.valueOf(4321L));
            issue.setFacilityID(AMRIT_FACILITY_ID);
            issue.setPrescriptionID(701);
            issue.setGender("Female");
            issue.setDoctorName("Dr Rao");
            issue.setCreatedDate(new Timestamp(1_700_000_000_000L));
            return issue;
        }

        private String benSearchAnswer(String lastName) {
            return "{\"data\":[{\"beneficiaryID\":9999,\"beneficiaryRegID\":4321,\"firstName\":\"Asha\","
                    + (lastName == null ? "" : "\"lastName\":\"" + lastName + "\",")
                    + "\"fatherName\":\"Ram\",\"actualAge\":34,\"ageUnits\":\"Years\","
                    + "\"dOB\":\"1990-08-15T00:00:00.000\"}]}";
        }

        /**
         * The repository signature says {@code List<Objects[]>} while the service iterates
         * it as {@code Object[]}, so the rows have to be handed over through a raw list.
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private void stubDispensedDrugs() {
            List rows = new ArrayList<>();
            rows.add(new Object[] { "BRAND-1", "BATCH-1", "10", "Paracetamol", "50", 101, "Tablet" });
            when(patientIssueRepo.getDispensedDrugDetails(701, AMRIT_FACILITY_ID, 901L)).thenReturn(rows);
        }

        @Test
        @DisplayName("syncDispenseDetailsToEAushadhi should page the dispense records of one facility")
        void sync_shouldPageOneFacility() throws Exception {
            when(facilityRepo.getAmritFacilityID(EAUSHADHI_FACILITY_ID)).thenReturn(mappedFacility());
            when(patientIssueRepo.getIssueDetailsForEAushadhi(eq(AMRIT_FACILITY_ID), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(patientIssue()), PageRequest.of(0, 10), 1));
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(benSearchAnswer("Devi"));
            stubDispensedDrugs();

            JsonObject result = JsonParser.parseString(service.syncDispenseDetailsToEAushadhi(
                    "{\"eAushadhiFacilityId\":" + EAUSHADHI_FACILITY_ID + ",\"pageNo\":0}", "session-key-123"))
                    .getAsJsonObject();

            assertEquals(1, result.get("Total Pages").getAsInt());
            JsonObject record = result.getAsJsonArray("patientIssueDetails").get(0).getAsJsonObject();
            assertEquals("Asha Devi", record.get("hststr_patient_name").getAsString());
            assertEquals("Ram", record.get("hststr_father_name").getAsString());
            assertEquals(9999, record.get("amrit_beneficiary_id").getAsInt());
            assertEquals("Female", record.get("gnum_gender_code").getAsString());
            assertEquals(1, record.getAsJsonArray("drug_dispensed").size());
        }

        @Test
        @DisplayName("syncDispenseDetailsToEAushadhi should page every facility when none is named")
        void sync_shouldPageEveryFacility() throws Exception {
            when(patientIssueRepo.getIssueDetailsForEAushadhiForAllFacility(any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(List.of(patientIssue()), PageRequest.of(0, 10), 1));
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(benSearchAnswer("Devi"));
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(mappedFacility());
            stubDispensedDrugs();

            JsonObject result = JsonParser.parseString(
                    service.syncDispenseDetailsToEAushadhi("{\"pageNo\":0}", "session-key-123")).getAsJsonObject();

            assertEquals(1, result.getAsJsonArray("patientIssueDetails").size());
        }

        @Test
        @DisplayName("syncDispenseDetailsToEAushadhi should refuse a facility AMRIT does not know")
        void sync_shouldRefuseUnknownFacility() {
            when(facilityRepo.getAmritFacilityID(EAUSHADHI_FACILITY_ID)).thenReturn(null);

            assertTrue(assertThrows(FHIRException.class, () -> service.syncDispenseDetailsToEAushadhi(
                    "{\"eAushadhiFacilityId\":" + EAUSHADHI_FACILITY_ID + ",\"pageNo\":0}", "session-key-123"))
                    .getMessage().contains("Error while receiving patient wise drug dispense details"));
        }

        @Test
        @DisplayName("the per-facility walk should keep only issues that actually dispensed a drug")
        void perFacilityWalk_shouldKeepOnlyDispensedIssues() throws Exception {
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(benSearchAnswer("Devi"));
            when(patientIssueRepo.getDispensedDrugDetails(anyInt(), anyInt(), anyLong()))
                    .thenReturn(new ArrayList<>());

            assertTrue(service.getPatientIssueDetailsForEAushadhByFacilityID(List.of(patientIssue()),
                    mappedFacility(), "session-key-123").isEmpty());
        }

        @Test
        @DisplayName("the per-facility walk should carry only the first name when there is no surname")
        void perFacilityWalk_shouldCarryFirstNameOnly() throws Exception {
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(benSearchAnswer(null));
            stubDispensedDrugs();

            ArrayList<java.util.Map<String, Object>> records = service
                    .getPatientIssueDetailsForEAushadhByFacilityID(List.of(patientIssue()), mappedFacility(),
                            "session-key-123");

            assertEquals("Asha", records.get(0).get("hststr_patient_name"));
        }

        @Test
        @DisplayName("the per-facility walk should carry on when the beneficiary search is ambiguous")
        void perFacilityWalk_shouldCarryOnForAmbiguousSearch() throws Exception {
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn("{\"data\":[]}");
            stubDispensedDrugs();

            ArrayList<java.util.Map<String, Object>> records = service
                    .getPatientIssueDetailsForEAushadhByFacilityID(List.of(patientIssue()), mappedFacility(),
                            "session-key-123");

            assertEquals(1, records.size());
            assertTrue(!records.get(0).containsKey("amrit_beneficiary_id"));
        }

        @Test
        @DisplayName("the per-facility walk should wrap a failing beneficiary search")
        void perFacilityWalk_shouldWrapSearchFailure() throws Exception {
            when(aPIChannel.benSearchByBenID(anyString(), any()))
                    .thenThrow(new FHIRException("error in patient search"));

            assertThrows(FHIRException.class, () -> service.getPatientIssueDetailsForEAushadhByFacilityID(
                    List.of(patientIssue()), mappedFacility(), "session-key-123"));
        }

        @Test
        @DisplayName("the all-facility walk should resolve each issue's own e-aushadhi facility")
        void allFacilityWalk_shouldResolveEachFacility() throws Exception {
            when(aPIChannel.benSearchByBenID(anyString(), any())).thenReturn(benSearchAnswer("Devi"));
            when(facilityRepo.geteAushadhiFacilityID(AMRIT_FACILITY_ID)).thenReturn(mappedFacility());
            stubDispensedDrugs();

            assertEquals(1, service.getPatientIssueDetailsForEAushadhiWithoutFacilityID(
                    List.of(patientIssue()), "session-key-123").size());
            verify(facilityRepo).geteAushadhiFacilityID(AMRIT_FACILITY_ID);
        }

        @Test
        @DisplayName("getDrugDispenseDetails should build one sync record per dispensed drug")
        void getDrugDispenseDetails_shouldBuildOneRecordPerDrug() throws Exception {
            stubDispensedDrugs();

            ArrayList<SyncDispenseDetailsRequest> records = service.getDrugDispenseDetails(patientIssue(),
                    mappedFacility(), EAUSHADHI_FACILITY_ID);

            assertEquals(1, records.size());
        }

        @Test
        @DisplayName("getDrugDispenseDetails should answer with nothing when no drug was dispensed")
        void getDrugDispenseDetails_shouldAnswerEmptyWithoutDrugs() throws Exception {
            when(patientIssueRepo.getDispensedDrugDetails(anyInt(), anyInt(), anyLong()))
                    .thenReturn(new ArrayList<>());

            assertTrue(service.getDrugDispenseDetails(patientIssue(), mappedFacility(), EAUSHADHI_FACILITY_ID)
                    .isEmpty());
        }

        @Test
        @DisplayName("getDrugDispenseDetails should wrap a failing lookup")
        void getDrugDispenseDetails_shouldWrapFailingLookup() {
            when(patientIssueRepo.getDispensedDrugDetails(anyInt(), anyInt(), anyLong()))
                    .thenThrow(new IllegalStateException("db down"));

            assertThrows(Exception.class, () -> service.getDrugDispenseDetails(patientIssue(), mappedFacility(),
                    EAUSHADHI_FACILITY_ID));
        }

        @Test
        @DisplayName("updateSyncStatusForEAushadhiDispense should report success when at least one row updated")
        void updateSyncStatus_shouldReportSuccess() throws Exception {
            when(patientIssueRepo.updateEAushadhiIssueSyncStatus(901L)).thenReturn(1);
            when(patientIssueRepo.updateEAushadhiIssueSyncStatus(902L)).thenReturn(0);

            assertEquals("1", service.updateSyncStatusForEAushadhiDispense(List.of("901", "902")));
        }

        @Test
        @DisplayName("updateSyncStatusForEAushadhiDispense should report failure when nothing updated")
        void updateSyncStatus_shouldReportFailure() throws Exception {
            when(patientIssueRepo.updateEAushadhiIssueSyncStatus(anyLong())).thenReturn(0);

            assertEquals("2", service.updateSyncStatusForEAushadhiDispense(List.of("901")));
        }

        @Test
        @DisplayName("updateSyncStatusForEAushadhiDispense should wrap an unusable issue id")
        void updateSyncStatus_shouldWrapUnusableId() {
            assertThrows(FHIRException.class,
                    () -> service.updateSyncStatusForEAushadhiDispense(List.of("not-a-number")));
        }
    }

    @Nested
    @DisplayName("getFacilityStockProcessLog")
    class ProcessLogTests {

        @Test
        @DisplayName("should answer with the newest process-log row for the facility")
        void processLog_shouldAnswerWithNewestRow() throws Exception {
            E_AusdhFacilityProcessLog older = new E_AusdhFacilityProcessLog();
            older.seteLID(1);
            E_AusdhFacilityProcessLog newest = new E_AusdhFacilityProcessLog();
            newest.seteLID(2);
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityId(AMRIT_FACILITY_ID))
                    .thenReturn(List.of(older, newest));

            JsonObject result = JsonParser
                    .parseString(service.getFacilityStockProcessLog("{\"facilityID\":" + AMRIT_FACILITY_ID + "}"))
                    .getAsJsonObject();

            assertEquals(2, result.get("eLID").getAsInt());
        }

        @Test
        @DisplayName("should answer with an empty list when the facility has never been processed")
        void processLog_shouldAnswerEmptyWithoutRows() throws Exception {
            when(e_AusdhFacilityProcessLogRepo.findByAmrithFacilityId(AMRIT_FACILITY_ID)).thenReturn(List.of());

            assertEquals("[]", service.getFacilityStockProcessLog("{\"facilityID\":" + AMRIT_FACILITY_ID + "}"));
        }
    }
}
