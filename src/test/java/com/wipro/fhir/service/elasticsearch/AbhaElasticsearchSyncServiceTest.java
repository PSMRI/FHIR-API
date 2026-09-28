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
package com.wipro.fhir.service.elasticsearch;

import java.net.SocketTimeoutException;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("AbhaElasticsearchSyncService Test Suite")
class AbhaElasticsearchSyncServiceTest {

    private static final Long BEN_REG_ID = 4321L;
    private static final String HEALTH_ID = "abc@sbx";
    private static final String HEALTH_ID_NUMBER = "11-1111-1111-1111";
    private static final String CREATED_DATE = "2026-08-24";

    @Mock
    private ElasticsearchClient esClient;

    @Mock
    private GetResponse<Object> getResponse;

    @Mock
    private UpdateResponse<Object> updateResponse;

    @InjectMocks
    private AbhaElasticsearchSyncService service;

    @BeforeEach
    @DisplayName("Enable the sync and name the index before each test")
    void setUp() {
        ReflectionTestUtils.setField(service, "beneficiaryIndex", "beneficiary");
        ReflectionTestUtils.setField(service, "esEnabled", true);
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should update the existing beneficiary document")
    void updateAbha_shouldUpdateExistingDocument() throws Exception {
        when(esClient.get(any(GetRequest.class), eq(Object.class))).thenReturn(getResponse);
        when(getResponse.found()).thenReturn(true);
        when(esClient.update(any(UpdateRequest.class), eq(Object.class))).thenReturn(updateResponse);

        service.updateAbhaInElasticsearch(BEN_REG_ID, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE);

        ArgumentCaptor<UpdateRequest> captor = ArgumentCaptor.forClass(UpdateRequest.class);
        verify(esClient).update(captor.capture(), eq(Object.class));
        assertEquals("beneficiary", captor.getValue().index());
        assertEquals("4321", captor.getValue().id());
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should do nothing at all while the sync is disabled")
    void updateAbha_shouldDoNothingWhenDisabled() {
        ReflectionTestUtils.setField(service, "esEnabled", false);

        service.updateAbhaInElasticsearch(BEN_REG_ID, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE);

        verifyNoInteractions(esClient);
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should do nothing without a beneficiary id to key on")
    void updateAbha_shouldDoNothingWithoutBenRegId() {
        service.updateAbhaInElasticsearch(null, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE);

        verifyNoInteractions(esClient);
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should retry three times when the document is never found")
    void updateAbha_shouldRetryWhenDocumentMissing() throws Exception {
        when(esClient.get(any(GetRequest.class), eq(Object.class))).thenReturn(getResponse);
        when(getResponse.found()).thenReturn(false);

        service.updateAbhaInElasticsearch(BEN_REG_ID, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE);

        verify(esClient, times(3)).get(any(GetRequest.class), eq(Object.class));
        verify(esClient, never()).update(any(UpdateRequest.class), eq(Object.class));
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should treat a lookup failure as a missing document")
    void updateAbha_shouldTreatLookupFailureAsMissing() throws Exception {
        when(esClient.get(any(GetRequest.class), eq(Object.class)))
                .thenThrow(new IllegalStateException("index closed"));

        assertDoesNotThrow(() ->
                service.updateAbhaInElasticsearch(BEN_REG_ID, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE));

        verify(esClient, never()).update(any(UpdateRequest.class), eq(Object.class));
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should retry after a socket timeout on the update")
    void updateAbha_shouldRetryAfterSocketTimeout() throws Exception {
        when(esClient.get(any(GetRequest.class), eq(Object.class))).thenReturn(getResponse);
        when(getResponse.found()).thenReturn(true);
        when(esClient.update(any(UpdateRequest.class), eq(Object.class)))
                .thenThrow(new SocketTimeoutException("read timed out"))
                .thenReturn(updateResponse);

        service.updateAbhaInElasticsearch(BEN_REG_ID, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE);

        verify(esClient, times(2)).update(any(UpdateRequest.class), eq(Object.class));
    }

    @Test
    @DisplayName("updateAbhaInElasticsearch should give up quietly after three failing updates")
    void updateAbha_shouldGiveUpAfterThreeFailures() throws Exception {
        when(esClient.get(any(GetRequest.class), eq(Object.class))).thenReturn(getResponse);
        when(getResponse.found()).thenReturn(true);
        when(esClient.update(any(UpdateRequest.class), eq(Object.class)))
                .thenThrow(new IllegalStateException("version conflict"));

        assertDoesNotThrow(() ->
                service.updateAbhaInElasticsearch(BEN_REG_ID, HEALTH_ID, HEALTH_ID_NUMBER, CREATED_DATE));

        verify(esClient, times(3)).update(any(UpdateRequest.class), eq(Object.class));
    }

    @Test
    @DisplayName("updateMultipleAbhaAddresses should write the comma-separated addresses through the same path")
    void updateMultipleAbhaAddresses_shouldDelegateToSingleUpdate() throws Exception {
        when(esClient.get(any(GetRequest.class), eq(Object.class))).thenReturn(getResponse);
        when(getResponse.found()).thenReturn(true);
        when(esClient.update(any(UpdateRequest.class), eq(Object.class))).thenReturn(updateResponse);

        service.updateMultipleAbhaAddresses(BEN_REG_ID, "abc@sbx,def@sbx", HEALTH_ID_NUMBER, CREATED_DATE);

        verify(esClient).update(any(UpdateRequest.class), eq(Object.class));
    }

    @Test
    @DisplayName("updateMultipleAbhaAddresses should do nothing while the sync is disabled or the id is absent")
    void updateMultipleAbhaAddresses_shouldDoNothingWhenDisabledOrUnkeyed() {
        ReflectionTestUtils.setField(service, "esEnabled", false);
        service.updateMultipleAbhaAddresses(BEN_REG_ID, "abc@sbx", HEALTH_ID_NUMBER, CREATED_DATE);

        ReflectionTestUtils.setField(service, "esEnabled", true);
        service.updateMultipleAbhaAddresses(null, "abc@sbx", HEALTH_ID_NUMBER, CREATED_DATE);

        verifyNoInteractions(esClient);
    }
}
