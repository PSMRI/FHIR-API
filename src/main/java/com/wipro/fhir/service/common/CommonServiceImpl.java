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
package com.wipro.fhir.service.common;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wipro.fhir.data.healthID.Authorize;
import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.mongo.amrit_resource.TempCollection;
import com.wipro.fhir.data.mongo.care_context.CareContexts;
import com.wipro.fhir.data.mongo.care_context.HIP;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.mongo.care_context.NDHMResponse;
import com.wipro.fhir.data.mongo.care_context.Notification;
import com.wipro.fhir.data.mongo.care_context.PatientCareContexts;
import com.wipro.fhir.data.mongo.care_context.PatientCareContextsStringOBJ;
import com.wipro.fhir.data.mongo.care_context.SMSNotify;
import com.wipro.fhir.data.patient.PatientDemographic;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.repo.mongo.amrit_resource.AMRIT_ResourceMongoRepo;
import com.wipro.fhir.repo.mongo.amrit_resource.PatientCareContextsMongoRepo;
import com.wipro.fhir.repo.mongo.amrit_resource.TempCollectionRepo;
import com.wipro.fhir.repo.mongo.ndhm_response.NDHMResponseRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.service.ndhm.Common_NDHMService;
import com.wipro.fhir.service.ndhm.GenerateSession_NDHMService;
import com.wipro.fhir.service.patient_data_handler.PatientDataGatewayService;
import com.wipro.fhir.service.resource_gateway.DiagnosticReportRecord;
import com.wipro.fhir.service.resource_gateway.OPConsultRecordBundle;
import com.wipro.fhir.service.resource_gateway.PrescriptionRecordBundle;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;

/***
 * 
 * @author NE298657
 *
 */

@Service
@PropertySource("classpath:application.properties")
public class CommonServiceImpl implements CommonService {

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Value("${patient-search-page-size}")
	private String patient_search_page_size;
	
	@Value("${abhaMode}")
	private String abhaMode;

	private static String authKey;
	private UUID uuid;

	//public static String NDHM_AUTH_TOKEN;
	//public static Long NDHM_TOKEN_EXP;
	//public static String NDHM_OTP_TOKEN;

	@Value("${clientID}")
	private String clientID;

	@Value("${clientSecret}")
	private String clientSecret;

	@Value("${ndhmuserAuthenticate}")
	private String ndhmUserAuthenticate;

	@Value("${generateABDM_NotifySMS}")
	private String generateABDM_NotifySMS;
	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private APIChannel aPIChannel;
	@Autowired
	private AMRIT_ResourceMongoRepo aMRIT_ResourceMongoRepo;
	

	@Autowired
	private PatientCareContextsMongoRepo patientCareContextsMongoRepo;

	@Autowired
	private OPConsultRecordBundle oPConsultRecordBundle;
	@Autowired
	private PrescriptionRecordBundle prescriptionBundle;
	@Autowired
	private DiagnosticReportRecord diagnosticReportRecordBundle;

	@Autowired
	private TempCollectionRepo tempCollectionRepo;
	@Autowired
	private NDHMResponseRepo nDHMResponseRepo;
	@Autowired
	private PatientDemographicModel_NDHM_Patient_Profile_Repo patientDemographicModel_NDHM_Patient_Profile_Repo;

