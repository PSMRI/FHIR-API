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
package com.wipro.fhir.service.e_aushdhi;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wipro.fhir.data.e_aushdhi.EAushadhiResponse;
import com.wipro.fhir.data.e_aushdhi.E_AusdhFacilityProcessLog;
import com.wipro.fhir.data.e_aushdhi.ItemMaster;
import com.wipro.fhir.data.e_aushdhi.ItemStockEntry;
import com.wipro.fhir.data.e_aushdhi.ItemStockExit;
import com.wipro.fhir.data.e_aushdhi.M_Facility;
import com.wipro.fhir.data.e_aushdhi.M_ItemCategory;
import com.wipro.fhir.data.e_aushdhi.M_ItemForm;
import com.wipro.fhir.data.e_aushdhi.M_Route;
import com.wipro.fhir.data.e_aushdhi.M_itemfacilitymapping;
import com.wipro.fhir.data.e_aushdhi.SyncDispenseDetailsRequest;
import com.wipro.fhir.data.e_aushdhi.T_PatientIssue;
import com.wipro.fhir.data.request_handler.PatientSearchAPIResponse;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.e_aushdhi.E_AusdhFacilityProcessLogRepo;
import com.wipro.fhir.repo.e_aushdhi.FacilityRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemCategoryRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemFormRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemStockEntryRepo;
import com.wipro.fhir.repo.e_aushdhi.ItemStockExitRepo;
import com.wipro.fhir.repo.e_aushdhi.M_itemfacilitymappingRepo;
import com.wipro.fhir.repo.e_aushdhi.ParkingPlaceRepo;
import com.wipro.fhir.repo.e_aushdhi.PatientIssueRepo;
import com.wipro.fhir.repo.e_aushdhi.RouteRepo;
import com.wipro.fhir.repo.e_aushdhi.VanMasterRepo;
import com.wipro.fhir.repo.healthID.BenHealthIDMappingRepo;
import com.wipro.fhir.service.api_channel.APIChannel;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.http.HttpUtils;
import com.wipro.fhir.utils.mapper.InputMapper;

