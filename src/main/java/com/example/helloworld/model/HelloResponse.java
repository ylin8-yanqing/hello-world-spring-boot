package com.example.helloworld.model;

/**
 * Response model for the GET /hello endpoint.
 *
 * <p>YSJP-221: Response body contains exactly {"message":"Hello World"}
 *
 * @param message the greeting message
 */
public record HelloResponse(String message) {
}
