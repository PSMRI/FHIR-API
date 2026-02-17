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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import com.wipro.fhir.service.health.HealthService;
import com.wipro.fhir.utils.JwtAuthenticationUtil;
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
    private final JwtAuthenticationUtil jwtAuthenticationUtil;
    
    public HealthController(HealthService healthService, JwtAuthenticationUtil jwtAuthenticationUtil) {
        this.healthService = healthService;
        this.jwtAuthenticationUtil = jwtAuthenticationUtil;
    }
    @GetMapping
    @Operation(summary = "Check infrastructure health", 
               description = "Returns the health status of MySQL, Redis, and other configured services")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "All checked components are UP"),
        @ApiResponse(responseCode = "503", description = "One or more critical services are DOWN")
    })
    public ResponseEntity<Map<String, Object>> checkHealth(HttpServletRequest request) {
        logger.info("Health check endpoint called");
        
        try {
            // Check if user is authenticated by verifying Authorization header
            boolean isAuthenticated = isUserAuthenticated(request);
            Map<String, Object> healthStatus = healthService.checkHealth(isAuthenticated);
            String overallStatus = (String) healthStatus.get("status");
            
            // Return 200 if overall status is UP, 503 if DOWN
            HttpStatus httpStatus = "UP".equals(overallStatus) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            
            logger.debug("Health check completed with status: {}", overallStatus);
            return new ResponseEntity<>(healthStatus, httpStatus);
            
        } catch (Exception e) {
            logger.error("Unexpected error during health check", e);
            
            // Return sanitized error response
            Map<String, Object> errorResponse = Map.of(
                "status", "DOWN",
                "error", "Health check service unavailable",
                "timestamp", Instant.now().toString()
            );
            
            return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private boolean isUserAuthenticated(HttpServletRequest request) {
        String token = null;
        
        // First, try to get token from JwtToken header
        token = request.getHeader("JwtToken");
        
        // If not found, try Authorization header
        if (token == null || token.trim().isEmpty()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && !authHeader.trim().isEmpty()) {
                // Extract token from "Bearer <token>" format
                token = authHeader.startsWith("Bearer ") 
                    ? authHeader.substring(7) 
                    : authHeader;
            }
        }
        
        // If still not found, try to get from cookies
        if (token == null || token.trim().isEmpty()) {
            token = getJwtTokenFromCookies(request);
        }
        
        // Validate the token if found
        if (token != null && !token.trim().isEmpty()) {
            try {
                return jwtAuthenticationUtil.validateUserIdAndJwtToken(token);
            } catch (Exception e) {
                logger.debug("JWT token validation failed: {}", e.getMessage());
                return false;
            }
        }
        
        return false;
    }
    
    private String getJwtTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("Jwttoken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
