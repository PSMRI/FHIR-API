package com.wipro.fhir.controller.healthIDvalidate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.service.healthID_validate.HealthIDValidationService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class HealthIDValidateControllerTest {

	@InjectMocks
	private HealthIDValidateController healthIDValidateController;

	@Mock
	private HealthIDValidationService healthIDValidationService;

	@Test
	@Description("Tests successful generation of OTP for HealthID validation with valid request data (TC_GenerateOTPForHealthIDValidation_Success_001)")
	public void testGenerateOTPForHealthIDValidation_Success() throws FHIRException {
		String requestJson = "{\"healthID\":\"1234567890\",\"isValidate\":true,\"authenticationMode\":\"OTP\"}";
		String otpResponse = "123456";

		String expectedResponse = "{\"data\":{\"response\":\"123456\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

		when(healthIDValidationService.generateOTPForHealthIDValidation(requestJson)).thenReturn(otpResponse);

		String result = healthIDValidateController.generateOTPForHealthIDValidation(requestJson, "Bearer valid_token");

		assertEquals(expectedResponse, result);
	}

	@Test
	@Description("Tests handling of FHIR exception during OTP generation (TC_GenerateOTPForHealthIDValidation_FHIRException_002)")
	public void testGenerateOTPForHealthIDValidation_FHIRException() throws FHIRException {
		String requestJson = "{\"healthID\":\"1234567890\",\"isValidate\":true,\"authenticationMode\":\"OTP\"}";

		String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Test Exception\",\"status\":\"Test Exception\"}";

		when(healthIDValidationService.generateOTPForHealthIDValidation(requestJson))
				.thenThrow(new FHIRException("Test Exception"));

		String result = healthIDValidateController.generateOTPForHealthIDValidation(requestJson, "Bearer valid_token");

		assertEquals(expectedResponse, result);
	}
	
	@Test
	@Description("Tests successful verification of OTP for HealthID validation with valid OTP (TC_VerifyOTPForHealthIDValidation_Success_001)")
	public void testVerifyOTPForHealthIDValidation_Success() throws FHIRException {
	    String requestJson = "{\"otp\":\"123456\",\"txnId\":\"txn123\",\"healthID\":\"healthID123\"}";
	    String validationResponse = "OTP validated successfully";

	    String expectedResponse = "{\"data\":{\"response\":\"OTP validated successfully\"},\"statusCode\":200,\"errorMessage\":\"Success\",\"status\":\"Success\"}";

	    when(healthIDValidationService.validateOTPAndHealthID(requestJson)).thenReturn(validationResponse);

	    String result = healthIDValidateController.verifyOTPForHealthIDValidation(requestJson, "Bearer valid_token");

	    assertEquals(expectedResponse, result);
	}
	
	@Test
	@Description("Tests handling of FHIR exception during OTP verification for HealthID (TC_VerifyOTPForHealthIDValidation_FHIRException_002)")
	public void testVerifyOTPForHealthIDValidation_FHIRException() throws FHIRException {
	    String requestJson = "{\"otp\":\"123456\",\"txnId\":\"txn123\",\"healthID\":\"healthID123\"}";

	    String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"Test Exception\",\"status\":\"Test Exception\"}";

	    when(healthIDValidationService.validateOTPAndHealthID(requestJson)).thenThrow(new FHIRException("Test Exception"));

	    String result = healthIDValidateController.verifyOTPForHealthIDValidation(requestJson, "Bearer valid_token");

	    assertEquals(expectedResponse, result);
	}
	
	@Test
	@Description("Tests handling of empty request body (TC_VerifyOTPForHealthIDValidation_EmptyRequest_003)")
	public void testVerifyOTPForHealthIDValidation_EmptyRequest() {
	    String expectedResponse = "{\"statusCode\":5000,\"errorMessage\":\"NDHM_FHIR Empty request object\",\"status\":\"NDHM_FHIR Empty request object\"}";

	    String result = healthIDValidateController.verifyOTPForHealthIDValidation(null, "Bearer valid_token");

	    assertEquals(expectedResponse, result);
	}



}
