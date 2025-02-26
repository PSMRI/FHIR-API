package com.wipro.fhir.data.users;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UserID")
	private Long userID;
	@Column(name = "userName")
	private String userName;
	@Column(name = "Deleted", insertable = false, updatable = true)
	private Boolean deleted;
}
