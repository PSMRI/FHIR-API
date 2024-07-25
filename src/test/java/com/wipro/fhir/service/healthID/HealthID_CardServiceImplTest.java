package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.gson.Gson;
import com.wipro.fhir.service.ndhm.GenerateHealthID_CardService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class HealthID_CardServiceImplTest {
	@Mock
	private GenerateHealthID_CardService generateHealthID_CardService;

	@InjectMocks
	private HealthID_CardServiceImpl healthID_CardServiceImpl;
	@Mock
	private Gson gson;

	@Test
	@Description("Tests successful generation and delivery of a One-Time Password (OTP) (TC_GenerateOTP_Success_001)")
	public void testGenerateOTP_Success() throws Exception {
		// Given
		String request = "SampleRequest";
		String expectedOTP = "123456";

		// When
		when(generateHealthID_CardService.generateOTP(request)).thenReturn(expectedOTP);

		// Call the method
		String result = healthID_CardServiceImpl.generateOTP(request);

		// Then
		assertEquals(expectedOTP, result);
	}

	@Test
	@Description("Tests handling of failures during OTP generation (TC_GenerateOTP_Failure_002)")
	public void testGenerateOTP_Failure() throws Exception {
		// Given
		String request = "SampleRequest";

		// When
		when(generateHealthID_CardService.generateOTP(request)).thenReturn(null);

		// Call the method
		FHIRException exception = assertThrows(FHIRException.class, () -> {
			healthID_CardServiceImpl.generateOTP(request);
		});

		// Then
		assertEquals("NDHM_FHIR Error while generating OTP", exception.getMessage());
	}

	@Test
	@Description("Tests handling of exceptions during OTP generation (TC_GenerateOTP_Exception_003)")
	public void testGenerateOTP_Exception() throws Exception {
		// Given
		String request = "SampleRequest";

		// When
		when(generateHealthID_CardService.generateOTP(request)).thenThrow(new RuntimeException("Test Exception"));

		// Call the method
		FHIRException exception = assertThrows(FHIRException.class, () -> {
			healthID_CardServiceImpl.generateOTP(request);
		});

		// Then
		assertEquals("Test Exception", exception.getMessage());
	}

	@Test
	@Description("Tests successful verification of OTP and card generation (TC_VerifyOTPAndGenerateCard_Success_001)")
	void testVerifyOTPAndGenerateCard_Success() throws FHIRException {
		// Arrange
		when(generateHealthID_CardService.generateCard(Mockito.<String>any(), Mockito.<String>any()))
				.thenReturn("Generate Card");
		when(generateHealthID_CardService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

		// Act
		String actualVerifyOTPAndGenerateCardResult = healthID_CardServiceImpl.verifyOTPAndGenerateCard("Request");

		// Assert
		verify(generateHealthID_CardService).generateCard(eq("Request"), eq("2020-03-01"));
		verify(generateHealthID_CardService).validateOTP(eq("Request"));
		assertEquals("{\"data\":\"Generate Card\"}", actualVerifyOTPAndGenerateCardResult);
	}

	@Test
	@Description("Tests handling of invalid OTP during verification (TC_VerifyOTPAndGenerateCard_OTPValidationFailure_002)")
	public void testVerifyOTPAndGenerateCard_OTPValidationFailure() throws Exception {
		// Given
		String request = "SampleRequest";

		// When
		when(generateHealthID_CardService.validateOTP(request)).thenReturn(null);

		// Call the method
		FHIRException exception = assertThrows(FHIRException.class, () -> {
			healthID_CardServiceImpl.verifyOTPAndGenerateCard(request);
		});

		// Then
		assertEquals("NDHM_FHIR Error while validating OTP", exception.getMessage());
	}

	@Test
	@Description("Tests handling of failures during card generation after successful OTP verification (TC_VerifyOTPAndGenerateCard_CardGenerationFailure_003)")
	public void testVerifyOTPAndGenerateCard_CardGenerationFailure() throws Exception {
		// Given
		String request = "SampleRequest";
		String ndhmXToken = "SampleToken";

		// When
		when(generateHealthID_CardService.validateOTP(request)).thenReturn(ndhmXToken);
		when(generateHealthID_CardService.generateCard(request, ndhmXToken)).thenReturn(null);

		// Call the method
		FHIRException exception = assertThrows(FHIRException.class, () -> {
			healthID_CardServiceImpl.verifyOTPAndGenerateCard(request);
		});

		// Then
		assertEquals("NDHM_FHIR Error while generating ABHA card", exception.getMessage());
	}

	@Test
	@Description("Tests handling of exceptions during OTP verification or card generation (TC_VerifyOTPAndGenerateCard_ExceptionHandling_004)")
	public void testVerifyOTPAndGenerateCard_ExceptionHandling() throws Exception {
		// Given
		String request = "SampleRequest";

		// When
		when(generateHealthID_CardService.validateOTP(request)).thenThrow(new RuntimeException("Test Exception"));

		// Call the method
		FHIRException exception = assertThrows(FHIRException.class, () -> {
			healthID_CardServiceImpl.verifyOTPAndGenerateCard(request);
		});

		// Then
		assertEquals("Test Exception", exception.getMessage());
	}
}
