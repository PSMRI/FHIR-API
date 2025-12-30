package com.wipro.fhir.service.bundle_creation;

import java.util.List;

import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

public interface DiagnosticRecordResourceBundle {

	
	int processDiagnosticReportRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

	Composition populateDiagnosticReportComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, List<DiagnosticReport> diagnosticReports, Practitioner practitioner,
			Organization organization);

	String populateDiagnosticReportResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

}
