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
import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.patient.PatientDemographic;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.common.CommonServiceImpl;
import com.wipro.fhir.utils.exception.FHIRException;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class PatientResource {

	@Autowired
	private APIChannel aPIChannel;
	@Autowired
	private CommonServiceImpl commonServiceImpl;

	private Patient p;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;

	@Autowired
	private PatientDemographic patientDemographic;

	public Patient getPatientResource(ResourceRequestHandler resourceRequestHandler) throws FHIRException {

		
		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo
				.callPatientDemographicSP(resourceRequestHandler.getBeneficiaryRegID());

		PatientDemographic patientDemographicOBJ = patientDemographic.getPatientDemographic(rsObjList);

		if (patientDemographicOBJ != null) {
			if (patientDemographicOBJ.getBeneficiaryRegID() != null) {
				return generatePatientResource(patientDemographicOBJ);
			} else
				throw new FHIRException("multiple patient found with given identifier / beneficiaryID");
		} else
			throw new FHIRException("patient not found");
	}

	private Patient generatePatientResource(PatientDemographic pd) {
		String UUID = commonServiceImpl.getUUID();
		p = new Patient();

		p.setId("Patient/" + pd.getBeneficiaryID());

		// get ABHA for patient - later will be available in patient search only.
		// temp code - till search API is integrated with ABHA


		// list of identifiers
		List<Identifier> identifierList = new ArrayList<Identifier>();
		Identifier identifiers;

		if (pd.getHealthIdNo() != null) {

			// ABHA no
			identifiers = new Identifier();
			CodeableConcept cc = new CodeableConcept();
			Coding c = new Coding();
			c.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
			c.setCode("MR");
			c.setDisplay(pd.getHealthIdNo());
			cc.addCoding(c);
			identifiers.setType(cc);
			identifierList.add(identifiers);
		}

		p.setIdentifier(identifierList);

		// name
		HumanName hName = new HumanName();
		
		if (pd.getName() != null)
			hName.setText(pd.getName());
		p.addName(hName);

		

		// gender
		if (pd.getGender() != null) {
			switch (pd.getGender()) {
			case "Male":
				p.setGender(AdministrativeGender.MALE);
				break;
			case "Female":
				p.setGender(AdministrativeGender.FEMALE);
				break;
			case "Transgender":
				p.setGender(AdministrativeGender.OTHER);
				break;
			default:
				p.setGender(AdministrativeGender.UNKNOWN);
				break;
			}
		} else {
			if (pd.getGenderID() != null) {
				switch (pd.getGenderID()) {
				case 1:
					p.setGender(AdministrativeGender.MALE);
					break;
				case 2:
					p.setGender(AdministrativeGender.FEMALE);
					break;
				case 3:
					p.setGender(AdministrativeGender.OTHER);
					break;

				default:
					p.setGender(AdministrativeGender.UNKNOWN);
					break;
				}
			}
		}

		// DOB
		if (pd.getDOB() != null) {
			p.setBirthDate(pd.getDOB());
		}


		return p;
	}
}
