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
package com.wipro.fhir.service.care_context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.common.CommonServiceImpl;
import com.wipro.fhir.service.healthID.HealthIDServiceImpl;
import com.wipro.fhir.service.ndhm.LinkCareContext_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@Service
public class CareContextServiceImpl implements CareContextService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	HealthIDServiceImpl healthID;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	@Autowired
	private CommonServiceImpl commonServiceImpl;
	
	@Value("${abdmFacilityId}")
	private String abdmFacilityId;

	HttpUtils httpUtils = new HttpUtils();
	@Autowired
	private LinkCareContext_NDHMService linkCareContext_NDHMService;

	@Override
	public String generateOTPForCareContext(String request) throws FHIRException {
		String OTPres = null;
		try {
			OTPres = linkCareContext_NDHMService.generateOTPForCareContext(request);
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		if (OTPres != null)
			return OTPres;
		else
			throw new FHIRException("NDHM_FHIR Error while generating OTP");
	}

	@Override
	public String validateOTPAndCreateCareContext(String request) throws FHIRException {
		String res = null;
		try {
			String ndhmOTPToken = linkCareContext_NDHMService.validateOTPForCareContext(request);
			if (ndhmOTPToken != null) {
				res = linkCareContext_NDHMService.addCareContext(request, ndhmOTPToken);
				if (res != null) {
					Integer a = updateHealthIDWithVisit(request);
					if (a > 0)
						logger.info("NDHM_FHIR Care context linked and ABHA updated successfully");
					else
						logger.info("NDHM_FHIR Error while updating ABHA");
				}
			} else
				throw new FHIRException("NDHM_FHIR Error while validating OTP");
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
	

		if (res != null)
			return res;
		else
			throw new FHIRException("NDHM_FHIR Error while adding care context");
	}

	/*
	 * shubham shekhar updating the ABHA for the Visit Code
	 */
	Integer updateHealthIDWithVisit(String request) {
		JsonObject jsnOBJ = new JsonObject();
		JsonParser jsnParser = new JsonParser();
		JsonElement jsnElmnt = jsnParser.parse(request);
		jsnOBJ = jsnElmnt.getAsJsonObject();
		Integer result = 0;
		

		if ((jsnOBJ.has("healthID") && jsnOBJ.get("healthID") != null && !jsnOBJ.get("healthID").isJsonNull())
				&& (jsnOBJ.has("healthIdNumber") && jsnOBJ.get("healthIdNumber") != null
						&& !jsnOBJ.get("healthIdNumber").isJsonNull())) {

			result = benHealthIDMappingRepo.updateHealthIDAndHealthIDNumberForCareContext(
					jsnOBJ.get("healthID").getAsString(), jsnOBJ.get("healthIdNumber").getAsString(),
					jsnOBJ.get("visitCode").getAsString());
		} else if (jsnOBJ.has("healthID") && jsnOBJ.get("healthID") != null && !jsnOBJ.get("healthID").isJsonNull()) {

			System.out.println("Passing ABHA" + jsnOBJ.get("healthID"));
			result = benHealthIDMappingRepo.updateHealthIDForCareContext(jsnOBJ.get("healthID").getAsString(),
					jsnOBJ.get("visitCode").getAsString());
		} else if (jsnOBJ.has("healthIdNumber") && jsnOBJ.get("healthIdNumber") != null
				&& !jsnOBJ.get("healthIdNumber").isJsonNull()) {

			result = benHealthIDMappingRepo.updateHealthIDNumberForCareContext(
					jsnOBJ.get("healthIdNumber").getAsString(), jsnOBJ.get("visitCode").getAsString());
		} else {
			logger.info("ABHA/ABHA Number is null or invalid");
		}

		return result;
	}

}
