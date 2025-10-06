package com.wipro.fhir.repo.mongo.generateToken_response;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.wipro.fhir.data.mongo.care_context.GenerateTokenAbdmResponses;

@Repository
@RestResource(exported = false)
public interface GenerateTokenAbdmResponsesRepo extends MongoRepository<GenerateTokenAbdmResponses, String> {

	GenerateTokenAbdmResponses findByAbhaAddress(String abhaAddress);
	
	

}
