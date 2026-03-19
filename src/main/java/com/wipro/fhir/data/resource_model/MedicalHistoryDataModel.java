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
	private BigInteger providerServiceMapID;
	private String currentMedication;
	private Timestamp currentMedYear;
	private String createdBy;
	private Timestamp createdDate;
	public MedicalHistoryDataModel() {
	}
	
	public MedicalHistoryDataModel(Object[] objArr) throws Exception {
		    try {

		        this.id = objArr[0] != null ? BigInteger.valueOf(Long.parseLong(objArr[0].toString())) : null;
		        
		        this.providerServiceMapID = objArr[1] != null ? BigInteger.valueOf(Long.parseLong(objArr[1].toString())) : null;

		        this.currentMedication = objArr[2] != null ? objArr[2].toString() : null;

		        this.currentMedYear = objArr[3] instanceof Timestamp ? (Timestamp) objArr[3] : null;

		        this.createdBy = objArr[4] != null ? objArr[4].toString() : null;

		        this.createdDate = objArr[5] instanceof Timestamp ? (Timestamp) objArr[5] : null;

		    } catch (Exception e) {
		    	throw new Exception("Medical History resource model failed with error - " + e.getMessage());
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
