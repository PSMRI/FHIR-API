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
package com.wipro.fhir;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;

/**
 * A loopback HTTP server for the services that build their own {@link org.springframework.web.client.RestTemplate}
 * and therefore cannot be handed a mock. Register a canned answer per path, point the
 * service's URL property at {@link #url(String)}, and read back what the service sent.
 */
public final class LocalHttpStub implements AutoCloseable {

    /** One canned answer: the status to return and the body to write. */
    public record Response(int status, String body) {

        public static Response ok(String body) {
            return new Response(200, body);
        }

        public static Response of(int status, String body) {
            return new Response(status, body);
        }
    }

    /** What one caller actually sent: its method, body and headers. */
    public record Request(String method, String body, Headers headers) {

        public String header(String name) {
            return headers.getFirst(name);
        }
    }

    private final HttpServer server;
    private final Map<String, Response> responses = new ConcurrentHashMap<>();
    private final Map<String, List<Request>> received = new ConcurrentHashMap<>();

    public LocalHttpStub() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String body;
            try (InputStream in = exchange.getRequestBody()) {
                body = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
            received.computeIfAbsent(path, key -> new ArrayList<>())
                    .add(new Request(exchange.getRequestMethod(), body, exchange.getRequestHeaders()));

            Response response = responses.getOrDefault(path, Response.of(404, "{\"error\":\"no stub for " + path + "\"}"));
            byte[] payload = response.body().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(response.status(), payload.length);
            exchange.getResponseBody().write(payload);
            exchange.close();
        });
        server.start();
    }

    /** Registers the answer this stub returns for {@code path}. */
    public LocalHttpStub stub(String path, Response response) {
        responses.put(path, response);
        return this;
    }

    /** Registers a 200 answer carrying {@code body}. */
    public LocalHttpStub stubOk(String path, String body) {
        return stub(path, Response.ok(body));
    }

    /** The absolute URL of {@code path} on this stub. */
    public String url(String path) {
        return "http://127.0.0.1:" + server.getAddress().getPort() + path;
    }

    /** Every request this stub saw on {@code path}, in arrival order. */
    public List<Request> requests(String path) {
        return received.getOrDefault(path, List.of());
    }

    /** The single request this stub saw on {@code path}. */
    public Request request(String path) {
        List<Request> all = requests(path);
        if (all.size() != 1) {
            throw new IllegalStateException("expected exactly one request on " + path + " but saw " + all.size());
        }
        return all.get(0);
    }

    /** A URL on a closed port, for driving the connection-failure branches. */
    public static String unreachableUrl() {
        return "http://127.0.0.1:1/unreachable";
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
