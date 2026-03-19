package com.wipro.fhir.service.bundle_creation;

import java.util.List;

import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

public interface ImmunizationRecordResourceBundle {

	Composition populateImmunizationComposition(Patient patient, Practitioner practitioner, Organization organization,
			List<Immunization> immunizations);

	String populateImmunizationResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

	int processImmunizationRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

}
