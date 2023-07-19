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
package com.wipro.fhir.controller.carecontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.care_context.CareContextService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@RestController
@RequestMapping(value = "/careContext", headers = "Authorization")
public class CareContextController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private CareContextService careContextService;

	@CrossOrigin
	@ApiOperation(value = "Generate OTP for care context linking", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/generateOTPForCareContext" }, method = { RequestMethod.POST })
	public String generateOTP(
			@ApiParam(value = "{\"healthID\":\"String\",\"authenticationMode\":\"String\",\"healthIdNumber\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR Generate Mobile OTP for care context API request" + request);
		try {
			if (request != null) {
				String s = careContextService.generateOTPForCareContext(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("NDHM_FHIR Generate Mobile OTP for care context API response" + response.toString());
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "Validate OTP and create care context", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/validateOTPAndCreateCareContext" }, method = { RequestMethod.POST })
	public String validateOTPAndCreateCareContext(
			@ApiParam(value = "{\"healthID\":\"String\",\"visitCode\\\":\\\"String\\\",\"beneficiaryID\\\":\\\"String\\\""
					+ ",\"beneficiaryRegID\\\":\\\"String\\\",\"otp\\\":\\\"String\\\""
					+ ",\\\"txnId\\\":\\\"String\\\",\\\"visitcategory\\\":\\\"String\\\",\"healthIdNumber\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR validate OTP and add care context API request" + request);
		try {
			if (request != null) {
				String s = careContextService.validateOTPAndCreateCareContext(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("NDHM_FHIR validate OTP and add care context API response" + response.toString());
		return response.toString();
	}

	@CrossOrigin
	@ApiOperation(value = "Add care context to Mongo", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = { "/addCarecontextToMongo" }, method = { RequestMethod.POST })
	public String saveCareContextToMongo(@ApiParam(value = "{}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		try {

		} catch (Exception e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		return response.toString();
	}

}
