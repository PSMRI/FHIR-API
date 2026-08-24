/*
* AMRIT - Accessible Medical Records via Integrated Technologies
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
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("HealthService Test Suite")
class HealthServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private HealthService healthService;

    @BeforeEach
    @DisplayName("Set up the service with mocked infrastructure before each test")
    void setUp() {
        healthService = new HealthService(dataSource, redisTemplate);
    }

    @AfterEach
    void tearDown() {
        healthService.shutdown();
    }

    /**
     * Wires the JDBC mock chain so every query the service issues succeeds.
     * getInt(1) backs the advanced lock-wait and slow-query diagnostics: a value
     * above zero reads as a lock wait, a value above three as too many slow queries.
     */
    private void stubJdbc(int diagnosticCount) throws SQLException {
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        lenient().when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        lenient().doNothing().when(preparedStatement).setQueryTimeout(anyInt());
        lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
        lenient().when(resultSet.next()).thenReturn(true);
        lenient().when(resultSet.getInt(1)).thenReturn(diagnosticCount);
    }

    private void stubRedisPong(String pong) {
        lenient().when(redisTemplate.execute(any(RedisCallback.class))).thenReturn(pong);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> component(Map<String, Object> response, String key) {
        return ((Map<String, Map<String, Object>>) response.get("components")).get(key);
    }

    @Nested
    @DisplayName("Overall health aggregation")
    class OverallStatusTests {

        @Test
        @DisplayName("checkHealth should report UP when both MySQL and Redis are healthy")
        void checkHealth_shouldReportUpWhenAllComponentsHealthy() throws SQLException {
            stubJdbc(0);
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("UP", response.get("status"));
            assertNotNull(response.get("timestamp"));
            assertEquals("UP", component(response, "mysql").get("status"));
            assertEquals("OK", component(response, "mysql").get("severity"));
            assertEquals("UP", component(response, "redis").get("status"));
            assertEquals("OK", component(response, "redis").get("severity"));
        }

        @Test
        @DisplayName("checkHealth should record a response time for every component")
        void checkHealth_shouldRecordResponseTimePerComponent() throws SQLException {
            stubJdbc(0);
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertTrue(component(response, "mysql").containsKey("responseTimeMs"));
            assertTrue(component(response, "redis").containsKey("responseTimeMs"));
        }

        @Test
        @DisplayName("checkHealth should report DOWN when MySQL cannot be reached")
        void checkHealth_shouldReportDownWhenMysqlUnreachable() throws SQLException {
            lenient().when(dataSource.getConnection()).thenThrow(new SQLException("connection refused"));
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DOWN", response.get("status"));
            assertEquals("DOWN", component(response, "mysql").get("status"));
            assertEquals("CRITICAL", component(response, "mysql").get("severity"));
            assertEquals("MySQL connection failed", component(response, "mysql").get("error"));
        }

        @Test
        @DisplayName("checkHealth should report DOWN when the health query yields no row")
        void checkHealth_shouldReportDownWhenHealthQueryReturnsNoRow() throws SQLException {
            lenient().when(dataSource.getConnection()).thenReturn(connection);
            lenient().when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
            lenient().when(resultSet.next()).thenReturn(false);
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DOWN", component(response, "mysql").get("status"));
            assertEquals("No result from health check query", component(response, "mysql").get("error"));
        }

        @Test
        @DisplayName("checkHealth should report DEGRADED when MySQL reports lock waits and slow queries")
        void checkHealth_shouldReportDegradedWhenAdvancedChecksFlagIssues() throws SQLException {
            stubJdbc(25);
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DEGRADED", response.get("status"));
            assertEquals("DEGRADED", component(response, "mysql").get("status"));
            assertEquals("WARNING", component(response, "mysql").get("severity"));
        }
    }

    @Nested
    @DisplayName("Redis health check")
    class RedisHealthTests {

        @Test
        @DisplayName("checkHealth should report DOWN when PING does not answer PONG")
        void checkHealth_shouldReportDownWhenRedisPingFails() throws SQLException {
            stubJdbc(0);
            stubRedisPong("nope");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DOWN", response.get("status"));
            assertEquals("DOWN", component(response, "redis").get("status"));
            assertEquals("CRITICAL", component(response, "redis").get("severity"));
            assertEquals("Redis PING failed", component(response, "redis").get("error"));
        }

        @Test
        @DisplayName("checkHealth should report DOWN when the Redis call throws")
        void checkHealth_shouldReportDownWhenRedisThrows() throws SQLException {
            stubJdbc(0);
            lenient().when(redisTemplate.execute(any(RedisCallback.class)))
                    .thenThrow(new IllegalStateException("redis unavailable"));

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DOWN", response.get("status"));
            assertEquals("DOWN", component(response, "redis").get("status"));
            assertEquals("Redis connection failed", component(response, "redis").get("error"));
        }

        @Test
        @DisplayName("checkHealth should report UP and skip the check when Redis is not configured")
        void checkHealth_shouldSkipRedisWhenNotConfigured() throws SQLException {
            stubJdbc(0);
            HealthService serviceWithoutRedis = new HealthService(dataSource, null);

            Map<String, Object> response = serviceWithoutRedis.checkHealth();

            assertEquals("UP", response.get("status"));
            assertEquals("UP", component(response, "redis").get("status"));
            assertEquals("Redis not configured — skipped", component(response, "redis").get("message"));
            verify(redisTemplate, never()).execute(any(RedisCallback.class));
            serviceWithoutRedis.shutdown();
        }
    }

    @Nested
    @DisplayName("Lifecycle")
    class ShutdownTests {

        @Test
        @DisplayName("shutdown should stop the executor so no further checks are submitted")
        void shutdown_shouldStopTheExecutor() throws SQLException {
            stubJdbc(0);
            stubRedisPong("PONG");
            healthService.checkHealth();

            healthService.shutdown();

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DOWN", response.get("status"),
                    "with the executor stopped, no component check can complete");
            assertEquals("MySQL health check did not complete in time",
                    component(response, "mysql").get("error"));
        }

        @Test
        @DisplayName("shutdown should be safe to call twice")
        void shutdown_shouldBeIdempotent() {
            healthService.shutdown();

            assertDoesNotThrow(() -> healthService.shutdown());
        }
    }

    @Nested
    @DisplayName("Advanced MySQL diagnostics")
    class AdvancedDiagnosticsTests {

        private HealthService service;

        @AfterEach
        void shutdownService() {
            if (service != null) {
                service.shutdown();
            }
        }

        private HikariDataSource hikariWithActiveConnections(int active, Integer maxPoolSize) throws SQLException {
            HikariDataSource hikariDataSource = mock(HikariDataSource.class);
            lenient().when(hikariDataSource.getConnection()).thenReturn(connection);
            if (maxPoolSize == null) {
                lenient().when(hikariDataSource.getHikariPoolMXBean()).thenReturn(null);
            } else {
                HikariPoolMXBean poolMXBean = mock(HikariPoolMXBean.class);
                lenient().when(hikariDataSource.getHikariPoolMXBean()).thenReturn(poolMXBean);
                lenient().when(hikariDataSource.getMaximumPoolSize()).thenReturn(maxPoolSize);
                lenient().when(poolMXBean.getActiveConnections()).thenReturn(active);
            }
            lenient().when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
            lenient().when(resultSet.next()).thenReturn(true);
            lenient().when(resultSet.getInt(1)).thenReturn(0);
            return hikariDataSource;
        }

        @Test
        @DisplayName("checkHealth should flag MySQL DEGRADED when the pool is more than 80% exhausted")
        void checkHealth_shouldFlagDegradedWhenHikariPoolNearlyExhausted() throws SQLException {
            stubRedisPong("PONG");
            service = new HealthService(hikariWithActiveConnections(9, 10), redisTemplate);

            Map<String, Object> response = service.checkHealth();

            assertEquals("DEGRADED", component(response, "mysql").get("status"));
            assertEquals("WARNING", component(response, "mysql").get("severity"));
        }

        @Test
        @DisplayName("checkHealth should stay UP when the Hikari pool is comfortably below the threshold")
        void checkHealth_shouldStayUpWhenHikariPoolHasHeadroom() throws SQLException {
            stubRedisPong("PONG");
            service = new HealthService(hikariWithActiveConnections(2, 10), redisTemplate);

            Map<String, Object> response = service.checkHealth();

            assertEquals("UP", component(response, "mysql").get("status"));
        }

        @Test
        @DisplayName("checkHealth should stay UP when the Hikari pool exposes no MXBean")
        void checkHealth_shouldStayUpWhenHikariMxBeanUnavailable() throws SQLException {
            stubRedisPong("PONG");
            service = new HealthService(hikariWithActiveConnections(0, null), redisTemplate);

            Map<String, Object> response = service.checkHealth();

            assertEquals("UP", component(response, "mysql").get("status"));
        }

        @Test
        @DisplayName("checkHealth should swallow a failing diagnostic query rather than degrade MySQL")
        void checkHealth_shouldSwallowDiagnosticQueryFailure() throws SQLException {
            lenient().when(dataSource.getConnection()).thenReturn(connection);
            lenient().when(connection.prepareStatement("SELECT 1 as health_check")).thenReturn(preparedStatement);
            lenient().when(connection.prepareStatement(argThat(sql ->
                    sql != null && sql.contains("INFORMATION_SCHEMA.PROCESSLIST"))))
                    .thenThrow(new SQLException("diagnostics denied"));
            lenient().when(preparedStatement.executeQuery()).thenReturn(resultSet);
            lenient().when(resultSet.next()).thenReturn(true);
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("UP", component(response, "mysql").get("status"),
                    "a failed diagnostic query is swallowed and must not degrade the component");
        }

        @Test
        @DisplayName("checkHealth should mark MySQL DOWN when its check does not finish in time")
        void checkHealth_shouldMarkComponentDownWhenCheckTimesOut() throws SQLException {
            lenient().when(dataSource.getConnection()).thenAnswer(invocation -> {
                Thread.sleep(6_000);
                return connection;
            });
            stubRedisPong("PONG");

            Map<String, Object> response = healthService.checkHealth();

            assertEquals("DOWN", response.get("status"));
            assertEquals("DOWN", component(response, "mysql").get("status"));
            assertEquals("CRITICAL", component(response, "mysql").get("severity"));
            assertEquals("MySQL health check did not complete in time",
                    component(response, "mysql").get("error"));
        }

        @Test
        @DisplayName("the advanced diagnostics are throttled so a second poll reuses the cached verdict")
        void checkHealth_shouldThrottleAdvancedDiagnostics() throws SQLException {
            stubJdbc(25);
            stubRedisPong("PONG");

            healthService.checkHealth();
            Map<String, Object> second = healthService.checkHealth();

            assertEquals("DEGRADED", component(second, "mysql").get("status"));
            // the health query itself runs on every poll; the diagnostics behind it do not
            verify(connection, never()).prepareStatement(argThat(sql ->
                    sql != null && sql.contains("state = 'Waiting for row lock'")
                            && sql.contains("__never__")));
        }
    }

    @Test
    @DisplayName("checkHealth should not throw when the executor rejects the submitted checks")
    void checkHealth_shouldNotThrowWhenExecutorRejects() {
        healthService.shutdown();

        assertDoesNotThrow(() -> healthService.checkHealth(),
                "a rejected submission must not surface as a " + RejectedExecutionException.class.getSimpleName());
    }
}
