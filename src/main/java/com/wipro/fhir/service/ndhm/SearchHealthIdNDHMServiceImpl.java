package com.wipro.fhir.service.ndhm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

@Service
public class SearchHealthIdNDHMServiceImpl implements SearchHealthIdNDHMService {

	@Value("${searchByHealthId}")
	private String searchByHealthId;

	@Value("${searchByMobile}")
	private String searchByMobile;

	@Autowired
	private Common_NDHMService common_NDHMService;

	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;

	@Autowired
	private HttpUtils httpUtils;

	@Override
	public String searchHealthId(String healthId) throws FHIRException {
		String res = null;
		ResponseEntity<String> responseEntity = null;
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			responseEntity = httpUtils.postWithResponseEntity(searchByHealthId, healthId, headers);
			res = common_NDHMService.getBody(responseEntity);
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA Search API" + e);
		}
		return res;
	}

	@Override
	public String searchWithMobileNumber(String mobileSearchDTO) throws FHIRException {
		String res = null;
		ResponseEntity<String> responseEntity = null;
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken);
			responseEntity = httpUtils.postWithResponseEntity(searchByMobile, mobileSearchDTO, headers);
			res = common_NDHMService.getBody(responseEntity);
		} catch (Exception e) {
			throw new FHIRException("NDHM_FHIR Error while accessing ABHA Search with Mobile Number API" + e);
		}
		return res;
	}

}
