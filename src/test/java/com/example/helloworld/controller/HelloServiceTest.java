package com.example.helloworld.controller;

import com.example.helloworld.model.HelloResponse;
import com.example.helloworld.service.HelloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link HelloService}.
 *
 * <p>YSJP-222: Unit test verifies service behavior for /hello.
 * <p>YSJP-222: Tests are deterministic and do not require external services.
 *
 * <p>Plain unit test – no Spring context loaded.
 */
class HelloServiceTest {

    private HelloService helloService;

    @BeforeEach
    void setUp() {
        helloService = new HelloService();
    }

    @Test
    @DisplayName("getHello() returns a HelloResponse with message 'Hello World'")
    void getHello_returnsHelloWorldMessage() {
        HelloResponse response = helloService.getHello();

        assertThat(response).isNotNull();
        assertThat(response.message()).isEqualTo("Hello World");
    }

    @Test
    @DisplayName("getHello() returns a consistent message on repeated calls")
    void getHello_isIdempotent() {
        HelloResponse first = helloService.getHello();
        HelloResponse second = helloService.getHello();

        assertThat(first.message()).isEqualTo(second.message());
    }
}
