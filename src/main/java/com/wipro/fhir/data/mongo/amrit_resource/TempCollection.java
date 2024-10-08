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
package com.wipro.fhir.data.mongo.amrit_resource;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Document(collection = "AMRIT_TempStorage")
@Data
public class TempCollection {

	@Id
	@Expose
	@Field(value = "id")
	private String id;

	@Expose
	@Field(value = "BeneficiaryRegID")
	private BigInteger beneficiaryRegID;

	@Expose
	@Field(value = "VisitCode")
	private BigInteger visitCode;

	@Expose
	@Field(value = "DataType")
	private String dataType;

	@Expose
	@Field(value = "DataJson")
	private Map<Integer, List<String>> dataJson;

	@Expose
	@Field(value = "CreateDate")
	private Date createDate;;

	@Expose
	@Field(value = "CreateBy")
	private String createBy;

}
