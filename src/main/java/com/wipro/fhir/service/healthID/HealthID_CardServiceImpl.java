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

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wipro.fhir.service.ndhm.GenerateHealthID_CardService;
import com.wipro.fhir.utils.exception.FHIRException;

/**
 * @author SH20094090 Date 09-11-2021
 */

@Service
public class HealthID_CardServiceImpl implements HealthID_CardService {
	@Autowired
	private GenerateHealthID_CardService generateHealthID_CardServiceImpl;

	@Override
	public String generateOTP(String request) throws FHIRException {
		String OTPres = null;
		try {
			OTPres = generateHealthID_CardServiceImpl.generateOTP(request);
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		if (OTPres != null)
			return OTPres;
		else
			throw new FHIRException("NDHM_FHIR Error while generating OTP");
	}

	@Override
	public String verifyOTPAndGenerateCard(String request) throws FHIRException {
		String res = null;
		try {
			String ndhmXToken = generateHealthID_CardServiceImpl.validateOTP(request);
			HashMap<String, String> map = new HashMap<String, String>();
			if (ndhmXToken != null) {
				String card = generateHealthID_CardServiceImpl.generateCard(request, ndhmXToken);
				if (card != null) {
					map.put("data", card);
					res = new Gson().toJson(map);
				} else
					throw new FHIRException("NDHM_FHIR Error while generating ABHA card");

			} else {
				throw new FHIRException("NDHM_FHIR Error while validating OTP");
			}

		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		if (res != null)
			return res;
		else
			throw new FHIRException("NDHM_FHIR Error while validating OTP");

	}

}
