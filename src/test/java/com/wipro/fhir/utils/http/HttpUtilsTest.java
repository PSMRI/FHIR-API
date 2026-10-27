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
package com.wipro.fhir.utils.http;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HttpUtils Test Suite")
class HttpUtilsTest {

    private static final String URI = "http://localhost:8080/api/resource";

    @Mock
    private RestTemplate restTemplate;

    private HttpUtils httpUtils;

    @BeforeEach
    @DisplayName("Replace the internal RestTemplate with a mock before each test")
    void setUp() {
        httpUtils = new HttpUtils();
        ReflectionTestUtils.setField(httpUtils, "rest", restTemplate);
    }

    @SuppressWarnings("unchecked")
    private ArgumentCaptor<HttpEntity<String>> captureRequest(HttpMethod method, ResponseEntity<String> reply) {
        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        when(restTemplate.exchange(eq(URI), eq(method), captor.capture(), eq(String.class))).thenReturn(reply);
        return captor;
    }

    @Nested
    @DisplayName("GET requests")
    class GetTests

    {
        @Test
        @DisplayName("get should return the response body and record the status")
        void get_shouldReturnBodyAndRecordStatus() {
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(new ResponseEntity<>("{\"ok\":true}", HttpStatus.OK));

            assertEquals("{\"ok\":true}", httpUtils.get(URI));
            assertEquals(HttpStatus.OK, httpUtils.getStatus());
        }

        @Test
        @DisplayName("get should send the default JSON content type")
        void get_shouldSendDefaultJsonContentType() {
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.GET, new ResponseEntity<>("body", HttpStatus.OK));

            httpUtils.get(URI);

            assertEquals("application/json",
                    captor.getValue().getHeaders().getFirst("Content-Type"));
        }

        @Test
        @DisplayName("get should record a non-OK status returned by the server")
        void get_shouldRecordNonOkStatus() {
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

            assertNull(httpUtils.get(URI));
            assertEquals(HttpStatus.NOT_FOUND, httpUtils.getStatus());
        }

        @Test
        @DisplayName("get with headers should forward the supplied Authorization header")
        void get_shouldForwardSuppliedAuthorizationHeader() {
            HashMap<String, Object> header = new HashMap<>();
            header.put(HttpHeaders.AUTHORIZATION, "session-key-123");
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.GET, new ResponseEntity<>("body", HttpStatus.OK));

            assertEquals("body", httpUtils.get(URI, header));
            assertEquals("session-key-123",
                    captor.getValue().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        }

        @Test
        @DisplayName("get with headers should forward an explicit Content-Type")
        void get_shouldForwardExplicitContentType() {
            HashMap<String, Object> header = new HashMap<>();
            header.put(HttpHeaders.CONTENT_TYPE, "application/xml");
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.GET, new ResponseEntity<>("body", HttpStatus.OK));

            httpUtils.get(URI, header);

            assertEquals("application/xml",
                    captor.getValue().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        }

        @Test
        @DisplayName("get with headers should default the Content-Type to JSON when none is supplied")
        void get_shouldDefaultContentTypeToJson() {
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.GET, new ResponseEntity<>("body", HttpStatus.OK));

            httpUtils.get(URI, new HashMap<>());

            assertEquals("application/json",
                    captor.getValue().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        }

