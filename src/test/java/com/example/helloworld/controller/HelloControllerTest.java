package com.example.helloworld.controller;

import com.example.helloworld.config.SecurityConfig;
import com.example.helloworld.model.HelloResponse;
import com.example.helloworld.service.HelloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link HelloController}.
 *
 * <p>YSJP-222: Unit test verifies controller behavior for /hello.
 * <p>YSJP-222: Tests are deterministic and do not require external services.
 *
 * <p>Uses Spring MVC slice (@WebMvcTest) to test the controller in isolation.
 * SecurityConfig is imported so that permitAll() on /hello is applied correctly.
 */
@WebMvcTest(HelloController.class)
@Import(SecurityConfig.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService helloService;

    @Test
    @DisplayName("GET /hello returns HTTP 200 with JSON message 'Hello World'")
    void hello_returnsOkWithHelloWorldMessage() throws Exception {
        // Given
        when(helloService.getHello()).thenReturn(new HelloResponse("Hello World"));

        // When / Then
        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Hello World"));
    }

    @Test
    @DisplayName("GET /hello Content-Type is application/json")
    void hello_returnsApplicationJsonContentType() throws Exception {
        when(helloService.getHello()).thenReturn(new HelloResponse("Hello World"));

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /hello response body contains exactly the 'message' field")
    void hello_responseBodyContainsMessageField() throws Exception {
        when(helloService.getHello()).thenReturn(new HelloResponse("Hello World"));

        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message").value("Hello World"));
    }
}
