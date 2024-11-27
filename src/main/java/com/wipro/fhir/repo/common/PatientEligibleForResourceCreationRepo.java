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
package com.wipro.fhir.repo.common;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;

/***
 * 
 * @author NE298657
 *
 */
@Repository
@RestResource(exported = false)
public interface PatientEligibleForResourceCreationRepo
		extends CrudRepository<PatientEligibleForResourceCreation, BigInteger> {

	@Query(" SELECT p FROM PatientEligibleForResourceCreation p "
			+ " WHERE (p.processed is null OR p.processed = false) AND p.visitDate >= :fromDate ")
	List<PatientEligibleForResourceCreation> getPatientListForResourceCreation(@Param("fromDate") Timestamp fromDate);

	// sp calling
	// allergy
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_AllergyIntolerance(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11 );")
	List<Object[]> callAllergySP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// condition - diagnosis
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_Diagnosis(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8 );")
	List<Object[]> callConditionSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// condition - chief complaints
	// condition
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_ChiefComplaints(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6 );")
	List<Object[]> callChiefComplaintsConditionSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// family_member_history
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_FamilyMemberHistory(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9 );")
	List<Object[]> callFamilyMemberHistorySP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// appointment
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_Appointment(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11, @12 );")
	List<Object[]> callAppointmentSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// encounter
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_Encounter(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9 );")
	List<Object[]> callEncounterSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// observation
	// (diagnostic_report same sp)
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_DiagnosticReportLab(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11, @12, @13, @14, @15, @16, @17 );")
	List<Object[]> callObservationSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// diagnostic_report
	// (observation same sp)
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_DiagnosticReportLab(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11, @12, @13, @14, @15, @16, @17 );")
	List<Object[]> callDiagnosticReportSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// (observation - vitals)
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_Vitals_Anthropometry(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11, @12, @13, @14, @15 );")
	List<Object[]> callVitals_AnthropometrySP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// medication request
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_MedicationStatement(:beneficiaryRegID_IN, "
			+ " :visitCode_IN, @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11, @12, @13, @14, @15, @16, @17, @18 );")
	List<Object[]> callMedicationRequestSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN,
			@Param("visitCode_IN") BigInteger visitCode_IN);

	// patient
	@Query(nativeQuery = true, value = "CALL db_iemr.FHIR_R_PatientDemographic(:beneficiaryRegID_IN, "
			+ " @0, @1, @2, @3, @4, @5, @6, @7, @8, @9, @10, @11, @12,@13, @14, @15, @16, @17 );")
	List<Object[]> callPatientDemographicSP(@Param("beneficiaryRegID_IN") BigInteger beneficiaryRegID_IN);
}
