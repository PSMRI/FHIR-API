package com.wipro.fhir.service.v3.abha;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.v3.abhaCard.LoginAbhaRequest;
import com.wipro.fhir.data.v3.abhaCard.LoginMethod;
import com.wipro.fhir.data.v3.abhaCard.OtpRequest;
import com.wipro.fhir.data.v3.abhaCard.RequestOTPEnrollment;
import com.wipro.fhir.data.v3.abhaCard.VerifyAbhaLogin;
import com.wipro.fhir.data.v3.abhaCard.VerifyProfileUserLogin;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.ndhm.GenerateSession_NDHMService;
import com.wipro.fhir.utils.Encryption;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
public class LoginAbhaV3ServiceImpl implements LoginAbhaV3Service {

	@Autowired
	private GenerateAuthSessionService generateAuthSessionService;
	@Autowired
	private Common_NDHMService common_NDHMService;
	@Autowired
	private Encryption encryption;
	@Autowired
	private CertificateKeyService certificateKeyService;

	@Value("${abhaLoginRequestOtp}")
	String abhaLoginRequestOtp;

	@Value("${webLoginAbhaRequestOtp}")
	String webLoginAbhaRequestOtp;

	@Value("${webLoginAbhaVerify}")
	String webLoginAbhaVerify;

	@Value("${verifyAbhaLogin}")
	String verifyAbhaLoginUrl;

	@Value("${abhaProfileLoginVerifyUser}")
	String abhaProfileLoginVerifyUser;
	
	@Value("${webLoginPhrCard}")
	String abhawebProfileLoginPhrCard;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public String requestOtpForAbhaLogin(String request) throws FHIRException {
		String res = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		String encryptedLoginId = null;
		String publicKeyString = null;
		ResponseEntity<String> responseEntity;

		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", abhaAuthToken);

			RequestOTPEnrollment reqOtpEnrollment = new RequestOTPEnrollment();
			LoginAbhaRequest loginAbhaRequest = InputMapper.gson().fromJson(request, LoginAbhaRequest.class);

			publicKeyString = certificateKeyService.getCertPublicKey(abhaAuthToken);
			if (loginAbhaRequest.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginAbhaRequest.getLoginId(), publicKeyString);
				reqOtpEnrollment.setLoginId(encryptedLoginId);
			}

