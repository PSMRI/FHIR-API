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




public class EAushadhiResponse {


    String brandid;
    String drugname;
	String inhandqty;
	String batchno;
	String stockstatus;
	String expdate;
    String mfgdate;
    String prgid;
	String pono;
    String category;
	String itemtypename;
    String specification;
    String cpacode;
    String edl;
	
	
	public String getBrandid() {
		return brandid;
	}
	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}
	public String getDrugname() {
		return drugname;
	}
	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}
	public String getInhandqty() {
		return inhandqty;
	}
	public void setInhandqty(String inhandqty) {
		this.inhandqty = inhandqty;
	}
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public String getStockstatus() {
		return stockstatus;
	}
	public void setStockstatus(String stockstatus) {
		this.stockstatus = stockstatus;
	}
	public String getExpdate() {
		return expdate;
	}
	public void setExpdate(String expdate) {
		this.expdate = expdate;
	}
	public String getMfgdate() {
		return mfgdate;
	}
	public void setMfgdate(String mfgdate) {
		this.mfgdate = mfgdate;
	}
	public String getPrgid() {
		return prgid;
	}
	public void setPrgid(String prgid) {
		this.prgid = prgid;
	}
	public String getPono() {
		return pono;
	}
	public void setPono(String pono) {
		this.pono = pono;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getItemtypename() {
		return itemtypename;
	}
	public void setItemtypename(String itemtypename) {
		this.itemtypename = itemtypename;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getCpacode() {
		return cpacode;
	}
	public void setCpacode(String cpacode) {
		this.cpacode = cpacode;
	}
	public String getEdl() {
		return edl;
	}
	public void setEdl(String edl) {
		this.edl = edl;
	}

	
	
}


