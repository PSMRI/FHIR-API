package com.wipro.fhir.data.v3.careContext;

import java.util.List;
import lombok.Data;

@Data
public class LinkCareContextRequest {
	
	private String abhaNumber;
	private String abhaAddress;
	private List<PatientCareContext> patient;
	

}
