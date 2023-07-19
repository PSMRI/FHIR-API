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
package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OpenMRSPerson {
	private String uuid;
	private String display;
	private String gender;
	private int age;
	private Date birthdate;
	private String birthtime;
	private boolean birthdateEstimated;
	private boolean dead;
	private Date deathDate;
	private boolean deathdateEstimated;
	private OpenMRSPersonName preferredName;
	private List<OpenMRSPersonName> names;
	private OpenMRSPersonAddress preferredAddress;
	private List<OpenMRSPersonAddress> addresses;
	private List<OpenMRSPersonAttribute> attributes;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getBirthtime() {
		return birthtime;
	}
	public void setBirthtime(String birthtime) {
		this.birthtime = birthtime;
	}
	public boolean isBirthdateEstimated() {
		return birthdateEstimated;
	}
	public void setBirthdateEstimated(boolean birthdateEstimated) {
		this.birthdateEstimated = birthdateEstimated;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public Date getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}
	public boolean isDeathdateEstimated() {
		return deathdateEstimated;
	}
	public void setDeathdateEstimated(boolean deathdateEstimated) {
		this.deathdateEstimated = deathdateEstimated;
	}
	public OpenMRSPersonName getPreferredName() {
		return preferredName;
	}
	public void setPreferredName(OpenMRSPersonName preferredName) {
		this.preferredName = preferredName;
	}
	public List<OpenMRSPersonName> getNames() {
		return names;
	}
	public void setNames(List<OpenMRSPersonName> names) {
		this.names = names;
	}
	public OpenMRSPersonAddress getPreferredAddress() {
		return preferredAddress;
	}
	public void setPreferredAddress(OpenMRSPersonAddress preferredAddress) {
		this.preferredAddress = preferredAddress;
	}
	public List<OpenMRSPersonAddress> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<OpenMRSPersonAddress> addresses) {
		this.addresses = addresses;
	}
	public List<OpenMRSPersonAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<OpenMRSPersonAttribute> attributes) {
		this.attributes = attributes;
	}
}
