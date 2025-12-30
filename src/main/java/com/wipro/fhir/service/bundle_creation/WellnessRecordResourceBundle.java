package com.wipro.fhir.service.bundle_creation;

import java.util.List;

import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

public interface WellnessRecordResourceBundle {

	String populateWellnessRecordResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

	Composition populateWellnessRecordComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, Practitioner practitioner, Organization organization,
			List<Observation> observationVitalList);

	int processWellnessRecordBundle(ResourceRequestHandler resourceRequestHandler, PatientEligibleForResourceCreation p)
			throws org.hl7.fhir.exceptions.FHIRException, Exception;


}
