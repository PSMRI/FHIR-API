package com.wipro.fhir.service.bundle_creation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Immunization;
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
import com.wipro.fhir.service.resource_model.ImmunizationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class ImmunizationRecordResourceBundleImpl implements ImmunizationRecordResourceBundle {
	
	@Autowired
	private PractitionerResource practitionerResource;

	@Autowired
	private OrganizationResource organizationResource;

	@Autowired
	private PatientResource patientResource;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImmunizationResource immunizationResource;
	
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Value("${hipSystemUrl}")
	private String systemUrl;
	
	@Override
	public int processImmunizationRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String immunizationBundle = PopulateImmunizationResourceBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createImmunizationBundleMongo(p,
				immunizationBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("Issue in processing the bundle");

		return i;

	}
	
	@Override
	public String PopulateImmunizationResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle diagReportBundle = new Bundle();
		String serializeBundle = null;

		try {
			String id = resourceRequestHandler.getVisitCode() + ":" + commonService.getUUID();
			diagReportBundle.setId(id);
			diagReportBundle.setType(Bundle.BundleType.DOCUMENT);
			diagReportBundle.setTimestamp(new Timestamp(System.currentTimeMillis()));

			Meta meta = new Meta();
			meta.setVersionId("1");
			meta.setLastUpdated(new Timestamp(System.currentTimeMillis()));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
			meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "R", "restricted"));
			diagReportBundle.setMeta(meta);

			Identifier identifier = new Identifier();
			identifier.setSystem(systemUrl);
			identifier.setValue(diagReportBundle.getId());
			diagReportBundle.setIdentifier(identifier);

			// practitioner resource 
			Practitioner practitioner = practitionerResource.getPractitionerResource(resourceRequestHandler);

			// Organization resource
			Organization organization = organizationResource.getOrganizationResource(resourceRequestHandler);

			// Patient resource 
			Patient patient = patientResource.getPatientResource(resourceRequestHandler);

			List<Immunization> immunizationList = immunizationResource.getImmunizations(patient, resourceRequestHandler);

			Composition composition = populateImmunizationComposition(patient, practitioner, organization, immunizationList);

			List<BundleEntryComponent> bundleEntries = new ArrayList<>();

			BundleEntryComponent entryComposition = new BundleEntryComponent();
			entryComposition.setFullUrl(composition.getIdElement().getValue());
			entryComposition.setResource(composition);
			bundleEntries.add(entryComposition);

			BundleEntryComponent entryPractitioner = new BundleEntryComponent();
			entryPractitioner.setFullUrl(practitioner.getIdElement().getValue());
			entryPractitioner.setResource(practitioner);
			bundleEntries.add(entryPractitioner);

			BundleEntryComponent entryOrganization = new BundleEntryComponent();
			entryOrganization.setFullUrl(organization.getIdElement().getValue());
			entryOrganization.setResource(organization);
			bundleEntries.add(entryOrganization);

			BundleEntryComponent entryPatient = new BundleEntryComponent();
			entryPatient.setFullUrl(patient.getIdElement().getValue());
			entryPatient.setResource(patient);
			bundleEntries.add(entryPatient);


	        for (Immunization imm : immunizationList) {
	            Bundle.BundleEntryComponent beImm = new Bundle.BundleEntryComponent();
	            beImm.setFullUrl(imm.getIdElement().getValue());
	            beImm.setResource(imm);
	            bundleEntries.add(beImm);
	        }

			diagReportBundle.setEntry(bundleEntries);

			FhirContext ctx = FhirContext.forR4();
			IParser parser = ctx.newJsonParser();
			serializeBundle = parser.encodeResourceToString(diagReportBundle);


		} catch (Exception e) {
			throw new FHIRException("Immunization FHIR Resource Bundle failed with error - " + e);
		}

		return serializeBundle;
	}

	@Override

public Composition populateImmunizationComposition(Patient patient,
                                                   Practitioner practitioner,
                                                   Organization organization,
                                                   List<Immunization> immunizations) {

    Composition composition = new Composition();
    composition.setId("Composition/" + commonService.getUUID());

    // NRCeS ImmunizationRecord profile on Composition
    Meta meta = new Meta();
    meta.setVersionId("1");
    meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ImmunizationRecord");
    composition.setMeta(meta);

    composition.setStatus(Composition.CompositionStatus.FINAL);

    composition.setType(new CodeableConcept()
            .addCoding(new Coding("http://snomed.info/sct", "41000179103", "Immunization record"))
            .setText("Immunization Record"));

    composition.setSubject(new Reference(patient.getIdElement().getValue()));
    composition.setDate(new java.util.Date());
	composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));
	composition.setCustodian(new Reference(organization.getIdElement().getValue()));
	composition.setTitle("Immunization Record");

    if (organization != null) {
        composition.setCustodian(new Reference(organization.getIdElement().getValue()));
    }
    
    // --- Sections ---
    SectionComponent immunizationSection = new SectionComponent();
    immunizationSection.setTitle("Administered Immunizations");
    immunizationSection.setCode(new CodeableConcept().addCoding(
            new Coding("http://snomed.info/sct", "41000179103", "Immunization record")
    ));

    for (Immunization imm : immunizations) {
        Reference ref = new Reference(imm.getIdElement().getValue());
        ref.setType("Immunization");
        immunizationSection.addEntry(ref);
    }

    composition.addSection(immunizationSection);

    return composition;
}
	
	private AMRIT_ResourceMongo createImmunizationBundleMongo(PatientEligibleForResourceCreation p,
			String immunizationBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(immunizationBundle);
		aMRIT_ResourceMongo.setResourceType("ImmunizationRecord");

		return aMRIT_ResourceMongo;
	}


}
