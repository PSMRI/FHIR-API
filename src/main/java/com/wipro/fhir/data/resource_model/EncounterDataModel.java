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
public class EncounterDataModel implements Serializable {

	/**
	 * default value
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;
	private BigInteger beneficiaryRegID;
	private BigInteger visitCode;
	private Integer providerServiceMapID;
	private Integer vanID;
	private Integer nurseFlag;
	private Integer docFlag;
	private Integer specialistFlag;
	private Timestamp createdDate;
	private String createdBy;

	public EncounterDataModel() {
	}

	public EncounterDataModel(Object[] objArr) {
	    this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
	    this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
	    this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
	    this.providerServiceMapID = objArr[3] != null ? (Integer) objArr[3] : null;
	    this.vanID = objArr[4] != null ? (Integer) objArr[4] : null;
	    this.nurseFlag = objArr[5] != null ? ((Number) objArr[5]).intValue() : null;
	    this.docFlag = objArr[6] != null ? ((Number) objArr[6]).intValue() : null;
	    this.specialistFlag = objArr[7] != null ? ((Number) objArr[7]).intValue() : null;
	    this.createdDate = objArr[8] != null ? (Timestamp) objArr[8] : null;
	    this.createdBy = objArr[9] != null ? (String) objArr[9] : null;

	}

	public List<EncounterDataModel> getEncounterList(List<Object[]> resultSetList) {
		EncounterDataModel encounterOBJ;
		List<EncounterDataModel> encounterList = new ArrayList<EncounterDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				encounterOBJ = new EncounterDataModel(objArr);
				encounterList.add(encounterOBJ);
			}
		}
		return encounterList;
	}

}
