// src/test/java/com/openclassrooms/starterjwt/security/WebSecurityConfigIT.java
package com.openclassrooms.starterjwt.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class WebSecurityConfigIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAccessPublicAuthEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // ou autre selon ton contrôleur
    }

    @Test
    void whenAccessProtectedEndpointWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}