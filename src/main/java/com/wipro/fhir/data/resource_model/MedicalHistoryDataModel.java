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
	private String visitCode;
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
	
	public MedicalHistoryDataModel(Object[] objArr) throws Exception {
		    try {

		        this.id = objArr[0] != null
		                ? BigInteger.valueOf(Long.parseLong(objArr[0].toString()))
		                : null;

		        this.beneficiaryRegID = objArr[1] != null
		                ? BigInteger.valueOf(Long.parseLong(objArr[1].toString()))
		                : null;

		        // visitCode is STRING only
		        this.visitCode = objArr[2] != null ? objArr[2].toString() : null;

		        this.currentMedication = objArr[3] != null ? objArr[3].toString() : null;

		        this.currentMedYear = objArr[4] != null
		                ? Integer.parseInt(objArr[4].toString())
		                : null;

		        this.yearOfIllness = objArr[5] != null ? objArr[5].toString() : null;
		        this.finalIllnessType = objArr[6] != null ? objArr[6].toString() : null;
		        this.yearOfSurgery = objArr[7] != null ? objArr[7].toString() : null;
		        this.finalSurgeryType = objArr[8] != null ? objArr[8].toString() : null;

		        this.createdDate = objArr[9] instanceof Timestamp ? (Timestamp) objArr[9] : null;
		        this.createdBy = objArr[10] != null ? objArr[10].toString() : null;

		    } catch (Exception e) {
		    	throw new Exception("Medical statement resource model failed with error - " + e.getMessage());
		    }

	}

	public List<MedicalHistoryDataModel> getMedicalList(List<Object[]> resultSetList) throws Exception {
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
