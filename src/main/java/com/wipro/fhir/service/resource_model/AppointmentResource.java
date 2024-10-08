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
package com.wipro.fhir.service.resource_model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Appointment.AppointmentParticipantComponent;
import org.hl7.fhir.r4.model.Appointment.AppointmentStatus;
import org.hl7.fhir.r4.model.Appointment.ParticipationStatus;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.fhir.data.request_handler.ResourceRequestHandler;
import com.wipro.fhir.data.resource_model.AppointmentDataModel;
import com.wipro.fhir.repo.common.PatientEligibleForResourceCreationRepo;
import com.wipro.fhir.service.common.CommonServiceImpl;

/***
 * 
 * @author NE298657
 *
 */

@Service
public class AppointmentResource {

	@Autowired
	private CommonServiceImpl commonServiceImpl;

	@Autowired
	private PatientEligibleForResourceCreationRepo patientEligibleForResourceCreationRepo;
	@Autowired
	private AppointmentDataModel appointmentDataModel;

	private Appointment appointment;
	private String UUID;

	public Appointment getAppointmentResource(ResourceRequestHandler resourceRequestHandler,
			Practitioner practitioner) {
		List<Object[]> rsObjList = patientEligibleForResourceCreationRepo
				.callAppointmentSP(resourceRequestHandler.getBeneficiaryRegID(), resourceRequestHandler.getVisitCode());

		List<AppointmentDataModel> appointmentList = appointmentDataModel.getAppointmentList(rsObjList);

		return generateAppointmentResource(appointmentList, practitioner);
	}

	private Appointment generateAppointmentResource(List<AppointmentDataModel> appointmentList,
			Practitioner practitioner) {
		UUID = commonServiceImpl.getUUID();
		appointment = new Appointment();

		appointment.setId("Appointment/" + UUID);

		String status = "";

		if (appointmentList.size() > 0)
			status = appointmentList.get(0).getStatus();

		switch (status) {
		case "A":
			appointment.setStatus(AppointmentStatus.ARRIVED);
			break;
		case "C":
			appointment.setStatus(AppointmentStatus.CANCELLED);
			break;
		case "D":
			appointment.setStatus(AppointmentStatus.FULFILLED);
			break;
		case "N":
			appointment.setStatus(AppointmentStatus.BOOKED);
			break;
		default:
			appointment.setStatus(AppointmentStatus.PROPOSED);

		}

		appointment.setDescription("Pirama Test Desc");

		appointment.setStart(new Date(System.currentTimeMillis()));
		appointment.setEnd(new Date(System.currentTimeMillis() + 100));

		List<AppointmentParticipantComponent> apcList = new ArrayList<Appointment.AppointmentParticipantComponent>();
		AppointmentParticipantComponent apc = new AppointmentParticipantComponent();
		apc.setStatus(ParticipationStatus.ACCEPTED);
		Reference reference = new Reference();
		reference.setDisplay(practitioner.getName().get(0).getText());
		reference.setReference(practitioner.getId());
		apc.setActor(reference);
		apcList.add(apc);

		appointment.setParticipant(apcList);

		return appointment;
	}
}
