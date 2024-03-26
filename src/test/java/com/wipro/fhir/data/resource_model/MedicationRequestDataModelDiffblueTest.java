package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MedicationRequestDataModel.class})
@ExtendWith(SpringExtension.class)
class MedicationRequestDataModelDiffblueTest {
    @Autowired
    private MedicationRequestDataModel medicationRequestDataModel;

    /**
     * Method under test:
     * {@link MedicationRequestDataModel#getMedicationRequestList(List)}
     */
    @Test
    void testGetMedicationRequestList() {
        // Arrange, Act and Assert
        assertTrue(medicationRequestDataModel.getMedicationRequestList(new ArrayList<>()).isEmpty());
    }
}
