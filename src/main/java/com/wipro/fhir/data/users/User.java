package com.wipro.fhir.data.users;

import java.sql.Timestamp;

import com.google.gson.annotations.Expose;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "m_user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	@Column(name = "UserID")
	private Long userID;

	@Expose
	@Column(name = "TitleID")
	private Integer titleID;
	@Expose
	@Column(name = "FirstName")
	private String firstName;
	@Expose
	@Column(name = "MiddleName")
	private String middleName;
	@Expose
	@Column(name = "LastName")
	private String lastName;

	@Expose
	@Column(name = "GenderID")
	private Integer genderID;

	@Expose
	@Column(name = "MaritalStatusID")
	private Integer maritalStatusID;

	@Expose
	@Column(name = "StatusID")
	private Integer statusID;

	@Expose
	@Column(name = "DOB")
	private Timestamp dOB;
	@Expose
	@Column(name = "DOJ")
	private Timestamp dOJ;
	@Expose
	@Column(name = "QualificationID")
	private Integer qualificationID;
	@Expose
	@Column(name = "userName")
	private String userName;
	@Expose
	@Column(name = "Deleted", insertable = false, updatable = true)
	private Boolean deleted;
	@Expose
	@Column(name = "CreatedBy")
	private String createdBy;
	@Column(name = "CreatedDate", insertable = false, updatable = false)
	private Timestamp createdDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "LastModDate", insertable = false, updatable = false)
	private Timestamp lastModDate;
}
