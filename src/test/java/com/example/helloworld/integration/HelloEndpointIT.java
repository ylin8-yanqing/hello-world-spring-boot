package com.example.helloworld.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration / system test for the GET /hello endpoint.
 *
 * <p>YSJP-222: Integration/system test starts the full app and calls /hello over HTTP,
 * validating status, content-type, and payload.
 * <p>YSJP-222: Tests are deterministic and do not require external services.
 *
 * <p>Starts the full Spring Boot application on a random port (RANDOM_PORT).
 * Uses {@link TestRestTemplate} to make real HTTP calls against the running server.
 *
 * <p>Naming convention *IT.java → picked up by maven-failsafe-plugin (mvn verify).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloEndpointIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    @DisplayName("GET /hello returns HTTP 200")
    void hello_returnsHttp200() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl() + "/hello", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET /hello Content-Type is application/json")
    void hello_returnsApplicationJson() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl() + "/hello", String.class);

        assertThat(response.getHeaders().getContentType())
                .isNotNull()
                .satisfies(ct -> assertThat(ct.isCompatibleWith(MediaType.APPLICATION_JSON)).isTrue());
    }

    @Test
    @DisplayName("GET /hello body contains {\"message\":\"Hello World\"}")
    @SuppressWarnings("unchecked")
    void hello_bodyContainsHelloWorldMessage() {
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl() + "/hello", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("message")).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("GET /hello does not leak stack traces in response body")
    void hello_doesNotLeakStackTrace() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl() + "/hello", String.class);

        assertThat(response.getBody()).doesNotContain("exception", "stacktrace", "trace");
    }
}
