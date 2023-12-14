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
public class DiagnosticReportDataModel {

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

	public DiagnosticReportDataModel() {
	}

	public DiagnosticReportDataModel(Object[] objArr) {
		this.id = ((BigInteger) objArr[0]).longValue();
		this.beneficiaryRegID = ((BigInteger) objArr[1]).longValue();
		this.visitCode = ((BigInteger) objArr[2]).longValue();
		this.providerServiceMapID = (Integer) objArr[3];
		this.vanID = (Integer) objArr[4];
		this.procedureID = (Integer) objArr[5];
		this.componentID = (Integer) objArr[6];
		this.procedureName = (String) objArr[7];
		this.testComponentName = (String) objArr[8];
		this.testResultValue = (String) objArr[9];
		this.rangeMin = (BigDecimal) objArr[10];
		this.rangeMax = (BigDecimal) objArr[11];
		this.loincCode = (String) objArr[12];
		this.loincValue = (String) objArr[13];
		this.createdDate = (Timestamp) objArr[14];
		this.createdBy = (String) objArr[15];
		this.remarks = (String) objArr[16];
		this.testResultUnit = (String) objArr[17];

	}

	public List<DiagnosticReportDataModel> getDiagnosticReportList(List<Object[]> resultSetList) {
		DiagnosticReportDataModel diagnosticOBJ;
		List<DiagnosticReportDataModel> diagnosticList = new ArrayList<DiagnosticReportDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				diagnosticOBJ = new DiagnosticReportDataModel(objArr);
				diagnosticList.add(diagnosticOBJ);
			}
		}
		return diagnosticList;
	}

}
