package com.wipro.fhir.service.v3.abha;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.Authorize;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class GenerateAuthSessionServiceImpl implements GenerateAuthSessionService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static String ABHA_AUTH_TOKEN;
	private static Long ABHA_TOKEN_EXP;
	
	@Autowired
	private Common_NDHMService common_NDHMService;

	@Value("${clientID}")
	private String clientID;

	@Value("${clientSecret}")
	private String clientSecret;
	
	@Value("${x-CM-ID}")
	private String xCMId;
	
	@Value("${abdmV3UserAuthenticate}")
	private String abdmV3UserAuthenticate;
	
	@Override
	public String generateAbhaAuthToken() throws FHIRException {
		
		RestTemplate restTemplate = new RestTemplate();
		Authorize obj = new Authorize();
		String res = null;
		
		try {
			obj.setClientId(clientID);
			obj.setClientSecret(clientSecret);
			obj.setGrantType("client_credentials");
			String requestOBJ = new Gson().toJson(obj);
			
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("X-CM-ID", xCMId);
			
			HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(abdmV3UserAuthenticate, HttpMethod.POST,
					httpEntity, String.class);
			
			String responseStrLogin=common_NDHMService.getBody(responseEntity);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = JsonParser.parseString(responseStrLogin).getAsJsonObject();
				ABHA_AUTH_TOKEN = "Bearer" + " " + jsnOBJ.get("accessToken").getAsString();
				Integer expiry = jsnOBJ.get("expiresIn").getAsInt();
				double time = expiry / 60;
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				Calendar ndhmCalendar = Calendar.getInstance();
				ndhmCalendar.setTime(sqlDate);
				ndhmCalendar.add(Calendar.MINUTE, (int) time);
				Date abhaTokenEndTime = ndhmCalendar.getTime();
				ABHA_TOKEN_EXP = abhaTokenEndTime.getTime();
				res = "success";
			} else
				throw new FHIRException("NDHM_FHIR Error while accessing authenticate API");
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing authenticate API " + e);
		}
		return res;
	}
	@Override
	public String getAbhaAuthToken() throws FHIRException {
		try {
			if (ABHA_AUTH_TOKEN == null || ABHA_TOKEN_EXP == null
					|| ABHA_TOKEN_EXP < System.currentTimeMillis()) {
				String authenticateMsg = generateAbhaAuthToken();

				if (authenticateMsg.equalsIgnoreCase("success"))
					logger.info("NDHM_FHIR NDHM V3 authentication success at : " + System.currentTimeMillis());
				else {
					logger.error("NDHM_FHIR NDHM V3 user authentication failed at : " + System.currentTimeMillis());
					throw new FHIRException("ABHA user authentication failed.");
				}
			}

		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return ABHA_AUTH_TOKEN;
	}

	
}
