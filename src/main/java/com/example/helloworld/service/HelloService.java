package com.example.helloworld.service;

import com.example.helloworld.model.HelloResponse;
import org.springframework.stereotype.Service;

/**
 * Service layer for the Hello World endpoint.
 *
 * <p>YSJP-221: Returns a stable HelloResponse with message "Hello World".
 * <p>YSJP-222: Decoupled from controller to allow isolated unit testing.
 */
@Service
public class HelloService {

    private static final String HELLO_MESSAGE = "Hello World";

    /**
     * Returns the Hello World greeting response.
     *
     * @return a {@link HelloResponse} with the standard greeting message
     */
    public HelloResponse getHello() {
        return new HelloResponse(HELLO_MESSAGE);
    }
}
