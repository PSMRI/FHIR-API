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

package com.wipro.fhir.controller.health;

import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wipro.fhir.service.health.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "APIs for checking infrastructure health status")
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    private final HealthService healthService;
    
    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    @Operation(summary = "Check infrastructure health", 
               description = "Returns the health status of MySQL, Redis, and other configured services")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Services are UP or DEGRADED (operational with warnings)"),
        @ApiResponse(responseCode = "503", description = "One or more critical services are DOWN")
    })
    public ResponseEntity<Map<String, Object>> checkHealth() {
        logger.info("Health check endpoint called");
        
        try {
            Map<String, Object> healthStatus = healthService.checkHealth();
            String overallStatus = (String) healthStatus.get("status");
            
            // Return 503 only if DOWN; 200 for both UP and DEGRADED (DEGRADED = operational with warnings)
            HttpStatus httpStatus = "DOWN".equals(overallStatus) ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.OK;
            
            logger.debug("Health check completed with status: {}", overallStatus);
            return new ResponseEntity<>(healthStatus, httpStatus);
            
        } catch (Exception e) {
            logger.error("Unexpected error during health check", e);
            
            Map<String, Object> errorResponse = Map.of(
                "status", "DOWN",
                "timestamp", Instant.now().toString()
            );
            
            return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
