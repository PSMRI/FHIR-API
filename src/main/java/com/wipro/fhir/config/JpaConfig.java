package com.wipro.fhir.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.wipro.fhir.data.e_aushdhi.ItemMaster;
import com.wipro.fhir.repo.e_aushdhi.ItemRepo;

@Configuration
@EnableJpaRepositories(basePackages = { "com.wipro.fhir.repo.e_aushdhi",
		"com.wipro.fhir.repo.patient_data_handler", 
		"com.wipro.fhir.repo.healthID",
		"com.wipro.fhir.repo.common",
		"com.wipro.fhir.repo.patient_data_handler"
})
@EntityScan(basePackages = { "com.wipro.fhir.data.e_aushdhi", 
		"com.wipro.fhir.entity.patient_data_handler",
		"com.wipro.fhir.data.healthID",
		"com.wipro.fhir.data.request_handler",
		"com.wipro.fhir.repo.patient_data_handler"

})
public class JpaConfig {

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean
	DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
}