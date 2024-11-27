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

import lombok.Data;

@Document(collection = "PatientCareContexts")
@Data
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

//	@Expose
//	@Field(value = "careContexts")
//	private String careContextsList;

	@Expose
	@Field(value = "careContextsList")
	private ArrayList<CareContexts> careContextsList;


}
