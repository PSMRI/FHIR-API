package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.jupiter.api.Test;

class ItemStockEntryDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link ItemStockEntry}
     *   <li>{@link ItemStockEntry#setBatchNo(String)}
     *   <li>{@link ItemStockEntry#setCreatedBy(String)}
     *   <li>{@link ItemStockEntry#setCreatedDate(Date)}
     *   <li>{@link ItemStockEntry#setDeleted(Boolean)}
     *   <li>{@link ItemStockEntry#setEntryType(String)}
     *   <li>{@link ItemStockEntry#setEntryTypeID(Integer)}
     *   <li>{@link ItemStockEntry#setExpiryDate(Date)}
     *   <li>{@link ItemStockEntry#setFacilityID(Integer)}
     *   <li>{@link ItemStockEntry#setItemID(Integer)}
     *   <li>{@link ItemStockEntry#setItemStockEntryID(Long)}
     *   <li>{@link ItemStockEntry#setLastModDate(Date)}
     *   <li>{@link ItemStockEntry#setModifiedBy(String)}
     *   <li>{@link ItemStockEntry#setParkingPlaceID(Integer)}
     *   <li>{@link ItemStockEntry#setProcessed(Character)}
     *   <li>{@link ItemStockEntry#setQuantity(Integer)}
     *   <li>{@link ItemStockEntry#setQuantityInHand(Integer)}
     *   <li>{@link ItemStockEntry#setSyncFacilityID(Integer)}
     *   <li>{@link ItemStockEntry#setTotalCostPrice(Double)}
     *   <li>{@link ItemStockEntry#setVanID(Integer)}
     *   <li>{@link ItemStockEntry#setVanSerialNo(Long)}
     *   <li>{@link ItemStockEntry#getBatchNo()}
     *   <li>{@link ItemStockEntry#getCreatedBy()}
     *   <li>{@link ItemStockEntry#getCreatedDate()}
     *   <li>{@link ItemStockEntry#getDeleted()}
     *   <li>{@link ItemStockEntry#getEntryType()}
     *   <li>{@link ItemStockEntry#getEntryTypeID()}
     *   <li>{@link ItemStockEntry#getExpiryDate()}
     *   <li>{@link ItemStockEntry#getFacilityID()}
     *   <li>{@link ItemStockEntry#getItemID()}
     *   <li>{@link ItemStockEntry#getItemStockEntryID()}
     *   <li>{@link ItemStockEntry#getLastModDate()}
     *   <li>{@link ItemStockEntry#getModifiedBy()}
     *   <li>{@link ItemStockEntry#getParkingPlaceID()}
     *   <li>{@link ItemStockEntry#getProcessed()}
     *   <li>{@link ItemStockEntry#getQuantity()}
     *   <li>{@link ItemStockEntry#getQuantityInHand()}
     *   <li>{@link ItemStockEntry#getSyncFacilityID()}
     *   <li>{@link ItemStockEntry#getTotalCostPrice()}
     *   <li>{@link ItemStockEntry#getVanID()}
     *   <li>{@link ItemStockEntry#getVanSerialNo()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        ItemStockEntry actualItemStockEntry = new ItemStockEntry();
        actualItemStockEntry.setBatchNo("Batch No");
        actualItemStockEntry.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualItemStockEntry.setCreatedDate(createdDate);
        actualItemStockEntry.setDeleted(true);
        actualItemStockEntry.setEntryType("Entry Type");
        actualItemStockEntry.setEntryTypeID(1);
        Date expiryDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualItemStockEntry.setExpiryDate(expiryDate);
        actualItemStockEntry.setFacilityID(1);
        actualItemStockEntry.setItemID(1);
        actualItemStockEntry.setItemStockEntryID(1L);
        Date lastModDate = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        actualItemStockEntry.setLastModDate(lastModDate);
        actualItemStockEntry.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        actualItemStockEntry.setParkingPlaceID(1);
        actualItemStockEntry.setProcessed('A');
        actualItemStockEntry.setQuantity(1);
        actualItemStockEntry.setQuantityInHand(1);
        actualItemStockEntry.setSyncFacilityID(1);
        actualItemStockEntry.setTotalCostPrice(10.0d);
        actualItemStockEntry.setVanID(1);
        actualItemStockEntry.setVanSerialNo(1L);
        String actualBatchNo = actualItemStockEntry.getBatchNo();
        String actualCreatedBy = actualItemStockEntry.getCreatedBy();
        Date actualCreatedDate = actualItemStockEntry.getCreatedDate();
        Boolean actualDeleted = actualItemStockEntry.getDeleted();
        String actualEntryType = actualItemStockEntry.getEntryType();
        Integer actualEntryTypeID = actualItemStockEntry.getEntryTypeID();
        Date actualExpiryDate = actualItemStockEntry.getExpiryDate();
        Integer actualFacilityID = actualItemStockEntry.getFacilityID();
        Integer actualItemID = actualItemStockEntry.getItemID();
        Long actualItemStockEntryID = actualItemStockEntry.getItemStockEntryID();
        Date actualLastModDate = actualItemStockEntry.getLastModDate();
        String actualModifiedBy = actualItemStockEntry.getModifiedBy();
        Integer actualParkingPlaceID = actualItemStockEntry.getParkingPlaceID();
        Character actualProcessed = actualItemStockEntry.getProcessed();
        Integer actualQuantity = actualItemStockEntry.getQuantity();
        Integer actualQuantityInHand = actualItemStockEntry.getQuantityInHand();
        Integer actualSyncFacilityID = actualItemStockEntry.getSyncFacilityID();
        Double actualTotalCostPrice = actualItemStockEntry.getTotalCostPrice();
        Integer actualVanID = actualItemStockEntry.getVanID();
        Long actualVanSerialNo = actualItemStockEntry.getVanSerialNo();

        // Assert that nothing has changed
        assertEquals("Batch No", actualBatchNo);
        assertEquals("Entry Type", actualEntryType);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualEntryTypeID.intValue());
        assertEquals(1, actualFacilityID.intValue());
        assertEquals(1, actualItemID.intValue());
        assertEquals(1, actualParkingPlaceID.intValue());
        assertEquals(1, actualQuantity.intValue());
        assertEquals(1, actualQuantityInHand.intValue());
        assertEquals(1, actualSyncFacilityID.intValue());
        assertEquals(1, actualVanID.intValue());
        assertEquals(10.0d, actualTotalCostPrice.doubleValue());
        assertEquals(1L, actualItemStockEntryID.longValue());
        assertEquals(1L, actualVanSerialNo.longValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(expiryDate, actualExpiryDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
