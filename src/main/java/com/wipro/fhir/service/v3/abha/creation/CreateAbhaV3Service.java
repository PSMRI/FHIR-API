package com.wipro.fhir.service.v3.abha.creation;

import com.wipro.fhir.utils.exception.FHIRException;

public interface CreateAbhaV3Service {

	String getOtpForEnrollment(String request) throws FHIRException;

	String enrollmentByAadhaar(String request) throws FHIRException;

	String verifyAuthByMobile(String request) throws FHIRException;

	
}
