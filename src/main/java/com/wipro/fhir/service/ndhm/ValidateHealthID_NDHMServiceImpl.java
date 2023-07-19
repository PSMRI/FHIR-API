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
package com.wipro.fhir.service.ndhm;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID_validate.HealthIDValidationResponse;
import com.wipro.fhir.data.mongo.care_context.Credential;
import com.wipro.fhir.data.mongo.care_context.GenerateOTPForCareContextAndValidate;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.Query;
import com.wipro.fhir.data.mongo.care_context.Requester;
import com.wipro.fhir.data.mongo.care_context.ValidateOTPForCareContextAndValidate;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;
@Service
public class ValidateHealthID_NDHMServiceImpl implements ValidateHealthID_NDHMService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Value("${clientID}")
	private String clientID;
	@Value("${clientSecret}")
	private String clientSecret;
	@Value("${generateOTPForCareContext}")
	private String generateOTPHealthIDValidation;
	@Value("${validateOTPForCareContext}")
	private String validateOTPHealthIDValidation;
	@Value("${addCareContext}")
	private String addCareContext;
	@Value("${abhaMode}")
	private String abhaMode;
	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private Common_NDHMService common_NDHMService;
	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;
	private static int ACCEPTED = 202;

	@Override
	public String generateOTPForHealthIDValidation(String request) throws FHIRException {
		try {
			String ndhmAuthToken=generateSession_NDHM.getNDHMAuthToken();
			GenerateOTPForCareContextAndValidate genOTP = InputMapper.gson().fromJson(request, GenerateOTPForCareContextAndValidate.class);//wrong class name
			NDHMRequest obj = common_NDHMService.getRequestIDAndTimeStamp();

			Requester requester = new Requester();
			requester.setId(clientID);
			requester.setType("HIP");
			JsonParser jsnParser = new JsonParser();
			Query query = new Query();
			query.setRequester(requester);
			query.setPurpose("KYC");
			if (genOTP.getAuthenticationMode() != null && genOTP.getAuthenticationMode().equalsIgnoreCase("AADHAR"))
				query.setAuthMode("AADHAAR_OTP");
			else
				query.setAuthMode("MOBILE_OTP");
			
			
		
				query.setId(genOTP.getHealthID());
			
			
			obj.setQuery(query);

			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR generateOTP API request Obj " + requestOBJ);
			if(abhaMode !=null && !(abhaMode.equalsIgnoreCase("abdm") || abhaMode.equalsIgnoreCase("sbx")))
				abhaMode="sbx";
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken,abhaMode);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(generateOTPHealthIDValidation, requestOBJ, headers);
			String responseStrLogin=common_NDHMService.getStatusCode(responseEntity);
			String txnID = null;
			if (Integer.parseInt(responseStrLogin) == ACCEPTED) {
				String mongoResponse = common_NDHMService.getMongoNDHMResponse(obj.getRequestId());
				if (!mongoResponse.equalsIgnoreCase("failure")) {
					JsonElement jsnElmnt1 = jsnParser.parse(mongoResponse);
					JsonObject jsnOBJ1 = new JsonObject();
					jsnOBJ1 = jsnElmnt1.getAsJsonObject();
					try
					{
						if (jsnOBJ1.get("Auth") != null
								&& jsnOBJ1.getAsJsonObject("Auth").get("TransactionId") != null)
							txnID = jsnOBJ1.getAsJsonObject("Auth").get("TransactionId").getAsString();
						else
							throw new FHIRException(jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					}
					catch(Exception e)
					{
						throw new FHIRException(jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					}
				}
			}
			Map<String, String> responseMap = new HashMap<String, String>();
			if (txnID != null)
				responseMap.put("txnId", txnID);
			return new Gson().toJson(responseMap);
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

	}

	@Override
	public String validateOTPForHealthIDValidation(String request) throws FHIRException {
		try {
			String ndhmAuthToken=generateSession_NDHM.getNDHMAuthToken();
			String response = null;
			NDHMRequest obj = common_NDHMService.getRequestIDAndTimeStamp();
			ValidateOTPForCareContextAndValidate validateOTP = InputMapper.gson().fromJson(request,
			ValidateOTPForCareContextAndValidate.class);//make name common
			JsonParser jsnParser = new JsonParser();
			obj.setTransactionId(validateOTP.getTxnId());
			Credential cr = new Credential();
			cr.setAuthCode(validateOTP.getOtp());
			obj.setCredential(cr);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR validateOTP API request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken,abhaMode);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(validateOTPHealthIDValidation, requestOBJ, headers);
			String responseStrLogin=common_NDHMService.getStatusCode(responseEntity);
			HealthIDValidationResponse res = new HealthIDValidationResponse();
			if (Integer.parseInt(responseStrLogin) == ACCEPTED) {
				String mongoResponse = common_NDHMService.getMongoNDHMResponse(obj.getRequestId());
				JsonElement jsnElmnt1 = jsnParser.parse(mongoResponse);
				JsonObject jsnOBJ1 = new JsonObject();
				jsnOBJ1 = jsnElmnt1.getAsJsonObject();
				try
				{
				if (jsnOBJ1.get("Auth") != null && jsnOBJ1.getAsJsonObject("Auth").get("Patient") != null) {
					res = InputMapper.gson().fromJson(mongoResponse, HealthIDValidationResponse.class);
						Map<String, HealthIDValidationResponse> responseMap = new HashMap<String, HealthIDValidationResponse>();
						responseMap.put("data", res);
						response = new Gson().toJson(res);
				}

				else if (jsnOBJ1.get("Error") != null
						&& jsnOBJ1.getAsJsonObject("Error").get("Message") != null)
					throw new FHIRException("NDHM_FHIR "+jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
				} catch(Exception e)
                {
                	throw new FHIRException("NDHM_FHIR "+jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
                }
			} else
				throw new FHIRException("NDHM_FHIR No ABHA found");
			return response;
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

	}

}
