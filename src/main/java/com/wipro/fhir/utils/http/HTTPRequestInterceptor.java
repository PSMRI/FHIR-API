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
package com.wipro.fhir.utils.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wipro.fhir.utils.response.OutputResponse;
import com.wipro.fhir.utils.sessionobject.SessionObject;
import com.wipro.fhir.utils.validator.Validator;

@Component
public class HTTPRequestInterceptor implements HandlerInterceptor {
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private SessionObject sessionObject;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		logger.info("http interceptor - pre Handle");
		boolean status = true;

		if (request.getRequestURI().toLowerCase().contains("swagger-ui"))
			return status;

		String authorization = null;
		String preAuth = request.getHeader("Authorization");
		if (null != preAuth && preAuth.contains("Bearer "))
			authorization = preAuth.replace("Bearer ", "");
		else
			authorization = preAuth;
		if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
			try {
				String[] requestURIParts = request.getRequestURI().split("/");
				String requestAPI = requestURIParts[requestURIParts.length - 1];
				switch (requestAPI) {
				case "swagger-ui.html":
					break;
				case "index.html":
					break;
				case "swagger-initializer.js":
					break;
				case "swagger-config":
					break;
				case "ui":
					break;
				case "swagger-resources":
					break;
				case "api-docs":
					break;

				case "error":
					status = false;
					break;
				default:
					logger.debug("RequestURI::" + request.getRequestURI() + " || Authorization ::" + authorization);
//					if (authorization == null)
//						throw new Exception(
//								"Authorization key is NULL, please pass valid session key to proceed further. ");
//					String userRespFromRedis = sessionObject.getSessionObject(authorization);
//					if (userRespFromRedis == null)
//						throw new Exception("invalid Authorization key, please pass a valid key to proceed further. ");
					break;
				}
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage());

				OutputResponse output = new OutputResponse();
				output.setError(e);
				response.getOutputStream().print(output.toString());
				response.setContentType(MediaType.APPLICATION_JSON);
				response.setContentLength(output.toString().length());
				response.setHeader("Access-Control-Allow-Origin", "*");
				status = false;
			}
		}

		return status;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
		try {
			logger.debug("In postHandle we are Intercepting the Request");
			String authorization = null;
			String postAuth = request.getHeader("Authorization");
			if (null != postAuth && postAuth.contains("Bearer "))
				authorization = postAuth.replace("Bearer ", "");
			else
				authorization = postAuth;
			logger.debug("RequestURI::" + request.getRequestURI() + " || Authorization ::" + authorization);
			if (authorization != null) {
				sessionObject.updateSessionObject(authorization, sessionObject.getSessionObject(authorization));
			}
		} catch (Exception e) {
			logger.debug("postHandle failed with error " + e.getMessage());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		logger.info("http interceptor - after completion");

	}

}
