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

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

@Document(collection = "PatientCareContexts")
public class PatientCareContexts {

	@Id
	@Expose
	@Field(value = "id")
	private String id;

	@Expose
	@Field(value = "phoneNumber")
	private String phoneNumber;

	@Expose
	@Field(value = "identifier")
	private String identifier;

	@Expose
	@Field(value = "email")
	private String email;

	@Expose
	@Field(value = "name")
	private String name;

	@Expose
	@Field(value = "caseReferenceNumber")
	private String caseReferenceNumber;

	@Expose
	@Field(value = "Gender")
	private String gender;

	@Expose
	@Field(value = "yearOfBirth")
	private String yearOfBirth;

	@Expose
	@Field(value = "HealthNumber")
	private String HealthNumber;

	@Expose
	@Field(value = "HealthId")
	private String HealthId;

	@Expose
	@Field(value = "careContexts")
	private String careContextsList;

	@Expose
	@Field(value = "careContextsListTemp")
	private ArrayList<CareContexts> careContextsListTemp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaseReferenceNumber() {
		return caseReferenceNumber;
	}

	public void setCaseReferenceNumber(String caseReferenceNumber) {
		this.caseReferenceNumber = caseReferenceNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getHealthNumber() {
		return HealthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		HealthNumber = healthNumber;
	}

	public String getHealthId() {
		return HealthId;
	}

	public void setHealthId(String healthId) {
		HealthId = healthId;
	}

	public String getCareContextsList() {
		return careContextsList;
	}

	public void setCareContextsList(String careContextsList) {
		this.careContextsList = careContextsList;
	}

	public ArrayList<CareContexts> getCareContextsListTemp() {
		return careContextsListTemp;
	}

	public void setCareContextsListTemp(ArrayList<CareContexts> careContextsListTemp) {
		this.careContextsListTemp = careContextsListTemp;
	}

}
