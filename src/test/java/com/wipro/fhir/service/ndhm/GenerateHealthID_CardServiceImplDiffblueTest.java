package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GenerateHealthID_CardServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class GenerateHealthID_CardServiceImplDiffblueTest {
    @MockBean
    private Common_NDHMService common_NDHMService;

    @Autowired
    private GenerateHealthID_CardServiceImpl generateHealthID_CardServiceImpl;

    @MockBean
    private GenerateSession_NDHMService generateSession_NDHMService;

    @MockBean
    private HttpUtils httpUtils;

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateOTP(""));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP4() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP5() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateOTP("token"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#validateOTP(String)}
     */
    @Test
    void testValidateOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.validateOTP("Req"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#validateOTP(String)}
     */
    @Test
    void testValidateOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.validateOTP("AADHAAR_OTP"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#validateOTP(String)}
     */
    @Test
    void testValidateOTP3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.validateOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#validateOTP(String)}
     */
    @Test
    void testValidateOTP4() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.validateOTP(""));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#validateOTP(String)}
     */
    @Test
    void testValidateOTP5() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.validateOTP("Req"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#validateOTP(String)}
     */
    @Test
    void testValidateOTP6() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.validateOTP("token"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateCard(String, String)}
     */
    @Test
    void testGenerateCard() throws FHIRException {
        // Arrange
        when(common_NDHMService.getHeadersWithXtoken(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.getWithResponseEntityByte(Mockito.<String>any(), Mockito.<HttpHeaders>any())).thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateCard("Req", "NDHM X TOKEN"));
        verify(common_NDHMService).getHeadersWithXtoken(eq("ABC123"), eq("NDHM X TOKEN"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).getWithResponseEntityByte(eq("${generateHealthCard}"), Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateCard(String, String)}
     */
    @Test
    void testGenerateCard2() throws FHIRException, UnsupportedEncodingException {
        // Arrange
        when(common_NDHMService.getHeadersWithXtoken(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<byte[]> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn("AXAXAXAX".getBytes("UTF-8"));
        when(httpUtils.getWithResponseEntityByte(Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act
        String actualGenerateCardResult = generateHealthID_CardServiceImpl.generateCard("Req", "NDHM X TOKEN");

        // Assert
        verify(common_NDHMService).getHeadersWithXtoken(eq("ABC123"), eq("NDHM X TOKEN"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).getWithResponseEntityByte(eq("${generateHealthCard}"), Mockito.<HttpHeaders>any());
        verify(responseEntity).getBody();
        assertEquals("QVhBWEFYQVg=", actualGenerateCardResult);
    }

    /**
     * Method under test:
     * {@link GenerateHealthID_CardServiceImpl#generateCard(String, String)}
     */
    @Test
    void testGenerateCard3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getHeadersWithXtoken(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<byte[]> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(null);
        when(httpUtils.getWithResponseEntityByte(Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateHealthID_CardServiceImpl.generateCard("Req", "NDHM X TOKEN"));
        verify(common_NDHMService).getHeadersWithXtoken(eq("ABC123"), eq("NDHM X TOKEN"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).getWithResponseEntityByte(eq("${generateHealthCard}"), Mockito.<HttpHeaders>any());
        verify(responseEntity).getBody();
    }
}
