package com.wipro.fhir.service.v3.abha;

import com.wipro.fhir.utils.exception.FHIRException;

public interface CreateAbhaV3Service {

	public String getOtpForEnrollment(String request) throws FHIRException;

	public String enrollmentByAadhaar(String request) throws FHIRException;

	public String verifyAuthByAbdm(String request) throws FHIRException;

	public String getAbhaCardPrinted(String reqObj) throws FHIRException;

	
}
