package com.wipro.fhir.data.resource_model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AppointmentDataModel.class})
@ExtendWith(SpringExtension.class)
class AppointmentDataModelDiffblueTest {
    @Autowired
    private AppointmentDataModel appointmentDataModel;

    /**
     * Method under test: {@link AppointmentDataModel#getAppointmentList(List)}
     */
    @Test
    void testGetAppointmentList() {
        // Arrange, Act and Assert
        assertTrue(appointmentDataModel.getAppointmentList(new ArrayList<>()).isEmpty());
    }
}
