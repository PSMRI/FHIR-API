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
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.ConfirmAadhaarBio;
import com.wipro.fhir.data.healthID.Details;
import com.wipro.fhir.data.healthID.HealthIDException;
import com.wipro.fhir.data.healthID.HealthIDRequestAadhar;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.healthID.MobileOTP;
import com.wipro.fhir.data.healthID.SendOTP;
import com.wipro.fhir.data.healthID.VerifyBio;
import com.wipro.fhir.data.healthID.VerifyOTP;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

/***
 * 
 * @author SH20094090
 *
 */

@Service
@PropertySource("classpath:application.properties")
public class CreateHealthID_Aadhaar_NDHMServiceImpl implements CreateHealthID_Aadhaar_NDHMService {
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Value("${ndhmGenerateOTPWithAadhaar}")
	private String ndhmGenerateOTPWithAadhaar;

	@Value("${abdmcreateHealthIdWithPreVerified}")
	private String abdmcreateHealthIdWithPreVerified;
	
	@Value("${abdmVerifyOTP}")
	private String abdmVerifyOTP;

	@Value("${abdmCheckAndGenerateMobileOTP}")
	private String abdmCheckAndGenerateMobileOTP;
	
	@Value("${abdmVerifyMobileOTP}")
	private String abdmVerifyMobileOTP;
	
	@Value("${abdmVerifyBio}")
	private String abdmVerifyBio;
	
	@Value("${abdmGenerateMobileOTP}")
	private String abdmGenerateMobileOTP;
	
	@Value("${abdmConfirmAadhaarBio}")
	private String abdmConfirmAadhaarBio;
	
	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private Common_NDHMService common_NDHMService;
	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;
	
	@Autowired
	private GenerateHealthID_CardService generateHealthID_CardService;

