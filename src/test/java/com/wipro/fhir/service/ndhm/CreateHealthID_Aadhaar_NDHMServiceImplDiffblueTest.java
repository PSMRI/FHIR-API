package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
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

@ContextConfiguration(classes = {CreateHealthID_Aadhaar_NDHMServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CreateHealthID_Aadhaar_NDHMServiceImplDiffblueTest {
    @MockBean
    private Common_NDHMService common_NDHMService;

    @Autowired
    private CreateHealthID_Aadhaar_NDHMServiceImpl createHealthID_Aadhaar_NDHMServiceImpl;

    @MockBean
    private GenerateSession_NDHMService generateSession_NDHMService;

    @MockBean
    private HttpUtils httpUtils;

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateOtp"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateOtp"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateOtp"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateOTP(String)}
     */
    @Test
    void testGenerateOTP6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateOtp"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn(null);
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(mock(ResponseEntity.class));

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP7() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(null));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP8() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP("tnxId"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP9() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP10() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyOTP(String)}
     */
    @Test
    void testVerifyOTP11() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn(null);
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(mock(ResponseEntity.class));

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP7() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(null));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP8() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP("tnxId"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP9() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP10() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#checkAndGenerateMobileOTP(String)}
     */
    @Test
    void testCheckAndGenerateMobileOTP11() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.checkAndGenerateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/checkAndGenerateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn(null);
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(mock(ResponseEntity.class));

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP7() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(null));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP8() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP("tnxId"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP9() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP10() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyMobileOTP(String)}
     */
    @Test
    void testVerifyMobileOTP11() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.createHealthIDWithUID("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.createHealthIDWithUID("N"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.createHealthIDWithUID("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.createHealthIDWithUID(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/createHealthIdWithPreVerified"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.createHealthIDWithUID(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/createHealthIdWithPreVerified"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#createHealthIDWithUID(String)}
     */
    @Test
    void testCreateHealthIDWithUID6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act
        HealthIDResponse actualCreateHealthIDWithUIDResult = createHealthID_Aadhaar_NDHMServiceImpl
                .createHealthIDWithUID("");

        // Assert
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/createHealthIdWithPreVerified"), eq("null"),
                Mockito.<HttpHeaders>any());
        assertNull(actualCreateHealthIDWithUIDResult);
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyBio("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyBio("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyBio"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyBio"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyBio"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#verifyBio(String)}
     */
    @Test
    void testVerifyBio6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.verifyBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/verifyBio"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn(null);
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(mock(ResponseEntity.class));

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP7() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(42);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(null));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP8() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP("tnxId"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP9() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP10() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#generateMobileOTP(String)}
     */
    @Test
    void testGenerateMobileOTP11() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        ResponseEntity<String> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(responseEntity);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.generateMobileOTP(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(
                eq("https://healthidsbx.abdm.gov.in/api/v1/registration/aadhaar/generateMobileOTP"), eq("null"),
                Mockito.<HttpHeaders>any());
        verify(responseEntity).getStatusCodeValue();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.confirmWithAadhaarBio("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.confirmWithAadhaarBio("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.confirmWithAadhaarBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("https://healthidsbx.abdm.gov.in/api/v1/auth/confirmWithAadhaarBio"),
                eq("null"), Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.confirmWithAadhaarBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("https://healthidsbx.abdm.gov.in/api/v1/auth/confirmWithAadhaarBio"),
                eq("null"), Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.confirmWithAadhaarBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("https://healthidsbx.abdm.gov.in/api/v1/auth/confirmWithAadhaarBio"),
                eq("null"), Mockito.<HttpHeaders>any());
    }

    /**
     * Method under test:
     * {@link CreateHealthID_Aadhaar_NDHMServiceImpl#confirmWithAadhaarBio(String)}
     */
    @Test
    void testConfirmWithAadhaarBio6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> createHealthID_Aadhaar_NDHMServiceImpl.confirmWithAadhaarBio(""));
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(eq("ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
        verify(httpUtils).postWithResponseEntity(eq("https://healthidsbx.abdm.gov.in/api/v1/auth/confirmWithAadhaarBio"),
                eq("null"), Mockito.<HttpHeaders>any());
    }
}
