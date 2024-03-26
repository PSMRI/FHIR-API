package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class GroupMembersDiffblueTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link GroupMembers#setAbnormal(String)}
     *   <li>{@link GroupMembers#setComment(String)}
     *   <li>{@link GroupMembers#setComplexData(String)}
     *   <li>{@link GroupMembers#setConcept(Concept)}
     *   <li>{@link GroupMembers#setConceptNameToDisplay(String)}
     *   <li>{@link GroupMembers#setConceptSortWeight(Integer)}
     *   <li>{@link GroupMembers#setConceptUuid(String)}
     *   <li>{@link GroupMembers#setCreatorName(String)}
     *   <li>{@link GroupMembers#setDuration(String)}
     *   <li>{@link GroupMembers#setEncounterUuid(String)}
     *   <li>{@link GroupMembers#setFormFieldPath(String)}
     *   <li>{@link GroupMembers#setFormNamespace(String)}
     *   <li>{@link GroupMembers#setGroupMembers(List)}
     *   <li>{@link GroupMembers#setHiNormal(String)}
     *   <li>{@link GroupMembers#setInterpretation(String)}
     *   <li>{@link GroupMembers#setIsAbnormal(String)}
     *   <li>{@link GroupMembers#setLowNormal(String)}
     *   <li>{@link GroupMembers#setObsGroupUuid(String)}
     *   <li>{@link GroupMembers#setOrderUuid(String)}
     *   <li>{@link GroupMembers#setParentConceptUuid(String)}
     *   <li>{@link GroupMembers#setProviders(List)}
     *   <li>{@link GroupMembers#setStatus(String)}
     *   <li>{@link GroupMembers#setTargetObsRelation(String)}
     *   <li>{@link GroupMembers#setType(String)}
     *   <li>{@link GroupMembers#setUnknown(Boolean)}
     *   <li>{@link GroupMembers#setUuid(String)}
     *   <li>{@link GroupMembers#setValueAsString(String)}
     *   <li>{@link GroupMembers#setVoidReason(String)}
     *   <li>{@link GroupMembers#setVoided(Boolean)}
     *   <li>{@link GroupMembers#getAbnormal()}
     *   <li>{@link GroupMembers#getComment()}
     *   <li>{@link GroupMembers#getComplexData()}
     *   <li>{@link GroupMembers#getConcept()}
     *   <li>{@link GroupMembers#getConceptNameToDisplay()}
     *   <li>{@link GroupMembers#getConceptSortWeight()}
     *   <li>{@link GroupMembers#getConceptUuid()}
     *   <li>{@link GroupMembers#getCreatorName()}
     *   <li>{@link GroupMembers#getDuration()}
     *   <li>{@link GroupMembers#getEncounterUuid()}
     *   <li>{@link GroupMembers#getFormFieldPath()}
     *   <li>{@link GroupMembers#getFormNamespace()}
     *   <li>{@link GroupMembers#getGroupMembers()}
     *   <li>{@link GroupMembers#getHiNormal()}
     *   <li>{@link GroupMembers#getInterpretation()}
     *   <li>{@link GroupMembers#getIsAbnormal()}
     *   <li>{@link GroupMembers#getLowNormal()}
     *   <li>{@link GroupMembers#getObsGroupUuid()}
     *   <li>{@link GroupMembers#getOrderUuid()}
     *   <li>{@link GroupMembers#getParentConceptUuid()}
     *   <li>{@link GroupMembers#getProviders()}
     *   <li>{@link GroupMembers#getStatus()}
     *   <li>{@link GroupMembers#getTargetObsRelation()}
     *   <li>{@link GroupMembers#getType()}
     *   <li>{@link GroupMembers#getUnknown()}
     *   <li>{@link GroupMembers#getUuid()}
     *   <li>{@link GroupMembers#getValueAsString()}
     *   <li>{@link GroupMembers#getVoidReason()}
     *   <li>{@link GroupMembers#getVoided()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        GroupMembers groupMembers = new GroupMembers();

        // Act
        groupMembers.setAbnormal("Abnormal");
        groupMembers.setComment("Comment");
        groupMembers.setComplexData("Complex Data");
        Concept concept = new Concept();
        groupMembers.setConcept(concept);
        groupMembers.setConceptNameToDisplay("Concept Name To Display");
        groupMembers.setConceptSortWeight(3);
        groupMembers.setConceptUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        groupMembers.setCreatorName("Creator Name");
        groupMembers.setDuration("Duration");
        groupMembers.setEncounterUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        groupMembers.setFormFieldPath("Form Field Path");
        groupMembers.setFormNamespace("Form Namespace");
        ArrayList<GroupMembers> groupMembers2 = new ArrayList<>();
        groupMembers.setGroupMembers(groupMembers2);
        groupMembers.setHiNormal("Hi Normal");
        groupMembers.setInterpretation("Interpretation");
        groupMembers.setIsAbnormal("Is Abnormal");
        groupMembers.setLowNormal("Low Normal");
        groupMembers.setObsGroupUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        groupMembers.setOrderUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        groupMembers.setParentConceptUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        ArrayList<Providers> providers = new ArrayList<>();
        groupMembers.setProviders(providers);
        groupMembers.setStatus("Status");
        groupMembers.setTargetObsRelation("Target Obs Relation");
        groupMembers.setType("Type");
        groupMembers.setUnknown(true);
        groupMembers.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        groupMembers.setValueAsString("42");
        groupMembers.setVoidReason("Just cause");
        groupMembers.setVoided(true);
        String actualAbnormal = groupMembers.getAbnormal();
        String actualComment = groupMembers.getComment();
        String actualComplexData = groupMembers.getComplexData();
        Concept actualConcept = groupMembers.getConcept();
        String actualConceptNameToDisplay = groupMembers.getConceptNameToDisplay();
        Integer actualConceptSortWeight = groupMembers.getConceptSortWeight();
        String actualConceptUuid = groupMembers.getConceptUuid();
        String actualCreatorName = groupMembers.getCreatorName();
        String actualDuration = groupMembers.getDuration();
        String actualEncounterUuid = groupMembers.getEncounterUuid();
        String actualFormFieldPath = groupMembers.getFormFieldPath();
        String actualFormNamespace = groupMembers.getFormNamespace();
        List<GroupMembers> actualGroupMembers = groupMembers.getGroupMembers();
        String actualHiNormal = groupMembers.getHiNormal();
        String actualInterpretation = groupMembers.getInterpretation();
        String actualIsAbnormal = groupMembers.getIsAbnormal();
        String actualLowNormal = groupMembers.getLowNormal();
        String actualObsGroupUuid = groupMembers.getObsGroupUuid();
        String actualOrderUuid = groupMembers.getOrderUuid();
        String actualParentConceptUuid = groupMembers.getParentConceptUuid();
        List<Providers> actualProviders = groupMembers.getProviders();
        String actualStatus = groupMembers.getStatus();
        String actualTargetObsRelation = groupMembers.getTargetObsRelation();
        String actualType = groupMembers.getType();
        Boolean actualUnknown = groupMembers.getUnknown();
        String actualUuid = groupMembers.getUuid();
        String actualValueAsString = groupMembers.getValueAsString();
        String actualVoidReason = groupMembers.getVoidReason();
        Boolean actualVoided = groupMembers.getVoided();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualConceptUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualEncounterUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualObsGroupUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualOrderUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualParentConceptUuid);
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
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
        assertSame(groupMembers2, actualGroupMembers);
        assertSame(providers, actualProviders);
    }
}
