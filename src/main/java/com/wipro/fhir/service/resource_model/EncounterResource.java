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
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.DiagnosisComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.EncounterDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;

/***
 * 
 * @author NE298657
 *
 */
@Service
public class EncounterResource {

	@Autowired
	private CommonService commonService;

	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private EncounterDataModel encounterDataModel;

	private Encounter encounter;

	public Encounter getEncounterResource(Patient patient, Appointment appointment,
			ResourceRequestHandler resourceRequestHandler, List<Condition> conditionListChiefComplaints,
			List<Condition> conditionListDiagnosis) {

		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo
				.callEncounterSP(resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<EncounterDataModel> encounterList = encounterDataModel.getEncounterList(rsObjList);
		return generateEncounterResource(patient, appointment, encounterList, conditionListChiefComplaints,
				conditionListDiagnosis);
	}

	private Encounter generateEncounterResource(Patient patient, Appointment appointment,
			List<EncounterDataModel> encounterList, List<Condition> conditionListChiefComplaints,
			List<Condition> conditionListDiagnosis) {
		encounter = new Encounter();

		encounter.setId("Encounter/" + commonService.getUUID());

		if (encounterList != null && encounterList.size() > 0) {
			// use nurse, doctor, specialist and pharma etc flag for annotation
			Integer nurseFlag = encounterList.get(0).getNurseFlag();
			Integer docFlag = encounterList.get(0).getDocFlag();
			Integer specialistFlag = encounterList.get(0).getSpecialistFlag();

			String status = "";
			if (specialistFlag != null && specialistFlag != 0) {
				if (specialistFlag == 1)
					status = "P";
				else if (specialistFlag == 3 || specialistFlag == 2)
					status = "IP";
				else if (specialistFlag == 9)
					status = "F";
			} else if (docFlag != null && docFlag != 0) {
				if (docFlag == 1)
					status = "P";
				else if (docFlag == 2 || docFlag == 3)
					status = "IP";
				else if (docFlag == 9)
					status = "F";
			} else if (nurseFlag != null && nurseFlag != 0) {
				if (nurseFlag == 1)
					status = "P";
				else if (nurseFlag == 2 || nurseFlag == 3 || nurseFlag == 9)
					status = "IP";

			}
			switch (status) {
			case "P":
				encounter.setStatus(EncounterStatus.PLANNED);
				break;
			case "IP":
				encounter.setStatus(EncounterStatus.INPROGRESS);
				break;
			case "F":
				encounter.setStatus(EncounterStatus.FINISHED);
				break;
			default:
				encounter.setStatus(EncounterStatus.UNKNOWN);
			}

			// period
			Period p = new Period();
			p.setStart(new Date(encounterList.get(0).getCreatedDate().getTime()));
			encounter.setPeriod(p);

		}

		// class
		Coding c = new Coding();
		c.setCode("AMB");
		c.setDisplay("ambulatory");
		encounter.setClass_(c);

		// referance - patient
		encounter.setSubject(new Reference(patient.getIdElement().getValue()));

		// diagnosis
		List<DiagnosisComponent> dcList = new ArrayList<>();
		DiagnosisComponent dc;
		CodeableConcept ccCondition;
		Coding cCondition;
		List<Coding> cConditionList;

		// chief complaints
		for (Condition condition : conditionListChiefComplaints) {
			dc = new DiagnosisComponent();
			cCondition = new Coding();
			cConditionList = new ArrayList<>();
			ccCondition = new CodeableConcept();

			dc.setCondition(new Reference(condition.getIdElement().getValue()));

			cCondition.setSystem("http://snomed.info/sct");
			cCondition.setCode("33962009");
			cCondition.setDisplay("Chief complaint");

			cConditionList.add(cCondition);
			ccCondition.setCoding(cConditionList);

			dc.setUse(ccCondition);

			dcList.add(dc);
		}

		// diagnosis
		for (Condition condition : conditionListDiagnosis) {
			dc = new DiagnosisComponent();
			cCondition = new Coding();
			cConditionList = new ArrayList<>();
			ccCondition = new CodeableConcept();

			dc.setCondition(new Reference(condition.getIdElement().getValue()));

			cCondition.setSystem("http://snomed.info/sct");
			cCondition.setCode("148006");
			cCondition.setDisplay("Provisional diagnosis");

			cConditionList.add(cCondition);
			ccCondition.setCoding(cConditionList);

			dc.setUse(ccCondition);

			dcList.add(dc);
		}

		encounter.setDiagnosis(dcList);

		return encounter;
	}
}
