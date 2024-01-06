package com.openclassrooms.starterjwt.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

/**
 * This class contains test cases for the {@link AuthController} class, focusing
 * on API endpoints related to user authentication and registration.
 *
 * @implNote This class uses Mockito for mocking dependencies and performs tests
 *           using the Spring MVC Test framework.
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link AuthController} in handling teacher-related operations.
 * @implNote The class utilizes JUnit 5, Mockito, and Spring's MockMvc for
 *           testing.
 *
 * @see MockMvc
 * @see UserRepository
 * @see JwtUtils
 * @see PasswordEncoder
 * @see WebApplicationContext
 *
 * @author Younes LAHOUITI
 * @version 1.0
 * @since 2024-01-05
 */
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Auth controller test: api/auth")
class AuthControllerTest {
    /**
     * The logger instance for logging test-related information.
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * The controller under test, injected with mock dependencies.
     */
    @InjectMocks
    private AuthController authController;

    /**
     * Mocked authentication manager for simulating user authentication.
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Mocked user repository for handling user data during tests.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked JWT utility class for working with JSON Web Tokens.
     */
    @Mock
    private JwtUtils jwtUtils;

    /**
     * Mocked password encoder for encoding and decoding passwords.
     */
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * The MockMvc instance for simulating HTTP requests and responses.
     */
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    /**
     * Test case for simulating the successful authentication of a valid user,
     * expecting a JWT response.
     *
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    @Tag("post_api/auth/login")
    @DisplayName("(HAPPY PATH) it should authenticate the user successfully and return a JWT")
    void authenticateValidUser_shouldReturnJwtResponse() throws Exception {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .firstName("Toto")
                .lastName("Toto")
                .username("toto3@toto.com")
                .password("test!1234")
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null);

        given(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        userDetails.getPassword())))
                .willReturn(authentication);

        given(authentication.getPrincipal()).willReturn(userDetails);

        given(jwtUtils.generateJwtToken(authentication)).willReturn("jwt");

        given(userRepository.findByEmail(userDetails.getUsername()))
                .willReturn(Optional.of(new User(userDetails.getUsername(), userDetails.getLastName(),
                        userDetails.getFirstName(), userDetails.getPassword(), false)));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("toto3@toto.com");
        loginRequest.setPassword("test!1234");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)));

        // Assert
        result.andExpect(status().isOk());
    }

    /**
     * Test case for simulating an invalid user authentication, expecting a 400
     * status code.
     *
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    @Tag("post_api/auth/login")
    @DisplayName("(EDGE CASE) it should return a 400 status code")
    void authenticateInvalidUser_shouldReturnError() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalid");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    /**
     * Test case for simulating the successful registration of a valid user,
     * expecting a success message response.
     *
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    @Tag("post_api/auth/register")
    @DisplayName("(HAPPY PATH) it should register the user")
    void registerValidUser_shouldReturnMessageResponse() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setEmail("toto3@toto.com");
        signupRequest.setLastName("Toto");
        signupRequest.setFirstName("Toto");
        signupRequest.setPassword("test!1234");

        // given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);
        String jsonRequest = new ObjectMapper().writeValueAsString(signupRequest);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    /**
     * Test case for simulating the registration of an invalid user, expecting a 400
     * status code.
     *
     * @throws Exception if there's an error during the test execution.
     */
    @Test
    @Tag("post_api/auth/register")
    @DisplayName("(EDGE CASE) it should not register the user")
    void registerInvalidUser_shouldReturnErrorResponse() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setFirstName("Toto");
        signupRequest.setLastName("Toto");
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setPassword("test!1234");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signupRequest)));

        // Assert
        result.andExpect(status().isBadRequest());
    }
}
