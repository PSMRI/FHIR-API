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
package com.wipro.fhir.service.atoms.feed.bahmni;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.FeedDataLog;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPatientFullRepresentation;
import com.wipro.fhir.data.atoms.feed.bahmni.patient.OpenMRSPatientIdentifier;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile.Profile;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile.Profile.Patient.Address;
import com.wipro.fhir.repo.atoms.feed.bahmni.patient.FeedDataLogRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
@Component
@PropertySource("classpath:application.properties")
public class PatientFeedWorker {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Value("${atomsFeedStartPage}")
	private int atomsFeedStartPage;

	@Value("${parentUrl}")
	private String parentUrl;
	@Value("${atomFeedURLPatientDemographic}")
	private String atomFeedURLPatientDemographic;
	@Value("${feedAuthUserName}")
	private String userName;
	@Value("${feedAuthPassword}")
	private String password;

	@Autowired
	HttpUtils httpUtils;
	@Autowired
	private FeedDataLogRepo feedDataLogRepo;
	@Autowired
	private PatientDemographicModel_NDHM_Patient_Profile_Repo patientDemographicModel_NDHM_Patient_Profile_Repo;
	@Autowired
	private CommonService commonService;

	HttpHeaders headers;

	public List<OpenMRSPatientFullRepresentation> readPatientDemographicFeeds(String parentUrl, String atomFeedURL,
			int pointer, FeedDataLog feedDataLog) {
		String responseEntity = null;
		List<OpenMRSPatientFullRepresentation> patientList = new ArrayList<OpenMRSPatientFullRepresentation>();

		int feedPageSuccess;
		Boolean feedPageCompleted;
		int pendingFeedEntry;

		List<SyndContent> list = new ArrayList<>();
		URL feedUrl;
		SyndFeedInput input;
		SyndFeed feed;
		List<SyndEntry> entries;
		List<SyndLink> feedLink;
		boolean nextFeed = false;
		while (pointer > 0) {
			try {
				feedUrl = new URL(parentUrl + atomFeedURL + pointer);
				input = new SyndFeedInput();
				feed = input.build(new XmlReader(feedUrl));
				entries = feed.getEntries();
				feedLink = feed.getLinks();

				feedPageSuccess = 1;
				feedPageCompleted = false;
				pendingFeedEntry = 0;

				for (SyndEntry syndEntry : entries) {
					try {
						list = new ArrayList<>();
						if (feedDataLog != null) {
							if (pendingFeedEntry == 1) {
								list = syndEntry.getContents();
							} else if (syndEntry.getUri().equalsIgnoreCase(feedDataLog.getEntryID())) {
								pendingFeedEntry = 1;
								feedDataLog = null;
							} else if (nextFeed) {
								list = syndEntry.getContents();
							}
						} else {
							list = syndEntry.getContents();
						}
						if (list != null && list.size() > 0) {
							String patientURL = getPatientURL_CData(list.get(0).getValue());
							responseEntity = httpUtils.getPatientDataFromFeed(parentUrl + patientURL, getHeaders());

							if (responseEntity != null) {
								OpenMRSPatientFullRepresentation patientFullRepresentation = InputMapper.gson()
										.fromJson(responseEntity, OpenMRSPatientFullRepresentation.class);

								// create object of class PatientDemographicModel_NDHM_Patient_Profile from
								// class OpenMRSPatientFullRepresentation
								PatientDemographicModel_NDHM_Patient_Profile patient = mapFeedDataToModel(
										patientFullRepresentation);
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								List<PatientDemographicModel_NDHM_Patient_Profile> resultList = patientDemographicModel_NDHM_Patient_Profile_Repo
										.findByExternalId(patient.getExternalId());
								if (resultList != null && resultList.size() > 0) {
									PatientDemographicModel_NDHM_Patient_Profile obj = resultList
											.get(resultList.size() - 1);
									patient.setId(obj.getId());
									patient.setAmritId(obj.getAmritId());
									patient.setLastModDate(timestamp);
								} else {
									patient.setCreatedDate(timestamp);
									patient.setLastModDate(timestamp);
								}
								// . save to mongo - already function is written in common service
								patient = patientDemographicModel_NDHM_Patient_Profile_Repo.save(patient);

								// if above operation is success, now its time log a success event
								if (feedPageSuccess == entries.size())
									feedPageCompleted = true;
								if (patient != null && patient.getId() != null)
									logFeedEntryCompletionStatus(feed.getUri(), feed.getTitle(), feed.getAuthor(),
											syndEntry, feedLink, patientURL, (parentUrl + patientURL), true,
											feedPageCompleted);

								// temp code
								patientList.add(patientFullRepresentation);
							}

						}
						feedPageSuccess++;

					} catch (HttpClientErrorException e) {
						
						logger.error("error in processing entry : " + syndEntry.getUri() + " error_message : "
								+ e.getMessage());

						// log failed usecase - failed entry parsing
						logFeedEntryCompletionStatus(feed.getUri(), feed.getTitle(), feed.getAuthor(), syndEntry,
								feedLink, null, null, false, false);

						feedPageSuccess++;
					} catch (FHIRException e) {
					
						logger.error("error in processing entry : " + syndEntry.getUri() + " error_message : "
								+ e.getMessage());

						// log failed usecase - failed entry parsing
						logFeedEntryCompletionStatus(feed.getUri(), feed.getTitle(), feed.getAuthor(), syndEntry,
								feedLink, null, null, false, false);

						feedPageSuccess++;
					}
				}

				int tempPointer = 0;
				for (SyndLink link : feedLink) {

					if (link.getRel() != null && link.getHref() != null
							&& link.getRel().equalsIgnoreCase("next-archive")) {
						String[] arr = link.getHref().split("/");
						tempPointer = Integer.parseInt(arr[arr.length - 1]);
						break;

					}
				}
				pointer = tempPointer;

			} catch (IllegalArgumentException e) {
				pointer = 0;
				
			} catch (FeedException e) {
				pointer = 0;
				
			} catch (IOException e) {
				pointer = 0;
				
			} catch (Exception e) {
				pointer = 0;
			}
			nextFeed = true;
		}

		return patientList;
	}

