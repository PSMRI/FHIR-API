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
package com.wipro.fhir.data.e_aushdhi;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "eaushadilogger")
public class E_AusdhFacilityProcessLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	@Column(name = "ELID")
	private Integer eLID;

	@Expose
	@Column(name = "Amrith_Facilityid")
	private Integer amrithFacilityId;

	@Expose
	@Column(name = "Eaushadi_Facilityid")
	private Integer eaushadiFacilityId;

	@Expose
	@Column(name = "LastSuccessDate")
	private Timestamp lastSuccessDate;

	@Expose
	@Column(name = "LastFailureDate")
	private Timestamp lastFailureDate;

	@Expose
	@Column(name = "Deleted")
	private Boolean deleted;

	@Expose
	@Column(name = "Processed", insertable = false)
	private String processed;

	@Expose
	@Column(name = "CreatedBy")
	private String createdBy;

	@Expose
	@Column(name = "CreatedDate", insertable = false, updatable = false)
	private Timestamp createdDate;

	@Expose
	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Expose
	@Column(name = "LastModDate", insertable = false, updatable = false)
	private Timestamp lastModDate;

	@Expose
	@Column(name = "StockUpdateAmrit")
	private Boolean stockUpdateAmrit;

	@Expose
	@Column(name = "Acknowledge")
	private Boolean acknowledge;

	public Integer geteLID() {
		return eLID;
	}

	public void seteLID(Integer eLID) {
		this.eLID = eLID;
	}

	public Integer getAmrithFacilityId() {
		return amrithFacilityId;
	}

	public void setAmrithFacilityId(Integer amrithFacilityId) {
		this.amrithFacilityId = amrithFacilityId;
	}

	public Integer getEaushadiFacilityId() {
		return eaushadiFacilityId;
	}

	public void setEaushadiFacilityId(Integer eaushadiFacilityId) {
		this.eaushadiFacilityId = eaushadiFacilityId;
	}

	public Timestamp getLastSuccessDate() {
		return lastSuccessDate;
	}

	public void setLastSuccessDate(Timestamp lastSuccessDate) {
		this.lastSuccessDate = lastSuccessDate;
	}

	public Timestamp getLastFailureDate() {
		return lastFailureDate;
	}

	public void setLastFailureDate(Timestamp lastFailureDate) {
		this.lastFailureDate = lastFailureDate;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getLastModDate() {
		return lastModDate;
	}

	public void setLastModDate(Timestamp lastModDate) {
		this.lastModDate = lastModDate;
	}

	public Boolean getStockUpdateAmrit() {
		return stockUpdateAmrit;
	}

	public void setStockUpdateAmrit(Boolean stockUpdateAmrit) {
		this.stockUpdateAmrit = stockUpdateAmrit;
	}

	public Boolean getAcknowledge() {
		return acknowledge;
	}

	public void setAcknowledge(Boolean acknowledge) {
		this.acknowledge = acknowledge;
	}

}
