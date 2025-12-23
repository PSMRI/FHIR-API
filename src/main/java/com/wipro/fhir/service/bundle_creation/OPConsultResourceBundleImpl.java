package com.wipro.fhir.service.bundle_creation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Meta;
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
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.FamilyMemberHistoryResource;
import com.wipro.fhir.service.resource_model.MedicalHistoryResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class OPConsultResourceBundleImpl implements OPConsultResourceBundle {
	
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
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	
	@Value("${hipSystemUrl}")
	private String systemUrl;
	
	@Override
	public int processOpConsultRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String opConsultBundle = PopulateOPConsultRecordResourceBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createOpConsultBundleMongo(p,
				opConsultBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("Issue in processing the bundle");

		return i;

	}
	
	
	@Override
	public String PopulateOPConsultRecordResourceBundle(ResourceRequestHandler resourceRequestHandler, PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle opConsultBundle = new Bundle();
		String serializeBundle = null;
		
		try {
			String id = resourceRequestHandler.getVisitCode()+ ":" + commonService.getUUID();
			opConsultBundle.setId(id);
			opConsultBundle.setType(BundleType.DOCUMENT);
			opConsultBundle.setTimestamp(new Timestamp(System.currentTimeMillis()));
 
			Meta meta = new Meta();
			meta.setVersionId("1");
			meta.setLastUpdated(new Timestamp(System.currentTimeMillis()));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
			meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "restricted", "R"));
			opConsultBundle.setMeta(meta);

			Identifier identifier = new Identifier();
			identifier.setSystem(systemUrl);
			identifier.setValue(opConsultBundle.getId());
			opConsultBundle.setIdentifier(identifier);
			
			// practitioner
			Practitioner practitioner = practitionerResource.getPractitionerResource(resourceRequestHandler);
			// organization
			Organization organization = organizationResource.getOrganizationResource(resourceRequestHandler);
			// Patient Resource
			Patient patient = patientResource.getPatientResource(resourceRequestHandler);
			
			// chiefcomplaints
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
			
			// composition
			Composition composition = PopulateOpConsultComposition(resourceRequestHandler, p, practitioner, organization, conditionListChiefComplaints, 
					conditionListDiagnosis, allergyList,familyMemberHistory, medicationStatement);
			
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
			
			opConsultBundle.setEntry(bundleEnteries);
			
			FhirContext ctx = FhirContext.forR4();
			IParser parser = ctx.newJsonParser();
			serializeBundle = parser.encodeResourceToString(opConsultBundle);

			
		} catch (Exception e) {
			throw new FHIRException("Op Consult FHIR Resource Bundle failed with error - " + e);
		}

		return serializeBundle;

	}
	
	@Override
	public Composition PopulateOpConsultComposition(ResourceRequestHandler resourceRequestHandler,PatientEligibleForResourceCreation p, 
			Practitioner practitioner, Organization organization, List<Condition> conditionListChiefComplaints, List<Condition> conditionListDiagnosis, 
			List<AllergyIntolerance> allergyList, FamilyMemberHistory familyMemberHistory, List<MedicationStatement> medicationStatement) {

		Composition composition = new Composition();
		composition.setId("Composition/" + commonService.getUUID());

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/OPConsultRecord");
		composition.setMeta(meta);

		composition.setStatus(CompositionStatus.FINAL);
		composition  
				.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "371530004", "Clinical consultation report`")));

		composition.setSubject(new Reference("Patient/"+ p.getBeneficiaryId().toString()));
		composition.setDate(new Date());
		composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));
		composition.setCustodian(new Reference(organization.getIdElement().getValue()));
		composition.setTitle("Consultation Report");
		
		List<SectionComponent> sectionList = new ArrayList<SectionComponent>();
		
		for(Condition condition: conditionListChiefComplaints) {
		SectionComponent section1 = new SectionComponent();
		section1.setTitle("Chief complaints");      
		section1.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "422843007", "Chief complaint section")))
				.addEntry(new Reference().setReference(condition.getIdElement().getValue()));
		
		sectionList.add(section1);
		}
		
		for(Condition diagnosis: conditionListDiagnosis) {
		SectionComponent section2 = new SectionComponent();
		section2.setTitle("Physical diagnosis");
		section2.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "425044008", "Physical exam section")))
				.addEntry(new Reference().setReference(diagnosis.getIdElement().getValue()));
		
		sectionList.add(section2);
		}

		for(AllergyIntolerance allergy: allergyList) {
		SectionComponent section3 = new SectionComponent();
		section3.setTitle("Allergies");  
		section3.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "722446000", "Allergy record")))
				.addEntry(new Reference().setReference(allergy.getIdElement().getValue()));
		
		sectionList.add(section3);
		}

		for(MedicationStatement medStatement: medicationStatement) {
		SectionComponent section4 = new SectionComponent();
		section4.setTitle("Medical History");
		section4.setCode(
				new CodeableConcept(new Coding("http://snomed.info/sct", "371529009", "History and physical report")))
				.addEntry(new Reference().setReference(medStatement.getIdElement().getValue()));
		
		sectionList.add(section4);
		}
		
		
		if(familyMemberHistory.getId() != null) {
			SectionComponent section5 = new SectionComponent();
			section5.setTitle("Family history");
			section5.setCode(
					new CodeableConcept(new Coding("http://snomed.info/sct", "422432008", "Family history section")))
					.addEntry(new Reference().setReference(familyMemberHistory.getIdElement().getValue()));
			
			sectionList.add(section5);
		}

		composition.setSection(sectionList);

		return composition;

	}
	
	private AMRIT_ResourceMongo createOpConsultBundleMongo(PatientEligibleForResourceCreation p,
			String opConsultBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(opConsultBundle);
		aMRIT_ResourceMongo.setResourceType("OPConsultation");

		return aMRIT_ResourceMongo;
	}

	
}
