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

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/patient/data", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class PatientDataGatewayController {

	@Autowired
	private PatientDataGatewayService patientDataGatewayService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Operation(summary = "Patient profile search from Mongo, search parameter - healthId, healthIdNo, amritId, externalId, phoneNo, state, district, village")
	@PostMapping(value = { "/profile/search/demographic" })
	public String patientDataSearchFromMongo(@RequestBody ResourceRequestHandler resourceRequestHandler,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		try {
			String s = patientDataGatewayService.searchPatientProfileMongo(Authorization, resourceRequestHandler);
			if (s != null)
				response.setResponse(s);
			else
				response.setResponse("patient not found");
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
		}

		return response.toString();

	}

	@Operation(summary = "Patient profile search from Mongo, all data based on page no")
	@GetMapping(value = { "/searchWithPagination/{pageNo}" }, produces = MediaType.APPLICATION_JSON)
	public String patientDataSearchFromMongoPagination(@PathVariable("pageNo") Integer pageNo) {

		OutputResponse response = new OutputResponse();
		try {
			String s = patientDataGatewayService.searchPatientProfileMongoPagination(pageNo);
			if (s != null)
				response.setResponse(s);
			else
				response.setResponse("No data found");
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
		}

		return response.toString();

	}

}
