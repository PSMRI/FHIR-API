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
package com.wipro.fhir.config.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.fhir.service.atoms.feed.bahmni.PatientFeedWorker;

@Service
@Transactional
public class Schedule_Job_Patient_Feed_ATOM implements Job{
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private PatientFeedWorker patientFeedWorkerService;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Started job for parsing Patient feed " + arg0.getClass().getName());

		try {
			
			patientFeedWorkerService.patientFeedManager();

		} catch (Exception e) {
			
			logger.error("Unexpected error:" , e);
		}
		logger.info("Completed job for parsing Patient feed " + arg0.getClass().getName());
	}

}