	String getPatientURL_CData(String s) {
		int index = s.indexOf("CDATA");
		index += 5;
		return s.substring(index + 1, s.length() - 3);
	}

	public HttpHeaders getHeaders() {
		if (headers != null)
			return headers;
		else {
			headers = new HttpHeaders();
			// String plainCreds = "superman:Admin123";
			String plainCreds = userName + ":" + password;
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);

			headers.add("Authorization", "Basic " + base64Creds);
		}
		return headers;
	}

	// check last read feed/entry
	// start reading a feed
	// keeps the meta data of feed reading
	// feed page, entry-info (id, index)
	// success - fail use case log
	public String patientFeedManager() throws IllegalArgumentException, FeedException, IOException, FHIRException {
//		String parentUrl = "https://demo.mybahmni.org";
//		String atomFeedURL = "/openmrs/ws/atomfeed/patient/";
		int pointer = 0;
		// 1. read last successed feed entry
		// feedDataLogRepo
		FeedDataLog feed = feedDataLogRepo.findOneByEntrySuccessOrderByIdDesc(true);

		// check last entry's self and call read feed method
		if (feed != null && feed.getId() != null && feed.getLinkSelf() != null) {

			String[] arr = feed.getLinkSelf().split("/");
			pointer = Integer.parseInt(arr[arr.length - 1]);

		} else if (feed == null
				|| (feed.getLinkSelf() == null && feed.getLinkVia() == null && feed.getLinkPrevArchive() == null)) {
			pointer = atomsFeedStartPage;
		}
		List<OpenMRSPatientFullRepresentation> patientList = readPatientDemographicFeeds(parentUrl,
				atomFeedURLPatientDemographic, pointer, feed);
		logger.info(patientList.size() + "patient entry processed successfully");
		return patientList.size() + "patient entry processed successfully";
	}

	private void logFeedEntryCompletionStatus(String feedID, String title, String author, SyndEntry syndEntry,
			List<SyndLink> feedLink, String entry_cdata, String patientFull_URL, Boolean status,
			Boolean feedPageCompleteStatus) {
		FeedDataLog feedDataLog = new FeedDataLog();
		try {

			if (feedID != null)
				feedDataLog.setFeedID(feedID);

			if (syndEntry != null && syndEntry.getUri() != null)
				feedDataLog.setEntryID(syndEntry.getUri());

			if (entry_cdata != null)
				feedDataLog.setEntryContent_cdata(entry_cdata);
			if (patientFull_URL != null)
				feedDataLog.setEntryContentPatientFull_URL(patientFull_URL);
			if (feedLink != null)
				feedDataLog.setFeedLink(feedLink.toString());
			if (syndEntry != null)
				feedDataLog.setEntry(syndEntry.toString());

			for (SyndLink link : feedLink) {


				if (link.getRel() != null && link.getHref() != null) {
					switch (link.getRel().toLowerCase()) {
					case "self":
						feedDataLog.setLinkSelf(link.getHref());
						break;
					case "via":
						feedDataLog.setLinkVia(link.getHref());
						break;
					case "next-archive":
						feedDataLog.setLinkNextArchive(link.getHref());
						break;
					case "prev-archive":
						feedDataLog.setLinkPrevArchive(link.getHref());
						break;
					default:
						
					}
				}
			}

			if (status != null)
				feedDataLog.setEntrySuccess(status);
			if (feedPageCompleteStatus != null)
				feedDataLog.setFeedSuccess(feedPageCompleteStatus);
			
			feedDataLog = feedDataLogRepo.save(feedDataLog);
		} catch (Exception e) {
			logger.info("Error while saving log:" + e.getMessage());
		}
		// feedDataLog.set
		// feedDataLogRepo
	}

	PatientDemographicModel_NDHM_Patient_Profile mapFeedDataToModel(OpenMRSPatientFullRepresentation data) {

		PatientDemographicModel_NDHM_Patient_Profile obj = new PatientDemographicModel_NDHM_Patient_Profile();
		try {
			obj.setRequestId(commonService.getUUID());
			obj.setTimestamp(new Timestamp(System.currentTimeMillis()));
			String identifier = null, identityType = null;
			Map<String, String> map = new HashMap<String, String>();

			List<Map<String, String>> patientIdentifier = new ArrayList<Map<String, String>>();

			if (data != null) {
				List<OpenMRSPatientIdentifier> identifiers = data.getIdentifiers();
				for (OpenMRSPatientIdentifier id : identifiers) {
					if (id.getIdentifier() != null)
						identifier = id.getIdentifier();
					if (id.getIdentifierType().getDisplay() != null)
						identityType = id.getIdentifierType().getDisplay();
					if (identityType.equalsIgnoreCase("patient identifier"))
						obj.setExternalId(identifier);
					map.put(identityType, identifier);

				}
				patientIdentifier.add(map);
				if (data.getPerson() != null) {
					Profile profile = obj.new Profile();
					Patient patient = profile.new Patient();
					patient.setGender(data.getPerson().getGender());
					if (data.getPerson().getPreferredName() != null) {
						patient.setFirstName(data.getPerson().getPreferredName().getGivenName());
						String lastName = "";
						if (data.getPerson().getPreferredName().getMiddleName() != null)
							lastName = lastName + data.getPerson().getPreferredName().getMiddleName();
						if (data.getPerson().getPreferredName().getFamilyName() != null)
							lastName = lastName + data.getPerson().getPreferredName().getFamilyName();
						if (data.getPerson().getPreferredName().getFamilyName2() != null)
							lastName = lastName + data.getPerson().getPreferredName().getFamilyName2();
						if (lastName.equals(""))
							lastName = null;
						patient.setLastName(lastName);
						patient.setName(data.getPerson().getPreferredName().getDisplay());

					}
					if (data.getPerson().getBirthdate() != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(data.getPerson().getBirthdate().getTime());
						patient.setYearOfBirth(String.valueOf(cal.get(Calendar.YEAR)));
						patient.setMonthOfBirth(String.valueOf(cal.get(Calendar.MONTH) + 1));
						patient.setDayOfBirth(String.valueOf(cal.get(Calendar.DATE)));


					}
					patient.setIdentifiers(patientIdentifier);
					Address add = patient.new Address();
					if (data.getPerson().getPreferredAddress() != null) {
						add.setState(data.getPerson().getPreferredAddress().getStateProvince());
						add.setPincode(data.getPerson().getPreferredAddress().getPostalCode());
						add.setDistrict(data.getPerson().getPreferredAddress().getCountyDistrict());
						add.setVillage(data.getPerson().getPreferredAddress().getCityVillage());
					}
					patient.setAddress(add);
					profile.setPatient(patient);
					obj.setProfile(profile);

				}
			}
		} catch (Exception e) {
			logger.info("Error while creating patient object :" + e.getMessage());
		}
		return obj;
	}
}
