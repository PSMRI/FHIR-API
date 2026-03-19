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

import java.util.List;

import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.OrganizationDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class OrganizationResource {
	
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	
	@Autowired
	private OrganizationDataModel organizationDataModel;
	
	public Organization getOrganizationResource(ResourceRequestHandler resourceRequestHandler) throws FHIRException {

		List<Object[]> rsObj = patientEligibleForResourceCreationRepo
				.callOrganizationSp(resourceRequestHandler.getVisitCode());

		if (rsObj != null && !rsObj.isEmpty()) {
			OrganizationDataModel orgData = organizationDataModel.getOrganization(rsObj.get(0));
			if (orgData != null) {
				return generateOrganizationResource(orgData);
			} else {
				throw new FHIRException("Organization data not found");
			}
		} else {
			throw new FHIRException("Organization not found");
		}
	}

private Organization generateOrganizationResource(OrganizationDataModel orgData) {
	
	    Organization organization = new Organization();

	    organization.setId("Organization/" + orgData.getServiceProviderID());

	    if (orgData.getServiceProviderName() != null) {
	        organization.setName(orgData.getServiceProviderName());
	    }

	    // Alias
	    if (orgData.getLocationName() != null) {
	        organization.addAlias(orgData.getLocationName());
	    }

	    // Identifier (ABDM Facility ID)
	    if (orgData.getAbdmFacilityId() != null) {
	        Identifier identifier = new Identifier();
	        identifier.setSystem("https://facilitysbx.ndhm.gov.in");
	        identifier.setValue(orgData.getAbdmFacilityId());
	        organization.addIdentifier(identifier);
	    }

	    // Address
	    Address address = new Address();

	    if (orgData.getAddress() != null) {
	        address.addLine(orgData.getAddress());
	    }

	    if (orgData.getDistrictName() != null) {
	        address.setDistrict(orgData.getDistrictName());
	    }

	    if (orgData.getStateName() != null) {
	        address.setState(orgData.getStateName());
	    }

	    address.setCountry("India");

	    organization.addAddress(address);
	    

	    return organization;
	}


}