	@Autowired
	private PatientDataGatewayService patientDataGatewayService;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GenerateSession_NDHMService generateSession_NDHM;
	private static int ACCEPTED = 202;
	@Autowired
	private PatientDemographic patientDemographic;
	@Autowired
	private Common_NDHMService common_NDHMService;
	
	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Override
	public String processResourceOperation() throws FHIRException {
		String response = null;
		// list of patient eligible for resource creation
		List<PatientEligibleForResourceCreation> pList = getPatientListForResourceEligible();
		logger.info("No of records available to create FHIR in last 2 dagetPatientListForResourceEligibleys : " + pList.size());
		ResourceRequestHandler resourceRequestHandler;
		for (PatientEligibleForResourceCreation p : pList) {

			logger.info("Bundle creation is started for BenID : " + p.getBeneficiaryId() + " | BenRegID : "
					+ p.getBeneficiaryRegID() + " | VisitCode : " + p.getVisitCode());

			resourceRequestHandler = new ResourceRequestHandler();
			resourceRequestHandler.setBeneficiaryID(p.getBeneficiaryId());
			resourceRequestHandler.setVisitCode(p.getVisitCode());
			resourceRequestHandler.setBeneficiaryRegID(p.getBeneficiaryRegID());

// ----------------------------------------------------------------------------------------------
			try {

				logger.info("*****Fetch beneficiary Id: " + resourceRequestHandler.getBeneficiaryRegID());
				List<Object[]> rsObjList = patientEligibleForResourceCreationRepo
						.callPatientDemographicSP(resourceRequestHandler.getBeneficiaryRegID());
				logger.info("*****Fetch beneficiary Id response recevied :", rsObjList);

				PatientDemographic patientDemographicOBJ = patientDemographic.getPatientDemographic(rsObjList);
				logger.info("*****Fetch patient after fetching demographics");
				if (patientDemographicOBJ != null) {
					addCareContextToMongo(patientDemographicOBJ, p);
					logger.info("*****Add to mongo success done");
					if (patientDemographicOBJ.getPreferredPhoneNo() != null)
						sendAbdmAdvSMS(patientDemographicOBJ.getPreferredPhoneNo());
					else
						throw new FHIRException("Advertisement sms could not be sent as beneficiary phone no not found");
				}
				else
					throw new FHIRException("Beneficiary not found, benRegId = " +resourceRequestHandler.getBeneficiaryRegID());

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
// ----------------------------------------------------------------------------------------------

			// 1. OP consult resource bundle
			int i = oPConsultRecordBundle.processOPConsultRecordBundle(resourceRequestHandler, p);
			// 2. diagnostic report record budle
			int j = diagnosticReportRecordBundle.processDiagnosticReportRecordBundle(resourceRequestHandler, p);
			// 3. prescription Bundle
			int k = prescriptionBundle.processPrescriptionRecordBundle(resourceRequestHandler, p);

			if (i > 0 && j > 0 && k > 0) {

				// update the processed flag in trigger table
				p.setProcessed(true);
				PatientEligibleForResourceCreation resultSet = patientEligibleForResourceCreationRepo.save(p);
				if (resultSet != null && resultSet.getId().compareTo(BigInteger.ZERO) > 0)
					logger.info("processed flag updated successfully after FHIR resource creation");

				response = "Bundle creation is success for BenID : " + p.getBeneficiaryId() + " | BenRegID : "
						+ p.getBeneficiaryRegID() + " | VisitCode : " + p.getVisitCode();

				logger.info("Bundle creation is success for BenID : " + p.getBeneficiaryId() + " | BenRegID : "
						+ p.getBeneficiaryRegID() + " | VisitCode : " + p.getVisitCode());
			}

			// adv SMS - ABDM notify2 API

		}
		return response;
	}

	// process patient profile creation for AMRIT beneficiary
	@Override
	public String processPatientProfileCreationAMRIT() throws FHIRException {
		String resultSet = patientDataGatewayService.generatePatientProfileAMRIT_SaveTo_Mongo(getAuthKey());
		if (resultSet != null)
			logger.info("patient profiles created : " + resultSet);
		else
			logger.info("No record for patient profile creation");
		return resultSet;
	}

	@Override
	public List<PatientEligibleForResourceCreation> getPatientListForResourceEligible() throws FHIRException {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);

		Timestamp t = new Timestamp(cal.getTimeInMillis());

		List<PatientEligibleForResourceCreation> pList = patientEligibleForResourceCreationRepo
				.getPatientListForResourceCreation(t);

		return pList;
	}

	@Override
	public String getAuthKey() throws FHIRException {
//		if (authKey == null)
		authKey = aPIChannel.userAuthentication();

		return authKey;
	}

	@Override
	public int saveResourceToMongo(AMRIT_ResourceMongo aMRIT_Resource) throws FHIRException {
		AMRIT_ResourceMongo resultSet = aMRIT_ResourceMongoRepo.save(aMRIT_Resource);

		if (resultSet.getId() != null)
			return 1;
		else
			return 0;
	}

//	private String updateProcessedFlagPostResourceProcessing() {
//		return null;
//	}

	@Override
	public String getUUID() {
		uuid = UUID.randomUUID();
		return uuid.toString();
	}

