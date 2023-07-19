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
package com.wipro.fhir.data.patient_data_handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import com.google.gson.annotations.Expose;

@Component
@Document(collection = "PatientDataDump_Ex_In")
public class PatientDemographicModel_NDHM_Patient_Profile {
	@Id
	@Expose
	@Field(value = "id")
	private String id;
	@Expose
	@Field(value = "requestId")
	private String requestId;
	@Expose
	@Field(value = "timestamp")
	private Date timestamp;
	@Expose
	@Field(value = "healthId")
	private String healthId;
	@Expose
	@Field(value = "healthIdNumber")
	private String healthIdNumber;
	@Expose
	@Field(value = "amritId")
	private String amritId;
	@Expose
	@Field(value = "externalId")
	private String externalId;
	@Expose
	@Field(value = "dataType")
	private String dataType = "Demographic";
	@Expose
	@Field(value = "hipId")
	private String hipId;
	@Expose
	@Field(value = "hipName")
	private String hipName;
	@Expose
	@Field(value = "CreatedDate")
	private Date createdDate;
	@Expose
	@Field(value = "LastModDate")
	private Date lastModDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModDate() {
		return lastModDate;
	}

	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}

	// to update status for created profile record
	@Transient
	private Long triggerTableAIId;

	public Long getTriggerTableAIId() {
		return triggerTableAIId;
	}

	public void setTriggerTableAIId(Long triggerTableAIId) {
		this.triggerTableAIId = triggerTableAIId;
	}

	// inner class object - Profile
	@Expose
	@Field(value = "profile")
	private Profile profile;

	public class Profile {
		@Expose
		@Field(value = "hipCode")
		private String hipCode;

		// inner class object - Patient
		@Expose
		@Field(value = "patient")
		private Patient patient;

		public Profile() {
			super();
		}

		public class Patient {
			@Expose
			@Field(value = "healthId")
			private String healthId;
			@Expose
			@Field(value = "healthIdNumber")
			private String healthIdNumber;
			@Expose
			@Field(value = "name")
			private String name;
			@Expose
			@Field(value = "firstName")
			private String firstName;
			@Expose
			@Field(value = "lastName")
			private String lastName;
			@Expose
			@Field(value = "gender")
			private String gender;
			@Expose
			@Field(value = "yearOfBirth")
			private String yearOfBirth;
			@Expose
			@Field(value = "dayOfBirth")
			private String dayOfBirth;
			@Expose
			@Field(value = "monthOfBirth")
			private String monthOfBirth;
			@Expose
			@Field(value = "phoneNo")
			private String phoneNo;

			@Expose
			List<Map<String, String>> identifiers;

			// inner class object - Patient
			@Expose
			@Field(value = "address")
			private Address address;

			public Patient() {
				super();
				
			}

			public class Address {
				@Expose
				@Field(value = "line")
				private String line;
				@Expose
				@Field(value = "district")
				private String district;
				@Expose
				@Field(value = "state")
				private String state;
				@Expose
				@Field(value = "pincode")
				private String pincode;

				@Expose
				@Field(value = "village")
				private String village;

				public Address() {
					super();
					
				}

				public String getLine() {
					return line;
				}

				public void setLine(String line) {
					this.line = line;
				}

				public String getDistrict() {
					return district;
				}

				public void setDistrict(String district) {
					this.district = district;
				}

				public String getState() {
					return state;
				}

				public void setState(String state) {
					this.state = state;
				}

				public String getPincode() {
					return pincode;
				}

				public void setPincode(String pincode) {
					this.pincode = pincode;
				}

				public String getVillage() {
					return village;
				}

				public void setVillage(String village) {
					this.village = village;
				}

			}

			public String getHealthId() {
				return healthId;
			}

			public void setHealthId(String healthId) {
				this.healthId = healthId;
			}

			public String getHealthIdNumber() {
				return healthIdNumber;
			}

			public void setHealthIdNumber(String healthIdNumber) {
				this.healthIdNumber = healthIdNumber;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getFirstName() {
				return firstName;
			}

			public void setFirstName(String name) {
				this.firstName = name;
			}

			public String getLastName() {
				return lastName;
			}

			public void setLastName(String name) {
				this.lastName = name;
			}

			public String getGender() {
				return gender;
			}

			public void setGender(String gender) {
				this.gender = gender;
			}

			public String getYearOfBirth() {
				return yearOfBirth;
			}

			public void setYearOfBirth(String yearOfBirth) {
				this.yearOfBirth = yearOfBirth;
			}

			public String getDayOfBirth() {
				return dayOfBirth;
			}

			public void setDayOfBirth(String dayOfBirth) {
				this.dayOfBirth = dayOfBirth;
			}

			public String getMonthOfBirth() {
				return monthOfBirth;
			}

			public void setMonthOfBirth(String monthOfBirth) {
				this.monthOfBirth = monthOfBirth;
			}

			public String getPhoneNo() {
				return phoneNo;
			}

			public void setPhoneNo(String phoneNo) {
				this.phoneNo = phoneNo;
			}

			public List<Map<String, String>> getIdentifiers() {
				return identifiers;
			}

			public void setIdentifiers(List<Map<String, String>> identifiers) {
				this.identifiers = identifiers;
			}

			public Address getAddress() {
				return address;
			}

			public void setAddress(Address address) {
				this.address = address;
			}

		}

		public String getHipCode() {
			return hipCode;
		}

		public void setHipCode(String hipCode) {
			this.hipCode = hipCode;
		}

		public Patient getPatient() {
			return patient;
		}

		public void setPatient(Patient patient) {
			this.patient = patient;
		}

	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getHealthIdNumber() {
		return healthIdNumber;
	}

	public void setHealthIdNumber(String healthIdNumber) {
		this.healthIdNumber = healthIdNumber;
	}

	public String getAmritId() {
		return amritId;
	}

	public void setAmritId(String amritId) {
		this.amritId = amritId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getHipId() {
		return hipId;
	}

	public void setHipId(String hipId) {
		this.hipId = hipId;
	}

	public String getHipName() {
		return hipName;
	}

	public void setHipName(String hipName) {
		this.hipName = hipName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

}
