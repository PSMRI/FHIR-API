package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AllergyIntoleranceDataModel.class})
@ExtendWith(SpringExtension.class)
class AllergyIntoleranceDataModelDiffblueTest {
    @Autowired
    private AllergyIntoleranceDataModel allergyIntoleranceDataModel;

    /**
     * Method under test: {@link AllergyIntoleranceDataModel#getAllergyList(List)}
     */
    @Test
    void testGetAllergyList() {
        // Arrange, Act and Assert
        assertTrue(allergyIntoleranceDataModel.getAllergyList(new ArrayList<>()).isEmpty());
    }
}
