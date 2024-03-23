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

public class SyncDispenseDetailsRequest {
	
	String drug_id;
	String quantity_prescribed;
	String quantity_dispensed;
	String hstnum_store_id;
	String hstnum_issue_no;
	String hstnum_itembrand_id;
	String hststr_batch_sl_no;
	String hstdt_issue_date;
	String hstnum_issue_qty;
	String hstdt_expiry_date;
	
	public String getDrug_id() {
		return drug_id;
	}
	public void setDrug_id(String drug_id) {
		this.drug_id = drug_id;
	}
	public String getQuantity_prescribed() {
		return quantity_prescribed;
	}
	public void setQuantity_prescribed(String quantity_prescribed) {
		this.quantity_prescribed = quantity_prescribed;
	}
	public String getQuantity_dispensed() {
		return quantity_dispensed;
	}
	public void setQuantity_dispensed(String quantity_dispensed) {
		this.quantity_dispensed = quantity_dispensed;
	}
	public String getHstnum_store_id() {
		return hstnum_store_id;
	}
	public void setHstnum_store_id(String hstnum_store_id) {
		this.hstnum_store_id = hstnum_store_id;
	}
	public String getHstnum_issue_no() {
		return hstnum_issue_no;
	}
	public void setHstnum_issue_no(String hstnum_issue_no) {
		this.hstnum_issue_no = hstnum_issue_no;
	}
	public String getHstnum_itembrand_id() {
		return hstnum_itembrand_id;
	}
	public void setHstnum_itembrand_id(String hstnum_itembrand_id) {
		this.hstnum_itembrand_id = hstnum_itembrand_id;
	}
	public String getHststr_batch_sl_no() {
		return hststr_batch_sl_no;
	}
	public void setHststr_batch_sl_no(String hststr_batch_sl_no) {
		this.hststr_batch_sl_no = hststr_batch_sl_no;
	}
	public String getHstdt_issue_date() {
		return hstdt_issue_date;
	}
	public void setHstdt_issue_date(String hstdt_issue_date) {
		this.hstdt_issue_date = hstdt_issue_date;
	}
	public String getHstnum_issue_qty() {
		return hstnum_issue_qty;
	}
	public void setHstnum_issue_qty(String hstnum_issue_qty) {
		this.hstnum_issue_qty = hstnum_issue_qty;
	}
	public String getHstdt_expiry_date() {
		return hstdt_expiry_date;
	}
	public void setHstdt_expiry_date(String hstdt_expiry_date) {
		this.hstdt_expiry_date = hstdt_expiry_date;
	}
	
	public SyncDispenseDetailsRequest() {
		super();
		
	}
	
	
	public SyncDispenseDetailsRequest(String drug_id, String quantity_prescribed, String hstnum_store_id,
			String hstnum_issue_no, String hstnum_itembrand_id, String hststr_batch_sl_no, String hstdt_issue_date, 
			String hstnum_issue_qty,String hstdt_expiry_date) {
		super();
		
		 this.drug_id=drug_id;
		 this.quantity_prescribed=quantity_prescribed;
		 this.hstnum_store_id=hstnum_store_id;
		 this.hstnum_issue_no=hstnum_issue_no;
		 this.hstnum_itembrand_id=hstnum_itembrand_id.replace("_EAushadhi", "");
		 this.hststr_batch_sl_no=hststr_batch_sl_no;
		 this.hstdt_issue_date=hstdt_issue_date;
		 this.hstnum_issue_qty=hstnum_issue_qty;
		 this.hstdt_expiry_date=hstdt_expiry_date;
		
	}

	
}
