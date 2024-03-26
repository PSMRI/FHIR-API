package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {M_Uom.class})
@ExtendWith(SpringExtension.class)
class M_UomDiffblueTest {
    @Autowired
    private M_Uom m_Uom;

    /**
     * Method under test: {@link M_Uom#getuOMID()}
     */
    @Test
    void testGetuOMID() {
        // Arrange, Act and Assert
        assertNull((new M_Uom()).getuOMID());
    }

    /**
     * Method under test: {@link M_Uom#getuOMID()}
     */
    @Test
    void testGetuOMID2() {
        // Arrange
        M_Uom m_Uom2 = new M_Uom();
        m_Uom2.setCreatedDate(mock(Date.class));

        // Act and Assert
        assertNull(m_Uom2.getuOMID());
    }

    /**
     * Method under test: {@link M_Uom#getuOMName()}
     */
    @Test
    void testGetuOMName() {
        // Arrange, Act and Assert
        assertNull((new M_Uom()).getuOMName());
    }

    /**
     * Method under test: {@link M_Uom#getuOMName()}
     */
    @Test
    void testGetuOMName2() {
        // Arrange
        M_Uom m_Uom2 = new M_Uom();
        m_Uom2.setCreatedDate(mock(Date.class));

        // Act and Assert
        assertNull(m_Uom2.getuOMName());
    }

    /**
     * Method under test: {@link M_Uom#getuOMDesc()}
     */
    @Test
    void testGetuOMDesc() {
        // Arrange, Act and Assert
        assertNull((new M_Uom()).getuOMDesc());
    }

    /**
     * Method under test: {@link M_Uom#getuOMDesc()}
     */
    @Test
    void testGetuOMDesc2() {
        // Arrange
        M_Uom m_Uom2 = new M_Uom();
        m_Uom2.setCreatedDate(mock(Date.class));

        // Act and Assert
        assertNull(m_Uom2.getuOMDesc());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_Uom#setCreatedBy(String)}
     *   <li>{@link M_Uom#setCreatedDate(Date)}
     *   <li>{@link M_Uom#setDeleted(Boolean)}
     *   <li>{@link M_Uom#setLastModDate(Date)}
     *   <li>{@link M_Uom#setModifiedBy(String)}
     *   <li>{@link M_Uom#setProcessed(Character)}
     *   <li>{@link M_Uom#setProviderServiceMapID(Integer)}
     *   <li>{@link M_Uom#setStatus(String)}
     *   <li>{@link M_Uom#setuOMCode(String)}
     *   <li>{@link M_Uom#setuOMDesc(String)}
     *   <li>{@link M_Uom#setuOMID(Integer)}
     *   <li>{@link M_Uom#setuOMName(String)}
     *   <li>{@link M_Uom#getCreatedBy()}
     *   <li>{@link M_Uom#getCreatedDate()}
     *   <li>{@link M_Uom#getDeleted()}
     *   <li>{@link M_Uom#getLastModDate()}
     *   <li>{@link M_Uom#getModifiedBy()}
     *   <li>{@link M_Uom#getProcessed()}
     *   <li>{@link M_Uom#getProviderServiceMapID()}
     *   <li>{@link M_Uom#getStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_Uom m_Uom = new M_Uom();

        // Act
        m_Uom.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        m_Uom.setCreatedDate(createdDate);
        m_Uom.setDeleted(true);
        Date lastModDate = mock(Date.class);
        m_Uom.setLastModDate(lastModDate);
        m_Uom.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_Uom.setProcessed('A');
        m_Uom.setProviderServiceMapID(1);
        m_Uom.setStatus("Status");
        m_Uom.setuOMCode("U OMCode");
        m_Uom.setuOMDesc("U OMDesc");
        m_Uom.setuOMID(1);
        m_Uom.setuOMName("U OMName");
        String actualCreatedBy = m_Uom.getCreatedBy();
        Date actualCreatedDate = m_Uom.getCreatedDate();
        Boolean actualDeleted = m_Uom.getDeleted();
        Date actualLastModDate = m_Uom.getLastModDate();
        String actualModifiedBy = m_Uom.getModifiedBy();
        Character actualProcessed = m_Uom.getProcessed();
        Integer actualProviderServiceMapID = m_Uom.getProviderServiceMapID();

        // Assert that nothing has changed
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Status", m_Uom.getStatus());
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }

    /**
     * Method under test: {@link M_Uom#getuOMCode()}
     */
    @Test
    void testGetuOMCode() {
        // Arrange, Act and Assert
        assertNull((new M_Uom()).getuOMCode());
    }

    /**
     * Method under test: {@link M_Uom#getuOMCode()}
     */
    @Test
    void testGetuOMCode2() {
        // Arrange
        M_Uom m_Uom2 = new M_Uom();
        m_Uom2.setCreatedDate(mock(Date.class));

        // Act and Assert
        assertNull(m_Uom2.getuOMCode());
    }
}
