package com.openclassrooms.starterjwt.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Auth controller test: api/auth")
class AuthControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

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

    static private Instant startedAt;

    @BeforeAll
    static public void initStartingTime() {
        logger.info("Before all the test suites");

        startedAt = Instant.now();
    }

    @AfterAll
    static public void showTestDuration() {
        logger.info("After all the test suites");

        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();

        logger.info(MessageFormat.format("Durée des tests : {0} ms", duration));
    }

    public AuthControllerTest() {
        // this.webApplicationContext = mock(WebApplicationContext.class);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Tag("post_api/auth/login")
    @DisplayName("(HAPPY PATH) it should authenticate the user successfully and return a JWT")
    void authenticateUser_shouldReturnJwtResponse() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("testPassword");

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
    @Tag("post_api/auth/register")
    @DisplayName("(HAPPY PATH) it should register the user")
    void registerUser_shouldReturnMessageResponse() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

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
