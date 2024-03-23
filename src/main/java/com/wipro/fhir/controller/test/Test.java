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
package com.wipro.fhir.controller.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.atoms.feed.bahmni.ClinicalFeedWorker;
import com.wipro.fhir.service.resource_gateway.OPConsultRecordBundle;
import com.wipro.fhir.service.resource_gateway.OPConsultRecordBundleImpl;
import com.wipro.fhir.service.resource_gateway.PrescriptionRecordBundleImpl;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin
@RestController
@RequestMapping(value = "/feeds", headers = "Authorization")
public class Test {
	@Autowired
	HttpUtils httpUtils;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Autowired
	private OPConsultRecordBundleImpl oPConsultRecordBundleImpl;

	@CrossOrigin
	@Operation(summary = "Test parse ATOM Feeds")
	@PostMapping(value = { "/parse/feed/ATOM" })
	public String parseFeeds(@RequestBody ResourceRequestHandler resourceRequestHandler,
			@RequestHeader(value = "Authorization") String Authorization) {
		OutputResponse response = new OutputResponse();
		String s = null;
		try {
			s = oPConsultRecordBundleImpl.getOPConsultRecordBundle(resourceRequestHandler, null);
			response.setResponse(s);
		} catch (Exception e) {
			logger.error("Unexpected error:" , e);
			System.out.println(e);
		}
		return response.toString();
	}
}
