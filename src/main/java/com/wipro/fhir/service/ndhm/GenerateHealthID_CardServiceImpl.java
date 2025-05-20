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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.Details;
import com.wipro.fhir.data.healthID.HealthIDException;
import com.wipro.fhir.data.healthID.OTPandHealthID;
import com.wipro.fhir.data.healthID.SendOTPForCard;
import com.wipro.fhir.data.healthID.VerifyOTP;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

/**
 * @author SH20094090 Date 09-11-2021
 */
@Service
public class GenerateHealthID_CardServiceImpl implements GenerateHealthID_CardService {

	@Value("${generateOTP_ForCard}")
	private String generateOTP_ForCard;
	@Value("${verifyOTP_ForCard}")
	private String verifyOTP_ForCard;
	@Value("${verifyOTP_ForCard_Aadhaar}")
	private String verifyOTP_ForCard_Aadhaar;
	@Value("${generateHealthCard}")
	private String generateHealthCard;
	@Value("${generateHealthIDCard}")
	private String generateHealthIDCard;
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	HttpUtils httpUtils;
	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;
	@Autowired
	private Common_NDHMService common_NDHMService;

	@Override
	public String generateOTP(String request) throws FHIRException {
		String res = null;
		try {

			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			Map<String, String> responseMap = new HashMap<String, String>();
			ObjectMapper objectMapper = new ObjectMapper();
			SendOTPForCard obj = objectMapper.convertValue(request, SendOTPForCard.class);

			Map<String, Object> requestMap = null;
			requestMap = new HashMap<String, Object>();
			requestMap.put("authMethod", obj.getAuthMethod());

			if (obj.getHealthid() != null)
				requestMap.put("healthid", obj.getHealthid());
			else {
				if (obj.getHealthIdNumber() != null)
					requestMap.put("healthid", obj.getHealthIdNumber());
				else
					throw new FHIRException("Please pass ABHA/ABHA Number");
			}

			String requestOBJ = new Gson().toJson(requestMap);


			logger.info("NDHM_FHIR generate OTP for card API request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);

			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(generateOTP_ForCard, requestOBJ,
					headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				responseMap.put("txnId", txnId);
				res = new Gson().toJson(responseMap);
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing generate OTP for card API");
		}
		/**
		 * @author SH20094090
		 * @purpose To get response body in case of exception
		 */
		catch (HttpClientErrorException e) {
			String message = null;
			if (e.getResponseBodyAsString() != null) {
				HealthIDException exception = InputMapper.gson().fromJson(e.getResponseBodyAsString(),
						HealthIDException.class);
				if (exception.getDetails() != null && exception.getDetails().length > 0) {
					Details details[] = exception.getDetails();
					if (details[0] != null && details[0].getAttribute() != null
							&& details[0].getAttribute().getKey() != null) {
						message = details[0].getMessage() + " :" + details[0].getAttribute().getKey();
					} else if (details[0] != null)
						message = details[0].getMessage();
				} else if (exception.getMessage() != null)
					message = exception.getMessage();
			}
			if (message != null)
				throw new FHIRException("NDHM_FHIR Error while generating OTP for ABHA card " + message);
			else
				throw new FHIRException("NDHM_FHIR Error while generating OTP for ABHA card : " + e.getMessage());

		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing generate OTP API " + e);
		}
		return res;
	}

	@Override
	public String validateOTP(String req) throws FHIRException {
		VerifyOTP obj = new VerifyOTP();
		String NDHM_OTP_TOKEN = null;
		String urlToValidateOTP = null;
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			OTPandHealthID requestOBJ = InputMapper.gson().fromJson(req, OTPandHealthID.class);
			obj.setOtp(requestOBJ.getOtp());
			obj.setTxnId(requestOBJ.getTxnId());
			String request = new Gson().toJson(obj);
			logger.info("NDHM_FHIR-verify OTP API For ABHA card request Obj " + request);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);

			if (requestOBJ != null && requestOBJ.getAuthMethod() != null) {
				if (requestOBJ.getAuthMethod().equalsIgnoreCase("MOBILE_OTP"))
					urlToValidateOTP = verifyOTP_ForCard;
				else if (requestOBJ.getAuthMethod().equalsIgnoreCase("AADHAAR_OTP"))
					urlToValidateOTP = verifyOTP_ForCard_Aadhaar;
				else
					throw new FHIRException(
							"Currently this facility is not available. We can generate ABHA Card for ABHA generated using Aadhaar or Mobile OTP");
			} else
				throw new FHIRException("Please pass correct Authentication method : MOBILE_OTP / AADHAAR_OTP");

			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(urlToValidateOTP, request,
					headers);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				if (jsnOBJ.get("token").getAsString() != null)
					NDHM_OTP_TOKEN = "Bearer " + jsnOBJ.get("token").getAsString();
				else
					throw new FHIRException("NDHM_FHIR Error while accessing verify OTP API for ABHA card");
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing verify OTP API for ABHA card");
		}
		/**
		 * @author SH20094090
		 * @purpose To get response body in case of exception
		 */
		catch (HttpClientErrorException e) {
			String message = null;
			if (e.getResponseBodyAsString() != null) {
				HealthIDException exception = InputMapper.gson().fromJson(e.getResponseBodyAsString(),
						HealthIDException.class);
				if (exception.getDetails() != null && exception.getDetails().length > 0) {
					Details details[] = exception.getDetails();
					if (details[0] != null && details[0].getAttribute() != null
							&& details[0].getAttribute().getKey() != null) {
						message = details[0].getMessage() + " :" + details[0].getAttribute().getKey();
					} else if (details[0] != null)
						message = details[0].getMessage();
				} else if (exception.getMessage() != null)
					message = exception.getMessage();
			}
			if (message != null)
				throw new FHIRException("NDHM_FHIR Error while verifying OTP for ABHA card : " + message);
			else
				throw new FHIRException("NDHM_FHIR Error while verifying OTP for ABHA card : " + e.getMessage());

		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing verify OTP API for ABHA card " + e);
		}
		return NDHM_OTP_TOKEN;
	}

