package com.wipro.fhir.controller.v3.abha.creation;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wipro.fhir.service.ndhm.GenerateHealthID_CardServiceImpl;
import com.wipro.fhir.service.v3.abha.creation.CreateAbhaV3Service;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/abhaCreation", headers = "Authorization")
public class CreateAbhaV3Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private CreateAbhaV3Service createAbhaV3Service;
	
	@Autowired 
	private GenerateHealthID_CardServiceImpl generateHealthID_CardServiceImpl;
	
	@CrossOrigin
	@Operation(summary = "Generate OTP for ABHA enrollment")
	@PostMapping(value = { "/requestOtpForAbhaEnrollment" })
	public String requestOtpForEnrollment(@RequestBody String request) {
		logger.info("Generate OTP for ABHA enrollment API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = createAbhaV3Service.getOtpForEnrollment(request);
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
	@Operation(summary = "ABHA enrollment by Aadhaar")
	@PostMapping(value = { "/abhaEnrollmentByAadhaar" })
	public String abhaEnrollmentByAadhaar(@RequestBody String request) {
		logger.info("ABHA enrollment BY Aadhaar API request " + request);
		OutputResponse response = new OutputResponse();
		String res = null;
		try {
			if (request != null) {
				 res = createAbhaV3Service.enrollmentByAadhaar(request);
//				if(abdmToken != null) {
//					HashMap<String, String> map = new HashMap<String, String>();
//						String card = generateHealthID_CardServiceImpl.generateCard(request, abdmToken);
//						if (card != null) {
//							map.put("data", card);
//							res = new Gson().toJson(map);
//						} else
//							throw new FHIRException("NDHM_FHIR Error while generating ABHA card");
//
//					} else {
//						throw new FHIRException("NDHM_FHIR Error while validating OTP");
//					}
				response.setResponse(res);
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
	@Operation(summary = "Verify Mobile OTP for ABHA enrollment")
	@PostMapping(value = { "/verifyAuthByMobile" })
	public String verifyMobileForAuth(@RequestBody String request) {
		logger.info("Generate OTP for ABHA enrollment API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = createAbhaV3Service.verifyAuthByMobile(request);
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
}
