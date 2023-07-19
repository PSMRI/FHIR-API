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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wipro.fhir.data.healthID.BenHealthIDMapping;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
public class HealthIDServiceImpl implements HealthIDService {
	@Autowired
	HttpUtils httpUtils;
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	public String mapHealthIDToBeneficiary(String request) throws FHIRException {
		BenHealthIDMapping health = InputMapper.gson().fromJson(request, BenHealthIDMapping.class);
		try {
			if (health.getBeneficiaryRegId() == null && health.getBeneficiaryID() == null)
				throw new FHIRException("Error in mapping request");
			if (health.getBeneficiaryRegId() != null)
				health = benHealthIDMappingRepo.save(health);
			else {
				if (health.getBeneficiaryID() != null) {
					Long check1 = benHealthIDMappingRepo.getBenRegID(health.getBeneficiaryID());
					health.setBeneficiaryRegId(check1);
					health = benHealthIDMappingRepo.save(health);
				}
			}
		} catch (Exception e) {
			throw new FHIRException("Error in saving data");
		}
		return new Gson().toJson(health);
	}

	public String getBenHealthID(Long benRegID) {
		Map<String, Object> resMap = new HashMap<>();

		ArrayList<BenHealthIDMapping> healthDetailsList = benHealthIDMappingRepo.getHealthDetails(benRegID);
		resMap.put("BenHealthDetails", new Gson().toJson(healthDetailsList));

		return resMap.toString();
	}
}