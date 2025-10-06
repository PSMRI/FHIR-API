package com.wipro.fhir.data.v3.careContext;

import java.util.List;
import lombok.Data;

@Data
public class PatientCareContext {
	
	private String referenceNumber;
	private String display;
	private List<CareContexts> careContexts;
	private String hiType;
	private int count;
	

}
