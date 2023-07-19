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

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
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
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.MedicationRequestResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;

@Service
public class PrescriptionRecordBundleImpl implements PrescriptionRecordBundle {
	@Autowired
	private PatientResource patientResource;
	@Autowired
	private EncounterResource encounterResource;
	@Autowired
	private AppointmentResource appointmentResource;
	@Autowired
	private ConditionResource conditionResource;
	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	@Autowired
	private MedicationRequestResource medicationRequestResource;
	@Autowired
	private PractitionerResource practitionerResource;
	@Autowired
	private OrganizationResource organizationResource;

	@Override
	public int processPrescriptionRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate Prescription resource
		String prescriptionRecordBundle = getPrescriptionRecordBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createPrescriptionRecordBundleMongo(p, prescriptionRecordBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {
			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("TODO - exception - later will implement");

		return i;

	}

	public String getPrescriptionRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		// bundle id
		bundle.setId(commonService.getUUID());

		// bundle identifier
		Identifier identifier = new Identifier();
		identifier.setSystem("https://www.max.in/bundle");
		identifier.setValue(bundle.getId());

		bundle.setIdentifier(identifier);

		// timestamp
		bundle.setTimestamp(new Timestamp(System.currentTimeMillis()));
		bundle.setMeta(new Meta().setLastUpdated(new Date(System.currentTimeMillis())));
		FhirContext fhirContext = FhirContext.forR4();
		// practitioner
		Practitioner practitioner = practitionerResource.getPractitioner();
		// organization
		Organization organization = organizationResource.getOrganization();
		// 1. Patient Resource
		Patient patient = patientResource.getPatientResource(resourceRequestHandler);
		// 3. Condition resource
		List<Condition> condition = conditionResource.getCondition(patient, resourceRequestHandler, "diagnosis");
		// Medication request
		List<MedicationRequest> medicationRequest = medicationRequestResource.getMedicationRequest(patient,
				resourceRequestHandler, practitioner, condition);
		// composition
		Composition composition = getCompositionResourcePrescription(patient, medicationRequest, practitioner);

		/***
		 * adding into bundle
		 */

		// composition
		bundle.addEntry().setFullUrl(composition.getIdElement().getValue()).setResource(composition).getRequest()
				.setUrl("Composition").setMethod(Bundle.HTTPVerb.POST);
		// practitioner
		bundle.addEntry().setFullUrl(organization.getIdElement().getValue()).setResource(organization).getRequest()
				.setUrl("Practitioner").setMethod(Bundle.HTTPVerb.POST);
		// organization
		bundle.addEntry().setFullUrl(practitioner.getIdElement().getValue()).setResource(practitioner).getRequest()
				.setUrl("Organization").setMethod(Bundle.HTTPVerb.POST);
		// patient
		bundle.addEntry().setFullUrl(patient.getIdElement().getValue()).setResource(patient).getRequest()
				.setUrl("Patient").setMethod(Bundle.HTTPVerb.POST);
		// medication request
		for (MedicationRequest med : medicationRequest) {
			bundle.addEntry().setFullUrl(med.getIdElement().getValue()).setResource(med).getRequest()
					.setUrl("MedicationRequest").setMethod(Bundle.HTTPVerb.POST);
		}

		// condition
		for (Condition cond : condition) {
			bundle.addEntry().setFullUrl(cond.getIdElement().getValue()).setResource(cond).getRequest()
					.setUrl("Condition").setMethod(Bundle.HTTPVerb.POST);
		}

		return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
	}

	// method to create object to save to mongo DB
	private AMRIT_ResourceMongo createPrescriptionRecordBundleMongo(PatientEligibleForResourceCreation p,
			String prescriptionRecordBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(prescriptionRecordBundle);
		aMRIT_ResourceMongo.setResourceType("Prescription");

		return aMRIT_ResourceMongo;
	}

	private Composition getCompositionResourcePrescription(Patient patient, List<MedicationRequest> medication,
			Practitioner practitioner) {
		Composition composition = new Composition();
		composition.setId("Composition/" + commonService.getUUID());
		composition.setStatus(CompositionStatus.FINAL);
		composition.setSubject(new Reference(patient.getId()));
		composition.setDate(new Date(System.currentTimeMillis()));
		composition.setTitle("Prescription Document");
		// bundle identifier
		Identifier identifier = new Identifier();
		identifier.setSystem("https://ndhm.in/phr");
		identifier.setValue(composition.getId());
		composition.setIdentifier(identifier);

		CodeableConcept cc = new CodeableConcept();
		Coding c = new Coding();
		c.setCode("440545006");
		c.setDisplay("Prescription record");
		c.setSystem("http://snomed.info/sct");
		cc.addCoding(c);
		composition.setType(cc);

		composition.addAuthor(new Reference(practitioner.getIdElement().getValue()));

		List<SectionComponent> scList = new ArrayList<>();

		// section
		// rescription record
		SectionComponent sc = new SectionComponent();
		sc.setTitle("Prescription record");
		CodeableConcept cc1 = new CodeableConcept();
		Coding c1 = new Coding();
		c1.setCode("440545006");
		c1.setDisplay("Prescription record");
		c1.setSystem("http://snomed.info/sct");
		cc1.addCoding(c1);
		sc.setCode(cc1);
		for (MedicationRequest med : medication) {
			sc.addEntry(new Reference(med.getId()));
		}

		scList.add(sc);

		composition.setSection(scList);

		return composition;
	}
}
