package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class ItemMasterDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link ItemMaster#setComposition(String)}
     *   <li>{@link ItemMaster#setCreatedBy(String)}
     *   <li>{@link ItemMaster#setCreatedDate(Date)}
     *   <li>{@link ItemMaster#setDeleted(Boolean)}
     *   <li>{@link ItemMaster#setDiscontinued(Boolean)}
     *   <li>{@link ItemMaster#setIsEDL(Boolean)}
     *   <li>{@link ItemMaster#setIsEaushadi(Boolean)}
     *   <li>{@link ItemMaster#setIsMedical(Boolean)}
     *   <li>{@link ItemMaster#setIsScheduledDrug(Boolean)}
     *   <li>{@link ItemMaster#setItemCategoryID(Integer)}
     *   <li>{@link ItemMaster#setItemCode(String)}
     *   <li>{@link ItemMaster#setItemDesc(String)}
     *   <li>{@link ItemMaster#setItemFormID(Integer)}
     *   <li>{@link ItemMaster#setItemID(Integer)}
     *   <li>{@link ItemMaster#setItemName(String)}
     *   <li>{@link ItemMaster#setLastModDate(Date)}
     *   <li>{@link ItemMaster#setManufacturerID(Integer)}
     *   <li>{@link ItemMaster#setModifiedBy(String)}
     *   <li>{@link ItemMaster#setPharmacologyCategoryID(Integer)}
     *   <li>{@link ItemMaster#setProcessed(Character)}
     *   <li>{@link ItemMaster#setProviderServiceMapID(Integer)}
     *   <li>{@link ItemMaster#setRouteID(Integer)}
     *   <li>{@link ItemMaster#setSctCode(String)}
     *   <li>{@link ItemMaster#setSctTerm(String)}
     *   <li>{@link ItemMaster#setStatus(String)}
     *   <li>{@link ItemMaster#setStrength(String)}
     *   <li>{@link ItemMaster#setUomID(Integer)}
     *   <li>{@link ItemMaster#getComposition()}
     *   <li>{@link ItemMaster#getCreatedBy()}
     *   <li>{@link ItemMaster#getCreatedDate()}
     *   <li>{@link ItemMaster#getDeleted()}
     *   <li>{@link ItemMaster#getDiscontinued()}
     *   <li>{@link ItemMaster#getIsEDL()}
     *   <li>{@link ItemMaster#getIsEaushadi()}
     *   <li>{@link ItemMaster#getIsMedical()}
     *   <li>{@link ItemMaster#getIsScheduledDrug()}
     *   <li>{@link ItemMaster#getItemCategoryID()}
     *   <li>{@link ItemMaster#getItemCode()}
     *   <li>{@link ItemMaster#getItemDesc()}
     *   <li>{@link ItemMaster#getItemFormID()}
     *   <li>{@link ItemMaster#getItemID()}
     *   <li>{@link ItemMaster#getItemName()}
     *   <li>{@link ItemMaster#getLastModDate()}
     *   <li>{@link ItemMaster#getManufacturerID()}
     *   <li>{@link ItemMaster#getModifiedBy()}
     *   <li>{@link ItemMaster#getPharmacologyCategoryID()}
     *   <li>{@link ItemMaster#getProcessed()}
     *   <li>{@link ItemMaster#getProviderServiceMapID()}
     *   <li>{@link ItemMaster#getRouteID()}
     *   <li>{@link ItemMaster#getSctCode()}
     *   <li>{@link ItemMaster#getSctTerm()}
     *   <li>{@link ItemMaster#getStatus()}
     *   <li>{@link ItemMaster#getStrength()}
     *   <li>{@link ItemMaster#getUomID()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        ItemMaster itemMaster = new ItemMaster();

        // Act
        itemMaster.setComposition("Composition");
        itemMaster.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        itemMaster.setCreatedDate(createdDate);
        itemMaster.setDeleted(true);
        itemMaster.setDiscontinued(true);
        itemMaster.setIsEDL(true);
        itemMaster.setIsEaushadi(true);
        itemMaster.setIsMedical(true);
        itemMaster.setIsScheduledDrug(true);
        itemMaster.setItemCategoryID(1);
        itemMaster.setItemCode("Item Code");
        itemMaster.setItemDesc("Item Desc");
        itemMaster.setItemFormID(1);
        itemMaster.setItemID(1);
        itemMaster.setItemName("Item Name");
        Date lastModDate = mock(Date.class);
        itemMaster.setLastModDate(lastModDate);
        itemMaster.setManufacturerID(1);
        itemMaster.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        itemMaster.setPharmacologyCategoryID(1);
        itemMaster.setProcessed('A');
        itemMaster.setProviderServiceMapID(1);
        itemMaster.setRouteID(1);
        itemMaster.setSctCode("Sct Code");
        itemMaster.setSctTerm("Sct Term");
        itemMaster.setStatus("Status");
        itemMaster.setStrength("Strength");
        itemMaster.setUomID(1);
        String actualComposition = itemMaster.getComposition();
        String actualCreatedBy = itemMaster.getCreatedBy();
        Date actualCreatedDate = itemMaster.getCreatedDate();
        Boolean actualDeleted = itemMaster.getDeleted();
        Boolean actualDiscontinued = itemMaster.getDiscontinued();
        Boolean actualIsEDL = itemMaster.getIsEDL();
        Boolean actualIsEaushadi = itemMaster.getIsEaushadi();
        Boolean actualIsMedical = itemMaster.getIsMedical();
        Boolean actualIsScheduledDrug = itemMaster.getIsScheduledDrug();
        Integer actualItemCategoryID = itemMaster.getItemCategoryID();
        String actualItemCode = itemMaster.getItemCode();
        String actualItemDesc = itemMaster.getItemDesc();
        Integer actualItemFormID = itemMaster.getItemFormID();
        Integer actualItemID = itemMaster.getItemID();
        String actualItemName = itemMaster.getItemName();
        Date actualLastModDate = itemMaster.getLastModDate();
        Integer actualManufacturerID = itemMaster.getManufacturerID();
        String actualModifiedBy = itemMaster.getModifiedBy();
        Integer actualPharmacologyCategoryID = itemMaster.getPharmacologyCategoryID();
        Character actualProcessed = itemMaster.getProcessed();
        Integer actualProviderServiceMapID = itemMaster.getProviderServiceMapID();
        Integer actualRouteID = itemMaster.getRouteID();
        String actualSctCode = itemMaster.getSctCode();
        String actualSctTerm = itemMaster.getSctTerm();
        String actualStatus = itemMaster.getStatus();
        String actualStrength = itemMaster.getStrength();
        Integer actualUomID = itemMaster.getUomID();

        // Assert that nothing has changed
        assertEquals("Composition", actualComposition);
        assertEquals("Item Code", actualItemCode);
        assertEquals("Item Desc", actualItemDesc);
        assertEquals("Item Name", actualItemName);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Sct Code", actualSctCode);
        assertEquals("Sct Term", actualSctTerm);
        assertEquals("Status", actualStatus);
        assertEquals("Strength", actualStrength);
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualItemCategoryID.intValue());
        assertEquals(1, actualItemFormID.intValue());
        assertEquals(1, actualItemID.intValue());
        assertEquals(1, actualManufacturerID.intValue());
        assertEquals(1, actualPharmacologyCategoryID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1, actualRouteID.intValue());
        assertEquals(1, actualUomID.intValue());
        assertTrue(actualDeleted);
        assertTrue(actualDiscontinued);
        assertTrue(actualIsEDL);
        assertTrue(actualIsEaushadi);
        assertTrue(actualIsMedical);
        assertTrue(actualIsScheduledDrug);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
