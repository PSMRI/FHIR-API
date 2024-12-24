package com.wipro.fhir.data.v3.abhaCard;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class OtpRequest {
	
	private String timestamp;
    private String txnId;
    private String otpValue;
    private String mobile;

}
