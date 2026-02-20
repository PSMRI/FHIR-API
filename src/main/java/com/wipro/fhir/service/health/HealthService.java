/*
* AMRIT – Accessible Medical Records via Integrated Technology 
* Integrated EHR (Electronic Health Records) Solution 
*
* Copyright (C) "Piramal Swasthya Management and Research Institute" 
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/

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
import jakarta.annotation.PreDestroy;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    private static final Logger logger = LoggerFactory.getLogger(HealthService.class);

    // Status constants
    private static final String STATUS_KEY = "status";
    private static final String STATUS_UP = "UP";
    private static final String STATUS_DOWN = "DOWN";
    private static final String STATUS_DEGRADED = "DEGRADED";
    
    // Severity levels and keys
    private static final String SEVERITY_KEY = "severity";
    private static final String SEVERITY_OK = "OK";
    private static final String SEVERITY_WARNING = "WARNING";
    private static final String SEVERITY_CRITICAL = "CRITICAL";
    
    // Response keys
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";
    private static final String RESPONSE_TIME_KEY = "responseTimeMs";
    
    // Timeouts (in seconds)
    private static final long MYSQL_TIMEOUT_SECONDS = 3;
    private static final long REDIS_TIMEOUT_SECONDS = 3;
    
    // Advanced checks configuration
    private static final long ADVANCED_CHECKS_TIMEOUT_MS = 500; // Strict timeout for advanced checks
    private static final long ADVANCED_CHECKS_THROTTLE_SECONDS = 30; // Run at most once per 30 seconds
    
    // Performance threshold (milliseconds) - response time > 2000ms = DEGRADED
    private static final long RESPONSE_TIME_THRESHOLD_MS = 2000;
    
    // Diagnostic event codes for concise logging
    private static final String DIAGNOSTIC_LOCK_WAIT = "MYSQL_LOCK_WAIT";
    private static final String DIAGNOSTIC_DEADLOCK = "MYSQL_DEADLOCK";
    private static final String DIAGNOSTIC_SLOW_QUERIES = "MYSQL_SLOW_QUERIES";
    private static final String DIAGNOSTIC_POOL_EXHAUSTED = "MYSQL_POOL_EXHAUSTED";
    private static final String DIAGNOSTIC_LOG_TEMPLATE = "Diagnostic: {}";

    private final DataSource dataSource;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExecutorService executorService;
    
    // Advanced checks throttling (thread-safe)
    private volatile long lastAdvancedCheckTime = 0;
    private volatile AdvancedCheckResult cachedAdvancedCheckResult = null;
    private final ReentrantReadWriteLock advancedCheckLock = new ReentrantReadWriteLock();
    
    // Deadlock check resilience - disable after first permission error
    private volatile boolean deadlockCheckDisabled = false;
    
    @Value("${health.advanced.enabled:true}")
    private boolean advancedHealthChecksEnabled;

    public HealthService(DataSource dataSource,
                        @Autowired(required = false) RedisTemplate<String, Object> redisTemplate) {
        this.dataSource = dataSource;
        this.redisTemplate = redisTemplate;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    @PreDestroy
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            try {
                executorService.shutdown();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    logger.warn("ExecutorService did not terminate gracefully");
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
                logger.warn("ExecutorService shutdown interrupted", e);
            }
        }
    }

    public Map<String, Object> checkHealth() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", Instant.now().toString());
        
        Map<String, Object> mysqlStatus = new LinkedHashMap<>();
        Map<String, Object> redisStatus = new LinkedHashMap<>();
        
        // Submit both checks concurrently
        CompletableFuture<Void> mysqlFuture = CompletableFuture.runAsync(
            () -> performHealthCheck("MySQL", mysqlStatus, this::checkMySQLHealthSync), executorService);
        CompletableFuture<Void> redisFuture = CompletableFuture.runAsync(
            () -> performHealthCheck("Redis", redisStatus, this::checkRedisHealthSync), executorService);
        
        // Wait for both checks to complete with combined timeout
        long maxTimeout = Math.max(MYSQL_TIMEOUT_SECONDS, REDIS_TIMEOUT_SECONDS) + 1;
        try {
            CompletableFuture.allOf(mysqlFuture, redisFuture)
                .get(maxTimeout, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            logger.warn("Health check aggregate timeout after {} seconds", maxTimeout);
            mysqlFuture.cancel(true);
            redisFuture.cancel(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Health check was interrupted");
            mysqlFuture.cancel(true);
            redisFuture.cancel(true);
        } catch (Exception e) {
            logger.warn("Health check execution error: {}", e.getMessage());
        }
        
        // Ensure timed-out or unfinished components are marked DOWN
        ensurePopulated(mysqlStatus, "MySQL");
        ensurePopulated(redisStatus, "Redis");
        
        Map<String, Map<String, Object>> components = new LinkedHashMap<>();
        components.put("mysql", mysqlStatus);
        components.put("redis", redisStatus);
        
        response.put("components", components);
        
        // Compute overall status
        String overallStatus = computeOverallStatus(components);
        response.put(STATUS_KEY, overallStatus);
        
        return response;
    }

    private void ensurePopulated(Map<String, Object> status, String componentName) {
        if (!status.containsKey(STATUS_KEY)) {
            status.put(STATUS_KEY, STATUS_DOWN);
            status.put(SEVERITY_KEY, SEVERITY_CRITICAL);
            status.put(ERROR_KEY, componentName + " health check did not complete in time");
        }
    }

    private HealthCheckResult checkMySQLHealthSync() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT 1 as health_check")) {
            
            stmt.setQueryTimeout((int) MYSQL_TIMEOUT_SECONDS);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Basic health check passed, now run advanced checks with throttling
                    boolean isDegraded = performAdvancedMySQLChecksWithThrottle(connection);
                    return new HealthCheckResult(true, null, isDegraded);
                }
            }
            
            return new HealthCheckResult(false, "No result from health check query", false);
            
        } catch (Exception e) {
            logger.warn("MySQL health check failed: {}", e.getMessage(), e);
            return new HealthCheckResult(false, "MySQL connection failed", false);
        }
    }

    private HealthCheckResult checkRedisHealthSync() {
        if (redisTemplate == null) {
            return new HealthCheckResult(true, "Redis not configured — skipped", false);
        }
        
        try {
            String pong = redisTemplate.execute((org.springframework.data.redis.core.RedisCallback<String>) (connection) -> connection.ping());
            
            if ("PONG".equals(pong)) {
                return new HealthCheckResult(true, null, false);
            }
            
            return new HealthCheckResult(false, "Redis PING failed", false);
            
        } catch (Exception e) {
            logger.warn("Redis health check failed: {}", e.getMessage(), e);
            return new HealthCheckResult(false, "Redis connection failed", false);
        }
    }

    private Map<String, Object> performHealthCheck(String componentName,
                                                    Map<String, Object> status,
                                                    Supplier<HealthCheckResult> checker) {
        long startTime = System.currentTimeMillis();
        
        try {
            HealthCheckResult result = checker.get();
            long responseTime = System.currentTimeMillis() - startTime;
            
            // Determine status: DOWN (unhealthy), DEGRADED (healthy but with issues), or UP
            String componentStatus;
            if (!result.isHealthy) {
                componentStatus = STATUS_DOWN;
            } else if (result.isDegraded) {
                componentStatus = STATUS_DEGRADED;
            } else {
                componentStatus = STATUS_UP;
            }
            status.put(STATUS_KEY, componentStatus);
            
            // Set response time
            status.put(RESPONSE_TIME_KEY, responseTime);
            
            // Determine severity based on health, response time, and degradation flags
            String severity = determineSeverity(result.isHealthy, responseTime, result.isDegraded);
            status.put(SEVERITY_KEY, severity);
            
            // Include message or error based on health status
            if (result.error != null) {
                // Use MESSAGE_KEY for informational messages when healthy
                // Use ERROR_KEY for actual error messages when unhealthy
                String fieldKey = result.isHealthy ? MESSAGE_KEY : ERROR_KEY;
                status.put(fieldKey, result.error);
            }
            
            return status;
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            logger.error("{} health check failed with exception: {}", componentName, e.getMessage(), e);
            
            status.put(STATUS_KEY, STATUS_DOWN);
            status.put(RESPONSE_TIME_KEY, responseTime);
            status.put(SEVERITY_KEY, SEVERITY_CRITICAL);
            status.put(ERROR_KEY, "Health check failed with an unexpected error");
            
            return status;
        }
    }

    private String determineSeverity(boolean isHealthy, long responseTimeMs, boolean isDegraded) {
        if (!isHealthy) {
            return SEVERITY_CRITICAL;
        }
        
        if (isDegraded) {
            return SEVERITY_WARNING;
        }
        
        if (responseTimeMs > RESPONSE_TIME_THRESHOLD_MS) {
            return SEVERITY_WARNING;
        }
        
        return SEVERITY_OK;
    }

    private String computeOverallStatus(Map<String, Map<String, Object>> components) {
        boolean hasCritical = false;
        boolean hasDegraded = false;
        
        for (Map<String, Object> componentStatus : components.values()) {
            String status = (String) componentStatus.get(STATUS_KEY);
            String severity = (String) componentStatus.get(SEVERITY_KEY);
            
            if (STATUS_DOWN.equals(status) || SEVERITY_CRITICAL.equals(severity)) {
                hasCritical = true;
            }
            
            if (STATUS_DEGRADED.equals(status)) {
                hasDegraded = true;
            }
            
            if (SEVERITY_WARNING.equals(severity)) {
                hasDegraded = true;
            }
        }
        
        if (hasCritical) {
            return STATUS_DOWN;
        }
        
        if (hasDegraded) {
            return STATUS_DEGRADED;
        }
        
        return STATUS_UP;
    }

    // Internal advanced health checks for MySQL - do not expose details in responses
    private boolean performAdvancedMySQLChecksWithThrottle(Connection connection) {
        if (!advancedHealthChecksEnabled) {
            return false; // Advanced checks disabled
        }
        
        long currentTime = System.currentTimeMillis();
        
        // Check throttle window - use read lock first for fast path
        advancedCheckLock.readLock().lock();
        try {
            if (cachedAdvancedCheckResult != null && 
                (currentTime - lastAdvancedCheckTime) < ADVANCED_CHECKS_THROTTLE_SECONDS * 1000) {
                // Return cached result - within throttle window
                return cachedAdvancedCheckResult.isDegraded;
            }
        } finally {
            advancedCheckLock.readLock().unlock();
        }
        
        // Outside throttle window - acquire write lock and run checks
        advancedCheckLock.writeLock().lock();
        try {
            // Double-check after acquiring write lock
            if (cachedAdvancedCheckResult != null && 
                (currentTime - lastAdvancedCheckTime) < ADVANCED_CHECKS_THROTTLE_SECONDS * 1000) {
                return cachedAdvancedCheckResult.isDegraded;
            }
            
            AdvancedCheckResult result = performAdvancedMySQLChecks(connection);
            
            // Cache the result
            lastAdvancedCheckTime = currentTime;
            cachedAdvancedCheckResult = result;
            
            return result.isDegraded;
        } finally {
            advancedCheckLock.writeLock().unlock();
        }
    }

    private AdvancedCheckResult performAdvancedMySQLChecks(Connection connection) {
        try {
            boolean hasIssues = false;
            
            if (hasLockWaits(connection)) {
                logger.warn(DIAGNOSTIC_LOG_TEMPLATE, DIAGNOSTIC_LOCK_WAIT);
                hasIssues = true;
            }
            
            if (hasDeadlocks(connection)) {
                logger.warn(DIAGNOSTIC_LOG_TEMPLATE, DIAGNOSTIC_DEADLOCK);
                hasIssues = true;
            }
            
            if (hasSlowQueries(connection)) {
                logger.warn(DIAGNOSTIC_LOG_TEMPLATE, DIAGNOSTIC_SLOW_QUERIES);
                hasIssues = true;
            }
            
            if (hasConnectionPoolExhaustion()) {
                logger.warn(DIAGNOSTIC_LOG_TEMPLATE, DIAGNOSTIC_POOL_EXHAUSTED);
                hasIssues = true;
            }
            
            return new AdvancedCheckResult(hasIssues);
        } catch (Exception e) {
            logger.debug("Advanced MySQL checks encountered exception, marking degraded");
            return new AdvancedCheckResult(true);
        }
    }

    private boolean hasLockWaits(Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.PROCESSLIST " +
                "WHERE (state = 'Waiting for table metadata lock' " +
                "   OR state = 'Waiting for row lock' " +
                "   OR state = 'Waiting for lock') " +
                "AND user NOT IN ('event_scheduler', 'system user', 'root')")) {
            stmt.setQueryTimeout(2);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int lockCount = rs.getInt(1);
                    return lockCount > 0;
                }
            }
        } catch (Exception e) {
            logger.debug("Could not check for lock waits");
        }
        return false;
    }

    private boolean hasDeadlocks(Connection connection) {
        // Skip deadlock check if already disabled due to permissions
        if (deadlockCheckDisabled) {
            return false;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement("SHOW ENGINE INNODB STATUS")) {
            stmt.setQueryTimeout(2);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String innodbStatus = rs.getString(3);
                    return innodbStatus != null && innodbStatus.contains("LATEST DETECTED DEADLOCK");
                }
            }
        } catch (java.sql.SQLException e) {
            // Check if this is a permission error
            if (e.getMessage() != null && 
                (e.getMessage().contains("Access denied") || 
                 e.getMessage().contains("permission"))) {
                // Disable this check permanently after first permission error
                deadlockCheckDisabled = true;
                logger.warn("Deadlock check disabled: Insufficient privileges");
            } else {
                logger.debug("Could not check for deadlocks");
            }
        } catch (Exception e) {
            logger.debug("Could not check for deadlocks");
        }
        return false;
    }

    private boolean hasSlowQueries(Connection connection) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.PROCESSLIST " +
                "WHERE command != 'Sleep' AND time > ? AND user NOT IN ('event_scheduler', 'system user')")) {
            stmt.setQueryTimeout(2);
            stmt.setInt(1, 10); // Queries running longer than 10 seconds
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int slowQueryCount = rs.getInt(1);
                    return slowQueryCount > 3; // Alert if more than 3 slow queries
                }
            }
        } catch (Exception e) {
            logger.debug("Could not check for slow queries");
        }
        return false;
    }

    private boolean hasConnectionPoolExhaustion() {
        // Use HikariCP metrics if available
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            try {
                HikariPoolMXBean poolMXBean = hikariDataSource.getHikariPoolMXBean();
                
                if (poolMXBean != null) {
                    int activeConnections = poolMXBean.getActiveConnections();
                    int maxPoolSize = hikariDataSource.getMaximumPoolSize();
                    
                    // Alert if > 80% of pool is exhausted
                    int threshold = (int) (maxPoolSize * 0.8);
                    return activeConnections > threshold;
                }
            } catch (Exception e) {
                logger.debug("Could not retrieve HikariCP pool metrics");
            }
        }
        
        // Fallback: try to get pool metrics via JMX if HikariCP is not directly available
        return checkPoolMetricsViaJMX();
    }

    private boolean checkPoolMetricsViaJMX() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objectName = new ObjectName("com.zaxxer.hikari:type=Pool (*)");
            var mBeans = mBeanServer.queryMBeans(objectName, null);
            
            for (var mBean : mBeans) {
                if (evaluatePoolMetrics(mBeanServer, mBean.getObjectName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.debug("Could not access HikariCP pool metrics via JMX");
        }
        
        // No pool metrics available - disable this check
        logger.debug("Pool exhaustion check disabled: HikariCP metrics unavailable");
        return false;
    }

    private boolean evaluatePoolMetrics(MBeanServer mBeanServer, ObjectName objectName) {
        try {
            Integer activeConnections = (Integer) mBeanServer.getAttribute(objectName, "ActiveConnections");
            Integer maximumPoolSize = (Integer) mBeanServer.getAttribute(objectName, "MaximumPoolSize");
            
            if (activeConnections != null && maximumPoolSize != null) {
                int threshold = (int) (maximumPoolSize * 0.8);
                return activeConnections > threshold;
            }
        } catch (Exception e) {
            // Continue to next MBean
        }
        return false;
    }

    private static class AdvancedCheckResult {
        final boolean isDegraded;

        AdvancedCheckResult(boolean isDegraded) {
            this.isDegraded = isDegraded;
        }
    }

    private static class HealthCheckResult {
        final boolean isHealthy;
        final String error;
        final boolean isDegraded;

        HealthCheckResult(boolean isHealthy, String error, boolean isDegraded) {
            this.isHealthy = isHealthy;
            this.error = error;
            this.isDegraded = isDegraded;
        }
    }
}
