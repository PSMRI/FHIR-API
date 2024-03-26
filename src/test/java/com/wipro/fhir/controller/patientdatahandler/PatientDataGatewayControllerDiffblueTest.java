package com.wipro.fhir.controller.patientdatahandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import org.junit.jupiter.api.Test;

class PatientDataGatewayControllerDiffblueTest {
    /**
     * Method under test:
     * {@link PatientDataGatewayController#patientDataSearchFromMongo(ResourceRequestHandler, String)}
     */
    @Test
    void testPatientDataSearchFromMongo() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        PatientDataGatewayController patientDataGatewayController = new PatientDataGatewayController();

        // Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Cannot invoke \\\"com.wipro.fhir.service.patient_data_handler"
                        + ".PatientDataGatewayService.searchPatientProfileMongo(String, com.wipro.fhir.data.request_handler"
                        + ".ResourceRequestHandler)\\\" because \\\"this.patientDataGatewayService\\\" is null\",\"status\":\"Cannot invoke"
                        + " \\\"com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService.searchPatientProfileMongo(String,"
                        + " com.wipro.fhir.data.request_handler.ResourceRequestHandler)\\\" because \\\"this.patientDataGatewayService\\\""
                        + " is null\"}",
                patientDataGatewayController.patientDataSearchFromMongo(new ResourceRequestHandler(), "JaneDoe"));
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayController#patientDataSearchFromMongo(ResourceRequestHandler, String)}
     */
    @Test
    void testPatientDataSearchFromMongo2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Cannot invoke \\\"com.wipro.fhir.service.patient_data_handler"
                        + ".PatientDataGatewayService.searchPatientProfileMongo(String, com.wipro.fhir.data.request_handler"
                        + ".ResourceRequestHandler)\\\" because \\\"this.patientDataGatewayService\\\" is null\",\"status\":\"Cannot invoke"
                        + " \\\"com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService.searchPatientProfileMongo(String,"
                        + " com.wipro.fhir.data.request_handler.ResourceRequestHandler)\\\" because \\\"this.patientDataGatewayService\\\""
                        + " is null\"}",
                (new PatientDataGatewayController()).patientDataSearchFromMongo(mock(ResourceRequestHandler.class), "JaneDoe"));
    }

    /**
     * Method under test:
     * {@link PatientDataGatewayController#patientDataSearchFromMongoPagination(Integer)}
     */
    @Test
    void testPatientDataSearchFromMongoPagination() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertEquals("{\"statusCode\":5000,\"errorMessage\":\"Cannot invoke \\\"com.wipro.fhir.service.patient_data_handler"
                        + ".PatientDataGatewayService.searchPatientProfileMongoPagination(java.lang.Integer)\\\" because"
                        + " \\\"this.patientDataGatewayService\\\" is null\",\"status\":\"Cannot invoke \\\"com.wipro.fhir.service.patient"
                        + "_data_handler.PatientDataGatewayService.searchPatientProfileMongoPagination(java.lang.Integer)\\\" because"
                        + " \\\"this.patientDataGatewayService\\\" is null\"}",
                (new PatientDataGatewayController()).patientDataSearchFromMongoPagination(1));
    }
}
