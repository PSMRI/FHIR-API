package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class VisitWiseEncounterDataDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link VisitWiseEncounterData#setBahmniDiagnoses(List)}
     *   <li>{@link VisitWiseEncounterData#setDrugOrders(List)}
     *   <li>{@link VisitWiseEncounterData#setEncounterDateTime(Date)}
     *   <li>{@link VisitWiseEncounterData#setObservationsGMConsultation(List)}
     *   <li>{@link VisitWiseEncounterData#setObservationsGMReg(List)}
     *   <li>{@link VisitWiseEncounterData#setPatientId(String)}
     *   <li>{@link VisitWiseEncounterData#setVisitType(String)}
     *   <li>{@link VisitWiseEncounterData#setVisitTypeUuid(String)}
     *   <li>{@link VisitWiseEncounterData#setVisitUuid(String)}
     *   <li>{@link VisitWiseEncounterData#getBahmniDiagnoses()}
     *   <li>{@link VisitWiseEncounterData#getDrugOrders()}
     *   <li>{@link VisitWiseEncounterData#getEncounterDateTime()}
     *   <li>{@link VisitWiseEncounterData#getObservationsGMConsultation()}
     *   <li>{@link VisitWiseEncounterData#getObservationsGMReg()}
     *   <li>{@link VisitWiseEncounterData#getPatientId()}
     *   <li>{@link VisitWiseEncounterData#getVisitType()}
     *   <li>{@link VisitWiseEncounterData#getVisitTypeUuid()}
     *   <li>{@link VisitWiseEncounterData#getVisitUuid()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        VisitWiseEncounterData visitWiseEncounterData = new VisitWiseEncounterData();
        ArrayList<BahmniDiagnoses> bahmniDiagnoses = new ArrayList<>();

        // Act
        visitWiseEncounterData.setBahmniDiagnoses(bahmniDiagnoses);
        ArrayList<DrugOrders> drugOrders = new ArrayList<>();
        visitWiseEncounterData.setDrugOrders(drugOrders);
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        visitWiseEncounterData.setEncounterDateTime(encounterDateTime);
        ArrayList<GroupMembers> observationsGMConsultation = new ArrayList<>();
        visitWiseEncounterData.setObservationsGMConsultation(observationsGMConsultation);
        ArrayList<GroupMembers> observationsGMReg = new ArrayList<>();
        visitWiseEncounterData.setObservationsGMReg(observationsGMReg);
        visitWiseEncounterData.setPatientId("42");
        visitWiseEncounterData.setVisitType("Visit Type");
        visitWiseEncounterData.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        visitWiseEncounterData.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        List<BahmniDiagnoses> actualBahmniDiagnoses = visitWiseEncounterData.getBahmniDiagnoses();
        List<DrugOrders> actualDrugOrders = visitWiseEncounterData.getDrugOrders();
        Date actualEncounterDateTime = visitWiseEncounterData.getEncounterDateTime();
        List<GroupMembers> actualObservationsGMConsultation = visitWiseEncounterData.getObservationsGMConsultation();
        List<GroupMembers> actualObservationsGMReg = visitWiseEncounterData.getObservationsGMReg();
        String actualPatientId = visitWiseEncounterData.getPatientId();
        String actualVisitType = visitWiseEncounterData.getVisitType();
        String actualVisitTypeUuid = visitWiseEncounterData.getVisitTypeUuid();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualVisitTypeUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", visitWiseEncounterData.getVisitUuid());
        assertEquals("42", actualPatientId);
        assertEquals("Visit Type", actualVisitType);
        assertSame(bahmniDiagnoses, actualBahmniDiagnoses);
        assertSame(drugOrders, actualDrugOrders);
        assertSame(observationsGMConsultation, actualObservationsGMConsultation);
        assertSame(observationsGMReg, actualObservationsGMReg);
        assertSame(encounterDateTime, actualEncounterDateTime);
    }
}
