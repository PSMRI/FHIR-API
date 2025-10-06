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
package com.wipro.fhir.data.healthID;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import com.google.gson.annotations.Expose;


@Data
@Entity
@Table(name = "t_healthid")
public class HealthIDResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	@Column(name = "HID")
	Integer hID;
	@Expose
	@Column(name = "HealthIdNumber")
    String healthIdNumber;
	@Expose
	@Column(name = "Name")
    String name;
	@Expose
	@Column(name = "ProviderServiceMapID")
	private Integer providerServiceMapID;
	@Expose
	@Column(name = "AuthenticationMode")
	private String authenticationMode;
	@Expose
	@Column(name = "Gender")
    String gender;
	@Expose
	@Column(name = "YearOfBirth")
    String yearOfBirth;
	@Expose
	@Column(name = "BeneficiaryRegID")
	private Long beneficiaryRegId;
	@Expose
	@Column(name = "MonthOfBirth")
    String monthOfBirth;
	@Expose
	@Column(name = "DayOfBirth")
    String dayOfBirth;
	@Expose
	@Column(name = "FirstName")
    String firstName;
	@Expose
	@Column(name = "HealthId")
    String healthId;
	@Expose
	@Column(name = "LastName")
	String lastName;
	@Expose
	@Column(name = "MiddleName")
    String middleName;
	@Expose
	@Column(name = "stateCode")
    String stateCode;
	@Expose
	@Column(name = "districtCode")
    String districtCode;
	@Expose
	@Column(name = "stateName")
    String stateName;
	@Expose
	@Column(name = "districtName")
    String districtName;
	@Expose
	@Column(name = "email")
    String email;
	@Transient
    String kycPhoto;
	@Expose
	@Column(name = "mobile")
    String mobile;
	@Expose
	@Column(name = "authMethods")
	String authMethod;

	@Transient
    List<String> authMethods;
	
	@Column(name = "Deleted", insertable = false, updatable = true)
	private Boolean deleted = false;
	@Expose
	@Column(name = "Processed")
	private String processed="N";
	@Column(name = "CreatedBy")
	@Expose
	private String createdBy;
	@Expose
	@Column(name = "CreatedDate", insertable = false, updatable = false)
	private Timestamp createdDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "LastModDate", insertable = false, updatable = false)
	private Timestamp lastModDate;
	@Column(name = "isNewAbha")
	private Boolean isNewAbha;
	
	@Expose
	@Column(name = "TxnID")
	String txnId;	
}
