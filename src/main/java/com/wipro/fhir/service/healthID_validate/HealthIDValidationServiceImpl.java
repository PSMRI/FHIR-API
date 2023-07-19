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
package com.wipro.fhir.service.healthID_validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.service.healthID.HealthIDServiceImpl;
import com.wipro.fhir.service.ndhm.ValidateHealthID_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@Service
public class HealthIDValidationServiceImpl implements HealthIDValidationService {
	@Autowired
	HealthIDServiceImpl healthID;
	@Autowired
	HttpUtils httpUtils;

	@Autowired
	private ValidateHealthID_NDHMService validateHealthID_NDHMService;

	@Override
	public String generateOTPForHealthIDValidation(String request) throws FHIRException {
		String OTPres = null;
		try {
			OTPres = validateHealthID_NDHMService.generateOTPForHealthIDValidation(request);
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		if (OTPres != null)
			return OTPres;
		else
			throw new FHIRException("NDHM_FHIR Error while generating OTP");
	}

	@Override
	public String validateOTPAndHealthID(String request) throws FHIRException {
		String OTPres = null;
		try {
			OTPres = validateHealthID_NDHMService.validateOTPForHealthIDValidation(request);
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		if (OTPres != null)
			return OTPres;
		else
			throw new FHIRException("NDHM_FHIR Error while validating OTP");

	}

}
