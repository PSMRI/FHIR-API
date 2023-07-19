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
package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class VisitWiseEncounterData {

	private String visitUuid;
	private Date encounterDateTime;
	private String visitTypeUuid;
	private String visitType;

	private String patientId;

	private List<BahmniDiagnoses> bahmniDiagnoses;

	private List<GroupMembers> observationsGMReg;
	private List<GroupMembers> observationsGMConsultation;

	private List<DrugOrders> drugOrders;

	public String getVisitUuid() {
		return visitUuid;
	}

	public void setVisitUuid(String visitUuid) {
		this.visitUuid = visitUuid;
	}

	public Date getEncounterDateTime() {
		return encounterDateTime;
	}

	public void setEncounterDateTime(Date encounterDateTime) {
		this.encounterDateTime = encounterDateTime;
	}

	public String getVisitTypeUuid() {
		return visitTypeUuid;
	}

	public void setVisitTypeUuid(String visitTypeUuid) {
		this.visitTypeUuid = visitTypeUuid;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public List<BahmniDiagnoses> getBahmniDiagnoses() {
		return bahmniDiagnoses;
	}

	public void setBahmniDiagnoses(List<BahmniDiagnoses> bahmniDiagnoses) {
		this.bahmniDiagnoses = bahmniDiagnoses;
	}

	public List<GroupMembers> getObservationsGMReg() {
		return observationsGMReg;
	}

	public void setObservationsGMReg(List<GroupMembers> observationsGMReg) {
		this.observationsGMReg = observationsGMReg;
	}

	public List<GroupMembers> getObservationsGMConsultation() {
		return observationsGMConsultation;
	}

	public void setObservationsGMConsultation(List<GroupMembers> observationsGMConsultation) {
		this.observationsGMConsultation = observationsGMConsultation;
	}

	public List<DrugOrders> getDrugOrders() {
		return drugOrders;
	}

	public void setDrugOrders(List<DrugOrders> drugOrders) {
		this.drugOrders = drugOrders;
	}

}
