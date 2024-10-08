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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestIntent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.MedicationRequestDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class MedicationRequestResource {

	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private MedicationRequestDataModel medicationRequestDataModel;

	MedicationRequest medicationRequest;

	public List<MedicationRequest> getMedicationRequest(Patient patient, ResourceRequestHandler resourceRequestHandler,
			Practitioner practitioner, List<Condition> conditionDiagnosis) {

		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo.callMedicationRequestSP(
				resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<MedicationRequestDataModel> medicationRequestList = medicationRequestDataModel
				.getMedicationRequestList(rsObjList);

		return generateMedicationRequestResource(patient, medicationRequestList, practitioner, conditionDiagnosis);

	}

	private List<MedicationRequest> generateMedicationRequestResource(Patient patient,
			List<MedicationRequestDataModel> medicationRequestList, Practitioner practitioner,
			List<Condition> conditionDiagnosis) {

		List<MedicationRequest> responseList = new ArrayList<>();
		// medicationCodeableConcept
		CodeableConcept codeableConcept;
		List<CodeableConcept> codeableConceptList;
		Coding c;
		List<Coding> cList;

		Dosage dosage;
		List<Dosage> dosageList;

		List<CodeableConcept> conceptList;
		List<Reference> ref;
		for (MedicationRequestDataModel obj : medicationRequestList) {
			medicationRequest = new MedicationRequest();

			codeableConcept = new CodeableConcept();
			c = new Coding();
			cList = new ArrayList<>();

			medicationRequest.setId("MedicationRequest/" + commonService.getUUID());

			medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
			medicationRequest.setIntent(MedicationRequestIntent.ORDER);

			// subject ref_patient
			medicationRequest.setSubject(new Reference(patient.getIdElement().getValue()));
			// authoredOn
			medicationRequest.setAuthoredOn(new Time(obj.getCreatedDate().getTime()));
			// requester ref_practitioner
			medicationRequest.setRequester(new Reference(practitioner.getIdElement().getValue()));

			// medicationCodeableConcept
			c.setCode(obj.getSnomedCTCodeDrug());
			c.setDisplay(obj.getSnomedCTTermDrug());
			c.setSystem("http://snomed.info/sct");
			cList.add(c);
			codeableConcept.setCoding(cList);
			codeableConcept.setText(obj.getGenericDrugName());
			medicationRequest.setMedication(codeableConcept);

			codeableConcept = new CodeableConcept();
			c = new Coding();
			cList = new ArrayList<>();
			codeableConceptList = new ArrayList<>();
			dosage = new Dosage();
			dosageList = new ArrayList<>();
			// dosage instruction
			codeableConcept.setText("Remarks : " + (obj.getInstructions() != null ? obj.getInstructions() : "N/A"));
			codeableConceptList.add(codeableConcept);
			dosage.setAdditionalInstruction(codeableConceptList);

			// text
			dosage.setText((obj.getDrugDose() != null ? obj.getDrugDose() + " " : "")
					+ (obj.getDrugFrequency() != null ? obj.getDrugFrequency() : "") + " for "
					+ (obj.getDuration() != null ? obj.getDuration() : "") + " "
					+ (obj.getDurationUnit() != null ? obj.getDurationUnit() : "") + ". Remarks : "
					+ (obj.getInstructions() != null ? obj.getInstructions() : ""));

			// route
			codeableConcept = new CodeableConcept();
			codeableConcept.setText(obj.getDrugRoute() != null ? obj.getDrugRoute() : "route not available");
			dosage.setRoute(codeableConcept);
			dosageList.add(dosage);

			medicationRequest.setDosageInstruction(dosageList);

			// reason
			conceptList = new ArrayList<CodeableConcept>();
			ref = new ArrayList<Reference>();
			for (Condition cond : conditionDiagnosis) {
				ref.add(new Reference(cond.getId()));
				conceptList.add(cond.getCode());
			}
			medicationRequest.setReasonCode(conceptList);
			medicationRequest.setReasonReference(ref);

			responseList.add(medicationRequest);

		}

		return responseList;
	}

}
