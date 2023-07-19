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
package com.wipro.fhir.repo.e_aushdhi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.fhir.data.e_aushdhi.T_PatientIssue;


@Repository
@RestResource(exported = false)
public interface PatientIssueRepo extends CrudRepository<T_PatientIssue, Long> {
	
	@Query("SELECT f FROM T_PatientIssue f WHERE f.facilityID = :facilityID AND deleted=0 order by createdDate desc")
	public Page<T_PatientIssue> getPatientIssueDetails(@Param("facilityID") Integer facilityID, Pageable pageable);
	
	
	@Query(value = "select f.FatherName from db_identity.i_beneficiarydetails f "
			+ "join db_identity.i_beneficiarymapping g on f.BeneficiaryDetailsId=g.BenDetailsId "
			+ "where g.BenRegId=:BenRegId ", nativeQuery = true)
	public String getBenFatherName(@Param("BenRegId") Long BenRegId);
	
	@Query(value = "select f.BeneficiaryID from db_identity.m_beneficiaryregidmapping f "
			+ "where f.BenRegId=:BenRegId AND Deleted=0", nativeQuery = true)
	public Long getBenID(@Param("BenRegId") Long BenRegId);
	
	
	@Query(value = "select f.DrugID, f.QtyPrescribed, g.BatchNo, g.ExpiryDate, h.ItemCode, g.ItemStockEntryID, v.Quantity from  t_prescribeddrug f join t_itemstockentry g on f.DrugID=g.ItemID join m_item h on g.ItemID=h.ItemID join t_itemstockexit v on g.ItemStockEntryID=v.ItemStockEntryID where  f.PrescriptionID = :prescriptionID and g.FacilityID = :facilityID and g.EntryType='E-Aushadhi Stock Entry' and v.ExitTypeID = :patientIssueID ", nativeQuery = true)
	public List<Objects[]> getDispensedDrugDetails(@Param("prescriptionID") Integer prescriptionID, @Param("facilityID") Integer facilityID, @Param("patientIssueID") Long patientIssueID);
	

	@Query(value = "select * from t_patientissue q left join t_prescribeddrug f on q.PrescriptionID=f.PrescriptionID " + 
			"left join t_itemstockentry g on f.DrugID=g.ItemID where q.FacilityID = :facilityID " + 
			"and g.EntryType='E-Aushadhi Stock Entry' and q.Processed <> 'E' group by q.PrescriptionID ORDER BY ?#{#pageable} ", nativeQuery = true)
	public Page<T_PatientIssue> getIssueDetailsForEAushadhi(@Param("facilityID") Integer facilityID, Pageable pageable);
	
	@Query(value = "select * from t_patientissue q left join t_prescribeddrug f on q.PrescriptionID=f.PrescriptionID " + 
			"left join t_itemstockentry g on f.DrugID=g.ItemID where " + 
			"g.EntryType='E-Aushadhi Stock Entry' and q.Processed <> 'E' group by q.PrescriptionID ORDER BY ?#{#pageable} ", nativeQuery = true)
	public Page<T_PatientIssue> getIssueDetailsForEAushadhiForAllFacility(Pageable pageable);
	
	
	@Query(value = "SELECT ben_age FROM db_iemr.i_ben_flow_outreach where beneficiary_reg_id=:BenRegId and Deleted=0 order by created_date desc limit 1", nativeQuery = true)
	public String getBenAge(@Param("BenRegId") Long BenRegId);
	
	
	@Transactional
	@Modifying
	@Query("update T_PatientIssue set processed = 'E' where patientIssueID = :patientIssueID ")
	public int updateEAushadhiIssueSyncStatus(@Param("patientIssueID") Long patientIssueID);
	
	
	
}
