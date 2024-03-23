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
package com.wipro.fhir.data.atoms.feed.bahmni.encounter;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import com.google.gson.annotations.Expose;

@Component
@Document(collection = "AMRIT_Atom_Feeds_Log_Clinical")
public class ClinicalFeedDataLog {
	@Id
	@Expose
	@Field(value = "id")
	private String id;

	@Expose
	@Field(value = "feedID")
	private String feedID;

	@Expose
	@Field(value = "title")
	private String title;
	@Expose
	@Field(value = "feedLink")
	private String feedLink;
	@Expose
	@Field(value = "linkSelf")
	private String linkSelf;
	@Expose
	@Field(value = "linkVia")
	private String linkVia;
	@Expose
	@Field(value = "linkPrevArchive")
	private String linkPrevArchive;
	@Expose
	@Field(value = "linkNextArchive")
	private String linkNextArchive;
	@Expose
	@Field(value = "author")
	private String author;
	@Expose
	@Field(value = "generatorUri")
	private String generatorUri;
	@Expose
	@Field(value = "updated")
	private Timestamp updated;
	@Expose
	@Field(value = "entry")
	private String entry;

	@Expose
	@Field(value = "entryID")
	private String entryID;

	@Expose
	@Field(value = "entryContent_cdata")
	private String entryContent_cdata;

	@Expose
	@Field(value = "entryContentEncounterFull_URL")
	private String entryContentEncounterFull_URL;

	@Expose
	@Field(value = "entrySuccess")
	private Boolean entrySuccess;

	@Expose
	@Field(value = "feedSuccess")
	private Boolean feedSuccess;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeedID() {
		return feedID;
	}

	public void setFeedID(String feedID) {
		this.feedID = feedID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFeedLink() {
		return feedLink;
	}

	public void setFeedLink(String feedLink) {
		this.feedLink = feedLink;
	}

	public String getLinkSelf() {
		return linkSelf;
	}

	public void setLinkSelf(String linkSelf) {
		this.linkSelf = linkSelf;
	}

	public String getLinkVia() {
		return linkVia;
	}

	public void setLinkVia(String linkVia) {
		this.linkVia = linkVia;
	}

	public String getLinkPrevArchive() {
		return linkPrevArchive;
	}

	public void setLinkPrevArchive(String linkPrevArchive) {
		this.linkPrevArchive = linkPrevArchive;
	}

	public String getLinkNextArchive() {
		return linkNextArchive;
	}

	public void setLinkNextArchive(String linkNextArchive) {
		this.linkNextArchive = linkNextArchive;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGeneratorUri() {
		return generatorUri;
	}

	public void setGeneratorUri(String generatorUri) {
		this.generatorUri = generatorUri;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getEntryID() {
		return entryID;
	}

	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	public String getEntryContent_cdata() {
		return entryContent_cdata;
	}

	public void setEntryContent_cdata(String entryContent_cdata) {
		this.entryContent_cdata = entryContent_cdata;
	}

	public String getEntryContentEncounterFull_URL() {
		return entryContentEncounterFull_URL;
	}

	public void setEntryContentEncounterFull_URL(String entryContentEncounterFull_URL) {
		this.entryContentEncounterFull_URL = entryContentEncounterFull_URL;
	}

	public Boolean getEntrySuccess() {
		return entrySuccess;
	}

	public void setEntrySuccess(Boolean entrySuccess) {
		this.entrySuccess = entrySuccess;
	}

	public Boolean getFeedSuccess() {
		return feedSuccess;
	}

	public void setFeedSuccess(Boolean feedSuccess) {
		this.feedSuccess = feedSuccess;
	}

}
