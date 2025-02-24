package com.wipro.fhir.service.v3.abha;

import com.wipro.fhir.utils.exception.FHIRException;

public interface GenerateAuthSessionService {

	String generateAbhaAuthToken() throws FHIRException;

	String getAbhaAuthToken() throws FHIRException;

}
