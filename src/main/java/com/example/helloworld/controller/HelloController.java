package com.example.helloworld.controller;

import com.example.helloworld.model.HelloResponse;
import com.example.helloworld.service.HelloService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing the GET /hello endpoint.
 *
 * <p>YSJP-221: Endpoint contract:
 * <ul>
 *   <li>Method: GET</li>
 *   <li>Path: /hello</li>
 *   <li>Success: HTTP 200, Content-Type: application/json, Body: {"message":"Hello World"}</li>
 * </ul>
 *
 * <p>Security notes (YSJP-221, HLSA §6):
 * <ul>
 *   <li>No stack traces are leaked in error responses (configured via application.properties)</li>
 *   <li>No unsafe deserialization – uses Java records with Jackson</li>
 *   <li>Input validation pattern applied (no inputs on this endpoint)</li>
 * </ul>
 */
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    /**
     * Returns a Hello World greeting.
     *
     * @return HTTP 200 with JSON body {@code {"message":"Hello World"}}
     */
    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HelloResponse> hello() {
        return ResponseEntity.ok(helloService.getHello());
    }
}
