package com.wipro.fhir.controller.patientdatahandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService;
import com.wipro.fhir.utils.exception.FHIRException;
@ExtendWith(SpringExtension.class)
class PatientDataGatewayControllerTest {
	
	@InjectMocks
	private PatientDataGatewayController patientDataGatewayController;
	
	@Mock
	private PatientDataGatewayService patientDataGatewayService;
	
	@Test
	@Description("Tests successful retrieval of patient data from MongoDB with valid search criteria (TC_PatientDataSearchFromMongo_Success_001)")
    public void testPatientDataSearchFromMongo_Success() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setHealthId("H123");
        request.setPhoneNo("1234567890");

        String authorizationHeader = "Bearer token";
        String patientProfileData = "Patient profile data";

        // Create the expected response structure
        String expectedResponse = "{\"data\":{\"response\":\"Patient profile data\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

        // When
        when(patientDataGatewayService.searchPatientProfileMongo(authorizationHeader, request)).thenReturn(patientProfileData);

        // Call the method
        String result = patientDataGatewayController.patientDataSearchFromMongo(request, authorizationHeader);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    @Description("Tests handling of failures during patient data search from MongoDB (TC_PatientDataSearchFromMongo_Failure_002)")
    public void testPatientDataSearchFromMongo_Failure() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setHealthId("H123");
        request.setPhoneNo("1234567890");

        String authorizationHeader = "Bearer token";
        // Create the expected response structure
        String expectedResponse = "{\"data\":{\"response\":\"patient not found\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";
        // When
        when(patientDataGatewayService.searchPatientProfileMongo(any(String.class), any(ResourceRequestHandler.class)))
                .thenReturn(null);

        // Call the method
        String result = patientDataGatewayController.patientDataSearchFromMongo(request, authorizationHeader);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    @Description("Tests handling of exceptions during patient data search from MongoDB (TC_PatientDataSearchFromMongo_Exception_003)")
    public void testPatientDataSearchFromMongo_Exception() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setHealthId("H123");
        request.setPhoneNo("1234567890");

        String authorizationHeader = "Bearer token";

        String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Test Exception\",\"status\":\"Test Exception\"}";

        // When
        when(patientDataGatewayService.searchPatientProfileMongo(any(String.class), any(ResourceRequestHandler.class)))
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method
        String result = patientDataGatewayController.patientDataSearchFromMongo(request, authorizationHeader);

        // Then
        assertEquals(expectedResponse, result);
    }
    
    @Test
    @Description("Tests successful retrieval of paginated patient data from MongoDB with valid search criteria and pagination parameters (TC_PatientDataSearchFromMongoPagination_Success_001)")
    public void testPatientDataSearchFromMongoPagination_Success() throws FHIRException {
        // Given
        Integer pageNo = 1;
        String patientProfileData = "Patient profile data";

        // Create the expected response structure
        String expectedResponse = "{\"data\":{\"response\":\"Patient profile data\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

        // When
        when(patientDataGatewayService.searchPatientProfileMongoPagination(pageNo)).thenReturn(patientProfileData);

        // Call the method
        String result = patientDataGatewayController.patientDataSearchFromMongoPagination(pageNo);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    @Description("Tests handling of failures during paginated patient data search from MongoDB (TC_PatientDataSearchFromMongoPagination_Failure_002)")
    public void testPatientDataSearchFromMongoPagination_Failure() throws FHIRException {
        // Given
        Integer pageNo = 1;

        // Create the expected response structure
        String expectedResponse = "{\"data\":{\"response\":\"No data found\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

        // When
        when(patientDataGatewayService.searchPatientProfileMongoPagination(anyInt())).thenReturn(null);

        // Call the method
        String result = patientDataGatewayController.patientDataSearchFromMongoPagination(pageNo);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    @Description("Tests handling of Exception during paginated patient data search from MongoDB (TC_PatientDataSearchFromMongoPagination_Failure_003)")
    public void testPatientDataSearchFromMongoPagination_Exception() throws FHIRException {
        // Given
        Integer pageNo = 1;

        String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Test Exception\",\"status\":\"Test Exception\"}";

        // When
        when(patientDataGatewayService.searchPatientProfileMongoPagination(Mockito.anyInt())).thenThrow(new RuntimeException("Test Exception"));

        // Call the method
        String result = patientDataGatewayController.patientDataSearchFromMongoPagination(pageNo);

        // Then
        assertEquals(expectedResponse, result);
    }

   
}
