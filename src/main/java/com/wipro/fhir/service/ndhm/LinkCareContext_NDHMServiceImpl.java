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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.mongo.care_context.AddCareContext;
import com.wipro.fhir.data.mongo.care_context.AddCareContextRequest;
import com.wipro.fhir.data.mongo.care_context.CareContextDetials;
import com.wipro.fhir.data.mongo.care_context.CareContextPatient;
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
public class LinkCareContext_NDHMServiceImpl implements LinkCareContext_NDHMService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Value("${abdmFacilityId}")
	private String abdmFacilityId;
	@Value("${abhaMode}")
	private String abhaMode;
	@Value("${clientSecret}")
	private String clientSecret;
	@Value("${generateOTPForCareContext}")
	private String generateOTPForCareContext;
	@Value("${validateOTPForCareContext}")
	private String validateOTPForCareContext;
	@Value("${addCareContext}")
	private String addCareContext;
	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private Common_NDHMService common_NDHMService;
	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;
	private static int ACCEPTED = 202;

	@Override
	public String generateOTPForCareContext(String request) throws FHIRException {
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			GenerateOTPForCareContextAndValidate genOTP = InputMapper.gson().fromJson(request,
					GenerateOTPForCareContextAndValidate.class);
			NDHMRequest obj = common_NDHMService.getRequestIDAndTimeStamp();

			Requester requester = new Requester();
			if(null != genOTP.getAbdmFacilityId() && null != genOTP.getAbdmFacilityName()) {
				requester.setId(genOTP.getAbdmFacilityId());
			} else {
			requester.setId(abdmFacilityId);
			}
			requester.setType("HIP");
			JsonParser jsnParser = new JsonParser();
			Query query = new Query();
			query.setRequester(requester);
			query.setPurpose("KYC_AND_LINK");
			if (genOTP.getAuthenticationMode() != null && genOTP.getAuthenticationMode().equalsIgnoreCase("AADHAR"))
				query.setAuthMode("AADHAAR_OTP");
			else
				query.setAuthMode("MOBILE_OTP");

			if (genOTP.getHealthID() != null)
				query.setId(genOTP.getHealthID());
			else {
				
				if (genOTP.getHealthIdNumber() != null)
					query.setId(genOTP.getHealthIdNumber());
				else
					throw new FHIRException("Please pass ABHA/ABHA Number");
			}

			obj.setQuery(query);

			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR Carecontext generateOTP API request Obj " + requestOBJ);
			if(abhaMode !=null && !(abhaMode.equalsIgnoreCase("abdm") || abhaMode.equalsIgnoreCase("sbx")))
				abhaMode="sbx";
				HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken, abhaMode);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(generateOTPForCareContext,
					requestOBJ, headers);
			String responseStrLogin = common_NDHMService.getStatusCode(responseEntity);
			String numericStatusCodeStr = responseStrLogin.split(" ")[0]; // Extracts "202" from "202 ACCEPTED"
			int numericStatusCode = Integer.parseInt(numericStatusCodeStr);
			String txnID = null;
		//	if (Integer.parseInt(responseStrLogin) == ACCEPTED) {
			if (numericStatusCode == HttpStatus.ACCEPTED.value()) {
				String mongoResponse = common_NDHMService.getMongoNDHMResponse(obj.getRequestId());
				if (!mongoResponse.equalsIgnoreCase("failure")) {
					JsonElement jsnElmnt1 = jsnParser.parse(mongoResponse);
					JsonObject jsnOBJ1 = new JsonObject();
					jsnOBJ1 = jsnElmnt1.getAsJsonObject();
					try {
						if (jsnOBJ1.get("Auth") != null && jsnOBJ1.getAsJsonObject("Auth").get("TransactionId") != null)
							txnID = jsnOBJ1.getAsJsonObject("Auth").get("TransactionId").getAsString();
						else
							throw new FHIRException(
									"NDHM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					} catch (Exception e) {
						throw new FHIRException(
								"NDHM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
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
	public String validateOTPForCareContext(String request) throws FHIRException {
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			String NDHM_OTP_TOKEN = null;
			NDHMRequest obj = common_NDHMService.getRequestIDAndTimeStamp();
			ValidateOTPForCareContextAndValidate validateOTP = InputMapper.gson().fromJson(request,
					ValidateOTPForCareContextAndValidate.class);
			JsonParser jsnParser = new JsonParser();
			obj.setTransactionId(validateOTP.getTxnId());
			Credential cr = new Credential();
			cr.setAuthCode(validateOTP.getOtp());
			obj.setCredential(cr);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR Carecontext validateOTP API request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken, abhaMode);
			;
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(validateOTPForCareContext,
					requestOBJ, headers);
			logger.info("NDHM_FHIR Carecontext validateOTP API Response " + responseEntity);
			String responseStrLogin = common_NDHMService.getStatusCode(responseEntity);
			String numericStatusCodeStr = responseStrLogin.split(" ")[0]; // Extracts "202" from "202 ACCEPTED"
			int numericStatusCode = Integer.parseInt(numericStatusCodeStr);
	//		if (Integer.parseInt(responseStrLogin) == ACCEPTED) {
			if (numericStatusCode == HttpStatus.ACCEPTED.value()) {
				String mongoResponse = common_NDHMService.getMongoNDHMResponse(obj.getRequestId());
				if (!mongoResponse.equalsIgnoreCase("failure")) {
					JsonElement jsnElmnt1 = jsnParser.parse(mongoResponse);
					JsonObject jsnOBJ1 = new JsonObject();
					jsnOBJ1 = jsnElmnt1.getAsJsonObject();
					try {
						if (jsnOBJ1.get("Auth") != null && jsnOBJ1.getAsJsonObject("Auth").get("AccessToken") != null) {
							NDHM_OTP_TOKEN = jsnOBJ1.getAsJsonObject("Auth").get("AccessToken").getAsString();
						} else
							throw new FHIRException(
									"NDHM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					} catch (Exception e) {
						throw new FHIRException(
								"NDHM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					}
				}
			} else
				throw new FHIRException("NDHM_FHIR Error while validating OTP");

			return NDHM_OTP_TOKEN;
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
	}

	@Override
	public String addCareContext(String request, String ndhmOTPToken) throws FHIRException {
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			AddCareContextRequest requestObj = InputMapper.gson().fromJson(request, AddCareContextRequest.class);
			String response = "Failure";
			NDHMRequest obj = common_NDHMService.getRequestIDAndTimeStamp();

			// adding accessToekn.
			AddCareContext link = new AddCareContext();
			link.setAccessToken(ndhmOTPToken);

			// adding patient details.
			CareContextPatient patient = new CareContextPatient();
			
			
			if (requestObj.getHealthID() != null)
			{
				patient.setReferencenumber(requestObj.getHealthID());// replace benregID with ABHA
			    patient.setDisplay("Care Context of " + requestObj.getHealthID());
			}
			else {
				if (requestObj.getHealthIdNumber() != null)
				{
					patient.setReferencenumber(requestObj.getHealthIdNumber());
				patient.setDisplay("Care Context of " + requestObj.getHealthIdNumber());
				}
				else
					throw new FHIRException("Please pass ABHA/ABHA Number");
			}
			
		

			// adding care context details.
			CareContextDetials cCD = new CareContextDetials();
			cCD.setReferenceNumber(requestObj.getVisitCode());
			cCD.setDisplay(requestObj.getVisitCategory());
			ArrayList<CareContextDetials> careContexts = new ArrayList<CareContextDetials>();
			careContexts.add(cCD);

			patient.setCarecontexts(careContexts);
			link.setPatient(patient);
			obj.setLink(link);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR add care context requestOBJ " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken, abhaMode);
			;
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(addCareContext, requestOBJ,
					headers);
			logger.info("NDHM_FHIR add care context response " + responseEntity);
			String responseStrLogin = common_NDHMService.getStatusCode(responseEntity);
			String numericStatusCodeStr = responseStrLogin.split(" ")[0]; // Extracts "202" from "202 ACCEPTED"
			int numericStatusCode = Integer.parseInt(numericStatusCodeStr);
	//		if (Integer.parseInt(responseStrLogin) == ACCEPTED) {
			if (numericStatusCode == HttpStatus.ACCEPTED.value()) {
				// fetching the care context response from MongoDB
				String addCareContextResponse = common_NDHMService.getMongoNDHMResponse(obj.getRequestId());
				logger.info("Mongo add CareContext Response " + addCareContextResponse);
				String successResponse = null;
				if (!addCareContextResponse.equalsIgnoreCase("failure")) {
					JsonObject jsnOBJ = new JsonObject();
					JsonParser jsnParser = new JsonParser();
					JsonElement jsnElmnt = jsnParser.parse(addCareContextResponse);
					jsnOBJ = jsnElmnt.getAsJsonObject();
					try {
						if (jsnOBJ.get("Acknowledgement") != null
								&& jsnOBJ.getAsJsonObject("Acknowledgement").get("Status") != null)
							successResponse = jsnOBJ.getAsJsonObject("Acknowledgement").get("Status").getAsString();
						else
							throw new FHIRException(
									"NDHM_FHIR " + jsnOBJ.getAsJsonObject("Error").get("Message").getAsString());
					} catch (Exception e) {
						throw new FHIRException(
								"NDHM_FHIR " + jsnOBJ.getAsJsonObject("Error").get("Message").getAsString());
					}

					if (successResponse.equalsIgnoreCase("success")) {
						response = "Care Context added successfully";
					}

				} else
					throw new FHIRException("NDHM_FHIR Error while adding care context");
			} else {
				throw new FHIRException("NDHM_FHIR Error while adding care context");
			}
			return response;
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

	}

}