	@Override
	public String generateOTP(String request) throws FHIRException {
		String res = null;
		Map<String, String> responseMap = new HashMap<String, String>();
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			SendOTP obj = InputMapper.gson().fromJson(request, SendOTP.class);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR-generate OTP API request Obj for aadhar" + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(ndhmGenerateOTPWithAadhaar,
					requestOBJ, headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				responseMap.put("aadhaar", obj.getAadhaar());
				responseMap.put("txnId", txnId);
				res = new Gson().toJson(responseMap);
			} else
				res = "NDHM_FHIR Error while accessing generate OTP with Aadhaar API";// throw w
		}
		/**
		 * @author SH20094090
		 * @purpose To get response body in case of exception
		 */
		catch(HttpClientErrorException e)
		{
			String message=null;
			if(e.getResponseBodyAsString() !=null)
			{
			HealthIDException exception=InputMapper.gson().fromJson(e.getResponseBodyAsString(), HealthIDException.class);
			if(exception.getDetails() !=null && exception.getDetails().length >0)
			{
				Details details[]=exception.getDetails();
				if(details[0] !=null && details[0].getAttribute() !=null && details[0].getAttribute().getKey() !=null)
				{
					message=details[0].getMessage()+" :"+details[0].getAttribute().getKey();
				}
				else if(details[0] !=null)
					message=details[0].getMessage();
			}
			else if(exception.getMessage() !=null)
				message=exception.getMessage();
			}
			if(message !=null)
				throw new FHIRException("NDHM_FHIR Error while generating OTP for ABHA creation"+message);
			else
				throw new FHIRException("NDHM_FHIR Error while generating OTP for ABHA creation : "+e.getMessage());
			
		}
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing generate OTP with Aadhaar API" + e);
		}
		return res;
	}
	
	@Override
	public String verifyOTP(String request) throws FHIRException {
		String res = null;
		Map<String, String> resMap = new HashMap<String, String>();
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			VerifyOTP obj = InputMapper.gson().fromJson(request, VerifyOTP.class);
			String reqObj = new Gson().toJson(obj);
			logger.info("NDHM_FHIR verfiy ADBM API with verfity OTP " + reqObj);
			
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmVerifyOTP,
					reqObj, headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if(responseStrLogin != null && responseEntity.getStatusCodeValue() == 200) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				resMap.put("txnId", jsnOBJ.get("txnId").getAsString());
				res = new Gson().toJson(resMap);
			} else 
				throw new FHIRException("NDHM_FHIR Error while verifying the OTP");
			
		}
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA API" + e);
		}
		return res;
	}
	
	@Override
	public String checkAndGenerateMobileOTP(String request) throws FHIRException {
		String res = null;
		Map<String, String> resMap = new HashMap<String, String>();
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			MobileOTP obj = InputMapper.gson().fromJson(request, MobileOTP.class);
			String reqObj = new Gson().toJson(obj);
			logger.info("NDHM_FHIR verfiy ADBM API with Check and Generate mobile OTP " + reqObj);
			
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmCheckAndGenerateMobileOTP,
					reqObj, headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if(responseStrLogin != null && responseEntity.getStatusCodeValue() == 200) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				//String mobileLinked = jsnOBJ.get("mobileLinked").getAsString();
				resMap.put("mobileLinked", jsnOBJ.get("mobileLinked").getAsString());
				resMap.put("txnId", jsnOBJ.get("txnId").getAsString());
				res = new Gson().toJson(resMap);
				
			} else 
				throw new FHIRException("NDHM_FHIR Error while checking and generating mobile OTP");
			
		}
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA API" + e);
		}
		return res;
	}
	
	@Override
	public String verifyMobileOTP(String request) throws FHIRException {
		String res = null;
		Map<String, String> resMap = new HashMap<String, String>();
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			VerifyOTP obj = InputMapper.gson().fromJson(request, VerifyOTP.class);
			String reqObj = new Gson().toJson(obj);
			logger.info("NDHM_FHIR verfiy ADBM API with verify Mobile OTP " + reqObj);
			
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmVerifyMobileOTP,
					reqObj, headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if(responseStrLogin != null && responseEntity.getStatusCodeValue() == 200) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				resMap.put("txnId", jsnOBJ.get("txnId").getAsString());
				res = new Gson().toJson(resMap);
			} else 
				throw new FHIRException("NDHM_FHIR Error while verifiying mobile OTP");
			
		}
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA API" + e);
		}
		return res;
	}


	@Override
	public HealthIDResponse createHealthIDWithUID(String request) throws FHIRException {
		HealthIDResponse health = new HealthIDResponse();
		
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			HealthIDRequestAadhar obj = InputMapper.gson().fromJson(request, HealthIDRequestAadhar.class);

			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR create ABHA API with aadhar request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmcreateHealthIdWithPreVerified,
					requestOBJ, headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				health = InputMapper.gson().fromJson(responseStrLogin, HealthIDResponse.class);
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing create ABHA with Aadhaar API");
		}
		/**
		 * @author SH20094090
		 * @purpose To get response body in case of exception
		 */
		catch(HttpClientErrorException e)
		{
			String message=null;
			if(e.getResponseBodyAsString() !=null)
			{
			HealthIDException exception=InputMapper.gson().fromJson(e.getResponseBodyAsString(), HealthIDException.class);
			if(exception.getDetails() !=null && exception.getDetails().length >0)
			{
				Details details[]=exception.getDetails();
				if(details[0] !=null && details[0].getAttribute() !=null && details[0].getAttribute().getKey() !=null)
				{
					message=details[0].getMessage()+" :"+details[0].getAttribute().getKey();
				}
				else if(details[0] !=null)
					message=details[0].getMessage();
			}
			else if(exception.getMessage() !=null)
				message=exception.getMessage();
			}
			if(message !=null)
				throw new FHIRException("NDHM_FHIR Error while creating ABHA "+message);
			else
				throw new FHIRException("NDHM_FHIR Error while creating ABHA : "+e.getMessage());
			
		}
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR-Error while accessing create ABHA with Aadhaar API" + e);
		}
		return health;
	}
	//New api for verify bio
	@Override
	public String verifyBio(String request) throws FHIRException {
		String res = null;
		Map<String, String> responseMap = new HashMap<String, String>();
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			VerifyBio obj = InputMapper.gson().fromJson(request, VerifyBio.class);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR-VerifyBio API request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmVerifyBio,
					requestOBJ, headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				//responseMap.put("aadhaar", obj.getAadhaar());
				responseMap.put("txnId", txnId);
				res = new Gson().toJson(responseMap);
			} else
				res = "NDHM_FHIR Error while verifying Bio";// throw w
		}
		/**
		 * @author SH20094090
		 * @purpose To get response body in case of exception
		 */
			
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing VerifyBio" + e.getMessage());
		}
		return res;
	}
	
   //method for generateMobileOTP
	@Override
	public String generateMobileOTP(String request) throws FHIRException {
		String res = null;
		Map<String, String> resMap = new HashMap<String, String>();
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			MobileOTP obj = InputMapper.gson().fromJson(request, MobileOTP.class);
			String reqObj = new Gson().toJson(obj);
			logger.info("NDHM_FHIR verfiy ADBM API with Generate mobile OTP " + reqObj);

			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmGenerateMobileOTP, reqObj,
					headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null && responseEntity.getStatusCodeValue() == 200) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				// String mobileLinked = jsnOBJ.get("mobileLinked").getAsString();
				// resMap.put("mobileLinked", jsnOBJ.get("mobileLinked").getAsString());
				resMap.put("txnId", jsnOBJ.get("txnId").getAsString());
				res = new Gson().toJson(resMap);

			} else
				throw new FHIRException("NDHM_FHIR Error while checking and generating mobile OTP");

		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA API" + e);
		}
		return res;
	}
	
	//New api for confirm Aadhaar bio
		@Override
		public String confirmWithAadhaarBio(String request) throws FHIRException {
			String res = null;
			Map<String, String> responseMap = new HashMap<String, String>();
			try {
				String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
				ConfirmAadhaarBio obj = InputMapper.gson().fromJson(request, ConfirmAadhaarBio.class);
				String requestOBJ = new Gson().toJson(obj);
				logger.info("NDHM_FHIR-confirm Aadhaar bio API request Obj " + requestOBJ);
				HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
				HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
				ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(abdmConfirmAadhaarBio,
						httpEntity, String.class);
				String responseStrLogin = common_NDHMService.getBody(responseEntity);
				if (responseStrLogin != null) {
					JsonObject jsnOBJ = new JsonObject();
					JsonParser jsnParser = new JsonParser();
					JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
					jsnOBJ = jsnElmnt.getAsJsonObject();
					String X_Token = jsnOBJ.get("x-token").getAsString();
					//responseMap.put("aadhaar", obj.getAadhaar());
					if (null != X_Token)
						res = this.generateHealthID_CardService.generateHealthCardForBio(requestOBJ, X_Token);
				} else {
					res = "NDHM_FHIR Error while confirm Aadhaar bio";// throw w
				}
				 
			}
			/**
			 * @author SH20094090
			 * @purpose To get response body in case of exception
			 */
				
			catch (Exception e) {
				throw new FHIRException("NDHM_FHIR Error while accessing confirm Aadhaar bio" + e.getMessage());
			}
			return res;
		}
}
