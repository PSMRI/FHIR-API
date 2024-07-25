package com.wipro.fhir.service.healthID_validate;

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

import com.wipro.fhir.service.healthID.HealthIDServiceImpl;
import com.wipro.fhir.service.ndhm.ValidateHealthID_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@ExtendWith(SpringExtension.class)
class HealthIDValidationServiceImplTest {
    @Mock
    private HealthIDServiceImpl healthIDServiceImpl;

    @InjectMocks
    private HealthIDValidationServiceImpl healthIDValidationServiceImpl;

    @Mock
    private HttpUtils httpUtils;

    @Mock
    private ValidateHealthID_NDHMService validateHealthID_NDHMService;

    @Test
    @Description(" Tests successful generation of a One-Time Password (OTP) specifically for Health ID validation. (TC_GenerateOTPForHealthIDValidation_Success_001)")
    void testGenerateOTPForHealthIDValidation_Success() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(Mockito.<String>any()))
                .thenReturn("Generate OTPFor Health IDValidation");

        // Act
        String actualGenerateOTPForHealthIDValidationResult = healthIDValidationServiceImpl
                .generateOTPForHealthIDValidation("Request");

        // Assert
        verify(validateHealthID_NDHMService).generateOTPForHealthIDValidation(eq("Request"));
        assertEquals("Generate OTPFor Health IDValidation", actualGenerateOTPForHealthIDValidationResult);
    }

    @Test
    @Description(" Tests handling of a scenario where the OTP generation dependency returns a null response. (TC_GenerateOTPForHealthIDValidation_NullOtpResponse_002)")
    void testGenerateOTPForHealthIDValidation_NullOtpResponse() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.generateOTPForHealthIDValidation("Request"));
        verify(validateHealthID_NDHMService).generateOTPForHealthIDValidation(eq("Request"));
    }

    @Test
    @Description("Tests handling of exceptions during OTP generation for Health ID validation (e.g., unexpected errors in your service logic). (TC_GenerateOTPForHealthIDValidation_Exception_003)")
    void testGenerateOTPForHealthIDValidation_Exception() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.generateOTPForHealthIDValidation("Request"));
        verify(validateHealthID_NDHMService).generateOTPForHealthIDValidation(eq("Request"));
    }

    @Test
    @Description("Tests successful validation of a provided OTP and a corresponding Health ID. (TC_ValidateOTPAndHealthID_Success_001)")
    void testValidateOTPAndHealthID_Success() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act
        String actualValidateOTPAndHealthIDResult = healthIDValidationServiceImpl.validateOTPAndHealthID("Request");

        // Assert
        verify(validateHealthID_NDHMService).validateOTPForHealthIDValidation(eq("Request"));
        assertEquals("2020-03-01", actualValidateOTPAndHealthIDResult);
    }

    @Test
    @Description("Tests handling of a scenario where the Health ID validation dependency returns a null response. (TC_ValidateOTPAndHealthID_NullOtpResponse_002)")
    void testValidateOTPAndHealthID_NullOtpResponse() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.validateOTPAndHealthID("Request"));
        verify(validateHealthID_NDHMService).validateOTPForHealthIDValidation(eq("Request"));
    }

    @Test
    @Description("Tests handling of exceptions during OTP and Health ID validation (e.g., unexpected errors in your service logic, network issues). (TC_ValidateOTPAndHealthID_Exception_003)")
    void testValidateOTPAndHealthID_Exception() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.validateOTPAndHealthID("Request"));
        verify(validateHealthID_NDHMService).validateOTPForHealthIDValidation(eq("Request"));
    }
}
