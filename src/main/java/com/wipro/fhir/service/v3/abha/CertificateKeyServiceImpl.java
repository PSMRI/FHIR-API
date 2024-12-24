package com.wipro.fhir.service.v3.abha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class CertificateKeyServiceImpl implements CertificateKeyService{
	
	@Value("${getAuthCertPublicKey}")
	String getAuthCertPublicKey;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	@Override
	public String getCertPublicKey() throws FHIRException {

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Void> requestEntity = new HttpEntity<>(null);

		ResponseEntity<String> certResp = restTemplate.exchange(getAuthCertPublicKey, HttpMethod.GET, requestEntity,
				String.class);
		String body = certResp.getBody();

		String publicKeyString = body.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s+", "");

		logger.info("publicKeyString : " + publicKeyString);

		return publicKeyString;

	}

}
