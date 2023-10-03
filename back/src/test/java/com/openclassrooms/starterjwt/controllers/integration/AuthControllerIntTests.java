package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Test to register and login a new user
    public void testLoginWithValidCredentials() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        // Prepare a login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("newuser@example.com");
        loginRequest.setPassword("password");
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);

        // Perform the login request
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }

}
