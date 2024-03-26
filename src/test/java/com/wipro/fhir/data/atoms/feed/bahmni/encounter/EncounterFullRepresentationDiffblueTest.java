package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class EncounterFullRepresentationDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>
     * {@link EncounterFullRepresentation#setAssociatedToPatientProgram(Boolean)}
     *   <li>{@link EncounterFullRepresentation#setBahmniDiagnoses(List)}
     *   <li>{@link EncounterFullRepresentation#setDisposition(String)}
     *   <li>{@link EncounterFullRepresentation#setDrugOrders(List)}
     *   <li>{@link EncounterFullRepresentation#setEncounterDateTime(Date)}
     *   <li>{@link EncounterFullRepresentation#setEncounterType(String)}
     *   <li>{@link EncounterFullRepresentation#setEncounterTypeUuid(String)}
     *   <li>{@link EncounterFullRepresentation#setEncounterUuid(String)}
     *   <li>{@link EncounterFullRepresentation#setId(String)}
     *   <li>{@link EncounterFullRepresentation#setLocationName(String)}
     *   <li>{@link EncounterFullRepresentation#setLocationUuid(String)}
     *   <li>{@link EncounterFullRepresentation#setObservations(List)}
     *   <li>{@link EncounterFullRepresentation#setPatientId(String)}
     *   <li>{@link EncounterFullRepresentation#setPatientProgramUuid(String)}
     *   <li>{@link EncounterFullRepresentation#setPatientUuid(String)}
     *   <li>{@link EncounterFullRepresentation#setProviders(List)}
     *   <li>{@link EncounterFullRepresentation#setReason(String)}
     *   <li>{@link EncounterFullRepresentation#setVisitType(String)}
     *   <li>{@link EncounterFullRepresentation#setVisitTypeUuid(String)}
     *   <li>{@link EncounterFullRepresentation#setVisitUuid(String)}
     *   <li>{@link EncounterFullRepresentation#getAssociatedToPatientProgram()}
     *   <li>{@link EncounterFullRepresentation#getBahmniDiagnoses()}
     *   <li>{@link EncounterFullRepresentation#getDisposition()}
     *   <li>{@link EncounterFullRepresentation#getDrugOrders()}
     *   <li>{@link EncounterFullRepresentation#getEncounterDateTime()}
     *   <li>{@link EncounterFullRepresentation#getEncounterType()}
     *   <li>{@link EncounterFullRepresentation#getEncounterTypeUuid()}
     *   <li>{@link EncounterFullRepresentation#getEncounterUuid()}
     *   <li>{@link EncounterFullRepresentation#getId()}
     *   <li>{@link EncounterFullRepresentation#getLocationName()}
     *   <li>{@link EncounterFullRepresentation#getLocationUuid()}
     *   <li>{@link EncounterFullRepresentation#getObservations()}
     *   <li>{@link EncounterFullRepresentation#getPatientId()}
     *   <li>{@link EncounterFullRepresentation#getPatientProgramUuid()}
     *   <li>{@link EncounterFullRepresentation#getPatientUuid()}
     *   <li>{@link EncounterFullRepresentation#getProviders()}
     *   <li>{@link EncounterFullRepresentation#getReason()}
     *   <li>{@link EncounterFullRepresentation#getVisitType()}
     *   <li>{@link EncounterFullRepresentation#getVisitTypeUuid()}
     *   <li>{@link EncounterFullRepresentation#getVisitUuid()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        EncounterFullRepresentation encounterFullRepresentation = new EncounterFullRepresentation();

        // Act
        encounterFullRepresentation.setAssociatedToPatientProgram(true);
        ArrayList<BahmniDiagnoses> bahmniDiagnoses = new ArrayList<>();
        encounterFullRepresentation.setBahmniDiagnoses(bahmniDiagnoses);
        encounterFullRepresentation.setDisposition("Disposition");
        ArrayList<DrugOrders> drugOrders = new ArrayList<>();
        encounterFullRepresentation.setDrugOrders(drugOrders);
        Date encounterDateTime = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        encounterFullRepresentation.setEncounterDateTime(encounterDateTime);
        encounterFullRepresentation.setEncounterType("3");
        encounterFullRepresentation.setEncounterTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setId("42");
        encounterFullRepresentation.setLocationName("Location Name");
        encounterFullRepresentation.setLocationUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        ArrayList<Observations> observations = new ArrayList<>();
        encounterFullRepresentation.setObservations(observations);
        encounterFullRepresentation.setPatientId("42");
        encounterFullRepresentation.setPatientProgramUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setPatientUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        ArrayList<Providers> providers = new ArrayList<>();
        encounterFullRepresentation.setProviders(providers);
        encounterFullRepresentation.setReason("Just cause");
        encounterFullRepresentation.setVisitType("Visit Type");
        encounterFullRepresentation.setVisitTypeUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        encounterFullRepresentation.setVisitUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        Boolean actualAssociatedToPatientProgram = encounterFullRepresentation.getAssociatedToPatientProgram();
        List<BahmniDiagnoses> actualBahmniDiagnoses = encounterFullRepresentation.getBahmniDiagnoses();
        String actualDisposition = encounterFullRepresentation.getDisposition();
        List<DrugOrders> actualDrugOrders = encounterFullRepresentation.getDrugOrders();
        Date actualEncounterDateTime = encounterFullRepresentation.getEncounterDateTime();
        String actualEncounterType = encounterFullRepresentation.getEncounterType();
        String actualEncounterTypeUuid = encounterFullRepresentation.getEncounterTypeUuid();
        String actualEncounterUuid = encounterFullRepresentation.getEncounterUuid();
        String actualId = encounterFullRepresentation.getId();
        String actualLocationName = encounterFullRepresentation.getLocationName();
        String actualLocationUuid = encounterFullRepresentation.getLocationUuid();
        List<Observations> actualObservations = encounterFullRepresentation.getObservations();
        String actualPatientId = encounterFullRepresentation.getPatientId();
        String actualPatientProgramUuid = encounterFullRepresentation.getPatientProgramUuid();
        String actualPatientUuid = encounterFullRepresentation.getPatientUuid();
        List<Providers> actualProviders = encounterFullRepresentation.getProviders();
        String actualReason = encounterFullRepresentation.getReason();
        String actualVisitType = encounterFullRepresentation.getVisitType();
        String actualVisitTypeUuid = encounterFullRepresentation.getVisitTypeUuid();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualEncounterTypeUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualEncounterUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualLocationUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualPatientProgramUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualPatientUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualVisitTypeUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", encounterFullRepresentation.getVisitUuid());
        assertEquals("3", actualEncounterType);
        assertEquals("42", actualId);
        assertEquals("42", actualPatientId);
        assertEquals("Disposition", actualDisposition);
        assertEquals("Just cause", actualReason);
        assertEquals("Location Name", actualLocationName);
        assertEquals("Visit Type", actualVisitType);
        assertTrue(actualAssociatedToPatientProgram);
        assertSame(bahmniDiagnoses, actualBahmniDiagnoses);
        assertSame(drugOrders, actualDrugOrders);
        assertSame(observations, actualObservations);
        assertSame(providers, actualProviders);
        assertSame(encounterDateTime, actualEncounterDateTime);
    }
}
