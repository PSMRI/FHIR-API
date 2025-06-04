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

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class MedicationRequestDataModel {

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

	private String drugForm;
	private String genericDrugName;
	private String snomedCTCodeDrug;
	private String snomedCTTermDrug;

	private String drugStrength;
	private String drugDose;

	private String drugRoute;
	private String snomedCTCodeROA;
	private String snomedCTTermROA;

	private String drugFrequency;

	private String duration;
	private String durationUnit;

	private String instructions;

	private String additionalInstructions;
	private String snomedCTCodeAI;
	private String snomedCTTermAI;

	private Integer qtyPrescribed;

	private Timestamp createdDate;
	private String createdBy;

	public MedicationRequestDataModel() {
	}

	public MedicationRequestDataModel(Object[] objArr) {
	    this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
	    this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
	    this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
	    this.providerServiceMapID = objArr[3] != null ? (Integer) objArr[3] : null;
	    this.vanID = objArr[4] != null ? (Integer) objArr[4] : null;

	    this.drugForm = objArr[5] != null ? (String) objArr[5] : null;
	    this.genericDrugName = objArr[6] != null ? (String) objArr[6] : null;
	    this.drugStrength = objArr[7] != null ? (String) objArr[7] : null;
	    this.drugDose = objArr[8] != null ? (String) objArr[8] : null;
	    this.drugRoute = objArr[9] != null ? (String) objArr[9] : null;
	    this.drugFrequency = objArr[10] != null ? (String) objArr[10] : null;

	    this.duration = objArr[11] != null ? (String) objArr[11] : null;
	    this.durationUnit = objArr[12] != null ? (String) objArr[12] : null;

	    this.instructions = objArr[13] != null ? (String) objArr[13] : null;
	    this.qtyPrescribed = objArr[14] != null ? (Integer) objArr[14] : null;

	    this.snomedCTCodeDrug = objArr[15] != null ? (String) objArr[15] : null;
	    this.snomedCTTermDrug = objArr[16] != null ? (String) objArr[16] : null;
	    this.createdDate = objArr[17] != null ? (Timestamp) objArr[17] : null;
	    this.createdBy = objArr[18] != null ? (String) objArr[18] : null;

	}

	public List<MedicationRequestDataModel> getMedicationRequestList(List<Object[]> resultSetList) {
		MedicationRequestDataModel medicationOBJ;
		List<MedicationRequestDataModel> medicationList = new ArrayList<MedicationRequestDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				medicationOBJ = new MedicationRequestDataModel(objArr);
				medicationList.add(medicationOBJ);
			}
		}
		return medicationList;
	}

}
