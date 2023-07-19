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
package com.wipro.fhir.data.healthID_validate;

public class HealthIDValidationResponse {
	
  String RequestId;
  String Timestamp;
  AuthNDHM Auth;
public String getRequestId() {
	return RequestId;
}
public void setRequestId(String requestId) {
	RequestId = requestId;
}
public String getTimestamp() {
	return Timestamp;
}
public void setTimestamp(String timestamp) {
	Timestamp = timestamp;
}
public AuthNDHM getAuth() {
	return Auth;
}
public void setAuth(AuthNDHM auth) {
	Auth = auth;
}
}
