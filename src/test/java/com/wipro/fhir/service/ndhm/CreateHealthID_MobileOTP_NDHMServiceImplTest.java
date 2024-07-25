package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@ExtendWith(SpringExtension.class)
class CreateHealthID_MobileOTP_NDHMServiceImplTest {
    @Mock
    private Common_NDHMService common_NDHMService;

    @InjectMocks
    private CreateHealthID_MobileOTP_NDHMServiceImpl createHealthID_MobileOTP_NDHMServiceImpl;

    @Mock
    private GenerateSession_NDHMService generateSession_NDHMService;

    @Mock
    private HttpUtils httpUtils;

    @Test
    void testGenerateOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testGenerateOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testGenerateOTP3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP("token"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testGenerateOTP4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("${ndhmGenerateOTP}"), eq("null"), Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateOTP5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("${ndhmGenerateOTP}"), eq("null"), Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateOTP6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("${ndhmGenerateOTP}"), eq("null"), Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateOTP7() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("${ndhmGenerateOTP}"), eq("null"), Mockito.<HttpHeaders>any());
    }

    @Test
    void testValidateOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.validateOTP("Req"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.validateOTP("com.wipro.fhir.data.healthID.SendOTP"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTP3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.validateOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTP4() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.validateOTP("token"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTP5() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.validateOTP(""));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTP6() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl.validateOTP("Req"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testCreateHealthID() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.createHealthID("Req", "NDHM OTP TOKEN"));
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.createHealthID("N", "NDHM OTP TOKEN"));
        assertThrows(FHIRException.class, () -> createHealthID_MobileOTP_NDHMServiceImpl
                .createHealthID("com.wipro.fhir.data.healthID.SendOTP", "NDHM OTP TOKEN"));
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.createHealthID("42", "NDHM OTP TOKEN"));
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.createHealthID("token", "NDHM OTP TOKEN"));
    }

    @Test
    void testCreateHealthID2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.createHealthID("", "NDHM OTP TOKEN"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testCreateHealthID3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> createHealthID_MobileOTP_NDHMServiceImpl.createHealthID("", "NDHM OTP TOKEN"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }
}
