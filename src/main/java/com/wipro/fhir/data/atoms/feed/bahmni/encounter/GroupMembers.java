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
public class GroupMembers {
	
	private String targetObsRelation;
	private List<GroupMembers> groupMembers;
	private List<Providers> providers;
	private String isAbnormal;
	private String duration;
	private String type;
	private String encounterUuid;
	private String obsGroupUuid;
	private String creatorName;
	private Integer conceptSortWeight;
	private String parentConceptUuid;
	private String hiNormal;
	private String lowNormal;
	private String formNamespace;
	private String formFieldPath;
	private String interpretation;
	private String status;
	private String complexData;
	private Concept concept;
	private String valueAsString;
	private String conceptUuid;
	private String uuid;
	private Boolean voided;
	private String voidReason;
	private Boolean unknown;
	
	private String abnormal;
	private String conceptNameToDisplay;
	private String orderUuid;
	private String comment;
	
	public String getTargetObsRelation() {
		return targetObsRelation;
	}
	public void setTargetObsRelation(String targetObsRelation) {
		this.targetObsRelation = targetObsRelation;
	}
	public List<GroupMembers> getGroupMembers() {
		return groupMembers;
	}
	public void setGroupMembers(List<GroupMembers> groupMembers) {
		this.groupMembers = groupMembers;
	}
	public List<Providers> getProviders() {
		return providers;
	}
	public void setProviders(List<Providers> providers) {
		this.providers = providers;
	}
	public String getIsAbnormal() {
		return isAbnormal;
	}
	public void setIsAbnormal(String isAbnormal) {
		this.isAbnormal = isAbnormal;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEncounterUuid() {
		return encounterUuid;
	}
	public void setEncounterUuid(String encounterUuid) {
		this.encounterUuid = encounterUuid;
	}
	public String getObsGroupUuid() {
		return obsGroupUuid;
	}
	public void setObsGroupUuid(String obsGroupUuid) {
		this.obsGroupUuid = obsGroupUuid;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public Integer getConceptSortWeight() {
		return conceptSortWeight;
	}
	public void setConceptSortWeight(Integer conceptSortWeight) {
		this.conceptSortWeight = conceptSortWeight;
	}
	public String getParentConceptUuid() {
		return parentConceptUuid;
	}
	public void setParentConceptUuid(String parentConceptUuid) {
		this.parentConceptUuid = parentConceptUuid;
	}
	public String getHiNormal() {
		return hiNormal;
	}
	public void setHiNormal(String hiNormal) {
		this.hiNormal = hiNormal;
	}
	public String getLowNormal() {
		return lowNormal;
	}
	public void setLowNormal(String lowNormal) {
		this.lowNormal = lowNormal;
	}
	public String getFormNamespace() {
		return formNamespace;
	}
	public void setFormNamespace(String formNamespace) {
		this.formNamespace = formNamespace;
	}
	public String getFormFieldPath() {
		return formFieldPath;
	}
	public void setFormFieldPath(String formFieldPath) {
		this.formFieldPath = formFieldPath;
	}
	public String getInterpretation() {
		return interpretation;
	}
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComplexData() {
		return complexData;
	}
	public void setComplexData(String complexData) {
		this.complexData = complexData;
	}
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public String getValueAsString() {
		return valueAsString;
	}
	public void setValueAsString(String valueAsString) {
		this.valueAsString = valueAsString;
	}
	public String getConceptUuid() {
		return conceptUuid;
	}
	public void setConceptUuid(String conceptUuid) {
		this.conceptUuid = conceptUuid;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Boolean getVoided() {
		return voided;
	}
	public void setVoided(Boolean voided) {
		this.voided = voided;
	}
	public String getVoidReason() {
		return voidReason;
	}
	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}
	public Boolean getUnknown() {
		return unknown;
	}
	public void setUnknown(Boolean unknown) {
		this.unknown = unknown;
	}
	public String getAbnormal() {
		return abnormal;
	}
	public void setAbnormal(String abnormal) {
		this.abnormal = abnormal;
	}
	public String getConceptNameToDisplay() {
		return conceptNameToDisplay;
	}
	public void setConceptNameToDisplay(String conceptNameToDisplay) {
		this.conceptNameToDisplay = conceptNameToDisplay;
	}
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
