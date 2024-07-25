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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.CreateHealthID_Aadhaar_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class HealthIDWithBioServiceImplTest {
    @Mock
    private CreateHealthID_Aadhaar_NDHMService createHealthID_Aadhaar_NDHMService;

    @Mock
    private HealthIDRepo healthIDRepo;

    @InjectMocks
    private HealthIDWithBioServiceImpl healthIDWithBioServiceImpl;

    @Test
    @Description("Tests successful verification of biometric data (TC_VerifyBio_Success_001)")
    void testVerifyBio_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyBio(Mockito.<String>any())).thenReturn("Verify Bio");

        // Act
        String actualVerifyBioResult = healthIDWithBioServiceImpl.verifyBio("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).verifyBio(eq("Request"));
        assertEquals("Verify Bio", actualVerifyBioResult);
    }

    @Test
    @Description("Tests handling of invalid biometric data during verification (TC_VerifyBio_InvalidData_002)")
    void testVerifyBio_Failure() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.verifyBio(null));
    }

    @Test
    @Description("Tests handling of exceptions during biometric verification (TC_VerifyBio_Exception_003)")
    void testVerifyBio_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyBio(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.verifyBio("Request"));
        verify(createHealthID_Aadhaar_NDHMService).verifyBio(eq("Request"));
    }

    @Test
    @Description("Tests successful generation and delivery of a Mobile One-Time Password (OTP) (TC_GenerateMobileOTP_Success_001)")
    void testGenerateMobileOTP_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateMobileOTP(Mockito.<String>any())).thenReturn("Generate Mobile OTP");

        // Act
        String actualGenerateMobileOTPResult = healthIDWithBioServiceImpl.generateMobileOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).generateMobileOTP(eq("Request"));
        assertEquals("Generate Mobile OTP", actualGenerateMobileOTPResult);
    }

    @Test
    @Description("Tests handling of invalid phone number format during mobile OTP generation (TC_GenerateMobileOTP_InvalidPhoneNumber_002)")
    void testGenerateMobileOTP_Failure() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.generateMobileOTP(null));
    }

    @Test
    @Description("Tests handling of exceptions during mobile OTP generation (TC_GenerateMobileOTP_Exception_003)")
    void testGenerateMobileOTP_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateMobileOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.generateMobileOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).generateMobileOTP(eq("Request"));
    }

    @Test
    @Description("Tests successful confirmation using Aadhaar Biometrics (TC_ConfirmWithAadhaarBio_Success_001)")
    void testConfirmWithAadhaarBio_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.confirmWithAadhaarBio(Mockito.<String>any()))
                .thenReturn("Confirm With Aadhaar Bio");

        // Act
        String actualConfirmWithAadhaarBioResult = healthIDWithBioServiceImpl.confirmWithAadhaarBio("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).confirmWithAadhaarBio(eq("Request"));
        assertEquals("Confirm With Aadhaar Bio", actualConfirmWithAadhaarBioResult);
    }

    @Test
    @Description("Tests handling of invalid Aadhaar number format during confirmation (TC_ConfirmWithAadhaarBio_InvalidAadhaar_002)")
    void testConfirmWithAadhaarBio_Failure() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.confirmWithAadhaarBio(null));
    }

    @Test
    @Description("Tests handling of exceptions during confirmation using Aadhaar Biometrics (TC_ConfirmWithAadhaarBio_Exception_003)")
    void testConfirmWithAadhaarBio_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.confirmWithAadhaarBio(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.confirmWithAadhaarBio("Request"));
        verify(createHealthID_Aadhaar_NDHMService).confirmWithAadhaarBio(eq("Request"));
    }
}
