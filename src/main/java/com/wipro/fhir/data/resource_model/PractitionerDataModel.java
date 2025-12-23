package com.wipro.fhir.data.resource_model;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.wipro.fhir.utils.exception.FHIRException;

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

	public PractitionerDataModel(Object[] objArr) throws FHIRException {

		try {

			this.benVisitID = objArr[0] != null ? Integer.parseInt(objArr[0].toString()) : null;
			this.userID = objArr[1] != null ? Integer.parseInt(objArr[1].toString()) : null;

			this.fullName = objArr[2] != null ? objArr[2].toString() : null;

			this.dob = objArr[3] != null ? (objArr[3] instanceof Date ? (Date) objArr[3] : null) : null;

			this.employeeID = objArr[4] != null ? objArr[4].toString() : null;
			this.contactNo = objArr[5] != null ? objArr[5].toString() : null;
			this.emailID = objArr[6] != null ? objArr[6].toString() : null;
			this.qualificationName = objArr[7] != null ? objArr[7].toString() : null;
			this.designationName = objArr[8] != null ? objArr[8].toString() : null;
			this.genderName = objArr[9] != null ? objArr[9].toString() : null;

			this.genderID = objArr[10] != null ? Integer.parseInt(objArr[10].toString()) : null;
			this.serviceProviderID = objArr[11] != null ? Integer.parseInt(objArr[11].toString()) : null;

			this.visitCode = objArr[12] != null ? Long.parseLong(objArr[12].toString()) : null;

			this.createdBy = objArr[13] != null ? objArr[13].toString() : null;

			this.createdDate = objArr[14] != null ? (objArr[14] instanceof Timestamp ? (Timestamp) objArr[14] : null)
					: null;
		} catch (Exception e) {
			throw new FHIRException("Practitioner resource failed with error - " + e.getMessage());
		}

	}
	

	public PractitionerDataModel getPractitioner(Object[] resultSet) throws FHIRException {
		if (resultSet == null || resultSet.length == 0) {
			return null;
		}
		return new PractitionerDataModel(resultSet);
	}

}
