package com.wipro.fhir.data.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

class BenHealthIDMappingDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link BenHealthIDMapping#setAuthenticationMode(String)}
     *   <li>{@link BenHealthIDMapping#setBenHealthID(Integer)}
     *   <li>{@link BenHealthIDMapping#setBeneficiaryID(Long)}
     *   <li>{@link BenHealthIDMapping#setBeneficiaryRegId(Long)}
     *   <li>{@link BenHealthIDMapping#setCreatedBy(String)}
     *   <li>{@link BenHealthIDMapping#setCreatedDate(Timestamp)}
     *   <li>{@link BenHealthIDMapping#setDeleted(Boolean)}
     *   <li>{@link BenHealthIDMapping#setHealthId(String)}
     *   <li>{@link BenHealthIDMapping#setHealthIdNumber(String)}
     *   <li>{@link BenHealthIDMapping#setLastModDate(Timestamp)}
     *   <li>{@link BenHealthIDMapping#setModifiedBy(String)}
     *   <li>{@link BenHealthIDMapping#setProcessed(String)}
     *   <li>{@link BenHealthIDMapping#setProviderServiceMapID(Integer)}
     *   <li>{@link BenHealthIDMapping#getAuthenticationMode()}
     *   <li>{@link BenHealthIDMapping#getBenHealthID()}
     *   <li>{@link BenHealthIDMapping#getBeneficiaryID()}
     *   <li>{@link BenHealthIDMapping#getBeneficiaryRegId()}
     *   <li>{@link BenHealthIDMapping#getCreatedBy()}
     *   <li>{@link BenHealthIDMapping#getCreatedDate()}
     *   <li>{@link BenHealthIDMapping#getDeleted()}
     *   <li>{@link BenHealthIDMapping#getHealthId()}
     *   <li>{@link BenHealthIDMapping#getHealthIdNumber()}
     *   <li>{@link BenHealthIDMapping#getLastModDate()}
     *   <li>{@link BenHealthIDMapping#getModifiedBy()}
     *   <li>{@link BenHealthIDMapping#getProcessed()}
     *   <li>{@link BenHealthIDMapping#getProviderServiceMapID()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        BenHealthIDMapping benHealthIDMapping = new BenHealthIDMapping();

        // Act
        benHealthIDMapping.setAuthenticationMode("Authentication Mode");
        benHealthIDMapping.setBenHealthID(1);
        benHealthIDMapping.setBeneficiaryID(1L);
        benHealthIDMapping.setBeneficiaryRegId(1L);
        benHealthIDMapping.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        Timestamp createdDate = mock(Timestamp.class);
        benHealthIDMapping.setCreatedDate(createdDate);
        benHealthIDMapping.setDeleted(true);
        benHealthIDMapping.setHealthId("42");
        benHealthIDMapping.setHealthIdNumber("42");
        Timestamp lastModDate = mock(Timestamp.class);
        benHealthIDMapping.setLastModDate(lastModDate);
        benHealthIDMapping.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        benHealthIDMapping.setProcessed("Processed");
        benHealthIDMapping.setProviderServiceMapID(1);
        String actualAuthenticationMode = benHealthIDMapping.getAuthenticationMode();
        Integer actualBenHealthID = benHealthIDMapping.getBenHealthID();
        Long actualBeneficiaryID = benHealthIDMapping.getBeneficiaryID();
        Long actualBeneficiaryRegId = benHealthIDMapping.getBeneficiaryRegId();
        String actualCreatedBy = benHealthIDMapping.getCreatedBy();
        Timestamp actualCreatedDate = benHealthIDMapping.getCreatedDate();
        Boolean actualDeleted = benHealthIDMapping.getDeleted();
        String actualHealthId = benHealthIDMapping.getHealthId();
        String actualHealthIdNumber = benHealthIDMapping.getHealthIdNumber();
        Timestamp actualLastModDate = benHealthIDMapping.getLastModDate();
        String actualModifiedBy = benHealthIDMapping.getModifiedBy();
        String actualProcessed = benHealthIDMapping.getProcessed();
        Integer actualProviderServiceMapID = benHealthIDMapping.getProviderServiceMapID();

        // Assert that nothing has changed
        assertEquals("42", actualHealthId);
        assertEquals("42", actualHealthIdNumber);
        assertEquals("Authentication Mode", actualAuthenticationMode);
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreatedBy);
        assertEquals("Jan 1, 2020 9:00am GMT+0100", actualModifiedBy);
        assertEquals("Processed", actualProcessed);
        assertEquals(1, actualBenHealthID.intValue());
        assertEquals(1, actualProviderServiceMapID.intValue());
        assertEquals(1L, actualBeneficiaryID.longValue());
        assertEquals(1L, actualBeneficiaryRegId.longValue());
        assertTrue(actualDeleted);
        assertSame(createdDate, actualCreatedDate);
        assertSame(lastModDate, actualLastModDate);
    }
}
