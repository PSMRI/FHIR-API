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



import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;



@Entity
@Table(name = "m_itemform")
public class M_ItemForm {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	@Column(name = "ItemFormID")
	private Integer itemFormID;

	@Expose
	@Column(name = "ItemFormName")
	private String itemForm;

	@Expose
	@Column(name = "ItemFormDesc")
	private String itemFormDesc;
	

	@Expose
	@Column(name = "ItemFormCode")
	private String itemFormCode;
	
	@Expose
	@Column(name = "ProviderServiceMapID")
	private Integer providerServiceMapID;

	@Expose
	@Column(name = "Deleted", insertable = false, updatable = false)
	private Boolean deleted;

	@Expose
	@Column(name = "Processed", insertable = false, updatable = false)
	private Character processed;

	@Expose
	@Column(name = "CreatedBy")
	private String CreatedBy;

	@Expose
	@Column(name = "CreatedDate", insertable = false, updatable = false)
	private Date CreatedDate;

	@Expose
	@Column(name = "ModifiedBy")
	private String ModifiedBy;

	@Expose
	@Column(name = "LastModDate", insertable = false, updatable = false)
	private Date LastModDate;

	public Integer getItemFormID() {
		return itemFormID;
	}

	public void setItemFormID(Integer itemFormID) {
		this.itemFormID = itemFormID;
	}

	public String getItemForm() {
		return itemForm;
	}

	public void setItemForm(String itemForm) {
		this.itemForm = itemForm;
	}

	public String getItemFormDesc() {
		return itemFormDesc;
	}

	public void setItemFormDesc(String itemFormDesc) {
		this.itemFormDesc = itemFormDesc;
	}

	public String getItemFormCode() {
		return itemFormCode;
	}

	public void setItemFormCode(String itemFormCode) {
		this.itemFormCode = itemFormCode;
	}

	public Integer getProviderServiceMapID() {
		return providerServiceMapID;
	}

	public void setProviderServiceMapID(Integer providerServiceMapID) {
		this.providerServiceMapID = providerServiceMapID;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Character getProcessed() {
		return processed;
	}

	public void setProcessed(Character processed) {
		this.processed = processed;
	}

	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

	public Date getLastModDate() {
		return LastModDate;
	}

	public void setLastModDate(Date lastModDate) {
		LastModDate = lastModDate;
	}


}

