package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@ExtendWith(SpringExtension.class)
class GenerateSession_NDHMServiceImplTest {
    @Mock
    private Common_NDHMService common_NDHMService;

    @InjectMocks
    private GenerateSession_NDHMServiceImpl generateSession_NDHMServiceImpl;

    @Mock
    private HttpUtils httpUtils;

    @Test
    void testGenerateNDHMAuthToken() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.generateNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateNDHMAuthToken2() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.generateNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateNDHMAuthToken3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.generateNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateNDHMAuthToken4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.generateNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateNDHMAuthToken5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn(null);
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.generateNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGenerateNDHMAuthToken6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("0.9E9");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.generateNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGetNDHMAuthToken() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("Not all who wander are lost");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.getNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGetNDHMAuthToken2() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("foo");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.getNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGetNDHMAuthToken3() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("42");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.getNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGetNDHMAuthToken4() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.getNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGetNDHMAuthToken5() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn(null);
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.getNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }

    @Test
    void testGetNDHMAuthToken6() throws FHIRException {
        // Arrange
        when(common_NDHMService.getBody(Mockito.<ResponseEntity<String>>any())).thenReturn("0.9E9");
        when(common_NDHMService.getHeaders(Mockito.<String>any())).thenReturn(new HttpHeaders());
        when(httpUtils.postWithResponseEntity(Mockito.<String>any(), Mockito.<String>any(), Mockito.<HttpHeaders>any()))
                .thenReturn(null);

        // Act and Assert
        assertThrows(FHIRException.class, () -> generateSession_NDHMServiceImpl.getNDHMAuthToken());
        verify(common_NDHMService).getBody(Mockito.<ResponseEntity<String>>any());
        verify(common_NDHMService).getHeaders(isNull());
        verify(httpUtils).postWithResponseEntity(eq("https://dev.abdm.gov.in/gateway/v0.5/sessions"),
                eq("{\"clientId\":\"PIRSWASTH_953256\",\"clientSecret\":\"c3631213-60ea-4a10-8e30-43523d3e0253\"}"),
                Mockito.<HttpHeaders>any());
    }
}
