package com.wipro.fhir.controller.v3.abha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.v3.abha.LoginAbhaV3Service;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/abhaLogin", headers = "Authorization")
public class LoginAbhaV3Controller {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private LoginAbhaV3Service loginAbhaV3Service;

	@CrossOrigin
	@Operation(summary = "Request OTP for Abha LOgin")
	@PostMapping(value = { "/abhaLoginRequestOtp" })
	public String requestOtpForAbhaLogin(@RequestBody String request) {
		logger.info("Generate OTP for ABHA Login API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = loginAbhaV3Service.requestOtpForAbhaLogin(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate OTP for ABHA login API response " + response.toString());
		return response.toString();
	}
	
	@CrossOrigin
	@Operation(summary = "verify OTP for Abha LOgin")
	@PostMapping(value = { "/verifyAbhaLogin" })
	public String verifyAbhaLogin(@RequestBody String request) {
		logger.info("verify OTP for ABHA Login API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = loginAbhaV3Service.verifyAbhaLogin(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR Verify abha login API response " + response.toString());
		return response.toString();
	}

}
