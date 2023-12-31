package com.openclassrooms.starterjwt.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration.WebFluxConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    AuthControllerTest() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void authenticateUser_shouldReturnJwtResponse() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("testPassword");

        // OngoingStubbing<Authentication> authentication =
        // // Mocks the authenticationManager.authenticate() method
        when(authenticationManager.authenticate(any())).thenAnswer(invocation -> {
            return mockAuthentication();
        });

        // Mock the jwtUtils.generateJwtToken() method
        when(jwtUtils.generateJwtToken(any())).thenReturn("mockedJwtToken");

        // Mock the userRepository.findByEmail() method
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User(/*
                                                                                 * provide necessary user
                                                                                 * details
                                                                                 */)));

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        // Assert
        result.andExpect(status().isOk());
        // .andExpect(/*
        // */);
    }

    @Test
    void registerUser_shouldReturnMessageResponse() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

        // Mock the userRepository.existsByEmail() method
        when(userRepository.existsByEmail(any())).thenReturn(false);

        // Mock the passwordEncoder.encode() method
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        // Assert
        result.andExpect(status().isOk());
    }

    // Define a method to create a mock Authentication object
    private Authentication mockAuthentication() {
        // Create and return a mock Authentication object as needed for your test
        // Example using Mockito:
        return Mockito.mock(Authentication.class);
    }
}
