package com.wipro.fhir.data.patient_data_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

class TRG_PatientResourceDataDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link TRG_PatientResourceData#setBenRegId(Long)}
     *   <li>{@link TRG_PatientResourceData#setBeneficiaryID(Long)}
     *   <li>{@link TRG_PatientResourceData#setCreatedBy(String)}
     *   <li>{@link TRG_PatientResourceData#setCreatedDate(Timestamp)}
     *   <li>{@link TRG_PatientResourceData#setId(Long)}
     *   <li>{@link TRG_PatientResourceData#setLastModDate(Timestamp)}
     *   <li>{@link TRG_PatientResourceData#setModifiedBy(String)}
     *   <li>{@link TRG_PatientResourceData#setProcessed(Boolean)}
     *   <li>{@link TRG_PatientResourceData#setTrgStatus(String)}
     *   <li>{@link TRG_PatientResourceData#getBenRegId()}
     *   <li>{@link TRG_PatientResourceData#getBeneficiaryID()}
     *   <li>{@link TRG_PatientResourceData#getCreatedBy()}
     *   <li>{@link TRG_PatientResourceData#getCreatedDate()}
     *   <li>{@link TRG_PatientResourceData#getId()}
     *   <li>{@link TRG_PatientResourceData#getLastModDate()}
     *   <li>{@link TRG_PatientResourceData#getModifiedBy()}
     *   <li>{@link TRG_PatientResourceData#getProcessed()}
     *   <li>{@link TRG_PatientResourceData#getTrgStatus()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        TRG_PatientResourceData trg_PatientResourceData = new TRG_PatientResourceData();

        // Act
        trg_PatientResourceData.setBenRegId(1L);
        trg_PatientResourceData.setBeneficiaryID(1L);
        trg_PatientResourceData.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Timestamp createdDate = mock(Timestamp.class);
        trg_PatientResourceData.setCreatedDate(createdDate);
        trg_PatientResourceData.setId(1L);
        Timestamp lastModDate = mock(Timestamp.class);
        trg_PatientResourceData.setLastModDate(lastModDate);
        trg_PatientResourceData.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        trg_PatientResourceData.setProcessed(true);
        trg_PatientResourceData.setTrgStatus("Trg Status");
        Long actualBenRegId = trg_PatientResourceData.getBenRegId();
        Long actualBeneficiaryID = trg_PatientResourceData.getBeneficiaryID();
        String actualCreatedBy = trg_PatientResourceData.getCreatedBy();
        Timestamp actualCreatedDate = trg_PatientResourceData.getCreatedDate();
        Long actualId = trg_PatientResourceData.getId();
        Timestamp actualLastModDate = trg_PatientResourceData.getLastModDate();
        String actualModifiedBy = trg_PatientResourceData.getModifiedBy();
        Boolean actualProcessed = trg_PatientResourceData.getProcessed();

        // Assert that nothing has changed
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Trg Status", trg_PatientResourceData.getTrgStatus());
        assertEquals(1L, actualBenRegId.longValue());
        assertEquals(1L, actualBeneficiaryID.longValue());
        assertEquals(1L, actualId.longValue());
        assertTrue(actualProcessed);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
