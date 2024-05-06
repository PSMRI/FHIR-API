package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.data.mongo.care_context.NDHMResponse;
import com.wipro.fhir.repo.mongo.ndhm_response.NDHMResponseRepo;
import com.wipro.fhir.utils.exception.FHIRException;

import java.util.List;

import org.junit.jupiter.api.Disabled;
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

@ContextConfiguration(classes = {Common_NDHMServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class Common_NDHMServiceImplDiffblueTest {
    @Autowired
    private Common_NDHMServiceImpl common_NDHMServiceImpl;

    @MockBean
    private NDHMResponseRepo nDHMResponseRepo;

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getHeaders(String)}
     */
    @Test
    void testGetHeaders() {
        // Arrange and Act
        HttpHeaders actualHeaders = common_NDHMServiceImpl.getHeaders("ABC123");

        // Assert
        assertEquals(2, actualHeaders.size());
        List<String> getResult = actualHeaders.get(HttpHeaders.AUTHORIZATION);
        assertEquals(1, getResult.size());
        assertEquals("ABC123", getResult.get(0));
        List<String> getResult2 = actualHeaders.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult2.size());
        assertEquals("application/json", getResult2.get(0));
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getHeaders(String)}
     */
    @Test
    void testGetHeaders2() {
        // Arrange and Act
        HttpHeaders actualHeaders = common_NDHMServiceImpl.getHeaders(null);

        // Assert
        assertEquals(1, actualHeaders.size());
        List<String> getResult = actualHeaders.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getHeaders(String, String)}
     */
    @Test
    void testGetHeaders3() {
        // Arrange and Act
        HttpHeaders actualHeaders = common_NDHMServiceImpl.getHeaders("ABC123", "Sbx");

        // Assert
        assertEquals(3, actualHeaders.size());
        List<String> getResult = actualHeaders.get(HttpHeaders.AUTHORIZATION);
        assertEquals(1, getResult.size());
        assertEquals("ABC123", getResult.get(0));
        List<String> getResult2 = actualHeaders.get("X-CM-ID");
        assertEquals(1, getResult2.size());
        assertEquals("Sbx", getResult2.get(0));
        List<String> getResult3 = actualHeaders.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult3.size());
        assertEquals("application/json", getResult3.get(0));
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getHeaders(String, String)}
     */
    @Test
    void testGetHeaders4() {
        // Arrange and Act
        HttpHeaders actualHeaders = common_NDHMServiceImpl.getHeaders(null, "Sbx");

        // Assert
        assertEquals(2, actualHeaders.size());
        List<String> getResult = actualHeaders.get("X-CM-ID");
        assertEquals(1, getResult.size());
        assertEquals("Sbx", getResult.get(0));
        List<String> getResult2 = actualHeaders.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult2.size());
        assertEquals("application/json", getResult2.get(0));
    }

    /**
     * Method under test:
     * {@link Common_NDHMServiceImpl#getHeadersWithXtoken(String, String)}
     */
    @Test
    void testGetHeadersWithXtoken() {
        // Arrange and Act
        HttpHeaders actualHeadersWithXtoken = common_NDHMServiceImpl.getHeadersWithXtoken("ABC123", "ABC123");

        // Assert
        assertEquals(3, actualHeadersWithXtoken.size());
        List<String> getResult = actualHeadersWithXtoken.get("X-Token");
        assertEquals(1, getResult.size());
        assertEquals("ABC123", getResult.get(0));
        List<String> getResult2 = actualHeadersWithXtoken.get(HttpHeaders.AUTHORIZATION);
        assertEquals(1, getResult2.size());
        assertEquals("ABC123", getResult2.get(0));
        List<String> getResult3 = actualHeadersWithXtoken.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult3.size());
        assertEquals("application/json", getResult3.get(0));
    }

    /**
     * Method under test:
     * {@link Common_NDHMServiceImpl#getHeadersWithXtoken(String, String)}
     */
    @Test
    void testGetHeadersWithXtoken2() {
        // Arrange and Act
        HttpHeaders actualHeadersWithXtoken = common_NDHMServiceImpl.getHeadersWithXtoken(null, "ABC123");

        // Assert
        assertEquals(2, actualHeadersWithXtoken.size());
        List<String> getResult = actualHeadersWithXtoken.get("X-Token");
        assertEquals(1, getResult.size());
        assertEquals("ABC123", getResult.get(0));
        List<String> getResult2 = actualHeadersWithXtoken.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult2.size());
        assertEquals("application/json", getResult2.get(0));
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getRequestIDAndTimeStamp()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetRequestIDAndTimeStamp() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Method may be time-sensitive.
        //   Diffblue Cover was only able to write tests that are time-sensitive.
        //   The assertions don't pass when run at an alternate date, time, and
        //   timezone. Try refactoring the method to take a java.time.Clock instance so
        //   that the time can be parameterized during testing.
        //   See Working with code R031 (https://diff.blue/R031) for details.

        // Arrange and Act
        common_NDHMServiceImpl.getRequestIDAndTimeStamp();
    }

    /**
     * Method under test:
     * {@link Common_NDHMServiceImpl#getMongoNDHMResponse(String)}
     */
    @Test
    void testGetMongoNDHMResponse() throws FHIRException {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> (new Common_NDHMServiceImpl()).getMongoNDHMResponse("Request ID"));
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getResponseMongo(String)}
     */
    @Test
    void testGetResponseMongo() {
        // Arrange
        NDHMResponse ndhmResponse = new NDHMResponse();
        ndhmResponse.setId("42");
        ndhmResponse.setRequestID("Request ID");
        ndhmResponse.setResponseData("Response Data");
        when(nDHMResponseRepo.findByRequestID(Mockito.<String>any())).thenReturn(ndhmResponse);

        // Act
        NDHMResponse actualResponseMongo = common_NDHMServiceImpl.getResponseMongo("Req ID");

        // Assert
        verify(nDHMResponseRepo).findByRequestID(eq("Req ID"));
        assertSame(ndhmResponse, actualResponseMongo);
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getBody(ResponseEntity)}
     */
    @Test
    void testGetBody() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> common_NDHMServiceImpl.getBody(null));
    }

    /**
     * Method under test: {@link Common_NDHMServiceImpl#getBody(ResponseEntity)}
     */
    @Test
    void testGetBody2() throws FHIRException {
        // Arrange
        ResponseEntity<String> res = mock(ResponseEntity.class);
        when(res.getBody()).thenReturn("https://example.org/example");

        // Act
        String actualBody = common_NDHMServiceImpl.getBody(res);

        // Assert
        verify(res).getBody();
        assertEquals("https://example.org/example", actualBody);
    }

    /**
     * Method under test:
     * {@link Common_NDHMServiceImpl#getStatusCode(ResponseEntity)}
     */
    @Test
    void testGetStatusCode() throws FHIRException {
        // Arrange, Act and Assert
        assertThrows(FHIRException.class, () -> common_NDHMServiceImpl.getStatusCode(null));
    }
}
