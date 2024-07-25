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
package com.wipro.fhir.controller.healthIDvalidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.healthID_validate.HealthIDValidationService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/validate", headers = "Authorization")
public class HealthIDValidateController {

	@Autowired
	private HealthIDValidationService healthIDValidationService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@CrossOrigin
	@Operation(summary = "Generate OTP for ABHA validation")
	@PostMapping(value = { "/generateOTPForHealthIDValidation" })
	public String generateOTPForHealthIDValidation(
			@Param(value = "{\"healthID\":\"String\",\"isValidate\":\"Boolean\",\"authenticationMode\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR Generate Mobile OTP for ABHA validate API request" + request);
		try {
			if (request != null) {
				String s = healthIDValidationService.generateOTPForHealthIDValidation(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("NDHM_FHIR Generate Mobile OTP for ABHA validate API response" + response.toString());
		return response.toString();
	}

	@CrossOrigin
	@Operation(summary = "Verify OTP for ABHA validation")
	@PostMapping(value = { "/verifyOTPForHealthIDValidation" })
	public String verifyOTPForHealthIDValidation(
			@Param(value = "{\"otp\":\"String\",\"txnId\":\"String\",\"healthID\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR verify OTP for ABHA validate API request" + request);
		try {
			if (request != null) {
				String s = healthIDValidationService.validateOTPAndHealthID(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("NDHM_FHIR verify OTP for ABHA validate API response" + response.toString());
		return response.toString();
	}
	
}
