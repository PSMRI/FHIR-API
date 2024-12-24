package com.wipro.fhir.data.v3.abhaCard;

import lombok.Data;

@Data
public class RequestOTPEnrollment {
	
		String txnId;
		String[] scope;
		String loginHint;
		String loginId;
		String otpSystem;

}
