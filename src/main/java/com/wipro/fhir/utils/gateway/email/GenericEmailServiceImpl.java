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
package com.wipro.fhir.utils.gateway.email;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class GenericEmailServiceImpl implements EmailService {
	JavaMailSender javaMailSender;

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmail(String jsonObject, String template) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		JSONObject requestObj = new JSONObject(jsonObject);
		String to = requestObj.getString("to");
		String from = requestObj.getString("from");
		String subject = requestObj.getString("subject");
		String message = requestObj.getString("message");

		

		mailMessage.setTo(to);
		mailMessage.setFrom(from);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);

	}

	@Override
	public void sendEmail(String jsonObject) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		JSONObject requestObj = new JSONObject(jsonObject);
		String to = requestObj.getString("to");
		String from = requestObj.getString("from");
		String subject = requestObj.getString("subject");
		String message = requestObj.getString("message");
		mailMessage.setTo(to.split(";"));
		mailMessage.setFrom(from);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);

	}

	@Override
	public void sendEmailWithAttachment(String jsonObject, String template) {
		

	}

	

}
