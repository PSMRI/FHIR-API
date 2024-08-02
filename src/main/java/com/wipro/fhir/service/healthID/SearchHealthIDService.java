package com.wipro.fhir.service.healthID;

import com.wipro.fhir.utils.exception.FHIRException;

public interface SearchHealthIDService {

	String searchHealthId(String healthId) throws FHIRException;

	String searchHealthIdWithMobile(String mobileSearchDTO) throws FHIRException;

}
