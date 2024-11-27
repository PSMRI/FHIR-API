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

import org.springframework.stereotype.Component;

import com.google.gson.annotations.Expose;

import lombok.Data;

/***
 * 
 * @author NE298657
 *
 */

@Component

@Data
public class ResourceRequestHandler {
	// patient ||
	private BigInteger beneficiaryID;
	// Encounter/ care-context
	private BigInteger visitCode;
	private BigInteger beneficiaryRegID;
	private Long benFlowID;

	// 27-10-2021
	@Expose
	private String healthId;
	@Expose
	private String healthIdNumber;
	@Expose
	private String amritId;
	@Expose
	private String externalId;
	@Expose
	private Integer pageNo;
	@Expose
	private String phoneNo;
	@Expose
	private String state;
	@Expose
	private String district;
	@Expose
	private String village;

}
