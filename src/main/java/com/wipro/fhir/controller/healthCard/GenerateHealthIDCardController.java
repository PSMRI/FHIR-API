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
package com.wipro.fhir.controller.healthCard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.healthID.HealthID_CardService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping(value = "/healthIDCard", headers = "Authorization")
public class GenerateHealthIDCardController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private HealthID_CardService healthID_CardService;

	@CrossOrigin
	@ApiOperation(value = "Generate OTP for ABHA card", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/generateOTP" }, method = { RequestMethod.POST })
	public String mapHealthIDToBeneficiary(
			@ApiParam(value = "{\"authMethod\":\"String\",\"healthid\":\"String\",\"healthIdNumber\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {
		logger.info("NDHM_FHIR generate OTP for ABHA card API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = healthID_CardService.generateOTP(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate OTP for ABHA card API response " + response.toString());
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "Generate OTP for ABHA card", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/verifyOTPAndGenerateHealthCard" }, method = { RequestMethod.POST })
	public String verifyOTPAndGenerateHealthCard(
			@ApiParam(value = "{\"otp\":\"String\",\"txnId\":\"String\",\"authMethod\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {
		logger.info("NDHM_FHIR verify OTP and generate ABHA card API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = healthID_CardService.verifyOTPAndGenerateCard(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate ABHA card API response " + response.toString());
		return response.toString();
	}

}
