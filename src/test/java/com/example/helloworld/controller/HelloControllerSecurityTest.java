package com.example.helloworld.controller;

import com.example.helloworld.model.HelloResponse;
import com.example.helloworld.service.HelloService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests verifying security behaviour of the HelloController.
 *
 * <p>YSJP-213: Ensure tests cover input validation and secure defaults.
 * <p>YSJP-214: Validate security headers, no stack-trace leakage.
 * <p>YSJP-221: Error responses do not leak stack traces or internal details.
 */
@WebMvcTest(HelloController.class)
class HelloControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService helloService;

    @Test
    @DisplayName("GET /hello response does not contain stack trace")
    void hello_noStackTraceInResponse() throws Exception {
        when(helloService.getHello()).thenReturn(new HelloResponse("Hello World"));

        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("exception"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("trace"))));
    }

    @Test
    @DisplayName("GET /hello returns X-Content-Type-Options: nosniff header")
    void hello_xContentTypeOptionsHeader() throws Exception {
        when(helloService.getHello()).thenReturn(new HelloResponse("Hello World"));

        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    @DisplayName("GET /hello returns X-Frame-Options: DENY header")
    void hello_xFrameOptionsHeader() throws Exception {
        when(helloService.getHello()).thenReturn(new HelloResponse("Hello World"));

        mockMvc.perform(get("/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    @DisplayName("Unknown endpoints return 403 (deny all by default)")
    void unknownEndpoint_returnsForbidden() throws Exception {
        mockMvc.perform(get("/unknown"))
                .andExpect(status().isForbidden());
    }
}