	@Override
	public String generateCard(String req, String NDHM_X_TOKEN) throws FHIRException {
		
		String res = null;
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			HttpHeaders headers = common_NDHMService.getHeadersWithXtoken(ndhmAuthToken, NDHM_X_TOKEN);
			ResponseEntity<byte[]> responseEntity = httpUtils.getWithResponseEntityByte(generateHealthCard, headers);
			// String responseStrLogin=common_NDHMService.getBody(responseEntity);
			byte[] ar = responseEntity.getBody();
			if (ar != null) {
				res = Base64.getEncoder().encodeToString(ar);

			} else
				throw new FHIRException("NDHM_FHIR Error while accessing generate card API");
		}
		/**
		 * @author SH20094090
		 * @purpose To get response body in case of exception
		 */
		catch (HttpClientErrorException e) {
			String message = null;
			if (e.getResponseBodyAsString() != null) {
				HealthIDException exception = InputMapper.gson().fromJson(e.getResponseBodyAsString(),
						HealthIDException.class);
				if (exception.getDetails() != null && exception.getDetails().length > 0) {
					Details details[] = exception.getDetails();
					if (details[0] != null && details[0].getAttribute() != null
							&& details[0].getAttribute().getKey() != null) {
						message = details[0].getMessage() + " :" + details[0].getAttribute().getKey();
					} else if (details[0] != null)
						message = details[0].getMessage();
				} else if (exception.getMessage() != null)
					message = exception.getMessage();
			}
			if (message != null)
				throw new FHIRException("NDHM_FHIR Error while generating ABHA card : " + message);
			else
				throw new FHIRException("NDHM_FHIR Error while generating ABHA card : " + e.getMessage());

		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA card API " + e);
		}
		if (res != null)
			return res;
		else
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA card API ");

	}
	
	@Override
	public String generateHealthCardForBio(String req, String NDHM_X_TOKEN) throws FHIRException {
		String res = null;
		try {
			String ndhmAuthToken = this.generateSession_NDHM.getNDHMAuthToken();
			HttpHeaders headers = this.common_NDHMService.getHeadersWithAadhaarBioXtoken(ndhmAuthToken, NDHM_X_TOKEN);
			ResponseEntity<byte[]> responseEntity = this.httpUtils.getWithResponseEntityByte(generateHealthIDCard, headers);
			byte[] ar = responseEntity.getBody();
			if (ar != null) {
				res = Base64.getEncoder().encodeToString(ar);
			} else {
				throw new FHIRException("NDHM_FHIR Error while accessing generate card API");
			} 
		} catch (HttpClientErrorException e) {
			String message = null;
			if (e.getResponseBodyAsString() != null) {
				HealthIDException exception = (HealthIDException)InputMapper.gson().fromJson(e.getResponseBodyAsString(), HealthIDException.class);
				if (exception.getDetails() != null && (exception.getDetails()).length > 0) {
					Details[] details = exception.getDetails();
					if (details[0] != null && details[0].getAttribute() != null && details[0]
						.getAttribute().getKey() != null) {
						message = details[0].getMessage() + " :" + details[0].getAttribute().getKey();
					} else if (details[0] != null) {
						message = details[0].getMessage();
					} 
				} else if (exception.getMessage() != null) {
					message = exception.getMessage();
				} 
			} 
			if (message != null)
				throw new FHIRException("NDHM_FHIR Error while generating ABHA card : " + message); 
			throw new FHIRException("NDHM_FHIR Error while generating ABHA card : " + e.getMessage());
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA card API " + e);
		} 
		if (res != null)
			return res; 
		throw new FHIRException("NDHM_FHIR Error while accessing ABHA card API ");
	}

}
