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
package com.wipro.fhir.data.patient;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

/***
 * 
 * @author NE298657
 *
 */
@Component
public class PatientDemographicDetails {
	private Long beneficiaryRegID;
	private Long beneficiaryID;
	private String firstName;
	private String lastName;
	private Integer genderID;
	private String email;
	private Timestamp dOB;
	private String fatherName;
	private Integer actualAge;
	private String ageUnits;

	private PatientAddress i_bendemographics;
	private MaritalStatus maritalStatus;
	private ArrayList<PatientPhoneMaps> benPhoneMaps;
	private M_title m_title;
	private M_Gender m_Gender;

	public M_Gender getM_Gender() {
		return m_Gender;
	}

	public void setM_Gender(M_Gender m_Gender) {
		this.m_Gender = m_Gender;
	}

	public M_title getM_title() {
		return m_title;
	}

	public void setM_title(M_title m_title) {
		this.m_title = m_title;
	}

	public Long getBeneficiaryRegID() {
		return beneficiaryRegID;
	}

	public void setBeneficiaryRegID(Long beneficiaryRegID) {
		this.beneficiaryRegID = beneficiaryRegID;
	}

	public Long getBeneficiaryID() {
		return beneficiaryID;
	}

	public void setBeneficiaryID(Long beneficiaryID) {
		this.beneficiaryID = beneficiaryID;
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

	public Integer getGenderID() {
		return genderID;
	}

	public void setGenderID(Integer genderID) {
		this.genderID = genderID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getdOB() {
		return dOB;
	}

	public void setdOB(Timestamp dOB) {
		this.dOB = dOB;
	}

	public PatientAddress getI_bendemographics() {
		return i_bendemographics;
	}

	public void setI_bendemographics(PatientAddress i_bendemographics) {
		this.i_bendemographics = i_bendemographics;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public ArrayList<PatientPhoneMaps> getBenPhoneMaps() {
		return benPhoneMaps;
	}

	public void setBenPhoneMaps(ArrayList<PatientPhoneMaps> benPhoneMaps) {
		this.benPhoneMaps = benPhoneMaps;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public Integer getActualAge() {
		return actualAge;
	}

	public void setActualAge(Integer actualAge) {
		this.actualAge = actualAge;
	}

	public String getAgeUnits() {
		return ageUnits;
	}

	public void setAgeUnits(String ageUnits) {
		this.ageUnits = ageUnits;
	}

	
}
