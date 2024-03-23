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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.EncounterFullRepresentation;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.GroupMembers;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.Observations;
import com.wipro.fhir.data.atoms.feed.bahmni.encounter.VisitWiseEncounterData;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.repo.atoms.feed.bahmni.encounter.EncounterFullRepresentationRepo;
import com.wipro.fhir.repo.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile_Repo;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class HigherHealthFacilityServiceImpl {
	@Autowired
	private PatientDemographicModel_NDHM_Patient_Profile_Repo patientProfileRepo;
	@Autowired
	private EncounterFullRepresentationRepo encounterFullRepresentationRepo;

	public String updateBengenIDToHigherHealthFacilityBeneficiary(ResourceRequestHandler reqObj) throws FHIRException {
		List<PatientDemographicModel_NDHM_Patient_Profile> resultSet = patientProfileRepo
				.findByExternalId(reqObj.getExternalId());

		HashMap<String, String> identifier;
		for (PatientDemographicModel_NDHM_Patient_Profile obj : resultSet) {
			identifier = new HashMap<>();
			identifier.put("amritId", reqObj.getAmritId());
			obj.setAmritId(reqObj.getAmritId());
			obj.getProfile().getPatient().getIdentifiers().add(identifier);
		}
		patientProfileRepo.saveAll(resultSet);
		return "Beneficiary ID updated successfully";
	}

	public String getCLinicalDataHigherhealthFacility(ResourceRequestHandler reqObj) throws FHIRException {
		List<EncounterFullRepresentation> resultList = new ArrayList<EncounterFullRepresentation>();
		List<VisitWiseEncounterData> visitList = new ArrayList<>();

		List<PatientDemographicModel_NDHM_Patient_Profile> resultSet = patientProfileRepo
				.findByAmritId(reqObj.getAmritId());
		
		if (resultSet != null && resultSet.size() > 0) {
			String externalID = resultSet.get(0).getExternalId();
			resultList = encounterFullRepresentationRepo.findByPatientId(externalID);
			
			if (resultList != null && resultList.size() > 0) {

				visitList = getVisitWiseEncounterData(resultList);

			}

		}
		return new Gson().toJson(visitList);
	}

	//// ------------------------------------------------------------------

	// List<VisitWiseEncounterData> visitList = new
	// ArrayList<VisitWiseEncounterData>();

	VisitWiseEncounterData visitWiseEncounterData;
	List<GroupMembers> grpMembersFinalReg;
	List<GroupMembers> grpMembersFinalCon;

	public List<VisitWiseEncounterData> getVisitWiseEncounterData(List<EncounterFullRepresentation> resultList) {
		Map<String, VisitWiseEncounterData> visitUUIDMap = new HashMap<>();
		List<VisitWiseEncounterData> visitList = new ArrayList<VisitWiseEncounterData>();
		Map<String, String> checkProcessedEncounter = new HashMap<>();

		for (EncounterFullRepresentation obj : resultList) {
			if (!checkProcessedEncounter.containsKey(obj.getVisitUuid() + obj.getEncounterType())) {

				grpMembersFinalReg = null;
				grpMembersFinalCon = null;
				if (obj.getVisitUuid() != null && !visitUUIDMap.containsKey(obj.getVisitUuid())) {
					visitWiseEncounterData = new VisitWiseEncounterData();
					grpMembersFinalReg = new ArrayList<>();
					grpMembersFinalCon = new ArrayList<>();

					visitWiseEncounterData.setVisitUuid(obj.getVisitUuid());
					if (obj.getEncounterDateTime() != null)
						visitWiseEncounterData.setEncounterDateTime(obj.getEncounterDateTime());
					if (obj.getVisitType() != null)
						visitWiseEncounterData.setVisitType(obj.getVisitType());
					if (obj.getVisitTypeUuid() != null)
						visitWiseEncounterData.setVisitTypeUuid(obj.getVisitTypeUuid());
					if (obj.getPatientId() != null)
						visitWiseEncounterData.setPatientId(obj.getPatientId());

				} else {
					if (obj.getVisitUuid() != null) {
						visitWiseEncounterData = visitUUIDMap.get(obj.getVisitUuid());
						grpMembersFinalReg = visitWiseEncounterData.getObservationsGMReg();
						grpMembersFinalCon = visitWiseEncounterData.getObservationsGMConsultation();
					}
				}

				if (obj.getEncounterType().equalsIgnoreCase("consultation") && visitWiseEncounterData != null) {
					if (visitWiseEncounterData.getBahmniDiagnoses() == null && obj.getBahmniDiagnoses() != null)
						visitWiseEncounterData.setBahmniDiagnoses(obj.getBahmniDiagnoses());
					if (visitWiseEncounterData.getDrugOrders() == null && obj.getDrugOrders() != null)
						visitWiseEncounterData.setDrugOrders(obj.getDrugOrders());
				}

				if (obj.getObservations() != null && obj.getObservations().size() > 0) {
					for (Observations o : obj.getObservations()) {
						finalfamilymember = new ArrayList<>();
						List<GroupMembers> grpMembers = getSerializedGroupMembersNew(o.getGroupMembers());
						if (grpMembers != null && grpMembers.size() > 0) {
							if (obj.getEncounterType().equalsIgnoreCase("consultation") && grpMembersFinalCon != null) {
								grpMembersFinalCon.addAll(grpMembers);
								visitWiseEncounterData.setObservationsGMConsultation(grpMembersFinalCon);
							} else if (obj.getEncounterType().equalsIgnoreCase("reg") && grpMembersFinalReg != null) {
								grpMembersFinalReg.addAll(grpMembers);
								visitWiseEncounterData.setObservationsGMReg(grpMembersFinalReg);
							}
						}
					}
				}

				visitUUIDMap.put(obj.getVisitUuid(), visitWiseEncounterData);
				checkProcessedEncounter.put(obj.getVisitUuid() + obj.getEncounterType(), "true");
			}
		}
		visitUUIDMap.forEach((k, v) -> visitList.add(v));
		return visitList;
	}

	List<GroupMembers> finalfamilymember = new ArrayList<GroupMembers>();

	private List<GroupMembers> getSerializedGroupMembersNew(List<GroupMembers> groupMembers) {
		
		for (GroupMembers gm : groupMembers) {
			if (gm.getGroupMembers() == null || gm.getGroupMembers().size() == 0) {
				finalfamilymember.add(gm);
			} else {
				getSerializedGroupMembersNew(gm.getGroupMembers());
			}
		}
		return finalfamilymember;
	}
}
