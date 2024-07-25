package com.wipro.fhir.service.ndhm;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.mongo.care_context.AddCareContext;
import com.wipro.fhir.data.mongo.care_context.CareContextPatient;
import com.wipro.fhir.data.mongo.care_context.Credential;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.Query;
import com.wipro.fhir.data.mongo.care_context.Requester;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@ExtendWith(SpringExtension.class)
class ValidateHealthID_NDHMServiceImplTest {
    @Mock
    private Common_NDHMService common_NDHMService;

    @Mock
    private GenerateSession_NDHMService generateSession_NDHMService;

    @Mock
    private HttpUtils httpUtils;

    @InjectMocks
    private ValidateHealthID_NDHMServiceImpl validateHealthID_NDHMServiceImpl;

    @Test
    void testGenerateOTPForHealthIDValidation() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> validateHealthID_NDHMServiceImpl.generateOTPForHealthIDValidation("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testGenerateOTPForHealthIDValidation2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> validateHealthID_NDHMServiceImpl.generateOTPForHealthIDValidation("42"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testGenerateOTPForHealthIDValidation3() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenReturn("ABC123");

        // Act and Assert
        assertThrows(FHIRException.class, () -> validateHealthID_NDHMServiceImpl.generateOTPForHealthIDValidation("Error"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testGenerateOTPForHealthIDValidation4() throws FHIRException {
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
        assertThrows(FHIRException.class, () -> validateHealthID_NDHMServiceImpl.generateOTPForHealthIDValidation(""));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation() throws FHIRException {
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
        assertThrows(FHIRException.class,
                () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation("Request"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation2() throws FHIRException {
        // Arrange
        when(generateSession_NDHMService.getNDHMAuthToken()).thenThrow(new FHIRException("An error occurred"));

        // Act and Assert
        assertThrows(FHIRException.class,
                () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation("Request"));
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation3() throws FHIRException {
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
        assertThrows(FHIRException.class, () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation("42"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation4() throws FHIRException {
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
        assertThrows(FHIRException.class, () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation("Error"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation5() throws FHIRException {
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
        assertThrows(FHIRException.class, () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation(""));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation6() throws FHIRException {
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
        assertThrows(FHIRException.class,
                () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation("TransactionId"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }

    @Test
    void testValidateOTPForHealthIDValidation7() throws FHIRException {
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
        assertThrows(FHIRException.class,
                () -> validateHealthID_NDHMServiceImpl.validateOTPForHealthIDValidation("failure"));
        verify(common_NDHMService).getRequestIDAndTimeStamp();
        verify(generateSession_NDHMService).getNDHMAuthToken();
    }
}
