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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.mongo.amrit_resource.TempCollection;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.resource_model.AllergyIntoleranceResource;
import com.wipro.fhir.service.resource_model.AppointmentResource;
import com.wipro.fhir.service.resource_model.CompositionResource;
import com.wipro.fhir.service.resource_model.ConditionResource;
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.FamilyMemberHistoryResource;
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PatientResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;
import com.wipro.fhir.utils.exception.FHIRException;

import ca.uhn.fhir.context.FhirContext;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class OPConsultRecordBundleImpl implements OPConsultRecordBundle {

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientResource patientResource;
	@Autowired
	private AllergyIntoleranceResource allergyIntoleranceResource;
	@Autowired
	private AppointmentResource appointmentResource;
	@Autowired
	private ConditionResource conditionResource;
	@Autowired
	private EncounterResource encounterResource;
	@Autowired
	private FamilyMemberHistoryResource familyMemberHistoryResource;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private ObservationResource observationResource;
	@Autowired
	private PractitionerResource practitionerResource;
	@Autowired
	private OrganizationResource organizationResource;
	@Autowired
	private CompositionResource compositionResource;

	@Override
	public int processOPConsultRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		int i = 0;
		// call method to generate OP_consult resource
		String opConsultRecordBundle = getOPConsultRecordBundle(resourceRequestHandler, p);

		// call private method to create mongo object with resource data
		AMRIT_ResourceMongo aMRIT_ResourceMongo = createOPConsultRecordBundleMongo(p, opConsultRecordBundle);
		// if resource data is not null, save to mongo
		if (aMRIT_ResourceMongo != null) {

			i = commonService.saveResourceToMongo(aMRIT_ResourceMongo);

		} else
			throw new FHIRException("TODO - exception - later will implement");

		return i;

	}

	@Override
	public String getOPConsultRecordBundle(ResourceRequestHandler resourceRequestHandler,
			PatientEligibleForResourceCreation p) throws FHIRException {
		Bundle bundle = new Bundle();

		// bundle type - document
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

		// practitioner resource
		Practitioner practitioner = practitionerResource.getPractitioner();

		// Organization resource
		Organization organization = organizationResource.getOrganization();

		// Patient resource
		Patient patient = patientResource.getPatientResource(resourceRequestHandler);

		// Appointment resource
		Appointment appointment = appointmentResource.getAppointmentResource(resourceRequestHandler, practitioner);

		// Condition resource, chiefcomplaints
		List<Condition> conditionListChiefComplaints = conditionResource.getCondition(patient, resourceRequestHandler,
				"chiefcomplaints");

		// Condition resource, diagnosis
		List<Condition> conditionListDiagnosis = conditionResource.getCondition(patient, resourceRequestHandler,
				"diagnosis");

		// FamilyMemberHistory resource
///		FamilyMemberHistory familyMemberHistory = familyMemberHistoryResource.getFamilyMemberHistory(patient,
///				resourceRequestHandler);

		// Encounter resource
		Encounter encounter = encounterResource.getEncounterResource(patient, appointment, resourceRequestHandler,
				conditionListChiefComplaints, conditionListDiagnosis);

		// AllergyIntolerance resource
		List<AllergyIntolerance> allergyList = allergyIntoleranceResource.getAllergyIntolerance(patient, encounter,
				resourceRequestHandler, practitioner);

		// Observation- Physical Examination - vitals
		List<Observation> observationVitalList = observationResource.getObservationVitals(patient, encounter,
				resourceRequestHandler);



		// Composition resource
		Composition composition = compositionResource.getComposition(patient, encounter, allergyList, appointment,
				conditionListChiefComplaints, conditionListDiagnosis, observationVitalList);

		/***
		 * add resource to bundle
		 */

		// composition
		bundle.addEntry().setFullUrl(composition.getIdElement().getValue()).setResource(composition).getRequest()
				.setUrl("Composition").setMethod(Bundle.HTTPVerb.POST);

		// practitioner
		bundle.addEntry().setFullUrl(practitioner.getIdElement().getValue()).setResource(practitioner).getRequest()
				.setUrl("Practitioner").setMethod(Bundle.HTTPVerb.POST);

		// organization
		bundle.addEntry().setFullUrl(organization.getIdElement().getValue()).setResource(organization).getRequest()
				.setUrl("Organization").setMethod(Bundle.HTTPVerb.POST);

		// patient
		bundle.addEntry().setFullUrl(patient.getIdElement().getValue()).setResource(patient).getRequest()
				.setUrl("Patient").setMethod(Bundle.HTTPVerb.POST);

		// appointment
		bundle.addEntry().setFullUrl(appointment.getIdElement().getValue()).setResource(appointment).getRequest()
				.setUrl("appointmentResource").setMethod(Bundle.HTTPVerb.POST);

		// condition - chief complaints
		for (Condition condition : conditionListChiefComplaints) {
			bundle.addEntry().setFullUrl(condition.getIdElement().getValue()).setResource(condition).getRequest()
					.setUrl("Condition").setMethod(Bundle.HTTPVerb.POST);
		}

		// condition - diagnosis
		for (Condition condition : conditionListDiagnosis) {
			bundle.addEntry().setFullUrl(condition.getIdElement().getValue()).setResource(condition).getRequest()
					.setUrl("Condition").setMethod(Bundle.HTTPVerb.POST);
		}

		// observation
		for (Observation obsr : observationVitalList) {
			bundle.addEntry().setFullUrl(obsr.getIdElement().getValue()).setResource(obsr).getRequest()
					.setUrl("Observation").setMethod(Bundle.HTTPVerb.POST);
		}

		// allergy
		for (AllergyIntolerance allergyResource : allergyList) {
			bundle.addEntry().setFullUrl(allergyResource.getIdElement().getValue()).setResource(allergyResource)
					.getRequest().setUrl("AllergyIntolerance").setMethod(Bundle.HTTPVerb.POST);
		}


		bundle.addEntry().setFullUrl(encounter.getIdElement().getValue()).setResource(encounter).getRequest()
				.setUrl("Encounter").setMethod(Bundle.HTTPVerb.POST);

		return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
	}

	// method to create object to save to mongo DB
	private AMRIT_ResourceMongo createOPConsultRecordBundleMongo(PatientEligibleForResourceCreation p,
			String opConsultRecordBundle) {
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

		aMRIT_ResourceMongo.setResourceJson(opConsultRecordBundle);
		aMRIT_ResourceMongo.setResourceType("OPConsultation");

		return aMRIT_ResourceMongo;
	}

	@Deprecated
	private TempCollection createTempCollectionOBJ(Map<Integer, List<Observation>> observationMap,
			ResourceRequestHandler rrh) {

		Map<Integer, List<String>> tempMap = new HashMap<Integer, List<String>>();
		List<String> tempList;

		TempCollection tempCollection = new TempCollection();
		tempCollection.setBeneficiaryRegID(rrh.getBeneficiaryRegID());
		tempCollection.setVisitCode(rrh.getVisitCode());
		tempCollection.setDataType("observationResourceMap");

		for (Map.Entry<Integer, List<Observation>> entry : observationMap.entrySet()) {
			if (entry.getValue().size() > 0) {
				tempList = new ArrayList<>();
				for (Observation o : entry.getValue()) {
					tempList.add(o.getId());
				}
				tempMap.put(entry.getKey(), tempList);
			}
		}

		tempCollection.setDataJson(tempMap);
		tempCollection.setCreateDate(new Date(System.currentTimeMillis()));
		tempCollection.setCreateBy("default");
		return tempCollection;
	}

}
