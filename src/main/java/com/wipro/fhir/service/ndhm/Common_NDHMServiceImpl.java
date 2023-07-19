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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.NDHMResponse;
import com.wipro.fhir.repo.mongo.ndhm_response.NDHMResponseRepo;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class Common_NDHMServiceImpl implements Common_NDHMService {
	@Autowired
	private NDHMResponseRepo nDHMResponseRepo;

	/***
	 * @author SH20094090
	 * @return headers for the NDHM API's
	 */
	@Override
	public HttpHeaders getHeaders(String ndhmAuthToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (ndhmAuthToken != null)
			headers.set("Authorization", ndhmAuthToken);
		return headers;
	}
	@Override
	public HttpHeaders getHeaders(String ndhmAuthToken,String sbx) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (ndhmAuthToken != null)
			headers.set("Authorization", ndhmAuthToken);
		headers.set("X-CM-ID", sbx);
		return headers;
	}
	
	@Override
	public HttpHeaders getHeadersWithXtoken(String ndhmAuthToken,String X_Token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (ndhmAuthToken != null)
			headers.set("Authorization", ndhmAuthToken);
		headers.set("X-Token", X_Token);
		return headers;
	}

	/***
	 * @author SH20094090
	 * @return RequestIDAndTimeStamp for the NDHM API's
	 */
	@Override
	public NDHMRequest getRequestIDAndTimeStamp() {
		NDHMRequest obj = new NDHMRequest();

		// get ISO time stamp
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		obj.setTimestamp(nowAsISO);

		// generate UUID
		UUID uuid = UUID.randomUUID();
		String requestId = uuid.toString();
		obj.setRequestId(requestId);

		return obj;
	}

	/***
	 * @author SH20094090
	 * @param requestID
	 * @return
	 * @throws FHIRException
	 * 
	 *                       hitting the MongoDB 10 Times and resting for 3 seconds
	 *                       in every hit.
	 */
	@Override
	public String getMongoNDHMResponse(String requestID) throws FHIRException {
		String response = null;
		NDHMResponse nDHMResponse = new NDHMResponse();
		try {

			int count = 0;
			while (count < 10) {
				nDHMResponse = getResponseMongo(requestID);
				if (nDHMResponse != null)
					break;
				count++;
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			throw new FHIRException("failure " + e);
		}

		if (nDHMResponse != null)
			response = nDHMResponse.getResponseData();
		else
			response = "failure";

		return response;

	}

	/***
	 * @author SH20094090
	 * @param reqID
	 * @return
	 * 
	 * 		hitting MongoDB
	 */
	NDHMResponse getResponseMongo(String reqID) {

		NDHMResponse res = nDHMResponseRepo.findByRequestID(reqID);
		if (res != null) {
			return res;
		} else
			return null;
	}

	@Override
	public String getBody(ResponseEntity<String> res) throws FHIRException {
		if (res != null)
			return res.getBody();
		else
			throw new FHIRException("NDHM_FHIR Null response returned from API");
	}

	@Override
	public String getStatusCode(ResponseEntity<String> res) throws FHIRException {
		if (res != null)
			return res.getStatusCode().toString();
		else
			throw new FHIRException("NDHM_FHIR Null response returned from API");
	}
}
