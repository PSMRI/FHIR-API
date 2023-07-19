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
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.Details;
import com.wipro.fhir.data.healthID.HealthIDException;
import com.wipro.fhir.data.healthID.HealthIDRequest;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.healthID.OTPandHealthID;
import com.wipro.fhir.data.healthID.SendOTP;
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
public class CreateHealthID_MobileOTP_NDHMServiceImpl implements CreateHealthID_MobileOTP_NDHMService{

	@Value("${ndhmGenerateOTP}")
	private String ndhmGenerateOTP;
	@Value("${ndhmVerifyOTP}")
	private String ndhmVerifyOTP;
	@Value("${ndhmCreateHealthID}")
	private String ndhmCreateHealthID;
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
			String ndhmAuthToken=generateSession_NDHM.getNDHMAuthToken();
		    Map<String, String> responseMap = new HashMap<String, String>();
			SendOTP obj = InputMapper.gson().fromJson(request, SendOTP.class);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR generate OTP API request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(ndhmGenerateOTP, requestOBJ, headers);
			String responseStrLogin=common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				responseMap.put("mobile", obj.getMobile());
				responseMap.put("txnId", txnId);
				res = new Gson().toJson(responseMap);
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing generate OTP API");
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
			throw new FHIRException("NDHM_FHIR Error while accessing generate OTP API " + e);
		}
		return res;
	}

	@Override
	public String validateOTP(String req) throws FHIRException {
		VerifyOTP obj = new VerifyOTP();
		String NDHM_OTP_TOKEN =null;
		try {
			String ndhmAuthToken=generateSession_NDHM.getNDHMAuthToken();
			OTPandHealthID requestOBJ = InputMapper.gson().fromJson(req, OTPandHealthID.class);
			obj.setOtp(requestOBJ.getOtp());
			obj.setTxnId(requestOBJ.getTxnId());
			String request = new Gson().toJson(obj);
			logger.info("NDHM_FHIR-verify OTP API request Obj " + request);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(ndhmVerifyOTP, request, headers);
			String responseStrLogin=common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				if(jsnOBJ.get("token").getAsString() !=null)
			    NDHM_OTP_TOKEN = jsnOBJ.get("token").getAsString();
				else
					throw new FHIRException("NDHM_FHIR Error while accessing verify OTP API");
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing verify OTP API");
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
			throw new FHIRException("NDHM_FHIR Error while accessing verify OTP API " + e);
		}
		return NDHM_OTP_TOKEN;
	}

	@Override
	public HealthIDResponse createHealthID(String req,String NDHM_OTP_TOKEN) throws FHIRException {
		HealthIDResponse health =new HealthIDResponse();
		try {
			HealthIDRequest requestOBJ=InputMapper.gson().fromJson(req,HealthIDRequest.class);
			String ndhmAuthToken=generateSession_NDHM.getNDHMAuthToken();
			requestOBJ.setToken(NDHM_OTP_TOKEN);
			String request = new Gson().toJson(requestOBJ);
			logger.info("NDHM_FHIR-create ABHA API request Obj " + request);
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(ndhmCreateHealthID, request, headers);
			String responseStrLogin=common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
			  health = InputMapper.gson().fromJson(responseStrLogin, HealthIDResponse.class);
			}
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
			throw new FHIRException("NDHM_FHIR Error while accessing create ABHA API " + e);
		}
		return health;
	}

}
