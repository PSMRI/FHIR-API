package com.wipro.fhir.data.patient;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PatientDemographic.class})
@ExtendWith(SpringExtension.class)
class PatientDemographicDiffblueTest {
    @Autowired
    private PatientDemographic patientDemographic;

    /**
     * Method under test: {@link PatientDemographic#getPatientDemographic(List)}
     */
    @Test
    void testGetPatientDemographic() {
        // Arrange, Act and Assert
        assertNull(patientDemographic.getPatientDemographic(new ArrayList<>()));
    }
}
