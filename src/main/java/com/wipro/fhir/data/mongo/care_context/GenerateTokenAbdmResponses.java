package com.wipro.fhir.data.mongo.care_context;

import java.time.LocalDateTime;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
@Document(collection = "GenerateTokenAbdmResponses")
public class GenerateTokenAbdmResponses {
	
	@Id
	@Expose
	@Field(value = "id")
	private String id;
	
	@Expose
	@Field(value = "abhaAddress")
	private String abhaAddress;
	
	@Expose
	@Field(value = "requestid")
	private String requestId;

	@Expose
	@Field(value = "response")
	private String response;
	
	@Expose
	@Field(value = "createdDate")
	private DateTime createdDate;

}
