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
package com.wipro.fhir.service.resource_model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCategory;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceType;
import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.AllergyIntoleranceDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class AllergyIntoleranceResource {

	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private AllergyIntoleranceDataModel allergyIntoleranceDataModel;

	private String UUID;
	AllergyIntolerance allergy;

	public List<AllergyIntolerance> getAllergyIntolerance(Patient patient, Encounter encounter,
			ResourceRequestHandler resourceRequestHandler, Practitioner practitioner) {

		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo
				.callAllergySP(resourceRequestHandler.getBeneficiaryRegID(),resourceRequestHandler.getVisitCode());

		List<AllergyIntoleranceDataModel> allergyList = allergyIntoleranceDataModel.getAllergyList(rsObjList);

		return generateAllergyIntoleranceResource(patient, encounter, allergyList, practitioner);

	}

	private List<AllergyIntolerance> generateAllergyIntoleranceResource(Patient patient, Encounter encounter,
			List<AllergyIntoleranceDataModel> rSList, Practitioner practitioner) {

		List<AllergyIntolerance> allergyResourceList = new ArrayList<>();

		CodeableConcept ccClinicalStatus;
		Coding cClinicalStatus;

		CodeableConcept ccVerificationStatus;
		Coding cVerificationStatus;

		CodeableConcept ccCode;
		Coding cCode;

		// for every allergy entry create a new coding and add to list
		for (AllergyIntoleranceDataModel obj : rSList) {

			allergy = new AllergyIntolerance();
			allergy.setRecordedDate(obj.getCreatedDate());
			

			// clinical status
			ccClinicalStatus = new CodeableConcept();
			cClinicalStatus = new Coding();
			cClinicalStatus.setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical");
			cClinicalStatus.setCode("active");
			cClinicalStatus.setDisplay("Active");
			ccClinicalStatus.addCoding(cClinicalStatus);
			allergy.setClinicalStatus(ccClinicalStatus);
			// verification status
			ccVerificationStatus = new CodeableConcept();
			cVerificationStatus = new Coding();
			cVerificationStatus.setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical");
			cVerificationStatus.setCode("unconfirmed");
			cVerificationStatus.setDisplay("Unconfirmed");
			ccVerificationStatus.addCoding(cVerificationStatus);
			allergy.setVerificationStatus(ccVerificationStatus);
			// type
			allergy.setType(AllergyIntoleranceType.ALLERGY);
			// category
			if (obj.getAllergyType() != null) {
				switch (obj.getAllergyType().toLowerCase()) {
				case "drugs": {
					allergy.addCategory(AllergyIntoleranceCategory.MEDICATION);
					allergy.setId("AllergyIntolerance/medication");
				}
					break;
				case "food": {
					allergy.addCategory(AllergyIntoleranceCategory.FOOD);
					allergy.setId("AllergyIntolerance/food");
				}
					break;
				case "environmental": {
					allergy.addCategory(AllergyIntoleranceCategory.ENVIRONMENT);
					allergy.setId("AllergyIntolerance/environment");
				}
				default:
				}

			}

			// code

			if (obj.getSctcode() != null) {
				ccCode = new CodeableConcept();
				cCode = new Coding();
				cCode.setSystem("http://snomed.info/sct");
				cCode.setCode((obj.getSctcode()));
				cCode.setDisplay(obj.getSctTerm());
				ccCode.addCoding(cCode);
				allergy.setCode(ccCode);

			}

			if (obj.getAllergicReactionType() != null && obj.getAllergicReactionType().length() > 0) {
				List<Annotation> annotationList = new ArrayList<>();
				Annotation annotation = new Annotation();
				annotation.setText("Having these side effects : " + obj.getAllergicReactionType());
				annotationList.add(annotation);
				allergy.setNote(annotationList);
			}
			allergyResourceList.add(allergy);
		}

		// referance - patient
		if(allergy !=null)
		{
			allergy.setPatient(new Reference(patient.getIdElement().getValue()));
			// asserter
			Reference asrtrRef = new Reference();
			asrtrRef.setReference(practitioner.getId());
			asrtrRef.setDisplay(practitioner.getName().get(0).getText());
			allergy.setAsserter(asrtrRef);
			// encounter
			allergy.setEncounter(new Reference(encounter.getIdElement().getValue()));
		}
		return allergyResourceList;
	}
}
