package com.wipro.fhir.data.resource_model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class OrganizationDataModel {

	private Long benVisitID;
	private Short serviceProviderID;
	private String serviceProviderName;

	private Integer stateID;
	private String stateName;

	private Integer districtID;
	private String districtName;

	private String locationName;
	private String address;

	private Short serviceID;
	private String serviceName;

	private Boolean isNational;

	private String abdmFacilityId;
	private String abdmFacilityName;

	private Integer psAddMapID;
	private Integer providerServiceMapID;

	public OrganizationDataModel() {
	}

	public OrganizationDataModel(Object[] objArr) {

		this.benVisitID = objArr[0] != null ? (Long) objArr[0] : null;
		this.serviceProviderID = objArr[1] != null ? (Short) objArr[1] : null;
		this.serviceProviderName = objArr[2] != null ? (String) objArr[2] : null;

		this.stateID = objArr[3] != null ? (Integer) objArr[3] : null;
		this.stateName = objArr[4] != null ? (String) objArr[4] : null;

		this.districtID = objArr[5] != null ? (Integer) objArr[5] : null;
		this.districtName = objArr[6] != null ? (String) objArr[6] : null;

		this.locationName = objArr[7] != null ? (String) objArr[7] : null;
		this.address = objArr[8] != null ? (String) objArr[8] : null;

		this.serviceID = objArr[9] != null ? (Short) objArr[9] : null;
		this.serviceName = objArr[10] != null ? (String) objArr[10] : null;

		this.isNational = objArr[11] != null ? (Boolean) objArr[11] : null;

		this.abdmFacilityId = objArr[12] != null ? (String) objArr[12] : null;
		this.abdmFacilityName = objArr[13] != null ? (String) objArr[13] : null;

		this.psAddMapID = objArr[14] != null ? (Integer) objArr[14] : null;
		this.providerServiceMapID = objArr[15] != null ? (Integer) objArr[15] : null;
	}


	public OrganizationDataModel getOrganization(Object[] resultSet) {

		if (resultSet == null || resultSet.length == 0) {
			return null;
		}

		return new OrganizationDataModel(resultSet);
	}
}
