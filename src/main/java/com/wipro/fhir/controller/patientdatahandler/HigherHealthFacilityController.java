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
package com.wipro.fhir.controller.patientdatahandler;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.patient_data_handler.HigherHealthFacilityServiceImpl;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/higher/health/facility", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class HigherHealthFacilityController {

	@Autowired
	private HigherHealthFacilityServiceImpl higherHealthFacilityServiceImpl;

	@Operation(summary = "Update beneficiary id for higher health facility")
	@PostMapping(value = { "/update/bengenid" })
	public String feedPatientDemographicData(@RequestBody ResourceRequestHandler resourceRequestHandler) {

		OutputResponse response = new OutputResponse();
		try {
			String s = higherHealthFacilityServiceImpl
					.updateBengenIDToHigherHealthFacilityBeneficiary(resourceRequestHandler);
			if (s != null)
				response.setResponse(s);
			else
				response.setError(5000, "Error in updating Beneficary ID to higher health data");
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
			response.setError(5000, "Error in updating Beneficary ID to higher health data. " + e.getMessage());
		}

		return response.toString();

	}

	@Operation(summary = "Get clinical data from higher health facility")
	@PostMapping(value = { "/get/clinical/data" })
	public String getCLinicalDataHigherhealthFacility(@RequestBody ResourceRequestHandler resourceRequestHandler) {

		OutputResponse response = new OutputResponse();
		try {
			String s = higherHealthFacilityServiceImpl.getCLinicalDataHigherhealthFacility(resourceRequestHandler);
			if (s != null)
				response.setResponse(s);
			else
				response.setError(5000, "Error in getting higher health data");
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
			response.setError(5000, "Error in getting higher health data. " + e.getMessage());
		}

		return response.toString();

	}
}
