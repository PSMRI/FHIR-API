package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class SMSNotifyDiffblueTest {
    /**
     * Method under test: {@link SMSNotify#SMSNotify(String, String, Notification)}
     */
    @Test
    void testNewSMSNotify() {
        // Arrange
        Notification notification = new Notification("6625550144", new HIP("Name", "42"));

        // Act and Assert
        Notification notification2 = (new SMSNotify("42", "Timestamp", notification)).notification;
        assertEquals("6625550144", notification2.getPhoneNo());
        HIP expectedHip = notification.hip;
        assertSame(expectedHip, notification2.getHip());
    }
}
