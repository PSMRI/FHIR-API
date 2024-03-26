package com.wipro.fhir.data.e_aushdhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {E_AusdhFacilityProcessLog.class})
@ExtendWith(SpringExtension.class)
class E_AusdhFacilityProcessLogDiffblueTest {
    @Autowired
    private E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog;

    /**
     * Method under test: {@link E_AusdhFacilityProcessLog#geteLID()}
     */
    @Test
    void testGeteLID() {
        // Arrange, Act and Assert
        assertNull((new E_AusdhFacilityProcessLog()).geteLID());
    }

    /**
     * Method under test: {@link E_AusdhFacilityProcessLog#geteLID()}
     */
    @Test
    void testGeteLID2() {
        // Arrange
        E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog2 = new E_AusdhFacilityProcessLog();
        e_AusdhFacilityProcessLog2.setLastSuccessDate(mock(Timestamp.class));

        // Act and Assert
        assertNull(e_AusdhFacilityProcessLog2.geteLID());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link E_AusdhFacilityProcessLog#setAcknowledge(Boolean)}
     *   <li>{@link E_AusdhFacilityProcessLog#setAmrithFacilityId(Integer)}
     *   <li>{@link E_AusdhFacilityProcessLog#setCreatedBy(String)}
     *   <li>{@link E_AusdhFacilityProcessLog#setCreatedDate(Timestamp)}
     *   <li>{@link E_AusdhFacilityProcessLog#setDeleted(Boolean)}
     *   <li>{@link E_AusdhFacilityProcessLog#setEaushadiFacilityId(Integer)}
     *   <li>{@link E_AusdhFacilityProcessLog#setLastFailureDate(Timestamp)}
     *   <li>{@link E_AusdhFacilityProcessLog#setLastModDate(Timestamp)}
     *   <li>{@link E_AusdhFacilityProcessLog#setLastSuccessDate(Timestamp)}
     *   <li>{@link E_AusdhFacilityProcessLog#setModifiedBy(String)}
     *   <li>{@link E_AusdhFacilityProcessLog#setProcessed(String)}
     *   <li>{@link E_AusdhFacilityProcessLog#setStockUpdateAmrit(Boolean)}
     *   <li>{@link E_AusdhFacilityProcessLog#seteLID(Integer)}
     *   <li>{@link E_AusdhFacilityProcessLog#getAcknowledge()}
     *   <li>{@link E_AusdhFacilityProcessLog#getAmrithFacilityId()}
     *   <li>{@link E_AusdhFacilityProcessLog#getCreatedBy()}
     *   <li>{@link E_AusdhFacilityProcessLog#getCreatedDate()}
     *   <li>{@link E_AusdhFacilityProcessLog#getDeleted()}
     *   <li>{@link E_AusdhFacilityProcessLog#getEaushadiFacilityId()}
     *   <li>{@link E_AusdhFacilityProcessLog#getLastFailureDate()}
     *   <li>{@link E_AusdhFacilityProcessLog#getLastModDate()}
     *   <li>{@link E_AusdhFacilityProcessLog#getLastSuccessDate()}
     *   <li>{@link E_AusdhFacilityProcessLog#getModifiedBy()}
     *   <li>{@link E_AusdhFacilityProcessLog#getProcessed()}
     *   <li>{@link E_AusdhFacilityProcessLog#getStockUpdateAmrit()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog = new E_AusdhFacilityProcessLog();

        // Act
        e_AusdhFacilityProcessLog.setAcknowledge(true);
        e_AusdhFacilityProcessLog.setAmrithFacilityId(1);
        e_AusdhFacilityProcessLog.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Timestamp createdDate = mock(Timestamp.class);
        e_AusdhFacilityProcessLog.setCreatedDate(createdDate);
        e_AusdhFacilityProcessLog.setDeleted(true);
        e_AusdhFacilityProcessLog.setEaushadiFacilityId(1);
        Timestamp lastFailureDate = mock(Timestamp.class);
        e_AusdhFacilityProcessLog.setLastFailureDate(lastFailureDate);
        Timestamp lastModDate = mock(Timestamp.class);
        e_AusdhFacilityProcessLog.setLastModDate(lastModDate);
        Timestamp lastSuccessDate = mock(Timestamp.class);
        e_AusdhFacilityProcessLog.setLastSuccessDate(lastSuccessDate);
        e_AusdhFacilityProcessLog.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        e_AusdhFacilityProcessLog.setProcessed("Processed");
        e_AusdhFacilityProcessLog.setStockUpdateAmrit(true);
        e_AusdhFacilityProcessLog.seteLID(1);
        Boolean actualAcknowledge = e_AusdhFacilityProcessLog.getAcknowledge();
        Integer actualAmrithFacilityId = e_AusdhFacilityProcessLog.getAmrithFacilityId();
        String actualCreatedBy = e_AusdhFacilityProcessLog.getCreatedBy();
        Timestamp actualCreatedDate = e_AusdhFacilityProcessLog.getCreatedDate();
        Boolean actualDeleted = e_AusdhFacilityProcessLog.getDeleted();
        Integer actualEaushadiFacilityId = e_AusdhFacilityProcessLog.getEaushadiFacilityId();
        Timestamp actualLastFailureDate = e_AusdhFacilityProcessLog.getLastFailureDate();
        Timestamp actualLastModDate = e_AusdhFacilityProcessLog.getLastModDate();
        Timestamp actualLastSuccessDate = e_AusdhFacilityProcessLog.getLastSuccessDate();
        String actualModifiedBy = e_AusdhFacilityProcessLog.getModifiedBy();
        String actualProcessed = e_AusdhFacilityProcessLog.getProcessed();
        Boolean actualStockUpdateAmrit = e_AusdhFacilityProcessLog.getStockUpdateAmrit();

        // Assert that nothing has changed
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Processed", actualProcessed);
        assertEquals(1, actualAmrithFacilityId.intValue());
        assertEquals(1, actualEaushadiFacilityId.intValue());
        assertTrue(actualAcknowledge);
        assertTrue(actualDeleted);
        assertTrue(actualStockUpdateAmrit);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastFailureDate, actualLastFailureDate);
        assertSame(lastModDate, actualLastModDate);
        assertSame(lastSuccessDate, actualLastSuccessDate);
    }
}
