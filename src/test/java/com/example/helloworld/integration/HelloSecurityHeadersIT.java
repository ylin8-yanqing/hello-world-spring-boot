package com.example.helloworld.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests validating security response headers on GET /hello.
 *
 * <p>YSJP-214: Apply baseline security best practices – validate security headers.
 * <p>YSJP-217: Integration/system tests validate HTTP status, JSON payload, and security headers.
 * <p>HLSA §6: Transport security – set security headers where applicable.
 *
 * <p>Naming convention *IT.java → picked up by maven-failsafe-plugin (mvn verify).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloSecurityHeadersIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String helloUrl() {
        return "http://localhost:" + port + "/hello";
    }

    @Test
    @DisplayName("GET /hello returns X-Content-Type-Options: nosniff")
    void hello_hasXContentTypeOptionsHeader() {
        ResponseEntity<String> response = restTemplate.getForEntity(helloUrl(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst("X-Content-Type-Options"))
                .isEqualTo("nosniff");
    }

    @Test
    @DisplayName("GET /hello returns X-Frame-Options: DENY")
    void hello_hasXFrameOptionsHeader() {
        ResponseEntity<String> response = restTemplate.getForEntity(helloUrl(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst("X-Frame-Options"))
                .isEqualTo("DENY");
    }

    @Test
    @DisplayName("GET /hello returns Content-Security-Policy header")
    void hello_hasContentSecurityPolicyHeader() {
        ResponseEntity<String> response = restTemplate.getForEntity(helloUrl(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst("Content-Security-Policy"))
                .isNotBlank();
    }

    @Test
    @DisplayName("GET /hello does not expose server internals in headers")
    void hello_doesNotExposeServerHeader() {
        ResponseEntity<String> response = restTemplate.getForEntity(helloUrl(), String.class);

        // Should not expose internal server details
        assertThat(response.getHeaders().getFirst("Server")).isNullOrEmpty();
    }

    @Test
    @DisplayName("GET /hello returns Referrer-Policy header")
    void hello_hasReferrerPolicyHeader() {
        ResponseEntity<String> response = restTemplate.getForEntity(helloUrl(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst("Referrer-Policy"))
                .isNotBlank();
    }
}
