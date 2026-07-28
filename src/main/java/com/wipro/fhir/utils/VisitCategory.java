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
package com.wipro.fhir.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Which AMRIT visit categories are out-patient consultations, and therefore get
 * an OPConsultation record shared over ABDM.
 *
 * Kept in one place because the answer is needed twice — once when declaring
 * hiTypes while linking the care context, and again when actually building the
 * bundles. A category present in only one of the two either advertises a record
 * that is never created or creates one the ABHA app was never told about.
 */
public final class VisitCategory {

	private static final List<String> OP_CONSULT_CATEGORIES = Arrays.asList("General OPD", "General OPD (QC)",
			"NCD care");

	private VisitCategory() {
	}

	public static boolean isOpConsult(String visitCategory) {
		if (visitCategory == null) {
			return false;
		}
		return OP_CONSULT_CATEGORIES.stream().anyMatch(category -> category.equalsIgnoreCase(visitCategory.trim()));
	}
}
