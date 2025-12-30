package com.wipro.fhir.service.bundle_creation;

import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

public interface OPConsultResourceBundle {

	Composition populateOpConsultComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, Practitioner practitioner, Organization organization,
			List<Condition> conditionListChiefComplaints, List<Condition> conditionListDiagnosis,
			List<AllergyIntolerance> allergyList, FamilyMemberHistory familyMemberHistory,
			List<MedicationStatement> medicationStatement);

	int processOpConsultRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

	String populateOPConsultRecordResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

}
