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

@Component
@Data
public class FamilyMemberHistoryDataModel implements Serializable {

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
	private String familyMembers;
	private String sctcode;
	private String sctTerm;
	private Timestamp createdDate;
	private String createdBy;

	public FamilyMemberHistoryDataModel() {
	}

	public FamilyMemberHistoryDataModel(Object[] objArr) {
	    this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
	    this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
	    this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
	    this.providerServiceMapID = objArr[3] != null ? (Integer) objArr[3] : null;
	    this.vanID = objArr[4] != null ? (Integer) objArr[4] : null;
	    this.familyMembers = objArr[5] != null ? (String) objArr[5] : null;
	    this.sctcode = objArr[6] != null ? (String) objArr[6] : null;
	    this.sctTerm = objArr[7] != null ? (String) objArr[7] : null;
	    this.createdDate = objArr[8] != null ? (Timestamp) objArr[8] : null;
	    this.createdBy = objArr[9] != null ? (String) objArr[9] : null;
	}

	public List<FamilyMemberHistoryDataModel> getFamilyMemberHistoryList(List<Object[]> resultSetList) {
		FamilyMemberHistoryDataModel familyMemberHistoryOBJ;
		List<FamilyMemberHistoryDataModel> familyMemberHistoryList = new ArrayList<FamilyMemberHistoryDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				familyMemberHistoryOBJ = new FamilyMemberHistoryDataModel(objArr);
				familyMemberHistoryList.add(familyMemberHistoryOBJ);
			}
		}
		return familyMemberHistoryList;
	}

}
