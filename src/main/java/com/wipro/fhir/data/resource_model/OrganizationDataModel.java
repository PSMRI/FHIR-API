package com.wipro.fhir.data.resource_model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wipro.fhir.utils.exception.FHIRException;

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

	public OrganizationDataModel(Object[] objArr) throws FHIRException {

		try {

			this.benVisitID = objArr[0] != null ? Long.parseLong(objArr[0].toString()) : null;
			this.serviceProviderID = objArr[1] != null ? Short.parseShort(objArr[1].toString()) : null;
			this.serviceProviderName = objArr[2] != null ? objArr[2].toString() : null;

			this.stateID = objArr[3] != null ? Integer.parseInt(objArr[3].toString()) : null;
			this.stateName = objArr[4] != null ? objArr[4].toString() : null;

			this.districtID = objArr[5] != null ? Integer.parseInt(objArr[5].toString()) : null;
			this.districtName = objArr[6] != null ? objArr[6].toString() : null;

			this.locationName = objArr[7] != null ? objArr[7].toString() : null;
			this.address = objArr[8] != null ? objArr[8].toString() : null;

			this.serviceID = objArr[9] != null ? Short.parseShort(objArr[9].toString()) : null;
			this.serviceName = objArr[10] != null ? objArr[10].toString() : null;

			this.isNational = objArr[11] != null
					? objArr[11].toString().equalsIgnoreCase("true") || objArr[11].toString().equals("1")
							: null;

			this.abdmFacilityId = objArr[12] != null ? objArr[12].toString() : null;
			this.abdmFacilityName = objArr[13] != null ? objArr[13].toString() : null;

			this.psAddMapID = objArr[14] != null ? Integer.parseInt(objArr[14].toString()) : null;
			this.providerServiceMapID = objArr[15] != null ? Integer.parseInt(objArr[15].toString()) : null;

		} catch (Exception e) {
			throw new FHIRException("Organization resource failed with error - " + e.getMessage());
		}

	}


	public OrganizationDataModel getOrganization(Object[] resultSet) throws FHIRException {

		if (resultSet == null || resultSet.length == 0) {
			return null;
		}

		return new OrganizationDataModel(resultSet);
	}
}
