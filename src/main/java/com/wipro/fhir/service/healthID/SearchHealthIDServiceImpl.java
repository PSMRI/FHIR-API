package com.wipro.fhir.service.healthID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.service.ndhm.SearchHealthIdNDHMService;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class SearchHealthIDServiceImpl implements SearchHealthIDService{

	@Autowired
	private SearchHealthIdNDHMService searchHealthIdNDHMService;
	
	@Override
	public String searchHealthId(String healthId) throws FHIRException {
		String resp = null;
		JSONObject jsonObject = new JSONObject(healthId);
		if( jsonObject.has("healthId") && null != jsonObject.getString("healthId")) {
			resp = searchHealthIdNDHMService.searchHealthId(healthId);
		}else {
			throw new FHIRException("NDHM_FHIR Error Invalid HealthId : "+healthId);
		}
		return resp;
	}

	@Override
	public String searchHealthIdWithMobile(String mobileSearchDTO) throws FHIRException {
		String searchWithMobileNumber = null;
		if(null != mobileSearchDTO) {
			searchWithMobileNumber = searchHealthIdNDHMService.searchWithMobileNumber(mobileSearchDTO);
		}else {
			throw new FHIRException("NDHM_FHIR Error Invalid Mobile Number : "+mobileSearchDTO);
		}
		return searchWithMobileNumber;
	}

}
