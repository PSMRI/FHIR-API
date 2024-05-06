package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ClinicalFeedDataLogDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ClinicalFeedDataLog#setAuthor(String)}
     *   <li>{@link ClinicalFeedDataLog#setEntry(String)}
     *   <li>{@link ClinicalFeedDataLog#setEntryContentEncounterFull_URL(String)}
     *   <li>{@link ClinicalFeedDataLog#setEntryContent_cdata(String)}
     *   <li>{@link ClinicalFeedDataLog#setEntryID(String)}
     *   <li>{@link ClinicalFeedDataLog#setEntrySuccess(Boolean)}
     *   <li>{@link ClinicalFeedDataLog#setFeedID(String)}
     *   <li>{@link ClinicalFeedDataLog#setFeedLink(String)}
     *   <li>{@link ClinicalFeedDataLog#setFeedSuccess(Boolean)}
     *   <li>{@link ClinicalFeedDataLog#setGeneratorUri(String)}
     *   <li>{@link ClinicalFeedDataLog#setId(String)}
     *   <li>{@link ClinicalFeedDataLog#setLinkNextArchive(String)}
     *   <li>{@link ClinicalFeedDataLog#setLinkPrevArchive(String)}
     *   <li>{@link ClinicalFeedDataLog#setLinkSelf(String)}
     *   <li>{@link ClinicalFeedDataLog#setLinkVia(String)}
     *   <li>{@link ClinicalFeedDataLog#setTitle(String)}
     *   <li>{@link ClinicalFeedDataLog#getAuthor()}
     *   <li>{@link ClinicalFeedDataLog#getEntry()}
     *   <li>{@link ClinicalFeedDataLog#getEntryContentEncounterFull_URL()}
     *   <li>{@link ClinicalFeedDataLog#getEntryContent_cdata()}
     *   <li>{@link ClinicalFeedDataLog#getEntryID()}
     *   <li>{@link ClinicalFeedDataLog#getEntrySuccess()}
     *   <li>{@link ClinicalFeedDataLog#getFeedID()}
     *   <li>{@link ClinicalFeedDataLog#getFeedLink()}
     *   <li>{@link ClinicalFeedDataLog#getFeedSuccess()}
     *   <li>{@link ClinicalFeedDataLog#getGeneratorUri()}
     *   <li>{@link ClinicalFeedDataLog#getId()}
     *   <li>{@link ClinicalFeedDataLog#getLinkNextArchive()}
     *   <li>{@link ClinicalFeedDataLog#getLinkPrevArchive()}
     *   <li>{@link ClinicalFeedDataLog#getLinkSelf()}
     *   <li>{@link ClinicalFeedDataLog#getLinkVia()}
     *   <li>{@link ClinicalFeedDataLog#getTitle()}
     *   <li>{@link ClinicalFeedDataLog#getUpdated()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ClinicalFeedDataLog clinicalFeedDataLog = new ClinicalFeedDataLog();

        // Act
        clinicalFeedDataLog.setAuthor("JaneDoe");
        clinicalFeedDataLog.setEntry("Entry");
        clinicalFeedDataLog.setEntryContentEncounterFull_URL("https://example.org/example");
        clinicalFeedDataLog.setEntryContent_cdata("Not all who wander are lost");
        clinicalFeedDataLog.setEntryID("Entry ID");
        clinicalFeedDataLog.setEntrySuccess(true);
        clinicalFeedDataLog.setFeedID("Feed ID");
        clinicalFeedDataLog.setFeedLink("Feed Link");
        clinicalFeedDataLog.setFeedSuccess(true);
        clinicalFeedDataLog.setGeneratorUri("Generator Uri");
        clinicalFeedDataLog.setId("42");
        clinicalFeedDataLog.setLinkNextArchive("Link Next Archive");
        clinicalFeedDataLog.setLinkPrevArchive("Link Prev Archive");
        clinicalFeedDataLog.setLinkSelf("Link Self");
        clinicalFeedDataLog.setLinkVia("Link Via");
        clinicalFeedDataLog.setTitle("Dr");
        String actualAuthor = clinicalFeedDataLog.getAuthor();
        String actualEntry = clinicalFeedDataLog.getEntry();
        String actualEntryContentEncounterFull_URL = clinicalFeedDataLog.getEntryContentEncounterFull_URL();
        String actualEntryContent_cdata = clinicalFeedDataLog.getEntryContent_cdata();
        String actualEntryID = clinicalFeedDataLog.getEntryID();
        Boolean actualEntrySuccess = clinicalFeedDataLog.getEntrySuccess();
        String actualFeedID = clinicalFeedDataLog.getFeedID();
        String actualFeedLink = clinicalFeedDataLog.getFeedLink();
        Boolean actualFeedSuccess = clinicalFeedDataLog.getFeedSuccess();
        String actualGeneratorUri = clinicalFeedDataLog.getGeneratorUri();
        String actualId = clinicalFeedDataLog.getId();
        String actualLinkNextArchive = clinicalFeedDataLog.getLinkNextArchive();
        String actualLinkPrevArchive = clinicalFeedDataLog.getLinkPrevArchive();
        String actualLinkSelf = clinicalFeedDataLog.getLinkSelf();
        String actualLinkVia = clinicalFeedDataLog.getLinkVia();
        String actualTitle = clinicalFeedDataLog.getTitle();
        clinicalFeedDataLog.getUpdated();

        // Assert that nothing has changed
        assertEquals("42", actualId);
        assertEquals("Dr", actualTitle);
        assertEquals("Entry ID", actualEntryID);
        assertEquals("Entry", actualEntry);
        assertEquals("Feed ID", actualFeedID);
        assertEquals("Feed Link", actualFeedLink);
        assertEquals("Generator Uri", actualGeneratorUri);
        assertEquals("JaneDoe", actualAuthor);
        assertEquals("Link Next Archive", actualLinkNextArchive);
        assertEquals("Link Prev Archive", actualLinkPrevArchive);
        assertEquals("Link Self", actualLinkSelf);
        assertEquals("Link Via", actualLinkVia);
        assertEquals("Not all who wander are lost", actualEntryContent_cdata);
        assertEquals("https://example.org/example", actualEntryContentEncounterFull_URL);
        assertTrue(actualEntrySuccess);
        assertTrue(actualFeedSuccess);
    }
}
