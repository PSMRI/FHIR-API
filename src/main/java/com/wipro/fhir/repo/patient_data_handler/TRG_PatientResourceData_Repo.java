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
package com.wipro.fhir.repo.patient_data_handler;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.fhir.data.patient_data_handler.TRG_PatientResourceData;

@Repository
@RestResource(exported = false)
public interface TRG_PatientResourceData_Repo extends CrudRepository<TRG_PatientResourceData, Long> {

	@Query(nativeQuery = true, value = " SELECT * FROM db_identity.trg_patientresourcedata "
			+ " WHERE beneficiaryID is not null AND (processed is null OR processed is false ) ORDER BY createdDate LIMIT 20")
	List<TRG_PatientResourceData> getByProcessedOrderByCreatedDateLimit20();

	@Query(nativeQuery = true, value = " SELECT * FROM db_identity.trg_patientresourcedata "
			+ " WHERE beneficiaryID  =:benId  AND (processed is null OR processed is false) ORDER BY id desc LIMIT 1")
	List<TRG_PatientResourceData> getByBenIdLatestRecord(@Param("benId") Long benId);

	@Transactional
	@Modifying
	@Query(" UPDATE TRG_PatientResourceData SET processed = true WHERE id IN :ids ")
	int updateProcessedFlagForProfileCreated(@Param("ids") List<Long> ids);
}