	// 31-03-2021
	// @Override
	public int addCareContextToMongo(PatientDemographic pDemo, PatientEligibleForResourceCreation pVisit)
			throws FHIRException {
		int response = 0;

		if (pDemo != null && pVisit != null) {

//			JsonObject jsnOBJ = new JsonObject();
//			JsonParser jsnParser = new JsonParser();
//			JsonElement jsnElmnt = jsnParser.parse(requestObj);
//			jsnOBJ = jsnElmnt.getAsJsonObject();

			PatientCareContextsStringOBJ patientCareContextsStringOBJ = new PatientCareContextsStringOBJ();

			// wrong variable name in request obj for benregid, need to correct in main
			// request obj first
//			Long benID = null;
//			Long benRegID = null;
//			Long visitCode = null;
//
//			if (jsnOBJ.has("beneficiaryID") && jsnOBJ.get("beneficiaryID") != null)
//				benRegID = jsnOBJ.get("beneficiaryID").getAsLong();
//			if (jsnOBJ.has("visitCode") && jsnOBJ.get("visitCode") != null)
//				visitCode = jsnOBJ.get("visitCode").getAsLong();
//			String healthID = jsnOBJ.get("healthID").getAsString();
//			String healthIDNumber = jsnOBJ.get("healthIdNumber").getAsString();
//			String visitCategory = jsnOBJ.get("visitCategory").getAsString();
//			String phoneNo;
//			String gender;
//			String yearOfBirth;
//			String name;
//			String email;

			// get benid
//			if (benRegID != null)
//				benID = benHealthIDMappingRepo.getBenID(benRegID);
			
			// fetch abdm facility id  
			logger.info("********t_benvisistData fetch request pvisit data :" ,  pVisit);

			List<Object[]> res = benHealthIDMappingRepo.getAbdmFacilityAndlinkedDate(pVisit.getVisitCode());
			
			// check care context record in mongo against beneficiaryID
			ArrayList<CareContexts> ccList = new ArrayList<>();

			CareContexts cc = new CareContexts();
			logger.info("********t_benvisistData fetch response : {}", res);

			cc.setReferenceNumber(pVisit.getVisitCode() != null ? pVisit.getVisitCode().toString() : null);
			cc.setDisplay(pVisit.getVisitCategory() != null ? pVisit.getVisitCategory().toString() : null);	
			Object[] resData = null;
			if (res.get(0) != null) {
				resData = res.get(0);
			cc.setAbdmFacilityId(resData[0] != null ? resData[0].toString() : null );
			cc.setCareContextLinkedDate(resData[1] != null ? resData[1].toString() : null);
			}
			
			logger.info("********data to be saved in mongo :" ,  cc);
			PatientCareContexts pcc;
			PatientCareContexts resultSet = null;


			if (pDemo.getBeneficiaryID() != null) {
				pcc = patientCareContextsMongoRepo.findByIdentifier(pDemo.getBeneficiaryID().toString());

				if (pcc != null && pcc.getIdentifier() != null) {
					ccList = pcc.getCareContextsList();
					ccList.add(cc);
					pcc.setCareContextsList(ccList);
					resultSet = patientCareContextsMongoRepo.save(pcc);

				} else {
					pcc = new PatientCareContexts();
					pcc.setCaseReferenceNumber(pDemo.getBeneficiaryID().toString());
					pcc.setIdentifier(pDemo.getBeneficiaryID().toString());
					if (pDemo.getGenderID() != null) {
						switch (pDemo.getGenderID()) {
						case 1:
							pcc.setGender("M");
							break;
						case 2:
							pcc.setGender("F");
							break;

						case 3:
							pcc.setGender("O");
							break;

						default:
							break;
						}
					}
					if (pDemo.getName() != null)
						pcc.setName(pDemo.getName());
					if (pDemo.getDOB() != null)
						pcc.setYearOfBirth(pDemo.getDOB().toString().split("-")[0]);
					if (pDemo.getPreferredPhoneNo() != null)
						pcc.setPhoneNumber(pDemo.getPreferredPhoneNo());
					if (pDemo.getHealthID() != null)
						pcc.setHealthId(pDemo.getHealthID());
					if (pDemo.getHealthIdNo() != null)
						pcc.setHealthNumber(pDemo.getHealthIdNo());
					ccList.add(cc);
					pcc.setCareContextsList(ccList);
					// save carecontext back to mongo
					resultSet = patientCareContextsMongoRepo.save(pcc);
				}

				if (resultSet != null && resultSet.get_id() != null)
					response = 1;
			}

		}
		return response;
	}

