package com.wipro.fhir.data.mongo.care_context;

import java.math.BigInteger;

import lombok.Data;

@Data
public class SaveFacilityIdForVisit {
	
	String facilityId;
	BigInteger visitCode;

}
