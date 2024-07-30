package com.wipro.fhir.service.ndhm;

import com.wipro.fhir.utils.exception.FHIRException;

public interface SearchHealthIdNDHMService {

	String searchHealthId(String healthId) throws FHIRException;

	String searchWithMobileNumber(String mobileSearchDTO) throws FHIRException;

}
