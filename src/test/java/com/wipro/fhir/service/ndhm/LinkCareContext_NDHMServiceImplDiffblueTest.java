package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wipro.fhir.data.mongo.care_context.AddCareContext;
import com.wipro.fhir.data.mongo.care_context.CareContextPatient;
import com.wipro.fhir.data.mongo.care_context.Credential;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.Query;
import com.wipro.fhir.data.mongo.care_context.Requester;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {LinkCareContext_NDHMServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class LinkCareContext_NDHMServiceImplDiffblueTest {
    @MockBean
    private Common_NDHMService common_NDHMService;

    @MockBean
    private GenerateSession_NDHMService generateSession_NDHMService;

    @MockBean
    private HttpUtils httpUtils;

    @Autowired
    private LinkCareContext_NDHMServiceImpl linkCareContext_NDHMServiceImpl;

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#generateOTPForCareContext(String)}
     */
    @Test
    void testGenerateOTPForCareContext() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.generateOTPForCareContext("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#generateOTPForCareContext(String)}
     */
    @Test
    void testGenerateOTPForCareContext2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.generateOTPForCareContext("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#generateOTPForCareContext(String)}
     */
    @Test
    void testGenerateOTPForCareContext3() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.generateOTPForCareContext(""));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#generateOTPForCareContext(String)}
     */
    @Test
    void testGenerateOTPForCareContext4() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.generateOTPForCareContext("Error"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext("Request"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext3() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext("42"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext4() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext(""));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext5() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext("Error"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext6() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext("TransactionId"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#validateOTPForCareContext(String)}
     */
    @Test
    void testValidateOTPForCareContext7() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.validateOTPForCareContext("failure"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#addCareContext(String, String)}
     */
    @Test
    void testAddCareContext() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.addCareContext("Request", "ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#addCareContext(String, String)}
     */
    @Test
    void testAddCareContext2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.addCareContext("42", "ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#addCareContext(String, String)}
     */
    @Test
    void testAddCareContext3() throws FHIRException {
        // Arrange
        Credential credential = new Credential();
        credential.setAuthCode("Auth Code");

        CareContextPatient patient = new CareContextPatient();
        patient.setCarecontexts(new ArrayList<>());
        patient.setDisplay("Display");
        patient.setReferencenumber("42");

        AddCareContext link = new AddCareContext();
        link.setAccessToken("ABC123");
        link.setPatient(patient);

        Requester requester = new Requester();
        requester.setId("42");
        requester.setType("Type");

        Query query = new Query();
        query.setAuthMode("Auth Mode");
        query.setId("42");
        query.setPurpose("Purpose");
        query.setRequester(requester);

        NDHMRequest ndhmRequest = new NDHMRequest();
        ndhmRequest.setCredential(credential);
        ndhmRequest.setLink(link);
        ndhmRequest.setQuery(query);
        ndhmRequest.setRequestId("42");
        ndhmRequest.setTimestamp("Timestamp");
        ndhmRequest.setTransactionId("42");
        when(common_NDHMService.getRequestIDAndTimeStamp()).thenReturn(ndhmRequest);
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.addCareContext("", "ABC123"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    /**
     * Method under test:
     * {@link LinkCareContext_NDHMServiceImpl#addCareContext(String, String)}
     */
    @Test
    void testAddCareContext4() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> linkCareContext_NDHMServiceImpl.addCareContext("Failure", "ABC123"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }
}
