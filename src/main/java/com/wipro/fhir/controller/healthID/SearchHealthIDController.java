package com.wipro.fhir.controller.healthID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.healthID.SearchHealthIDService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/search", headers = "Authorization")
public class SearchHealthIDController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private SearchHealthIDService searchHealthIDService;
	
	@CrossOrigin
	@Operation(summary = "Search ABHA With Health ID")
	@PostMapping(value = { "/searchByHealthId" })
	public String searchHealthID(@RequestBody String healthId) throws FHIRException {
		OutputResponse response = new OutputResponse();
		try {
			logger.info("Search ABHA With Health Id request {} : ", healthId);
			String resp = searchHealthIDService.searchHealthId(healthId);
			response.setResponse(resp);
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		return response.toString();
	}

	@CrossOrigin
	@Operation(summary = "Search ABHA With Mobile Number")
	@PostMapping(value = { "/searchByMobile" })
	public String searchHealthIDWithMobile(@RequestBody String mobileSearchDTO) throws FHIRException {
		OutputResponse response = new OutputResponse();
		try {
			logger.info("Search ABHA With Mobile request {} : ", mobileSearchDTO);
			String resp = searchHealthIDService.searchHealthIdWithMobile(mobileSearchDTO);
			response.setResponse(resp);
		} catch (Exception e) {
			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		return response.toString();
	}
}
