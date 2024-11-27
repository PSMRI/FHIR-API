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
package com.wipro.fhir.data.mongo.care_context;

public class GenerateOTPForCareContextAndValidate {//give common name

	String authenticationMode;
	String healthID;
	String healthIdNumber;
	String abdmFacilityName;
	String abdmFacilityId;
	public String getAuthenticationMode() {
		return authenticationMode;
	}
	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}
	public String getHealthID() {
		return healthID;
	}
	public void setHealthID(String healthID) {
		this.healthID = healthID;
	}
	public String getHealthIdNumber() {
		return healthIdNumber;
	}
	public void setHealthIdNumber(String healthIdNumber) {
		this.healthIdNumber = healthIdNumber;
     }
	public String getAbdmFacilityName() {
		return abdmFacilityName;
	}
	public void setAbdmFacilityName(String abdmFacilityName) {
		this.abdmFacilityName = abdmFacilityName;
	}
	public String getAbdmFacilityId() {
		return abdmFacilityId;
	}
	public void setAbdmFacilityId(String abdmFacilityId) {
		this.abdmFacilityId = abdmFacilityId;
	}
	
}
