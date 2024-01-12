package com.openclassrooms.starterjwt.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTests {

    /**
     * The main entry point for integration tests of Spring MVC applications.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Jackson ObjectMapper for converting Java objects to JSON and vice versa.
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObjectMapper() throws Exception {
        mockMvc.perform(get("/api/session"));
    }
}
