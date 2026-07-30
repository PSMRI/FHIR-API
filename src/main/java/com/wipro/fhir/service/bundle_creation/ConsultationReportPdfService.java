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
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.DocumentReference.ReferredDocumentStatus;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.service.resource_model.AllergyIntoleranceResource;
import com.wipro.fhir.service.resource_model.ConditionResource;
import com.wipro.fhir.service.resource_model.DiagnosticReportResource;
import com.wipro.fhir.service.resource_model.EncounterResource;
import com.wipro.fhir.service.resource_model.FamilyMemberHistoryResource;
import com.wipro.fhir.service.resource_model.ImmunizationResource;
import com.wipro.fhir.service.resource_model.MedicalHistoryResource;
import com.wipro.fhir.service.resource_model.MedicationRequestResource;
import com.wipro.fhir.service.resource_model.ObservationResource;
import com.wipro.fhir.service.resource_model.OrganizationResource;
import com.wipro.fhir.service.resource_model.PractitionerResource;

/***
 * Builds the consolidated consultation report for a visit as a PDF and wraps it
 * in an NDHM DocumentReference, so the record can be downloaded from the ABHA /
 * PHR app instead of only being rendered as structured FHIR entries.
 *
 * The report is a single document per visit covering every record type AMRIT
 * shares - vitals and anthropometry (BP, pulse, temperature, height, weight,
 * BMI), chief complaints, diagnosis, allergies, medical and family history,
 * prescribed medications, lab results and immunizations.
 *
 * It is attached to the OPConsultation bundle only. Attaching it to every bundle
 * of a visit made the ABHA app show the same "Consultation Report" download on
 * each record page, and the other bundles are left exactly as they were.
 *
 * Everything the feature needs lives in this one class on purpose: the bundle
 * builders only ask for a DocumentReference, so the file can be dropped into
 * another AMRIT repo without dragging along helper classes or touching shared
 * code. Failures never propagate - a visit without a PDF is shared exactly as it
 * was before this feature existed.
 ***/
