package com.wipro.fhir.service.facility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.mongo.care_context.SaveFacilityIdForVisit;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.ndhm.GenerateSession_NDHMService;
import com.wipro.fhir.service.v3.abha.GenerateAuthSessionService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;
@Service
public class FacilityServiceImpl implements FacilityService{
	
	@Value("${getAbdmFacilityServicies}")
	private String getAbdmServicies;
	@Value("${abdmFacilityId}")
	private String abdmFacilityId;
	
	@Value("${x-CM-ID}")
	private String xCMId;
	
	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private Common_NDHMService common_NDHMService;
	
	@Autowired
	private GenerateAuthSessionService generateAuthSessionService;
	
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Override
	public String fetchRegisteredFacilities() throws FHIRException {
		String res = null;
		List<HashMap<String,Object>> list = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String,Object> map = new HashMap<>();
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
			headers.add("X-CM-ID", xCMId);
			headers.add("Authorization", abhaAuthToken);
			
			HttpEntity<String> httpEntity = new HttpEntity<>(headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(getAbdmServicies, HttpMethod.GET,
					httpEntity, String.class);
			
			String responseStrLogin = common_NDHMService.getBody(responseEntity);
			if (responseEntity.getStatusCode() == HttpStatusCode.valueOf(200) && responseEntity.hasBody()) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				JsonElement jsonElement = jsnOBJ.get("services");
				JsonArray asJsonArray = jsonElement.getAsJsonArray();
				for (JsonElement jsonElement2 : asJsonArray) {
					JsonObject asJsonObject = jsonElement2.getAsJsonObject();
					String types = asJsonObject.get("types").toString();
					if(null != types && types.contains("HIP")) {
						map = new HashMap<>();
						map.put("id", asJsonObject.get("id"));
						map.put("name", asJsonObject.get("name"));
						list.add(map);
					}
				}
				res = new Gson().toJson(list);
			} else
				res = "NDHM_FHIR Error while getting facilities";
		}
			
		catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABDM API" + e.getMessage());
		}
		return res;
	}
	
	@Override
	public String saveAbdmFacilityId(String reqObj) throws FHIRException {
		String res = null;
		try {
			SaveFacilityIdForVisit requestObj = InputMapper.gson().fromJson(reqObj, SaveFacilityIdForVisit.class);
			if(requestObj.getAbdmFacilityId() == null || requestObj.getAbdmFacilityId() == "") {
				requestObj.setAbdmFacilityId(abdmFacilityId);
			}
			Integer response = benHealthIDMappingRepo.updateFacilityIdForVisit(requestObj.getVisitCode(), requestObj.getAbdmFacilityId());
			if(response > 0 ) {
				res = "ABDM Facility ID updated successfully";
			} else
				res = "FHIR Error while updating ABDM Facility ID";
		}
		catch (Exception e) {
			throw new FHIRException(e.getMessage());
		}
		return res;
	}
	
}
  