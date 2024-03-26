package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ConditionChiefComplaintsDataModel.class})
@ExtendWith(SpringExtension.class)
class ConditionChiefComplaintsDataModelDiffblueTest {
    @Autowired
    private ConditionChiefComplaintsDataModel conditionChiefComplaintsDataModel;

    /**
     * Method under test:
     * {@link ConditionChiefComplaintsDataModel#getConditionChiefComplaintList(List)}
     */
    @Test
    void testGetConditionChiefComplaintList() {
        // Arrange, Act and Assert
        assertTrue(conditionChiefComplaintsDataModel.getConditionChiefComplaintList(new ArrayList<>()).isEmpty());
    }
}
