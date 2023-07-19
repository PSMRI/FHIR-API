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
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.LabTestAndComponentModel;
import com.wipro.fhir.data.resource_model.VitalsAnthropometryModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class ObservationResource {

	@Autowired
	private CommonService commonService;
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private LabTestAndComponentModel observationDataModel;
	@Autowired
	private VitalsAnthropometryModel vitalsAnthropometryModel;

	public List<Observation> getObservationVitals(Patient patient, Encounter encounter,
			ResourceRequestHandler resourceRequestHandler) {

		List<Object[]> vitals = patientEligibleForResourceCreationRepo.callVitals_AnthropometrySP(
				resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<VitalsAnthropometryModel> vitalsList = vitalsAnthropometryModel.getVitalsAndAnthropometryList(vitals);
		return generateObservationResourceVitals(patient, vitalsList);

	}

	public Map<Integer, List<Observation>> getObservationLab(Patient patient,
			ResourceRequestHandler resourceRequestHandler) {

		List<Object[]> labTest = patientEligibleForResourceCreationRepo
				.callObservationSP(resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<LabTestAndComponentModel> labTestList = observationDataModel.getlabTestAndComponentList(labTest);
		return generateObservationResourceLabComponent(patient, labTestList);

	}

	private List<Observation> generateObservationResourceVitals(Patient patient,
			List<VitalsAnthropometryModel> vitalsAndAnthroList) {
		Observation observation;
		VitalsAnthropometryModel vital;
		List<Observation> observationList = new ArrayList<>();
		if (vitalsAndAnthroList.size() > 0) {
			vital = vitalsAndAnthroList.get(0);

			if (vital != null) {
				CodeableConcept ccVital;

				if (vital.getTemperature() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Body temperature");
					observation.setCode(ccVital);

					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getTemperature() + " [degF]");

					observation.setValue(st);
					observationList.add(observation);

				}
				if (vital.getPulseRate() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Pulse Rate");
					observation.setCode(ccVital);

					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getPulseRate() + " / min");

					observation.setValue(st);
					observationList.add(observation);

				}

				if (vital.getRespiratoryRate() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Respiratory Rate");
					observation.setCode(ccVital);

					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getRespiratoryRate() + " / min");

					observation.setValue(st);
					observationList.add(observation);

				}
				if (vital.getSystolicBP_1stReading() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Systolic blood pressure");
					observation.setCode(ccVital);
					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getSystolicBP_1stReading() + " mm[Hg]");
					observation.setValue(st);
					observationList.add(observation);

				}
				if (vital.getDiastolicBP_1stReading() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Diastolic blood pressure");
					observation.setCode(ccVital);
					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getDiastolicBP_1stReading() + " mm[Hg]");
					observation.setValue(st);
					observationList.add(observation);

				}
				if (vital.getHeight_cm() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Body height");
					observation.setCode(ccVital);
					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getHeight_cm() + " cm");
					observation.setValue(st);
					observationList.add(observation);

				}
				if (vital.getWeight_Kg() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Body weight");
					observation.setCode(ccVital);
					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getWeight_Kg() + " kg");
					observation.setValue(st);
					observationList.add(observation);

				}
				if (vital.getBMI() != null) {
					observation = new Observation();
					observation = getObservationTemp(observation, vital);

					ccVital = new CodeableConcept();
					ccVital.setText("Body mass index");
					observation.setCode(ccVital);
					StringType st = new StringType();
					st.setId("valueString");
					st.setValue(vital.getBMI() + " kg/m2");
					observation.setValue(st);
					observationList.add(observation);

				}

			}
		}

		return observationList;

	}

	private Observation getObservationTemp(Observation observation, VitalsAnthropometryModel vital) {
		// issued
		observation.setId("Observation/" + commonService.getUUID());
		observation.setStatus(ObservationStatus.FINAL);
		observation.setIssued(vital.getCreatedDate());
		
		DateTimeType dtt = new DateTimeType(vital.getCreatedDate());

		observation.setEffective(dtt);

		return observation;
	}

	private Map<Integer, List<Observation>> generateObservationResourceLabComponent(Patient patient,
			List<LabTestAndComponentModel> observationList) {
		Observation observation;

		Map<Integer, List<Observation>> resourceMapTestWise = new HashMap<>();
		List<Observation> resourceList;

		CodeableConcept cc;
		Coding c;

		ArrayList<Reference> performerList;

		StringBuffer sb;
		CodeableConcept resultCC;
		for (LabTestAndComponentModel o : observationList) {
			sb = new StringBuffer();

			observation = new Observation();
			String UUID = commonService.getUUID();
			observation.setId("Observation/" + UUID);
			observation.setStatus(ObservationStatus.FINAL);

			DateTimeType dtt = new DateTimeType(o.getCreatedDate());
			observation.setEffective(dtt);

			
			cc = new CodeableConcept();
			c = new Coding();

			c.setCode(o.getLoincCode());
			c.setDisplay(o.getLoincValue());

			cc.addCoding(c);
			cc.setText(o.getTestComponentName());

			observation.setCode(cc);

			// referance - patient
			observation.setSubject(new Reference(patient.getIdElement().getValue()));

			// reference performer
			performerList = new ArrayList<>();
			Reference performer = new Reference(patient.getIdElement().getValue());
			performerList.add(performer);
			observation.setPerformer(performerList);

			
			if (o.getTestResultValue() != null)
				sb.append(o.getTestResultValue());
			if (o.getTestResultUnit() != null)
				sb.append(" ").append(o.getTestResultUnit());

			resultCC = new CodeableConcept();
			resultCC.setText(sb.toString());

			observation.setValue(resultCC);

			if (resourceMapTestWise != null && resourceMapTestWise.containsKey(o.getProcedureID())) {
				resourceMapTestWise.get(o.getProcedureID()).add(observation);
			} else {
				resourceList = new ArrayList<>();
				resourceList.add(observation);
				resourceMapTestWise.put(o.getProcedureID(), resourceList);
			}

		}

		return resourceMapTestWise;
	}

}
