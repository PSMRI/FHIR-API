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
package com.wipro.fhir.controller.eaushdhi;

import java.lang.reflect.Type;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wipro.fhir.service.e_aushdhi.EAushadhiService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;

/**
 * @author DE40034072 Date 01-12-2021
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/eAushadhi", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class EAushadhiController {

	@Autowired
	private EAushadhiService eAushadhiService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@CrossOrigin
	@Operation(summary = "Getting store stock details from e-aushadhi")
	@PostMapping(value = { "/getStoreStockDetails" })
	public String getStoreStockDetails(@Param(value = "{\"facilityID\":\"Integer\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();

		try {

			String resp = eAushadhiService.getEaushadhiStoreDetailsByFacilityID(request);

			if (resp.equals("success")) {
				response.setResponse("E-aushadhi Stock Added Successfully");

			} else

				response.setError(5000, "E-aushadhi Error while getting store stock details : " + resp);

		} catch (FHIRException e) {

			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("E-Aushadhi store stock details API response" + response.toString());
		return response.toString();
	}

	/***
	 * 
	 * @param request
	 * @param Authorization
	 * @return sync dispense data and patient information to E-Aushadhi.
	 */
	@CrossOrigin
	@Operation(summary = "Sync drug dispense data and patient details with e-aushadhi")
	@PostMapping(value = { "/syncDrugDispenseDetails" })
	public String syncDrugDispenseAndPatientDetails(
			@Param(value = "{\"eAushadhiFacilityId\":\"Integer\",\"pageNo\":\"Integer\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("E-Aushadhi Sync dispense data and patient details API request" + request);
		try {
			if (request != null) {
				String s = eAushadhiService.syncDispenseDetailsToEAushadhi(request, Authorization);
				response.setResponse(s);
			} else
				throw new FHIRException("Error empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("E-Aushadhi Sync dispense data and patient details API response" + response.toString());
		return response.toString();
	}

	@CrossOrigin
	@Operation(summary = "Get log for stock processing")
	@PostMapping(value = { "/getFacilityStockProcessLog" })
	public String getFacilityStockProcessLog(@RequestBody String request) {

		OutputResponse response = new OutputResponse();
		logger.info("get log for stock processing API request" + request);
		try {
			if (request != null) {
				String s = eAushadhiService.getFacilityStockProcessLog(request);
				response.setResponse(s);
			} else
				throw new FHIRException("Error empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("get log for stock processing API response" + response.toString());
		return response.toString();
	}

	@CrossOrigin
	@Operation(summary = "Sync e-aushadhi for patient issue details")
	@PostMapping(value = { "/updatePatientIssueSyncStatus" })
	public String addFacility(@RequestBody String request) {

		OutputResponse response = new OutputResponse();

		try {

			String jsonStringArray = request;
			Gson converter = new Gson();
			Type type = new TypeToken<List<String>>() {
			}.getType();
			List<String> syncedIssueDetails = converter.fromJson(jsonStringArray, type);

			if (syncedIssueDetails.size() > 0) {
				String s = eAushadhiService.updateSyncStatusForEAushadhiDispense(syncedIssueDetails);
				response.setResponse(s);
			} else
				throw new FHIRException("Error empty request object");

		} catch (Exception e) {

			logger.error("Unexpected error:", e);
			response.setError(e);

		}
		return response.toString();

	}

}
