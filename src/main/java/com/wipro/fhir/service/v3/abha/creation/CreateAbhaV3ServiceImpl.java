package com.wipro.fhir.service.v3.abha.creation;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.HealthIDRequestAadhar;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.v3.abhaCard.ConsentRequest;
import com.wipro.fhir.data.v3.abhaCard.EnrollByAadhaar;
import com.wipro.fhir.data.v3.abhaCard.LoginMethod;
import com.wipro.fhir.data.v3.abhaCard.OtpRequest;
import com.wipro.fhir.data.v3.abhaCard.RequestOTPEnrollment;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.ndhm.GenerateSession_NDHMService;
import com.wipro.fhir.utils.Encryption;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Service
public class CreateAbhaV3ServiceImpl implements CreateAbhaV3Service {

	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;
	@Autowired
	private Common_NDHMService common_NDHMService;
	@Autowired
	HttpUtils httpUtils;
	@Autowired
	Encryption encryption;

	@Value("${getAuthCertPublicKey}")
	String getAuthCertPublicKey;

	@Value("${requestOtpForEnrollment}")
	String requestOtpForEnrollment;

	@Value("${abhaEnrollByAadhaar}")
	String abhaEnrollByAadhaar;

	@Value("${abhaMode}")
	String abhaMode;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public String getOtpForEnrollment(String request) throws FHIRException {
		String res = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		String encryptedLoginId = null;
		String publicKeyString = null;

		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", ndhmAuthToken);

			RequestOTPEnrollment reqOtpEnrollment = new RequestOTPEnrollment();
			LoginMethod loginMethod = InputMapper.gson().fromJson(request, LoginMethod.class);

			publicKeyString = getCertPublicKey();
			if (loginMethod.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginMethod.getLoginId(), publicKeyString);
			}

			if ("AADHAAR".equalsIgnoreCase(loginMethod.getLoginMethod())) {
				reqOtpEnrollment.setLoginId(encryptedLoginId);
					reqOtpEnrollment.setOtpSystem("aadhaar");
					reqOtpEnrollment.setLoginHint("aadhaar");
					reqOtpEnrollment.setScope(new String[] { "abha-enrol" });
			}

			String requestOBJ = new Gson().toJson(reqOtpEnrollment);
			logger.info("ABDM reqobj for request otp for enrollment: " + requestOBJ);

			HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestOtpForEnrollment, HttpMethod.POST,
					httpEntity, String.class);

			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsnOBJ = JsonParser.parseString(responseStrLogin).getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				responseMap.put("txnId", txnId);
				res = new Gson().toJson(responseMap);
			} else {
				throw new FHIRException("ABDM Error while accessing request OTP for Enrollment API - status code: "
						+ responseEntity.getStatusCode() + ", error message: " + responseEntity.getBody());
			}
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABDM API: " + e.getMessage(), e);
		}

		return res;
	}

	@Override
	public String enrollmentByAadhaar(String request) throws FHIRException {
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		String encryptedLoginId = null;
		String publicKeyString = null;
		String abdmOtpToken = null;
		HealthIDResponse health = new HealthIDResponse();

		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", ndhmAuthToken);

			// Create the enrollByAadhar object
			EnrollByAadhaar enrollByAadhar = new EnrollByAadhaar();
			LoginMethod loginData = InputMapper.gson().fromJson(request, LoginMethod.class);

			publicKeyString = getCertPublicKey();
			if (loginData.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginData.getLoginId(), publicKeyString);
			}

			if ("AADHAAR".equalsIgnoreCase(loginData.getLoginMethod())) {
				OtpRequest otp = new OtpRequest();

				// Get current timestamp
				OffsetDateTime now = OffsetDateTime.now(java.time.ZoneOffset.UTC);
				DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
				String formattedTimestamp = now.format(formatter);
				otp.setTimestamp(formattedTimestamp);

				otp.setTxnId(loginData.getTnxId());
				otp.setOtpValue(encryptedLoginId);
				otp.setMobile(loginData.getMobileNumber());

				Map<String, Object> authDataMap = new HashMap<>();
				authDataMap.put("otp", otp);
				authDataMap.put("authMethods", new String[] { "otp" });

				enrollByAadhar.setAuthData(authDataMap);

				ConsentRequest consent = new ConsentRequest();
				consent.setCode("abha-enrollment");
				consent.setVersion("1.4");
				enrollByAadhar.setConsent(consent);

				logger.info("ABDM request for enroll by Aadhaar: " + enrollByAadhar);

				String requestObj = new Gson().toJson(enrollByAadhar);
				logger.info("ABDM request object for enrollment: " + requestObj);

				HttpEntity<String> httpEntity = new HttpEntity<>(requestObj, headers);
				ResponseEntity<String> responseEntity = restTemplate.exchange(abhaEnrollByAadhaar, HttpMethod.POST,
						httpEntity, String.class);

				String responseStrLogin = common_NDHMService.getBody(responseEntity);
				if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
					JsonObject jsonResponse = JsonParser.parseString(responseStrLogin).getAsJsonObject();

					// Check for success messages
					String message = jsonResponse.get("message").getAsString();
					if (message != null && (message.equalsIgnoreCase("account created successfully")
							|| message.equalsIgnoreCase("this account already exist"))) {
//						if (jsonResponse.has("tokens")) {
//							JsonObject tokenAsJsonObj = jsonResponse.get("tokens").getAsJsonObject();
//							abdmOtpToken = "Bearer " + tokenAsJsonObj.get("token").getAsString();
//							responseMap.put("abdmToken", abdmOtpToken);
//						} else {
//							throw new FHIRException("NDHM_FHIR Error: Token not found in the response.");
//						}
						if (jsonResponse.has("ABHAProfile")) {
							JsonObject abhaProfileAsJsonObj = jsonResponse.get("ABHAProfile").getAsJsonObject();
							if (abhaProfileAsJsonObj.has("mobile") && abhaProfileAsJsonObj.get("mobile") != null) {
									health = InputMapper.gson().fromJson(abhaProfileAsJsonObj, HealthIDResponse.class);
								responseMap.put("message", "Mobile number linked to ABHA");
							} else {
								
//								getOtpForEnrollment(request, "mobile-verify", jsonResponse.get("txnId").getAsString());
							}
						}
					}
				} else {
					throw new FHIRException("ABDM Error: Status code " + responseEntity.getStatusCode()
							+ ", error message: " + responseEntity.getBody());
				}
			}
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABDM API: " + e.getMessage(), e);
		}

		return abdmOtpToken;
	}
	
	
	@Override
	public String verifyAuthByMobile(String request) throws FHIRException {
		String res = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		String encryptedLoginId = null;
		String publicKeyString = null;

		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", ndhmAuthToken);

			RequestOTPEnrollment reqOtpEnrollment = new RequestOTPEnrollment();
			LoginMethod loginMethod = InputMapper.gson().fromJson(request, LoginMethod.class);

			publicKeyString = getCertPublicKey();
			if (loginMethod.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginMethod.getLoginId(), publicKeyString);
			}

			if ("AADHAAR".equalsIgnoreCase(loginMethod.getLoginMethod())) {
				reqOtpEnrollment.setLoginId(encryptedLoginId);
				reqOtpEnrollment.setTnxId(loginMethod.getTnxId());
					reqOtpEnrollment.setOtpSystem("abdm");
					reqOtpEnrollment.setLoginHint("mobile");
					reqOtpEnrollment.setScope(new String[] { "abha-enrol", "mobile-verify" });
				
			}

			String requestOBJ = new Gson().toJson(reqOtpEnrollment);
			logger.info("ABDM reqobj for request otp for enrollment: " + requestOBJ);

			HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestOtpForEnrollment, HttpMethod.POST,
					httpEntity, String.class);

			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsnOBJ = JsonParser.parseString(responseStrLogin).getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				responseMap.put("txnId", txnId);
				res = new Gson().toJson(responseMap);
			} else {
				throw new FHIRException("ABDM Error while accessing request OTP for Enrollment API - status code: "
						+ responseEntity.getStatusCode() + ", error message: " + responseEntity.getBody());
			}
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABDM API: " + e.getMessage(), e);
		}

		return res;
	}

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
