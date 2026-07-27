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

import java.util.Base64;
import java.util.Date;

import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.DocumentReference.ReferredDocumentStatus;
import org.hl7.fhir.r4.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.service.common.CommonService;

/**
 * Builds the ABDM DocumentReference resource that carries a PDF of the record as
 * a base64 attachment. This is what lets the ABHA / PHR app offer the record as
 * a download instead of only rendering the structured FHIR entries.
 *
 * Profile: https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference
 * status and content.attachment.contentType/data are all 1..1 in that profile.
 */
@Service
public class DocumentReferenceResource {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static final String PDF_CONTENT_TYPE = "application/pdf";

	@Autowired
	private CommonService commonService;

	/**
	 * @param patient    subject of the document; may be null
	 * @param pdfBytes   the raw PDF; null or empty yields null (no DocumentReference)
	 * @param title      human readable title, e.g. "Consultation Report"
	 * @param typeCoding SNOMED coding describing the document, e.g. 371530004
	 *                   "Clinical consultation report"
	 * @return the DocumentReference, or null when there is no document to attach
	 */
	public DocumentReference getDocumentReference(Patient patient, byte[] pdfBytes, String title,
			Coding typeCoding) {
		if (pdfBytes == null || pdfBytes.length == 0) {
			logger.info("No PDF supplied for '" + title + "', skipping DocumentReference");
			return null;
		}
		return getDocumentReference(patient, Base64.getEncoder().encodeToString(pdfBytes), title, typeCoding);
	}

	/**
	 * Overload for callers that already hold the base64 string (e.g. a PDF fetched
	 * from another service).
	 */
	public DocumentReference getDocumentReference(Patient patient, String base64Pdf, String title,
			Coding typeCoding) {
		if (base64Pdf == null || base64Pdf.trim().isEmpty()) {
			logger.info("No PDF supplied for '" + title + "', skipping DocumentReference");
			return null;
		}

		// The profile requires attachment.data to be valid base64. Anything else would
		// be rejected downstream by the CM/HIU, so fail here where it is diagnosable.
		byte[] decoded;
		try {
			decoded = Base64.getDecoder().decode(base64Pdf.trim());
		} catch (IllegalArgumentException e) {
			logger.error("PDF for '" + title + "' is not valid base64, skipping DocumentReference: "
					+ e.getMessage());
			return null;
		}

		DocumentReference documentReference = new DocumentReference();
		documentReference.setId("DocumentReference/" + commonService.getUUID());

		Meta meta = new Meta();
		meta.setVersionId("1");
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference");
		documentReference.setMeta(meta);

		documentReference.setStatus(DocumentReferenceStatus.CURRENT);
		documentReference.setDocStatus(ReferredDocumentStatus.FINAL);

		if (typeCoding != null) {
			CodeableConcept type = new CodeableConcept(typeCoding);
			type.setText(typeCoding.getDisplay());
			documentReference.setType(type);
		}

		if (patient != null) {
			documentReference.setSubject(
					new Reference(patient.getIdElement().getValue()).setDisplay("Patient"));
		}

		documentReference.addContent().setAttachment(pdfAttachment(decoded, title));

		logger.info("DocumentReference created for '" + title + "' (" + decoded.length + " bytes of PDF)");
		return documentReference;
	}

	private Attachment pdfAttachment(byte[] decoded, String title) {
		Attachment attachment = new Attachment();
		attachment.setContentType(PDF_CONTENT_TYPE);
		attachment.setLanguage("en-IN");
		attachment.setData(decoded);
		attachment.setTitle(title);
		attachment.setCreationElement(new DateTimeType(new Date()));
		return attachment;
	}
}
