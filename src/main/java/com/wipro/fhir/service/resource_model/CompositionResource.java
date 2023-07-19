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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.service.common.CommonService;

@Service
public class CompositionResource {

	@Autowired
	private CommonService commonService;


	private Composition composition;

	public Composition getComposition(Patient patient, Encounter encounter, List<AllergyIntolerance> allergyList,
			Appointment appointment, List<Condition> conditionListChiefComplaints,
			List<Condition> conditionListDiagnosis, List<Observation> observationVitalList) {
		return generateCompositionResource(patient, encounter, allergyList, appointment, conditionListChiefComplaints,
				conditionListDiagnosis, observationVitalList);

	}

	// generating dummy Practitioner resource
	private Composition generateCompositionResource(Patient patient, Encounter encounter,
			List<AllergyIntolerance> allergyList, Appointment appointment, List<Condition> conditionListChiefComplaints,
			List<Condition> conditionListDiagnosis, List<Observation> observationVitalList) {
		composition = new Composition();

		composition.setId("Composition/" + commonService.getUUID());
		composition.setStatus(CompositionStatus.FINAL);

		CodeableConcept cc = new CodeableConcept();
		Coding c = new Coding();
		c.setCode("371530004");
		c.setDisplay("Clinical consultation report");
		c.setSystem("https://projecteka.in/sct");
		cc.addCoding(c);

		composition.setType(cc);

		composition.setSubject(new Reference(patient.getId()));
		composition.setDate(new Date(System.currentTimeMillis()));
		composition.setEncounter(new Reference(encounter.getId()));
		composition.setTitle("OP Consultation Document");

		Reference r = new Reference();
		r.setDisplay("Dr Harsh Dhave");
		r.setReference("Practitioner/MAX1456");
		composition.addAuthor(r);

		List<SectionComponent> scList = new ArrayList<>();

		// section
		// allergy-section
		if(allergyList !=null && allergyList.size()>0)
		{
			SectionComponent sc = new SectionComponent();
			sc.setTitle("Allergy Section");
			CodeableConcept cc1 = new CodeableConcept();
			Coding c1 = new Coding();
			c1.setCode("371530004");
			c1.setDisplay("Clinical consultation report");
			c1.setSystem("https://projecteka.in/sct");
			cc1.addCoding(c1);
			sc.setCode(cc1);
			for (AllergyIntolerance allergyObj : allergyList) {
				sc.addEntry(new Reference(allergyObj.getId()));
			}
			scList.add(sc);

		}
		
		// section
		// Follow-up
		if(appointment !=null)
		{
			SectionComponent sc11 = new SectionComponent();
			sc11.setTitle("Follow up");
			CodeableConcept cc11 = new CodeableConcept();
			Coding c11 = new Coding();
			c11.setCode("736271009");
			c11.setDisplay("Follow up");
			c11.setSystem("https://projecteka.in/sct");
			cc11.addCoding(c11);
			sc11.setCode(cc11);
			sc11.addEntry(new Reference(appointment.getId()));
			scList.add(sc11);
		}
		// section
		// Chief complaints

		SectionComponent scChiefComplaints = new SectionComponent();
		if (conditionListChiefComplaints != null && conditionListChiefComplaints.size() > 0) {
			scChiefComplaints = new SectionComponent();
			scChiefComplaints.setTitle("Chief Complaints");
			CodeableConcept ccChiefComplaints = new CodeableConcept();
			Coding cChiefComplaints = new Coding();
			cChiefComplaints.setSystem("https://projecteka.in/sct");
			cChiefComplaints.setCode("422843007");
			cChiefComplaints.setDisplay("Chief Complaint Section");
			ccChiefComplaints.addCoding(cChiefComplaints);
			scChiefComplaints.setCode(ccChiefComplaints);

			for (Condition condition : conditionListChiefComplaints) {
				scChiefComplaints.addEntry(new Reference(condition.getId()));
			}
			scList.add(scChiefComplaints);
		}
		

		// physical examination
		if(observationVitalList !=null && observationVitalList.size()>0)
		{
			SectionComponent sc111 = new SectionComponent();
			sc111.setTitle("Physical Examination");
			CodeableConcept cc111 = new CodeableConcept();
			Coding c111 = new Coding();
			c111.setCode("425044008");
			c111.setDisplay("Physical exam section");
			c111.setSystem("https://projecteka.in/sct");
			cc111.addCoding(c111);
			sc111.setCode(cc111);
			for (Observation observation : observationVitalList) {
				sc111.addEntry(new Reference(observation.getId()));
			}
			scList.add(sc111);

			composition.setSection(scList);

		}
		
		return composition;
	}
}