	@Deprecated
	@Override
	public String ndhmUserAuthenticate() throws FHIRException {

		Authorize obj = new Authorize();
		String res = "";
		try {
			obj.setClientId(clientID);
			obj.setClientSecret(clientSecret);
			String requestOBJ = new Gson().toJson(obj);
			logger.info("NDHM user authenticate API request Obj " + requestOBJ);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			String responseStrLogin = httpUtils.post(ndhmUserAuthenticate, requestOBJ, headers);
			if (responseStrLogin != null) {
				JsonObject jsnOBJ = new JsonObject();
				JsonParser jsnParser = new JsonParser();
				JsonElement jsnElmnt = jsnParser.parse(responseStrLogin);
				jsnOBJ = jsnElmnt.getAsJsonObject();
				//NDHM_AUTH_TOKEN = "Bearer" + " " + jsnOBJ.get("accessToken").getAsString();
				Integer expiry = jsnOBJ.get("expiresIn").getAsInt();
				double time = expiry / 60;
				Date date = new Date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				Calendar ndhmCalendar = Calendar.getInstance();
				ndhmCalendar.setTime(sqlDate);
				ndhmCalendar.add(Calendar.MINUTE, (int) time);
			
				res = "success";
			} else
				res = "Error while accessing authenticate API";
		} catch (Exception e) {
			throw new FHIRException("Error while accessing authenticate API " + e);
		}
		return res;
	}

	@Override
	public int saveTempResourceToMongo(TempCollection tempCollection) throws FHIRException {
		if (tempCollection != null) {

			ResourceRequestHandler rrh = new ResourceRequestHandler();
			rrh.setBeneficiaryRegID(tempCollection.getBeneficiaryRegID());
			rrh.setVisitCode(tempCollection.getVisitCode());

			List<TempCollection> resultList = fetchTempResourceFromMongo(rrh);

			if (resultList != null && resultList.size() > 0)
				tempCollection.setId(resultList.get(0).getId());

			TempCollection resultSet = tempCollectionRepo.save(tempCollection);

			if (resultSet.getId() != null)
				return 1;
			else
				return 0;
		} else
			throw new FHIRException("Null data");
	}

