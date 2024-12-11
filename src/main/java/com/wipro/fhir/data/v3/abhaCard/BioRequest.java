package com.wipro.fhir.data.v3.abhaCard;

import lombok.Data;

@Data
public class BioRequest {
	
	private String timestamp;
    private String txnId;
    private String aadhaar;
    private Object fingerPrintAuthPid;
    private String mobile;

}
