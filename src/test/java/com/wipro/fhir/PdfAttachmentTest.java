package com.wipro.fhir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import com.wipro.fhir.service.bundle_creation.RecordPdfServiceImpl;
import com.wipro.fhir.service.common.CommonService;
import com.wipro.fhir.service.resource_model.DocumentReferenceResource;

import ca.uhn.fhir.context.FhirContext;

class PdfAttachmentTest {

	private DocumentReferenceResource documentReferenceResource() throws Exception {
		DocumentReferenceResource r = new DocumentReferenceResource();
		// only getUUID() is used by the builder
		CommonService stub = (CommonService) java.lang.reflect.Proxy.newProxyInstance(
				CommonService.class.getClassLoader(), new Class[] { CommonService.class },
				(p, m, args) -> "getUUID".equals(m.getName()) ? "test-uuid-0001" : null);
		java.lang.reflect.Field f = DocumentReferenceResource.class.getDeclaredField("commonService");
		f.setAccessible(true);
		f.set(r, stub);
		return r;
	}

	private Observation vital(String label, String value) {
		Observation o = new Observation();
		o.setId("Observation/" + label.hashCode());
		o.setCode(new CodeableConcept().setText(label));
		o.setValue(new StringType(value));
		return o;
	}

	private Condition condition(String text) {
		Condition c = new Condition();
		c.setCode(new CodeableConcept().setText(text));
		return c;
	}

	@Test
	void generatedPdfContainsTheClinicalDataAndOpens() throws Exception {
		Patient patient = new Patient();
		patient.setId("Patient/123");
		patient.addName().setText("S Vanitha");
		patient.setGender(org.hl7.fhir.r4.model.Enumerations.AdministrativeGender.FEMALE);

		Organization org = new Organization();
		org.setName("NP BI Jamui Laxmipur");

		Practitioner doctor = new Practitioner();
		doctor.addName().setText("Dr Rao");

		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.setCode(new CodeableConcept().setText("Penicillin"));

		MedicationStatement med = new MedicationStatement();
		med.setMedication(new CodeableConcept().setText("Paracetamol 500mg"));

		byte[] pdf = new RecordPdfServiceImpl().getOpConsultPdf(patient, org, doctor,
				Collections.singletonList(condition("Fever since 3 days")),
				Collections.singletonList(condition("Viral fever")), Collections.singletonList(allergy),
				new FamilyMemberHistory(), Collections.singletonList(med),
				Arrays.asList(vital("Body height", "160 cm"), vital("Systolic blood pressure", "120 mm[Hg]")));

		assertNotNull(pdf, "a PDF should be produced");
		assertTrue(new String(Arrays.copyOf(pdf, 5)).startsWith("%PDF"), "must be a real PDF");

		// It must actually open, and carry the clinical content.
		try (PDDocument opened = PDDocument.load(new ByteArrayInputStream(pdf))) {
			String text = new PDFTextStripper().getText(opened);
			System.out.println(text);
			assertTrue(opened.getNumberOfPages() >= 1);
			assertTrue(text.contains("OP Consultation Record"));
			assertTrue(text.contains("S Vanitha"));
			assertTrue(text.contains("NP BI Jamui Laxmipur"));
			assertTrue(text.contains("Fever since 3 days"));
			assertTrue(text.contains("Viral fever"));
			assertTrue(text.contains("Penicillin"));
			assertTrue(text.contains("Paracetamol 500mg"));
			assertTrue(text.contains("Body height: 160 cm"), "vitals are on the case sheet");
			assertTrue(text.contains("Systolic blood pressure: 120 mm[Hg]"));
			assertTrue(text.contains("Not recorded"), "empty sections are labelled");
		}
	}

	@Test
	void nonLatinNamesDoNotLoseThePdf() throws Exception {
		// Standard PDF fonts are WinAnsi; a Devanagari name must not blow up the PDF.
		Patient patient = new Patient();
		patient.setId("Patient/9");
		patient.addName().setText("अमृत रोगी");

		byte[] pdf = new RecordPdfServiceImpl().getOpConsultPdf(patient, null, null, null, null, null, null, null, null);

		assertNotNull(pdf, "a non-Latin name must not prevent PDF generation");
		try (PDDocument opened = PDDocument.load(new ByteArrayInputStream(pdf))) {
			assertTrue(new PDFTextStripper().getText(opened).contains("OP Consultation Record"));
		}
	}

	@Test
	void pdfIsEmbeddedAsBase64InTheDocumentReference() throws Exception {
		byte[] pdf = "%PDF-1.5\ntest\n%%EOF\n".getBytes();
		String b64 = Base64.getEncoder().encodeToString(pdf);

		Patient patient = new Patient();
		patient.setId("Patient/123");

		DocumentReference dr = documentReferenceResource().getDocumentReference(patient, pdf, "Consultation Report",
				new Coding("http://snomed.info/sct", "371530004", "Clinical consultation report"));

		String json = FhirContext.forR4().newJsonParser().encodeResourceToString(dr);
		System.out.println(json);

		assertTrue(json.contains("\"contentType\":\"application/pdf\""), "contentType");
		assertTrue(json.contains("\"data\":\"" + b64 + "\""), "base64 round-trips exactly");
		assertTrue(json.contains("StructureDefinition/DocumentReference"), "ABDM profile");
		assertTrue(json.contains("\"status\":\"current\""), "status required by profile");
		assertEquals("application/pdf", dr.getContentFirstRep().getAttachment().getContentType());
		assertTrue(new String(dr.getContentFirstRep().getAttachment().getData()).startsWith("%PDF"));
	}

	@Test
	void noPdfMeansNoDocumentReference() throws Exception {
		assertNull(documentReferenceResource().getDocumentReference(new Patient(), (byte[]) null, "x", null));
		assertNull(documentReferenceResource().getDocumentReference(new Patient(), new byte[0], "x", null));
		assertNull(documentReferenceResource().getDocumentReference(new Patient(), "!!not base64!!", "x", null));
	}
}
