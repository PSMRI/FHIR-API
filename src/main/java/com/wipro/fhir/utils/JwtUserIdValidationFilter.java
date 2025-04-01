package com.wipro.fhir.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtUserIdValidationFilter implements Filter {

	private final JwtAuthenticationUtil jwtAuthenticationUtil;
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public JwtUserIdValidationFilter(JwtAuthenticationUtil jwtAuthenticationUtil) {
		this.jwtAuthenticationUtil = jwtAuthenticationUtil;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		logger.info("JwtUserIdValidationFilter invoked for path: " + path);

		// Log cookies for debugging
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userId".equals(cookie.getName())) {
					logger.warn("userId found in cookies! Clearing it...");
					clearUserIdCookie(response); // Explicitly remove userId cookie
				}
			}
		} else {
			logger.info("No cookies found in the request");
		}

		// Log headers for debugging
		String jwtTokenFromHeader = request.getHeader("Jwttoken");
		logger.info("JWT token from header: ");

		// Skip login and public endpoints
		if (path.equals(contextPath + "/user/userAuthenticate")
				|| path.equalsIgnoreCase(contextPath + "/user/logOutUserFromConcurrentSession")
				|| path.startsWith(contextPath + "/swagger-ui")
				|| path.startsWith(contextPath + "/v3/api-docs")
				|| path.startsWith(contextPath + "/public")) {
			logger.info("Skipping filter for path: " + path);
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		try {
			// Retrieve JWT token from cookies
			String jwtTokenFromCookie = getJwtTokenFromCookies(request);
			logger.info("JWT token from cookie: ");

			// Determine which token (cookie or header) to validate
			String jwtToken = jwtTokenFromCookie != null ? jwtTokenFromCookie : jwtTokenFromHeader;
			if (jwtToken == null) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token not found in cookies or headers");
				return;
			}

			// Validate JWT token and userId
			boolean isValid = jwtAuthenticationUtil.validateUserIdAndJwtToken(jwtToken);

			if (isValid) {
				// If token is valid, allow the request to proceed
				filterChain.doFilter(servletRequest, servletResponse);
			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
			}
		} catch (Exception e) {
			logger.error("Authorization error: ", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization error: " + e.getMessage());
		}
	}

	private String getJwtTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("Jwttoken")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	private void clearUserIdCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("userId", null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(0); // Invalidate the cookie
		response.addCookie(cookie);
	}
}
