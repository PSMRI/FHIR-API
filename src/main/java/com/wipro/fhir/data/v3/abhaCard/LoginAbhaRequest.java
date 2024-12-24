package com.wipro.fhir.data.v3.abhaCard;

import lombok.Data;

@Data
public class LoginAbhaRequest {
	
	private String loginId;
	private String loginMethod;
	private String loginHint;

}
