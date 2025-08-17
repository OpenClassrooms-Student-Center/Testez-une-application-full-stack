// src/test/java/com/openclassrooms/starterjwt/security/jwt/AuthTokenFilterIT.java
package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class AuthTokenFilterIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenNoToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/protected-endpoint") // Remplace par un endpoint protégé réel
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenInvalidToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/protected-endpoint")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}