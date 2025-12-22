package com.wipro.fhir.data.resource_model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ImmunizationDataModel {

	private BigInteger id;
	private BigInteger beneficiaryRegID;
	private BigInteger visitCode;
	private Integer providerServiceMapID;
	private Integer vanID;
	private String defaultReceivingAge;
	private String vaccineName;
	private Timestamp receivedDate;
	private String receivedFacilityName;
	private String sctcode;
	private String sctTerm;
	private Timestamp createdDate;
	private String createdBy;

	public ImmunizationDataModel() {
	}

	public ImmunizationDataModel(Object[] objArr) {
		this.id = objArr[0] != null ? BigInteger.valueOf(((Number) objArr[0]).longValue()) : null;
		this.beneficiaryRegID = objArr[1] != null ? BigInteger.valueOf(((Number) objArr[1]).longValue()) : null;
		this.visitCode = objArr[2] != null ? BigInteger.valueOf(((Number) objArr[2]).longValue()) : null;
		this.providerServiceMapID = objArr[3] != null ? ((Number) objArr[3]).intValue() : null;
		this.vanID = objArr[4] != null ? ((Number) objArr[4]).intValue() : null;
		this.defaultReceivingAge = objArr[5] != null ? (String) objArr[5] : null;
		this.vaccineName = objArr[6] != null ? (String) objArr[6] : null;
		this.receivedDate = objArr[7] != null ? (Timestamp) objArr[7] : null;
		this.receivedFacilityName = objArr[8] != null ? (String) objArr[8] : null;
		this.sctcode = objArr[9] != null ? (String) objArr[9] : null;
		this.sctTerm = objArr[10] != null ? (String) objArr[10] : null;
		this.createdDate = objArr[11] != null ? (Timestamp) objArr[11] : null;
		this.createdBy = objArr[12] != null ? (String) objArr[12] : null;
	}

	public List<ImmunizationDataModel> getImmunizationList(List<Object[]> resultSetList) {
		List<ImmunizationDataModel> out = new ArrayList<>();
		if (resultSetList != null && !resultSetList.isEmpty()) {
			for (Object[] objArr : resultSetList) {
				out.add(new ImmunizationDataModel(objArr));
			}
		}
		return out;
	}

}
