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

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.Authorize;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

/***
 * 
 * @author SH20094090
 *
 */

@Service
@PropertySource("classpath:application.properties")
public class GenerateSession_NDHMServiceImpl implements GenerateSession_NDHMService{
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());


	private static String NDHM_AUTH_TOKEN;
	private static Long NDHM_TOKEN_EXP;

	@Value("${clientID}")
	private String clientID;

	@Value("${clientSecret}")
	private String clientSecret;

	@Value("${ndhmuserAuthenticate}")
	private String ndhmUserAuthenticate;

	@Autowired
	private HttpUtils httpUtils;
	@Autowired
	private Common_NDHMService common_NDHMService;
	@Override
	public String generateNDHMAuthToken() throws FHIRException {

		Authorize obj = new Authorize();
		String res = null;
		try {
			obj.setClientId(clientID);
			obj.setClientSecret(clientSecret);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM_FHIR-session authenticate API request Obj " + requestOBJ);
			HttpHeaders headers = common_NDHMService.getHeaders(null);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(ndhmUserAuthenticate, requestOBJ, headers);
			String responseStrLogin=common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				NDHM_AUTH_TOKEN = "Bearer" + " " + jsnOBJ.get("accessToken").getAsString();
				Integer expiry = jsnOBJ.get("expiresIn").getAsInt();
				double time = expiry / 60;
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				Calendar ndhmCalendar = Calendar.getInstance();
				ndhmCalendar.setTime(sqlDate);
				ndhmCalendar.add(Calendar.MINUTE, (int) time);
				Date ndhmTokenEndTime = ndhmCalendar.getTime();
				NDHM_TOKEN_EXP = ndhmTokenEndTime.getTime();
				res = "success";
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing authenticate API");
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing authenticate API " + e);
		}
		return res;
	}
	@Override
	public String getNDHMAuthToken() throws FHIRException
	{
		try {
			if (NDHM_AUTH_TOKEN == null || NDHM_TOKEN_EXP == null
					|| NDHM_TOKEN_EXP < System.currentTimeMillis()) {
				String authenticateMsg = generateNDHMAuthToken();

				if (authenticateMsg.equalsIgnoreCase("success"))
					logger.info("NDHM_FHIR NDHM authentication success at : " + System.currentTimeMillis());
				else {
					logger.error("NDHM_FHIR NDHM user authentication failed at : " + System.currentTimeMillis());
					throw new FHIRException("NDHM_FHIR NDHM user authentication failed.");
				}
			}

		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return NDHM_AUTH_TOKEN;
	}

}
