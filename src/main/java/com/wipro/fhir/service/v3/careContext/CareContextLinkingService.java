package com.wipro.fhir.service.v3.careContext;

import com.wipro.fhir.utils.exception.FHIRException;

public interface CareContextLinkingService {

	String generateTokenForCareContext(String request) throws FHIRException;

	String linkCareContext(String request) throws FHIRException;

}
