package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FamilyMemberHistoryDataModel.class})
@ExtendWith(SpringExtension.class)
class FamilyMemberHistoryDataModelDiffblueTest {
    @Autowired
    private FamilyMemberHistoryDataModel familyMemberHistoryDataModel;

    /**
     * Method under test:
     * {@link FamilyMemberHistoryDataModel#getFamilyMemberHistoryList(List)}
     */
    @Test
    void testGetFamilyMemberHistoryList() {
        // Arrange, Act and Assert
        assertTrue(familyMemberHistoryDataModel.getFamilyMemberHistoryList(new ArrayList<>()).isEmpty());
    }
}
