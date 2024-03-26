package com.wipro.fhir.controller.patientdatahandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import org.junit.jupiter.api.Test;

class HigherHealthFacilityControllerDiffblueTest {
    /**
     * Method under test:
     * {@link HigherHealthFacilityController#feedPatientDemographicData(ResourceRequestHandler)}
     */
    @Test
    void testFeedPatientDemographicData() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        HigherHealthFacilityController higherHealthFacilityController = new HigherHealthFacilityController();

        // Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Error in updating Beneficary ID to higher health data. Cannot invoke"
                        + " \\\"com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl.updateBengenIDToHighe"
                        + "rHealthFacilityBeneficiary(com.wipro.fhir.data.request_handler.ResourceRequestHandler)\\\" because"
                        + " \\\"this.higherHealthFacilityServiceImpl\\\" is null\",\"status\":\"Error in updating Beneficary ID to higher"
                        + " health data. Cannot invoke \\\"com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl"
                        + ".updateBengenIDToHigherHealthFacilityBeneficiary(com.wipro.fhir.data.request_handler.ResourceRequestHandler"
                        + ")\\\" because \\\"this.higherHealthFacilityServiceImpl\\\" is null\"}",
                higherHealthFacilityController.feedPatientDemographicData(new ResourceRequestHandler()));
    }

    /**
     * Method under test:
     * {@link HigherHealthFacilityController#feedPatientDemographicData(ResourceRequestHandler)}
     */
    @Test
    void testFeedPatientDemographicData2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Error in updating Beneficary ID to higher health data. Cannot invoke"
                        + " \\\"com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl.updateBengenIDToHighe"
                        + "rHealthFacilityBeneficiary(com.wipro.fhir.data.request_handler.ResourceRequestHandler)\\\" because"
                        + " \\\"this.higherHealthFacilityServiceImpl\\\" is null\",\"status\":\"Error in updating Beneficary ID to higher"
                        + " health data. Cannot invoke \\\"com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl"
                        + ".updateBengenIDToHigherHealthFacilityBeneficiary(com.wipro.fhir.data.request_handler.ResourceRequestHandler"
                        + ")\\\" because \\\"this.higherHealthFacilityServiceImpl\\\" is null\"}",
                (new HigherHealthFacilityController()).feedPatientDemographicData(mock(ResourceRequestHandler.class)));
    }

    /**
     * Method under test:
     * {@link HigherHealthFacilityController#getCLinicalDataHigherhealthFacility(ResourceRequestHandler)}
     */
    @Test
    void testGetCLinicalDataHigherhealthFacility() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        HigherHealthFacilityController higherHealthFacilityController = new HigherHealthFacilityController();

        // Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Error in getting higher health data. Cannot invoke \\\"com.wipro.fhir"
                        + ".service.patient_data_handler.HigherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(com"
                        + ".wipro.fhir.data.request_handler.ResourceRequestHandler)\\\" because \\\"this.higherHealthFacilityServiceImpl\\\""
                        + " is null\",\"status\":\"Error in getting higher health data. Cannot invoke \\\"com.wipro.fhir.service.patient"
                        + "_data_handler.HigherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(com.wipro.fhir.data"
                        + ".request_handler.ResourceRequestHandler)\\\" because \\\"this.higherHealthFacilityServiceImpl\\\" is"
                        + " null\"}",
                higherHealthFacilityController.getCLinicalDataHigherhealthFacility(new ResourceRequestHandler()));
    }

    /**
     * Method under test:
     * {@link HigherHealthFacilityController#getCLinicalDataHigherhealthFacility(ResourceRequestHandler)}
     */
    @Test
    void testGetCLinicalDataHigherhealthFacility2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals(
                "{\"statusCode\":5000,\"errorMessage\":\"Error in getting higher health data. Cannot invoke \\\"com.wipro.fhir"
                        + ".service.patient_data_handler.HigherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(com"
                        + ".wipro.fhir.data.request_handler.ResourceRequestHandler)\\\" because \\\"this.higherHealthFacilityServiceImpl\\\""
                        + " is null\",\"status\":\"Error in getting higher health data. Cannot invoke \\\"com.wipro.fhir.service.patient"
                        + "_data_handler.HigherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(com.wipro.fhir.data"
                        + ".request_handler.ResourceRequestHandler)\\\" because \\\"this.higherHealthFacilityServiceImpl\\\" is"
                        + " null\"}",
                (new HigherHealthFacilityController()).getCLinicalDataHigherhealthFacility(mock(ResourceRequestHandler.class)));
    }
}
