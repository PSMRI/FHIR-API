package com.wipro.fhir.data.v3.careContext;

import lombok.Data;

@Data
public class AddCareContextRequest {
	
	private long beneficiaryID;
	private String abhaAddress;
	private String abhaNumber;
	private String linkToken;
	private String requestId;
	private String visitCategory;
	private String visitCode;
	private String abdmFacilityId;
	private String abdmFacilityName;
	private String hiType;
	private String display;
	
}
