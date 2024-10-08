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
		this.id = BigInteger.valueOf((long) objArr[0]);
		this.beneficiaryRegID = BigInteger.valueOf((long) objArr[1]);
		this.visitCode = BigInteger.valueOf((long) objArr[2]);
		this.providerServiceMapID = (Integer) objArr[3];
		this.vanID = (Integer) objArr[4];

		if (objArr[5] != null)
			this.nurseFlag = ((Short) objArr[5]).intValue();
		if (objArr[6] != null)
			this.docFlag = ((Short) objArr[6]).intValue();
		if (objArr[7] != null)
			this.specialistFlag = ((Short) objArr[7]).intValue();

		this.createdDate = (Timestamp) objArr[8];
		this.createdBy = (String) objArr[9];

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
