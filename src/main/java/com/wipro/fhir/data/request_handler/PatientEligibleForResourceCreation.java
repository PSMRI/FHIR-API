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
package com.wipro.fhir.data.request_handler;

import java.math.BigInteger;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "ndhm_trg_visitdata")
@Data
public class PatientEligibleForResourceCreation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	@Column(name = "id")
	private BigInteger id;
	@Expose
	@Column(name = "BeneficiaryRegID")
	private BigInteger beneficiaryRegID;
	@Expose
	@Column(name = "beneficiary_id")
	private BigInteger beneficiaryId;
	@Expose
	@Column(name = "visit_reason")
	private String visitReason;
	@Expose
	@Column(name = "visit_category")
	private String visitCategory;
	@Expose
	@Column(name = "VisitCode")
	private BigInteger visitCode;
	@Expose
	@Column(name = "visitdate")
	private Timestamp visitDate;
	@Expose
	@Column(name = "created_date")
	private Timestamp createdDate;
	@Expose
	@Column(name = "Processed_Flag")
	private Boolean processed;

}
