package com.wipro.fhir.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	@Bean
	public FilterRegistrationBean<JwtUserIdValidationFilter> jwtUserIdValidationFilter(
			JwtAuthenticationUtil jwtAuthenticationUtil) {
		FilterRegistrationBean<JwtUserIdValidationFilter> registrationBean = new FilterRegistrationBean<>();

		// Pass allowedOrigins explicitly to the filter constructor
		JwtUserIdValidationFilter filter = new JwtUserIdValidationFilter(jwtAuthenticationUtil, allowedOrigins);

		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("/*"); // Apply filter to all API endpoints
		return registrationBean;
	}
}
