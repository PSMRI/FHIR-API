package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class M_GenderDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link M_Gender#setGenderID(Integer)}
     *   <li>{@link M_Gender#setGenderName(String)}
     *   <li>{@link M_Gender#getGenderID()}
     *   <li>{@link M_Gender#getGenderName()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        M_Gender m_Gender = new M_Gender();

        // Act
        m_Gender.setGenderID(1);
        m_Gender.setGenderName("Gender Name");
        Integer actualGenderID = m_Gender.getGenderID();

        // Assert that nothing has changed
        assertEquals("Gender Name", m_Gender.getGenderName());
        assertEquals(1, actualGenderID.intValue());
    }
}
