package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class M_VanDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_Van#setCountryID(Integer)}
     *   <li>{@link M_Van#setCreatedBy(String)}
     *   <li>{@link M_Van#setCreatedDate(Date)}
     *   <li>{@link M_Van#setDeleted(Boolean)}
     *   <li>{@link M_Van#setLastModDate(Date)}
     *   <li>{@link M_Van#setModifiedBy(String)}
     *   <li>{@link M_Van#setParkingPlaceID(Integer)}
     *   <li>{@link M_Van#setProcessed(String)}
     *   <li>{@link M_Van#setProviderServiceMapID(Integer)}
     *   <li>{@link M_Van#setStateID(Integer)}
     *   <li>{@link M_Van#setVanName(String)}
     *   <li>{@link M_Van#setVanTypeID(Integer)}
     *   <li>{@link M_Van#setVehicalNo(String)}
     *   <li>{@link M_Van#getCountryID()}
     *   <li>{@link M_Van#getCreatedBy()}
     *   <li>{@link M_Van#getCreatedDate()}
     *   <li>{@link M_Van#getDeleted()}
     *   <li>{@link M_Van#getLastModDate()}
     *   <li>{@link M_Van#getModifiedBy()}
     *   <li>{@link M_Van#getParkingPlaceID()}
     *   <li>{@link M_Van#getProcessed()}
     *   <li>{@link M_Van#getProviderServiceMapID()}
     *   <li>{@link M_Van#getStateID()}
     *   <li>{@link M_Van#getVanID()}
     *   <li>{@link M_Van#getVanName()}
     *   <li>{@link M_Van#getVanTypeID()}
     *   <li>{@link M_Van#getVehicalNo()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_Van m_Van = new M_Van();

        // Act
        m_Van.setCountryID(1);
        m_Van.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        m_Van.setCreatedDate(createdDate);
        m_Van.setDeleted(true);
        Date lastModDate = mock(Date.class);
        m_Van.setLastModDate(lastModDate);
        m_Van.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Van.setParkingPlaceID(1);
        m_Van.setProcessed("Processed");
        m_Van.setProviderServiceMapID(1);
        m_Van.setStateID(1);
        m_Van.setVanName("Van Name");
        m_Van.setVanTypeID(1);
        m_Van.setVehicalNo("Vehical No");
        Integer actualCountryID = m_Van.getCountryID();
        String actualCreatedBy = m_Van.getCreatedBy();
        Date actualCreatedDate = m_Van.getCreatedDate();
        Boolean actualDeleted = m_Van.getDeleted();
        Date actualLastModDate = m_Van.getLastModDate();
        String actualModifiedBy = m_Van.getModifiedBy();
        Integer actualParkingPlaceID = m_Van.getParkingPlaceID();
        String actualProcessed = m_Van.getProcessed();
        Integer actualProviderServiceMapID = m_Van.getProviderServiceMapID();
        Integer actualStateID = m_Van.getStateID();
        m_Van.getVanID();
        String actualVanName = m_Van.getVanName();
        Integer actualVanTypeID = m_Van.getVanTypeID();

        // Assert that nothing has changed
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Processed", actualProcessed);
        assertEquals("Van Name", actualVanName);
        assertEquals("Vehical No", m_Van.getVehicalNo());
        assertEquals(1, actualCountryID.intValue());
        assertEquals(1, actualParkingPlaceID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1, actualStateID.intValue());
        assertEquals(1, actualVanTypeID.intValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
