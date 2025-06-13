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
	private BigInteger id;
	private BigInteger beneficiaryRegID;
	private BigInteger visitCode;
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
	    this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
	    this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
	    this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
	    this.providerServiceMapID = objArr[3] != null ? (Integer) objArr[3] : null;
	    this.vanID = objArr[4] != null ? (Integer) objArr[4] : null;
	    this.allergyStatus = objArr[5] != null ? (String) objArr[5] : null;
	    this.allergyType = objArr[6] != null ? (String) objArr[6] : null;
	    this.sctcode = objArr[7] != null ? (String) objArr[7] : null;
	    this.sctTerm = objArr[8] != null ? (String) objArr[8] : null;
	    this.createdDate = objArr[9] != null ? (Timestamp) objArr[9] : null;
	    this.createdBy = objArr[10] != null ? (String) objArr[10] : null;
	    this.allergicReactionType = objArr[11] != null ? (String) objArr[11] : null;
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
