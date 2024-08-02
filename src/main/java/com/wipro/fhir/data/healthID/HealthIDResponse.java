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

import com.google.gson.annotations.Expose;


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
    
	
	@Expose
	@Column(name = "TxnID")
	String txnId;
	
	private boolean isABHAAlreadyExist;
	
	public boolean isABHAAlreadyExist() {
		return isABHAAlreadyExist;
	}
	public void setABHAAlreadyExist(boolean isABHAAlreadyExist) {
		this.isABHAAlreadyExist = isABHAAlreadyExist;
	}
	
	
    public String getHealthId() {
		return healthId;
	}
	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}
	public String getHealthIdNumber() {
		return healthIdNumber;
	}
	public void setHealthIdNumber(String healthIdNumber) {
		this.healthIdNumber = healthIdNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getMonthOfBirth() {
		return monthOfBirth;
	}
	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}
	public String getDayOfBirth() {
		return dayOfBirth;
	}
	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getKycPhoto() {
		return kycPhoto;
	}
	public void setKycPhoto(String kycPhoto) {
		this.kycPhoto = kycPhoto;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Integer getProviderServiceMapID() {
		return providerServiceMapID;
	}
	public void setProviderServiceMapID(Integer providerServiceMapID) {
		this.providerServiceMapID = providerServiceMapID;
	}
	
	public List<String> getAuthMethods() {
		return authMethods;
	}
	public void setAuthMethods(List<String> authMethods) {
		this.authMethods = authMethods;
	}
	public Integer gethID() {
		return hID;
	}
	public void sethID(Integer hID) {
		this.hID = hID;
	}
	public Long getBeneficiaryRegId() {
		return beneficiaryRegId;
	}
	public void setBeneficiaryRegId(Long beneficiaryRegId) {
		this.beneficiaryRegId = beneficiaryRegId;
	}
	public String getAuthMethod() {
		return authMethod;
	}
	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public String getProcessed() {
		return processed;
	}
	public void setProcessed(String processed) {
		this.processed = processed;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getLastModDate() {
		return lastModDate;
	}
	public void setLastModDate(Timestamp lastModDate) {
		this.lastModDate = lastModDate;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getAuthenticationMode() {
		return authenticationMode;
	}
	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}
	
	
}
