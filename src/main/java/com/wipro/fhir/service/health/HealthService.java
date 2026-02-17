package com.wipro.fhir.service.health;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    private static final Logger logger = LoggerFactory.getLogger(HealthService.class);
    private static final String STATUS_KEY = "status";
    private static final String DB_HEALTH_CHECK_QUERY = "SELECT 1 as health_check";
    private static final String DB_VERSION_QUERY = "SELECT VERSION()";
    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";
    private static final String UNKNOWN_VALUE = "unknown";
    private static final int REDIS_TIMEOUT_SECONDS = 3;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private final DataSource dataSource;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String dbUrl;
    private final String redisHost;
    private final int redisPort;

    public HealthService(DataSource dataSource,
                        @Autowired(required = false) RedisTemplate<String, Object> redisTemplate,
                        @Value("${spring.datasource.url:unknown}") String dbUrl,
                        @Value("${spring.data.redis.host:localhost}") String redisHost,
                        @Value("${spring.data.redis.port:6379}") int redisPort) {
        this.dataSource = dataSource;
        this.redisTemplate = redisTemplate;
        this.dbUrl = dbUrl;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
    }

    public Map<String, Object> checkHealth(boolean includeDetails) {
        Map<String, Object> healthStatus = new LinkedHashMap<>();
        Map<String, Object> components = new LinkedHashMap<>();
        boolean overallHealth = true;

        Map<String, Object> mysqlStatus = checkMySQLHealth(includeDetails);
        components.put("mysql", mysqlStatus);
        if (!isHealthy(mysqlStatus)) {
            overallHealth = false;
        }

        if (redisTemplate != null) {
            Map<String, Object> redisStatus = checkRedisHealth(includeDetails);
            components.put("redis", redisStatus);
            if (!isHealthy(redisStatus)) {
                overallHealth = false;
            }
        }

        healthStatus.put(STATUS_KEY, overallHealth ? STATUS_UP : STATUS_DOWN);
        healthStatus.put("timestamp", Instant.now().toString());
        healthStatus.put("components", components);
        logger.info("Health check completed - Overall status: {}", overallHealth ? STATUS_UP : STATUS_DOWN);

        return healthStatus;
    }

    public Map<String, Object> checkHealth() {
        return checkHealth(true);
    }

    private Map<String, Object> checkMySQLHealth(boolean includeDetails) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("type", "MySQL");
        
        if (includeDetails) {
            details.put("host", extractHost(dbUrl));
            details.put("port", extractPort(dbUrl));
            details.put("database", extractDatabaseName(dbUrl));
        }

        return performHealthCheck("MySQL", details, () -> {
            try {
                try (Connection connection = dataSource.getConnection()) {
                    if (connection.isValid(2)) {
                        try (PreparedStatement stmt = connection.prepareStatement(DB_HEALTH_CHECK_QUERY)) {
                            stmt.setQueryTimeout(3);
                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next() && rs.getInt(1) == 1) {
                                    String version = includeDetails ? getMySQLVersion(connection) : null;
                                    return new HealthCheckResult(true, version, null);
                                }
                            }
                        }
                    }
                    return new HealthCheckResult(false, null, "Connection validation failed");
                }
            } catch (Exception e) {
                throw new IllegalStateException("Failed to perform MySQL health check", e);
            }
        });
    }

    private Map<String, Object> checkRedisHealth(boolean includeDetails) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("type", "Redis");
        
        if (includeDetails) {
            details.put("host", redisHost);
            details.put("port", redisPort);
        }

        return performHealthCheck("Redis", details, () -> {
            try {
                // Wrap PING in CompletableFuture with timeout
                String pong = CompletableFuture.supplyAsync(() ->
                    redisTemplate.execute((RedisCallback<String>) connection -> connection.ping()),
                    executorService
                ).get(REDIS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                
                if ("PONG".equals(pong)) {
                    String version = includeDetails ? getRedisVersionWithTimeout() : null;
                    return new HealthCheckResult(true, version, null);
                }
                return new HealthCheckResult(false, null, "Ping returned unexpected response");
            } catch (TimeoutException e) {
                return new HealthCheckResult(false, null, "Redis ping timed out after " + REDIS_TIMEOUT_SECONDS + " seconds");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new HealthCheckResult(false, null, "Redis health check was interrupted");
            } catch (Exception e) {
                throw new IllegalStateException("Redis health check failed", e);
            }
        });
    }

    private Map<String, Object> performHealthCheck(String componentName,
                                                    Map<String, Object> details,
                                                    Supplier<HealthCheckResult> checker) {
        Map<String, Object> status = new LinkedHashMap<>();
        long startTime = System.currentTimeMillis();
        
        try {
            HealthCheckResult result = checker.get();
            long responseTime = System.currentTimeMillis() - startTime;
            
            details.put("responseTimeMs", responseTime);

            if (result.isHealthy) {
                logger.debug("{} health check: UP ({}ms)", componentName, responseTime);
                status.put(STATUS_KEY, STATUS_UP);
                if (result.version != null) {
                    details.put("version", result.version);
                }
            } else {
                String safeError = result.error != null ? result.error : "Health check failed";
                logger.warn("{} health check failed: {}", componentName, safeError);
                status.put(STATUS_KEY, STATUS_DOWN);
                details.put("error", safeError);
                details.put("errorType", "CheckFailed");
            }
            
            status.put("details", details);
            return status;
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            
            logger.error("{} health check failed", componentName, e);
            
            status.put(STATUS_KEY, STATUS_DOWN);
            details.put("responseTimeMs", responseTime);
            details.put("error", "Health check failed");
            details.put("errorType", "InternalError");
            status.put("details", details);
            
            return status;
        }
    }

    private boolean isHealthy(Map<String, Object> componentStatus) {
        return STATUS_UP.equals(componentStatus.get(STATUS_KEY));
    }

    private String getMySQLVersion(Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(DB_VERSION_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            logger.debug("Could not retrieve MySQL version", e);
        }
        return null;
    }

    private String getRedisVersion() {
        try {
            Properties info = redisTemplate.execute((RedisCallback<Properties>) connection ->
                connection.serverCommands().info("server")
            );
            if (info != null && info.containsKey("redis_version")) {
                return info.getProperty("redis_version");
            }
        } catch (Exception e) {
            logger.debug("Could not retrieve Redis version", e);
        }
        return null;
    }

    private String getRedisVersionWithTimeout() {
        try {
            return CompletableFuture.supplyAsync(this::getRedisVersion, executorService)
                .get(REDIS_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logger.debug("Redis version retrieval timed out");
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.debug("Redis version retrieval was interrupted");
            return null;
        } catch (Exception e) {
            logger.debug("Could not retrieve Redis version with timeout", e);
            return null;
        }
    }

    private String extractHost(String jdbcUrl) {
        if (jdbcUrl == null || UNKNOWN_VALUE.equals(jdbcUrl)) {
            return UNKNOWN_VALUE;
        }
        try {
            String withoutPrefix = jdbcUrl.replaceFirst("jdbc:mysql://", "");
            int slashIndex = withoutPrefix.indexOf('/');
            String hostPort = slashIndex > 0
                ? withoutPrefix.substring(0, slashIndex)
                : withoutPrefix;
            int colonIndex = hostPort.indexOf(':');
            return colonIndex > 0 ? hostPort.substring(0, colonIndex) : hostPort;
        } catch (Exception e) {
            logger.debug("Could not extract host from URL", e);
        }
        return UNKNOWN_VALUE;
    }

    private String extractPort(String jdbcUrl) {
        if (jdbcUrl == null || UNKNOWN_VALUE.equals(jdbcUrl)) {
            return UNKNOWN_VALUE;
        }
        try {
            String withoutPrefix = jdbcUrl.replaceFirst("jdbc:mysql://", "");
            int slashIndex = withoutPrefix.indexOf('/');
            String hostPort = slashIndex > 0
                ? withoutPrefix.substring(0, slashIndex)
                : withoutPrefix;
            int colonIndex = hostPort.indexOf(':');
            return colonIndex > 0 ? hostPort.substring(colonIndex + 1) : "3306";
        } catch (Exception e) {
            logger.debug("Could not extract port from URL", e);
        }
        return "3306";
    }

    private String extractDatabaseName(String jdbcUrl) {
        if (jdbcUrl == null || UNKNOWN_VALUE.equals(jdbcUrl)) {
            return UNKNOWN_VALUE;
        }
        try {
            int lastSlash = jdbcUrl.lastIndexOf('/');
            if (lastSlash >= 0 && lastSlash < jdbcUrl.length() - 1) {
                String afterSlash = jdbcUrl.substring(lastSlash + 1);
                int queryStart = afterSlash.indexOf('?');
                if (queryStart > 0) {
                    return afterSlash.substring(0, queryStart);
                }
                return afterSlash;
            }
        } catch (Exception e) {
            logger.debug("Could not extract database name from URL", e);
        }
        return UNKNOWN_VALUE;
    }

    private static class HealthCheckResult {
        final boolean isHealthy;
        final String version;
        final String error;

        HealthCheckResult(boolean isHealthy, String version, String error) {
            this.isHealthy = isHealthy;
            this.version = version;
            this.error = error;
        }
    }
}
