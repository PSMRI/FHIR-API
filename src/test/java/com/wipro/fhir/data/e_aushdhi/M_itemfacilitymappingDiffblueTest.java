package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class M_itemfacilitymappingDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link M_itemfacilitymapping}
     *   <li>{@link M_itemfacilitymapping#setCreatedBy(String)}
     *   <li>{@link M_itemfacilitymapping#setCreatedDate(Date)}
     *   <li>{@link M_itemfacilitymapping#setDeleted(Boolean)}
     *   <li>{@link M_itemfacilitymapping#setFacilityID(Integer)}
     *   <li>{@link M_itemfacilitymapping#setItemFacilityMapID(Integer)}
     *   <li>{@link M_itemfacilitymapping#setItemID(Integer)}
     *   <li>{@link M_itemfacilitymapping#setLastModDate(Date)}
     *   <li>{@link M_itemfacilitymapping#setMappingType(String)}
     *   <li>{@link M_itemfacilitymapping#setModifiedBy(String)}
     *   <li>{@link M_itemfacilitymapping#setProviderServiceMapID(Integer)}
     *   <li>{@link M_itemfacilitymapping#setStatus(String)}
     *   <li>{@link M_itemfacilitymapping#getCreatedBy()}
     *   <li>{@link M_itemfacilitymapping#getCreatedDate()}
     *   <li>{@link M_itemfacilitymapping#getDeleted()}
     *   <li>{@link M_itemfacilitymapping#getFacilityID()}
     *   <li>{@link M_itemfacilitymapping#getItemFacilityMapID()}
     *   <li>{@link M_itemfacilitymapping#getItemID()}
     *   <li>{@link M_itemfacilitymapping#getLastModDate()}
     *   <li>{@link M_itemfacilitymapping#getMappingType()}
     *   <li>{@link M_itemfacilitymapping#getModifiedBy()}
     *   <li>{@link M_itemfacilitymapping#getProviderServiceMapID()}
     *   <li>{@link M_itemfacilitymapping#getStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange and Act
        M_itemfacilitymapping actualM_itemfacilitymapping = new M_itemfacilitymapping();
        actualM_itemfacilitymapping.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        actualM_itemfacilitymapping.setCreatedDate(createdDate);
        actualM_itemfacilitymapping.setDeleted(true);
        actualM_itemfacilitymapping.setFacilityID(1);
        actualM_itemfacilitymapping.setItemFacilityMapID(1);
        actualM_itemfacilitymapping.setItemID(1);
        Date lastModDate = mock(Date.class);
        actualM_itemfacilitymapping.setLastModDate(lastModDate);
        actualM_itemfacilitymapping.setMappingType("Mapping Type");
        actualM_itemfacilitymapping.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        actualM_itemfacilitymapping.setProviderServiceMapID(1);
        actualM_itemfacilitymapping.setStatus("Status");
        String actualCreatedBy = actualM_itemfacilitymapping.getCreatedBy();
        Date actualCreatedDate = actualM_itemfacilitymapping.getCreatedDate();
        Boolean actualDeleted = actualM_itemfacilitymapping.getDeleted();
        Integer actualFacilityID = actualM_itemfacilitymapping.getFacilityID();
        Integer actualItemFacilityMapID = actualM_itemfacilitymapping.getItemFacilityMapID();
        Integer actualItemID = actualM_itemfacilitymapping.getItemID();
        Date actualLastModDate = actualM_itemfacilitymapping.getLastModDate();
        String actualMappingType = actualM_itemfacilitymapping.getMappingType();
        String actualModifiedBy = actualM_itemfacilitymapping.getModifiedBy();
        Integer actualProviderServiceMapID = actualM_itemfacilitymapping.getProviderServiceMapID();

        // Assert that nothing has changed
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Mapping Type", actualMappingType);
        assertEquals("Status", actualM_itemfacilitymapping.getStatus());
        assertEquals(1, actualFacilityID.intValue());
        assertEquals(1, actualItemFacilityMapID.intValue());
        assertEquals(1, actualItemID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
