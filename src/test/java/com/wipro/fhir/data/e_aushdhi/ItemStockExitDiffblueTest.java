package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.Test;

class ItemStockExitDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ItemStockExit#setCreatedBy(String)}
     *   <li>{@link ItemStockExit#setCreatedDate(Date)}
     *   <li>{@link ItemStockExit#setDeleted(Boolean)}
     *   <li>{@link ItemStockExit#setExitType(String)}
     *   <li>{@link ItemStockExit#setExitTypeID(Long)}
     *   <li>{@link ItemStockExit#setFacilityID(Integer)}
     *   <li>{@link ItemStockExit#setItemStockEntryID(Integer)}
     *   <li>{@link ItemStockExit#setItemStockExitID(Long)}
     *   <li>{@link ItemStockExit#setLastModDate(Date)}
     *   <li>{@link ItemStockExit#setModifiedBy(String)}
     *   <li>{@link ItemStockExit#setParkingPlaceID(Long)}
     *   <li>{@link ItemStockExit#setProcessed(Character)}
     *   <li>{@link ItemStockExit#setQuantity(Integer)}
     *   <li>{@link ItemStockExit#setSyncFacilityID(Integer)}
     *   <li>{@link ItemStockExit#setVanID(Long)}
     *   <li>{@link ItemStockExit#setVanSerialNo(Long)}
     *   <li>{@link ItemStockExit#getCreatedBy()}
     *   <li>{@link ItemStockExit#getCreatedDate()}
     *   <li>{@link ItemStockExit#getDeleted()}
     *   <li>{@link ItemStockExit#getExitType()}
     *   <li>{@link ItemStockExit#getExitTypeID()}
     *   <li>{@link ItemStockExit#getFacilityID()}
     *   <li>{@link ItemStockExit#getItemStockEntryID()}
     *   <li>{@link ItemStockExit#getItemStockExitID()}
     *   <li>{@link ItemStockExit#getLastModDate()}
     *   <li>{@link ItemStockExit#getModifiedBy()}
     *   <li>{@link ItemStockExit#getParkingPlaceID()}
     *   <li>{@link ItemStockExit#getProcessed()}
     *   <li>{@link ItemStockExit#getQuantity()}
     *   <li>{@link ItemStockExit#getSyncFacilityID()}
     *   <li>{@link ItemStockExit#getVanID()}
     *   <li>{@link ItemStockExit#getVanSerialNo()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ItemStockExit itemStockExit = new ItemStockExit();

        // Act
        itemStockExit.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        itemStockExit.setCreatedDate(createdDate);
        itemStockExit.setDeleted(true);
        itemStockExit.setExitType("Exit Type");
        itemStockExit.setExitTypeID(1L);
        itemStockExit.setFacilityID(1);
        itemStockExit.setItemStockEntryID(1);
        itemStockExit.setItemStockExitID(1L);
        Date lastModDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        itemStockExit.setLastModDate(lastModDate);
        itemStockExit.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        itemStockExit.setParkingPlaceID(1L);
        itemStockExit.setProcessed('A');
        itemStockExit.setQuantity(1);
        itemStockExit.setSyncFacilityID(1);
        itemStockExit.setVanID(1L);
        itemStockExit.setVanSerialNo(1L);
        String actualCreatedBy = itemStockExit.getCreatedBy();
        Date actualCreatedDate = itemStockExit.getCreatedDate();
        Boolean actualDeleted = itemStockExit.getDeleted();
        String actualExitType = itemStockExit.getExitType();
        Long actualExitTypeID = itemStockExit.getExitTypeID();
        Integer actualFacilityID = itemStockExit.getFacilityID();
        Integer actualItemStockEntryID = itemStockExit.getItemStockEntryID();
        Long actualItemStockExitID = itemStockExit.getItemStockExitID();
        Date actualLastModDate = itemStockExit.getLastModDate();
        String actualModifiedBy = itemStockExit.getModifiedBy();
        Long actualParkingPlaceID = itemStockExit.getParkingPlaceID();
        Character actualProcessed = itemStockExit.getProcessed();
        Integer actualQuantity = itemStockExit.getQuantity();
        Integer actualSyncFacilityID = itemStockExit.getSyncFacilityID();
        Long actualVanID = itemStockExit.getVanID();
        Long actualVanSerialNo = itemStockExit.getVanSerialNo();

        // Assert that nothing has changed
        assertEquals("Exit Type", actualExitType);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualFacilityID.intValue());
        assertEquals(1, actualItemStockEntryID.intValue());
        assertEquals(1, actualQuantity.intValue());
        assertEquals(1, actualSyncFacilityID.intValue());
        assertEquals(1L, actualExitTypeID.longValue());
        assertEquals(1L, actualItemStockExitID.longValue());
        assertEquals(1L, actualParkingPlaceID.longValue());
        assertEquals(1L, actualVanID.longValue());
        assertEquals(1L, actualVanSerialNo.longValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
