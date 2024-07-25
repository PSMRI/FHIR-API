package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.CreateHealthID_Aadhaar_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

@ExtendWith(SpringExtension.class)
class HealthIDWithUIDServiceImplTest {
    @Mock
    private CreateHealthID_Aadhaar_NDHMService createHealthID_Aadhaar_NDHMService;

    @Mock
    private HealthIDRepo healthIDRepo;

    @InjectMocks
    private HealthIDWithUIDServiceImpl healthIDWithUIDServiceImpl;

    @Test
    @Description("Tests successful generation of a One-Time Password (OTP). (TC_GenerateOTP_Success_001)")
    void testGenerateOTP_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateOTP(Mockito.<String>any())).thenReturn("Generate OTP");

        // Act
        String actualGenerateOTPResult = healthIDWithUIDServiceImpl.generateOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).generateOTP(eq("Request"));
        assertEquals("Generate OTP", actualGenerateOTPResult);
    }

    @Test
    @Description("Tests handling of failures during OTP generation (e.g., invalid data, dependency issues). (TC_GenerateOTP_Failure_002)")
    void testGenerateOTP_Failure() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateOTP(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.generateOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).generateOTP(eq("Request"));
    }

    @Test
    @Description("Tests handling of exceptions during OTP generation(TC_GenerateOTP_Exception_003)")
    void testGenerateOTP_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.generateOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).generateOTP(eq("Request"));
    }

    @Test
    @Description("Tests successful verification of a provided OTP. (TC_VerifyOTP_Success_001)")
    void testVerifyOTP_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyOTP(Mockito.<String>any())).thenReturn("Verify OTP");

        // Act
        String actualVerifyOTPResult = healthIDWithUIDServiceImpl.verifyOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).verifyOTP(eq("Request"));
        assertEquals("Verify OTP", actualVerifyOTPResult);
    }

    @Test
    @Description("Tests handling of failures during OTP verification (e.g., invalid OTP, expired OTP). (TC_VerifyOTP_Failure_002)")
    void testVerifyOTP_Failure() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyOTP(null));
    }

    @Test
    @Description(" Tests handling of exceptions during OTP verification(TC_VerifyOTP_Exception_003)")
    void testVerifyOTP_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).verifyOTP(eq("Request"));
    }

    @Test
    @Description("Tests successful logic for checking existing OTP validity and generating a new one if needed. (TC_CheckAndGenerateOTP_Success_001)")
    void testCheckAndGenerateOTP_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.checkAndGenerateMobileOTP(Mockito.<String>any()))
                .thenReturn("Check And Generate Mobile OTP");

        // Act
        String actualCheckAndGenerateOTPResult = healthIDWithUIDServiceImpl.checkAndGenerateOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).checkAndGenerateMobileOTP(eq("Request"));
        assertEquals("Check And Generate Mobile OTP", actualCheckAndGenerateOTPResult);
    }

    @Test
    @Description(" Tests handling of failures during the check and generate OTP logic (e.g., issues verifying existing OTP, dependency failures during generation). (TC_CheckAndGenerateOTP_Failure_002)")
    void testCheckAndGenerateOTP_Failure() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.checkAndGenerateOTP(null));
    }

    @Test
    @Description("Tests handling of exceptions during the check and generate OTP logic (TC_CheckAndGenerateOTP_Exception_003)")
    void testCheckAndGenerateOTP_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.checkAndGenerateMobileOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.checkAndGenerateOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).checkAndGenerateMobileOTP(eq("Request"));
    }

    @Test
    @Description("Tests successful verification of a mobile OTP. (TC_VerifyMobileOTP_Success_001)")
    void testVerifyMobileOTP_Success() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyMobileOTP(Mockito.<String>any())).thenReturn("Verify Mobile OTP");

        // Act
        String actualVerifyMobileOTPResult = healthIDWithUIDServiceImpl.verifyMobileOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).verifyMobileOTP(eq("Request"));
        assertEquals("Verify Mobile OTP", actualVerifyMobileOTPResult);
    }

    @Test
    @Description("Tests handling of failures during mobile OTP verification (e.g., invalid OTP, expired OTP, network issues). (TC_VerifyMobileOTP_Failure_002)")
    void testVerifyMobileOTP_Failure() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyMobileOTP(null));
    }

    @Test
    @Description("Tests handling of exceptions during mobile OTP verification (e.g., unexpected errors in your service logic). (TC_VerifyMobileOTP_Exception_003)")
    void testVerifyMobileOTP_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyMobileOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyMobileOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).verifyMobileOTP(eq("Request"));
    }

    @Test
    @Description("Tests handling of exceptions during Health ID creation using a Unique Identifier (UID). (TC_CreateHealthIDWithUID_Exception_001)")
    void testCreateHealthIDWithUID_Exception() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID("Request"));
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID("42"));
    }

    @Test
    @Description("Tests handling of failures during Health ID creation using a UID (e.g., invalid UID format, dependency issues). (TC_CreateHealthIDWithUID_Failure_002)")
    void testCreateHealthIDWithUID_Failure() throws FHIRException {
        // Arrange
        HealthIDResponse healthIDResponse = new HealthIDResponse();
        healthIDResponse.setAuthMethod("Auth Method");
        healthIDResponse.setAuthMethods(new ArrayList<>());
        healthIDResponse.setAuthenticationMode("Authentication Mode");
        healthIDResponse.setBeneficiaryRegId(1L);
        healthIDResponse.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        healthIDResponse.setCreatedDate(mock(Timestamp.class));
        healthIDResponse.setDayOfBirth("Day Of Birth");
        healthIDResponse.setDeleted(true);
        healthIDResponse.setDistrictCode("District Code");
        healthIDResponse.setDistrictName("District Name");
        healthIDResponse.setEmail("jane.doe@example.org");
        healthIDResponse.setFirstName("Jane");
        healthIDResponse.setGender("Gender");
        healthIDResponse.setHealthId("42");
        healthIDResponse.setHealthIdNumber("42");
        healthIDResponse.setKycPhoto("alice.liddell@example.org");
        healthIDResponse.setLastModDate(mock(Timestamp.class));
        healthIDResponse.setLastName("Doe");
        healthIDResponse.setMiddleName("Middle Name");
        healthIDResponse.setMobile("Mobile");
        healthIDResponse.setModifiedBy("Jan 1, 2020 9:00am GMT+0100");
        healthIDResponse.setMonthOfBirth("Month Of Birth");
        healthIDResponse.setName("Name");
        healthIDResponse.setProcessed("Processed");
        healthIDResponse.setProviderServiceMapID(1);
        healthIDResponse.setStateCode("MD");
        healthIDResponse.setStateName("MD");
        healthIDResponse.setTxnId("42");
        healthIDResponse.setYearOfBirth("Year Of Birth");
        healthIDResponse.sethID(1);
        when(createHealthID_Aadhaar_NDHMService.createHealthIDWithUID(Mockito.<String>any())).thenReturn(healthIDResponse);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID(""));
        verify(createHealthID_Aadhaar_NDHMService).createHealthIDWithUID(eq(""));
    }
}
