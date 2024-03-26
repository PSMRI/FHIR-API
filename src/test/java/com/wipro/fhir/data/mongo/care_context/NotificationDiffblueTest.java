package com.wipro.fhir.data.mongo.care_context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class NotificationDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Notification#Notification(String, HIP)}
     *   <li>{@link Notification#setHip(HIP)}
     *   <li>{@link Notification#setPhoneNo(String)}
     *   <li>{@link Notification#getHip()}
     *   <li>{@link Notification#getPhoneNo()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        Notification actualNotification = new Notification("6625550144", new HIP("Name", "42"));
        HIP hip = new HIP("Name", "42");

        actualNotification.setHip(hip);
        actualNotification.setPhoneNo("6625550144");
        HIP actualHip = actualNotification.getHip();

        // Assert that nothing has changed
        assertEquals("6625550144", actualNotification.getPhoneNo());
        assertSame(hip, actualHip);
    }
}
