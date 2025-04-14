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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.healthID.HealthIDWithUIDService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping(value = "/healthIDWithUID", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class CreateHealthIDWithUID {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private HealthIDWithUIDService HealthIDWithUIDService;

	
	@Operation(summary = "Generate OTP")
	@PostMapping(value = { "/generateOTP" })
	public String generateOTP(@Param(value = "{\"mobile\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR generate Aadhar OTP API request" + request);
		try {
			if (request != null) {
				String s = HealthIDWithUIDService.generateOTP(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");

		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate OTP API response" + response.toString());
		return response.toString();
	}

	
	@Operation(summary = "Verify OTP")
	@PostMapping(value = { "/verifyOTP" })
	public String verifyOTP(@Param(value = "{\"OTP\":\"String\", \"txnId\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR generate Aadhar OTP API request" + request);
		try {
			if (request != null) {
				String s = HealthIDWithUIDService.verifyOTP(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");

		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate OTP API response" + response.toString());
		return response.toString();
	}

	
	@Operation(summary = "Check and generate OTP")
	@PostMapping(value = { "/checkAndGenerateMobileOTP" })
	public String checkAndGenerateMobileOTP(
			@Param(value = "{\"mobile\":\"String\", \"txnId\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR generate Aadhar OTP API request" + request);
		try {
			if (request != null) {
				String s = HealthIDWithUIDService.checkAndGenerateOTP(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");

		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate OTP API response" + response.toString());
		return response.toString();
	}

	
	@Operation(summary = "Verify mobile OTP")
	@PostMapping(value = { "/verifyMobileOTP" })
	public String verifyMobileOTP(
			@Param(value = "{\"mobileOTP\":\"String\", \"txnId\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR generate Aadhar OTP API request" + request);
		try {
			if (request != null) {
				String s = HealthIDWithUIDService.verifyMobileOTP(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");

		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate OTP API response" + response.toString());
		return response.toString();
	}

	
	@Operation(summary = "Create ABHA with UID")
	@PostMapping(value = { "/createHealthIDWithUID" })
	public String createHealthIDWithUID(
			@Param(value = "{\"otp\":\"String\",\"txnId\":\"String\",\"address\":\"String\",\"dayOfBirth\":\"String\",\"email\":\"String\",\"profilePhoto\":\"String\",\"password\":\"String\","
					+ "\"healthId\":\"String\",\"healthIdNumber\":\"String\",\"firstName\":\"String\",\"gender\":\"String\",\"lastName\":\"String\",\"middleName\":\"String\",\"monthOfBirth\":\"String\","
					+ "\"name\":\"String\",\"pincode\":\"Integer\",\"yearOfBirth\":\"String\",\"providerServiceMapId\":\"Integer\",\"createdBy\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR validate Aadhar OTP and create ABHA API request" + request);
		try {
			if (request != null) {
				String s = HealthIDWithUIDService.createHealthIDWithUID(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR validate Aadhar OTP and create ABHA API response" + response.toString());
		return response.toString();
	}

}
