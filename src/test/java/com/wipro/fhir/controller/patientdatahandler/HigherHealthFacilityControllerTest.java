package com.wipro.fhir.controller.patientdatahandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class HigherHealthFacilityControllerTest {

	@InjectMocks
	private HigherHealthFacilityController higherHealthFacilityController;

	@Mock
	private HigherHealthFacilityServiceImpl higherHealthFacilityServiceImpl;

	@Test
	@Description("Tests successful feeding of patient demographic data with valid request data (TC_FeedPatientDemographicData_Success_001)")
	public void testFeedPatientDemographicData_Success() throws FHIRException {
		ResourceRequestHandler request = new ResourceRequestHandler();
		request.setBeneficiaryID(123L);
		request.setBeneficiaryRegID(345L);
		request.setDistrict("district");

		String updateResponse = "Beneficiary ID updated successfully";
		String expectedResponse = "{\"data\":{\"response\":\"Beneficiary ID updated successfully\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

		when(higherHealthFacilityServiceImpl.updateBengenIDToHigherHealthFacilityBeneficiary(request))
				.thenReturn(updateResponse);

		String result = higherHealthFacilityController.feedPatientDemographicData(request);

		assertEquals(expectedResponse, result);
	}

	@Test
	@Description("Tests handling of failure during patient demographic data feeding (TC_FeedPatientDemographicData_Failure_002)")
    public void testFeedPatientDemographicData_Failure() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler(); // Create a sample request
        request.setBeneficiaryID(123L);
		request.setBeneficiaryRegID(345L);
		request.setDistrict("district");

        // Create the actual response structure
        String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Error in updating Beneficary ID to higher health data\",\"status\":\"Error in updating Beneficary ID to higher health data\"}";


        // When
        when(higherHealthFacilityServiceImpl.updateBengenIDToHigherHealthFacilityBeneficiary(any(ResourceRequestHandler.class)))
                .thenReturn(null);

        // Call the method
        String result = higherHealthFacilityController.feedPatientDemographicData(request);

        // Then
        assertEquals(expectedResponse, result);
    }

	@Test
	@Description("Tests handling of exceptions during patient demographic data feeding (TC_FeedPatientDemographicData_Exception_003)")
	public void testFeedPatientDemographicData_Exception() throws FHIRException {
		// Given
		ResourceRequestHandler request = new ResourceRequestHandler(); // Create a sample request
		request.setBeneficiaryID(123L);
		request.setBeneficiaryRegID(345L);
		request.setDistrict("district");

		String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Error in updating Beneficary ID to higher health data. Test Exception\",\"status\":\"Error in updating Beneficary ID to higher health data. Test Exception\"}";

		// When
		when(higherHealthFacilityServiceImpl
				.updateBengenIDToHigherHealthFacilityBeneficiary(any(ResourceRequestHandler.class)))
				.thenThrow(new RuntimeException("Test Exception"));

		// Call the method
		String result = higherHealthFacilityController.feedPatientDemographicData(request);

		// Then
		assertEquals(expectedResponse, result);
	}
	
	@Test
	@Description("Tests successful retrieval of clinical data from higher health facility (TC_GetCLinicalDataHigherhealthFacility_Success_001)")
    public void testGetCLinicalDataHigherhealthFacility_Success() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setBeneficiaryID(123L);
        request.setBeneficiaryRegID(345L);
        request.setDistrict("district");

        String clinicalData = "Sample clinical data";

        // Create the expected response structure
        String expectedResponse = "{\"data\":{\"response\":\"Sample clinical data\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

        // When
        when(higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(request)).thenReturn(clinicalData);

        // Call the method
        String result = higherHealthFacilityController.getCLinicalDataHigherhealthFacility(request);

        // Then
        assertEquals(expectedResponse, result);
    }
	
	@Test
	@Description("Tests handling of failure during retrieval of clinical data from higher health facility (TC_GetCLinicalDataHigherhealthFacility_Failure_002)")
    public void testGetCLinicalDataHigherhealthFacility_Failure() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setBeneficiaryID(123L);
        request.setBeneficiaryRegID(345L);
        request.setDistrict("district");

        // Create the expected response structure
        String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Error in getting higher health data\",\"status\":\"Error in getting higher health data\"}";

        // When
        when(higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(any(ResourceRequestHandler.class)))
                .thenReturn(null);

        // Call the method
        String result = higherHealthFacilityController.getCLinicalDataHigherhealthFacility(request);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    @Description("Tests handling of exceptions during retrieval of clinical data from higher health facility (TC_GetCLinicalDataHigherhealthFacility_Exception_003)")
    public void testGetCLinicalDataHigherhealthFacility_Exception() throws FHIRException {
        // Given
        ResourceRequestHandler request = new ResourceRequestHandler();
        request.setBeneficiaryID(123L);
        request.setBeneficiaryRegID(345L);
        request.setDistrict("district");

        String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Error in getting higher health data. Test Exception\",\"status\":\"Error in getting higher health data. Test Exception\"}";

        // When
        when(higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(any(ResourceRequestHandler.class)))
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method
        String result = higherHealthFacilityController.getCLinicalDataHigherhealthFacility(request);

        // Then
        assertEquals(expectedResponse, result);
    }
}
