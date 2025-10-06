package com.wipro.fhir.data.v3.abhaCard;

import lombok.Data;

@Data
public class VerifyProfileUserLogin {

	private String ABHANumber;
	private String txnId;
}
