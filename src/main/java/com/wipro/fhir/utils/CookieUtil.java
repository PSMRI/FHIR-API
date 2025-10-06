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

	public static String getJwtTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() == null) {
	        return null;  // If cookies are null, return null safely.
	    }
		return Arrays.stream(request.getCookies()).filter(cookie -> "Jwttoken".equals(cookie.getName()))
				.map(Cookie::getValue).findFirst().orElse(null);
	}
}
