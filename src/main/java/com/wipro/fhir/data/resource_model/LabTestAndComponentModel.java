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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class LabTestAndComponentModel {

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
	private Integer procedureID;
	private Integer componentID;
	private String procedureName;
	private String testComponentName;
	private String testResultValue;
	private BigDecimal rangeMin;
	private BigDecimal rangeMax;
	private String loincCode;
	private String loincValue;
	private Timestamp createdDate;
	private String createdBy;
	private String remarks;
	private String snomedCTCode = "snomedCode";
	private String snomedCTTerm = "snomedTerm";
	private String testResultUnit;

	public LabTestAndComponentModel() {
	}

	public LabTestAndComponentModel(Object[] objArr) {
	    this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
	    this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
	    this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
	    this.providerServiceMapID = objArr[3] != null ? (Integer) objArr[3] : null;
	    this.vanID = objArr[4] != null ? (Integer) objArr[4] : null;
	    this.procedureID = objArr[5] != null ? (Integer) objArr[5] : null;
	    this.componentID = objArr[6] != null ? (Integer) objArr[6] : null;
	    this.procedureName = objArr[7] != null ? (String) objArr[7] : null;
	    this.testComponentName = objArr[8] != null ? (String) objArr[8] : null;
	    this.testResultValue = objArr[9] != null ? (String) objArr[9] : null;
	    this.rangeMin = objArr[10] != null ? (BigDecimal) objArr[10] : null;
	    this.rangeMax = objArr[11] != null ? (BigDecimal) objArr[11] : null;
	    this.loincCode = objArr[12] != null ? (String) objArr[12] : null;
	    this.loincValue = objArr[13] != null ? (String) objArr[13] : null;
	    this.createdDate = objArr[14] != null ? (Timestamp) objArr[14] : null;
	    this.createdBy = objArr[15] != null ? (String) objArr[15] : null;
	    this.remarks = objArr[16] != null ? (String) objArr[16] : null;
	    this.testResultUnit = objArr[17] != null ? (String) objArr[17] : null;

	}

	public List<LabTestAndComponentModel> getlabTestAndComponentList(List<Object[]> resultSetList) {
		LabTestAndComponentModel labTestAndComponentModel;
		List<LabTestAndComponentModel> labTestAndComponentModelList = new ArrayList<LabTestAndComponentModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				labTestAndComponentModel = new LabTestAndComponentModel(objArr);
				labTestAndComponentModelList.add(labTestAndComponentModel);
			}
		}
		return labTestAndComponentModelList;
	}

}
