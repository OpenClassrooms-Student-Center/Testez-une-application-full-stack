package com.openclassrooms.starterjwt.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;

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
     * Mocked web application context for setting up the MockMvc instance.
     */
    @Mock
    private WebApplicationContext webApplicationContext;

    /**
     * The MockMvc instance for simulating HTTP requests and responses.
     */
    private MockMvc mockMvc;

    /**
     * ObjectMapper for converting objects to JSON and vice versa.
     */
    private ObjectMapper objectMapper;

    /**
     * The starting time for test suites to calculate the duration of tests.
     */
    static private Instant startedAt;

    /**
     * Constructor to set up the web application context and initialize objects.
     *
     * @param webApplicationContext The web application context to be set.
     */
    public AuthControllerTest(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Initializes the starting time before all test suites are executed.
     */
    @BeforeAll
    static public void initStartingTime() {
        logger.info("Before all the test suites");
        startedAt = Instant.now();
    }

    /**
     * Displays the duration of all test suites after they have been executed.
     */
    @AfterAll
    static public void showTestDuration() {
        logger.info("After all the test suites");
        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();
        logger.info(MessageFormat.format("Duration of the tests: {0} ms", duration));
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
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("toto3@toto.com");
        loginRequest.setPassword("test!1234");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

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
                .content(objectMapper.writeValueAsString(loginRequest)));

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

        signupRequest.setEmail("test@example.com");
        signupRequest.setLastName("Doe");
        signupRequest.setFirstName("John");
        signupRequest.setPassword("password");

        // Act
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

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
                .content(objectMapper.writeValueAsString(signupRequest)));

        // Assert
        result.andExpect(status().isBadRequest());
    }
}
