package com.wipro.fhir.service.health;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    private static final Logger logger = LoggerFactory.getLogger(HealthService.class);
    private static final String STATUS_KEY = "status";
    private static final String DB_HEALTH_CHECK_QUERY = "SELECT 1 as health_check";
    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";
    private static final int REDIS_TIMEOUT_SECONDS = 3;

    private final DataSource dataSource;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final RedisTemplate<String, Object> redisTemplate;

    public HealthService(DataSource dataSource,
                        @Autowired(required = false) RedisTemplate<String, Object> redisTemplate) {
        this.dataSource = dataSource;
        this.redisTemplate = redisTemplate;
    }

    public Map<String, Object> checkHealth() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", Instant.now().toString());
        
        Map<String, Object> components = new LinkedHashMap<>();
        
        // Check MySQL
        components.put("mysql", checkMySQLHealth());
        
        // Check Redis if available
        if (redisTemplate != null) {
            components.put("redis", checkRedisHealth());
        }
        
        response.put("components", components);
        
        // Overall status: UP only if all components are UP
        boolean allUp = components.values().stream()
            .allMatch(value -> isHealthy((Map<String, Object>) value));
        response.put(STATUS_KEY, allUp ? STATUS_UP : STATUS_DOWN);
        
        logger.info("Health check completed - Overall status: {}", allUp ? STATUS_UP : STATUS_DOWN);
        
        return response;
    }

    private Map<String, Object> checkMySQLHealth() {
        Map<String, Object> status = new LinkedHashMap<>();
        
        return performHealthCheck("MySQL", status, () -> {
            try {
                try (Connection connection = dataSource.getConnection()) {
                    try (PreparedStatement stmt = connection.prepareStatement(DB_HEALTH_CHECK_QUERY)) {
                        stmt.setQueryTimeout(3);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) == 1) {
                                return new HealthCheckResult(true, null);
                            }
                        }
                    }
                    return new HealthCheckResult(false, "Query validation failed");
                }
            } catch (Exception e) {
                throw new IllegalStateException("MySQL health check failed: " + e.getMessage(), e);
            }
        });
    }

    private Map<String, Object> checkRedisHealth() {
        Map<String, Object> status = new LinkedHashMap<>();

        return performHealthCheck("Redis", status, () -> {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(
                () -> redisTemplate.execute((RedisCallback<String>) connection -> connection.ping()),
                executorService);
            try {
                String pong = future.get(REDIS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                
                if ("PONG".equals(pong)) {
                    return new HealthCheckResult(true, null);
                }
                return new HealthCheckResult(false, "Ping returned unexpected response");
            } catch (TimeoutException e) {
                future.cancel(true);
                return new HealthCheckResult(false, "Redis ping timed out after " + REDIS_TIMEOUT_SECONDS + " seconds");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new HealthCheckResult(false, "Redis health check was interrupted");
            } catch (Exception e) {
                throw new IllegalStateException("Redis health check failed", e);
            }
        });
    }

    private Map<String, Object> performHealthCheck(String componentName,
                                                    Map<String, Object> status,
                                                    Supplier<HealthCheckResult> checker) {
        long startTime = System.currentTimeMillis();
        
        try {
            HealthCheckResult result = checker.get();
            long responseTime = System.currentTimeMillis() - startTime;
            
            status.put("responseTimeMs", responseTime);

            if (result.isHealthy) {
                logger.debug("{} health check: UP ({}ms)", componentName, responseTime);
                status.put(STATUS_KEY, STATUS_UP);
            } else {
                String safeError = result.error != null ? result.error : "Health check failed";
                logger.warn("{} health check failed: {}", componentName, safeError);
                status.put(STATUS_KEY, STATUS_DOWN);
            }
            
            return status;
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            logger.error("{} health check failed with exception: {}", componentName, e.getMessage(), e);
            
            status.put(STATUS_KEY, STATUS_DOWN);
            status.put("responseTimeMs", responseTime);
            
            return status;
        }
    }

    private boolean isHealthy(Map<String, Object> componentStatus) {
        return STATUS_UP.equals(componentStatus.get(STATUS_KEY));
    }

    private static class HealthCheckResult {
        final boolean isHealthy;
        final String error;

        HealthCheckResult(boolean isHealthy, String error) {
            this.isHealthy = isHealthy;
            this.error = error;
        }
    }
}
