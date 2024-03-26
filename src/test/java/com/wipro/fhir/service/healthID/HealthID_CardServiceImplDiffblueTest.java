package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.service.ndhm.GenerateHealthID_CardService;
import com.wipro.fhir.utils.exception.FHIRException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {HealthID_CardServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class HealthID_CardServiceImplDiffblueTest {
    @MockBean
    private GenerateHealthID_CardService generateHealthID_CardService;

    @Autowired
    private HealthID_CardServiceImpl healthID_CardServiceImpl;

    /**
     * Method under test: {@link HealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.generateOTP(Mockito.<String>any())).thenReturn("Generate OTP");

        // Act
        String actualGenerateOTPResult = healthID_CardServiceImpl.generateOTP("Request");

        // Assert
        verify(generateHealthID_CardService).generateOTP(eq("Request"));
        assertEquals("Generate OTP", actualGenerateOTPResult);
    }

    /**
     * Method under test: {@link HealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP2() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.generateOTP(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_CardServiceImpl.generateOTP("Request"));
        verify(generateHealthID_CardService).generateOTP(eq("Request"));
    }

    /**
     * Method under test: {@link HealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP3() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.generateOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_CardServiceImpl.generateOTP("Request"));
        verify(generateHealthID_CardService).generateOTP(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthID_CardServiceImpl#verifyOTPAndGenerateCard(String)}
     */
    @Test
    void testVerifyOTPAndGenerateCard() throws FHIRException {
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

    /**
     * Method under test:
     * {@link HealthID_CardServiceImpl#verifyOTPAndGenerateCard(String)}
     */
    @Test
    void testVerifyOTPAndGenerateCard2() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.generateCard(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));
        when(generateHealthID_CardService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_CardServiceImpl.verifyOTPAndGenerateCard("Request"));
        verify(generateHealthID_CardService).generateCard(eq("Request"), eq("2020-03-01"));
        verify(generateHealthID_CardService).validateOTP(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthID_CardServiceImpl#verifyOTPAndGenerateCard(String)}
     */
    @Test
    void testVerifyOTPAndGenerateCard3() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.generateCard(Mockito.<String>any(), Mockito.<String>any())).thenReturn(null);
        when(generateHealthID_CardService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_CardServiceImpl.verifyOTPAndGenerateCard("Request"));
        verify(generateHealthID_CardService).generateCard(eq("Request"), eq("2020-03-01"));
        verify(generateHealthID_CardService).validateOTP(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthID_CardServiceImpl#verifyOTPAndGenerateCard(String)}
     */
    @Test
    void testVerifyOTPAndGenerateCard4() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.generateCard(Mockito.<String>any(), Mockito.<String>any())).thenReturn("");
        when(generateHealthID_CardService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act
        String actualVerifyOTPAndGenerateCardResult = healthID_CardServiceImpl.verifyOTPAndGenerateCard("Request");

        // Assert
        verify(generateHealthID_CardService).generateCard(eq("Request"), eq("2020-03-01"));
        verify(generateHealthID_CardService).validateOTP(eq("Request"));
        assertEquals("{\"data\":\"\"}", actualVerifyOTPAndGenerateCardResult);
    }

    /**
     * Method under test:
     * {@link HealthID_CardServiceImpl#verifyOTPAndGenerateCard(String)}
     */
    @Test
    void testVerifyOTPAndGenerateCard5() throws FHIRException {
        // Arrange
        when(generateHealthID_CardService.validateOTP(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_CardServiceImpl.verifyOTPAndGenerateCard("Request"));
        verify(generateHealthID_CardService).validateOTP(eq("Request"));
    }
}
