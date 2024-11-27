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

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class PatientDemographic {
	private String healthID;
	private String healthIdNo;
	private BigInteger beneficiaryRegID;
	private BigInteger beneficiaryID;

	private String firstName;
	private String middleName;
	private String lastName;
	private String name;

	private Integer genderID;
	private String gender;

	private Integer maritalStatusID;
	private String maritalStatus;

	private String preferredPhoneNo;

	private String country;
	private String state;
	private String district;
	private String village;

	private Timestamp dOB;

	private String email;
	private String fatherName;
	private Integer actualAge;
	private String ageUnits;

	public PatientDemographic(String healthID, String healthIdNo, BigInteger beneficiaryRegID, BigInteger beneficiaryID,
			String name, Integer genderID, String gender, Integer maritalStatusID, String maritalStatus, Timestamp dOB,
			String preferredPhoneNo, String country, String state, String district) {
		super();
		this.healthID = healthID;
		this.healthIdNo = healthIdNo;
		this.beneficiaryRegID = beneficiaryRegID;
		this.beneficiaryID = beneficiaryID;

		this.name = name;
		this.genderID = genderID;
		this.gender = gender;
		this.maritalStatusID = maritalStatusID;
		this.maritalStatus = maritalStatus;

		this.dOB = dOB;
		this.preferredPhoneNo = preferredPhoneNo;

		this.country = country;
		this.state = state;
		this.district = district;

	}

	public PatientDemographic() {
		super();
		
	}

	public PatientDemographic getPatientDemographic(List<Object[]> objList) {
		if (objList != null && objList.size() > 0) {
			Object[] objArr = objList.get(objList.size() - 1);
			PatientDemographic obj = new PatientDemographic((String) objArr[0], (String) objArr[1],
					(BigInteger.valueOf((long) objArr[2])), BigInteger.valueOf((long) objArr[3]), (String) objArr[4],
					(Integer) objArr[5], (String) objArr[6], (Integer) objArr[7], (String) objArr[8],
					(Timestamp) objArr[9], (String) objArr[10], (String) objArr[11], (String) objArr[12],
					(String) objArr[13]);
			return obj;
		} else
			return null;

	}

}
