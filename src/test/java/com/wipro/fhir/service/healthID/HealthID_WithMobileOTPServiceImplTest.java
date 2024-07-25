package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.gson.Gson;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.CreateHealthID_MobileOTP_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@ExtendWith(SpringExtension.class)
class HealthID_WithMobileOTPServiceImplTest {
    @Mock
    private CreateHealthID_MobileOTP_NDHMService createHealthID_MobileOTP_NDHMService;

    @Mock
    private HealthIDRepo healthIDRepo;

    @InjectMocks
    private HealthID_WithMobileOTPServiceImpl healthID_WithMobileOTPServiceImpl;

    @Mock
    private HttpUtils httpUtils;
    
    @Mock
    private Gson gson;

    @Test
    @Description("Tests successful generation and delivery of a One-Time Password (OTP) (TC_GenerateOTP_Success_001)")
    void testGenerateOTP_Success() throws FHIRException {
        // Arrange
        when(createHealthID_MobileOTP_NDHMService.generateOTP(Mockito.<String>any())).thenReturn("Generate OTP");

        // Act
        String actualGenerateOTPResult = healthID_WithMobileOTPServiceImpl.generateOTP("Request");

        // Assert
        verify(createHealthID_MobileOTP_NDHMService).generateOTP(eq("Request"));
        assertEquals("Generate OTP", actualGenerateOTPResult);
    }

    @Test
    @Description("Tests handling of invalid phone number format during OTP generation (TC_GenerateOTP_InvalidPhoneNumber_002)")
    void testGenerateOTP_OTPValidationFailure() throws FHIRException {
        // Arrange
        when(createHealthID_MobileOTP_NDHMService.generateOTP(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.generateOTP("Request"));
        verify(createHealthID_MobileOTP_NDHMService).generateOTP(eq("Request"));
    }

    @Test
    @Description("Tests handling of exceptions during OTP generation (TC_GenerateOTP_Exception_003)")
    void testGenerateOTP_Exception() throws FHIRException {
        // Arrange
        when(createHealthID_MobileOTP_NDHMService.generateOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.generateOTP("Request"));
        verify(createHealthID_MobileOTP_NDHMService).generateOTP(eq("Request"));
    }
   
    @Test
    void testVerifyOTPandGenerateHealthID() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID("Request"));
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID("42"));
        assertThrows(FHIRException.class,
                () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID("NDHM_FHIR Error while validating OTP"));
    }

    @Test
    void testVerifyOTPandGenerateHealthID2() throws FHIRException {
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
        when(createHealthID_MobileOTP_NDHMService.createHealthID(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(healthIDResponse);
        when(createHealthID_MobileOTP_NDHMService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID(""));
        verify(createHealthID_MobileOTP_NDHMService).createHealthID(eq(""), eq("2020-03-01"));
        verify(createHealthID_MobileOTP_NDHMService).validateOTP(eq(""));
    }

    @Test
    void testVerifyOTPandGenerateHealthID3() throws FHIRException {
        // Arrange
        when(createHealthID_MobileOTP_NDHMService.createHealthID(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));
        when(createHealthID_MobileOTP_NDHMService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID(""));
        verify(createHealthID_MobileOTP_NDHMService).createHealthID(eq(""), eq("2020-03-01"));
        verify(createHealthID_MobileOTP_NDHMService).validateOTP(eq(""));
    }

    @Test
    void testVerifyOTPandGenerateHealthID4() throws FHIRException {
        // Arrange
        ArrayList<String> authMethods = new ArrayList<>();
        authMethods.add("foo");

        HealthIDResponse healthIDResponse = new HealthIDResponse();
        healthIDResponse.setAuthMethod("Auth Method");
        healthIDResponse.setAuthMethods(authMethods);
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
        when(createHealthID_MobileOTP_NDHMService.createHealthID(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(healthIDResponse);
        when(createHealthID_MobileOTP_NDHMService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID(""));
        verify(createHealthID_MobileOTP_NDHMService).createHealthID(eq(""), eq("2020-03-01"));
        verify(createHealthID_MobileOTP_NDHMService).validateOTP(eq(""));
    }

    @Test
    void testVerifyOTPandGenerateHealthID5() throws FHIRException {
        // Arrange
        when(createHealthID_MobileOTP_NDHMService.validateOTP(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID(""));
        verify(createHealthID_MobileOTP_NDHMService).validateOTP(eq(""));
    }

    @Test
    void testVerifyOTPandGenerateHealthID6() throws FHIRException {
        // Arrange
        when(createHealthID_MobileOTP_NDHMService.createHealthID(Mockito.<String>any(), Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));
        when(createHealthID_MobileOTP_NDHMService.validateOTP(Mockito.<String>any())).thenReturn("2020-03-01");

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthID_WithMobileOTPServiceImpl.verifyOTPandGenerateHealthID(null));
        verify(createHealthID_MobileOTP_NDHMService).createHealthID(isNull(), eq("2020-03-01"));
        verify(createHealthID_MobileOTP_NDHMService).validateOTP(isNull());
    }
}
