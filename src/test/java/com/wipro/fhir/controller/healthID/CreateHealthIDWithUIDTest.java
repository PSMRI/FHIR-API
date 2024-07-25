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

import com.wipro.fhir.service.healthID.HealthIDWithUIDService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class CreateHealthIDWithUIDTest {

	@InjectMocks
	private CreateHealthIDWithUID createHealthIDWithUID;

	@Mock
	private HealthIDWithUIDService healthIDWithUIDService;

	@Test
	@Description("Tests successful generation of OTP with valid request data (TC_GenerateOTP_Success_001)")
	public void testGenerateOTPSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"mobile\":\"9876543210\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "{\"status\":\"OTP generated successfully\"}";

		// Mock the behavior of HealthIDWithUIDService to return the expected OTP
		// response
		Mockito.when(healthIDWithUIDService.generateOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithUID.generateOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP generation success message");

		// Verify that the correct service method was called with the requestJson
		verify(healthIDWithUIDService).generateOTP(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_GenerateOTP_EmptyRequest_002)")
	public void testGenerateOTPNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithUID.generateOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful verification of OTP (TC_VerifyOTP_Success_001)")
	public void testVerifyOTPSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"OTP\":\"123456\", \"txnId\":\"TXN123456\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "{\"status\":\"OTP verified successfully\"}";

		// Mock the behavior of HealthIDWithUIDService to return the expected OTP
		// verification response
		Mockito.when(healthIDWithUIDService.verifyOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithUID.verifyOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP verification success message");

		// Verify that the correct service method was called with the requestJson
		verify(healthIDWithUIDService).verifyOTP(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_VerifyOTP_EmptyRequest_002)")
	public void testVerifyOTPNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithUID.verifyOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful mobile OTP generation with valid request data (TC_CheckAndGenerateMobileOTP_Success_001)")
	public void testCheckAndGenerateMobileOTPSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"mobile\":\"9876543210\", \"txnId\":\"TXN123456\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "{\"status\":\"OTP generated successfully\"}";

		// Mock the behavior of HealthIDWithUIDService to return the expected OTP
		// generation response
		Mockito.when(healthIDWithUIDService.checkAndGenerateOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithUID.checkAndGenerateMobileOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP generation success message");

		// Verify that the correct service method was called with the requestJson
		verify(healthIDWithUIDService).checkAndGenerateOTP(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_CheckAndGenerateMobileOTP_EmptyRequest_002)")
	public void testCheckAndGenerateMobileOTPNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithUID.checkAndGenerateMobileOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful mobile OTP verification with valid OTP (TC_VerifyMobileOTP_Success_001)")
	public void testVerifyMobileOTPSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"mobileOTP\":\"123456\", \"txnId\":\"TXN123456\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "{\"status\":\"OTP verified successfully\"}";

		// Mock the behavior of HealthIDWithUIDService to return the expected OTP
		// verification response
		Mockito.when(healthIDWithUIDService.verifyMobileOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithUID.verifyMobileOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP verification success message");

		// Verify that the correct service method was called with the requestJson
		verify(healthIDWithUIDService).verifyMobileOTP(requestJson);
	}

	@Test
	@Description("Tests handling of empty request (TC_VerifyMobileOTP_EmptyRequest_002)")
	public void testVerifyMobileOTPNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithUID.verifyMobileOTP(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful creation of HealthID with valid user ID (TC_CreateHealthIDWithUID_Success_001)")
	public void testCreateHealthIDWithUIDSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"otp\":\"123456\",\"txnId\":\"TXN123456\",\"address\":\"123 Main St\",\"dayOfBirth\":\"01\",\"email\":\"example@example.com\",\"profilePhoto\":\"photoBase64\",\"password\":\"password123\",\"healthId\":\"ABHA123456\",\"healthIdNumber\":\"1234567890\",\"firstName\":\"John\",\"gender\":\"Male\",\"lastName\":\"Doe\",\"middleName\":\"M\",\"monthOfBirth\":\"01\",\"name\":\"John Doe\",\"pincode\":123456,\"yearOfBirth\":\"1990\",\"providerServiceMapId\":1,\"createdBy\":\"admin\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "{\"status\":\"Health ID created successfully\"}";

		// Mock the behavior of HealthIDWithUIDService to return the expected Health ID
		// creation response
		Mockito.when(healthIDWithUIDService.createHealthIDWithUID(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = createHealthIDWithUID.createHealthIDWithUID(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse),
				"Response should contain the Health ID creation success message");

		// Verify that the correct service method was called with the requestJson
		verify(healthIDWithUIDService).createHealthIDWithUID(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_CreateHealthIDWithUID_EmptyRequest_002)")
	public void testCreateHealthIDWithUIDNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = createHealthIDWithUID.createHealthIDWithUID(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

}
