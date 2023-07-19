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

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FirstDiagnosis {
	private String order;
	private String certainty;
	private String freeTextAnswer;

	private CodedAnswer codedAnswer;

	private String existingObs;
	
	private Boolean voided;
	private String voidReason;
	private String comments;
	private List<Providers> providers;
	private String diagnosisStatusConcept;
	private String firstDiagnosis;
	private String latestDiagnosis;
	private Boolean revised;
	private String previousObs;
	private String encounterUuid;
	private String creatorName;

}
