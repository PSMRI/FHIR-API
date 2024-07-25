package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ObservationsDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Observations#setAbnormal(String)}
     *   <li>{@link Observations#setComment(String)}
     *   <li>{@link Observations#setComplexData(String)}
     *   <li>{@link Observations#setConcept(Concept)}
     *   <li>{@link Observations#setConceptNameToDisplay(String)}
     *   <li>{@link Observations#setConceptSortWeight(Integer)}
     *   <li>{@link Observations#setConceptUuid(String)}
     *   <li>{@link Observations#setCreatorName(String)}
     *   <li>{@link Observations#setDuration(String)}
     *   <li>{@link Observations#setEncounterUuid(String)}
     *   <li>{@link Observations#setFormFieldPath(String)}
     *   <li>{@link Observations#setFormNamespace(String)}
     *   <li>{@link Observations#setGroupMembers(List)}
     *   <li>{@link Observations#setHiNormal(String)}
     *   <li>{@link Observations#setInterpretation(String)}
     *   <li>{@link Observations#setIsAbnormal(String)}
     *   <li>{@link Observations#setLowNormal(String)}
     *   <li>{@link Observations#setObsGroupUuid(String)}
     *   <li>{@link Observations#setOrderUuid(String)}
     *   <li>{@link Observations#setParentConceptUuid(String)}
     *   <li>{@link Observations#setProviders(List)}
     *   <li>{@link Observations#setStatus(String)}
     *   <li>{@link Observations#setTargetObsRelation(String)}
     *   <li>{@link Observations#setType(String)}
     *   <li>{@link Observations#setUnknown(Boolean)}
     *   <li>{@link Observations#setUuid(String)}
     *   <li>{@link Observations#setValue(String)}
     *   <li>{@link Observations#setValueAsString(String)}
     *   <li>{@link Observations#setVoidReason(String)}
     *   <li>{@link Observations#setVoided(Boolean)}
     *   <li>{@link Observations#getAbnormal()}
     *   <li>{@link Observations#getComment()}
     *   <li>{@link Observations#getComplexData()}
     *   <li>{@link Observations#getConcept()}
     *   <li>{@link Observations#getConceptNameToDisplay()}
     *   <li>{@link Observations#getConceptSortWeight()}
     *   <li>{@link Observations#getConceptUuid()}
     *   <li>{@link Observations#getCreatorName()}
     *   <li>{@link Observations#getDuration()}
     *   <li>{@link Observations#getEncounterUuid()}
     *   <li>{@link Observations#getFormFieldPath()}
     *   <li>{@link Observations#getFormNamespace()}
     *   <li>{@link Observations#getGroupMembers()}
     *   <li>{@link Observations#getHiNormal()}
     *   <li>{@link Observations#getInterpretation()}
     *   <li>{@link Observations#getIsAbnormal()}
     *   <li>{@link Observations#getLowNormal()}
     *   <li>{@link Observations#getObsGroupUuid()}
     *   <li>{@link Observations#getOrderUuid()}
     *   <li>{@link Observations#getParentConceptUuid()}
     *   <li>{@link Observations#getProviders()}
     *   <li>{@link Observations#getStatus()}
     *   <li>{@link Observations#getTargetObsRelation()}
     *   <li>{@link Observations#getType()}
     *   <li>{@link Observations#getUnknown()}
     *   <li>{@link Observations#getUuid()}
     *   <li>{@link Observations#getValue()}
     *   <li>{@link Observations#getValueAsString()}
     *   <li>{@link Observations#getVoidReason()}
     *   <li>{@link Observations#getVoided()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        Observations observations = new Observations();

        // Act
        observations.setAbnormal("Abnormal");
        observations.setComment("Comment");
        observations.setComplexData("Complex Data");
        Concept concept = new Concept();
        observations.setConcept(concept);
        observations.setConceptNameToDisplay("Concept Name To Display");
        observations.setConceptSortWeight(3);
        observations.setConceptUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        observations.setCreatorName("Creator Name");
        observations.setDuration("Duration");
        observations.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        observations.setFormFieldPath("Form Field Path");
        observations.setFormNamespace("Form Namespace");
        ArrayList<GroupMembers> groupMembers = new ArrayList<>();
        observations.setGroupMembers(groupMembers);
        observations.setHiNormal("Hi Normal");
        observations.setInterpretation("Interpretation");
        observations.setIsAbnormal("Is Abnormal");
        observations.setLowNormal("Low Normal");
        observations.setObsGroupUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        observations.setOrderUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        observations.setParentConceptUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        ArrayList<Providers> providers = new ArrayList<>();
        observations.setProviders(providers);
        observations.setStatus("Status");
        observations.setTargetObsRelation("Target Obs Relation");
        observations.setType("Type");
        observations.setUnknown(true);
        observations.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        observations.setValue("42");
        observations.setValueAsString("42");
        observations.setVoidReason("Just cause");
        observations.setVoided(true);
        String actualAbnormal = observations.getAbnormal();
        String actualComment = observations.getComment();
        String actualComplexData = observations.getComplexData();
        Concept actualConcept = observations.getConcept();
        String actualConceptNameToDisplay = observations.getConceptNameToDisplay();
        Integer actualConceptSortWeight = observations.getConceptSortWeight();
        String actualConceptUuid = observations.getConceptUuid();
        String actualCreatorName = observations.getCreatorName();
        String actualDuration = observations.getDuration();
        String actualEncounterUuid = observations.getEncounterUuid();
        String actualFormFieldPath = observations.getFormFieldPath();
        String actualFormNamespace = observations.getFormNamespace();
        List<GroupMembers> actualGroupMembers = observations.getGroupMembers();
        String actualHiNormal = observations.getHiNormal();
        String actualInterpretation = observations.getInterpretation();
        String actualIsAbnormal = observations.getIsAbnormal();
        String actualLowNormal = observations.getLowNormal();
        String actualObsGroupUuid = observations.getObsGroupUuid();
        String actualOrderUuid = observations.getOrderUuid();
        String actualParentConceptUuid = observations.getParentConceptUuid();
        List<Providers> actualProviders = observations.getProviders();
        String actualStatus = observations.getStatus();
        String actualTargetObsRelation = observations.getTargetObsRelation();
        String actualType = observations.getType();
        Boolean actualUnknown = observations.getUnknown();
        String actualUuid = observations.getUuid();
        String actualValue = observations.getValue();
        String actualValueAsString = observations.getValueAsString();
        String actualVoidReason = observations.getVoidReason();
        Boolean actualVoided = observations.getVoided();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualConceptUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualEncounterUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualObsGroupUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualOrderUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualParentConceptUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
        assertEquals("42", actualValue);
        assertEquals("42", actualValueAsString);
        assertEquals("Abnormal", actualAbnormal);
        assertEquals("Comment", actualComment);
        assertEquals("Complex Data", actualComplexData);
        assertEquals("Concept Name To Display", actualConceptNameToDisplay);
        assertEquals("Creator Name", actualCreatorName);
        assertEquals("Duration", actualDuration);
        assertEquals("Form Field Path", actualFormFieldPath);
        assertEquals("Form Namespace", actualFormNamespace);
        assertEquals("Hi Normal", actualHiNormal);
        assertEquals("Interpretation", actualInterpretation);
        assertEquals("Is Abnormal", actualIsAbnormal);
        assertEquals("Just cause", actualVoidReason);
        assertEquals("Low Normal", actualLowNormal);
        assertEquals("Status", actualStatus);
        assertEquals("Target Obs Relation", actualTargetObsRelation);
        assertEquals("Type", actualType);
        assertEquals(3, actualConceptSortWeight.intValue());
        assertTrue(actualUnknown);
        assertTrue(actualVoided);
        assertSame(concept, actualConcept);
        assertSame(groupMembers, actualGroupMembers);
        assertSame(providers, actualProviders);
    }
}
