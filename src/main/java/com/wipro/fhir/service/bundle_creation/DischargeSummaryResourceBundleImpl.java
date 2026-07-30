package com.wipro.fhir.service.bundle_creation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.resource_model.AllergyIntoleranceResource;
import com.wipro.fhir.service.resource_model.ConditionResource;
import com.wipro.fhir.service.resource_model.DiagnosticReportResource;
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.FamilyMemberHistoryResource;
import com.wipro.fhir.service.resource_model.MedicalHistoryResource;
import com.wipro.fhir.service.resource_model.MedicationRequestResource;
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class DischargeSummaryResourceBundleImpl implements DischargeSummaryResourceBundle {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private PractitionerResource practitionerResource;
	
	@Autowired
	private OrganizationResource organizationResource;
	
	@Autowired
	private PatientResource patientResource;
	
	@Autowired
	private ConditionResource conditionResource;
	
	@Autowired
	private EncounterResource encounterResource;
	
	@Autowired
	private AllergyIntoleranceResource allergyIntoleranceResource;
	
	@Autowired
	private FamilyMemberHistoryResource familyMemberHistoryResource;
	
	@Autowired
	private MedicalHistoryResource medicalHistoryResource;
	
	@Autowired
	private ObservationResource observationResource;

	@Autowired
	private DiagnosticReportResource diagnosticReportResource;
	
	@Autowired
	private MedicationRequestResource medicationRequestResource;
	
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	
	@Value("${hipSystemUrl}")
	private String systemUrl;
	
	@Override
	public int processDischargeSummaryRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String dischargeSummaryBundle = populateDischargeSummaryResourceBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createDischargeSummaryBundleMongo(p,
				dischargeSummaryBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("Issue in processing the bundle");

		return i;

	}
	
	
	@Override
	public String populateDischargeSummaryResourceBundle(ResourceRequestHandler resourceRequestHandler, PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle dischargeSummaryBundle = new Bundle();
		String serializeBundle = null;
		
		
		try {
			String id = resourceRequestHandler.getVisitCode()+ ":" + commonService.getUUID();
			dischargeSummaryBundle.setId(id);
			dischargeSummaryBundle.setType(BundleType.DOCUMENT);
			dischargeSummaryBundle.setTimestamp(new Timestamp(System.currentTimeMillis()));
 
			Meta meta = new Meta();
			meta.setVersionId("1");
			meta.setLastUpdated(new Timestamp(System.currentTimeMillis()));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
			meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "restricted", "R"));
			dischargeSummaryBundle.setMeta(meta);

			Identifier identifier = new Identifier();
			identifier.setSystem(systemUrl);
			identifier.setValue(dischargeSummaryBundle.getId());
			dischargeSummaryBundle.setIdentifier(identifier);
			
			// practitioner
			Practitioner practitioner = practitionerResource.getPractitionerResource(resourceRequestHandler);
			// organization
			Organization organization = organizationResource.getOrganizationResource(resourceRequestHandler);
			// Patient Resource
			Patient patient = patientResource.getPatientResource(resourceRequestHandler);
			
			//Chief complaints
			List<Condition> conditionListChiefComplaints = conditionResource.getCondition(patient, resourceRequestHandler,
					"chiefcomplaints");

			// diagnosis
			List<Condition> conditionListDiagnosis = conditionResource.getCondition(patient, resourceRequestHandler,
					"diagnosis");
			
			Encounter encounter = encounterResource.getEncounterResource(patient, resourceRequestHandler,
					conditionListChiefComplaints, conditionListDiagnosis);
			
			// AllergyIntolerance resource
			List<AllergyIntolerance> allergyList = allergyIntoleranceResource.getAllergyIntolerance(patient, encounter,
					resourceRequestHandler, practitioner);
			
			// FamilyMemberHistory resource
			FamilyMemberHistory familyMemberHistory = familyMemberHistoryResource.getFamilyMemberHistory(patient, 
					resourceRequestHandler);
			
			List<MedicationStatement> medicationStatement = medicalHistoryResource.getMedicalHistory(patient, resourceRequestHandler);
			
			// Medication request
			List<MedicationRequest> medicationRequest = medicationRequestResource.getMedicationRequest(patient,
								resourceRequestHandler, practitioner, null);
			
			// Observation- Physical Examination 
			Map<Integer, List<Observation>> observationMap = observationResource.getObservationLab(patient,
								resourceRequestHandler);
			
			List<DiagnosticReport> diagnosticResourceList = diagnosticReportResource.getDiagnosticReport(patient,
					new Encounter(), resourceRequestHandler, observationMap);
			
			// composition
			Composition composition = populateDischargeSummaryComposition(resourceRequestHandler, p, practitioner, organization, patient, encounter, conditionListChiefComplaints, 
					conditionListDiagnosis, allergyList, familyMemberHistory, medicationStatement, medicationRequest, diagnosticResourceList);
			
			List<BundleEntryComponent> bundleEnteries = new ArrayList<>();
			
			BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
			bundleEntry1.setFullUrl(composition.getIdElement().getValue());
			bundleEntry1.setResource(composition);
			
			BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
			bundleEntry2.setFullUrl(practitioner.getIdElement().getValue());
			bundleEntry2.setResource(practitioner);
			
			BundleEntryComponent bundleEntry3 = new BundleEntryComponent();
			bundleEntry3.setFullUrl(organization.getIdElement().getValue());
			bundleEntry3.setResource(organization);
			
			BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
			bundleEntry4.setFullUrl(patient.getIdElement().getValue());
			bundleEntry4.setResource(patient);
			
			bundleEnteries.add(bundleEntry1);
			bundleEnteries.add(bundleEntry2);
			bundleEnteries.add(bundleEntry3);
			bundleEnteries.add(bundleEntry4);
			
			for (Condition conditionCheifComplaints : conditionListChiefComplaints) {
				BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
				bundleEntry5.setFullUrl(conditionCheifComplaints.getIdElement().getValue());
				bundleEntry5.setResource(conditionCheifComplaints);

				bundleEnteries.add(bundleEntry5);
			}
			
			for (Condition conditionDiagnosis : conditionListDiagnosis) {
				BundleEntryComponent bundleEntry6 = new BundleEntryComponent();
				bundleEntry6.setFullUrl(conditionDiagnosis.getIdElement().getValue());
				bundleEntry6.setResource(conditionDiagnosis);

				bundleEnteries.add(bundleEntry6);
			}
			
			for (AllergyIntolerance allergy : allergyList) {
				BundleEntryComponent bundleEntry7 = new BundleEntryComponent();
				bundleEntry7.setFullUrl(allergy.getIdElement().getValue());
				bundleEntry7.setResource(allergy);

				bundleEnteries.add(bundleEntry7);
			}
			
			if(familyMemberHistory.getId() != null) {
				BundleEntryComponent bundleEntry8 = new BundleEntryComponent();
				bundleEntry8.setFullUrl(familyMemberHistory.getIdElement().getValue());
				bundleEntry8.setResource(familyMemberHistory);
				bundleEnteries.add(bundleEntry8);
			}
				
			for(MedicationStatement medStatement: medicationStatement) {
				BundleEntryComponent bundleEntry9 = new BundleEntryComponent();
				bundleEntry9.setFullUrl(medStatement.getIdElement().getValue());
				bundleEntry9.setResource(medStatement);
				
				bundleEnteries.add(bundleEntry9);
			}
			
			for (DiagnosticReport dr : diagnosticResourceList) {
				BundleEntryComponent entryDR = new BundleEntryComponent();
				entryDR.setFullUrl(dr.getIdElement().getValue());
				entryDR.setResource(dr);
				
				bundleEnteries.add(entryDR);
			}
			

			if (observationMap != null && !observationMap.isEmpty()) {
				for (Map.Entry<Integer, List<Observation>> e : observationMap.entrySet()) {
					List<Observation> obsList = e.getValue();
					if (obsList == null) 
						continue;

					for (Observation obs : obsList) {
						BundleEntryComponent entryObs = new BundleEntryComponent();
						entryObs.setFullUrl(obs.getIdElement().getValue());
						entryObs.setResource(obs);
						bundleEnteries.add(entryObs);
					}
				}
			}
			
			dischargeSummaryBundle.setEntry(bundleEnteries);
			
			FhirContext ctx = FhirContext.forR4();
			IParser parser = ctx.newJsonParser();
			serializeBundle = parser.encodeResourceToString(dischargeSummaryBundle);
			
		} catch (Exception e) {
			throw new FHIRException("Discharge summary FHIR Resource Bundle failed with error - " + e);
		}

		return serializeBundle;

	}
	
	@Override

	public Composition populateDischargeSummaryComposition(
			ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p,
			Practitioner practitioner,
			Organization organization,         
			Patient patient,                    
			Encounter encounter,
			List<Condition> chiefComplaints,
			List<Condition> physicalExam,
			List<AllergyIntolerance> allergyList,
			FamilyMemberHistory familyMemberHistory,
			List<MedicationStatement> pastMedicalHistoryConditions,
			List<MedicationRequest> medicationRequests,
			List<DiagnosticReport> procedures
			) {
		Composition composition = new Composition();
		composition.setId("Composition/" + commonService.getUUID());

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DischargeSummaryRecord");
		composition.setMeta(meta);

		composition.setStatus(CompositionStatus.FINAL);

		composition.setType(new CodeableConcept()
				.addCoding(new Coding("http://snomed.info/sct", "373942005", "Discharge summary")));

		composition.setSubject(new Reference("Patient/"+ p.getBeneficiaryId().toString()));

		composition.setDate(new Date());
		composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));
		composition.setCustodian(new Reference(organization.getIdElement().getValue()));
		
		composition.setTitle("Discharge Summary");

		List<SectionComponent> sectionList = new ArrayList<>();

		// 1) Chief complaints (Condition) – SNOMED 422843007
		if (chiefComplaints != null) {
			for (Condition condition : chiefComplaints) {
				SectionComponent s = new SectionComponent();
				s.setTitle("Chief complaints");
				s.setCode(new CodeableConcept().addCoding(
						new Coding("http://snomed.info/sct", "422843007", "Chief complaint section")));
				s.addEntry(new Reference(condition.getIdElement().getValue()));
				sectionList.add(s);
			}
		}

		// 2) Physical examination (Observation) – SNOMED 425044008
		if (physicalExam != null) {
			for (Condition obs : physicalExam) {
				SectionComponent s = new SectionComponent();
				s.setTitle("Physical examination");
				s.setCode(new CodeableConcept().addCoding(
						new Coding("http://snomed.info/sct", "425044008", "Physical exam section")));
				s.addEntry(new Reference(obs.getIdElement().getValue()));
				sectionList.add(s);
			}
		}

		// 3) Allergies (AllergyIntolerance) – SNOMED 722446000
		if (allergyList != null) {
			for (AllergyIntolerance allergy : allergyList) {
				SectionComponent s = new SectionComponent();
				s.setTitle("Allergies");
				s.setCode(new CodeableConcept().addCoding(
						new Coding("http://snomed.info/sct", "722446000", "Allergy record")));
				s.addEntry(new Reference(allergy.getIdElement().getValue()));
				sectionList.add(s);
			}
		}

		// 4) Past medical history (Condition|Procedure) – SNOMED 1003642006
		boolean hasPMH = (pastMedicalHistoryConditions != null && !pastMedicalHistoryConditions.isEmpty());
		if (hasPMH) {
			SectionComponent s = new SectionComponent();
			s.setTitle("Past Medical History");
			s.setCode(new CodeableConcept().addCoding(
					new Coding("http://snomed.info/sct", "1003642006", "Past medical history section")));
			if (pastMedicalHistoryConditions != null) {
				for (MedicationStatement c : pastMedicalHistoryConditions) {
					s.addEntry(new Reference(c.getIdElement().getValue()));
				}
			}
			sectionList.add(s);
		}

		// 5) Family history (FamilyMemberHistory) – SNOMED 422432008
		if (familyMemberHistory != null && familyMemberHistory.getId() != null) {
			SectionComponent s = new SectionComponent();
			s.setTitle("Family history");
			s.setCode(new CodeableConcept().addCoding(
					new Coding("http://snomed.info/sct", "422432008", "Family history section")));
			s.addEntry(new Reference(familyMemberHistory.getIdElement().getValue()));
			sectionList.add(s);
		}

		// 6) Investigations (Diagnostic studies report) – SNOMED 721981007 (Lab + Imaging)
		boolean hasInvestigations = (procedures != null && !procedures.isEmpty());
		if (hasInvestigations) {
			SectionComponent s = new SectionComponent();
			s.setTitle("Investigations");
			s.setCode(new CodeableConcept().addCoding(
					new Coding("http://snomed.info/sct", "721981007", "Diagnostic studies report")));
			if (procedures != null) {
				for (DiagnosticReport dr : procedures) {
					s.addEntry(new Reference(dr.getIdElement().getValue()));
				}
			}
			sectionList.add(s);
		}

		// 7) Medications (MedicationRequest) – SNOMED 1003606003
		if (medicationRequests != null) {
			for (MedicationRequest mr : medicationRequests) {
				SectionComponent s = new SectionComponent();
				s.setTitle("Medications");
				s.setCode(new CodeableConcept().addCoding(
						new Coding("http://snomed.info/sct", "1003606003", "Medication history section")));
				s.addEntry(new Reference(mr.getIdElement().getValue()));
				sectionList.add(s);
			}
		}

  
		composition.setSection(sectionList);
		return composition;
	}
	
	private AMRIT_ResourceMongo createDischargeSummaryBundleMongo(PatientEligibleForResourceCreation p,
			String dischargeRecordBundle) {
		AMRIT_ResourceMongo aMRIT_ResourceMongo = new AMRIT_ResourceMongo();
		aMRIT_ResourceMongo.setBeneficiaryID(p.getBeneficiaryId());
		aMRIT_ResourceMongo.setBeneficiaryRegID(p.getBeneficiaryRegID());
		// get ABHA from table "m_benhealthmapping" for this visit(visit code)
		if (p.getVisitCode() != null) {
			aMRIT_ResourceMongo.setVisitCode(p.getVisitCode());
			List<String> objArrResultSet = benHealthIDMappingRepo.getLinkedHealthIDForVisit(p.getVisitCode());
			if (objArrResultSet != null && objArrResultSet.size() > 0) {
				aMRIT_ResourceMongo.setNationalHealthID(objArrResultSet.get(0));
			}
		}

		aMRIT_ResourceMongo.setResourceJson(dischargeRecordBundle);
		aMRIT_ResourceMongo.setResourceType("DischargeSummary");

		return aMRIT_ResourceMongo;
	}

}
