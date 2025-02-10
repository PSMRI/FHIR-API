package com.wipro.fhir.utils;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<JwtUserIdValidationFilter> jwtUserIdValidationFilter(
			JwtAuthenticationUtil jwtAuthenticationUtil) {
		FilterRegistrationBean<JwtUserIdValidationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtUserIdValidationFilter(jwtAuthenticationUtil));
		registrationBean.addUrlPatterns("/*"); // Apply filter to all API endpoints
		return registrationBean;
	}

}
