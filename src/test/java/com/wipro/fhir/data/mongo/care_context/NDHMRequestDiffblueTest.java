package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class NDHMRequestDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link NDHMRequest#setCredential(Credential)}
     *   <li>{@link NDHMRequest#setLink(AddCareContext)}
     *   <li>{@link NDHMRequest#setQuery(Query)}
     *   <li>{@link NDHMRequest#setRequestId(String)}
     *   <li>{@link NDHMRequest#setTimestamp(String)}
     *   <li>{@link NDHMRequest#setTransactionId(String)}
     *   <li>{@link NDHMRequest#getCredential()}
     *   <li>{@link NDHMRequest#getLink()}
     *   <li>{@link NDHMRequest#getQuery()}
     *   <li>{@link NDHMRequest#getRequestId()}
     *   <li>{@link NDHMRequest#getTimestamp()}
     *   <li>{@link NDHMRequest#getTransactionId()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        NDHMRequest ndhmRequest = new NDHMRequest();

        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        // Act
        ndhmRequest.setCredential(credential);
        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");
        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);
        ndhmRequest.setLink(link);
        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");
        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        Credential actualCredential = ndhmRequest.getCredential();
        AddCareContext actualLink = ndhmRequest.getLink();
        Query actualQuery = ndhmRequest.getQuery();
        String actualRequestId = ndhmRequest.getRequestId();
        String actualTimestamp = ndhmRequest.getTimestamp();

        // Assert that nothing has changed
        assertEquals("42", actualRequestId);
        assertEquals("42", ndhmRequest.getTransactionId());
        assertEquals("Timestamp", actualTimestamp);
        assertSame(link, actualLink);
        assertSame(credential, actualCredential);
        assertSame(query, actualQuery);
    }
}
