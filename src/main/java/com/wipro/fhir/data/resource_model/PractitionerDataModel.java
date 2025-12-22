package com.wipro.fhir.data.resource_model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class PractitionerDataModel {
	
	private static final long serialVersionUID = 1L;

	private Integer benVisitID;
	private Integer userID;
	private String fullName;
	private Date dob;

	private String employeeID;
	private String contactNo;
	private String emailID;

	private String qualificationName;
	private String designationName;

	private String genderName;
	private Integer genderID;

	private Integer serviceProviderID;
	private Long visitCode;

	private String createdBy;
	private Timestamp createdDate;

	public PractitionerDataModel() {
	}

	public PractitionerDataModel(Object[] objArr) {

		this.benVisitID = objArr[0] != null ? ((Number) objArr[0]).intValue() : null;
		this.userID = objArr[1] != null ? ((Number) objArr[1]).intValue() : null;
		this.fullName = (String) objArr[2];
		this.dob = (Date) objArr[3];
		this.employeeID = (String) objArr[4];
		this.contactNo = (String) objArr[5];
		this.emailID = (String) objArr[6];
		this.qualificationName = (String) objArr[7];
		this.designationName = (String) objArr[8];
		this.genderName = (String) objArr[9];
		this.genderID = objArr[10] != null ? ((Number) objArr[10]).intValue() : null;
		this.serviceProviderID = objArr[11] != null ? ((Number) objArr[11]).intValue() : null;
		this.visitCode = objArr[12] != null ? ((Number) objArr[12]).longValue() : null;
		this.createdBy = (String) objArr[13];
		this.createdDate = (Timestamp) objArr[14];
	}
	

	public PractitionerDataModel getPractitioner(Object[] resultSet) {
		if (resultSet == null || resultSet.length == 0) {
			return null;
		}
		return new PractitionerDataModel(resultSet);
	}

}
