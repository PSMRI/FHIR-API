package com.wipro.fhir.service.bundle_creation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.resource_model.MedicationRequestResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class PrescriptionResourceBundleImpl implements PrescriptionResourceBundle {

	@Autowired
	private CommonService commonService;
	@Autowired
	private PractitionerResource practitionerResource;
	@Autowired
	private PatientResource patientResource;
	@Autowired
	private MedicationRequestResource medicationRequestResource;
	@Autowired
	private OrganizationResource organizationResource;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	
	@Value("${hipSystemUrl}")
	private String systemUrl;
	
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	
	@Override
	public int processPrescriptionRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String prescriptionBundle = PopulatePrescriptionResourceBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createPrescriptionBundleMongo(p,
				prescriptionBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("Issue in processing the bundle");

		return i;

	}
	
	@Override
	public String PopulatePrescriptionResourceBundle(ResourceRequestHandler resourceRequestHandler, PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle prescriptionBundle = new Bundle();
		String serializeBundle = null;
		
		try {
			String id = resourceRequestHandler.getVisitCode()+ ":" + commonService.getUUID();
			prescriptionBundle.setId(id);
			prescriptionBundle.setType(BundleType.DOCUMENT);
			prescriptionBundle.setTimestamp(new Timestamp(System.currentTimeMillis()));
 
			Meta meta = new Meta();
			meta.setVersionId("1");
			meta.setLastUpdated(new Timestamp(System.currentTimeMillis()));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
			meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "restricted", "R"));
			prescriptionBundle.setMeta(meta);

			Identifier identifier = new Identifier();
			identifier.setSystem(systemUrl);
			identifier.setValue(prescriptionBundle.getId());
			prescriptionBundle.setIdentifier(identifier);
			
			// practitioner
			Practitioner practitioner = practitionerResource.getPractitionerResource(resourceRequestHandler);
			// organization
			Organization organization = organizationResource.getOrganizationResource(resourceRequestHandler);
			// 1. Patient Resource
			Patient patient = patientResource.getPatientResource(resourceRequestHandler);
			// Medication request
			List<MedicationRequest> medicationRequest = medicationRequestResource.getMedicationRequest(patient,
					resourceRequestHandler, practitioner, null);
			// composition
			Composition composition = PopulatePrescriptionComposition(resourceRequestHandler, p, medicationRequest, practitioner, organization);
			
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
			
			for (MedicationRequest medRequest : medicationRequest) {
				BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
				bundleEntry5.setFullUrl(medRequest.getIdElement().getValue());
				bundleEntry5.setResource(medRequest);

				bundleEnteries.add(bundleEntry5);
			}
			
			prescriptionBundle.setEntry(bundleEnteries);
			
			FhirContext ctx = FhirContext.forR4();
			IParser parser = ctx.newJsonParser();
			serializeBundle = parser.encodeResourceToString(prescriptionBundle);

			
		} catch (Exception e) {
			throw new FHIRException("Prescription FHIR Resource Bundle failed with error - " + e);
		}

		return serializeBundle;

	}
 
	@Override
	public Composition PopulatePrescriptionComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, List<MedicationRequest> medicationRequest, Practitioner practitioner, Organization organization) {

		Composition composition = new Composition();
		composition.setId("Composition/" + commonService.getUUID());

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord");
		composition.setMeta(meta);

		composition.setStatus(CompositionStatus.FINAL);
		composition
				.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "440545006", "Prescription record")));

		composition.setSubject(new Reference("Patient/"+ p.getBeneficiaryId().toString()));
		composition.setDate(new Date());
		composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));
		composition.setCustodian(new Reference(organization.getIdElement().getValue()));
		composition.setTitle("Prescription Record");

		SectionComponent section = new SectionComponent();
		section.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "440545006", "Prescription record")));

		for (MedicationRequest med : medicationRequest) {
			Reference reference = new Reference();
			reference.setReference(med.getIdElement().getValue());
			reference.setType("MedicationRequest");

			section.addEntry(reference);

		}

		composition.addSection(section);

		return composition;

	}
	
	private AMRIT_ResourceMongo createPrescriptionBundleMongo(PatientEligibleForResourceCreation p,
			String prescriptionBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(prescriptionBundle);
		aMRIT_ResourceMongo.setResourceType("Prescription");

		return aMRIT_ResourceMongo;
	}

}
