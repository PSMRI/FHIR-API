package com.wipro.fhir.data.v3.careContext;

import lombok.Data;

@Data
public class CareContextLinkTokenRequest {
	
	private String abhaNumber;
	private String abhaAddress;
	private String name;
	private String gender;
	private Integer yearOfBirth;
	private String abdmFacilityId;

}
