package com.wipro.fhir.service.v3.careContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.joda.time.DateTime;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.mongo.care_context.GenerateTokenAbdmResponses;
import com.wipro.fhir.data.v3.abhaCard.LoginMethod;
import com.wipro.fhir.data.v3.abhaCard.RequestOTPEnrollment;
import com.wipro.fhir.data.v3.careContext.CareContextLinkTokenRequest;
import com.wipro.fhir.data.v3.careContext.CareContexts;
import com.wipro.fhir.data.v3.careContext.GenerateCareContextTokenRequest;
import com.wipro.fhir.data.v3.careContext.LinkCareContextRequest;
import com.wipro.fhir.data.v3.careContext.PatientCareContext;
import com.wipro.fhir.repo.mongo.generateToken_response.GenerateTokenAbdmResponsesRepo;
import com.wipro.fhir.data.v3.careContext.AddCareContextRequest;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.v3.abha.GenerateAuthSessionService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
public class CareContextLinkingServiceImpl implements CareContextLinkingService {

	@Autowired
	private GenerateAuthSessionService generateAuthSessionService;

	@Autowired
	private Common_NDHMService common_NDHMService;

	@Autowired
	private GenerateTokenAbdmResponsesRepo generateTokenAbdmResponsesRepo;

	@Value("${x-CM-ID}")
	String abhaMode;

	@Value("${abdmFacilityId}")
	String abdmFacilityId;

	@Value("${generateTokenForLinkCareContext}")
	String generateTokenForLinkCareContext;

