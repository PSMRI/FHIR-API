package com.wipro.fhir.service.v3.abha;

import com.wipro.fhir.utils.exception.FHIRException;

public interface CertificateKeyService {

	public String getCertPublicKey(String ndhmAuthToken) throws FHIRException;

}
