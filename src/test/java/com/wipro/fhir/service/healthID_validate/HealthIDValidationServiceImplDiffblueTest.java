package com.wipro.fhir.service.healthID_validate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.service.healthID.HealthIDServiceImpl;
import com.wipro.fhir.service.ndhm.ValidateHealthID_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {HealthIDValidationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class HealthIDValidationServiceImplDiffblueTest {
    @MockBean
    private HealthIDServiceImpl healthIDServiceImpl;

    @Autowired
    private HealthIDValidationServiceImpl healthIDValidationServiceImpl;

    @MockBean
    private HttpUtils httpUtils;

    @MockBean
    private ValidateHealthID_NDHMService validateHealthID_NDHMService;

    /**
     * Method under test:
     * {@link HealthIDValidationServiceImpl#generateOTPForHealthIDValidation(String)}
     */
    @Test
    void testGenerateOTPForHealthIDValidation() throws FHIRException {
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

    /**
     * Method under test:
     * {@link HealthIDValidationServiceImpl#generateOTPForHealthIDValidation(String)}
     */
    @Test
    void testGenerateOTPForHealthIDValidation2() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.generateOTPForHealthIDValidation("Request"));
        verify(validateHealthID_NDHMService).generateOTPForHealthIDValidation(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDValidationServiceImpl#generateOTPForHealthIDValidation(String)}
     */
    @Test
    void testGenerateOTPForHealthIDValidation3() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.generateOTPForHealthIDValidation(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.generateOTPForHealthIDValidation("Request"));
        verify(validateHealthID_NDHMService).generateOTPForHealthIDValidation(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDValidationServiceImpl#validateOTPAndHealthID(String)}
     */
    @Test
    void testValidateOTPAndHealthID() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act
        String actualValidateOTPAndHealthIDResult = healthIDValidationServiceImpl.validateOTPAndHealthID("Request");

        // Assert
        verify(validateHealthID_NDHMService).validateOTPForHealthIDValidation(eq("Request"));
        assertEquals("2020-03-01", actualValidateOTPAndHealthIDResult);
    }

    /**
     * Method under test:
     * {@link HealthIDValidationServiceImpl#validateOTPAndHealthID(String)}
     */
    @Test
    void testValidateOTPAndHealthID2() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.validateOTPAndHealthID("Request"));
        verify(validateHealthID_NDHMService).validateOTPForHealthIDValidation(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDValidationServiceImpl#validateOTPAndHealthID(String)}
     */
    @Test
    void testValidateOTPAndHealthID3() throws FHIRException {
        // Arrange
        when(validateHealthID_NDHMService.validateOTPForHealthIDValidation(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDValidationServiceImpl.validateOTPAndHealthID("Request"));
        verify(validateHealthID_NDHMService).validateOTPForHealthIDValidation(eq("Request"));
    }
}
