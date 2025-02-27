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
package com.wipro.fhir.controller.healthID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.healthID.HealthIDService;
import com.wipro.fhir.service.healthID.HealthID_WithMobileOTPService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/healthID", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class CreateHealthIDWithMobileOTP {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private HealthID_WithMobileOTPService healthID;
	@Autowired
	private HealthIDService healthIDService;

	/***
	 * 
	 * @param request
	 * @param Authorization
	 * @return NDHM transactionID
	 */
	@CrossOrigin
	@Operation(summary = "generate OTP")
	@PostMapping(value = { "/generateOTP" })
	public String generateOTP(@Param(value = "{\"mobile\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR Generate Mobile OTP API request" + request);
		try {
			if (request != null) {
				String s = healthID.generateOTP(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR Generate Mobile OTP API response" + response.toString());
		return response.toString();
	}

	/***
	 * 
	 * @param request
	 * @param Authorization
	 * @return Generated ABHA for Beneficiary
	 */
	@CrossOrigin
	@Operation(summary = "verify OTP and generate ABHA")
	@PostMapping(value = { "/verifyOTPAndGenerateHealthID" })
	public String verifyOTPAndGenerateHealthID(
			@Param(value = "{\"otp\":\"String\",\"txnId\":\"String\",\"address\":\"String\",\"dayOfBirth\":\"String\",\"email\":\"String\","
					+ "\"healthId\":\"String\",\"healthIdNumber\":\"String\",\"firstName\":\"String\",\"gender\":\"String\",\"lastName\":\"String\",\"monthOfBirth\":\"String\","
					+ "\"name\":\"String\",\"pincode\":\"Integer\",\"yearOfBirth\":\"String\",\"providerServiceMapId\":\"Integer\",\"createdBy\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR validate Mobile OTP and create ABHA API request" + request);
		try {
			if (request != null) {
				String s = healthID.verifyOTPandGenerateHealthID(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR validate Mobile OTP and create ABHA API response" + response.toString());
		return response.toString();
	}

	/***
	 * 
	 * @param comingRequest
	 * @return ABHA of Beneficiary
	 */
	@CrossOrigin()
	@Operation(summary = "Get Beneficiary ABHA details")
	@PostMapping(value = { "/getBenhealthID" })
	public String getBenhealthID(
			@Param(value = "{\"beneficiaryRegID\":\"Long\"}") @RequestBody String comingRequest) {
		OutputResponse response = new OutputResponse();

		logger.info("NDHM_FHIR Request obj to fetch beneficiary ABHA details :" + comingRequest);
		try {
			JSONObject obj = new JSONObject(comingRequest);
			if (obj.length() > 0) {
				Long benRegID = obj.getLong("beneficiaryRegID");
				String res = healthIDService.getBenHealthID(benRegID);
				response.setResponse(res);
			} else {
				logger.info("NDHM_FHIR Invalid Request Data.");
				response.setError(5000, "NDHM_FHIR Invalid request");
			}
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
			logger.error("NDHM_FHIR Error while getting beneficiary ABHA:" + e);
		}
		logger.info("NDHM_FHIR get beneficiary ABHA response:" + response.toString());
		return response.toString();
	}

}
