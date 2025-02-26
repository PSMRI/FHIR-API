package com.wipro.fhir.service.v3.abha;

import com.wipro.fhir.utils.exception.FHIRException;

public interface LoginAbhaV3Service {

	String requestOtpForAbhaLogin(String request) throws FHIRException;

	String verifyAbhaLogin(String request) throws FHIRException;

	String verifyProfileLoginUser(String tToken, String txnId, String abhaNumber) throws FHIRException;

	String getWebLoginPhrCard(String reqObj) throws FHIRException;

}
