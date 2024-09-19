package com.wipro.fhir.service.facility;

import com.wipro.fhir.utils.exception.FHIRException;

public interface FacilityService {

	String fetchRegisteredFacilities() throws FHIRException;

}
