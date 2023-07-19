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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.ClinicalFeedDataLog;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.EncounterFullRepresentation;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.ClinicalFeedDataLogRepo;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.EncounterFullRepresentationRepo;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
@PropertySource("classpath:application.properties")
public class ClinicalFeedWorker {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Value("${atomsFeedStartPage}")
	private int atomsFeedStartPage;

	@Value("${feedAuthUserName}")
	private String userName;
	@Value("${feedAuthPassword}")
	private String password;

	@Value("${parentUrl}")
	private String parentUrl;
	@Value("${atomFeedURLPatientEncounter}")
	private String atomFeedURLPatientEncounter;

	HttpHeaders headers;

	@Autowired
	HttpUtils httpUtils;

	@Autowired
	private ClinicalFeedDataLogRepo clinicalFeedDataLogRepo;
	@Autowired
	private EncounterFullRepresentationRepo encounterFullRepresentationRepo;

	public String encounterFeedManager() throws IllegalArgumentException, FeedException, IOException, FHIRException {
		int pointer = 0;
		// 1. read last successed feed entry
		// feedDataLogRepo
		ClinicalFeedDataLog feed = clinicalFeedDataLogRepo.findOneByEntrySuccessOrderByIdDesc(true);

		// check last entry's self and call read feed method
		if (feed != null && feed.getId() != null && feed.getLinkSelf() != null) {

			String[] arr = feed.getLinkSelf().split("/");
			pointer = Integer.parseInt(arr[arr.length - 1]);

		} else if (feed == null
				|| (feed.getLinkSelf() == null && feed.getLinkVia() == null && feed.getLinkPrevArchive() == null)) {
			pointer = atomsFeedStartPage;
		}
		List<EncounterFullRepresentation> encounterList = readPatientEncounterFeeds(parentUrl,
				atomFeedURLPatientEncounter, pointer, feed);
		logger.info(encounterList.size() + " clinical entry processed successfully");
		
		return new Gson().toJson(encounterList);
	}

	public List<EncounterFullRepresentation> readPatientEncounterFeeds(String parentUrl, String atomFeedURL,
			int pointer, ClinicalFeedDataLog feedDataLog) {

		List<EncounterFullRepresentation> returnList = new ArrayList<>();

		String responseEntity = null;
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
				feedUrl = new URL(parentUrl + atomFeedURLPatientEncounter + pointer);
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
							String encounterURL = getPatientEncounterURL_CData(list.get(0).getValue());
							responseEntity = httpUtils.getPatientDataFromFeed(parentUrl + encounterURL, getHeaders());

							if (responseEntity != null) {
								EncounterFullRepresentation encounterFullRepresentation = InputMapper.gson()
										.fromJson(responseEntity, EncounterFullRepresentation.class);

								if (encounterFullRepresentation.getPatientId() != null) {
									EncounterFullRepresentation resultSet = encounterFullRepresentationRepo
											.save(encounterFullRepresentation);

									// if above operation is success, now its time log a success event
									if (feedPageSuccess == entries.size())
										feedPageCompleted = true;

									if (resultSet != null) {
										logFeedEntryCompletionStatus(feed.getUri(), feed.getTitle(), feed.getAuthor(),
												syndEntry, feedLink, encounterURL, (parentUrl + encounterURL), true,
												feedPageCompleted);
										returnList.add(encounterFullRepresentation);
									} else
										throw new Exception("Error in saving clinical data for patient : "
												+ encounterFullRepresentation.getPatientId());
								} else
									throw new Exception("Patient ID not available");
							}

						}
						feedPageSuccess++;

					} catch (HttpClientErrorException e) {
						
						logger.error("error in processing encounter entry : " + syndEntry.getUri() + " || link - "
								+ syndEntry.getLink() + " error_message : " + e.getMessage());

						// log failed usecase - failed entry parsing
						logFeedEntryCompletionStatus(feed.getUri(), feed.getTitle(), feed.getAuthor(), syndEntry,
								feedLink, null, null, false, false);
						feedPageSuccess++;
					} catch (Exception e) {
					
						logger.error("error in processing encounter entry : " + syndEntry.getUri() + " || link - "
								+ syndEntry.getLink() + " error_message : " + e.getMessage());

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

		return returnList;
	}

	String getPatientEncounterURL_CData(String s) {
		int index = s.indexOf("CDATA");
		index += 5;
		return s.substring(index + 1, s.length() - 3);
	}

	private void logFeedEntryCompletionStatus(String feedID, String title, String author, SyndEntry syndEntry,
			List<SyndLink> feedLink, String entry_cdata, String encounterFull_URL, Boolean status,
			Boolean feedPageCompleteStatus) {
		ClinicalFeedDataLog feedDataLog = new ClinicalFeedDataLog();
		try {

			if (feedID != null)
				feedDataLog.setFeedID(feedID);

			if (syndEntry != null && syndEntry.getUri() != null)
				feedDataLog.setEntryID(syndEntry.getUri());

			if (entry_cdata != null)
				feedDataLog.setEntryContent_cdata(entry_cdata);
			if (encounterFull_URL != null)
				feedDataLog.setEntryContentEncounterFull_URL(encounterFull_URL);
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
			
			feedDataLog = clinicalFeedDataLogRepo.save(feedDataLog);
		} catch (Exception e) {
			logger.info("Error while saving log:" + e.getMessage());
		}
		
	}

	public HttpHeaders getHeaders() {
		String decryptUserName = userName;
		String decryptPassword = password;

		if (headers != null)
			return headers;
		else {
			headers = new HttpHeaders();
			
			String plainCreds = decryptUserName + ":" + decryptPassword;
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);

			headers.add("Authorization", "Basic " + base64Creds);
		}
		return headers;
	}
}