@Service
public class ConsultationReportPdfService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static final String SNOMED = "http://snomed.info/sct";
	private static final String PDF_CONTENT_TYPE = "application/pdf";
	private static final String REPORT_TITLE = "Consultation Report";
	private static final String NOT_RECORDED = "Not recorded";

	/**
	 * All bundles for a visit are built back to back from the same snapshot, so the
	 * report is rendered once and reused - both to avoid re-running every stored
	 * procedure six times and so each bundle carries an identical document. Bounded
	 * to the visit in hand and to a few minutes, since a re-run must pick up data
	 * entered in the meantime.
	 */
	private static final long CACHE_TTL_MS = 5 * 60 * 1000L;

	private volatile BigInteger cachedVisitCode;
	private volatile byte[] cachedPdf;
	private volatile long cachedAt;

	@Autowired
	private PractitionerResource practitionerResource;

	@Autowired
	private OrganizationResource organizationResource;

	@Autowired
	private ConditionResource conditionResource;

	@Autowired
	private EncounterResource encounterResource;

	@Autowired
	private AllergyIntoleranceResource allergyIntoleranceResource;

	@Autowired
	private FamilyMemberHistoryResource familyMemberHistoryResource;

	@Autowired
	private MedicalHistoryResource medicalHistoryResource;

	@Autowired
	private ObservationResource observationResource;

	@Autowired
	private MedicationRequestResource medicationRequestResource;

	@Autowired
	private DiagnosticReportResource diagnosticReportResource;

	@Autowired
	private ImmunizationResource immunizationResource;

	/***
	 * The one call the bundle builders make.
	 *
	 * @param resourceRequestHandler visit being shared
	 * @param patient                Patient resource already built for the bundle,
	 *                               so the DocumentReference points at the same one
	 * @return DocumentReference carrying the report as a base64 PDF attachment, or
	 *         null when no report could be produced - the caller then builds the
	 *         bundle exactly as before
	 ***/
	public DocumentReference getConsultationReportDocumentReference(ResourceRequestHandler resourceRequestHandler,
			Patient patient) {

		byte[] pdf = getConsultationReportPdf(resourceRequestHandler, patient);
		if (pdf == null || pdf.length == 0)
			return null;

		DocumentReference documentReference = new DocumentReference();
		documentReference.setId("DocumentReference/" + UUID.randomUUID().toString());

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference");
		documentReference.setMeta(meta);

		documentReference.setStatus(DocumentReferenceStatus.CURRENT);
		documentReference.setDocStatus(ReferredDocumentStatus.FINAL);

		CodeableConcept type = new CodeableConcept(
				new Coding(SNOMED, "371530004", "Clinical consultation report"));
		type.setText(REPORT_TITLE);
		documentReference.setType(type);

		if (patient != null)
			documentReference.setSubject(new Reference(patient.getIdElement().getValue()).setDisplay("Patient"));

		Attachment attachment = new Attachment();
		attachment.setContentType(PDF_CONTENT_TYPE);
		attachment.setLanguage("en-IN");
		// HAPI base64-encodes Attachment.data on serialization, which is what the
		// profile requires and what the PHR app decodes to offer the download.
		attachment.setData(pdf);
		attachment.setTitle(REPORT_TITLE);
		attachment.setCreationElement(new DateTimeType(new Date()));
		documentReference.addContent().setAttachment(attachment);

		logger.info("Consultation report DocumentReference created for visit "
				+ resourceRequestHandler.getVisitCode() + " (" + pdf.length + " bytes of PDF, "
				+ Base64.getEncoder().encodeToString(pdf).length() + " base64 chars)");

		return documentReference;
	}

	/***
	 * Wires the document into a Composition as its own "Document Reference" section.
	 *
	 * A document Bundle must have every entry reachable from the Composition, so the
	 * report has to be referenced as well as carried. The OPConsultRecord profile
	 * defines a Document Reference section for exactly this.
	 *
	 * Does nothing when there is no document, leaving the Composition untouched.
	 ***/
	public void addDocumentReferenceSection(Composition composition, DocumentReference documentReference) {
		if (composition == null || documentReference == null)
			return;

		SectionComponent section = new SectionComponent();
		section.setTitle("Document Reference");
		section.setCode(new CodeableConcept(new Coding(SNOMED, "371530004", "Clinical consultation report")));
		section.addEntry(new Reference(documentReference.getIdElement().getValue()).setType("DocumentReference")
				.setDisplay(REPORT_TITLE));

		composition.addSection(section);
	}

	/*** Renders the report, reusing the last one when it is for the same visit. ***/
	private byte[] getConsultationReportPdf(ResourceRequestHandler resourceRequestHandler, Patient patient) {
		BigInteger visitCode = resourceRequestHandler.getVisitCode();

		if (visitCode != null && visitCode.equals(cachedVisitCode) && cachedPdf != null
				&& (System.currentTimeMillis() - cachedAt) < CACHE_TTL_MS) {
			logger.info("Reusing consultation report already rendered for visit " + visitCode);
			return cachedPdf;
		}

		byte[] pdf = renderConsultationReport(resourceRequestHandler, patient);

		if (pdf != null && visitCode != null) {
			cachedPdf = pdf;
			cachedAt = System.currentTimeMillis();
			cachedVisitCode = visitCode;
		}
		return pdf;
	}

	private byte[] renderConsultationReport(ResourceRequestHandler resourceRequestHandler, Patient patient) {

		try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			// Read through the same resource builders the bundles use, so the PDF can
			// never disagree with the FHIR entries it ships alongside.
			Practitioner practitioner = practitionerResource.getPractitionerResource(resourceRequestHandler);
			Organization organization = organizationResource.getOrganizationResource(resourceRequestHandler);

			List<Condition> chiefComplaints = conditionResource.getCondition(patient, resourceRequestHandler,
					"chiefcomplaints");
			List<Condition> diagnoses = conditionResource.getCondition(patient, resourceRequestHandler, "diagnosis");
			Encounter encounter = encounterResource.getEncounterResource(patient, resourceRequestHandler,
					chiefComplaints, diagnoses);
			List<AllergyIntolerance> allergies = allergyIntoleranceResource.getAllergyIntolerance(patient, encounter,
					resourceRequestHandler, practitioner);
			FamilyMemberHistory familyHistory = familyMemberHistoryResource.getFamilyMemberHistory(patient,
					resourceRequestHandler);
			List<MedicationStatement> medicalHistory = medicalHistoryResource.getMedicalHistory(patient,
					resourceRequestHandler);
			List<Observation> vitals = observationResource.getObservationVitals(patient, resourceRequestHandler);
			List<MedicationRequest> prescriptions = medicationRequestResource.getMedicationRequest(patient,
					resourceRequestHandler, practitioner, null);
			Map<Integer, List<Observation>> labObservations = observationResource.getObservationLab(patient,
					resourceRequestHandler);
			List<DiagnosticReport> diagnosticReports = diagnosticReportResource.getDiagnosticReport(patient,
					new Encounter(), resourceRequestHandler, labObservations);
			List<Immunization> immunizations = immunizationResource.getImmunizations(patient, resourceRequestHandler);

			PageWriter writer = new PageWriter(document);

			writer.heading(
					organization != null && organization.hasName() ? organization.getName() : "Health Facility");
			writer.title(REPORT_TITLE);
			writer.keyValue("Visit code", asText(resourceRequestHandler.getVisitCode()));
			writer.keyValue("Generated on", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date()));

			writer.section("Patient Details");
			if (patient != null) {
				writer.keyValue("Name", nameOf(patient.hasName() ? patient.getNameFirstRep() : null));
				if (patient.hasGender())
					writer.keyValue("Gender", patient.getGender().getDisplay());
				if (patient.hasBirthDate())
					writer.keyValue("Date of birth",
							new SimpleDateFormat("dd-MM-yyyy").format(patient.getBirthDate()));
				writer.keyValue("ABHA number", abhaNumberOf(patient));
			}
			writer.keyValue("Beneficiary ID", asText(resourceRequestHandler.getBeneficiaryID()));
			if (practitioner != null && practitioner.hasName())
				writer.keyValue("Attended by", nameOf(practitioner.getNameFirstRep()));
			if (encounter != null && encounter.hasPeriod() && encounter.getPeriod().hasStart())
				writer.keyValue("Visit date",
						new SimpleDateFormat("dd-MM-yyyy HH:mm").format(encounter.getPeriod().getStart()));

			writer.bulletSection("Vitals & Anthropometry", observationLines(vitals));
			writer.bulletSection("Chief Complaints", conditionLines(chiefComplaints));
			writer.bulletSection("Diagnosis", conditionLines(diagnoses));
			writer.bulletSection("Allergies", allergyLines(allergies));
			writer.bulletSection("Medical History", medicationStatementLines(medicalHistory));
			writer.bulletSection("Family History", familyHistoryLines(familyHistory));
			writer.bulletSection("Prescribed Medications", medicationRequestLines(prescriptions));
			writer.bulletSection("Investigations & Lab Results", labResultLines(labObservations, diagnosticReports));
			writer.bulletSection("Immunizations", immunizationLines(immunizations));

			writer.footNote("This is a computer generated consultation report shared over ABDM.");

			// The page stream has to be closed before the document is saved.
			writer.close();
			document.save(out);

			byte[] pdf = out.toByteArray();
			logger.info("Consultation report rendered for visit " + resourceRequestHandler.getVisitCode() + " ("
					+ pdf.length + " bytes)");
			return pdf;

		} catch (Exception e) {
			// The structured record is still worth sharing, so the bundle goes out
			// without a DocumentReference rather than failing.
			logger.error("Consultation report PDF could not be generated for visit "
					+ resourceRequestHandler.getVisitCode() + ", bundle will be shared without one : " + e.getMessage(),
					e);
			return null;
		}
	}

	// ------------------------------------------------------------------
	// Turning FHIR resources into report lines
	// ------------------------------------------------------------------

	private List<String> observationLines(List<Observation> observations) {
		List<String> lines = new ArrayList<>();
		if (observations == null)
			return lines;

		for (Observation observation : observations) {
			String label = textOf(observation.getCode());
			String value = observationValue(observation);
			if (label == null || value == null)
				continue;
			lines.add(label + " : " + value);
		}
		return lines;
	}

	/*** AMRIT records vitals as strings ("120 mm[Hg]") and lab results as a coded
	 * text, so all three value flavours have to be handled. ***/
	private String observationValue(Observation observation) {
		if (observation.hasValueStringType())
			return observation.getValueStringType().getValue();

		if (observation.hasValueQuantity()) {
			Quantity quantity = observation.getValueQuantity();
			if (!quantity.hasValue())
				return null;
			String value = quantity.getValue().toPlainString();
			String unit = quantity.hasUnit() ? quantity.getUnit() : quantity.getCode();
			return unit != null ? value + " " + unit : value;
		}

		if (observation.hasValueCodeableConcept())
			return textOf(observation.getValueCodeableConcept());

		return null;
	}

	private List<String> conditionLines(List<Condition> conditions) {
		List<String> lines = new ArrayList<>();
		if (conditions == null)
			return lines;

		for (Condition condition : conditions) {
			String text = textOf(condition.getCode());
			if (text != null)
				lines.add(text);
		}
		return lines;
	}

	private List<String> allergyLines(List<AllergyIntolerance> allergies) {
		List<String> lines = new ArrayList<>();
		if (allergies == null)
			return lines;

		for (AllergyIntolerance allergy : allergies) {
			String text = textOf(allergy.getCode());
			if (text == null)
				continue;
			// The reaction is captured as a note, e.g. "Having these side effects : rash".
			if (allergy.hasNote() && allergy.getNoteFirstRep().hasText())
				text = text + " (" + allergy.getNoteFirstRep().getText() + ")";
			lines.add(text);
		}
		return lines;
	}

	private List<String> medicationStatementLines(List<MedicationStatement> medicationStatements) {
		List<String> lines = new ArrayList<>();
		if (medicationStatements == null)
			return lines;

		for (MedicationStatement medicationStatement : medicationStatements) {
			String text = medicationStatement.hasMedicationCodeableConcept()
					? textOf(medicationStatement.getMedicationCodeableConcept())
					: null;
			if (text != null)
				lines.add(text);
		}
		return lines;
	}

	private List<String> medicationRequestLines(List<MedicationRequest> medicationRequests) {
		List<String> lines = new ArrayList<>();
		if (medicationRequests == null)
			return lines;

		for (MedicationRequest medicationRequest : medicationRequests) {
			String text = medicationRequest.hasMedicationCodeableConcept()
					? textOf(medicationRequest.getMedicationCodeableConcept())
					: null;
			if (text == null)
				continue;

			for (Dosage dosage : medicationRequest.getDosageInstruction()) {
				if (dosage.hasText())
					text = text + " - " + dosage.getText();
			}
			lines.add(text);
		}
		return lines;
	}

	private List<String> familyHistoryLines(FamilyMemberHistory familyHistory) {
		List<String> lines = new ArrayList<>();
		if (familyHistory == null || !familyHistory.hasCondition())
			return lines;

		// Relationship codings and condition components are built in the same order,
		// one relation per family member, so they line up by index.
		List<Coding> relations = familyHistory.hasRelationship() ? familyHistory.getRelationship().getCoding()
				: new ArrayList<>();
		List<FamilyMemberHistoryConditionComponent> conditions = familyHistory.getCondition();

		for (int i = 0; i < conditions.size(); i++) {
			String relation = i < relations.size() ? relations.get(i).getDisplay() : null;
			for (Coding disease : conditions.get(i).getCode().getCoding()) {
				String text = disease.hasDisplay() ? disease.getDisplay() : disease.getCode();
				if (text == null || text.trim().isEmpty())
					continue;
				lines.add(relation != null ? relation + " : " + text : text);
			}
		}
		return lines;
	}

	private List<String> labResultLines(Map<Integer, List<Observation>> labObservations,
			List<DiagnosticReport> diagnosticReports) {
		List<String> lines = new ArrayList<>();
		if (labObservations == null || labObservations.isEmpty())
			return lines;

		for (List<Observation> observations : labObservations.values())
			lines.addAll(observationLines(observations));

		if (!lines.isEmpty() && diagnosticReports != null && !diagnosticReports.isEmpty()) {
			for (DiagnosticReport diagnosticReport : diagnosticReports) {
				if (diagnosticReport.hasConclusion() && !diagnosticReport.getConclusion().trim().isEmpty())
					lines.add("Conclusion : " + diagnosticReport.getConclusion());
			}
		}
		return lines;
	}

	private List<String> immunizationLines(List<Immunization> immunizations) {
		List<String> lines = new ArrayList<>();
		if (immunizations == null)
			return lines;

		for (Immunization immunization : immunizations) {
			String text = immunization.hasVaccineCode() ? textOf(immunization.getVaccineCode()) : null;
			if (text == null)
				continue;
			if (immunization.hasOccurrenceDateTimeType() && immunization.getOccurrenceDateTimeType().getValue() != null)
				text = text + " on "
						+ new SimpleDateFormat("dd-MM-yyyy").format(immunization.getOccurrenceDateTimeType().getValue());
			if (immunization.hasStatus())
				text = text + " (" + immunization.getStatus().getDisplay() + ")";
			lines.add(text);
		}
		return lines;
	}

	private String nameOf(HumanName name) {
		if (name == null)
			return null;
		if (name.hasText())
			return name.getText();

		StringBuilder sb = new StringBuilder();
		name.getGiven().forEach(given -> sb.append(given.getValue()).append(" "));
		if (name.hasFamily())
			sb.append(name.getFamily());

		String text = sb.toString().trim();
		return text.isEmpty() ? null : text;
	}

	/*** PatientResource carries ABHA in identifier.type.coding.display, so read
	 * that before falling back to identifier.value. ***/
	private String abhaNumberOf(Patient patient) {
		for (Identifier identifier : patient.getIdentifier()) {
			if (identifier.hasType() && identifier.getType().hasCoding()
					&& identifier.getType().getCodingFirstRep().hasDisplay())
				return identifier.getType().getCodingFirstRep().getDisplay();
			if (identifier.hasValue())
				return identifier.getValue();
		}
		return null;
	}

	private String textOf(CodeableConcept concept) {
		if (concept == null)
			return null;
		if (concept.hasText() && !concept.getText().trim().isEmpty())
			return concept.getText();
		if (concept.hasCoding() && concept.getCodingFirstRep().hasDisplay())
			return concept.getCodingFirstRep().getDisplay();
		return null;
	}

	private String asText(Object value) {
		return value != null ? value.toString() : null;
	}

	/***
	 * Writes the report top to bottom over PDFBox: keeps the cursor, wraps long
	 * lines and starts a new page when it runs out of room, so the caller above just
	 * emits headings, key/value pairs and bullet lists.
	 *
	 * One instance per report - the cursor is per document, and the enclosing
	 * service is a singleton that two requests can enter at once.
	 ***/
	private static final class PageWriter implements AutoCloseable {

		private static final PDRectangle PAGE_SIZE = PDRectangle.A4;
		private static final float MARGIN = 50f;
		private static final float BOTTOM_LIMIT = 60f;
		private static final float LINE_GAP = 4f;
		private static final PDFont REGULAR = PDType1Font.HELVETICA;
		private static final PDFont BOLD = PDType1Font.HELVETICA_BOLD;

		private final PDDocument document;
		private PDPageContentStream content;
		private float cursorY;

		private PageWriter(PDDocument document) throws Exception {
			this.document = document;
			newPage();
		}

		private void newPage() throws Exception {
			close();
			PDPage page = new PDPage(PAGE_SIZE);
			document.addPage(page);
			content = new PDPageContentStream(document, page);
			cursorY = PAGE_SIZE.getHeight() - MARGIN;
		}

		private void heading(String text) throws Exception {
			write(text, BOLD, 15f);
			cursorY -= 6f;
		}

		private void title(String text) throws Exception {
			write(text, BOLD, 12f);
			cursorY -= 2f;
		}

		private void section(String text) throws Exception {
			cursorY -= 10f;
			write(text, BOLD, 11f);
			cursorY -= 2f;
		}

		private void keyValue(String key, String value) throws Exception {
			if (value == null || value.trim().isEmpty())
				return;
			write(key + " : " + value, REGULAR, 10f);
		}

		/*** A section is always printed, so the reader can tell "nothing recorded"
		 * apart from a section that was left out of the report altogether. ***/
		private void bulletSection(String heading, List<String> lines) throws Exception {
			section(heading);
			if (lines == null || lines.isEmpty()) {
				write(NOT_RECORDED, REGULAR, 10f);
				return;
			}
			for (String line : lines) {
				if (line != null && !line.trim().isEmpty())
					write("- " + line.trim(), REGULAR, 10f);
			}
		}

		private void footNote(String text) throws Exception {
			cursorY -= 14f;
			write(text, REGULAR, 8f);
		}

		private void write(String rawText, PDFont font, float fontSize) throws Exception {
			float maxWidth = PAGE_SIZE.getWidth() - (2 * MARGIN);
			for (String line : wrap(sanitize(rawText), font, fontSize, maxWidth)) {
				if (cursorY <= BOTTOM_LIMIT)
					newPage();

				content.beginText();
				content.setFont(font, fontSize);
				content.newLineAtOffset(MARGIN, cursorY);
				content.showText(line);
				content.endText();
				cursorY -= fontSize + LINE_GAP;
			}
		}

		/***
		 * The standard PDF fonts are WinAnsi encoded, so a Devanagari or Tamil name, a
		 * curly quote or an em dash makes showText throw and would cost the whole
		 * report. Substitute what cannot be drawn instead of losing the document.
		 ***/
		private String sanitize(String text) {
			if (text == null)
				return "";

			String normalized = text.replace('—', '-').replace('–', '-').replace('‘', '\'')
					.replace('’', '\'').replace('“', '"').replace('”', '"');

			StringBuilder sb = new StringBuilder(normalized.length());
			for (char c : normalized.toCharArray()) {
				if (c == '\r' || c == '\n' || c == '\t')
					sb.append(' ');
				else if (c >= 32 && c <= 255)
					sb.append(c);
				else
					sb.append('?');
			}
			return sb.toString();
		}

		private List<String> wrap(String text, PDFont font, float fontSize, float maxWidth) throws Exception {
			List<String> lines = new ArrayList<>();
			if (text.isEmpty()) {
				lines.add("");
				return lines;
			}

			StringBuilder current = new StringBuilder();
			for (String word : text.split(" ")) {
				String candidate = current.length() == 0 ? word : current + " " + word;
				if (widthOf(candidate, font, fontSize) <= maxWidth) {
					current.setLength(0);
					current.append(candidate);
					continue;
				}

				if (current.length() > 0) {
					lines.add(current.toString());
					current.setLength(0);
				}

				// A single word wider than the line has to be broken character-wise.
				if (widthOf(word, font, fontSize) > maxWidth) {
					StringBuilder chunk = new StringBuilder();
					for (char c : word.toCharArray()) {
						if (widthOf(chunk.toString() + c, font, fontSize) > maxWidth) {
							lines.add(chunk.toString());
							chunk.setLength(0);
						}
						chunk.append(c);
					}
					current.append(chunk);
				} else {
					current.append(word);
				}
			}

			if (current.length() > 0)
				lines.add(current.toString());

			return lines;
		}

		private float widthOf(String text, PDFont font, float fontSize) throws Exception {
			return font.getStringWidth(text) / 1000 * fontSize;
		}

		@Override
		public void close() throws Exception {
			if (content != null) {
				content.close();
				content = null;
			}
		}
	}
}
