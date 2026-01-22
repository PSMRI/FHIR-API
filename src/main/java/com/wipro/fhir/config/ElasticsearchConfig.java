package com.wipro.fhir.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.username}")
    private String esUsername;

    @Value("${elasticsearch.password}")
    private String esPassword;

    @Value("${elasticsearch.connection.timeout:10000}")
    private int connectionTimeout;

    @Value("${elasticsearch.socket.timeout:120000}")
    private int socketTimeout;

    @Value("${elasticsearch.max.connections:200}")
    private int maxConnections;

    @Value("${elasticsearch.max.connections.per.route:100}")
    private int maxConnectionsPerRoute;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            AuthScope.ANY,
            new UsernamePasswordCredentials(esUsername, esPassword)
        );

        RestClientBuilder builder = RestClient.builder(
            new HttpHost(esHost, esPort, "http")
        );

        // Apply timeout configurations
        builder.setRequestConfigCallback(requestConfigBuilder -> 
            requestConfigBuilder
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectionTimeout)
        );

        // Apply connection pool settings
        builder.setHttpClientConfigCallback(httpClientBuilder -> 
            httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider)
                .setMaxConnTotal(maxConnections)
                .setMaxConnPerRoute(maxConnectionsPerRoute)
                .setDefaultIOReactorConfig(
                    IOReactorConfig.custom()
                        .setSoTimeout(socketTimeout)
                        .build()
                )
        );

        RestClient restClient = builder.build();

        ElasticsearchTransport transport = new RestClientTransport(
            restClient,
            new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }

    @Bean(name = "esAsyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  
        executor.setMaxPoolSize(20);  
        executor.setQueueCapacity(500);  
        executor.setThreadNamePrefix("es-sync-");
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}