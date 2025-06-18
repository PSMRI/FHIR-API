package com.wipro.fhir.controller.healthID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.service.healthID.HealthIDService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/healthIDRecord", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class CreateHealthIdRecord {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private HealthIDService healthIDService;
	
	/***
	 * 
	 * @param request
	 * @param Authorization
	 * @return BenRegID of beneficiary after mapping
	 */
	@Operation(summary = "Map ABHA to beneficiary")
	@PostMapping(value = { "/mapHealthIDToBeneficiary" })
	public String mapHealthIDToBeneficiary(
			@RequestBody String request, @RequestHeader(value = "Authorization") String Authorization) {
		logger.info("NDHM_FHIR Map ABHA to beneficiary API request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = healthIDService.mapHealthIDToBeneficiary(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR Map ABHA to beneficiary API response " + response.toString());
		return response.toString();
	}
	@Operation(summary = "Add New health ID record to healthId table")
	@PostMapping(value = { "/addHealthIdRecord" })
	public String addRecordToHealthIdTable(
			@RequestBody String request, @RequestHeader(value = "Authorization") String Authorization) {
		logger.info("NDHM_FHIR API to add the new health record coming from FLW request " + request);
		OutputResponse response = new OutputResponse();
		try {
			if (request != null) {
				String s = healthIDService.addRecordToHealthIdTable(request);
				response.setResponse(s);
			} else
				throw new FHIRException("NDHM_FHIR Empty request object");
		} catch (FHIRException e) {
			response.setError(5000, e.getMessage());
			logger.error(e.toString());
		}
		logger.info("NDHM_FHIR API to add the new health record coming from FLW response " + response.toString());
		return response.toString();
	}


}
