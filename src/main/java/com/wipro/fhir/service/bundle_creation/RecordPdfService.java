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

import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;

/**
 * Renders the printable case sheet for a visit so it can be attached to the FHIR
 * bundle as a base64 DocumentReference — this is what makes the record
 * downloadable as a PDF in the ABHA / PHR app.
 *
 * It is built from the resources the bundle already holds, so no data is fetched
 * twice. Returning null means "no PDF for this visit"; the bundle is then built
 * exactly as it was before, without a DocumentReference.
 */
public interface RecordPdfService {

	/**
	 * @return the OP consultation case sheet as PDF bytes, or null if one could not
	 *         be produced
	 */
	byte[] getOpConsultPdf(Patient patient, Organization organization, Practitioner practitioner,
			List<Condition> chiefComplaints, List<Condition> diagnoses, List<AllergyIntolerance> allergies,
			FamilyMemberHistory familyHistory, List<MedicationStatement> medications,
			List<Observation> vitals);
}