	@Override
	public List<TempCollection> fetchTempResourceFromMongo(ResourceRequestHandler resourceRequestHandler)
			throws FHIRException {
		List<TempCollection> resultSet = tempCollectionRepo.findByBeneficiaryRegIDAndVisitCode(
				resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		return resultSet;
	}

	/***
	 * @author SH20094090
	 * @return
	 * 
	 * 		get the UUID and isoTimestamp for NDMH API's
	 */
	@Deprecated
	@Override
	public NDHMRequest getRequestIDAndTimeStamp() {
		NDHMRequest obj = new NDHMRequest();

		// get ISO time stamp
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		obj.setTimestamp(nowAsISO);

		// generate UUID
		UUID uuid = UUID.randomUUID();
		String requestId = uuid.toString();
		obj.setRequestId(requestId);

		return obj;
	}

	/***
	 * @author SH20094090
	 * @param requestID
	 * @return
	 * @throws FHIRException
	 * 
	 *                       hitting the MongoDB 5 Times and resting for 2 seconds
	 *                       in every hit.
	 */
	@Deprecated
	@Override
	public String getMongoNDHMResponse(String requestID) throws FHIRException {
		String response = null;
		NDHMResponse nDHMResponse = new NDHMResponse();
		try {

			int count = 0;
			while (count < 5) {
				nDHMResponse = getResponseMongo(requestID);
				if (nDHMResponse != null)
					break;
				count++;
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			throw new FHIRException("failure " + e);
		}

		if (nDHMResponse != null)
			response = nDHMResponse.getResponseData();
		else
			response = "failure";

		return response;

	}

	/***
	 * @author SH20094090
	 * @param reqID
	 * @return
	 * 
	 * 		hitting MongoDB
	 */
	@Deprecated
	NDHMResponse getResponseMongo(String reqID) {

		// Query for extracting data from MongoDB
//		Query query = new Query();
//		query.addCriteria(Criteria.where("name").is("Eric"));
//		List<User> users = mongoTemplate.find(query, User.class);
		NDHMResponse res = nDHMResponseRepo.findByRequestID(reqID);
		if (res != null) {
			return res;
		} else
			return null;
	}

	@Override
	public List<PatientDemographicModel_NDHM_Patient_Profile> savePatientProfileDataToMongo(
			List<PatientDemographicModel_NDHM_Patient_Profile> patientProfile_list) throws FHIRException {

		List<PatientDemographicModel_NDHM_Patient_Profile> resultSet = (List<PatientDemographicModel_NDHM_Patient_Profile>) patientDemographicModel_NDHM_Patient_Profile_Repo
				.saveAll(patientProfile_list);

		return resultSet;
	}

	@Override
	public Page<PatientDemographicModel_NDHM_Patient_Profile> searchPatientProfileFromMongo(
			ResourceRequestHandler resourceRequestHandler) throws FHIRException {

		if (resourceRequestHandler.getPageNo() != null && resourceRequestHandler.getPageNo() >= 0) {
			PageRequest pr;
			if (patient_search_page_size != null)
				pr = PageRequest.of(resourceRequestHandler.getPageNo(), Integer.valueOf(patient_search_page_size));
			else
				pr = PageRequest.of(resourceRequestHandler.getPageNo(), 10);

			if (resourceRequestHandler != null && resourceRequestHandler.getHealthId() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.findByHealthId(resourceRequestHandler.getHealthId(), pr);
			if (resourceRequestHandler != null && resourceRequestHandler.getHealthIdNumber() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.findByHealthIdNumber(resourceRequestHandler.getHealthIdNumber(), pr);
			if (resourceRequestHandler != null && resourceRequestHandler.getAmritId() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.findByAmritId(resourceRequestHandler.getAmritId(), pr);
			if (resourceRequestHandler != null && resourceRequestHandler.getBeneficiaryID() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.findByAmritId(resourceRequestHandler.getBeneficiaryID().toString(), pr);
			if (resourceRequestHandler != null && resourceRequestHandler.getExternalId() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.findByExternalId(resourceRequestHandler.getExternalId(), pr);

			if (resourceRequestHandler != null && resourceRequestHandler.getPhoneNo() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.searchPatientByPhoneNo(resourceRequestHandler.getPhoneNo(), pr);

			if (resourceRequestHandler != null && resourceRequestHandler.getVillage() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.searchPatientByAddressVillage(resourceRequestHandler.getVillage(), pr);
			if (resourceRequestHandler != null && resourceRequestHandler.getDistrict() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.searchPatientByAddressDistrict(resourceRequestHandler.getDistrict(), pr);
			if (resourceRequestHandler != null && resourceRequestHandler.getState() != null)
				return patientDemographicModel_NDHM_Patient_Profile_Repo
						.searchPatientByAddressState(resourceRequestHandler.getState(), pr);
		} else
			throw new FHIRException("page no is required");

		return null;
	}

	/***
	 * @author SH20094090
	 * @param phoneNo
	 * @throws FHIRException
	 * @purpose Send ABDM advertisement SMS to beneficiary
	 */
	public void sendAbdmAdvSMS(String phone) throws FHIRException {
		try {
			String ndhmAuthToken = generateSession_NDHM.getNDHMAuthToken();
			HIP hip = new HIP("Piramal Swasthya", clientID);
			NDHMRequest obj = common_NDHMService.getRequestIDAndTimeStamp();
			Notification notification = new Notification(phone, hip);
			SMSNotify smsNotify = new SMSNotify(obj.getRequestId(), obj.getTimestamp(), notification);
			String requestOBJ = new Gson().toJson(smsNotify);
			logger.info("NDHM_FHIR Generate Notify SMS request Obj: " + requestOBJ);
			if(abhaMode !=null && !(abhaMode.equalsIgnoreCase("abdm") || abhaMode.equalsIgnoreCase("sbx")))
				abhaMode="sbx";
			HttpHeaders headers = common_NDHMService.getHeaders(ndhmAuthToken, abhaMode);
			ResponseEntity<String> responseEntity = httpUtils.postWithResponseEntity(generateABDM_NotifySMS, requestOBJ,
					headers);
			String responseStrLogin = common_NDHMService.getStatusCode(responseEntity);
			if (Integer.parseInt(responseStrLogin) == ACCEPTED) {
				logger.info("ABDM Notify SMS successfully sent for phone no " + phone);
			} else
				logger.info("Error while sending ABDM Notify SMS for phone no " + phone + " status code:"
						+ responseStrLogin);
		} catch (Exception e) {
			logger.error("Error while sending ABDM Notify SMS for phone no " + phone + " failed with exception "
					+ e.getMessage());
		}

	}

}
