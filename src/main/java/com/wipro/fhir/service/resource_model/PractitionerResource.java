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

import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.PractitionerDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class PractitionerResource {

	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	
	@Autowired
	private PractitionerDataModel practitionerDataModel;
	
	@Value("${hipSystemUrl}")
	private String systemUrl;

	public Practitioner getPractitionerResource(ResourceRequestHandler resourceRequestHandler)
			throws FHIRException {

		List<Object[]> rsObj = patientEligibleForResourceCreationRepo.callPractitionerSP(resourceRequestHandler.getVisitCode());

		 if (rsObj == null || rsObj.isEmpty()) {
		        throw new FHIRException("invalid practitioner data");
		    }
		
		PractitionerDataModel practitionerData = practitionerDataModel.getPractitioner(rsObj.get(0));
		return generatePractitionerResource(practitionerData);
	}
	
	private Practitioner generatePractitionerResource(PractitionerDataModel practitionerData) {

		Practitioner practitioner = new Practitioner();

		// ID
		practitioner.setId("Practitioner/" + practitionerData.getUserID());

		// Identifier (Employee / Registration ID)
		if (practitionerData.getEmployeeID() != null) {
			Identifier identifier = new Identifier();
			identifier.setSystem(systemUrl);
			identifier.setValue(practitionerData.getEmployeeID());
			practitioner.addIdentifier(identifier);
		}

		// Name
		HumanName name = new HumanName();

		if (practitionerData.getFullName() != null) {
			name.setText(practitionerData.getFullName());
		}

		// Prefix (Designation)
		if (practitionerData.getDesignationName() != null) {
			name.addPrefix(practitionerData.getDesignationName());
		}

		// Suffix (Qualification)
		if (practitionerData.getQualificationName() != null) {
			name.addSuffix(practitionerData.getQualificationName());
		}

		practitioner.addName(name);

		// Gender
		if (practitionerData.getGenderName() != null) {
			switch (practitionerData.getGenderName()) {
			case "Male":
				practitioner.setGender(AdministrativeGender.MALE);
				break;
			case "Female":
				practitioner.setGender(AdministrativeGender.FEMALE);
				break;
			default:
				practitioner.setGender(AdministrativeGender.UNKNOWN);
				break;
			}
		}

		// DOB
		if (practitionerData.getDob() != null) {
			practitioner.setBirthDate(practitionerData.getDob());
		}

		// Telecom - Phone
		if (practitionerData.getContactNo() != null) {
			ContactPoint phone = new ContactPoint();
			phone.setSystem(ContactPointSystem.PHONE);
			phone.setValue(practitionerData.getContactNo());
			practitioner.addTelecom(phone);
		}

		// Telecom - Email
		if (practitionerData.getEmailID() != null) {
			ContactPoint email = new ContactPoint();
			email.setSystem(ContactPointSystem.EMAIL);
			email.setValue(practitionerData.getEmailID());
			practitioner.addTelecom(email);
		}

		return practitioner;
	}


}