        @Test
        @DisplayName("get should propagate a transport failure to the caller")
        void get_shouldPropagateTransportFailure() {
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                    .thenThrow(new RestClientException("connection refused"));

            assertThrows(RestClientException.class, () -> httpUtils.get(URI));
        }
    }

    @Nested
    @DisplayName("POST requests")
    class PostTests {

        @Test
        @DisplayName("post should send the JSON payload and return the response body")
        void post_shouldSendPayloadAndReturnBody() {
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.POST, new ResponseEntity<>("created", HttpStatus.CREATED));

            assertEquals("created", httpUtils.post(URI, "{\"count\":5}"));
            assertEquals("{\"count\":5}", captor.getValue().getBody());
            assertEquals(HttpStatus.CREATED, httpUtils.getStatus());
        }

        @Test
        @DisplayName("post with headers should forward the supplied Authorization header")
        void post_shouldForwardSuppliedAuthorizationHeader() {
            HashMap<String, Object> header = new HashMap<>();
            header.put(HttpHeaders.AUTHORIZATION, "session-key-123");
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.POST, new ResponseEntity<>("created", HttpStatus.CREATED));

            assertEquals("created", httpUtils.post(URI, "{\"count\":5}", header));
            assertEquals("session-key-123",
                    captor.getValue().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            assertEquals("{\"count\":5}", captor.getValue().getBody());
        }

        @Test
        @DisplayName("post with headers should omit the Authorization header when none is supplied")
        void post_shouldOmitAuthorizationHeaderWhenNoneSupplied() {
            ArgumentCaptor<HttpEntity<String>> captor =
                    captureRequest(HttpMethod.POST, new ResponseEntity<>("created", HttpStatus.CREATED));

            httpUtils.post(URI, "{}", new HashMap<>());

            assertNull(captor.getValue().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        }

        @Test
        @DisplayName("post should record a server error status")
        void post_shouldRecordServerErrorStatus() {
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(new ResponseEntity<>("boom", HttpStatus.INTERNAL_SERVER_ERROR));

            httpUtils.post(URI, "{}");

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, httpUtils.getStatus());
        }

        @Test
        @DisplayName("post should issue the request against the supplied URI with the POST method")
        void post_shouldIssueRequestWithPostMethod() {
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                    .thenReturn(new ResponseEntity<>("created", HttpStatus.CREATED));

            httpUtils.post(URI, "{}");

            verify(restTemplate).exchange(eq(URI), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
        }
    }

    @Nested
    @DisplayName("Status tracking")
    class StatusTests {

        @Test
        @DisplayName("getStatus should be null until a request has been made")
        void getStatus_shouldBeNullBeforeAnyRequest() {
            assertNull(httpUtils.getStatus());
        }

        @Test
        @DisplayName("setStatus should record the supplied status code")
        void setStatus_shouldRecordSuppliedStatusCode() {
            httpUtils.setStatus(HttpStatus.ACCEPTED);

            assertEquals(HttpStatus.ACCEPTED, httpUtils.getStatus());
        }
    }

    @Nested
    @DisplayName("The overloads that carry explicit headers")
    class HeaderCarryingOverloadTests {

        private HttpHeaders headers() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "session-key-123");
            return headers;
        }

        @Test
        @DisplayName("getPatientDataFromFeed should GET with the caller's headers and record the status")
        void getPatientDataFromFeed_shouldGetWithCallerHeaders() {
            ArgumentCaptor<HttpEntity<String>> captor = captureRequest(HttpMethod.GET,
                    new ResponseEntity<>("feed-payload", HttpStatus.OK));

            assertEquals("feed-payload", httpUtils.getPatientDataFromFeed(URI, headers()));

            assertEquals("session-key-123", captor.getValue().getHeaders().getFirst("Authorization"));
            assertEquals(HttpStatus.OK, httpUtils.getStatus());
        }

        @Test
        @DisplayName("get with a header map should forward the authorization and content type it was given")
        void getWithHeaderMap_shouldForwardGivenHeaders() {
            HashMap<String, Object> header = new HashMap<>();
            header.put(HttpHeaders.AUTHORIZATION, "session-key-123");
            header.put(HttpHeaders.CONTENT_TYPE, "application/json");
            ArgumentCaptor<HttpEntity<String>> captor = captureRequest(HttpMethod.GET,
                    new ResponseEntity<>("payload", HttpStatus.OK));

            assertEquals("payload", httpUtils.get(URI, header));

            assertEquals("session-key-123", captor.getValue().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            assertEquals("application/json", captor.getValue().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        }

        @Test
        @DisplayName("get with a header map should default the content type to JSON")
        void getWithHeaderMap_shouldDefaultContentType() {
            ArgumentCaptor<HttpEntity<String>> captor = captureRequest(HttpMethod.GET,
                    new ResponseEntity<>("payload", HttpStatus.OK));

            httpUtils.get(URI, new HashMap<>());

            assertEquals("application/json", captor.getValue().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        }

        @Test
        @DisplayName("post with a header map should forward only the authorization it was given")
        void postWithHeaderMap_shouldForwardAuthorization() {
            HashMap<String, Object> header = new HashMap<>();
            header.put(HttpHeaders.AUTHORIZATION, "session-key-123");
            ArgumentCaptor<HttpEntity<String>> captor = captureRequest(HttpMethod.POST,
                    new ResponseEntity<>("created", HttpStatus.OK));

            assertEquals("created", httpUtils.post(URI, "{}", header));

            assertEquals("session-key-123", captor.getValue().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            assertEquals("{}", captor.getValue().getBody());
        }

        @Test
        @DisplayName("post with explicit headers should send the body under those headers")
        void postWithHeaders_shouldSendBodyUnderHeaders() {
            ArgumentCaptor<HttpEntity<String>> captor = captureRequest(HttpMethod.POST,
                    new ResponseEntity<>("created", HttpStatus.OK));

            assertEquals("created", httpUtils.post(URI, "{}", headers()));

            assertEquals("session-key-123", captor.getValue().getHeaders().getFirst("Authorization"));
        }

        @Test
        @DisplayName("postWithResponseEntity should hand the whole response back to the caller")
        void postWithResponseEntity_shouldReturnWholeResponse() {
            captureRequest(HttpMethod.POST, new ResponseEntity<>("", HttpStatus.ACCEPTED));

            ResponseEntity<String> response = httpUtils.postWithResponseEntity(URI, "{}", headers());

            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
            assertEquals(HttpStatus.ACCEPTED, httpUtils.getStatus());
        }

        @Test
        @DisplayName("getWithResponseEntity should hand the whole response back to the caller")
        void getWithResponseEntity_shouldReturnWholeResponse() {
            captureRequest(HttpMethod.GET, new ResponseEntity<>("payload", HttpStatus.OK));

            ResponseEntity<String> response = httpUtils.getWithResponseEntity(URI, headers());

            assertEquals("payload", response.getBody());
            assertEquals(HttpStatus.OK, httpUtils.getStatus());
        }

        @Test
        @DisplayName("getWithResponseEntityByte should hand the raw bytes back to the caller")
        void getWithResponseEntityByte_shouldReturnRawBytes() {
            byte[] png = new byte[] { 1, 2, 3 };
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class)))
                    .thenReturn(new ResponseEntity<>(png, HttpStatus.OK));

            assertEquals(png, httpUtils.getWithResponseEntityByte(URI, headers()).getBody());
            assertEquals(HttpStatus.OK, httpUtils.getStatus());
        }

        @Test
        @DisplayName("postStatusCode should answer with the status alone")
        void postStatusCode_shouldAnswerWithStatusAlone() {
            captureRequest(HttpMethod.POST, new ResponseEntity<>("", HttpStatus.ACCEPTED));

            assertEquals("202 ACCEPTED", httpUtils.postStatusCode(URI, "{}", headers()));
        }

        @Test
        @DisplayName("postWithResponseEntity should also accept a prepared request entity")
        void postWithResponseEntity_shouldAcceptPreparedEntity() {
            when(restTemplate.exchange(eq(URI), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class),
                    new Object[0])).thenReturn(new ResponseEntity<>("created", HttpStatus.OK));

            ResponseEntity<String> response = httpUtils.postWithResponseEntity(URI,
                    new HttpEntity<>("{}", headers()), String.class);

            assertEquals("created", response.getBody());
        }
    }
}
