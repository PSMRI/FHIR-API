package com.wipro.fhir.service.resource_model;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.MedicalHistoryDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;

@Service
public class MedicalHistoryResource {

	@Autowired
	private CommonService commonService;
	@Autowired
	private MedicalHistoryDataModel medicalHistoryDataModel;
	
	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;

	public List<MedicationStatement> getMedicalHistory(Patient patient, ResourceRequestHandler resourceRequestHandler) {

		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo.callMedicalHistorySp(resourceRequestHandler.getVisitCode());

		List<MedicalHistoryDataModel> medicalList = medicalHistoryDataModel.getMedicalList(rsObjList);

		return generateMedicalHistoryResource(patient, medicalList);

	}

	private List<MedicationStatement> generateMedicalHistoryResource(Patient patient, List<MedicalHistoryDataModel> msList) {

		List<MedicationStatement> msResourceList = new ArrayList<>();

		// For every medication entry, create a new MedicationStatement and add to list
		int index = 0;
		for(MedicalHistoryDataModel med: msList) {
			index++;

			MedicationStatement ms = new MedicationStatement();

			ms.setId("MedicationRequest-" + index + "/" + commonService.getUUID()); 
			ms.setSubject(new Reference(patient.getIdElement().getValue()));

			ms.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationStatement");
			ms.setStatus(MedicationStatement.MedicationStatementStatus.COMPLETED);        

			CodeableConcept medCC = new CodeableConcept();
			medCC.addCoding(new Coding()
					.setSystem("http://snomed.info/sct")
					.setCode(" ")
					.setDisplay(med.getCurrentMedication())); // scts code so kept only the name

			medCC.setText(med.getCurrentMedication());
			ms.setMedication(medCC);
			ms.setDateAsserted(med.getCreatedDate());

			msResourceList.add(ms);
		}

		return msResourceList;
	}

}
