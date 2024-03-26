package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class M_ItemCategoryDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_ItemCategory#setAlertBeforeDays(Integer)}
     *   <li>{@link M_ItemCategory#setCreatedBy(String)}
     *   <li>{@link M_ItemCategory#setCreatedDate(Date)}
     *   <li>{@link M_ItemCategory#setDeleted(Boolean)}
     *   <li>{@link M_ItemCategory#setIssueType(String)}
     *   <li>{@link M_ItemCategory#setItemCategoryCode(String)}
     *   <li>{@link M_ItemCategory#setItemCategoryDesc(String)}
     *   <li>{@link M_ItemCategory#setItemCategoryID(Integer)}
     *   <li>{@link M_ItemCategory#setItemCategoryName(String)}
     *   <li>{@link M_ItemCategory#setLastModDate(Date)}
     *   <li>{@link M_ItemCategory#setModifiedBy(String)}
     *   <li>{@link M_ItemCategory#setProcessed(Character)}
     *   <li>{@link M_ItemCategory#setProviderServiceMapID(Integer)}
     *   <li>{@link M_ItemCategory#setStatus(String)}
     *   <li>{@link M_ItemCategory#getAlertBeforeDays()}
     *   <li>{@link M_ItemCategory#getCreatedBy()}
     *   <li>{@link M_ItemCategory#getCreatedDate()}
     *   <li>{@link M_ItemCategory#getDeleted()}
     *   <li>{@link M_ItemCategory#getIssueType()}
     *   <li>{@link M_ItemCategory#getItemCategoryCode()}
     *   <li>{@link M_ItemCategory#getItemCategoryDesc()}
     *   <li>{@link M_ItemCategory#getItemCategoryID()}
     *   <li>{@link M_ItemCategory#getItemCategoryName()}
     *   <li>{@link M_ItemCategory#getLastModDate()}
     *   <li>{@link M_ItemCategory#getModifiedBy()}
     *   <li>{@link M_ItemCategory#getProcessed()}
     *   <li>{@link M_ItemCategory#getProviderServiceMapID()}
     *   <li>{@link M_ItemCategory#getStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_ItemCategory m_ItemCategory = new M_ItemCategory();

        // Act
        m_ItemCategory.setAlertBeforeDays(1);
        m_ItemCategory.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        m_ItemCategory.setCreatedDate(createdDate);
        m_ItemCategory.setDeleted(true);
        m_ItemCategory.setIssueType("Issue Type");
        m_ItemCategory.setItemCategoryCode("Item Category Code");
        m_ItemCategory.setItemCategoryDesc("Item Category Desc");
        m_ItemCategory.setItemCategoryID(1);
        m_ItemCategory.setItemCategoryName("Item Category Name");
        Date lastModDate = mock(Date.class);
        m_ItemCategory.setLastModDate(lastModDate);
        m_ItemCategory.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_ItemCategory.setProcessed('A');
        m_ItemCategory.setProviderServiceMapID(1);
        m_ItemCategory.setStatus("Status");
        Integer actualAlertBeforeDays = m_ItemCategory.getAlertBeforeDays();
        String actualCreatedBy = m_ItemCategory.getCreatedBy();
        Date actualCreatedDate = m_ItemCategory.getCreatedDate();
        Boolean actualDeleted = m_ItemCategory.getDeleted();
        String actualIssueType = m_ItemCategory.getIssueType();
        String actualItemCategoryCode = m_ItemCategory.getItemCategoryCode();
        String actualItemCategoryDesc = m_ItemCategory.getItemCategoryDesc();
        Integer actualItemCategoryID = m_ItemCategory.getItemCategoryID();
        String actualItemCategoryName = m_ItemCategory.getItemCategoryName();
        Date actualLastModDate = m_ItemCategory.getLastModDate();
        String actualModifiedBy = m_ItemCategory.getModifiedBy();
        Character actualProcessed = m_ItemCategory.getProcessed();
        Integer actualProviderServiceMapID = m_ItemCategory.getProviderServiceMapID();

        // Assert that nothing has changed
        assertEquals("Issue Type", actualIssueType);
        assertEquals("Item Category Code", actualItemCategoryCode);
        assertEquals("Item Category Desc", actualItemCategoryDesc);
        assertEquals("Item Category Name", actualItemCategoryName);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Status", m_ItemCategory.getStatus());
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualAlertBeforeDays.intValue());
        assertEquals(1, actualItemCategoryID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
