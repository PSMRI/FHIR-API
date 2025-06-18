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
package com.wipro.fhir.controller.generateresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.resource_gateway.DiagnosticReportRecord;
import com.wipro.fhir.service.resource_gateway.OPConsultRecordBundle;
import com.wipro.fhir.service.resource_gateway.PrescriptionRecordBundle;
import com.wipro.fhir.utils.exception.FHIRException;
import com.wipro.fhir.utils.response.OutputResponse;

import io.swagger.v3.oas.annotations.Operation;

/***
 * 
 * 
 * Implement the check if a bundle resource is already there for a care-context,
 * fetch from Mongo instead creating again ***
 *
 */
@RestController
@RequestMapping(value = "/get/resource", headers = "Authorization", consumes = "application/json", produces = "application/json")
public class ResourceRequestGateway {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private OPConsultRecordBundle opConsultRecordBundle;
	@Autowired
	private PrescriptionRecordBundle prescriptionRecordBundle;
	@Autowired
	private DiagnosticReportRecord diagnosticReportRecord;

	/***
	 * 
	 * @param patientResourceRequest
	 * @param Authorization
	 * @return (OPConsultRecord - Bundle){ Patient || Encounter || Organization ||
	 *         Condition || Observation || AllergyIntolerance || FamilyMemberHistory
	 *         || ServiceRequest || MedicationStatement || Appointment ||
	 *         DocumentReference}
	 * 
	 */
	@Operation(summary = "Get OP consult record bundle")
	@PostMapping(value = { "/OPConsultRecord" })
	public String getPatientResource(@RequestBody ResourceRequestHandler patientResourceRequest,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		try {
			String s = opConsultRecordBundle.getOPConsultRecordBundle(patientResourceRequest, null);

			response.setResponse(s);
		} catch (FHIRException e) {
			logger.error("error in creating OP Consult Record bundle : " + e.getMessage());
			response.setError(5000, "error in creating OP Consult Record bundle : " + e.getMessage());
		}
		return response.toString();
	}

	/***
	 * 
	 * @param patientResourceRequest
	 * @param Authorization
	 * @return (DiagnosticReportRecord - Bundle){ Patient || Encounter ||
	 *         Practitioner || Organization || DiagnosticReportLab ||
	 *         DocumentReference}
	 * 
	 */
	@Operation(summary = "Get diagnostic report record bundle")
	@PostMapping(value = { "/DiagnosticReportRecord" })
	public String getDiagnosticReportRecord(@RequestBody ResourceRequestHandler patientResourceRequest,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		try {
			String s = diagnosticReportRecord.getDiagnosticReportRecordBundle(patientResourceRequest, null);

			response.setResponse(s);
		} catch (FHIRException e) {
			logger.error("error in creating Diagnostic Report Record bundle : " + e.getMessage());
			response.setError(5000, "error in creating Diagnostic Report Record bundle : " + e.getMessage());
		}
		return response.toString();
	}

	/***
	 * 
	 * @param patientResourceRequest
	 * @param Authorization
	 * @return (PrescriptionRecord - Bundle){ Patient || Encounter || Practitioner
	 *         || Organization || MedicationRequest || Binary}
	 * 
	 */
	@Operation(summary = "Get prescription record")
	@PostMapping(value = { "/PrescriptionRecord" })
	public String getPrescriptionRecord(@RequestBody ResourceRequestHandler patientResourceRequest,
			@RequestHeader(value = "Authorization") String Authorization) {

		OutputResponse response = new OutputResponse();
		try {
			String s = prescriptionRecordBundle.getPrescriptionRecordBundle(patientResourceRequest, null);

			response.setResponse(s);
		} catch (FHIRException e) {
			logger.error("error in creating Prescription Record bundle : " + e.getMessage());
			response.setError(5000, "error in creating Prescription Record bundle : " + e.getMessage());
		}
		return response.toString();
	}

}
