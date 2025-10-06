package com.wipro.fhir.data.v3.abhaCard;

import lombok.Data;

@Data
public class LoginMethod {
	
	private String loginId;
	private String loginMethod;
	private String loginHint;
	private String pId;
	private String txnId;
	private String mobileNumber;
	private String createdBy;
	private int providerServiceMapId;

}
