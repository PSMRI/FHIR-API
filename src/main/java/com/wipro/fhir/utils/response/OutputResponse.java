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
package com.wipro.fhir.utils.response;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

public class OutputResponse {
	@Expose
	private Object data;
	Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	public static final int SUCCESS = 200;
	public static final int GENERIC_FAILURE = 5000;
	public static final int OBJECT_FAILURE = 5001;
	public static final int USERID_FAILURE = 5002;
	public static final int PASSWORD_FAILURE = 5003;
	public static final int PREVILAGE_FAILURE = 5004;
	public static final int CODE_EXCEPTION = 5005;
	public static final int ENVIRONMENT_EXCEPTION = 5006;
	public static final int PARSE_EXCEPTION = 5007;
	public static final int SWYMED_EXCEPTION = 5010;
	public static final int TM_EXCEPTION = 5010;

	@Expose
	private int statusCode = GENERIC_FAILURE;
	@Expose
	private String errorMessage = "Failed with generic error";
	@Expose
	private String status = "FAILURE";
	private static final String RESPONSE = "{\"response\":\"$$STRING\"}";
	private static final String RESPONSE_VALUE = "$$STRING";

	public void setResponse(String message) {
		JsonArray ja = null;
		try {
			Object obj = new JsonParser().parse(message);
			if ((obj instanceof JsonArray)) {
				ja = (JsonArray) obj;
				this.data = ja;
			} else if ((obj instanceof JsonObject)) {
				this.data = obj;
			} else {
				this.data = new JsonParser().parse(RESPONSE.replace(RESPONSE_VALUE, message));
				// this.data = message;
			}
		} catch (Exception exe) {
			this.data = message;
			this.data = new JsonParser().parse(RESPONSE.replace(RESPONSE_VALUE, message));
		}
		statusCode = SUCCESS;
		errorMessage = "Success";
		status = "Success";

	}

	public void setError(Throwable thrown) {
		Date currDate = Calendar.getInstance().getTime();
		logger.info("error happened due to " + thrown.getClass().getSimpleName() + " at " + currDate.toString());
		switch (thrown.getClass().getSimpleName()) {
		case "IEMRException":
			this.statusCode = USERID_FAILURE;
			status = "User login failed";
			errorMessage = thrown.getMessage();
			break;
		case "SwymedException":
			this.statusCode = SWYMED_EXCEPTION;
			status = "Swymed integration error";
			errorMessage = thrown.getMessage();
			break;
		case "TMException":
			this.statusCode = TM_EXCEPTION;
			status = "Invalid input";
			errorMessage = thrown.getMessage();
			break;
		case "JSONException":
			this.statusCode = OBJECT_FAILURE;
			status = "Invalid object conversion";
			errorMessage = "Invalid object conversion";
			break;
		case "SQLException":
		case "ParseException":
		case "NullPointerException":
		case "SQLGrammarException":
			this.statusCode = CODE_EXCEPTION;
			status = "Failed with internal errors at " + currDate.toString() + ".Please try after some time. "
					+ "If error is still seen, contact your administrator.";
			errorMessage = thrown.getMessage();
			break;
		case "IOException":
		case "ConnectException":
		case "ConnectIOException":
			this.statusCode = ENVIRONMENT_EXCEPTION;
			status = "Failed with connection issues at " + currDate.toString() + "Please try after some time. "
					+ "If error is still seen,  contact your administrator.";
			errorMessage = thrown.getMessage();
			break;
		case "JDBCException":
			this.statusCode = ENVIRONMENT_EXCEPTION;
			status = "Failed with DB connection issues at " + currDate.toString() + ". Please try after some time. "
					+ "If error is still seen,  contact your administrator.";
			errorMessage = thrown.getMessage();
			break;
		default:
			this.statusCode = GENERIC_FAILURE;
			status = "Failed with " + thrown.getMessage() + " at " + currDate.toString()
					+ ".Please try after some time. If error is still seen, contact your administrator.";
			errorMessage = thrown.getMessage();
			break;
		}
		logger.error("Failure happend with " + thrown.getMessage() + "at " + currDate.toString(), thrown);
	}

	public void setError(int errorCode, String message, String status) {
		this.errorMessage = message;
		this.status = status;
		this.statusCode = errorCode;
	}

	public void setError(int errorCode, String message) {
		setError(errorCode, message, message);
	}

	public boolean isSuccess() {
		return (this.statusCode == SUCCESS);
	}

	/**
	 * @return the data
	 */
	public String getData() {
		JSONObject obj = new JSONObject(toString());
		if (obj.has("data")) {
			return obj.get("data").toString();
		} else {
			return null;
		}
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		
		return builder.create().toJson(this);
		
	}

	public String toStringWithSerialization() {
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		builder.serializeNulls();
		return builder.create().toJson(this);
	}

	
}
