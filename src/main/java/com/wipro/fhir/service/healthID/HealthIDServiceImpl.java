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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.BenHealthIDMapping;
import com.wipro.fhir.data.healthID.HealthIDRequestAadhar;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
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
	@Autowired
	HealthIDRepo healthIDRepo;

	@Override
	public String mapHealthIDToBeneficiary(String request) throws FHIRException {
		BenHealthIDMapping health = InputMapper.gson().fromJson(request, BenHealthIDMapping.class);
		String[] beneficiaryIdsList = null;
		try {
			if (health.getBeneficiaryRegID() == null && health.getBeneficiaryID() == null)
				throw new FHIRException("Error in mapping request");
			else {
				if (health.getHealthIdNumber() != null) {
					beneficiaryIdsList = benHealthIDMappingRepo.getBenIdForHealthId(health.getHealthIdNumber());

					if (beneficiaryIdsList != null && beneficiaryIdsList.length > 0) {
						return "HealthId is already linked to other beneficiary ID";
					} else {
						if (health.getBeneficiaryRegID() != null)
							health = benHealthIDMappingRepo.save(health);
						else {
							if (health.getBeneficiaryID() != null) {
								Long benRegId = benHealthIDMappingRepo.getBenRegID(health.getBeneficiaryID());
								health.setBeneficiaryRegID(benRegId);
								health = benHealthIDMappingRepo.save(health);
							}
						}
						// Adding the code to check if the received healthId is present in t_healthId
						// table and add if missing
						Integer healthIdCount = healthIDRepo.getCountOfHealthIdNumber(health.getHealthIdNumber());
						if (healthIdCount < 1) {
							JsonObject jsonRequest = JsonParser.parseString(request).getAsJsonObject();
							JsonObject abhaProfileJson = jsonRequest.getAsJsonObject("ABHAProfile");
							HealthIDResponse healthID = InputMapper.gson().fromJson(abhaProfileJson,
									HealthIDResponse.class);

							healthID.setHealthIdNumber(abhaProfileJson.get("ABHANumber").getAsString());
							JsonArray phrAddressArray = abhaProfileJson.getAsJsonArray("phrAddress");
							StringBuilder abhaAddressBuilder = new StringBuilder();

							for (int i = 0; i < phrAddressArray.size(); i++) {
								abhaAddressBuilder.append(phrAddressArray.get(i).getAsString());
								if (i < phrAddressArray.size() - 1) {
									abhaAddressBuilder.append(", ");
								}
							}
							healthID.setHealthId(abhaAddressBuilder.toString());
							healthID.setName(abhaProfileJson.get("firstName").getAsString() + " "
									+ abhaProfileJson.get("middleName").getAsString() + " "
									+ abhaProfileJson.get("lastName").getAsString());
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
							Date date = simpleDateFormat.parse(abhaProfileJson.get("dob").getAsString());
							SimpleDateFormat year = new SimpleDateFormat("yyyy");
							SimpleDateFormat month = new SimpleDateFormat("MM");
							SimpleDateFormat day = new SimpleDateFormat("dd");
							healthID.setYearOfBirth(year.format(date));
							healthID.setMonthOfBirth(month.format(date));
							healthID.setDayOfBirth(day.format(date));
							healthID.setCreatedBy(jsonRequest.get("createdBy").getAsString());
							healthID.setProviderServiceMapID(jsonRequest.get("providerServiceMapId").getAsInt());
							healthID.setIsNewAbha(jsonRequest.get("isNew").getAsBoolean());
							healthIDRepo.save(healthID);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FHIRException("Error in saving data");
		}
		return new Gson().toJson(health);
	}

	public String getBenHealthID(Long benRegID) {
        List<BenHealthIDMapping> healthDetailsList = benHealthIDMappingRepo.getHealthDetails(benRegID);

        List<String> healthIdNumbers = healthDetailsList.stream()
            .map(BenHealthIDMapping::getHealthIdNumber)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        Map<String, Boolean> abhaMap = new HashMap<>();
        if (!healthIdNumbers.isEmpty()) {
            List<Object[]> abhaResults = benHealthIDMappingRepo.getIsNewAbhaBatch(healthIdNumbers);
            for (Object[] row : abhaResults) {
                String healthIdNumber = (String) row[0];
                Boolean isNewAbha = (Boolean) row[1];
                abhaMap.put(healthIdNumber, isNewAbha);
            }
        }

        for (BenHealthIDMapping healthDetails : healthDetailsList) {
    		Boolean isNew = abhaMap.get(healthDetails.getHealthIdNumber());
    		healthDetails.setNewAbha(Boolean.TRUE.equals(isNew));
		}

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("BenHealthDetails", healthDetailsList);

        return new Gson().toJson(responseMap);
    }
	
	@Override
	public String addRecordToHealthIdTable(String request) throws FHIRException {
		JsonObject jsonRequest = JsonParser.parseString(request).getAsJsonObject();
        JsonObject abhaProfileJson = jsonRequest.getAsJsonObject("ABHAProfile");
        HealthIDResponse healthID = InputMapper.gson().fromJson(abhaProfileJson, HealthIDResponse.class);
		String res = null;
		try {
			Integer healthIdCount = healthIDRepo.getCountOfHealthIdNumber(healthID.getHealthIdNumber());
			if(healthIdCount < 1) {	            
	    		healthID.setHealthIdNumber(abhaProfileJson.get("ABHANumber").getAsString());
	    		JsonArray phrAddressArray = abhaProfileJson.getAsJsonArray("phrAddress");
	    		StringBuilder abhaAddressBuilder = new StringBuilder();

	    		for (int i = 0; i < phrAddressArray.size(); i++) {
	    		    abhaAddressBuilder.append(phrAddressArray.get(i).getAsString());
	    		    if (i < phrAddressArray.size() - 1) {
	    		        abhaAddressBuilder.append(", ");
	    		    }
	    		}
	    		healthID.setHealthId(abhaAddressBuilder.toString());
				healthID.setName(
						abhaProfileJson.get("firstName").getAsString() + " " + abhaProfileJson.get("middleName").getAsString() + " " + abhaProfileJson.get("lastName").getAsString());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = simpleDateFormat.parse(abhaProfileJson.get("dob").getAsString());
				SimpleDateFormat year = new SimpleDateFormat("yyyy");
				SimpleDateFormat month = new SimpleDateFormat("MM");
				SimpleDateFormat day = new SimpleDateFormat("dd");
				healthID.setYearOfBirth(year.format(date));
				healthID.setMonthOfBirth(month.format(date));
				healthID.setDayOfBirth(day.format(date));
				healthID.setCreatedBy(jsonRequest.get("createdBy").getAsString());
				healthID.setProviderServiceMapID(jsonRequest.get("providerServiceMapId").getAsInt());
				healthID.setIsNewAbha(jsonRequest.get("isNew").getAsBoolean());
				healthIDRepo.save(healthID);
				res = "Data Saved Successfully";
			} else {
				res = "Data already exists";
			}		
		} catch (Exception e) {
			throw new FHIRException("Error in saving data");
		}
		return res;
	}
	
	@Override
	public String getMappedBenIdForHealthId(String healthIdNumber) {
		String[] beneficiaryIdsList = benHealthIDMappingRepo.getBenIdForHealthId(healthIdNumber);
		
		if(beneficiaryIdsList.length > 0) {
			String[] benIds = benHealthIDMappingRepo.getBeneficiaryIds(beneficiaryIdsList);
			return Arrays.toString(benIds);
		} 
		return "No Beneficiary Found";
	}
	
}