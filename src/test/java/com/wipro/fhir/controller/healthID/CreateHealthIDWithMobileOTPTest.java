package com.wipro.fhir.controller.healthID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.service.healthID.HealthIDService;
import com.wipro.fhir.service.healthID.HealthID_WithMobileOTPService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class CreateHealthIDWithMobileOTPTest {

	@InjectMocks
	private CreateHealthIDWithMobileOTP createHealthIDWithMobileOTP;

	@Mock
	private HealthID_WithMobileOTPService healthID;

	@Mock
	private HealthIDService healthIDService;

	@Test
	@Description("Tests successful OTP generation (TC_GenerateOTP_Success_001)")
	public void testGenerateOTPSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"mobile\":\"9876543210\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "OTP generated successfully";

		// Mock the behavior of healthID to return the expected OTP response
		Mockito.when(healthID.generateOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithMobileOTP.generateOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP generated success message");

		// Verify that the service method was called with the correct request
		verify(healthID).generateOTP(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_GenerateOTP_EmptyRequest_002)")
	public void testGenerateOTPNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithMobileOTP.generateOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful verification of OTP and health card generation with minimal data (TC_VerifyOTPAndGenerateHealthCard_Success_MinimalData_001)")
	public void testVerifyOTPAndGenerateHealthIDSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"otp\":\"123456\",\"txnId\":\"txn123\",\"address\":\"123 Main St\",\"dayOfBirth\":\"15\",\"email\":\"example@example.com\","
				+ "\"healthId\":\"ABHA123456\",\"healthIdNumber\":\"1234567890\",\"firstName\":\"John\",\"gender\":\"Male\",\"lastName\":\"Doe\",\"monthOfBirth\":\"08\","
				+ "\"name\":\"John Doe\",\"pincode\":123456,\"yearOfBirth\":\"1990\",\"providerServiceMapId\":1,\"createdBy\":\"admin\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "Health ID generated successfully";

		// Mock the behavior of healthID to return the expected response
		Mockito.when(healthID.verifyOTPandGenerateHealthID(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithMobileOTP.verifyOTPAndGenerateHealthID(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse),
				"Response should contain the success message for OTP verification and Health ID generation");

		// Verify that the service method was called with the correct request
		verify(healthID).verifyOTPandGenerateHealthID(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_VerifyOTPAndGenerateHealthCard_EmptyRequest_002)")
	public void testVerifyOTPAndGenerateHealthIDNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithMobileOTP.verifyOTPAndGenerateHealthID(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful mapping of health ID to beneficiary (TC_MapHealthIDToBeneficiary_Success_001)")
	public void testMapHealthIDToBeneficiarySuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"beneficiaryRegID\":1,\"beneficiaryID\":123456,\"healthId\":\"ABHA123456\",\"healthIdNumber\":\"1234567890\","
				+ "\"providerServiceMapId\":1,\"createdBy\":\"admin\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "ABHA mapped to beneficiary successfully";

		// Mock the behavior of healthIDService to return the expected response
		Mockito.when(healthIDService.mapHealthIDToBeneficiary(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithMobileOTP.mapHealthIDToBeneficiary(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse),
				"Response should contain the success message for mapping ABHA to beneficiary");

		// Verify that the service method was called with the correct request
		verify(healthIDService).mapHealthIDToBeneficiary(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_MapHealthIDToBeneficiary_EmptyRequest_002)")
	public void testMapHealthIDToBeneficiaryNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithMobileOTP.mapHealthIDToBeneficiary(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful retrieval of BenhealthID (TC_GetBenhealthID_Success_001)")
	public void testGetBenhealthIDSuccess() throws FHIRException {
		// Arrange
		String comingRequestJson = "{\"beneficiaryRegID\":123456}";
		Long beneficiaryRegID = 123456L;
		String expectedResponse = "{\"healthId\":\"ABHA123456\",\"status\":\"success\"}";

		// Mock the behavior of healthIDService to return the expected response
		Mockito.when(healthIDService.getBenHealthID(beneficiaryRegID)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithMobileOTP.getBenhealthID(comingRequestJson);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the health ID details");

		// Verify that the service method was called with the correct beneficiaryRegID
		verify(healthIDService).getBenHealthID(beneficiaryRegID);
	}

	@Test
	@Description("Tests handling of empty request (TC_GetBenhealthID_EmptyRequest_002)")
	public void testGetBenhealthIDEmptyRequest() {
		// Arrange
		String comingRequestJson = "{}";

		// Act
		String response = createHealthIDWithMobileOTP.getBenhealthID(comingRequestJson);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Invalid request"), "Response should indicate an invalid request");
	}
	
	@Test
	@Description("Tests handling of invalid JSON request body (TC_GetBenhealthID_InvalidJsonRequest_003)")
	public void testGetBenhealthIDInvalidJsonRequest() {
	    // Arrange
	    String invalidJson = "{beneficiaryRegID:123456}"; // Invalid JSON format

	    // Act
	    String response = createHealthIDWithMobileOTP.getBenhealthID(invalidJson);

	    // Assert
	    assertFalse(response.contains("Unexpected error:"), "Response should indicate an unexpected error occurred");
	}

}