			if ("AADHAAR".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())
					&& "abha-number".equalsIgnoreCase(loginAbhaRequest.getLoginHint())) {
				reqOtpEnrollment.setScope(new String[] { "abha-login", "aadhaar-verify" });
				reqOtpEnrollment.setLoginHint(loginAbhaRequest.getLoginHint());
				reqOtpEnrollment.setOtpSystem("aadhaar");
			} else if ("mobile".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())
					&& "abha-number".equalsIgnoreCase(loginAbhaRequest.getLoginHint())) {
				reqOtpEnrollment.setScope(new String[] { "abha-login", "mobile-verify" });
				reqOtpEnrollment.setLoginHint(loginAbhaRequest.getLoginHint());
				reqOtpEnrollment.setOtpSystem("abdm");
			} else if ("aadhaar".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())
					&& "abha-address".equalsIgnoreCase(loginAbhaRequest.getLoginHint())) {
				reqOtpEnrollment.setScope(new String[] { "abha-address-login", "aadhaar-verify" });
				reqOtpEnrollment.setLoginHint(loginAbhaRequest.getLoginHint());
				reqOtpEnrollment.setOtpSystem("aadhaar");
			} else if ("mobile".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())
					&& "abha-address".equalsIgnoreCase(loginAbhaRequest.getLoginHint())) {
				reqOtpEnrollment.setScope(new String[] { "abha-address-login", "mobile-verify" });
				reqOtpEnrollment.setLoginHint(loginAbhaRequest.getLoginHint());
				reqOtpEnrollment.setOtpSystem("abdm");
			} else if ("mobile".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())
					&& "mobile".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())) {
				reqOtpEnrollment.setScope(new String[] { "abha-login", "mobile-verify" });
				reqOtpEnrollment.setLoginHint("mobile");
				reqOtpEnrollment.setOtpSystem("abdm");
			} else if ("aadhaar".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())
					&& "aadhaar".equalsIgnoreCase(loginAbhaRequest.getLoginMethod())) {
				reqOtpEnrollment.setScope(new String[] { "abha-login", "aadhaar-verify" });
				reqOtpEnrollment.setLoginHint("aadhaar");
				reqOtpEnrollment.setOtpSystem("aadhaar");
			} else {
				throw new FHIRException("Invalid Login ID and Login Hint, Please Pass Valid ID");
			}

			String requestOBJ = new Gson().toJson(reqOtpEnrollment);
			logger.info("ABDM reqobj for request otp for Abha login: " + requestOBJ);

			HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
			if ("abha-address".equalsIgnoreCase(loginAbhaRequest.getLoginHint())) {
				responseEntity = restTemplate.exchange(webLoginAbhaRequestOtp, HttpMethod.POST, httpEntity,
						String.class);
			} else {
				responseEntity = restTemplate.exchange(abhaLoginRequestOtp, HttpMethod.POST, httpEntity, String.class);
			}

			logger.info("ABDM response for response otp for Abha login: " + responseEntity);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsnOBJ = JsonParser.parseString(responseStrLogin).getAsJsonObject();
				String txnId = jsnOBJ.get("txnId").getAsString();
				String message = jsnOBJ.get("message").getAsString();
				responseMap.put("txnId", txnId);
				responseMap.put("message", message);
				res = new Gson().toJson(responseMap);
			} else {
				throw new FHIRException(responseEntity.getBody());
			}
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

		return res;
	}

	@Override
	public String verifyAbhaLogin(String request) throws FHIRException {
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		String encryptedLoginId = null;
		String publicKeyString = null;
		HealthIDResponse health = new HealthIDResponse();
		ResponseEntity<String> responseEntity;

		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", abhaAuthToken);

			// Create the enrollByAadhar object
			VerifyAbhaLogin verifyAbhaLogin = new VerifyAbhaLogin();
			LoginMethod loginData = InputMapper.gson().fromJson(request, LoginMethod.class);
			publicKeyString = certificateKeyService.getCertPublicKey(abhaAuthToken);
			if (loginData.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginData.getLoginId(), publicKeyString);
			}

			OtpRequest otp = new OtpRequest();

			otp.setTxnId(loginData.getTxnId());
			otp.setOtpValue(encryptedLoginId);

			Map<String, Object> authDataMap = new HashMap<>();
			authDataMap.put("otp", otp);
			authDataMap.put("authMethods", new String[] { "otp" });

			verifyAbhaLogin.setAuthData(authDataMap);

			if ("AADHAAR".equalsIgnoreCase(loginData.getLoginMethod())) {
				verifyAbhaLogin.setScope(new String[] { "abha-login", "aadhaar-verify" });

			} else if ("MOBILE".equalsIgnoreCase(loginData.getLoginMethod())) {
				verifyAbhaLogin.setScope(new String[] { "abha-login", "mobile-verify" });

			} else if ("abha-mobile".equalsIgnoreCase(loginData.getLoginMethod())) {
				verifyAbhaLogin.setScope(new String[] { "abha-address-login", "mobile-verify" });

			} else if ("abha-aadhaar".equalsIgnoreCase(loginData.getLoginMethod())) {
				verifyAbhaLogin.setScope(new String[] { "abha-address-login", "aadhaar-verify" });
			}

			String requestObj = new Gson().toJson(verifyAbhaLogin);
			logger.info("ABDM request for verify abha login: " + requestObj);

			HttpEntity<String> httpEntity = new HttpEntity<>(requestObj, headers);

			if ("abha-aadhaar".equalsIgnoreCase(loginData.getLoginMethod())
					|| "abha-mobile".equalsIgnoreCase(loginData.getLoginMethod())) {
				responseEntity = restTemplate.exchange(webLoginAbhaVerify, HttpMethod.POST, httpEntity, String.class);
			} else {
				responseEntity = restTemplate.exchange(verifyAbhaLoginUrl, HttpMethod.POST, httpEntity, String.class);
			}

			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			logger.info("ABDM response for verify abha login: " + responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsonResponse = JsonParser.parseString(responseStrLogin).getAsJsonObject();

				// Check for success messages
				String authResult = jsonResponse.get("authResult").getAsString();
				if (authResult != null && (authResult.equalsIgnoreCase("success"))) {

					if (jsonResponse.has("accounts")) {
						String abhaNumber = jsonResponse.get("accounts").getAsJsonArray().get(0).getAsJsonObject()
								.get("ABHANumber").getAsString();
						responseMap.put("abhaDetails",
								jsonResponse.get("accounts").getAsJsonArray().get(0).getAsJsonObject().toString());
						responseMap.put("txnId", jsonResponse.get("txnId").getAsString());
						if ("MOBILE".equalsIgnoreCase(loginData.getLoginMethod()) && jsonResponse.has("token")) {
							String xtoken = verifyProfileLoginUser(jsonResponse.get("token").getAsString(),
									jsonResponse.get("txnId").getAsString(), abhaNumber);
							responseMap.put("xToken", xtoken);
						} else if (jsonResponse.has("token")) {
							responseMap.put("xToken", jsonResponse.get("token").getAsString());
						}
					} else if (jsonResponse.has("users")) {
						responseMap.put("abhaDetails",
								jsonResponse.get("users").getAsJsonArray().get(0).getAsJsonObject().toString());
						if (jsonResponse.has("tokens") && jsonResponse.get("tokens").isJsonObject()) {
							JsonObject tokensObject = jsonResponse.get("tokens").getAsJsonObject();
							if (tokensObject.has("token") && !tokensObject.get("token").isJsonNull()) {
								String token = tokensObject.get("token").getAsString();
								responseMap.put("xToken", token);
							}
						}
					}
				} else {
					String message = jsonResponse.get("message").getAsString();
					throw new FHIRException(message);
				}
			} else {
				throw new FHIRException(responseEntity.getBody());
			}
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

		return responseMap.toString();
	}

	@Override
	public String verifyProfileLoginUser(String tToken, String txnId, String abhaNumber) throws FHIRException {

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity;
		String token = null;

		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", abhaAuthToken);
			headers.add("T-token", "Bearer " + tToken);

			VerifyProfileUserLogin verifyUser = new VerifyProfileUserLogin();
			verifyUser.setABHANumber(abhaNumber);
			verifyUser.setTxnId(txnId);

			String requestObj = new Gson().toJson(verifyUser);
			logger.info("ABDM request for verify profile user login: " + requestObj);
			HttpEntity<String> httpEntity = new HttpEntity<>(requestObj, headers);

			responseEntity = restTemplate.exchange(abhaProfileLoginVerifyUser, HttpMethod.POST, httpEntity,
					String.class);

			logger.info("ABDM response for response otp for Abha login: " + responseEntity);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsnOBJ = JsonParser.parseString(responseStrLogin).getAsJsonObject();
				token = jsnOBJ.get("token").getAsString();
			}
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

		return token;
	}
	
	
	@Override
	public String getWebLoginPhrCard(String reqObj) throws FHIRException {

		String res = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();


		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", abhaAuthToken);

			JsonObject stringReqObj = JsonParser.parseString(reqObj).getAsJsonObject();
			if (stringReqObj.has("xToken") && stringReqObj.get("xToken") != null) {
				String xToken = stringReqObj.get("xToken").getAsString();
				headers.add("X-token", "Bearer " + xToken);
			}
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(abhawebProfileLoginPhrCard, HttpMethod.GET, httpEntity,
					String.class);

			logger.info("ABDM response for print Abha card:" + responseEntity);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(202)) {
				responseMap.put("png", responseStrLogin);
				res = new Gson().toJson(responseMap);
			} else {
				throw new FHIRException(responseEntity.getBody());
			}

		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return res;

	}


}
