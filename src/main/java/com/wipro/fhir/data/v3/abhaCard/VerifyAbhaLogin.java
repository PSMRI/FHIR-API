package com.wipro.fhir.data.v3.abhaCard;

import java.util.Map;

import lombok.Data;

@Data
public class VerifyAbhaLogin {
	
	public String[] scope;
	public Map<String, Object> authData;	

}
