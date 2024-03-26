package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LabTestAndComponentModel.class})
@ExtendWith(SpringExtension.class)
class LabTestAndComponentModelDiffblueTest {
    @Autowired
    private LabTestAndComponentModel labTestAndComponentModel;

    /**
     * Method under test:
     * {@link LabTestAndComponentModel#getlabTestAndComponentList(List)}
     */
    @Test
    void testGetlabTestAndComponentList() {
        // Arrange, Act and Assert
        assertTrue(labTestAndComponentModel.getlabTestAndComponentList(new ArrayList<>()).isEmpty());
    }
}
