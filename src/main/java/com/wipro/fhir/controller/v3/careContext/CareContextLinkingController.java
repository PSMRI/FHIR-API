package com.wipro.fhir.controller.v3.careContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.v3.careContext.CareContextLinkingService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/careContext", headers = "Authorization")
public class CareContextLinkingController {

	@Autowired
	private CareContextLinkingService careContextLinkingService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Operation(summary = "Generate token for care context linking")
	@PostMapping(value = { "/generateCareContextToken" })
	public String requestOtpForEnrollment(@RequestBody String request) {
		logger.info("Generate token for care context API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = careContextLinkingService.generateTokenForCareContext(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR generate token for care context API response " + response.toString());
		return response.toString();
	}

	@Operation(summary = "link care context")
	@PostMapping(value = { "/linkCareContext" })
	public String add(@RequestBody String request) {
		logger.info("link care context API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = careContextLinkingService.linkCareContext(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("link care context API response " + response.toString());
		return response.toString();
	}
}
