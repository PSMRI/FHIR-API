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

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.stereotype.Service;

@Service
public class PractitionerResource {


	private Practitioner practitioner;

	public Practitioner getPractitioner() {
		return generatePractitionerResource();

	}

	// generating dummy Practitioner resource
	private Practitioner generatePractitionerResource() {
		practitioner = new Practitioner();

		practitioner.setId("Practitioner/MAX1456");

		List<Identifier> iList = new ArrayList<>();
		Identifier i = new Identifier();
		i.setSystem("https://www.mciindia.in/doctor");
		i.setValue("MAX1456");
		iList.add(i);
		practitioner.setIdentifier(iList);

		List<HumanName> pNameList = new ArrayList<>();
		HumanName hName = new HumanName();

		hName.setText("Harsh Dhave");

		List<StringType> stList = new ArrayList<>();
		StringType st = new StringType();
		st.setValue("Dr");
		stList.add(st);
		hName.setPrefix(stList);

		List<StringType> stList1 = new ArrayList<>();
		StringType st1 = new StringType();
		st.setValue("MBBS");
		stList1.add(st1);
		hName.setSuffix(stList1);

		pNameList.add(hName);

		practitioner.setName(pNameList);

		return practitioner;
	}
}
