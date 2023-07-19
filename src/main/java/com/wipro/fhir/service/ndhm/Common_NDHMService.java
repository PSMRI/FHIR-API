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

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.utils.exception.FHIRException;

public interface Common_NDHMService {
	public HttpHeaders getHeaders(String ndhmAuthToken);
	public HttpHeaders getHeaders(String ndhmAuthToken,String sbx);
	public NDHMRequest getRequestIDAndTimeStamp();
	public String getMongoNDHMResponse(String requestID) throws FHIRException ;
	public String getBody(ResponseEntity<String> res) throws FHIRException;
	public String getStatusCode(ResponseEntity<String> res) throws FHIRException;
	public HttpHeaders getHeadersWithXtoken(String ndhmAuthToken,String X_Token);
}
