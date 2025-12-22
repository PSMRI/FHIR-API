package com.wipro.fhir.service.resource_model;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.ImmunizationDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class ImmunizationResource {

	@Autowired
	private CommonService commonService;

	@Autowired
	private ImmunizationDataModel immunizationDataModel;

	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;

	public List<Immunization> getImmunizations(Patient patient, ResourceRequestHandler resourceRequestHandler)
			throws FHIRException {

		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo.callImmunizationSP(
				resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<ImmunizationDataModel> immunizationList = immunizationDataModel.getImmunizationList(rsObjList);

		// Build FHIR Immunization resources
		return generateImmunizationResource(patient, immunizationList);
	}

	private List<Immunization> generateImmunizationResource(Patient patient, List<ImmunizationDataModel> imList) {

		List<Immunization> immResourceList = new ArrayList<>();

		int index = 0;
		for (ImmunizationDataModel im : imList) {
			index++;

			Immunization immune = new Immunization();

			// Id style similar to your MedicationStatement example
			immune.setId("Immunization-" + index + "/" + commonService.getUUID());

			// Subject (patient)
			immune.setPatient(new Reference(patient.getIdElement().getValue()));

			// NRCeS Immunization profile
			immune.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Immunization");

			// Status (completed if we have a receivedDate, otherwise not-done)
			if (im.getReceivedDate() != null) {
				immune.setStatus(Immunization.ImmunizationStatus.COMPLETED);
				immune.setOccurrence(new DateTimeType(im.getReceivedDate()));
			} else {
				// If you prefer to exclude not-done, comment the next line and add a
				// `continue;`
				immune.setStatus(Immunization.ImmunizationStatus.NOTDONE);
			}

			// Vaccine code: prefer SNOMED if provided, else text fallback
			CodeableConcept vaccineCC = new CodeableConcept();
			if (im.getSctcode() != null && !im.getSctcode().isEmpty()) {
				vaccineCC.addCoding(new Coding().setSystem("http://snomed.info/sct").setCode(im.getSctcode())
						.setDisplay(im.getSctTerm() != null ? im.getSctTerm() : im.getVaccineName()));
			}
			// Always set text for human readability
			vaccineCC.setText(im.getVaccineName());
			immune.setVaccineCode(vaccineCC);

			if (im.getCreatedDate() != null) {
				immune.setRecorded(im.getCreatedDate());
			}

			// Optional free-text notes for age schedule and facility
			if (im.getDefaultReceivingAge() != null && !im.getDefaultReceivingAge().isEmpty()) {
				immune.addNote(new Annotation().setText("Schedule: " + im.getDefaultReceivingAge()));
			}
			if (im.getReceivedFacilityName() != null && !im.getReceivedFacilityName().isEmpty()) {
				immune.addNote(new Annotation().setText("Facility: " + im.getReceivedFacilityName()));
			}

			immResourceList.add(immune);
		}

		return immResourceList;
	}

}
