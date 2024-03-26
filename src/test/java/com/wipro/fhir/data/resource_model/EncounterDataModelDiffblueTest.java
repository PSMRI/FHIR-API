package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EncounterDataModel.class})
@ExtendWith(SpringExtension.class)
class EncounterDataModelDiffblueTest {
    @Autowired
    private EncounterDataModel encounterDataModel;

    /**
     * Method under test: {@link EncounterDataModel#getEncounterList(List)}
     */
    @Test
    void testGetEncounterList() {
        // Arrange, Act and Assert
        assertTrue(encounterDataModel.getEncounterList(new ArrayList<>()).isEmpty());
    }
}
