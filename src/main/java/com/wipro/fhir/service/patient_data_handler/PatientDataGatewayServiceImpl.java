/*
* AMRIT – Accessible Medical Records via Integrated Technology 
* Integrated EHR (Electronic Health Records) Solution 
*
* Copyright (C) "Piramal Swasthya Management and Research Institute" 
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.service.patient_data_handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wipro.fhir.data.healthID.BenHealthIDMapping;
import com.wipro.fhir.data.patient.PatientDemographicDetails;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.patient_data_handler.TRG_PatientResourceData;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile.Profile;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address;
import com.wipro.fhir.data.request_handler.PatientSearchAPIResponse;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.repo.patient_data_handler.TRG_PatientResourceData_Repo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
@PropertySource("classpath:application.properties")
public class PatientDataGatewayServiceImpl implements PatientDataGatewayService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private PatientDemographicModel_NDHM_Patient_Profile_Repo patientDemographicModel_NDHM_Patient_Profile_Repo;
	@Autowired
	private CommonService commonService;
	@Autowired
	private TRG_PatientResourceData_Repo tRG_PatientResourceData_Repo;
	@Autowired
	private APIChannel aPIChannel;
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;
	@Value("${patient-search-page-size}")
	private String patient_search_page_size;

	@Override
	public List<PatientDemographicModel_NDHM_Patient_Profile> feedPatientProfileToMongoDB(
			List<PatientDemographicModel_NDHM_Patient_Profile> ndhm_Patient_Profile_List) throws FHIRException {
		List<PatientDemographicModel_NDHM_Patient_Profile> resultSet = null;
		for (PatientDemographicModel_NDHM_Patient_Profile profile : ndhm_Patient_Profile_List) {
			if (profile.getProfile().getPatient().getHealthId() != null)
				profile.setHealthId(profile.getProfile().getPatient().getHealthId());
			if (profile.getProfile().getPatient().getHealthIdNumber() != null)
				profile.setHealthIdNumber(profile.getProfile().getPatient().getHealthIdNumber());
		}
		if (ndhm_Patient_Profile_List != null && ndhm_Patient_Profile_List.size() > 0)
			resultSet = commonService.savePatientProfileDataToMongo(ndhm_Patient_Profile_List);

		return resultSet;
	}

	@Override
	public String generatePatientProfileAMRIT_SaveTo_Mongo(String Authorization) throws FHIRException {
		
		List<PatientDemographicModel_NDHM_Patient_Profile> ppList = new ArrayList<>();

		List<TRG_PatientResourceData> resultSetList = tRG_PatientResourceData_Repo
				.getByProcessedOrderByCreatedDateLimit20();
		ppList = generatePatientProfileFromAMRIT(Authorization, resultSetList);

		ppList = feedPatientProfileToMongoDB(ppList);

		if (ppList != null && ppList.size() > 0) {
			List<Long> ids = new ArrayList<>();
			ids.add((long) 0);
			for (PatientDemographicModel_NDHM_Patient_Profile pp : ppList) {
				ids.add(pp.getTriggerTableAIId());
			}
			tRG_PatientResourceData_Repo.updateProcessedFlagForProfileCreated(ids);
		}

		return new Gson().toJson(ppList);
	}

	@Override
	public String generatePatientProfileAMRIT_SaveTo_Mongo(String Authorization,
			ResourceRequestHandler resourceRequestHandler) throws FHIRException {
		
		List<PatientDemographicModel_NDHM_Patient_Profile> ppList = new ArrayList<>();

		List<TRG_PatientResourceData> resultSetList = tRG_PatientResourceData_Repo
				.getByBenIdLatestRecord(resourceRequestHandler.getBeneficiaryID());

		ppList = generatePatientProfileFromAMRIT(Authorization, resultSetList);

		ppList = feedPatientProfileToMongoDB(ppList);

		if (ppList != null && ppList.size() > 0) {
			List<Long> ids = new ArrayList<>();
			ids.add((long) 0);
			for (PatientDemographicModel_NDHM_Patient_Profile pp : ppList) {
				ids.add(pp.getTriggerTableAIId());
			}
			tRG_PatientResourceData_Repo.updateProcessedFlagForProfileCreated(ids);
		}

		return new Gson().toJson(ppList);
	}

	@Override
	public String searchPatientProfileMongo(String Authorization, ResourceRequestHandler resourceRequestHandler)
			throws FHIRException {
		String response = null;

		if (resourceRequestHandler != null) {
			int totalPage = 0;
			List<PatientDemographicModel_NDHM_Patient_Profile> result = null;
			Page<PatientDemographicModel_NDHM_Patient_Profile> resultSet = commonService
					.searchPatientProfileFromMongo(resourceRequestHandler);
			if (resultSet != null && resultSet.getSize() > 0) {
				result = resultSet.getContent();
				totalPage = resultSet.getTotalPages();
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("data", result);
				responseMap.put("pageSize", Integer.parseInt(patient_search_page_size));
				responseMap.put("totalPage", totalPage);
				response = new Gson().toJson(responseMap);
			}
		} else
			throw new FHIRException(
					"request is null.kindly provide valid required parameter. ABHA or ABHA No or Amrit Id, External Id");
		return response;
	}

// generate patient profile for AMRIT patient - using patient resource as input to create NDHM patient profile
	private List<PatientDemographicModel_NDHM_Patient_Profile> generatePatientProfileFromAMRIT(String Authorization,
			List<TRG_PatientResourceData> resultSetList) throws FHIRException {
		List<PatientDemographicModel_NDHM_Patient_Profile> ppList = new ArrayList<>();
		PatientDemographicModel_NDHM_Patient_Profile patientProfile = null;

		if (resultSetList != null && resultSetList.size() > 0) {
			ResourceRequestHandler resourceRequestHandler;
			for (TRG_PatientResourceData obj : resultSetList) {
				try {

					if (obj.getBeneficiaryID() != null) {
						List<PatientDemographicModel_NDHM_Patient_Profile> resultList = patientDemographicModel_NDHM_Patient_Profile_Repo
								.findByAmritId(obj.getBeneficiaryID().toString());

						if (resultList != null && resultList.size() > 0)
							continue;
					}

					resourceRequestHandler = new ResourceRequestHandler();
					resourceRequestHandler.setBeneficiaryID(obj.getBeneficiaryID());

					String responseBody = aPIChannel.benSearchByBenID(Authorization, resourceRequestHandler);
					PatientSearchAPIResponse psr = (InputMapper.gson().fromJson(responseBody,
							PatientSearchAPIResponse.class));
					if (psr != null && psr.getData().size() == 1) {
						patientProfile = generatePatientProfileFromPatientDemographicData(psr.getData().get(0), obj);
						patientProfile.setTriggerTableAIId(obj.getId());
						ppList.add(patientProfile);
					} else {
						// multiple patient found
					}
				} catch (Exception e) {
					
					logger.error("Error in patient profile creation - Beneficiary ID : " + obj.getBeneficiaryID()
							+ " error : " + e.getMessage());
				}
			}
		}
		return ppList;
	}

	private PatientDemographicModel_NDHM_Patient_Profile generatePatientProfileFromPatientDemographicData(
			PatientDemographicDetails pd, TRG_PatientResourceData obj) {
		PatientDemographicModel_NDHM_Patient_Profile patientProfile = null;
		if (pd != null) {
			patientProfile = new PatientDemographicModel_NDHM_Patient_Profile();

			patientProfile.setRequestId(commonService.getUUID());
			
			patientProfile.setTimestamp(obj.getCreatedDate());
			

			patientProfile.setAmritId(obj.getBeneficiaryID().toString());

			// profile class obj
			Profile profile = patientProfile.new Profile();
			profile.setHipCode("AMRIT_HIP_CODE");

			// patient class obj
			Patient patient = profile.new Patient();
			String title = "";
			if (pd.getM_title() != null && pd.getM_title().getTitleName() != null)
				title = pd.getM_title().getTitleName() + " ";

			if (pd.getFirstName() != null)
				patient.setFirstName(pd.getFirstName());
			if (pd.getLastName() != null)
				patient.setLastName(pd.getLastName());
			patient.setName(title + pd.getFirstName() + " " + ((pd.getLastName() != null) ? pd.getLastName() : ""));
			if (pd.getGenderID() != null) {
				switch (pd.getGenderID()) {
				case 1:
					patient.setGender("M");
					break;
				case 2:
					patient.setGender("F");
					break;
				case 3:
					patient.setGender("O");
					break;

				default:
					patient.setGender("UNKNOWN");
					break;
				}
			}

			if (pd.getdOB() != null) {
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(pd.getdOB().getTime());

				patient.setYearOfBirth(String.valueOf(cal.get(Calendar.YEAR)));
				patient.setMonthOfBirth(String.valueOf(cal.get(Calendar.MONTH) + 1));
				patient.setDayOfBirth(String.valueOf(cal.get(Calendar.DATE)));
			}

			// check ABHA for patients
			ArrayList<BenHealthIDMapping> healthIDMappedList = benHealthIDMappingRepo
					.getHealthDetails(pd.getBeneficiaryRegID());
			if (healthIDMappedList != null && healthIDMappedList.size() > 0) {
				patientProfile.setHealthId(healthIDMappedList.get(healthIDMappedList.size() - 1).getHealthId());
				patientProfile
						.setHealthIdNumber(healthIDMappedList.get(healthIDMappedList.size() - 1).getHealthIdNumber());

				patient.setHealthId(healthIDMappedList.get(healthIDMappedList.size() - 1).getHealthId());
				patient.setHealthIdNumber(healthIDMappedList.get(healthIDMappedList.size() - 1).getHealthIdNumber());
			}

			// set identifiers
			Map<String, String> identifiersMap = new HashMap<String, String>();
			List<Map<String, String>> identifiersList = new ArrayList<>();

			if (pd.getBenPhoneMaps() != null && pd.getBenPhoneMaps().size() > 0) {
				identifiersMap.put("MOBILE", pd.getBenPhoneMaps().get(0).getPhoneNo());
				identifiersList.add(identifiersMap);
			}
			patient.setIdentifiers(identifiersList);

			// address class obj
			Address address = patient.new Address();
			if (pd.getI_bendemographics().getPinCode() != null)
				address.setPincode(pd.getI_bendemographics().getPinCode());
			if (pd.getI_bendemographics().getDistrictName() != null)
				address.setDistrict(pd.getI_bendemographics().getDistrictName());
			if (pd.getI_bendemographics().getStateName() != null)
				address.setState(pd.getI_bendemographics().getStateName());

			if (pd.getI_bendemographics().getAddressLine1() != null
					|| pd.getI_bendemographics().getAddressLine1() != null
					|| pd.getI_bendemographics().getAddressLine1() != null) {
				String address1 = (pd.getI_bendemographics().getAddressLine1() != null)
						? pd.getI_bendemographics().getAddressLine1()
						: "";
				String address2 = (pd.getI_bendemographics().getAddressLine2() != null)
						? pd.getI_bendemographics().getAddressLine2()
						: "";
				String address3 = (pd.getI_bendemographics().getAddressLine3() != null)
						? pd.getI_bendemographics().getAddressLine3()
						: "";

				address.setLine(address1 + " " + address2 + " " + address3);
			}

			patient.setAddress(address);
			profile.setPatient(patient);
			patientProfile.setProfile(profile);

		}
		return patientProfile;
	}

	@Override
	public String searchPatientProfileMongoPagination(Integer pageNo) throws FHIRException {
		String response = null;
		int totalPage = 0;
		Integer pageSize = Integer.valueOf(patient_search_page_size);
		List<PatientDemographicModel_NDHM_Patient_Profile> resultSet = null;
		Page<PatientDemographicModel_NDHM_Patient_Profile> p = null;
		if (pageNo != null) {
			PageRequest pr = new PageRequest(pageNo, pageSize);

			p = patientDemographicModel_NDHM_Patient_Profile_Repo.findAll(pr);
			if (p != null) {
				resultSet = p.getContent();
				totalPage = p.getTotalPages();
			}

		} else {
			// page no not invalid
			throw new FHIRException("Invalid page no");
		}

		if (resultSet != null && resultSet.size() > 0) {
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("data", resultSet);
			responseMap.put("pageSize", Integer.parseInt(patient_search_page_size));
			responseMap.put("totalPage", totalPage);
			response = new Gson().toJson(responseMap);
		}

		return response;
	}

}
