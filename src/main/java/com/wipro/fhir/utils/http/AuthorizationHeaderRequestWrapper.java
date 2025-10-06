package com.wipro.fhir.utils.http;


import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class AuthorizationHeaderRequestWrapper extends HttpServletRequestWrapper{
	private final String Authorization;

    public AuthorizationHeaderRequestWrapper(HttpServletRequest request, String authHeaderValue) {
        super(request);
        this.Authorization = authHeaderValue;
    }

    @Override
    public String getHeader(String name) {
        if ("Authorization".equalsIgnoreCase(name)) {
            return Authorization;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if ("Authorization".equalsIgnoreCase(name)) {
            return Collections.enumeration(Collections.singletonList(Authorization));
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        if (!names.contains("Authorization")) {
            names.add("Authorization");
        }
        return Collections.enumeration(names);
    }
}
