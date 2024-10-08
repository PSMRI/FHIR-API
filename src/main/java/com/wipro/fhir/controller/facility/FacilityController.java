package com.wipro.fhir.controller.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.facility.FacilityService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;


@CrossOrigin
@RestController
@RequestMapping(value = "/facility", headers = "Authorization")
public class FacilityController {
	
	
	@Autowired
	private FacilityService facilityService;
	
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@CrossOrigin
	@Operation(summary = "Get ABDM Registered Facilities")
	@GetMapping(value = { "/getAbdmRegisteredFacilities" })
	public String getAbdmRegisteredFacilities(@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();

		try {

			String resp = facilityService.fetchRegisteredFacilities();

				response.setResponse(resp);

		} catch (FHIRException e) {

			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("Get ABDM Registered facilities API response" + response.toString());
		return response.toString();
	}
	
	
	@CrossOrigin
	@Operation(summary = "Get ABDM Registered Facilities")
	@PostMapping(value = { "/saveAbdmFacilityId" })
	public String saveAbdmFacilityForVisit(@RequestHeader(value = "Authorization") String Authorization,  @RequestBody() String reqObj) {

		OutputResponse response = new OutputResponse();

		try {

			String resp = facilityService.saveAbdmFacilityId(reqObj);

				response.setResponse(resp);
		} catch (FHIRException e) {

			response.setError(5000, e.getMessage());
			logger.error(e.getMessage());
		}
		logger.info("ABDM save Facility ID response" + response.toString());
		return response.toString();
	}

}
