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

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.stereotype.Service;

@Service
public class OrganizationResource {
	private Organization organization;

	public Organization getOrganization() {
		return generateOrganizationResource();

	}

	// generating dummy Practitioner resource
	private Organization generateOrganizationResource() {
		organization = new Organization();

		organization.setId("Organization/MaxSaket01");
		organization.setName("Max Super Speciality Hospital, Saket");

		List<StringType> aliasList = new ArrayList<>();
		StringType alias = new StringType();
		alias.setValue("Max");
		aliasList.add(alias);
		organization.setAlias(aliasList);

		List<Identifier> iList = new ArrayList<>();
		Identifier i = new Identifier();
		i.setSystem("https://facilitysbx.ndhm.gov.in");
		i.setValue("IN0410000183");
		iList.add(i);
		organization.setIdentifier(iList);

		List<ContactPoint> cpList = new ArrayList<>();
		ContactPoint contactPoint = new ContactPoint();
		contactPoint.setSystem(ContactPointSystem.PHONE);
		contactPoint.setValue("(+91) 011-2651-5050");
		cpList.add(contactPoint);

		organization.setTelecom(cpList);

		List<Address> addressList = new ArrayList<>();
		Address address = new Address();
		address.setCity("New Delhi");
		address.setState("New Delhi");
		address.setPostalCode("New Delhi");
		address.setCountry("India");
		List<StringType> addressLineList = new ArrayList<>();
		StringType addLine = new StringType();
		addLine.setValue("1, 2, Press Enclave Marg, Saket Institutional Area, Saket");
		addressLineList.add(addLine);
		address.setLine(addressLineList);
		addressList.add(address);

		organization.setAddress(addressList);

		List<Reference> rlist = new ArrayList<>();
		Reference r = new Reference();
		r.setReference("https://www.max.in/hospital-network/max-super-speciality-hospital-saket");
		r.setDisplay("Website");
		rlist.add(r);
		organization.setEndpoint(rlist);

		return organization;
	}
}
