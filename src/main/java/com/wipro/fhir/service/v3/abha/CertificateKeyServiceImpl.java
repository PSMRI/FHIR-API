package com.wipro.fhir.service.v3.abha;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class CertificateKeyServiceImpl implements CertificateKeyService {

	@Autowired
	private Common_NDHMService common_NDHMService;

	@Value("${getAuthCertPublicKey}")
	String getAuthCertPublicKey;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public String getCertPublicKey(String ndhmAuthToken) throws FHIRException {

		RestTemplate restTemplate = new RestTemplate();
		String publicKey = null;

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
		headers.add("REQUEST-ID", UUID.randomUUID().toString());

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		headers.add("TIMESTAMP", nowAsISO);
		headers.add("Authorization", ndhmAuthToken);

		HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> certResp = restTemplate.exchange(getAuthCertPublicKey, HttpMethod.GET, httpEntity,
				String.class);
		String responseStrLogin = common_NDHMService.getBody(certResp);
		if (certResp.getStatusCode() == HttpStatusCode.valueOf(200) && certResp.hasBody()) {
			JsonObject jsnOBJ = JsonParser.parseString(responseStrLogin).getAsJsonObject();
			publicKey = jsnOBJ.get("publicKey").getAsString();

			logger.info("publicKeyString : " + publicKey);

			return publicKey;

		}
		return publicKey;
	}

}