	@Value("${linkCareContext}")
	String linkCareContext;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public String generateTokenForCareContext(String request) throws FHIRException {
		String res = null;
		String linkToken = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();

		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();
			CareContextLinkTokenRequest careContextLinkRequest = InputMapper.gson().fromJson(request,
					CareContextLinkTokenRequest.class);

			if (null != careContextLinkRequest.getAbhaAddress()) {
				String linkExists = checkRecordExisits(careContextLinkRequest.getAbhaAddress());
				responseMap.put("linkToken", linkExists);
			} else {

				MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
				headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
				String requestId = UUID.randomUUID().toString();
				headers.add("REQUEST-ID", requestId);

				TimeZone tz = TimeZone.getTimeZone("UTC");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				df.setTimeZone(tz);
				String nowAsISO = df.format(new Date());
				headers.add("TIMESTAMP", nowAsISO);
				headers.add("Authorization", abhaAuthToken);
				headers.add("X-CM-ID", abhaMode);
				if (null != careContextLinkRequest.getAbdmFacilityId()
						&& "" != careContextLinkRequest.getAbdmFacilityId()) {
					headers.add("X-HIP-ID", careContextLinkRequest.getAbdmFacilityId());
				} else {
					headers.add("X-HIP-ID", abdmFacilityId);
				}

				GenerateCareContextTokenRequest generateTokenRequest = new GenerateCareContextTokenRequest();
				if (null != careContextLinkRequest.getAbhaNumber() && "" != careContextLinkRequest.getAbhaNumber()) {
					String abha = careContextLinkRequest.getAbhaNumber();
					String abhaNumber = abha.replace("-", "");
					generateTokenRequest.setAbhaNumber(abhaNumber);
				}

				generateTokenRequest.setAbhaAddress(careContextLinkRequest.getAbhaAddress());
				generateTokenRequest.setName(careContextLinkRequest.getName());
				generateTokenRequest.setYearOfBirth(careContextLinkRequest.getYearOfBirth());

				if (careContextLinkRequest.getGender().equalsIgnoreCase("female")) {
					generateTokenRequest.setGender("F");
				} else if (careContextLinkRequest.getGender().equalsIgnoreCase("male")) {
					generateTokenRequest.setGender("M");
				} else {
					generateTokenRequest.setGender("O");
				}

				String requestOBJ = new Gson().toJson(generateTokenRequest);
				logger.info("ABDM reqobj for generate token link for carecontext : " + requestOBJ);

				HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
				ResponseEntity<String> responseEntity = restTemplate.exchange(generateTokenForLinkCareContext,
						HttpMethod.POST, httpEntity, String.class);

				logger.info("ABDM response for generate token link for carecontext : " + responseEntity);
				String responseStrLogin = common_NDHMService.getBody(responseEntity);
				JsonParser jsnParser = new JsonParser();
				if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(202)) {
					String mongoResponse = common_NDHMService.getMongoNDHMResponse(requestId);
					responseMap.put("requestId", requestId);
					if (!mongoResponse.equalsIgnoreCase("failure")) {
						JsonElement jsnElmnt1 = jsnParser.parse(mongoResponse);
						JsonObject jsnOBJ1 = new JsonObject();
						jsnOBJ1 = jsnElmnt1.getAsJsonObject();
						try {
							if (jsnOBJ1.get("linkToken") != null) {
								linkToken = jsnOBJ1.getAsJsonObject("linkToken").getAsString();
								responseMap.put("linkToken", linkToken);
							} else
								throw new FHIRException(
										"NDHM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
						} catch (Exception e) {
							throw new FHIRException(
									"NDHM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
						}
					}

				} else {
					throw new FHIRException(responseEntity.getBody());
				}
			}
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

		return responseMap.toString();
	}

	@Override
	public String linkCareContext(String request) throws FHIRException {
		String res = null;
		String linkToken = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();

		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();
			AddCareContextRequest addCareContextRequest = InputMapper.gson().fromJson(request,
					AddCareContextRequest.class);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			JsonParser jsnParser = new JsonParser();

			if (null != addCareContextRequest.getLinkToken()) {
				headers.add("X-LINK-TOKEN", addCareContextRequest.getLinkToken());
			} else { // if link token is not found then fetch from mongo DB
				String mongoResponse = common_NDHMService.getMongoNDHMResponse(addCareContextRequest.getRequestId());
				if (!mongoResponse.equalsIgnoreCase("failure")) {
					JsonElement jsnElmnt1 = jsnParser.parse(mongoResponse);
					JsonObject jsnOBJ1 = new JsonObject();
					jsnOBJ1 = jsnElmnt1.getAsJsonObject();
					try {
						if (jsnOBJ1.get("linkToken") != null) {
							linkToken = jsnOBJ1.getAsJsonObject("linkToken").getAsString();
							headers.add("X-LINK-TOKEN", linkToken);
						} else
							throw new FHIRException(
									"ABDM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					} catch (Exception e) {
						throw new FHIRException(
								"ABDM_FHIR " + jsnOBJ1.getAsJsonObject("Error").get("Message").getAsString());
					}
				}

			}

			headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
			headers.add("REQUEST-ID", UUID.randomUUID().toString());

			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date());
			headers.add("TIMESTAMP", nowAsISO);
			headers.add("Authorization", abhaAuthToken);
			headers.add("X-CM-ID", abhaMode);
			if (null != addCareContextRequest.getAbdmFacilityId() && "" != addCareContextRequest.getAbdmFacilityId()) {
				headers.add("X-HIP-ID", addCareContextRequest.getAbdmFacilityId());
			} else {
				headers.add("X-HIP-ID", abdmFacilityId);
			}

			LinkCareContextRequest linkCareContextRequest = new LinkCareContextRequest();
			CareContexts careContexts = new CareContexts();
			PatientCareContext patient = new PatientCareContext();

			ArrayList<CareContexts> cc = new ArrayList<CareContexts>();
			careContexts.setReferenceNumber(addCareContextRequest.getVisitCode());
			careContexts.setDisplay(addCareContextRequest.getDisplay());
			cc.add(careContexts);

			ArrayList<PatientCareContext> pcc = new ArrayList<PatientCareContext>();
			patient.setReferenceNumber(addCareContextRequest.getVisitCode());
			patient.setDisplay(addCareContextRequest.getDisplay());
			patient.setDisplay(addCareContextRequest.getDisplay());
			patient.setCount(1);
			patient.setCareContexts(cc);
			pcc.add(patient);

			if (null != addCareContextRequest.getAbhaNumber() && "" != addCareContextRequest.getAbhaNumber()) {
				String abha = addCareContextRequest.getAbhaNumber();
				String abhaNumber = abha.replace("-", "");
				linkCareContextRequest.setAbhaNumber(abhaNumber);
			}

			linkCareContextRequest.setAbhaAddress(addCareContextRequest.getAbhaAddress());
			linkCareContextRequest.setPatient(pcc);

			String requestOBJ = new Gson().toJson(linkCareContextRequest);
			logger.info("ABDM reqobj for generate token link for carecontext : " + requestOBJ);

			HttpEntity<String> httpEntity = new HttpEntity<>(requestOBJ, headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(linkCareContext, HttpMethod.POST, httpEntity,
					String.class);

			logger.info("ABDM response for generate token link for carecontext : " + responseEntity);
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(202)) {
				res = "Care Context added successfully";

			} else {
				throw new FHIRException(responseEntity.getBody());
			}
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

		return res;
	}

	public String checkRecordExisits(String abhaAddress) {
		GenerateTokenAbdmResponses result = generateTokenAbdmResponsesRepo.findByAbhaAddress(abhaAddress);
		logger.info("find by abha address result - ", result);

		if (result != null && result.getCreatedDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			Date threeMonthsAgo = cal.getTime();
			String linkResponse = result.getResponse();

			if (result.getCreatedDate().isAfter(threeMonthsAgo.getTime())) {
				if (linkResponse != null) {
					try {
						ObjectMapper mapper = new ObjectMapper();
						JsonNode root = mapper.readTree(linkResponse);
						JsonNode linkToken = root.path("LinkToken");
						if (!linkToken.isNull() && !linkToken.isMissingNode()) {
							return linkToken.asText();
						}
					} catch (Exception e) {
						logger.info("failed abha exists check with exception - ", e.getMessage());
						return null;
					}
				}
			}
		}

		return null;
	}

}
