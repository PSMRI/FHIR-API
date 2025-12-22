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
public class MedicalHistoryDataModel implements Serializable {
	
	private static final long serialVersionUID = 1L;


	private BigInteger id;
	private BigInteger beneficiaryRegID;
	private BigInteger visitCode;
	private String currentMedication;
	private Integer currentMedYear;
	private String yearOfIllness;
	private String finalIllnessType;
	private String yearOfSurgery;
	private String finalSurgeryType;
	private Timestamp createdDate;
	private String createdBy;
	public MedicalHistoryDataModel() {
	}
	
	public MedicalHistoryDataModel(Object[] objArr) {
	    this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
	    this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
	    this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
	    this.currentMedication = objArr[3] != null ? (String) objArr[3] : null;
	    this.currentMedYear = objArr[4] != null ? (Integer) objArr[4] : null;
	    this.yearOfIllness = objArr[5] != null ? (String) objArr[5] : null;
	    this.finalIllnessType = objArr[6] != null ? (String) objArr[6] : null;
	    this.yearOfSurgery = objArr[7] != null ? (String) objArr[7] : null;
	    this.finalSurgeryType = objArr[8] != null ? (String) objArr[8] : null;
	    this.createdDate = objArr[9] != null ? (Timestamp) objArr[9] : null;
	    this.createdBy = objArr[10] != null ? (String) objArr[10] : null;
	}

	public List<MedicalHistoryDataModel> getMedicalList(List<Object[]> resultSetList) {
		MedicalHistoryDataModel medHistoryObj;
		List<MedicalHistoryDataModel> medHistoryList = new ArrayList<MedicalHistoryDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				medHistoryObj = new MedicalHistoryDataModel(objArr);
				medHistoryList.add(medHistoryObj);
			}
		}
		return medHistoryList;
	}

}
