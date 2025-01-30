package com.wipro.fhir.service.v3.abha;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.HealthIDResponse;
import com.wipro.fhir.data.v3.abhaCard.BioRequest;
import com.wipro.fhir.data.v3.abhaCard.ConsentRequest;
import com.wipro.fhir.data.v3.abhaCard.EnrollAuthByABDM;
import com.wipro.fhir.data.v3.abhaCard.EnrollByAadhaar;
import com.wipro.fhir.data.v3.abhaCard.LoginMethod;
import com.wipro.fhir.data.v3.abhaCard.OtpRequest;
import com.wipro.fhir.data.v3.abhaCard.RequestOTPEnrollment;
import com.wipro.fhir.repo.healthID.HealthIDRepo;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.ndhm.GenerateSession_NDHMService;
import com.wipro.fhir.utils.Encryption;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

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
	@Autowired
	HealthIDRepo healthIDRepo;
	@Autowired
	CertificateKeyService certificateKeyService;

	@Value("${requestOtpForEnrollment}")
	String requestOtpForEnrollment;
	
	@Value("${requestAuthByAbdm}")
	String requestAuthByAbdm;

	@Value("${abhaEnrollByAadhaar}")
	String abhaEnrollByAadhaar;

	@Value("${printAbhaCard}")
	String printAbhaCard;

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

			publicKeyString = certificateKeyService.getCertPublicKey(ndhmAuthToken);
			if (loginMethod.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginMethod.getLoginId(), publicKeyString);
			}

			if ("AADHAAR".equalsIgnoreCase(loginMethod.getLoginMethod())) {
				reqOtpEnrollment.setLoginId(encryptedLoginId);
				reqOtpEnrollment.setOtpSystem("aadhaar");
				reqOtpEnrollment.setLoginHint("aadhaar");
				reqOtpEnrollment.setScope(new String[] { "abha-enrol" });
			} else if ("MOBILE".equalsIgnoreCase(loginMethod.getLoginMethod())) {
				reqOtpEnrollment.setLoginId(encryptedLoginId);
				reqOtpEnrollment.setTxnId(loginMethod.getTxnId());
				reqOtpEnrollment.setOtpSystem("abdm");
				reqOtpEnrollment.setLoginHint("mobile");
				reqOtpEnrollment.setScope(new String[] { "abha-enrol", "mobile-verify" });
			}

			String requestOBJ = new Gson().toJson(reqOtpEnrollment);
			logger.info("ABDM reqobj for request otp for enrollment: " + requestOBJ);

			HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestOtpForEnrollment, HttpMethod.POST,
					httpEntity, String.class);
			
			logger.info("ABDM response for request otp for enrollment: " + responseEntity);
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
	public String enrollmentByAadhaar(String request) throws FHIRException {
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();
		String encryptedLoginId = null;
		String publicKeyString = null;
		String requestObj = null;

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
			LoginMethod loginData = InputMapper.gson().fromJson(request, LoginMethod.class);

			publicKeyString = certificateKeyService.getCertPublicKey(ndhmAuthToken);
			if (loginData.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginData.getLoginId(), publicKeyString);
			}

			if ("AADHAAR".equalsIgnoreCase(loginData.getLoginMethod())) {

				requestObj = formAadharEnrollReqObjByAadhar(loginData, encryptedLoginId);
				logger.info("ABDM request object for ABHA enrollment by AADHAAR: " + requestObj);

			} else if ("BIOMETRIC".equalsIgnoreCase(loginData.getLoginMethod())) {

				requestObj = formAadharEnrollReqObjByBiometric(loginData, encryptedLoginId);
				logger.info("ABDM request object for ABHA enrollment by BIOMETRIC: " + requestObj);

			}

			HttpEntity<String> httpEntity = new HttpEntity<>(requestObj, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(abhaEnrollByAadhaar, HttpMethod.POST,
					httpEntity, String.class);
			
			logger.info("ABDM response for ABHA enrollment: " + responseEntity);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsonResponse = JsonParser.parseString(responseStrLogin).getAsJsonObject();

				// Check for success messages
				String message = jsonResponse.get("message").getAsString();
				if (message != null && (message.equalsIgnoreCase("account created successfully")
						|| message.equalsIgnoreCase("this account already exist"))) {

					if (jsonResponse.has("ABHAProfile")) {
						JsonObject abhaProfileAsJsonObj = jsonResponse.get("ABHAProfile").getAsJsonObject();
						Gson gson = new Gson();
						String json = gson.toJson(abhaProfileAsJsonObj);

						HealthIDResponse healthIDResp = gson.fromJson(json, HealthIDResponse.class);
						constructHealthIdResponse(healthIDResp, abhaProfileAsJsonObj);
						healthIDResp.setProviderServiceMapID(loginData.getProviderServiceMapId());
						healthIDResp.setCreatedBy(loginData.getCreatedBy());
						Integer healthIdCount = healthIDRepo.getCountOfHealthIdNumber(healthIDResp.getHealthIdNumber());
						HealthIDResponse save = healthIDResp;
						if (healthIdCount < 1) {
							healthIDRepo.save(healthIDResp);
						}
						responseMap.put("ABHAProfile", gson.toJson(save));
						responseMap.put("txnId", jsonResponse.get("txnId").getAsString());
						responseMap.put("isNew", jsonResponse.get("isNew").getAsString());
						if (jsonResponse.has("tokens") && jsonResponse.get("tokens").isJsonObject()) {
							JsonObject tokensObject = jsonResponse.get("tokens").getAsJsonObject();
							if (tokensObject.has("token") && !tokensObject.get("token").isJsonNull()) {
								String token = tokensObject.get("token").getAsString();
								responseMap.put("xToken", token);
							}
						}
					}
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
	public String verifyAuthByAbdm(String request) throws FHIRException {
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

			LoginMethod loginMethod = InputMapper.gson().fromJson(request, LoginMethod.class);

			publicKeyString = certificateKeyService.getCertPublicKey(ndhmAuthToken);
			if (loginMethod.getLoginId() != null) {
				encryptedLoginId = encryption.encrypt(loginMethod.getLoginId(), publicKeyString);
			}

			EnrollAuthByABDM enrollAuthByabdm = new EnrollAuthByABDM();
			OtpRequest otp = new OtpRequest();

			// Get current timestamp
			OffsetDateTime now = OffsetDateTime.now(java.time.ZoneOffset.UTC);
			DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
			String formattedTimestamp = now.format(formatter);
			otp.setTimestamp(formattedTimestamp);

			otp.setTxnId(loginMethod.getTxnId());
			otp.setOtpValue(encryptedLoginId);
			
			String[] scope;
			scope = new String[] {"abha-enrol", "mobile-verify"};

			Map<String, Object> authDataMap = new HashMap<>();
			authDataMap.put("otp", otp);
			authDataMap.put("authMethods", new String[] { "otp" });

			enrollAuthByabdm.setAuthData(authDataMap);
			enrollAuthByabdm.setScope(scope);
			

			logger.info("ABDM request for enroll by ABDM: " + enrollAuthByabdm);

			String requestObj = new Gson().toJson(enrollAuthByabdm);
			HttpEntity<String> httpEntity = new HttpEntity<>(requestObj, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestAuthByAbdm, HttpMethod.POST,
					httpEntity, String.class);

			logger.info("ABDM response for verify mobile number for abha enrollment: " + responseEntity);
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

	public String formAadharEnrollReqObjByAadhar(LoginMethod loginData, String encryptedLoginId) {

		EnrollByAadhaar enrollByAadhar = new EnrollByAadhaar();
		OtpRequest otp = new OtpRequest();

		// Get current timestamp
		OffsetDateTime now = OffsetDateTime.now(java.time.ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
		String formattedTimestamp = now.format(formatter);
		otp.setTimestamp(formattedTimestamp);

		otp.setTxnId(loginData.getTxnId());
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

		return requestObj;
	}

	public String formAadharEnrollReqObjByBiometric(LoginMethod loginData, String encryptedLoginId) {

		EnrollByAadhaar enrollByAadhar = new EnrollByAadhaar();
		BioRequest bio = new BioRequest();

		// Get current timestamp
		OffsetDateTime now = OffsetDateTime.now(java.time.ZoneOffset.UTC);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
		String formattedTimestamp = now.format(formatter);
		bio.setTimestamp(formattedTimestamp);

		bio.setTxnId(loginData.getTxnId());
		
		bio.setAadhaar(encryptedLoginId);
		bio.setFingerPrintAuthPid(loginData.getPId());
		bio.setMobile(loginData.getMobileNumber());

		Map<String, Object> authDataMap = new HashMap<>();
		authDataMap.put("bio", bio);
		authDataMap.put("authMethods", new String[] { "bio" });

		enrollByAadhar.setAuthData(authDataMap);

		ConsentRequest consent = new ConsentRequest();
		consent.setCode("abha-enrollment");
		consent.setVersion("1.4");
		enrollByAadhar.setConsent(consent);
		logger.info("ABDM request for enroll by biometric: " + enrollByAadhar);

		String requestObj = new Gson().toJson(enrollByAadhar);

		return requestObj;
	}

	@Override
	public String getAbhaCardPrinted(String reqObj) throws FHIRException {

		String res = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();


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

			JsonObject stringReqObj = JsonParser.parseString(reqObj).getAsJsonObject();
			if (stringReqObj.has("xToken") && stringReqObj.get("xToken") != null) {
				String xToken = stringReqObj.get("xToken").getAsString();
				headers.add("X-token", "Bearer " + xToken);
			}
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(printAbhaCard, HttpMethod.GET, httpEntity,
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

	private void constructHealthIdResponse(HealthIDResponse healthIDResp, JsonObject profile) throws ParseException {
		healthIDResp.setHealthIdNumber(profile.get("ABHANumber").getAsString());
		JsonArray phrAddressArray = profile.getAsJsonArray("phrAddress");
		StringBuilder abhaAddressBuilder = new StringBuilder();

		for (int i = 0; i < phrAddressArray.size(); i++) {
		    abhaAddressBuilder.append(phrAddressArray.get(i).getAsString());
		    if (i < phrAddressArray.size() - 1) {
		        abhaAddressBuilder.append(", ");
		    }
		}
		healthIDResp.setHealthId(abhaAddressBuilder.toString());
		healthIDResp.setName(
				healthIDResp.getFirstName() + " " + healthIDResp.getMiddleName() + " " + healthIDResp.getLastName());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = simpleDateFormat.parse(profile.get("dob").getAsString());
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		SimpleDateFormat month = new SimpleDateFormat("MM");
		SimpleDateFormat day = new SimpleDateFormat("dd");
		healthIDResp.setYearOfBirth(year.format(date));
		healthIDResp.setMonthOfBirth(month.format(date));
		healthIDResp.setDayOfBirth(day.format(date));
	}
	
}
