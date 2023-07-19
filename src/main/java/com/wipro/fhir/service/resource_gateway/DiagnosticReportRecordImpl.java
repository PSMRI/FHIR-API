/*
* AMRIT – Accessible Medical Records via Integrated Technology 
* Integrated EHR (Electronic Health Records) Solution 
*
* Copyright (C) "Piramal Swasthya Management and Research Institute" 
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.service.resource_gateway;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
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
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.resource_model.AppointmentResource;
import com.wipro.fhir.service.resource_model.ConditionResource;
import com.wipro.fhir.service.resource_model.DiagnosticReportResource;
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;

@Service
public class DiagnosticReportRecordImpl implements DiagnosticReportRecord {

	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private PatientResource patientResource;
	@Autowired
	private EncounterResource encounterResource;
	@Autowired
	private AppointmentResource appointmentResource;
	@Autowired
	private ConditionResource conditionResource;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	@Autowired
	private DiagnosticReportResource diagnosticReportResource;
	@Autowired
	private ObservationResource observationResource;

	@Override
	public int processDiagnosticReportRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String diagnosticReportRecordBundle = getDiagnosticReportRecordBundle(resourceRequestHandler, p);

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

	@Autowired
	PractitionerResource practitionerResource;
	@Autowired
	OrganizationResource organizationResource;

	@Override
	public String getDiagnosticReportRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.setId(commonService.getUUID());
		// bundle identifier
		Identifier identifier = new Identifier();
		identifier.setSystem("https://www.max.in/bundle");
		identifier.setValue(bundle.getId());

		bundle.setIdentifier(identifier);
		bundle.setTimestamp(new Timestamp(System.currentTimeMillis()));
		bundle.setMeta(new Meta().setLastUpdated(new Date(System.currentTimeMillis())));
		FhirContext fhirContext = FhirContext.forR4();

		// practitioner resource
		Practitioner practitioner = practitionerResource.getPractitioner();

		// Organization resource
		Organization organization = organizationResource.getOrganization();

		// Patient resource
		Patient patient = patientResource.getPatientResource(resourceRequestHandler);

		// Observation- Physical Examination - vitals
		Map<Integer, List<Observation>> observationMap = observationResource.getObservationLab(patient,
				resourceRequestHandler);

		// diagnostic report
		List<DiagnosticReport> diagnosticResourceList = diagnosticReportResource.getDiagnosticReport(patient,
				new Encounter(), resourceRequestHandler, observationMap);

		// composition
		Composition composition = getCompositionResourceDiagnosticReport(patient, practitioner, diagnosticResourceList);

		/***
		 * bundle addition
		 */


		bundle.addEntry().setFullUrl(composition.getIdElement().getValue()).setResource(composition).getRequest()
				.setUrl("composition").setMethod(Bundle.HTTPVerb.POST);

		bundle.addEntry().setFullUrl(practitioner.getIdElement().getValue()).setResource(practitioner).getRequest()
				.setUrl("practitioner").setMethod(Bundle.HTTPVerb.POST);

		bundle.addEntry().setFullUrl(organization.getIdElement().getValue()).setResource(organization).getRequest()
				.setUrl("organization").setMethod(Bundle.HTTPVerb.POST);

		bundle.addEntry().setFullUrl(patient.getIdElement().getValue()).setResource(patient).getRequest()
				.setUrl("Patient").setMethod(Bundle.HTTPVerb.POST);

		for (DiagnosticReport dr : diagnosticResourceList) {
			bundle.addEntry().setFullUrl(dr.getIdElement().getValue()).setResource(dr).getRequest()
					.setUrl("DiagnosticReport").setMethod(Bundle.HTTPVerb.POST);
		}

		List<Observation> tempList = new ArrayList<>();
		if (observationMap != null && observationMap.size() > 0) {
			for (Entry<Integer, List<Observation>> entry : observationMap.entrySet()) {
				for (Observation observation : entry.getValue()) {
					tempList.add(observation);

					bundle.addEntry().setFullUrl(observation.getIdElement().getValue()).setResource(observation)
							.getRequest().setUrl("DiagnosticReport").setMethod(Bundle.HTTPVerb.POST);
				}
			}

		}

		return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
	}

	private Composition getCompositionResourceDiagnosticReport(Patient patient, Practitioner practitioner,
			List<DiagnosticReport> diagnosticReportList) {

		Composition composition = new Composition();
		composition.setId("Composition/" + commonService.getUUID());
		composition.setStatus(CompositionStatus.FINAL);
		composition.setSubject(new Reference(patient.getId()));
		composition.setDate(new Date(System.currentTimeMillis()));
		composition.setTitle("Diagnostic Report- Lab");
		// bundle identifier
		Identifier identifier = new Identifier();
		identifier.setSystem("https://ndhm.in/phr");
		identifier.setValue(composition.getId());
		composition.setIdentifier(identifier);

		CodeableConcept cc = new CodeableConcept();
		Coding c = new Coding();
		c.setCode("721981007");
		c.setDisplay("Diagnostic studies report");
		c.setSystem("http://snomed.info/sct");
		cc.addCoding(c);
		composition.setType(cc);

		composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));

		List<SectionComponent> scList = new ArrayList<>();

		// section
		// rescription record
		SectionComponent sc = new SectionComponent();
		sc.setTitle("Lab Test report");
		CodeableConcept cc1 = new CodeableConcept();
		Coding c1 = new Coding();
		c1.setCode("118246004");
		c1.setDisplay("Laboratory test result");
		c1.setSystem("http://snomed.info/sct");
		cc1.addCoding(c1);
		sc.setCode(cc1);
		for (DiagnosticReport dr : diagnosticReportList) {
			sc.addEntry(new Reference(dr.getId()));
		}

		scList.add(sc);

		composition.setSection(scList);

		return composition;
	}

	// method to create object to save to mongo DB
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