@Service
public class EAushadhiServiceImpl implements EAushadhiService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Value("${eaushadhiStoreStockDetailsUrl}")
	private String eaushadhiStoreStockDetailsUrl;

	@Value("${eaushadhiStoreStockAckUrl}")
	private String eaushadhiStoreStockAckUrl;

	@Value("${eAushadhiDispensePageSize}")
	private String eAushadhiDispensePageSize;

	@Value("${eAushadhiDummy}")
	private String eAushadhiDummy;

	@Autowired
	private HttpUtils httpUtils;

	@Autowired
	private ItemRepo itemRepo;

	@Autowired
	private M_itemfacilitymappingRepo mItemFacilityMappingRepo;

	@Autowired
	private FacilityRepo facilityRepo;

	@Autowired
	private ItemStockEntryRepo itemStockEntryRepo;

	@Autowired
	private VanMasterRepo vanMasterRepo;

	@Autowired
	private ParkingPlaceRepo parkingPlaceRepo;

	@Autowired
	private ItemFormRepo itemFormRepo;

	@Autowired
	private ItemCategoryRepo itemCategoryRepo;

	@Autowired
	private RouteRepo routeRepo;

	@Autowired
	private PatientIssueRepo patientIssueRepo;

	@Autowired
	private ItemStockExitRepo itemStockExitRepo;

	@Autowired
	private BenHealthIDMappingRepo benHealthIDMappingRepo;

	@Autowired
	private E_AusdhFacilityProcessLogRepo e_AusdhFacilityProcessLogRepo;

	@Autowired
	private APIChannel aPIChannel;

	/** Getting E-aushadhi stock details by passing Facility ID From UI */
	@Override
	public String getEaushadhiStoreDetailsByFacilityID(String request) throws FHIRException {
		String savedStockDetailsStatus = "failure";
		try {
			String ackResp = null;
			M_Facility facilityDetails = InputMapper.gson().fromJson(request, M_Facility.class);
			/** Fetching Facility details */
			M_Facility facilityDet = facilityRepo.geteAushadhiFacilityID(facilityDetails.getFacilityID());

			if (facilityDet != null) {
				if (facilityDet.geteAushadhiFacilityId() != null) {

					E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog;
					List<E_AusdhFacilityProcessLog> resultList = e_AusdhFacilityProcessLogRepo
							.findByAmrithFacilityIdAndEaushadiFacilityId(facilityDet.getFacilityID(),
									facilityDet.geteAushadhiFacilityId());

					if (resultList.size() > 1)
						logger.error("Alert : there are more than 1 record for a facility in log table - "
								+ " amritFId " + facilityDet.getFacilityID() + " eAusdhFId "
								+ facilityDet.geteAushadhiFacilityId());

					if (resultList.size() > 0)
						e_AusdhFacilityProcessLog = resultList.get(resultList.size() - 1);
					else
						e_AusdhFacilityProcessLog = new E_AusdhFacilityProcessLog();

					e_AusdhFacilityProcessLog.setAmrithFacilityId(facilityDet.getFacilityID());
					e_AusdhFacilityProcessLog.setEaushadiFacilityId(facilityDet.geteAushadhiFacilityId());
					e_AusdhFacilityProcessLog.setCreatedBy("manual_job");
					e_AusdhFacilityProcessLog.setModifiedBy("manual_job");

					if (e_AusdhFacilityProcessLog.getStockUpdateAmrit() != null
							&& e_AusdhFacilityProcessLog.getStockUpdateAmrit() == true
							&& e_AusdhFacilityProcessLog.getAcknowledge() != null
							&& e_AusdhFacilityProcessLog.getAcknowledge() == false) {

						 ackResp = sendStockAdditionAckToEAushadhi(1,
						 facilityDet.geteAushadhiFacilityId());


						if (ackResp.equals("1")) {
							e_AusdhFacilityProcessLog.setLastSuccessDate(new Timestamp(System.currentTimeMillis()));
							e_AusdhFacilityProcessLog.setAcknowledge(true);
							e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
							savedStockDetailsStatus = "success";
						} else {
							e_AusdhFacilityProcessLog.setLastFailureDate(new Timestamp(System.currentTimeMillis()));
							e_AusdhFacilityProcessLog.setAcknowledge(false);
							e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
						}

					} else {
						int count = 3;
						while (count > 0) {
							savedStockDetailsStatus = getStoreStockDetailsService(facilityDet);
							if (savedStockDetailsStatus.equals("success")) {
								e_AusdhFacilityProcessLog.setStockUpdateAmrit(true);
								int countAck = 3;
								while (countAck > 0) {
									 ackResp = sendStockAdditionAckToEAushadhi(1,
									 facilityDet.geteAushadhiFacilityId());
//									ackResp = "1";
									if (ackResp.equals("1")) {
										e_AusdhFacilityProcessLog
												.setLastSuccessDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(true);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
										break;
									} else {
										countAck--;
										e_AusdhFacilityProcessLog
												.setLastFailureDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(false);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
									}
								}
								break;
							} else {
								e_AusdhFacilityProcessLog.setStockUpdateAmrit(false);
								int countAck = 3;
								while (countAck > 0) {
									ackResp = sendStockAdditionAckToEAushadhi(2, facilityDet.geteAushadhiFacilityId());
									if (ackResp.equals("1")) {
										e_AusdhFacilityProcessLog
												.setLastSuccessDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(true);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
										break;
									}

									else {
										countAck--;
										e_AusdhFacilityProcessLog
												.setLastFailureDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(false);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
									}
								}
								count--;
							}
						}
					}
				} else
					throw new FHIRException("Facility is not mapped with E-aushadhi");

			} else
				throw new FHIRException("Error while getting facility details");
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new FHIRException(e.getMessage());

		}
		return savedStockDetailsStatus;
	}

	@Override
	public void getStockDetailsFromEAushadhi() {
		String savedStockDetailsStatus = "failure";
		try {
			String ackResp = null;
			/** Fetching e-aushadhi mapped facility details */
			ArrayList<M_Facility> facilityDetails = facilityRepo.getFacilityDetails();

			if (facilityDetails != null && facilityDetails.size() > 0) {

				E_AusdhFacilityProcessLog e_AusdhFacilityProcessLog;
				for (M_Facility facilityRecords : facilityDetails) {



					List<E_AusdhFacilityProcessLog> resultList = e_AusdhFacilityProcessLogRepo
							.findByAmrithFacilityIdAndEaushadiFacilityId(facilityRecords.getFacilityID(),
									facilityRecords.geteAushadhiFacilityId());

					if (resultList.size() > 1)
						logger.error("Alert : there are more than 1 record for a facility in log table - "
								+ " amritFId " + facilityRecords.getFacilityID() + " eAusdhFId "
								+ facilityRecords.geteAushadhiFacilityId());

					if (resultList.size() > 0)
						e_AusdhFacilityProcessLog = resultList.get(resultList.size() - 1);
					else
						e_AusdhFacilityProcessLog = new E_AusdhFacilityProcessLog();

					e_AusdhFacilityProcessLog.setAmrithFacilityId(facilityRecords.getFacilityID());
					e_AusdhFacilityProcessLog.setEaushadiFacilityId(facilityRecords.geteAushadhiFacilityId());
					e_AusdhFacilityProcessLog.setCreatedBy("cron_job");
					e_AusdhFacilityProcessLog.setModifiedBy("cron_Job");
					if (e_AusdhFacilityProcessLog.getStockUpdateAmrit() != null
							&& e_AusdhFacilityProcessLog.getStockUpdateAmrit() == true
							&& e_AusdhFacilityProcessLog.getAcknowledge() != null
							&& e_AusdhFacilityProcessLog.getAcknowledge() == false) {

						 ackResp = sendStockAdditionAckToEAushadhi(1,
								 facilityRecords.geteAushadhiFacilityId());


						if (ackResp.equals("1")) {
							e_AusdhFacilityProcessLog.setLastSuccessDate(new Timestamp(System.currentTimeMillis()));
							e_AusdhFacilityProcessLog.setAcknowledge(true);
							e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
							savedStockDetailsStatus = "success";
						} else {
							e_AusdhFacilityProcessLog.setLastFailureDate(new Timestamp(System.currentTimeMillis()));
							e_AusdhFacilityProcessLog.setAcknowledge(false);
							e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
						}
					} else {
						int count = 3;
						while (count > 0) {
							savedStockDetailsStatus = getStoreStockDetailsService(facilityRecords);
							if (savedStockDetailsStatus.equals("success")) {
								e_AusdhFacilityProcessLog.setStockUpdateAmrit(true);
								int countAck = 3;
								while (countAck > 0) {

									 ackResp = sendStockAdditionAckToEAushadhi(1,
									 facilityRecords.geteAushadhiFacilityId());
									if (ackResp.equals("1")) {
										e_AusdhFacilityProcessLog
												.setLastSuccessDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(true);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
										break;
									} else {
										countAck--;
										e_AusdhFacilityProcessLog
												.setLastFailureDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(false);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);

									}
								}
								break;
							} else {
								e_AusdhFacilityProcessLog.setStockUpdateAmrit(false);
								int countAck = 3;
								while (countAck > 0) {
									ackResp = sendStockAdditionAckToEAushadhi(2,
											facilityRecords.geteAushadhiFacilityId());
									if (ackResp.equals("1")) {
										e_AusdhFacilityProcessLog
												.setLastSuccessDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(true);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);
										break;
									}

									else {
										countAck--;
										e_AusdhFacilityProcessLog
												.setLastFailureDate(new Timestamp(System.currentTimeMillis()));
										e_AusdhFacilityProcessLog.setAcknowledge(false);
										e_AusdhFacilityProcessLogRepo.save(e_AusdhFacilityProcessLog);

									}
								}
								count--;
							}
						}
					}
				}
			} else
				logger.info("No mapped facility records available for e-aushadhi stock entry : "
						+ new Timestamp(System.currentTimeMillis()));
		} catch (Exception e) {
			logger.info(e.getMessage());

		}
	}

	/**
	 * Getting stock details from e-aushadhi by sending facilityID
	 * 
	 * @throws FHIRException
	 */
	public String getStoreStockDetailsService(M_Facility facilityRecords) throws FHIRException {

		EAushadhiResponse[] eAushadhiResp = null;
//		Integer eAushadhiFacilityId = 99927641;
		String response = "success";
		try {

			/** Fetching Item Stock Details from E-Aushadhi */

			HttpHeaders headers = new HttpHeaders();
			ResponseEntity<String> responseEntity = httpUtils.getWithResponseEntity(eaushadhiStoreStockDetailsUrl
					+ facilityRecords.geteAushadhiFacilityId() + "/Phulwaria_Phc-Labor%20Room/10.10.10.234", headers);
			if (responseEntity.getStatusCodeValue() == 200 && responseEntity.hasBody()) {
				String responseEAushadhi = responseEntity.getBody();

				eAushadhiResp = InputMapper.gson().fromJson(responseEAushadhi, EAushadhiResponse[].class);

				logger.info("E-Aushadhi store stock details response" + eAushadhiResp);

				if (eAushadhiResp.length > 0) {

					/** Facility Van & Parking Place ID */
					Integer vanId = null;
					vanId = vanMasterRepo.getvanID(facilityRecords.getFacilityID());

					Integer parkingPlaceId = null;
					parkingPlaceId = parkingPlaceRepo.getParkingPlaceID(facilityRecords.getFacilityID());

					for (EAushadhiResponse data : eAushadhiResp) {

						try {

							if (data.getStockstatus().equals("10")) {

								/** Checking Item already Exist in Amrit or not */

								ItemMaster itemDetails = null;
								itemDetails = itemRepo.checkItemExists(data.getDrugname(),
										facilityRecords.getProviderServiceMapID(), data.getBrandid() + "_EAushadhi");

								if (itemDetails == null || (itemDetails != null && (itemDetails.getIsEaushadi() == null
										|| itemDetails.getIsEaushadi() == false))) {

									/** Adding e-aushadhi item to amrit */
									ItemMaster addedItemDetails = insertItemsInAmrit(data, facilityRecords);

									if (addedItemDetails != null) {

										/** Mapping Items to Main Facility */

										int itemMainFacilityMappingStatus = insertItemFacilityMapping(
												facilityRecords.getMainFacilityID(), addedItemDetails);

										if (itemMainFacilityMappingStatus > 0) {
											/** Mapping Items to Sub Facility */

											mapItemDetailsAndInsertStockToSubFacility(data, facilityRecords, vanId,
													parkingPlaceId, addedItemDetails);
										} else {
											throw new FHIRException(
													"E-Aushadhi Error - Issue with item main facility mapping");
										}

									} else {
										throw new FHIRException(
												"E-Aushadhi Error - Issue while adding stock details in item master");
									}

								}

								else {

									/** Checking Item - Main Facility Mapping */
									M_itemfacilitymapping isItemMainFacilityMapped = mItemFacilityMappingRepo
											.checkItemFacilityMapping(itemDetails.getItemID(),
													facilityRecords.getMainFacilityID(),
													facilityRecords.getProviderServiceMapID());

									int itemMainFacilityMappingStatus = 1;
									if (isItemMainFacilityMapped == null) {
										/** Item - Main Facility Mapping */

										itemMainFacilityMappingStatus = insertItemFacilityMapping(
												facilityRecords.getMainFacilityID(), itemDetails);

									}

									if (itemMainFacilityMappingStatus > 0) {
										/** Checking Item - Sub Facility Mapping */
										M_itemfacilitymapping isItemSubFacilityMapped = mItemFacilityMappingRepo
												.checkItemFacilityMapping(itemDetails.getItemID(),
														facilityRecords.getFacilityID(),
														facilityRecords.getProviderServiceMapID());
										if (isItemSubFacilityMapped != null) {

											/** Item Stock Entry */
											int itemEntryStatus = insertItemStockEntry(facilityRecords.getFacilityID(),
													itemDetails, data, vanId, parkingPlaceId);

											if (itemEntryStatus <= 0) {

												throw new FHIRException(
														"E-Aushadhi Error - Issue with item stock entry");
											}

										} else {

											mapItemDetailsAndInsertStockToSubFacility(data, facilityRecords, vanId,
													parkingPlaceId, itemDetails);
										}
									} else {
										throw new FHIRException(
												"E-Aushadhi Error - Issue with item main facility mapping");
									}

								}

							} else {
								throw new FHIRException(
										"E-Aushadhi Error - Stock rejected, brandId :" + data.getBrandid());
							}

						} catch (Exception e) {
							logger.info(e.getMessage());

						}

					}

				} else {
					throw new FHIRException("E-Aushadhi Update - No new stock from E-Aushadhi");
				}

			} else {

				throw new FHIRException("E-Aushadhi Error while accessing store stock details , response status code :"
						+ responseEntity.getStatusCodeValue());
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
//			response = "failure";
			throw new FHIRException(e.getMessage());
		}

		return response;
	}

	/** Adding Items in Amrit */
	public ItemMaster insertItemsInAmrit(EAushadhiResponse data, M_Facility facilityRecords) throws Exception {

		ItemMaster addedItemDetails;
		try {
			/** Fetching ItemFormID */
			M_ItemForm itemFormDetails = null;
			itemFormDetails = itemFormRepo.getItemFormID(data.getItemtypename());

			/** Fetching RouteID */
			M_Route itemRouteDetails = null;
			itemRouteDetails = routeRepo.getItemRouteID("Oral");

			/** Fetching Item Category */
			String categoryName = null;
			switch (Integer.parseInt(data.getCategory())) {
			case 10:
				categoryName = "drug";
				break;

			case 11:
				categoryName = "surgical";
				break;

			case 12:
				categoryName = "reagents";
				break;

			default:
				break;
			}

			M_ItemCategory itemCategoryDetails = null;
			itemCategoryDetails = itemCategoryRepo.getItemCategoryID(categoryName,
					facilityRecords.getProviderServiceMapID());

			/** EDL or Non-EDL Checking */
			Boolean isEDL = data.getEdl().equalsIgnoreCase("EDL") ? true : false;

			/** Adding E-Aushadhi Items in Amrit */

			ItemMaster itemMasterObj = new ItemMaster();
			itemMasterObj.setItemName(data.getDrugname());
			itemMasterObj.setProviderServiceMapID(facilityRecords.getProviderServiceMapID());
			itemMasterObj.setCreatedBy(facilityRecords.getCreatedBy());
			itemMasterObj.setIsScheduledDrug(false);
			itemMasterObj.setStatus("active");
			itemMasterObj.setComposition(data.getSpecification());
			itemMasterObj.setItemFormID(itemFormDetails.getItemFormID());
			itemMasterObj.setItemCode(data.getBrandid() + "_EAushadhi");
			itemMasterObj.setIsEDL(isEDL);
			itemMasterObj.setRouteID(itemRouteDetails.getRouteID());
			itemMasterObj.setItemCategoryID(itemCategoryDetails.getItemCategoryID());
			itemMasterObj.setIsEaushadi(true);

			addedItemDetails = itemRepo.save(itemMasterObj);

		} catch (Exception e) {
			logger.error(e.toString());
			throw new FHIRException(e.toString());

		}
		return addedItemDetails;

	}

	/** Item Stock Entry */
	public int insertItemStockEntry(Integer amritFacilityID, ItemMaster addedItemDetails,
			EAushadhiResponse eAushadhiResp, Integer vanId, Integer parkingPlaceId) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

		Date date = formatter.parse(eAushadhiResp.getExpdate());
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateValue = formatter.format(date);

		Date expDate = formatter.parse(dateValue);

		/** Checking Item Entry is already exist or not */

		ItemStockEntry entryObj = new ItemStockEntry();
		entryObj.setFacilityID(amritFacilityID);
		entryObj.setItemID(addedItemDetails.getItemID());
		entryObj.setBatchNo(eAushadhiResp.getBatchno());
		entryObj.setQuantity(Integer.parseInt(eAushadhiResp.getInhandqty()));
		entryObj.setQuantityInHand(Integer.parseInt(eAushadhiResp.getInhandqty()));
		entryObj.setExpiryDate(expDate);
		entryObj.setVanID(vanId);
		entryObj.setParkingPlaceID(parkingPlaceId);
		entryObj.setCreatedBy(addedItemDetails.getCreatedBy());
		entryObj.setEntryTypeID(110001);
		entryObj.setEntryType("E-Aushadhi Stock Entry");
		entryObj.setSyncFacilityID(amritFacilityID);

		ItemStockEntry newStockEntry = itemStockEntryRepo.save(entryObj);
		if (newStockEntry != null) {
			int isVanSerialNoUpdated = itemStockEntryRepo.updateVanSerialNo(newStockEntry.getItemStockEntryID());

			return isVanSerialNoUpdated;

		} else {
			return 0;
		}
		// }

	}

	/** Item - Facility Mapping **/
	public int insertItemFacilityMapping(Integer amritFacilityID, ItemMaster addedItemDetails) throws Exception {

		M_itemfacilitymapping itemFacilityMapObj = new M_itemfacilitymapping();
		itemFacilityMapObj.setFacilityID(amritFacilityID);
		itemFacilityMapObj.setItemID(addedItemDetails.getItemID());
		itemFacilityMapObj.setProviderServiceMapID(addedItemDetails.getProviderServiceMapID());
		itemFacilityMapObj.setCreatedBy(addedItemDetails.getCreatedBy());
		itemFacilityMapObj.setStatus("Active");

		M_itemfacilitymapping isItemFacilityMapped = mItemFacilityMappingRepo.save(itemFacilityMapObj);

		if (isItemFacilityMapped != null) {

			return 1;

		} else {
			return 0;
		}

	}

	/** Mapping Item details with sub facility **/
	public void mapItemDetailsAndInsertStockToSubFacility(EAushadhiResponse data, M_Facility amritFacilityID,
			Integer vanId, Integer parkingPlaceId, ItemMaster addedItemDetails) throws Exception {

		try {

			/** Facility - Item Mapping */
			int isItemFacilityMapped = insertItemFacilityMapping(amritFacilityID.getFacilityID(), addedItemDetails);

			if (isItemFacilityMapped > 0) {

				/** Item Stock Entry */
				int itemEntryStatus = insertItemStockEntry(amritFacilityID.getFacilityID(), addedItemDetails, data,
						vanId, parkingPlaceId);

				if (itemEntryStatus <= 0) {

					throw new FHIRException("E-Aushadhi Error - Issue with item stock entry");
				}

			} else {
				throw new FHIRException("E-Aushadhi Error - Issue with item sub facility mapping");
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/** Syncing amrit dispense details to e-aushadhi */
	@Override
	public String syncDispenseDetailsToEAushadhi(String request, String Authorization) throws FHIRException {

		Map<String, Object> dispenseResponseMap = new HashMap<>();
		try {
			M_Facility facilityDetails = InputMapper.gson().fromJson(request, M_Facility.class);
			Integer pageSize = Integer.valueOf(eAushadhiDispensePageSize);
			PageRequest pageReq = new PageRequest(facilityDetails.getPageNo(), pageSize);

			if (facilityDetails != null && !Objects.equals(facilityDetails.geteAushadhiFacilityId(), null)) {
				M_Facility facilityDet = facilityRepo.getAmritFacilityID(facilityDetails.geteAushadhiFacilityId());

				if (facilityDet != null && facilityDet.getFacilityID() != null) {

					Page<T_PatientIssue> patientIssueDetails = patientIssueRepo
							.getIssueDetailsForEAushadhi(facilityDet.getFacilityID(), pageReq);

					ArrayList<Map<String, Object>> dispenseDetailsForFacility = getPatientIssueDetailsForEAushadhByFacilityID(
							patientIssueDetails.getContent(), facilityDet, Authorization);

					dispenseResponseMap.put("patientIssueDetails", dispenseDetailsForFacility);
					dispenseResponseMap.put("Total Pages", patientIssueDetails.getTotalPages());
					return new Gson().toJson(dispenseResponseMap);

				} else {

					logger.info("E-aushadhi facilityId " + facilityDetails.geteAushadhiFacilityId() + " is not available in Amrit");
					throw new FHIRException(
							"Error while receiving patient wise drug dispense details from Amrit");

				}
			} else {

				Page<T_PatientIssue> patientIssueDetails = patientIssueRepo
						.getIssueDetailsForEAushadhiForAllFacility(pageReq);

				ArrayList<Map<String, Object>> dispenseDetailsForAllFacility = getPatientIssueDetailsForEAushadhiWithoutFacilityID(
						patientIssueDetails.getContent(), Authorization);

				dispenseResponseMap.put("patientIssueDetails", dispenseDetailsForAllFacility);
				dispenseResponseMap.put("Total Pages", patientIssueDetails.getTotalPages());

				return new Gson().toJson(dispenseResponseMap);

			}

		} catch (Exception e) {
			logger.error(e.toString());
			throw new FHIRException(e.toString());

		}

//		return new Gson().toJson(dispenseDetailsMap);

//		if (syncRes != null)
//			return syncRes;
//		else
//			throw new FHIRException("Error while syncing dispense details to E-aushadi");
	}

	public ArrayList<Map<String, Object>> getPatientIssueDetailsForEAushadhByFacilityID(
			List<T_PatientIssue> patientIssueDetails, M_Facility facilityDet, String Authorization)
			throws FHIRException {
		ResourceRequestHandler resourceRequestHandler;
		ArrayList<Map<String, Object>> issueDetailsForFacility = new ArrayList<Map<String, Object>>();
		try {

			for (T_PatientIssue patientIssueDet : patientIssueDetails) {
				Map<String, Object> dispenseDetailsMap = new HashMap<String, Object>();
//				dispenseDetailsMap.put("hststr_patient_name", patientIssueDet.getPatientName());
//				dispenseDetailsMap.put("hstdt_age", patientIssueDet.getAge().toString());
				dispenseDetailsMap.put("gnum_gender_code", patientIssueDet.getGender());
				dispenseDetailsMap.put("hststr_prescribed_by", patientIssueDet.getDoctorName());
				dispenseDetailsMap.put("hststr_patient_id", patientIssueDet.getBenRegID());

				resourceRequestHandler = new ResourceRequestHandler();
				resourceRequestHandler.setBeneficiaryRegID(patientIssueDet.getBenRegID());

				String responseBody = aPIChannel.benSearchByBenID(Authorization, resourceRequestHandler);

				PatientSearchAPIResponse benDetails = (InputMapper.gson().fromJson(responseBody,
						PatientSearchAPIResponse.class));
				if (benDetails != null && benDetails.getData().size() == 1) {

					dispenseDetailsMap.put("amrit_beneficiary_id", benDetails.getData().get(0).getBeneficiaryID());
					dispenseDetailsMap.put("hststr_father_name", benDetails.getData().get(0).getFatherName());
					dispenseDetailsMap.put("hstdt_age", benDetails.getData().get(0).getActualAge());
					dispenseDetailsMap.put("hstdt_age_unit", benDetails.getData().get(0).getAgeUnits());

					TimeZone tz = TimeZone.getTimeZone("IST");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					df.setTimeZone(tz);
					String newDob = df.format(benDetails.getData().get(0).getdOB());
					dispenseDetailsMap.put("hstdt_dob", newDob);

					if (!Objects.equals(benDetails.getData().get(0).getLastName(), null)) {
						dispenseDetailsMap.put("hststr_patient_name",
								benDetails.getData().get(0).getFirstName() + " "
										+ ((benDetails.getData().get(0).getLastName() != null)
												? benDetails.getData().get(0).getLastName()
												: ""));
					} else
						dispenseDetailsMap.put("hststr_patient_name", benDetails.getData().get(0).getFirstName());

				} else {
					// multiple patient found
					logger.info("Error - Multiple beneficiaries found, BenRegID:" + patientIssueDet.getBenRegID());
				}

				/** Fetching beneficiaryID */
//				Long benID = patientIssueRepo.getBenID(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("amrit_beneficiary_id", benID);

				/** Fetching beneficiary's father name */
//				String benFatherName = patientIssueRepo.getBenFatherName(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("hststr_father_name", benFatherName);

				/** Fetching beneficiary's Age */
//				String benAge = patientIssueRepo.getBenAge(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("hstdt_age", benAge);



				ArrayList<SyncDispenseDetailsRequest> dispenseDetails = getDrugDispenseDetails(patientIssueDet,
						facilityDet, facilityDet.geteAushadhiFacilityId());

				if (dispenseDetails != null && dispenseDetails.size() > 0) {
					dispenseDetailsMap.put("drug_dispensed", dispenseDetails);

					issueDetailsForFacility.add(dispenseDetailsMap);

				} else {

					logger.info("E-aushadhi drugs are not dispensed");
				}

			}
		} catch (Exception e) {
			logger.error(e.toString());
			throw new FHIRException(e.toString());

		}
		return issueDetailsForFacility;

	}

	public ArrayList<Map<String, Object>> getPatientIssueDetailsForEAushadhiWithoutFacilityID(
			List<T_PatientIssue> patientIssueDetails, String Authorization) throws FHIRException {
		ResourceRequestHandler resourceRequestHandler;
		ArrayList<Map<String, Object>> issueDetailsForFacility = new ArrayList<Map<String, Object>>();
		try {

			for (T_PatientIssue patientIssueDet : patientIssueDetails) {
				Map<String, Object> dispenseDetailsMap = new HashMap<String, Object>();
//				dispenseDetailsMap.put("hststr_patient_name", patientIssueDet.getPatientName());
//				dispenseDetailsMap.put("hstdt_age", patientIssueDet.getAge().toString());
				dispenseDetailsMap.put("gnum_gender_code", patientIssueDet.getGender());
				dispenseDetailsMap.put("hststr_prescribed_by", patientIssueDet.getDoctorName());
				dispenseDetailsMap.put("hststr_patient_id", patientIssueDet.getBenRegID());

				resourceRequestHandler = new ResourceRequestHandler();
				resourceRequestHandler.setBeneficiaryRegID(patientIssueDet.getBenRegID());

				String responseBody = aPIChannel.benSearchByBenID(Authorization, resourceRequestHandler);

				PatientSearchAPIResponse benDetails = (InputMapper.gson().fromJson(responseBody,
						PatientSearchAPIResponse.class));
				if (benDetails != null && benDetails.getData().size() == 1) {

					dispenseDetailsMap.put("amrit_beneficiary_id", benDetails.getData().get(0).getBeneficiaryID());
					dispenseDetailsMap.put("hststr_father_name", benDetails.getData().get(0).getFatherName());
					dispenseDetailsMap.put("hstdt_age", benDetails.getData().get(0).getActualAge());
					dispenseDetailsMap.put("hstdt_age_unit", benDetails.getData().get(0).getAgeUnits());

					TimeZone tz = TimeZone.getTimeZone("IST");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					df.setTimeZone(tz);
					String newDob = df.format(benDetails.getData().get(0).getdOB());
					dispenseDetailsMap.put("hstdt_dob", newDob);

					if (!Objects.equals(benDetails.getData().get(0).getLastName(), null)) {
						dispenseDetailsMap.put("hststr_patient_name",
								benDetails.getData().get(0).getFirstName() + " "
										+ ((benDetails.getData().get(0).getLastName() != null)
												? benDetails.getData().get(0).getLastName()
												: ""));
					} else
						dispenseDetailsMap.put("hststr_patient_name", benDetails.getData().get(0).getFirstName());

				} else {
					// multiple patient found
					logger.info("Error - Multiple beneficiaries found, BenRegID:" + patientIssueDet.getBenRegID());
				}

				/** Fetching beneficiaryID */
//				Long benID = patientIssueRepo.getBenID(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("amrit_beneficiary_id", benID);

				/** Fetching beneficiary's father name */
//				String benFatherName = patientIssueRepo.getBenFatherName(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("hststr_father_name", benFatherName);

				/** Fetching beneficiary's Age */
//				String benAge = patientIssueRepo.getBenAge(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("hstdt_age", benAge);

//				/** Fetching Health ID Details **/
//				ArrayList<BenHealthIDMapping> healthDetailsList = benHealthIDMappingRepo
//						.getHealthDetails(patientIssueDet.getBenRegID());
//				dispenseDetailsMap.put("hststr_patient_healthIdDetails", healthDetailsList);

				/** Getting eAushadhi Facility ID */
				M_Facility facilityDet = facilityRepo.geteAushadhiFacilityID(patientIssueDet.getFacilityID());

				ArrayList<SyncDispenseDetailsRequest> dispenseDetails = getDrugDispenseDetails(patientIssueDet,
						facilityDet, facilityDet.geteAushadhiFacilityId());

				if (dispenseDetails != null && dispenseDetails.size() > 0) {
					dispenseDetailsMap.put("drug_dispensed", dispenseDetails);

					issueDetailsForFacility.add(dispenseDetailsMap);

				} else {

					logger.info("E-aushadhi drugs are not dispensed");
				}

			}
		} catch (Exception e) {
			logger.error(e.toString());
			throw new FHIRException(e.toString());

		}
		return issueDetailsForFacility;

	}

	public ArrayList<SyncDispenseDetailsRequest> getDrugDispenseDetails(T_PatientIssue patientIssueDetails,
			M_Facility requestObj, Integer eAushadhiFacilityId) throws Exception {
		try {
			logger.info("Presc IDsss" + patientIssueDetails.getPrescriptionID());
			List<Objects[]> dispenseDetails = patientIssueRepo.getDispensedDrugDetails(
					patientIssueDetails.getPrescriptionID(), requestObj.getFacilityID(),
					patientIssueDetails.getPatientIssueID());

			logger.info("dispenseDetails IDsss" + dispenseDetails.size());

			SyncDispenseDetailsRequest dispenseDetailsReq = null;
			ArrayList<SyncDispenseDetailsRequest> syncDispenseDetailsRequest = new ArrayList<SyncDispenseDetailsRequest>();
			if (dispenseDetails != null && dispenseDetails.size() > 0) {
				for (Object[] obj : dispenseDetails) {

//					ItemStockExit quantityDispensed = itemStockExitRepo
//							.getItemExitDetails(patientIssueDetails.getPatientIssueID(), (Integer) obj[5]);

					TimeZone tz = TimeZone.getTimeZone("IST");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					df.setTimeZone(tz);
					String newIssueDate = df.format(patientIssueDetails.getCreatedDate());

					dispenseDetailsReq = new SyncDispenseDetailsRequest(obj[0].toString(), obj[1].toString(),
							eAushadhiFacilityId.toString(), patientIssueDetails.getPatientIssueID().toString(),
							obj[4].toString(), obj[2].toString(), newIssueDate, obj[6].toString(), obj[3].toString());

					syncDispenseDetailsRequest.add(dispenseDetailsReq);
				}
			}

			return syncDispenseDetailsRequest;
		} catch (Exception e) {
			logger.error(e.toString());
			throw new FHIRException(e.toString());

		}

	}

	/** Sending stock addition acknowledgement back to e-aushadhi */
	public String sendStockAdditionAckToEAushadhi(Integer ackStatus, Integer eAushadhiFacilityId) throws Exception {

		String ackResp = "0";
		try {

			HttpHeaders headers = new HttpHeaders();

			ResponseEntity<String> responseEntity = httpUtils
					.getWithResponseEntity(eaushadhiStoreStockAckUrl + eAushadhiFacilityId + "/" + ackStatus, headers);
			logger.info("E-aushadhi stock addition acknowledgement api response" + responseEntity);

			if (responseEntity.getStatusCodeValue() == 200 && responseEntity.hasBody()) {
//			String responseEAushadhi = responseEntity.getBody();
				JSONArray json = new JSONArray(responseEntity.getBody());
				if (json.length() > 0) {
					if (json.getJSONObject(0).get("trans_RESULT").equals("1")) {
						ackResp = "1";
					} else if (json.getJSONObject(0).get("trans_RESULT").equals("2")) {
						ackResp = "2";
					}
				}

				else {
					throw new FHIRException(
							"Empty response is coming from e-aushadhi stock addition acknowledgement api");
				}
			} else
				throw new FHIRException("Error while sending stock addition acknowledgement to e-aushadhi");

		} catch (Exception e) {
			logger.error(e.toString());
			ackResp = "0";
//		throw new FHIRException(e.toString());

		}
		return ackResp;
	}

	@Override
	public String getFacilityStockProcessLog(String request) throws FHIRException {

		M_Facility facilityDetails = InputMapper.gson().fromJson(request, M_Facility.class);
		List<E_AusdhFacilityProcessLog> resultList = e_AusdhFacilityProcessLogRepo
				.findByAmrithFacilityId(facilityDetails.getFacilityID());

		if (resultList.size() > 0)
			return new Gson().toJson(resultList.get(resultList.size() - 1));
		else
			return new Gson().toJson(resultList);

	}

	@Override
	public String updateSyncStatusForEAushadhiDispense(List<String> syncedIssueDetails) throws FHIRException {
		int syncCount = 0;
		try {

			for (int i = 0; i < syncedIssueDetails.size(); i++)

			{

				int updatedSyncStatus = patientIssueRepo
						.updateEAushadhiIssueSyncStatus(Long.parseLong(syncedIssueDetails.get(i)));
				if (updatedSyncStatus > 0) {
					syncCount++;
				} else {
					logger.info("Failed to update sync status, patientIssueID:"
							+ Long.parseLong(syncedIssueDetails.get(i)));
				}
			}

		} catch (Exception e) {
			logger.error(e.toString());

			throw new FHIRException(e.toString());

		}

		if (syncCount > 0) {
			return "1";
		} else {
			return "2";
		}
	}

}
