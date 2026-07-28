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
package com.wipro.fhir.service.bundle_creation;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wipro.fhir.utils.pdf.PdfWriter;

/**
 * Renders the OP consultation case sheet with PDFBox, from the FHIR resources the
 * bundle builder already assembled.
 */
@Service
public class RecordPdfServiceImpl implements RecordPdfService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public byte[] getOpConsultPdf(Patient patient, Organization organization, Practitioner practitioner,
			List<Condition> chiefComplaints, List<Condition> diagnoses, List<AllergyIntolerance> allergies,
			FamilyMemberHistory familyHistory, List<MedicationStatement> medications,
			List<Observation> vitals) {

		try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			PdfWriter writer = new PdfWriter(document);

			writer.heading(organization != null && organization.getName() != null ? organization.getName()
					: "Health Facility");
			writer.title("OP Consultation Record");
			writer.keyValue("Generated on", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));

			writer.section("Patient");
			if (patient != null) {
				writer.keyValue("Name", nameOf(patient.hasName() ? patient.getNameFirstRep() : null));
				if (patient.hasGender()) {
					writer.keyValue("Gender", patient.getGender().getDisplay());
				}
				if (patient.hasBirthDate()) {
					writer.keyValue("Date of birth",
							new SimpleDateFormat("dd-MM-yyyy").format(patient.getBirthDate()));
				}
				if (patient.hasIdentifier() && patient.getIdentifierFirstRep().hasValue()) {
					writer.keyValue("ABHA / ID", patient.getIdentifierFirstRep().getValue());
				}
				if (patient.hasTelecom() && patient.getTelecomFirstRep().hasValue()) {
					writer.keyValue("Phone", patient.getTelecomFirstRep().getValue());
				}
			}
			if (practitioner != null && practitioner.hasName()) {
				writer.keyValue("Attended by", nameOf(practitioner.getNameFirstRep()));
			}

			writer.bulletSection("Vitals & Anthropometry", observationTexts(vitals));
			writer.bulletSection("Chief Complaints", conditionTexts(chiefComplaints));
			writer.bulletSection("Diagnosis", conditionTexts(diagnoses));
			writer.bulletSection("Allergies", allergyTexts(allergies));
			writer.bulletSection("Medications", medicationTexts(medications));
			writer.bulletSection("Family History", familyHistoryTexts(familyHistory));

			writer.footNote("This is a computer generated record shared over ABDM.");
			writer.close();

			document.save(out);
			byte[] pdf = out.toByteArray();
			logger.info("Case sheet PDF generated (" + pdf.length + " bytes)");
			return pdf;
		} catch (Exception e) {
			// A missing PDF must never break bundle creation — the structured record is
			// still worth sharing.
			logger.error("Could not generate case sheet PDF, bundle will be sent without one: " + e.getMessage(), e);
			return null;
		}
	}

	private String nameOf(HumanName name) {
		if (name == null) {
			return "";
		}
		if (name.hasText()) {
			return name.getText();
		}
		StringBuilder sb = new StringBuilder();
		name.getGiven().forEach(given -> sb.append(given.getValue()).append(" "));
		if (name.hasFamily()) {
			sb.append(name.getFamily());
		}
		return sb.toString().trim();
	}

	private String textOf(CodeableConcept concept) {
		if (concept == null) {
			return null;
		}
		if (concept.hasText()) {
			return concept.getText();
		}
		if (concept.hasCoding() && concept.getCodingFirstRep().hasDisplay()) {
			return concept.getCodingFirstRep().getDisplay();
		}
		return null;
	}

	private List<String> conditionTexts(List<Condition> conditions) {
		List<String> lines = new ArrayList<>();
		if (conditions != null) {
			for (Condition condition : conditions) {
				String text = textOf(condition.getCode());
				if (text != null) {
					lines.add(text);
				}
			}
		}
		return lines;
	}

	private List<String> observationTexts(List<Observation> observations) {
		List<String> lines = new ArrayList<>();
		if (observations == null) {
			return lines;
		}
		for (Observation observation : observations) {
			String label = textOf(observation.getCode());
			String value = observationValue(observation);
			if (label == null || value == null) {
				continue;
			}
			lines.add(label + ": " + value);
		}
		return lines;
	}

	private String observationValue(Observation observation) {
		if (observation.hasValueStringType()) {
			return observation.getValueStringType().getValue();
		}
		if (observation.hasValueQuantity()) {
			Quantity quantity = observation.getValueQuantity();
			String value = quantity.hasValue() ? quantity.getValue().toPlainString() : null;
			if (value == null) {
				return null;
			}
			String unit = quantity.hasUnit() ? quantity.getUnit() : quantity.getCode();
			return unit != null ? value + " " + unit : value;
		}
		if (observation.hasValueCodeableConcept()) {
			return textOf(observation.getValueCodeableConcept());
		}
		return null;
	}

	private List<String> allergyTexts(List<AllergyIntolerance> allergies) {
		List<String> lines = new ArrayList<>();
		if (allergies != null) {
			for (AllergyIntolerance allergy : allergies) {
				String text = textOf(allergy.getCode());
				if (text != null) {
					lines.add(text);
				}
			}
		}
		return lines;
	}

	private List<String> medicationTexts(List<MedicationStatement> medications) {
		List<String> lines = new ArrayList<>();
		if (medications != null) {
			for (MedicationStatement medication : medications) {
				String text = medication.hasMedicationCodeableConcept()
						? textOf(medication.getMedicationCodeableConcept())
						: null;
				if (text == null) {
					continue;
				}
				if (medication.hasDosage() && medication.getDosageFirstRep().hasText()) {
					text = text + " — " + medication.getDosageFirstRep().getText();
				}
				lines.add(text);
			}
		}
		return lines;
	}

	private List<String> familyHistoryTexts(FamilyMemberHistory familyHistory) {
		List<String> lines = new ArrayList<>();
		if (familyHistory == null || familyHistory.getId() == null) {
			return lines;
		}
		String relationship = textOf(familyHistory.getRelationship());
		for (FamilyMemberHistory.FamilyMemberHistoryConditionComponent condition : familyHistory.getCondition()) {
			String text = textOf(condition.getCode());
			if (text != null) {
				lines.add(relationship != null ? relationship + ": " + text : text);
			}
		}
		return lines;
	}
}
