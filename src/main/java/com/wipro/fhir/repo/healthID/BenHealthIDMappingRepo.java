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
package com.wipro.fhir.repo.healthID;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.fhir.data.healthID.BenHealthIDMapping;

@Repository
@RestResource(exported = false)
public interface BenHealthIDMappingRepo extends CrudRepository<BenHealthIDMapping, Long> {

	@Query(value = "SELECT BenRegId FROM db_identity.m_beneficiaryregidmapping where beneficiaryid= :beneficiaryID ", nativeQuery = true)
	Long getBenRegID(@Param("beneficiaryID") Long beneficiaryID);

	@Query(" SELECT bvd from BenHealthIDMapping bvd WHERE bvd.beneficiaryRegID = :benRegID")
	public ArrayList<BenHealthIDMapping> getHealthDetails(@Param("benRegID") Long benRegID);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE db_iemr.t_benvisitdetail SET HealthID= :healthID,HealthIdNumber= :healthIdNumber,CarecontextLinkDate=CURRENT_TIMESTAMP() WHERE VisitCode= :visitCode ", nativeQuery = true)
	Integer updateHealthIDAndHealthIDNumberForCareContext(@Param("healthID") String healthID, @Param("healthIdNumber") String healthIdNumber, @Param("visitCode") String visitCode);


	@Transactional
	@Modifying
	@Query(value = "UPDATE db_iemr.t_benvisitdetail SET HealthID= :healthID,CarecontextLinkDate=CURRENT_TIMESTAMP() WHERE VisitCode= :visitCode ", nativeQuery = true)
	Integer updateHealthIDForCareContext(@Param("healthID") String healthID, @Param("visitCode") String visitCode);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE db_iemr.t_benvisitdetail SET HealthIdNumber = :healthIdNumber,CarecontextLinkDate=CURRENT_TIMESTAMP() WHERE VisitCode= :visitCode ", nativeQuery = true)
	Integer updateHealthIDNumberForCareContext(@Param("healthIdNumber") String healthIdNumber, @Param("visitCode") String visitCode);

	@Query(nativeQuery = true, value = " SELECT HealthID FROM t_benvisitdetail t WHERE t.VisitCode = :visitCode")
	public List<String> getLinkedHealthIDForVisit(@Param("visitCode") Long visitCode);

	@Query(value = "SELECT beneficiaryid FROM db_identity.m_beneficiaryregidmapping "
			+ " WHERE BenRegId= :BenRegId ", nativeQuery = true)
	Long getBenID(@Param("BenRegId") Long BenRegId);
}
