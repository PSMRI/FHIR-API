package com.wipro.fhir.controller.healthCard;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.service.healthID.HealthID_CardService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class GenerateHealthIDCardControllerTest {

	@InjectMocks
	private GenerateHealthIDCardController generateHealthIDCardController;

	@Mock
	private HealthID_CardService healthID_CardService;

	@Test
	@Description("Tests successful generation of OTP for ABHA card (TC_GenerateOTP_Success_001)")
	public void testMapHealthIDToBeneficiarySuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"authMethod\":\"OTP\",\"healthid\":\"ABHA123456\",\"healthIdNumber\":\"1234567890\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "OTP generated successfully";

		// Mock the behavior of healthID_CardService to return the expected OTP response
		Mockito.when(healthID_CardService.generateOTP(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = generateHealthIDCardController.mapHealthIDToBeneficiary(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the OTP generated success message");
	}

	@Test
	@Description("Tests handling of empty request body (TC_GenerateOTP_EmptyRequest_002)")
	public void testMapHealthIDToBeneficiaryNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = generateHealthIDCardController.mapHealthIDToBeneficiary(requestJson, authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}

	@Test
	@Description("Tests successful verification of OTP and health card generation (TC_VerifyOTPAndGenerateHealthCard_Success_001)")
	public void testVerifyOTPAndGenerateHealthCardSuccess() throws FHIRException {
		// Arrange
		String requestJson = "{\"otp\":\"123456\",\"txnId\":\"txn123\",\"authMethod\":\"OTP\"}";
		String authorizationHeader = "Bearer valid_token";
		String expectedResponse = "Card generated successfully";

		// Mock the behavior of healthID_CardService to return the expected response
		Mockito.when(healthID_CardService.verifyOTPAndGenerateCard(requestJson)).thenReturn(expectedResponse);

		// Act
		String response = generateHealthIDCardController.verifyOTPAndGenerateHealthCard(requestJson,
				authorizationHeader);

		// Assert
		assertTrue(response.contains(expectedResponse), "Response should contain the card generation success message");

		// Verify that the service method was called with the correct request
		verify(healthID_CardService).verifyOTPAndGenerateCard(requestJson);
	}

	@Test
	@Description("Tests handling of empty request body (TC_VerifyOTPAndGenerateHealthCard_EmptyRequest_002)")
	public void testVerifyOTPAndGenerateHealthCardNullRequest() {
		// Arrange
		String requestJson = null;
		String authorizationHeader = "Bearer valid_token";

		// Act
		String response = generateHealthIDCardController.verifyOTPAndGenerateHealthCard(requestJson,
				authorizationHeader);

		// Assert
		assertTrue(response.contains("NDHM_FHIR Empty request object"),
				"Response should indicate an empty request object error");
	}
}