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
package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import org.springframework.stereotype.Component;

@Component
public class DrugOrders {
	private Concept concept;

	private String instructions;
	private String uuid;
	private String orderType;
	private String orderGroup;

	private String orderNumber;
	private String careSetting;
	private String action;
	private String commentToFulfiller;

	private String urgency;
	private String previousOrderUuid;

	private Drug drug;

	private String drugNonCoded;
	private String dosingInstructionType;

	private DosingInstructions dosingInstructions;


	private String orderReasonText;
	private Integer duration;
	private String durationUnits;
	private Boolean voided;
	private String voidReason;
	private String orderReasonConcept;
	private String sortWeight;

}
