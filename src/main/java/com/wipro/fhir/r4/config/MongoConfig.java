package com.wipro.fhir.r4.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;
import com.wipro.fhir.r4.utils.CryptoUtil;
import com.wipro.fhir.r4.utils.config.ConfigProperties;

@Configuration
public class MongoConfig {

	@Autowired
	private CryptoUtil cryptoUtil;

	String uri = "mongodb://userName:pass@host:port/database?authSource=authentication";

	@Bean
	public MongoDbFactory mongoDbFactory() throws UnknownHostException {
		String host = ConfigProperties.getPropertyByName("mongodb.host");
		String port = ConfigProperties.getPropertyByName("mongodb.port");
		String auth = ConfigProperties.getPropertyByName("mongodb.authentication-database");
		String database = ConfigProperties.getPropertyByName("mongodb.database");

		String userName = cryptoUtil.decrypt(ConfigProperties.getPropertyByName("encMongoDbUserName"));
		String pass = cryptoUtil.decrypt(ConfigProperties.getPropertyByName("encMongoDbPass"));
		String mongoURI = uri.replace("userName", userName).replace("pass", pass).replace("host", host)
				.replace("port", port).replace("database", database).replace("authentication", auth);

		return new SimpleMongoDbFactory(new MongoClientURI(mongoURI));
	}

	@Bean
	public MongoTemplate mongoTemplate() throws UnknownHostException {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());

		return mongoTemplate;

	}

}
