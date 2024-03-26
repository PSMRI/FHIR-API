package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FeedDataLogDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link FeedDataLog#setAuthor(String)}
     *   <li>{@link FeedDataLog#setEntry(String)}
     *   <li>{@link FeedDataLog#setEntryContentPatientFull_URL(String)}
     *   <li>{@link FeedDataLog#setEntryContent_cdata(String)}
     *   <li>{@link FeedDataLog#setEntryID(String)}
     *   <li>{@link FeedDataLog#setEntrySuccess(Boolean)}
     *   <li>{@link FeedDataLog#setFeedID(String)}
     *   <li>{@link FeedDataLog#setFeedLink(String)}
     *   <li>{@link FeedDataLog#setFeedSuccess(Boolean)}
     *   <li>{@link FeedDataLog#setGeneratorUri(String)}
     *   <li>{@link FeedDataLog#setId(String)}
     *   <li>{@link FeedDataLog#setLinkNextArchive(String)}
     *   <li>{@link FeedDataLog#setLinkPrevArchive(String)}
     *   <li>{@link FeedDataLog#setLinkSelf(String)}
     *   <li>{@link FeedDataLog#setLinkVia(String)}
     *   <li>{@link FeedDataLog#setTitle(String)}
     *   <li>{@link FeedDataLog#getAuthor()}
     *   <li>{@link FeedDataLog#getEntry()}
     *   <li>{@link FeedDataLog#getEntryContentPatientFull_URL()}
     *   <li>{@link FeedDataLog#getEntryContent_cdata()}
     *   <li>{@link FeedDataLog#getEntryID()}
     *   <li>{@link FeedDataLog#getEntrySuccess()}
     *   <li>{@link FeedDataLog#getFeedID()}
     *   <li>{@link FeedDataLog#getFeedLink()}
     *   <li>{@link FeedDataLog#getFeedSuccess()}
     *   <li>{@link FeedDataLog#getGeneratorUri()}
     *   <li>{@link FeedDataLog#getId()}
     *   <li>{@link FeedDataLog#getLinkNextArchive()}
     *   <li>{@link FeedDataLog#getLinkPrevArchive()}
     *   <li>{@link FeedDataLog#getLinkSelf()}
     *   <li>{@link FeedDataLog#getLinkVia()}
     *   <li>{@link FeedDataLog#getTitle()}
     *   <li>{@link FeedDataLog#getUpdated()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        FeedDataLog feedDataLog = new FeedDataLog();

        // Act
        feedDataLog.setAuthor("JaneDoe");
        feedDataLog.setEntry("Entry");
        feedDataLog.setEntryContentPatientFull_URL("https://example.org/example");
        feedDataLog.setEntryContent_cdata("Not all who wander are lost");
        feedDataLog.setEntryID("Entry ID");
        feedDataLog.setEntrySuccess(true);
        feedDataLog.setFeedID("Feed ID");
        feedDataLog.setFeedLink("Feed Link");
        feedDataLog.setFeedSuccess(true);
        feedDataLog.setGeneratorUri("Generator Uri");
        feedDataLog.setId("42");
        feedDataLog.setLinkNextArchive("Link Next Archive");
        feedDataLog.setLinkPrevArchive("Link Prev Archive");
        feedDataLog.setLinkSelf("Link Self");
        feedDataLog.setLinkVia("Link Via");
        feedDataLog.setTitle("Dr");
        String actualAuthor = feedDataLog.getAuthor();
        String actualEntry = feedDataLog.getEntry();
        String actualEntryContentPatientFull_URL = feedDataLog.getEntryContentPatientFull_URL();
        String actualEntryContent_cdata = feedDataLog.getEntryContent_cdata();
        String actualEntryID = feedDataLog.getEntryID();
        Boolean actualEntrySuccess = feedDataLog.getEntrySuccess();
        String actualFeedID = feedDataLog.getFeedID();
        String actualFeedLink = feedDataLog.getFeedLink();
        Boolean actualFeedSuccess = feedDataLog.getFeedSuccess();
        String actualGeneratorUri = feedDataLog.getGeneratorUri();
        String actualId = feedDataLog.getId();
        String actualLinkNextArchive = feedDataLog.getLinkNextArchive();
        String actualLinkPrevArchive = feedDataLog.getLinkPrevArchive();
        String actualLinkSelf = feedDataLog.getLinkSelf();
        String actualLinkVia = feedDataLog.getLinkVia();
        String actualTitle = feedDataLog.getTitle();
        feedDataLog.getUpdated();

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
        assertEquals("https://example.org/example", actualEntryContentPatientFull_URL);
        assertTrue(actualEntrySuccess);
        assertTrue(actualFeedSuccess);
    }
}
