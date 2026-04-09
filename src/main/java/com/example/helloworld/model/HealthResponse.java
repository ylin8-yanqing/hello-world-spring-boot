package com.example.helloworld.model;

/**
 * Response model for the GET /health endpoint.
 *
 * @param status the health status (e.g. "UP")
 */
public record HealthResponse(String status) {
}
