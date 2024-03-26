package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {VitalsAnthropometryModel.class})
@ExtendWith(SpringExtension.class)
class VitalsAnthropometryModelDiffblueTest {
    @Autowired
    private VitalsAnthropometryModel vitalsAnthropometryModel;

    /**
     * Method under test:
     * {@link VitalsAnthropometryModel#getVitalsAndAnthropometryList(List)}
     */
    @Test
    void testGetVitalsAndAnthropometryList() {
        // Arrange, Act and Assert
        assertTrue(vitalsAnthropometryModel.getVitalsAndAnthropometryList(new ArrayList<>()).isEmpty());
    }
}
