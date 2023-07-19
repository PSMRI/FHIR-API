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

public class SyncPatientDetailsRequest {
	
	String hststr_patient_name;
	String hststr_father_name;
	String hstdt_age;
	String gnum_gender_code;
	String hststr_patient_id;
	String amrit_beneficiary_id;
	String ndhm_health_id;
	String ndhm_health_id_no;
	String hststr_prescribed_by;
	
	
	public String getHststr_patient_name() {
		return hststr_patient_name;
	}
	public void setHststr_patient_name(String hststr_patient_name) {
		this.hststr_patient_name = hststr_patient_name;
	}
	public String getHststr_father_name() {
		return hststr_father_name;
	}
	public void setHststr_father_name(String hststr_father_name) {
		this.hststr_father_name = hststr_father_name;
	}
	public String getHstdt_age() {
		return hstdt_age;
	}
	public void setHstdt_age(String hstdt_age) {
		this.hstdt_age = hstdt_age;
	}
	public String getGnum_gender_code() {
		return gnum_gender_code;
	}
	public void setGnum_gender_code(String gnum_gender_code) {
		this.gnum_gender_code = gnum_gender_code;
	}
	public String getHststr_patient_id() {
		return hststr_patient_id;
	}
	public void setHststr_patient_id(String hststr_patient_id) {
		this.hststr_patient_id = hststr_patient_id;
	}
	public String getAmrit_beneficiary_id() {
		return amrit_beneficiary_id;
	}
	public void setAmrit_beneficiary_id(String amrit_beneficiary_id) {
		this.amrit_beneficiary_id = amrit_beneficiary_id;
	}
	public String getNdhm_health_id() {
		return ndhm_health_id;
	}
	public void setNdhm_health_id(String ndhm_health_id) {
		this.ndhm_health_id = ndhm_health_id;
	}
	public String getNdhm_health_id_no() {
		return ndhm_health_id_no;
	}
	public void setNdhm_health_id_no(String ndhm_health_id_no) {
		this.ndhm_health_id_no = ndhm_health_id_no;
	}
	public String getHststr_prescribed_by() {
		return hststr_prescribed_by;
	}
	public void setHststr_prescribed_by(String hststr_prescribed_by) {
		this.hststr_prescribed_by = hststr_prescribed_by;
	}
	

}
