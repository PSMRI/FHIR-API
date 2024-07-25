package com.wipro.fhir.data.request_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

class PatientEligibleForResourceCreationDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link PatientEligibleForResourceCreation#setBeneficiaryId(Long)}
     *   <li>{@link PatientEligibleForResourceCreation#setBeneficiaryRegID(Long)}
     *   <li>{@link PatientEligibleForResourceCreation#setCreatedDate(Timestamp)}
     *   <li>{@link PatientEligibleForResourceCreation#setId(Long)}
     *   <li>{@link PatientEligibleForResourceCreation#setProcessed(Boolean)}
     *   <li>{@link PatientEligibleForResourceCreation#setVisitCategory(String)}
     *   <li>{@link PatientEligibleForResourceCreation#setVisitCode(Long)}
     *   <li>{@link PatientEligibleForResourceCreation#setVisitDate(Timestamp)}
     *   <li>{@link PatientEligibleForResourceCreation#setVisitReason(String)}
     *   <li>{@link PatientEligibleForResourceCreation#getBeneficiaryId()}
     *   <li>{@link PatientEligibleForResourceCreation#getBeneficiaryRegID()}
     *   <li>{@link PatientEligibleForResourceCreation#getCreatedDate()}
     *   <li>{@link PatientEligibleForResourceCreation#getId()}
     *   <li>{@link PatientEligibleForResourceCreation#getProcessed()}
     *   <li>{@link PatientEligibleForResourceCreation#getVisitCategory()}
     *   <li>{@link PatientEligibleForResourceCreation#getVisitCode()}
     *   <li>{@link PatientEligibleForResourceCreation#getVisitDate()}
     *   <li>{@link PatientEligibleForResourceCreation#getVisitReason()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        PatientEligibleForResourceCreation patientEligibleForResourceCreation = new PatientEligibleForResourceCreation();

        // Act
        patientEligibleForResourceCreation.setBeneficiaryId(1L);
        patientEligibleForResourceCreation.setBeneficiaryRegID(1L);
        Timestamp createdDate = mock(Timestamp.class);
        patientEligibleForResourceCreation.setCreatedDate(createdDate);
        patientEligibleForResourceCreation.setId(1L);
        patientEligibleForResourceCreation.setProcessed(true);
        patientEligibleForResourceCreation.setVisitCategory("Visit Category");
        patientEligibleForResourceCreation.setVisitCode(1L);
        Timestamp visitDate = mock(Timestamp.class);
        patientEligibleForResourceCreation.setVisitDate(visitDate);
        patientEligibleForResourceCreation.setVisitReason("Just cause");
        Long actualBeneficiaryId = patientEligibleForResourceCreation.getBeneficiaryId();
        Long actualBeneficiaryRegID = patientEligibleForResourceCreation.getBeneficiaryRegID();
        Timestamp actualCreatedDate = patientEligibleForResourceCreation.getCreatedDate();
        Long actualId = patientEligibleForResourceCreation.getId();
        Boolean actualProcessed = patientEligibleForResourceCreation.getProcessed();
        String actualVisitCategory = patientEligibleForResourceCreation.getVisitCategory();
        Long actualVisitCode = patientEligibleForResourceCreation.getVisitCode();
        Timestamp actualVisitDate = patientEligibleForResourceCreation.getVisitDate();

        // Assert that nothing has changed
        assertEquals("Just cause", patientEligibleForResourceCreation.getVisitReason());
        assertEquals("Visit Category", actualVisitCategory);
        assertEquals(1L, actualBeneficiaryId.longValue());
        assertEquals(1L, actualBeneficiaryRegID.longValue());
        assertEquals(1L, actualId.longValue());
        assertEquals(1L, actualVisitCode.longValue());
        assertTrue(actualProcessed);
        assertSame(createdDate, actualCreatedDate);
        assertSame(visitDate, actualVisitDate);
    }
}
