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
package com.wipro.fhir.data.resource_model;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
//@Entity
//@Table(name = "t_benallergyhistory")

public class AllergyIntoleranceDataModel implements Serializable {
	/**
	 * default value
	 */
	private static final long serialVersionUID = 1L;

	// @Id
	// @GeneratedValue
	private Long id;
	private Long beneficiaryRegID;
	private Long visitCode;
	private Integer providerServiceMapID;
	private Integer vanID;
	private String allergyStatus;
	private String allergyType;
	private String sctcode;
	private String sctTerm;
	private Timestamp createdDate;
	private String createdBy;
	private String allergicReactionType;

	public AllergyIntoleranceDataModel() {
	}

	public AllergyIntoleranceDataModel(Object[] objArr) {
		this.id = ((BigInteger) objArr[0]).longValue();
		this.beneficiaryRegID = ((BigInteger) objArr[1]).longValue();
		this.visitCode = ((BigInteger) objArr[2]).longValue();
		this.providerServiceMapID = (Integer) objArr[3];
		this.vanID = (Integer) objArr[4];
		this.allergyStatus = (String) objArr[5];
		this.allergyType = (String) objArr[6];
		this.sctcode = (String) objArr[7];
		this.sctTerm = (String) objArr[8];
		this.createdDate = (Timestamp) objArr[9];
		this.createdBy = (String) objArr[10];
		this.allergicReactionType = (String) objArr[11];

	}

	public List<AllergyIntoleranceDataModel> getAllergyList(List<Object[]> resultSetList) {
		AllergyIntoleranceDataModel allergyOBJ;
		List<AllergyIntoleranceDataModel> allergyList = new ArrayList<AllergyIntoleranceDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				allergyOBJ = new AllergyIntoleranceDataModel(objArr);
				allergyList.add(allergyOBJ);
			}
		}
		return allergyList;
	}

}
