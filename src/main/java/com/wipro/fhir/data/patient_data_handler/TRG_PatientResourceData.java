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
package com.wipro.fhir.data.patient_data_handler;

import java.math.BigInteger;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.Expose;

@Entity
@Table(name = "db_identity.trg_patientresourcedata")
@Data
public class TRG_PatientResourceData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	@Column(name = "id")
	private Long id;

	@Expose
	@Column(name = "BenRegId")
	private BigInteger benRegId;

	@Expose
	@Column(name = "BeneficiaryID")
	private BigInteger beneficiaryID;

	@Expose
	@Column(name = "CreatedBy")
	private String createdBy;

	@Expose
	@Column(name = "CreatedDate")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Kolkata")
	private Timestamp createdDate;

	@Expose
	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Expose
	@Column(name = "LastModDate")
	private Timestamp lastModDate;

	@Expose
	@Column(name = "Trg_status")
	private String trgStatus;

	@Expose
	@Column(name = "Processed")
	private Boolean processed;

}
