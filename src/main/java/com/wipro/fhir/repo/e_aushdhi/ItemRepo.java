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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.wipro.fhir.data.e_aushdhi.ItemMaster;


@Repository
@RestResource(exported = false)
public interface ItemRepo extends CrudRepository<ItemMaster, Integer> {
	
	@Query("SELECT f FROM ItemMaster f WHERE f.itemName = :drugname AND f.providerServiceMapID = :providerServiceMapID AND itemCode = :brandid AND deleted=0")
     public ItemMaster checkItemExists(@Param("drugname") String drugname, @Param("providerServiceMapID") Integer providerServiceMapID, @Param("brandid") String brandid);
	

	
	@Query(value = "select * from m_item f "
			+ "join m_itemfacilitymapping g on f.itemID=g.itemID "
			+ "where g.facilityID=:mainFacilityID and f.itemName=:drugname "
			+ " and f.itemCode=:brandid and f.deleted=0 and g.deleted=0 ", nativeQuery = true)
	public ItemMaster checkItemExistForMainFacility(@Param("mainFacilityID") Integer mainFacilityID, @Param("drugname") String drugname, @Param("brandid") String brandid);
//	

}
