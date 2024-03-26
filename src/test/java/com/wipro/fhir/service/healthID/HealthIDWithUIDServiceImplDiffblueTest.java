package com.wipro.fhir.service.healthID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.CreateHealthID_Aadhaar_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {HealthIDWithUIDServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class HealthIDWithUIDServiceImplDiffblueTest {
    @MockBean
    private CreateHealthID_Aadhaar_NDHMService createHealthID_Aadhaar_NDHMService;

    @MockBean
    private HealthIDRepo healthIDRepo;

    @Autowired
    private HealthIDWithUIDServiceImpl healthIDWithUIDServiceImpl;

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateOTP(Mockito.<String>any())).thenReturn("Generate OTP");

        // Act
        String actualGenerateOTPResult = healthIDWithUIDServiceImpl.generateOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).generateOTP(eq("Request"));
        assertEquals("Generate OTP", actualGenerateOTPResult);
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP2() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateOTP(Mockito.<String>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.generateOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).generateOTP(eq("Request"));
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.generateOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.generateOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).generateOTP(eq("Request"));
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyOTP(Mockito.<String>any())).thenReturn("Verify OTP");

        // Act
        String actualVerifyOTPResult = healthIDWithUIDServiceImpl.verifyOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).verifyOTP(eq("Request"));
        assertEquals("Verify OTP", actualVerifyOTPResult);
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP2() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyOTP(null));
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).verifyOTP(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#checkAndGenerateOTP(String)}
     */
    @Test
    void testCheckAndGenerateOTP() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.checkAndGenerateMobileOTP(Mockito.<String>any()))
                .thenReturn("Check And Generate Mobile OTP");

        // Act
        String actualCheckAndGenerateOTPResult = healthIDWithUIDServiceImpl.checkAndGenerateOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).checkAndGenerateMobileOTP(eq("Request"));
        assertEquals("Check And Generate Mobile OTP", actualCheckAndGenerateOTPResult);
    }

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#checkAndGenerateOTP(String)}
     */
    @Test
    void testCheckAndGenerateOTP2() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.checkAndGenerateOTP(null));
    }

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#checkAndGenerateOTP(String)}
     */
    @Test
    void testCheckAndGenerateOTP3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.checkAndGenerateMobileOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.checkAndGenerateOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).checkAndGenerateMobileOTP(eq("Request"));
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyMobileOTP(Mockito.<String>any())).thenReturn("Verify Mobile OTP");

        // Act
        String actualVerifyMobileOTPResult = healthIDWithUIDServiceImpl.verifyMobileOTP("Request");

        // Assert
        verify(createHealthID_Aadhaar_NDHMService).verifyMobileOTP(eq("Request"));
        assertEquals("Verify Mobile OTP", actualVerifyMobileOTPResult);
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP2() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyMobileOTP(null));
    }

    /**
     * Method under test: {@link HealthIDWithUIDServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP3() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.verifyMobileOTP(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.verifyMobileOTP("Request"));
        verify(createHealthID_Aadhaar_NDHMService).verifyMobileOTP(eq("Request"));
    }

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID("Request"));
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID("42"));
    }

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID2() throws FHIRException {
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

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID3() throws FHIRException {
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
        when(createHealthID_Aadhaar_NDHMService.createHealthIDWithUID(Mockito.<String>any())).thenReturn(healthIDResponse);

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID(""));
        verify(createHealthID_Aadhaar_NDHMService).createHealthIDWithUID(eq(""));
    }

    /**
     * Method under test:
     * {@link HealthIDWithUIDServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID4() throws FHIRException {
        // Arrange
        when(createHealthID_Aadhaar_NDHMService.createHealthIDWithUID(Mockito.<String>any()))
                .thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> healthIDWithUIDServiceImpl.createHealthIDWithUID(""));
        verify(createHealthID_Aadhaar_NDHMService).createHealthIDWithUID(eq(""));
    }
}
