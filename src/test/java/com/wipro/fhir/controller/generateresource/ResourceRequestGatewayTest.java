package com.wipro.fhir.controller.generateresource;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.resource_gateway.DiagnosticReportRecord;
import com.wipro.fhir.service.resource_gateway.OPConsultRecordBundle;
import com.wipro.fhir.service.resource_gateway.PrescriptionRecordBundle;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

@ExtendWith(SpringExtension.class)
class ResourceRequestGatewayTest {

	@InjectMocks
	private ResourceRequestGateway resourceRequestGateway;

	@Mock
	private OPConsultRecordBundle opConsultRecordBundle;
	@Mock
	private PrescriptionRecordBundle prescriptionRecordBundle;
	@Mock
	private DiagnosticReportRecord diagnosticReportRecord;

	@Test
	void testGetPatientResource() throws Exception {

		ResourceRequestHandler patientResourceRequest = new ResourceRequestHandler();

		patientResourceRequest.setBeneficiaryID(12L);
		patientResourceRequest.setVisitCode(23L);
		patientResourceRequest.setBeneficiaryRegID(11L);
		patientResourceRequest.setBenFlowID(13L);
		patientResourceRequest.setHealthId("Ahhjfgjfj");
		patientResourceRequest.setHealthIdNumber("Hdsagfjgjdsjh");
		patientResourceRequest.setAmritId("Afhdg");
		patientResourceRequest.setExternalId("Ex3675v");
		patientResourceRequest.setPageNo(12);
		patientResourceRequest.setPhoneNo("9804120118");
		patientResourceRequest.setState("WB");
		patientResourceRequest.setDistrict("Kolkata");
		patientResourceRequest.setVillage("WB");

		patientResourceRequest.toString();

		String Authorization = "Authorization";

		OutputResponse response = new OutputResponse();

		String s = patientResourceRequest.toString();

		when(opConsultRecordBundle.getOPConsultRecordBundle(patientResourceRequest, null)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertEquals(response.toString(),
				resourceRequestGateway.getPatientResource(patientResourceRequest, Authorization));

	}

	@Test
	void testGetPatientResource_Exception() throws FHIRException {

		OutputResponse response = new OutputResponse();

		ResourceRequestHandler patientResourceRequest = new ResourceRequestHandler();

		String Authorization = null;

		when(opConsultRecordBundle.getOPConsultRecordBundle(patientResourceRequest, null))
				.thenThrow(FHIRException.class);

		FHIRException e = new FHIRException(Authorization);

		response.setError(5000, "error in creating OP Consult Record bundle : " + e.getMessage());

		Assertions.assertEquals(response.toString(),
				resourceRequestGateway.getPatientResource(patientResourceRequest, Authorization));
	}

	@Test
	void testGetDiagnosticReportRecord() throws Exception {

		ResourceRequestHandler patientResourceRequest = new ResourceRequestHandler();

		patientResourceRequest.setBeneficiaryID(12L);
		patientResourceRequest.setVisitCode(23L);
		patientResourceRequest.setBeneficiaryRegID(11L);
		patientResourceRequest.setBenFlowID(13L);
		patientResourceRequest.setHealthId("Ahhjfgjfj");
		patientResourceRequest.setHealthIdNumber("Hdsagfjgjdsjh");
		patientResourceRequest.setAmritId("Afhdg");
		patientResourceRequest.setExternalId("Ex3675v");
		patientResourceRequest.setPageNo(12);
		patientResourceRequest.setPhoneNo("9804120118");
		patientResourceRequest.setState("WB");
		patientResourceRequest.setDistrict("Kolkata");
		patientResourceRequest.setVillage("WB");

		patientResourceRequest.toString();

		String Authorization = "Authorization";

		OutputResponse response = new OutputResponse();

		String s = patientResourceRequest.toString();

		when(diagnosticReportRecord.getDiagnosticReportRecordBundle(patientResourceRequest, null)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertEquals(response.toString(),
				resourceRequestGateway.getDiagnosticReportRecord(patientResourceRequest, Authorization));

	}

	@Test
	void testGetDiagnosticReportRecord_DiagnosticReportRecordThrowsFHIRException() throws Exception {

		OutputResponse response = new OutputResponse();

		ResourceRequestHandler patientResourceRequest = new ResourceRequestHandler();

		String Authorization = null;

		when(diagnosticReportRecord.getDiagnosticReportRecordBundle(patientResourceRequest, null))
				.thenThrow(FHIRException.class);

		FHIRException e = new FHIRException(Authorization);

		response.setError(5000, "error in creating Diagnostic Report Record bundle : " + e.getMessage());

		Assertions.assertEquals(response.toString(),
				resourceRequestGateway.getDiagnosticReportRecord(patientResourceRequest, Authorization));

	}

	@Test
	void testGetPrescriptionRecord() throws Exception {

		ResourceRequestHandler patientResourceRequest = new ResourceRequestHandler();

		patientResourceRequest.setBeneficiaryID(12L);
		patientResourceRequest.setVisitCode(23L);
		patientResourceRequest.setBeneficiaryRegID(11L);
		patientResourceRequest.setBenFlowID(13L);
		patientResourceRequest.setHealthId("Ahhjfgjfj");
		patientResourceRequest.setHealthIdNumber("Hdsagfjgjdsjh");
		patientResourceRequest.setAmritId("Afhdg");
		patientResourceRequest.setExternalId("Ex3675v");
		patientResourceRequest.setPageNo(12);
		patientResourceRequest.setPhoneNo("9804120118");
		patientResourceRequest.setState("WB");
		patientResourceRequest.setDistrict("Kolkata");
		patientResourceRequest.setVillage("WB");

		patientResourceRequest.toString();

		String Authorization = "Authorization";

		OutputResponse response = new OutputResponse();

		String s = patientResourceRequest.toString();

		when(prescriptionRecordBundle.getPrescriptionRecordBundle(patientResourceRequest, null)).thenReturn(s);

		response.setResponse(s);

		Assertions.assertEquals(response.toString(),
				resourceRequestGateway.getPrescriptionRecord(patientResourceRequest, Authorization));

	}

	@Test
	void testGetPrescriptionRecord_PrescriptionRecordBundleThrowsFHIRException() throws Exception {
		
		OutputResponse response = new OutputResponse();

		ResourceRequestHandler patientResourceRequest = new ResourceRequestHandler();

		String Authorization = null;

		when(prescriptionRecordBundle.getPrescriptionRecordBundle(patientResourceRequest, null))
				.thenThrow(FHIRException.class);

		FHIRException e = new FHIRException(Authorization);

		response.setError(5000, "error in creating Prescription Record bundle : " + e.getMessage());

		Assertions.assertEquals(response.toString(),
				resourceRequestGateway.getPrescriptionRecord(patientResourceRequest, Authorization));
		
	}
}
