package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class M_ItemFormDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_ItemForm#setCreatedBy(String)}
     *   <li>{@link M_ItemForm#setCreatedDate(Date)}
     *   <li>{@link M_ItemForm#setDeleted(Boolean)}
     *   <li>{@link M_ItemForm#setItemForm(String)}
     *   <li>{@link M_ItemForm#setItemFormCode(String)}
     *   <li>{@link M_ItemForm#setItemFormDesc(String)}
     *   <li>{@link M_ItemForm#setItemFormID(Integer)}
     *   <li>{@link M_ItemForm#setLastModDate(Date)}
     *   <li>{@link M_ItemForm#setModifiedBy(String)}
     *   <li>{@link M_ItemForm#setProcessed(Character)}
     *   <li>{@link M_ItemForm#setProviderServiceMapID(Integer)}
     *   <li>{@link M_ItemForm#getCreatedBy()}
     *   <li>{@link M_ItemForm#getCreatedDate()}
     *   <li>{@link M_ItemForm#getDeleted()}
     *   <li>{@link M_ItemForm#getItemForm()}
     *   <li>{@link M_ItemForm#getItemFormCode()}
     *   <li>{@link M_ItemForm#getItemFormDesc()}
     *   <li>{@link M_ItemForm#getItemFormID()}
     *   <li>{@link M_ItemForm#getLastModDate()}
     *   <li>{@link M_ItemForm#getModifiedBy()}
     *   <li>{@link M_ItemForm#getProcessed()}
     *   <li>{@link M_ItemForm#getProviderServiceMapID()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_ItemForm m_ItemForm = new M_ItemForm();

        // Act
        m_ItemForm.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Date createdDate = mock(Date.class);
        m_ItemForm.setCreatedDate(createdDate);
        m_ItemForm.setDeleted(true);
        m_ItemForm.setItemForm("Item Form");
        m_ItemForm.setItemFormCode("Item Form Code");
        m_ItemForm.setItemFormDesc("Item Form Desc");
        m_ItemForm.setItemFormID(1);
        Date lastModDate = mock(Date.class);
        m_ItemForm.setLastModDate(lastModDate);
        m_ItemForm.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        m_ItemForm.setProcessed('A');
        m_ItemForm.setProviderServiceMapID(1);
        String actualCreatedBy = m_ItemForm.getCreatedBy();
        Date actualCreatedDate = m_ItemForm.getCreatedDate();
        Boolean actualDeleted = m_ItemForm.getDeleted();
        String actualItemForm = m_ItemForm.getItemForm();
        String actualItemFormCode = m_ItemForm.getItemFormCode();
        String actualItemFormDesc = m_ItemForm.getItemFormDesc();
        Integer actualItemFormID = m_ItemForm.getItemFormID();
        Date actualLastModDate = m_ItemForm.getLastModDate();
        String actualModifiedBy = m_ItemForm.getModifiedBy();
        Character actualProcessed = m_ItemForm.getProcessed();
        Integer actualProviderServiceMapID = m_ItemForm.getProviderServiceMapID();

        // Assert that nothing has changed
        assertEquals("Item Form Code", actualItemFormCode);
        assertEquals("Item Form Desc", actualItemFormDesc);
        assertEquals("Item Form", actualItemForm);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals('A', actualProcessed.charValue());
        assertEquals(1, actualItemFormID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
