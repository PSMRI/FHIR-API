package com.wipro.fhir.service.v3.careContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.wipro.fhir.repo.v3.careContext.CareContextRepo;
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

	@Autowired
	private CareContextRepo careContextRepo;
	
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
					GenerateTokenAbdmResponses mongoResponse = common_NDHMService.getLinkToken(requestId);
					responseMap.put("requestId", requestId);
					if (mongoResponse != null && mongoResponse.getResponse() != null) {
				    String abhaResponse = mongoResponse.getResponse();
				    JsonElement jsonElement = jsnParser.parse(abhaResponse);
				    JsonObject jsonObject = jsonElement.getAsJsonObject();

					    try {
					        JsonElement linkTokenElement = jsonObject.get("LinkToken");
					        
					        if (linkTokenElement != null && !linkTokenElement.isJsonNull()) {
					            linkToken = linkTokenElement.getAsString();
					            responseMap.put("X-LINK-TOKEN", linkToken);
					            logger.info("Mongo has link token");
					        } else {
					        	if (jsonObject.has("Error") && !jsonObject.get("Error").isJsonNull()) {
					        	    JsonObject errorObject = jsonObject.getAsJsonObject("Error");
						        	logger.info("Mongo has no link token other message - " + errorObject.toString());
					        	    responseMap.put("error", errorObject.toString());
					        	} else {
					        	    responseMap.put("error", "Unknown error");
					        	}
					        }
					    } catch (Exception e) {
					        throw new FHIRException("ABDM_FHIR Error while parsing response: " + e.getMessage());
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
	public String linkCareContext(String request) throws FHIRException {		String linkToken = null;
		Map<String, String> responseMap = new HashMap<>();
		RestTemplate restTemplate = new RestTemplate();

		try {
			String abhaAuthToken = generateAuthSessionService.getAbhaAuthToken();
			AddCareContextRequest addCareContextRequest = InputMapper.gson().fromJson(request,
					AddCareContextRequest.class);
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			JsonParser jsnParser = new JsonParser();

			if (null != addCareContextRequest.getLinkToken()) {
				linkToken = addCareContextRequest.getLinkToken();
				headers.add("X-LINK-TOKEN", addCareContextRequest.getLinkToken());
			} else { // if link token is not found then fetch from mongo DB
				GenerateTokenAbdmResponses mongoResponse = common_NDHMService
						.getLinkToken(addCareContextRequest.getRequestId());
				if (mongoResponse != null && mongoResponse.getResponse() != null) {
					String abhaResponse = mongoResponse.getResponse();
					JsonElement jsonElement = jsnParser.parse(abhaResponse);
					JsonObject jsonObject = jsonElement.getAsJsonObject();

					try {
						JsonElement linkTokenElement = jsonObject.get("LinkToken"); 
 						if (linkTokenElement != null && !linkTokenElement.isJsonNull()) {
							linkToken = linkTokenElement.getAsString();
							headers.add("X-LINK-TOKEN", linkToken);
						}  else {
				        	if (jsonObject.has("Error") && !jsonObject.get("Error").isJsonNull()) {
				        	    JsonObject errorObject = jsonObject.getAsJsonObject("Error");
				        	    responseMap.put("error", errorObject.toString());
				        	} else {
				        	    responseMap.put("error", "Unknown error");
				        	}
				        }
					} catch (Exception e) {
						throw new FHIRException("ABDM_FHIR Error while parsing response: " + e.getMessage());
					}
				}

			}
			
			if (linkToken != null) { 

				headers.add("Content-Type", MediaType.APPLICATION_JSON + ";charset=utf-8");
				headers.add("REQUEST-ID", UUID.randomUUID().toString());

				TimeZone tz = TimeZone.getTimeZone("UTC");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				df.setTimeZone(tz);
				String nowAsISO = df.format(new Date());
				headers.add("TIMESTAMP", nowAsISO);
				headers.add("Authorization", abhaAuthToken);
				headers.add("X-CM-ID", abhaMode);
				if (null != addCareContextRequest.getAbdmFacilityId()
						&& "" != addCareContextRequest.getAbdmFacilityId()) {
					headers.add("X-HIP-ID", addCareContextRequest.getAbdmFacilityId());
				} else {
					headers.add("X-HIP-ID", abdmFacilityId);
				}
				
				String[] hiTypes = findHiTypes(addCareContextRequest.getVisitCode(), addCareContextRequest.getVisitCategory());
				
				LinkCareContextRequest linkCareContextRequest = new LinkCareContextRequest();
				CareContexts careContexts = new CareContexts();
				ArrayList<PatientCareContext> pcc = new ArrayList<PatientCareContext>();

				for (String hiType : hiTypes) {
					PatientCareContext patient = new PatientCareContext();

					ArrayList<CareContexts> cc = new ArrayList<CareContexts>();
					careContexts.setReferenceNumber(addCareContextRequest.getVisitCode());
					careContexts.setDisplay(addCareContextRequest.getVisitCategory());
					cc.add(careContexts);

					patient.setReferenceNumber(addCareContextRequest.getVisitCode());
					patient.setDisplay(addCareContextRequest.getVisitCategory() + " care context of " + addCareContextRequest.getAbhaNumber());
					patient.setCount(1);
					patient.setCareContexts(cc);
					patient.setHiType(hiType);
					pcc.add(patient);
				}
				

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
				ResponseEntity<String> responseEntity = restTemplate.exchange(linkCareContext, HttpMethod.POST,
						httpEntity, String.class);

				logger.info("ABDM response for generate token link for carecontext : " + responseEntity);
				String responseStrLogin = common_NDHMService.getBody(responseEntity);
				if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(202)) {
					responseMap.put("message", "Care Context added successfully");

				} else {
					throw new FHIRException(responseEntity.getBody());
				}
			}
		} catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}

		return responseMap.toString();
	}

	public String checkRecordExisits(String abhaAddress) {
		GenerateTokenAbdmResponses result = generateTokenAbdmResponsesRepo.findByAbhaAddress(abhaAddress);
		logger.info("find by abha address result - ", result);
		String linkResponse = null;

		if (result != null && result.getCreatedDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -3);
			Date threeMonthsAgo = cal.getTime();
			linkResponse = result.getResponse();

			if (result.getCreatedDate().after(threeMonthsAgo)) {
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

		return linkResponse;
	}
	
	public String[] findHiTypes(String visitCode, String visitCategory) {

		List<String> hiTypes = new ArrayList<>();
		if (visitCategory.equalsIgnoreCase("General OPD")) {
			hiTypes.add("OPConsultation");
		} else if (visitCategory.equalsIgnoreCase("General OPD (QC)")) {
			hiTypes.add("OPConsultation");
		}
		hiTypes.add("DischargeSummary");
		
		int hasPhyVitals = careContextRepo.hasPhyVitals(visitCode);
		if(hasPhyVitals > 0) {
			hiTypes.add("WellnessRecord");
		}
		int hasPrescription = careContextRepo.hasPrescribedDrugs(visitCode);
		if (hasPrescription > 0) {
			hiTypes.add("Prescription");
		}

		int hasLabTests = careContextRepo.hasLabtestsDone(visitCode);
		if (hasLabTests > 0) {
			hiTypes.add("DiagnoticsReport");
		}
		
		int hasVaccineDetails = careContextRepo.hasVaccineDetails(visitCode);
		if (hasVaccineDetails > 0) {
			hiTypes.add("ImmunizationRecord");
		}
		logger.info("HiTypes", hiTypes);		
		return hiTypes.toArray(new String[0]);
	}

}

