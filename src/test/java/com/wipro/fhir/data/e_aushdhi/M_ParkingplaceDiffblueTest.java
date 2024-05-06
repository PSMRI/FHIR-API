package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class M_ParkingplaceDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_Parkingplace#setAreaHQAddress(String)}
     *   <li>{@link M_Parkingplace#setCountryID(Integer)}
     *   <li>{@link M_Parkingplace#setCreatedBy(String)}
     *   <li>{@link M_Parkingplace#setCreatedDate(Date)}
     *   <li>{@link M_Parkingplace#setDeleted(Boolean)}
     *   <li>{@link M_Parkingplace#setDistrictBlockID(Integer)}
     *   <li>{@link M_Parkingplace#setDistrictBranchID(Integer)}
     *   <li>{@link M_Parkingplace#setDistrictID(Integer)}
     *   <li>{@link M_Parkingplace#setFacilityID(Integer)}
     *   <li>{@link M_Parkingplace#setIsFacility(Boolean)}
     *   <li>{@link M_Parkingplace#setLastModDate(Date)}
     *   <li>{@link M_Parkingplace#setModifiedBy(String)}
     *   <li>{@link M_Parkingplace#setParkingPlaceDesc(String)}
     *   <li>{@link M_Parkingplace#setParkingPlaceID(Integer)}
     *   <li>{@link M_Parkingplace#setParkingPlaceName(String)}
     *   <li>{@link M_Parkingplace#setProcessed(String)}
     *   <li>{@link M_Parkingplace#setProviderServiceMapID(Integer)}
     *   <li>{@link M_Parkingplace#setStateID(Integer)}
     *   <li>{@link M_Parkingplace#setZoneID(Integer)}
     *   <li>{@link M_Parkingplace#getAreaHQAddress()}
     *   <li>{@link M_Parkingplace#getCountryID()}
     *   <li>{@link M_Parkingplace#getCreatedBy()}
     *   <li>{@link M_Parkingplace#getCreatedDate()}
     *   <li>{@link M_Parkingplace#getDeleted()}
     *   <li>{@link M_Parkingplace#getDistrictBlockID()}
     *   <li>{@link M_Parkingplace#getDistrictBranchID()}
     *   <li>{@link M_Parkingplace#getDistrictID()}
     *   <li>{@link M_Parkingplace#getFacilityID()}
     *   <li>{@link M_Parkingplace#getIsFacility()}
     *   <li>{@link M_Parkingplace#getLastModDate()}
     *   <li>{@link M_Parkingplace#getModifiedBy()}
     *   <li>{@link M_Parkingplace#getParkingPlaceDesc()}
     *   <li>{@link M_Parkingplace#getParkingPlaceID()}
     *   <li>{@link M_Parkingplace#getParkingPlaceName()}
     *   <li>{@link M_Parkingplace#getProcessed()}
     *   <li>{@link M_Parkingplace#getProviderServiceMapID()}
     *   <li>{@link M_Parkingplace#getStateID()}
     *   <li>{@link M_Parkingplace#getZoneID()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_Parkingplace m_Parkingplace = new M_Parkingplace();

        // Act
        m_Parkingplace.setAreaHQAddress("42 Main St");
        m_Parkingplace.setCountryID(1);
        m_Parkingplace.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        m_Parkingplace.setCreatedDate(createdDate);
        m_Parkingplace.setDeleted(true);
        m_Parkingplace.setDistrictBlockID(1);
        m_Parkingplace.setDistrictBranchID(1);
        m_Parkingplace.setDistrictID(1);
        m_Parkingplace.setFacilityID(1);
        m_Parkingplace.setIsFacility(true);
        Date lastModDate = mock(Date.class);
        m_Parkingplace.setLastModDate(lastModDate);
        m_Parkingplace.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Parkingplace.setParkingPlaceDesc("Parking Place Desc");
        m_Parkingplace.setParkingPlaceID(1);
        m_Parkingplace.setParkingPlaceName("Parking Place Name");
        m_Parkingplace.setProcessed("Processed");
        m_Parkingplace.setProviderServiceMapID(1);
        m_Parkingplace.setStateID(1);
        m_Parkingplace.setZoneID(1);
        String actualAreaHQAddress = m_Parkingplace.getAreaHQAddress();
        Integer actualCountryID = m_Parkingplace.getCountryID();
        String actualCreatedBy = m_Parkingplace.getCreatedBy();
        Date actualCreatedDate = m_Parkingplace.getCreatedDate();
        Boolean actualDeleted = m_Parkingplace.getDeleted();
        Integer actualDistrictBlockID = m_Parkingplace.getDistrictBlockID();
        Integer actualDistrictBranchID = m_Parkingplace.getDistrictBranchID();
        Integer actualDistrictID = m_Parkingplace.getDistrictID();
        Integer actualFacilityID = m_Parkingplace.getFacilityID();
        Boolean actualIsFacility = m_Parkingplace.getIsFacility();
        Date actualLastModDate = m_Parkingplace.getLastModDate();
        String actualModifiedBy = m_Parkingplace.getModifiedBy();
        String actualParkingPlaceDesc = m_Parkingplace.getParkingPlaceDesc();
        Integer actualParkingPlaceID = m_Parkingplace.getParkingPlaceID();
        String actualParkingPlaceName = m_Parkingplace.getParkingPlaceName();
        String actualProcessed = m_Parkingplace.getProcessed();
        Integer actualProviderServiceMapID = m_Parkingplace.getProviderServiceMapID();
        Integer actualStateID = m_Parkingplace.getStateID();
        Integer actualZoneID = m_Parkingplace.getZoneID();

        // Assert that nothing has changed
        assertEquals("42 Main St", actualAreaHQAddress);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Parking Place Desc", actualParkingPlaceDesc);
        assertEquals("Parking Place Name", actualParkingPlaceName);
        assertEquals("Processed", actualProcessed);
        assertEquals(1, actualCountryID.intValue());
        assertEquals(1, actualDistrictBlockID.intValue());
        assertEquals(1, actualDistrictBranchID.intValue());
        assertEquals(1, actualDistrictID.intValue());
        assertEquals(1, actualFacilityID.intValue());
        assertEquals(1, actualParkingPlaceID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1, actualStateID.intValue());
        assertEquals(1, actualZoneID.intValue());
        assertTrue(actualDeleted);
        assertTrue(actualIsFacility);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
