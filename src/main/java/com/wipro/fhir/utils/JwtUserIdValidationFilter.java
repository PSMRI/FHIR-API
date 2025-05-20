package com.wipro.fhir.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wipro.fhir.utils.http.AuthorizationHeaderRequestWrapper;

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
		
		// Skip login and public endpoints
		if (shouldSkipPath(path, contextPath)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		try {
			String jwtFromCookie = getJwtTokenFromCookies(request);
			String jwtFromHeader = request.getHeader("JwtToken");
			String authHeader = request.getHeader("Authorization");

			if (jwtFromCookie != null) {
				logger.info("Validating JWT token from cookie");
				if (jwtAuthenticationUtil.validateUserIdAndJwtToken(jwtFromCookie)) {
					AuthorizationHeaderRequestWrapper authorizationHeaderRequestWrapper = new AuthorizationHeaderRequestWrapper(request, "");
					filterChain.doFilter(authorizationHeaderRequestWrapper, servletResponse);
					return;
				}
			}

			if (jwtFromHeader != null) {
				logger.info("Validating JWT token from header");
				if (jwtAuthenticationUtil.validateUserIdAndJwtToken(jwtFromHeader)) {
					AuthorizationHeaderRequestWrapper authorizationHeaderRequestWrapper = new AuthorizationHeaderRequestWrapper(request, "");
					filterChain.doFilter(authorizationHeaderRequestWrapper, servletResponse);
					return;
				}
			}
			String userAgent = request.getHeader("User-Agent");
			logger.info("User-Agent: " + userAgent);

			if (userAgent != null && isMobileClient(userAgent) && authHeader != null) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			logger.warn("No valid authentication token found");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid or missing token");

		} catch (Exception e) {
			logger.error("Authorization error: ", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization error: " + e.getMessage());
		}
	}

	private boolean isMobileClient(String userAgent) {
		if (userAgent == null)
			return false;

		userAgent = userAgent.toLowerCase();

		return userAgent.contains("okhttp"); // iOS (custom clients)
	}

	private boolean shouldSkipPath(String path, String contextPath) {
		return path.equals(contextPath + "/user/userAuthenticate")
				|| path.equalsIgnoreCase(contextPath + "/user/logOutUserFromConcurrentSession")
				|| path.startsWith(contextPath + "/swagger-ui")
				|| path.startsWith(contextPath + "/v3/api-docs")
				|| path.startsWith(contextPath + "/public");
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
