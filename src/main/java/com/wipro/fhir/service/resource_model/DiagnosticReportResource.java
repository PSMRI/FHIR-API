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
package com.wipro.fhir.service.resource_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.DiagnosticReportDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.exception.FHIRException;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class DiagnosticReportResource {

	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private DiagnosticReportDataModel diagnosticReportDataModel;

	private String UUID;
	DiagnosticReport diagnosticReport;

	public List<DiagnosticReport> getDiagnosticReport(Patient patient, Encounter encounter,
			ResourceRequestHandler resourceRequestHandler, Map<Integer, List<Observation>> observationMap)
			throws FHIRException {

		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo.callDiagnosticReportSP(
				resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<DiagnosticReportDataModel> diagnosticList = diagnosticReportDataModel.getDiagnosticReportList(rsObjList);



		return generateDiagnosticReportResource(patient, encounter, observationMap, diagnosticList);
		

	}

	private List<DiagnosticReport> generateDiagnosticReportResource(Patient patient, Encounter encounter,
			Map<Integer, List<Observation>> observationMap, List<DiagnosticReportDataModel> diagnosticList) {

		List<DiagnosticReport> diagnosticReportList = new ArrayList<>();

		Map<Integer, Boolean> testMap = new HashMap<Integer, Boolean>();

		CodeableConcept cc;
		Reference observationRef;
		List<Reference> observationListRef;

		for (DiagnosticReportDataModel d : diagnosticList) {
			if (d.getProcedureID() != null && testMap != null && !testMap.containsKey(d.getProcedureID())) {

				diagnosticReport = new DiagnosticReport();
				UUID = commonService.getUUID();
				diagnosticReport.setId("DiagnosticReport/" + UUID);

				DateTimeType dtt = new DateTimeType(d.getCreatedDate());
				diagnosticReport.setEffective(dtt);

				diagnosticReport.setStatus(DiagnosticReportStatus.FINAL);

				cc = new CodeableConcept();
				cc.setText(d.getProcedureName());

				diagnosticReport.setCode(cc);

				// referance - patient
				diagnosticReport.setSubject(new Reference(patient.getIdElement().getValue()));

				if (observationMap != null && observationMap.size() > 0) {
					List<Observation> observationList = observationMap.get(d.getProcedureID());

					if (observationList != null && observationList.size() > 0) {
						observationListRef = new ArrayList<>();
						for (Observation o : observationList) {
							observationRef = new Reference(o.getIdElement().getValue());
							observationListRef.add(observationRef);
						}

						diagnosticReport.setResult(observationListRef);
					}
				}

				diagnosticReport.setConclusion("");

				diagnosticReportList.add(diagnosticReport);

				testMap.put(d.getProcedureID(), true);
			}
		}
		return diagnosticReportList;

	}
}
