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
package com.wipro.fhir.service.common;

import java.util.List;

import org.springframework.data.domain.Page;

import com.wipro.fhir.data.mongo.amrit_resource.AMRIT_ResourceMongo;
import com.wipro.fhir.data.mongo.amrit_resource.TempCollection;
import com.wipro.fhir.data.mongo.care_context.NDHMRequest;
import com.wipro.fhir.data.patient_data_handler.PatientDemographicModel_NDHM_Patient_Profile;
import com.wipro.fhir.data.request_handler.PatientEligibleForResourceCreation;
import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.utils.exception.FHIRException;

public interface CommonService {

	public List<PatientEligibleForResourceCreation> getPatientListForResourceEligible() throws FHIRException;

	public String getAuthKey() throws FHIRException;

	public int saveResourceToMongo(AMRIT_ResourceMongo aMRIT_Resource) throws FHIRException;

	public String processResourceOperation() throws FHIRException;

	public String getUUID();

	public String ndhmUserAuthenticate() throws FHIRException;

	public int saveTempResourceToMongo(TempCollection tempCollection) throws FHIRException;

	public List<TempCollection> fetchTempResourceFromMongo(ResourceRequestHandler resourceRequestHandler)
			throws FHIRException;

	public NDHMRequest getRequestIDAndTimeStamp();

	public String getMongoNDHMResponse(String requestID) throws FHIRException;

	// Bahmni
	public List<PatientDemographicModel_NDHM_Patient_Profile> savePatientProfileDataToMongo(
			List<PatientDemographicModel_NDHM_Patient_Profile> patientProfile_list) throws FHIRException;

	// patient profile creation processing
	public String processPatientProfileCreationAMRIT() throws FHIRException;

	public Page<PatientDemographicModel_NDHM_Patient_Profile> searchPatientProfileFromMongo(
			ResourceRequestHandler resourceRequestHandler) throws FHIRException;

}
