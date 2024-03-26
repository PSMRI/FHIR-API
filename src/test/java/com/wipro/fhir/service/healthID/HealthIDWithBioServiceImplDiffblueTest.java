package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.CreateHealthID_Aadhaar_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {HealthIDWithBioServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class HealthIDWithBioServiceImplDiffblueTest {
    @MockBean
    private CreateHealthID_Aadhaar_NDHMService createHealthID_Aadhaar_NDHMService;

    @MockBean
    private HealthIDRepo healthIDRepo;

    @Autowired
    private HealthIDWithBioServiceImpl healthIDWithBioServiceImpl;

    /**
     * Method under test: {@link HealthIDWithBioServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyBio(Mockito.<String>any())).thenReturn("Verify Bio");

        // Act
        String actualVerifyBioResult = healthIDWithBioServiceImpl.verifyBio("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).verifyBio(eq("Request"));
        assertEquals("Verify Bio", actualVerifyBioResult);
    }

    /**
     * Method under test: {@link HealthIDWithBioServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio2() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.verifyBio(null));
    }

    /**
     * Method under test: {@link HealthIDWithBioServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyBio(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.verifyBio("Request"));
        verify(createHealthID_Aadhaar_NDHMService).verifyBio(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDWithBioServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateMobileOTP(Mockito.<String>any())).thenReturn("Generate Mobile OTP");

        // Act
        String actualGenerateMobileOTPResult = healthIDWithBioServiceImpl.generateMobileOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).generateMobileOTP(eq("Request"));
        assertEquals("Generate Mobile OTP", actualGenerateMobileOTPResult);
    }

    /**
     * Method under test:
     * {@link HealthIDWithBioServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP2() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.generateMobileOTP(null));
    }

    /**
     * Method under test:
     * {@link HealthIDWithBioServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateMobileOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.generateMobileOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).generateMobileOTP(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDWithBioServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.confirmWithAadhaarBio(Mockito.<String>any()))
                .thenReturn("Confirm With Aadhaar Bio");

        // Act
        String actualConfirmWithAadhaarBioResult = healthIDWithBioServiceImpl.confirmWithAadhaarBio("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).confirmWithAadhaarBio(eq("Request"));
        assertEquals("Confirm With Aadhaar Bio", actualConfirmWithAadhaarBioResult);
    }

    /**
     * Method under test:
     * {@link HealthIDWithBioServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio2() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.confirmWithAadhaarBio(null));
    }

    /**
     * Method under test:
     * {@link HealthIDWithBioServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.confirmWithAadhaarBio(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithBioServiceImpl.confirmWithAadhaarBio("Request"));
        verify(createHealthID_Aadhaar_NDHMService).confirmWithAadhaarBio(eq("Request"));
    }
}
