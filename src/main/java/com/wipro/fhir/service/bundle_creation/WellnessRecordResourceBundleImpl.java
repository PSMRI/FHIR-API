package com.wipro.fhir.service.bundle_creation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
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
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

@Service
public class WellnessRecordResourceBundleImpl implements WellnessRecordResourceBundle {

	@Autowired
	private CommonService commonService;

	@Autowired
	private PractitionerResource practitionerResource;

	@Autowired
	private PatientResource patientResource;

	@Autowired
	private OrganizationResource organizationResource;

	@Autowired
	private ObservationResource observationResource;

	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Value("${hipSystemUrl}")
	private String systemUrl;

	@Override
	public int processWellnessRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException, Exception {
		int i = 0;
		// call method to generate Prescription resource
		String wellnessBundle = PopulateWellnessRecordResourceBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createPrescriptionBundleMongo(p, wellnessBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("Issue in processing the bundle");
		return i;
	}

	@Override
	public String PopulateWellnessRecordResourceBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		Bundle wellnessBundle = new Bundle();
		String serializeBundle = null;

		try {

			String id = resourceRequestHandler.getVisitCode() + ":" + commonService.getUUID();
			wellnessBundle.setId(id);
			wellnessBundle.setType(Bundle.BundleType.DOCUMENT);
			wellnessBundle.setTimestamp(new Timestamp(System.currentTimeMillis()));

			Meta meta = new Meta();
			meta.setVersionId("1");
			meta.setLastUpdated(new Timestamp(System.currentTimeMillis()));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
			meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "restricted", "R"));
			wellnessBundle.setMeta(meta);

			Identifier identifier = new Identifier();
			identifier.setSystem(systemUrl);
			identifier.setValue(wellnessBundle.getId());
			wellnessBundle.setIdentifier(identifier);

			Practitioner practitioner = practitionerResource.getPractitionerResource(resourceRequestHandler);
			Organization organization = organizationResource.getOrganizationResource(resourceRequestHandler);
			Patient patient = patientResource.getPatientResource(resourceRequestHandler);

			List<Observation> observationVitalList = observationResource.getObservationVitals(patient,
					resourceRequestHandler);

			// Composition
			Composition composition = PopulateWellnessRecordComposition(resourceRequestHandler, p, practitioner,
					organization, observationVitalList);

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

			for (Observation obsVital : observationVitalList) {
				BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
				bundleEntry5.setFullUrl(obsVital.getIdElement().getValue());
				bundleEntry5.setResource(obsVital);

				bundleEnteries.add(bundleEntry5);
			}

			wellnessBundle.setEntry(bundleEnteries);

			FhirContext ctx = FhirContext.forR4();
			IParser parser = ctx.newJsonParser();
			serializeBundle = parser.encodeResourceToString(wellnessBundle);

		} catch (Exception e) {
			throw new FHIRException("Wellness FHIR Resource Bundle failed with error - " + e);
		}

		return serializeBundle;
	}

	@Override
	public Composition PopulateWellnessRecordComposition(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p, Practitioner practitioner, Organization organization,
			List<Observation> observationVitalList) {
		Composition comp = new Composition();
		comp.setId("Composition/" + commonService.getUUID());

		// Composition.meta – bind WellnessRecord profile
		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/WellnessRecord");
		comp.setMeta(meta);

		comp.setStatus(Composition.CompositionStatus.FINAL);

		CodeableConcept type = new CodeableConcept();
		type.setText("Wellness Record");
		comp.setType(type);

		comp.setSubject(new Reference("Patient/" + p.getBeneficiaryId().toString()));
		comp.setDate(new Date());

		comp.addAuthor(new Reference(practitioner.getIdElement().getValue()));
		comp.setCustodian(new Reference(organization.getIdElement().getValue()));
		comp.setTitle("Wellness Record");

		Composition.SectionComponent vitalSignsSection = new Composition.SectionComponent();
		vitalSignsSection.setTitle("Vital Signs");

		Composition.SectionComponent bodyMeasurementSection = new Composition.SectionComponent();
		bodyMeasurementSection.setTitle("Body Measurement");

		if (observationVitalList.size() > 0) {
			for (Observation obs : observationVitalList) {
				String label = (obs.getCode() != null) ? obs.getCode().getText() : null;

				// Create reference for bundle entry
				Reference ref = new Reference();
				ref.setReference(obs.getIdElement().getValue());
				ref.setType("Observation");

				if (isVitalSignLabel(label)) {
					vitalSignsSection.addEntry(ref);
				} else if (isBodyMeasurementLabel(label)) {
					bodyMeasurementSection.addEntry(ref);
				} else {
					vitalSignsSection.addEntry(ref);
				}
			}
		}

		// Add sections only if they have entries
		if (vitalSignsSection.hasEntry()) {
			comp.addSection(vitalSignsSection);
		}
		if (bodyMeasurementSection.hasEntry()) {
			comp.addSection(bodyMeasurementSection);
		}

		return comp;
	}

	private boolean isVitalSignLabel(String label) {
		if (label == null)
			return false;
		String s = label.trim().toLowerCase();
		return s.equals("body temperature") || s.equals("pulse rate") || s.equals("respiratory rate")
				|| s.equals("systolic blood pressure") || s.equals("diastolic blood pressure");
	}

	private boolean isBodyMeasurementLabel(String label) {
		if (label == null)
			return false;
		String s = label.trim().toLowerCase();
		return s.equals("body height") || s.equals("body weight") || s.equals("body mass index");
	}

	private AMRIT_ResourceMongo createPrescriptionBundleMongo(PatientEligibleForResourceCreation p,
			String wellnessBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(wellnessBundle);
		aMRIT_ResourceMongo.setResourceType("WellnessRecord");

		return aMRIT_ResourceMongo;
	}

}
