package com.wipro.fhir.controller.healthID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.service.healthID.HealthIDWithBioService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class CreateHealthIDWithBioTest {

	@InjectMocks
	private CreateHealthIDWithBio createHealthIDWithBio;

	@Mock
	private HealthIDWithBioService healthIDWithBioService;

	@Test
	@Description("Tests successful biometric verification (TC_VerifyBio_Success_001)")
	public void testVerifyBioSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"Aadhaar\":\"123456789012\",\"pid\":\"pid123\",\"bioType\":\"Fingerprint\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "Bio verification successful";

		// Mock the behavior of healthIDWithBioService to return the expected response
		Mockito.when(healthIDWithBioService.verifyBio(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithBio.verifyBio(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the bio verification success message");

		// Verify that the service method was called with the correct request
		verify(healthIDWithBioService).verifyBio(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_VerifyBio_EmptyRequest_002)")
	public void testVerifyBioNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithBio.verifyBio(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful mobile OTP generation (TC_CheckAndGenerateMobileOTP_Success_001)")
	public void testCheckAndGenerateMobileOTPSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"mobile\":\"9876543210\",\"txnId\":\"txn123\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "OTP generated successfully";

		// Mock the behavior of healthIDWithBioService to return the expected OTP
		// response
		Mockito.when(healthIDWithBioService.generateMobileOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithBio.checkAndGenerateMobileOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP generation success message");

		// Verify that the service method was called with the correct request
		verify(healthIDWithBioService).generateMobileOTP(requestJson);
	}

	@Test
	@Description("Tests handling of empty phone number (TC_CheckAndGenerateMobileOTP_EmptyRequest_002)")
	public void testCheckAndGenerateMobileOTPNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithBio.checkAndGenerateMobileOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful confirmation with Aadhaar Biometrics (TC_ConfirmWithAadhaarBio_Success_001)")
	public void testConfirmWithAadhaarBioSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"txnId\":\"txn123\",\"pid\":\"pid123\",\"bioType\":\"Fingerprint\",\"authType\":\"OTP\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "Aadhaar bio confirmation successful";

		// Mock the behavior of healthIDWithBioService to return the expected response
		Mockito.when(healthIDWithBioService.confirmWithAadhaarBio(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithBio.confirmWithAadhaarBio(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse),
				"Response should contain the Aadhaar bio confirmation success message");

		// Verify that the service method was called with the correct request
		verify(healthIDWithBioService).confirmWithAadhaarBio(requestJson);
	}

	@Test
	@Description("Tests handling of empty request (TC_ConfirmWithAadhaarBio_EmptyRequest_002)")
	public void testConfirmWithAadhaarBioNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithBio.confirmWithAadhaarBio(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}
}
