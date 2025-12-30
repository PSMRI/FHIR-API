package com.wipro.fhir.service.bundle_creation;

import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

public interface DischargeSummaryResourceBundle {

	int processDischargeSummaryRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

	Composition populateDischargeSummaryComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, Practitioner practitioner, Organization organization, Patient patient,
			Encounter encounter, List<Condition> chiefComplaints, List<Condition> physicalExam,
			List<AllergyIntolerance> allergyList, FamilyMemberHistory familyMemberHistory,
			List<MedicationStatement> pastMedicalHistoryConditions, List<MedicationRequest> medicationRequests,
			List<DiagnosticReport> procedures);

	String populateDischargeSummaryResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException;

}
