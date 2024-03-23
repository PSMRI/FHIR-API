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

import com.wipro.fhir.service.healthID.HealthIDWithBioService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/healthIDWithBio", headers = "Authorization")
public class CreateHealthIDWithBio {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private HealthIDWithBioService healthIDWithBioService;
	
	@CrossOrigin
	@Operation(summary = "Verify Bio")
	@PostMapping(value = { "/verifyBio" })
	public String verifyBio(@Param(value = "{\"Aadhaar\":\"String\", \"pid\":\"String\",\"bioType\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR verify Bio API request" + request);
		try {
			if (request != null) {
				String s = healthIDWithBioService.verifyBio(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");

		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR verify Bio API response" + response.toString());
		return response.toString();
	}
	
	
	@CrossOrigin
	@Operation(summary = "generate Mobile OTP")
	@PostMapping(value = { "/generateMobileOTP" })
	public String checkAndGenerateMobileOTP(
			@Param(value = "{\"mobile\":\"String\", \"txnId\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {
		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR generate Aadhar OTP API request" + request);
		try {
			if (request != null) {
				String s = healthIDWithBioService.generateMobileOTP(request);
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

	@CrossOrigin
	@Operation(summary = "Confirm with Aadhaar Bio")
	@PostMapping(value = { "/confirmWithAadhaarBio" })
	public String confirmWithAadhaarBio(@Param(value = "{\"txnId\":\"String\", \"pid\":\"String\",\"bioType\":\"String\",\"authType\":\"String\"}") @RequestBody String request,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		logger.info("NDHM_FHIR Confirm Aadhaar Bio API request" + request);
		try {
			if (request != null) {
				String s = healthIDWithBioService.confirmWithAadhaarBio(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");

		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR Confirm Aadhaar Bio API response" + response.toString());
		return response.toString();
	}
}
