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

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.fhir.data.e_aushdhi.ItemStockEntry;

@Repository
@RestResource(exported = false)
public interface ItemStockEntryRepo extends CrudRepository<ItemStockEntry, Integer> {

	@Query("SELECT f FROM ItemStockEntry f WHERE f.facilityID = :amritFacilityID AND f.itemID = :itemId AND f.batchNo = :batchno AND deleted=0")
	public ItemStockEntry checkItemEntryExits(@Param("amritFacilityID") Integer amritFacilityID,
			@Param("itemId") Integer itemId, @Param("batchno") String batchno);


	@Transactional
	@Modifying
	@Query("update ItemStockEntry set " + " quantityInHand=:inhandqty, expiryDate=:expDate where "
			+ "itemStockEntryID=:itemStockEntryID")
	public int updateItemEntryDetails(@Param("itemStockEntryID") Long itemStockEntryID,
			@Param("inhandqty") Integer inhandqty, @Param("expDate") Date expDate);

	@Transactional
	@Modifying
	@Query("update ItemStockEntry set vanSerialNo=:itemStockEntryID where " + "itemStockEntryID=:itemStockEntryID")
	public int updateVanSerialNo(@Param("itemStockEntryID") Long itemStockEntryID);
}
