package com.wipro.fhir.service.bundle_creation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
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
import com.wipro.fhir.service.resource_model.DiagnosticReportResource;
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class DiagnosticRecordResourceBundleImpl implements DiagnosticRecordResourceBundle {

	@Autowired
	private PractitionerResource practitionerResource;

	@Autowired
	private OrganizationResource organizationResource;

	@Autowired
	private PatientResource patientResource;

	@Autowired
	private ObservationResource observationResource;

	@Autowired
	private DiagnosticReportResource diagnosticReportResource;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Value("${hipSystemUrl}")
	private String systemUrl;
	
	@Override
	public int processDiagnosticReportRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String diagnosticReportRecordBundle = populateDiagnosticReportResourceBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createDiagnosticReportRecordBundleMongo(p,
				diagnosticReportRecordBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("TODO - exception - later will implement");

		return i;

	}

	@Override
	public String populateDiagnosticReportResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle diagReportBundle = new Bundle();
		String serializeBundle;

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

			// Observation- Physical Examination - vitals
			Map<Integer, List<Observation>> observationMap = observationResource.getObservationLab(patient,
					resourceRequestHandler);

			// diagnostic report
			List<DiagnosticReport> diagnosticResourceList = diagnosticReportResource.getDiagnosticReport(patient,
					new Encounter(), resourceRequestHandler, observationMap);

			Composition composition = populateDiagnosticReportComposition(resourceRequestHandler, p,
					diagnosticResourceList, practitioner, organization);

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

			
			for (DiagnosticReport dr : diagnosticResourceList) {
				BundleEntryComponent entryDR = new BundleEntryComponent();
				entryDR.setFullUrl(dr.getIdElement().getValue());
				entryDR.setResource(dr);
				
				bundleEntries.add(entryDR);
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
						bundleEntries.add(entryObs);
					}
				}
			}

			diagReportBundle.setEntry(bundleEntries);

			FhirContext ctx = FhirContext.forR4();
			IParser parser = ctx.newJsonParser();
			serializeBundle = parser.encodeResourceToString(diagReportBundle);			

		} catch (Exception e) {
			throw new FHIRException("Diagnostic Report FHIR Resource Bundle failed with error - " + e);
		}

		return serializeBundle;
	}

	@Override
	public Composition populateDiagnosticReportComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, List<DiagnosticReport> diagnosticReports, Practitioner practitioner, Organization organization) {

		Composition composition = new Composition();
		composition.setId("Composition/" + commonService.getUUID());

 		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportRecord");
		composition.setMeta(meta);

		composition.setStatus(Composition.CompositionStatus.FINAL);

		composition.setType(new CodeableConcept()
				.addCoding(new Coding("http://snomed.info/sct", "721981007", "Diagnostic studies report")));

		composition.setSubject(new Reference("Patient/" + p.getBeneficiaryId().toString()));
		composition.setDate(new Date());
		composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));
		composition.setCustodian(new Reference(organization.getIdElement().getValue()));
		composition.setTitle("Diagnostic Report Record");

		Composition.SectionComponent section = new Composition.SectionComponent();

		section.setCode(new CodeableConcept()
				.addCoding(new Coding("http://snomed.info/sct", "721981007", "Diagnostic studies report")));

		for (DiagnosticReport dr : diagnosticReports) {
			Reference drRef = new Reference(dr.getIdElement().getValue());
			drRef.setType("DiagnosticReport");
			section.addEntry(drRef);
		}

		composition.addSection(section);
		return composition;
	}
	
	private AMRIT_ResourceMongo createDiagnosticReportRecordBundleMongo(PatientEligibleForResourceCreation p,
			String diagnosticReportRecordBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(diagnosticReportRecordBundle);
		aMRIT_ResourceMongo.setResourceType("DiagnosticReport");

		return aMRIT_ResourceMongo;
	}

}
