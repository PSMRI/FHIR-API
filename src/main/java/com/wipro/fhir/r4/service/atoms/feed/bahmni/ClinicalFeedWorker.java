package com.wipro.fhir.r4.service.atoms.feed.bahmni;

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
import org.springframework.stereotype.Component;
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
import com.wipro.fhir.r4.data.atoms.feed.bahmni.encounter.ClinicalFeedDataLog;
import com.wipro.fhir.r4.data.atoms.feed.bahmni.encounter.EncounterFullRepresentation;
import com.wipro.fhir.r4.repo.atoms.feed.bahmni.encounter.ClinicalFeedDataLogRepo;
import com.wipro.fhir.r4.repo.atoms.feed.bahmni.encounter.EncounterFullRepresentationRepo;
import com.wipro.fhir.r4.utils.CryptoUtil;
import com.wipro.fhir.r4.utils.exception.FHIRException;
import com.wipro.fhir.r4.utils.http.HttpUtils;
import com.wipro.fhir.r4.utils.mapper.InputMapper;

@Service
@Component
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
	private CryptoUtil cryptoUtil;

	@Autowired
	HttpUtils httpUtils;

	@Autowired
	private ClinicalFeedDataLogRepo clinicalFeedDataLogRepo;
	@Autowired
	private EncounterFullRepresentationRepo encounterFullRepresentationRepo;

	public String encounterFeedManager() throws IllegalArgumentException, FeedException, IOException, FHIRException {
//		String parentUrl = "https://demo.mybahmni.org";
//		String atomFeedURL = "/openmrs/ws/atomfeed/patient/";
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
		// return patientList.size() + " entry processed successfully";
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
						// TODO: handle exception
						logger.error("error in processing encounter entry : " + syndEntry.getUri() + " || link - "
								+ syndEntry.getLink() + " error_message : " + e.getMessage());

						// log failed usecase - failed entry parsing
						logFeedEntryCompletionStatus(feed.getUri(), feed.getTitle(), feed.getAuthor(), syndEntry,
								feedLink, null, null, false, false);
						feedPageSuccess++;
					} catch (Exception e) {
						// TODO: handle exception
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
				// TODO: handle exception
			} catch (FeedException e) {
				pointer = 0;
				// TODO: handle exception
			} catch (IOException e) {
				pointer = 0;
				// TODO: handle exception
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
//			System.out.println(link.getRel());
//			System.out.println(link.getHref());

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
						// System.out.println("temp code");
					}
				}
			}

			if (status != null)
				feedDataLog.setEntrySuccess(status);
			if (feedPageCompleteStatus != null)
				feedDataLog.setFeedSuccess(feedPageCompleteStatus);
			// System.out.println(feedDataLog);
			feedDataLog = clinicalFeedDataLogRepo.save(feedDataLog);
		} catch (Exception e) {
			logger.info("Error while saving log:" + e.getMessage());
		}
		// feedDataLog.set
		// feedDataLogRepo
	}

	public HttpHeaders getHeaders() {

		String decryptUserName = cryptoUtil.decrypt(userName);
		String decryptPassword = cryptoUtil.decrypt(password);

		if (headers != null)
			return headers;
		else {
			headers = new HttpHeaders();
			// String plainCreds = "superman:Admin123";
			String plainCreds = decryptUserName + ":" + decryptPassword;
			byte[] plainCredsBytes = plainCreds.getBytes();
			byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
			String base64Creds = new String(base64CredsBytes);

			headers.add("Authorization", "Basic " + base64Creds);
		}
		return headers;
	}
}
