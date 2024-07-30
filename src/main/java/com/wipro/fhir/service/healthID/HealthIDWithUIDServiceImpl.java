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
package com.wipro.fhir.service.healthID;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.wipro.fhir.data.healthID.HealthIDRequestAadhar;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.CreateHealthID_Aadhaar_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
@PropertySource("classpath:application.properties")
public class HealthIDWithUIDServiceImpl implements HealthIDWithUIDService {

	@Autowired
	private HealthIDRepo healthIDRepo;

	@Autowired
	private CreateHealthID_Aadhaar_NDHMService createHealthID_Aadhaar_NDHMService;

	@Override
	public String generateOTP(String request) throws FHIRException {
		String OTPres = null;
		try {
			OTPres = createHealthID_Aadhaar_NDHMService.generateOTP(request);
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		if (OTPres != null)
			return OTPres;
		else
			throw new FHIRException("NDHM_FHIR Error while generating OTP");
	}
	
	@Override
	public String verifyOTP(String request) throws FHIRException {
		String response = null;
		try {
			if (request != null ) {
			  response = createHealthID_Aadhaar_NDHMService.verifyOTP(request);
		} else
			throw new FHIRException("NDHM_FHIR Error Entered OTP is incorrect");
	} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return response;
	}
	
	@Override
	public String checkAndGenerateOTP(String request) throws FHIRException {
		String response = null;
		try {
			if (request != null ) {
				response = createHealthID_Aadhaar_NDHMService.checkAndGenerateMobileOTP(request);
		} else
			throw new FHIRException("NDHM_FHIR Error Entered OTP is incorrect");
	} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return response;
	}
	
	@Override
	public String verifyMobileOTP(String request) throws FHIRException {
		String response = null;
		try {
			if (request != null ) {
				response = createHealthID_Aadhaar_NDHMService.verifyMobileOTP(request);
		} else
			throw new FHIRException("NDHM_FHIR Error Entered OTP is incorrect");
	} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return response;
	}

	@Override
	public String createHealthIDWithUID(String request) throws FHIRException {
		String res = null;
		try {
			HealthIDRequestAadhar obj = InputMapper.gson().fromJson(request, HealthIDRequestAadhar.class);
			HealthIDResponse health = createHealthID_Aadhaar_NDHMService.createHealthIDWithUID(request);
			if (health != null) {
				ArrayList<HealthIDResponse> healthIDDetails = healthIDRepo.getHealthIDDetails(health.getHealthId());
				HealthIDResponse health1 = null;
				if (CollectionUtils.isEmpty(healthIDDetails)) {
					health1 = saveHealthIDResponse(health, obj);
				} else {
					health1 = health;
					health1.setABHAAlreadyExist(true);
				}
				res = new Gson().toJson(health1);
			} else
				throw new FHIRException("NDHM_FHIR Error while creating ABHA");

		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return res;
	}

	private HealthIDResponse saveHealthIDResponse(HealthIDResponse health, HealthIDRequestAadhar healthOBJ) {
		String temp = "";
		if (health.getAuthMethods() != null && health.getAuthMethods().size() > 0) {
			for (int a = 0; a < health.getAuthMethods().size(); a++) {
				temp = temp + health.getAuthMethods().get(a) + "||";
			}
		}
		if (temp.length() > 2)
			temp = temp.substring(0, temp.length() - 2);
		if (temp.equalsIgnoreCase(""))
			temp = null;
		health.setAuthMethod(temp);
		if (healthOBJ.getCreatedBy() != null)
			health.setCreatedBy(healthOBJ.getCreatedBy());
		if (healthOBJ.getTxnId() != null)
			health.setTxnId(healthOBJ.getTxnId());
		if (healthOBJ.getProviderServiceMapID() != null)
			health.setProviderServiceMapID(healthOBJ.getProviderServiceMapID());
		HealthIDResponse health1 = healthIDRepo.save(health);
		return health1;
	}

}
