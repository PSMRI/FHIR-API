package com.wipro.fhir.utils;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieUtil {

	public Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return Optional.of(cookie.getValue());
				}
			}
		}
		return Optional.empty();
	}

	public void addJwtTokenToCookie(String Jwttoken, HttpServletResponse response) {
		// Create a new cookie with the JWT token
		Cookie cookie = new Cookie("Jwttoken", Jwttoken);
		cookie.setHttpOnly(true); // Prevent JavaScript access for security
		cookie.setSecure(true); // Ensure the cookie is sent only over HTTPS
		cookie.setMaxAge(60 * 60 * 24); // 1 day expiration time
		cookie.setPath("/"); // Make the cookie available for the entire application
		response.addCookie(cookie); // Add the cookie to the response
	}

	public String getJwtTokenFromCookie(HttpServletRequest request) {
		return Arrays.stream(request.getCookies()).filter(cookie -> "Jwttoken".equals(cookie.getName()))
				.map(Cookie::getValue).findFirst().orElse(null);
	}
}
