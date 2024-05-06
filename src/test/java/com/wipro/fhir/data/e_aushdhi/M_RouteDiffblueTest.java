package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class M_RouteDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_Route#setCreatedBy(String)}
     *   <li>{@link M_Route#setCreatedDate(Date)}
     *   <li>{@link M_Route#setDeleted(Boolean)}
     *   <li>{@link M_Route#setLastModDate(Date)}
     *   <li>{@link M_Route#setModifiedBy(String)}
     *   <li>{@link M_Route#setProcessed(Character)}
     *   <li>{@link M_Route#setProviderServiceMapID(Integer)}
     *   <li>{@link M_Route#setRouteCode(String)}
     *   <li>{@link M_Route#setRouteDesc(String)}
     *   <li>{@link M_Route#setRouteID(Integer)}
     *   <li>{@link M_Route#setRouteName(String)}
     *   <li>{@link M_Route#getCreatedBy()}
     *   <li>{@link M_Route#getCreatedDate()}
     *   <li>{@link M_Route#getDeleted()}
     *   <li>{@link M_Route#getLastModDate()}
     *   <li>{@link M_Route#getModifiedBy()}
     *   <li>{@link M_Route#getProcessed()}
     *   <li>{@link M_Route#getProviderServiceMapID()}
     *   <li>{@link M_Route#getRouteCode()}
     *   <li>{@link M_Route#getRouteDesc()}
     *   <li>{@link M_Route#getRouteID()}
     *   <li>{@link M_Route#getRouteName()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_Route m_Route = new M_Route();

        // Act
        m_Route.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        m_Route.setCreatedDate(createdDate);
        m_Route.setDeleted(true);
        Date lastModDate = mock(Date.class);
        m_Route.setLastModDate(lastModDate);
        m_Route.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Route.setProcessed('A');
        m_Route.setProviderServiceMapID(1);
        m_Route.setRouteCode("Route Code");
        m_Route.setRouteDesc("Route Desc");
        m_Route.setRouteID(1);
        m_Route.setRouteName("Route Name");
        String actualCreatedBy = m_Route.getCreatedBy();
        Date actualCreatedDate = m_Route.getCreatedDate();
        Boolean actualDeleted = m_Route.getDeleted();
        Date actualLastModDate = m_Route.getLastModDate();
        String actualModifiedBy = m_Route.getModifiedBy();
        Character actualProcessed = m_Route.getProcessed();
        Integer actualProviderServiceMapID = m_Route.getProviderServiceMapID();
        String actualRouteCode = m_Route.getRouteCode();
        String actualRouteDesc = m_Route.getRouteDesc();
        Integer actualRouteID = m_Route.getRouteID();

        // Assert that nothing has changed
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Route Code", actualRouteCode);
        assertEquals("Route Desc", actualRouteDesc);
        assertEquals("Route Name", m_Route.getRouteName());
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1, actualRouteID.intValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
