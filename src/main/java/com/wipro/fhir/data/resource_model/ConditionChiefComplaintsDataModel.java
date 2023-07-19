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
package com.wipro.fhir.data.resource_model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ConditionChiefComplaintsDataModel {

	/**
	 * default value
	 */
	private static final long serialVersionUID = 1L;

	private String chiefComplaint;
	private String sCTCode;
	private Integer duration;
	private String unitOfDuration;
	private String description;
	private Timestamp createdDate;
	private String createdBy;

	public ConditionChiefComplaintsDataModel() {
	}

	public ConditionChiefComplaintsDataModel(Object[] objArr) {
		if (objArr[0] != null)
			this.chiefComplaint = ((String) objArr[0]);
		if (objArr[1] != null)
			this.sCTCode = ((String) objArr[1]);
		if (objArr[2] != null)
			this.duration = ((Integer) objArr[2]);
		if (objArr[3] != null)
			this.unitOfDuration = (String) objArr[3];
		if (objArr[4] != null)
			this.description = (String) objArr[4];
		if (objArr[5] != null)
			this.createdDate = (Timestamp) objArr[5];
		if (objArr[6] != null)
			this.createdBy = (String) objArr[6];

	}

	public List<ConditionChiefComplaintsDataModel> getConditionChiefComplaintList(List<Object[]> resultSetList) {
		ConditionChiefComplaintsDataModel conditionOBJ;
		List<ConditionChiefComplaintsDataModel> conditionChiefComplaintsList = new ArrayList<ConditionChiefComplaintsDataModel>();
		if (resultSetList != null && resultSetList.size() > 0) {
			for (Object[] objArr : resultSetList) {
				conditionOBJ = new ConditionChiefComplaintsDataModel(objArr);
				conditionChiefComplaintsList.add(conditionOBJ);
			}
		}
		return conditionChiefComplaintsList;
	}

}
