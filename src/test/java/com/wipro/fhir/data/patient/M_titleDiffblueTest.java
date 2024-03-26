package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class M_titleDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_title#setTitleID(Integer)}
     *   <li>{@link M_title#setTitleName(String)}
     *   <li>{@link M_title#getTitleID()}
     *   <li>{@link M_title#getTitleName()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_title m_title = new M_title();

        // Act
        m_title.setTitleID(1);
        m_title.setTitleName("Dr");
        Integer actualTitleID = m_title.getTitleID();

        // Assert that nothing has changed
        assertEquals("Dr", m_title.getTitleName());
        assertEquals(1, actualTitleID.intValue());
    }
}
