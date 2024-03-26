package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ConditionDiagnosisDataModel.class})
@ExtendWith(SpringExtension.class)
class ConditionDiagnosisDataModelDiffblueTest {
    @Autowired
    private ConditionDiagnosisDataModel conditionDiagnosisDataModel;

    /**
     * Method under test: {@link ConditionDiagnosisDataModel#getConditionList(List)}
     */
    @Test
    void testGetConditionList() {
        // Arrange, Act and Assert
        assertTrue(conditionDiagnosisDataModel.getConditionList(new ArrayList<>()).isEmpty());
    }
}
