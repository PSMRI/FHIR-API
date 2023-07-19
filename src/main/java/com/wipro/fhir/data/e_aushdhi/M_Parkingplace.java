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
@Table(name="m_parkingplace")

public class M_Parkingplace {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	@Column(name="ParkingPlaceID")
	private Integer parkingPlaceID;
	
	@Expose
	@Column(name="ParkingPlaceName")
	private String parkingPlaceName;
	
	@Expose
	@Column(name="ParkingPlaceDesc")
	private String parkingPlaceDesc;
	
	
	@Expose
	@Column(name="ZoneID")
	private Integer zoneID;
	

	
	@Expose
	@Column(name="ProviderServiceMapID")
	private Integer providerServiceMapID;
	
	@Expose
	@Column(name="CountryID")
	private Integer countryID;
	
	
	
	@Expose
	@Column(name="StateID")
	private Integer stateID;
	
	
	
	@Expose
	@Column(name="DistrictID")
	private Integer districtID;
	
	
	
	

	@Expose
	@Column(name="DistrictBlockID")
	private Integer districtBlockID;
	
	
	@Expose
	@Column(name="DistrictBranchID")
	private Integer districtBranchID;
	
	
	
	@Expose
	@Column(name="AreaHQAddress")
	private String areaHQAddress;
	
	@Expose
	@Column(name="IsFacility")
	private Boolean isFacility;
	
	@Expose
	@Column(name="FacilityID")
	private Integer facilityID;
	
	
	
	@Expose
	@Column(name = "Deleted",insertable = false, updatable = true)
	private Boolean deleted; 
	@Expose
	@Column(name = "Processed",insertable = false, updatable = true)
	private String processed;
	@Expose
	@Column(name = "CreatedBy")
	private String createdBy;
	@Expose
	@Column(name = "CreatedDate" ,insertable = false, updatable = false)
	private Date createdDate;
	@Expose
	@Column(name = "ModifiedBy")
	private String modifiedBy; 
	@Expose
	@Column(name = "LastModDate", insertable = false, updatable = false)
	private Date lastModDate;
	public Integer getParkingPlaceID() {
		return parkingPlaceID;
	}
	public void setParkingPlaceID(Integer parkingPlaceID) {
		this.parkingPlaceID = parkingPlaceID;
	}
	public String getParkingPlaceName() {
		return parkingPlaceName;
	}
	public void setParkingPlaceName(String parkingPlaceName) {
		this.parkingPlaceName = parkingPlaceName;
	}
	public String getParkingPlaceDesc() {
		return parkingPlaceDesc;
	}
	public void setParkingPlaceDesc(String parkingPlaceDesc) {
		this.parkingPlaceDesc = parkingPlaceDesc;
	}
	public Integer getZoneID() {
		return zoneID;
	}
	public void setZoneID(Integer zoneID) {
		this.zoneID = zoneID;
	}
	public Integer getProviderServiceMapID() {
		return providerServiceMapID;
	}
	public void setProviderServiceMapID(Integer providerServiceMapID) {
		this.providerServiceMapID = providerServiceMapID;
	}
	public Integer getCountryID() {
		return countryID;
	}
	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}
	public Integer getStateID() {
		return stateID;
	}
	public void setStateID(Integer stateID) {
		this.stateID = stateID;
	}
	public Integer getDistrictID() {
		return districtID;
	}
	public void setDistrictID(Integer districtID) {
		this.districtID = districtID;
	}
	public Integer getDistrictBlockID() {
		return districtBlockID;
	}
	public void setDistrictBlockID(Integer districtBlockID) {
		this.districtBlockID = districtBlockID;
	}
	public Integer getDistrictBranchID() {
		return districtBranchID;
	}
	public void setDistrictBranchID(Integer districtBranchID) {
		this.districtBranchID = districtBranchID;
	}
	public String getAreaHQAddress() {
		return areaHQAddress;
	}
	public void setAreaHQAddress(String areaHQAddress) {
		this.areaHQAddress = areaHQAddress;
	}
	public Boolean getIsFacility() {
		return isFacility;
	}
	public void setIsFacility(Boolean isFacility) {
		this.isFacility = isFacility;
	}
	public Integer getFacilityID() {
		return facilityID;
	}
	public void setFacilityID(Integer facilityID) {
		this.facilityID = facilityID;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getLastModDate() {
		return lastModDate;
	}
	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}
	

	
	
	
}

