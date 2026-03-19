package com.wipro.fhir.service.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight ES sync service for FHIR-API
 * Only updates ABHA-related fields without fetching full beneficiary data
 */
@Service
public class AbhaElasticsearchSyncService {

    private static final Logger logger = LoggerFactory.getLogger(AbhaElasticsearchSyncService.class);

    @Autowired
    private ElasticsearchClient esClient;

    @Value("${elasticsearch.index.beneficiary}")
    private String beneficiaryIndex;

    @Value("${elasticsearch.enabled}")
    private boolean esEnabled;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Update ABHA details in Elasticsearch after ABHA is created/updated
     * This method updates only ABHA fields, doesn't require full beneficiary data
     */
    @Async("esAsyncExecutor")
public void updateAbhaInElasticsearch(Long benRegId, String healthId, String healthIdNumber, String createdDate) {
    if (!esEnabled) {
        logger.debug("Elasticsearch is disabled, skipping ABHA sync");
        return;
    }

    if (benRegId == null) {
        logger.warn("benRegId is null, cannot sync ABHA to ES");
        return;
    }

    int maxRetries = 3;
    int retryDelay = 2000; // 2 seconds

    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            logger.info("Syncing ABHA details to ES for benRegId: {} (attempt {}/{})", benRegId, attempt, maxRetries);

            Map<String, Object> abhaData = new HashMap<>();
            abhaData.put("healthID", healthId);
            abhaData.put("abhaID", healthIdNumber);
            abhaData.put("abhaCreatedDate", createdDate);

            String documentId = String.valueOf(benRegId);
            boolean exists = checkDocumentExists(documentId);

            if (exists) {
                UpdateRequest<Object, Object> updateRequest = UpdateRequest.of(u -> u
                    .index(beneficiaryIndex)
                    .id(documentId)
                    .doc(abhaData)
                    .refresh(Refresh.True)  
                    .docAsUpsert(false)
                    .retryOnConflict(3) 
                );

                esClient.update(updateRequest, Object.class);
                logger.info("Successfully updated ABHA in ES: benRegId={}", benRegId);
                return; 

            } else {
                logger.warn("Document not found in ES for benRegId={} (attempt {}/{})", benRegId, attempt, maxRetries);
                if (attempt < maxRetries) {
                    Thread.sleep(retryDelay * attempt); 
                }
            }

        } catch (java.net.SocketTimeoutException e) {
            logger.error("Timeout updating ABHA in ES for benRegId {} (attempt {}/{}): {}", 
                        benRegId, attempt, maxRetries, e.getMessage());
            if (attempt < maxRetries) {
                try {
                    Thread.sleep(retryDelay * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error updating ABHA in ES for benRegId {} (attempt {}/{}): {}", 
                        benRegId, attempt, maxRetries, e.getMessage());
            if (attempt == maxRetries) {
                logger.error("Failed to sync ABHA after {} attempts for benRegId {}", maxRetries, benRegId);
            }
        }
    }
}

    /**
     * Check if document exists in ES
     */
    private boolean checkDocumentExists(String documentId) {
        try {
            GetRequest getRequest = GetRequest.of(g -> g
                .index(beneficiaryIndex)
                .id(documentId)
            );

            GetResponse<Object> response = esClient.get(getRequest, Object.class);
            return response.found();

        } catch (Exception e) {
            logger.debug("Document not found or error checking: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Retry sync after 5 seconds if document wasn't found
     * (Handles race condition where ABHA is saved before beneficiary is synced to ES)
     */
    @Async("esAsyncExecutor")
    private void retryAfterDelay(Long benRegId, String healthId, String healthIdNumber, String createdDate) {
        try {
            Thread.sleep(5000); // Wait 5 seconds
            logger.info("Retrying ABHA sync for benRegId: {}", benRegId);
            updateAbhaInElasticsearch(benRegId, healthId, healthIdNumber, createdDate);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Retry interrupted for benRegId: {}", benRegId);
        }
    }

    /**
     * Update multiple ABHA addresses (comma-separated)
     */
    @Async("esAsyncExecutor")
    public void updateMultipleAbhaAddresses(Long benRegId, String commaSeparatedHealthIds, 
                                           String healthIdNumber, String createdDate) {
        if (!esEnabled || benRegId == null) {
            return;
        }

        // For multiple ABHA addresses, store as comma-separated string
        updateAbhaInElasticsearch(benRegId, commaSeparatedHealthIds, healthIdNumber, createdDate);
    }
}