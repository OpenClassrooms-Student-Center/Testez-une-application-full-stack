package com.openclassrooms.starterjwt.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;

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

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

/**
 * @implNote This class uses Mockito for mocking dependencies and performs tests
 *           using the Spring MVC Test framework.
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link UserController} in handling teacher-related operations.
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
@DisplayName("User controller test: api/user")
public class UserControllerTest {
    /**
     * The logger instance for logging test-related information.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * The controller under test, injected with mock dependencies.
     */
    @InjectMocks
    private UserController userController;

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
     * The starting time for test suites to calculate the duration of tests.
     */
    static private Instant startedAt;

    /**
     * Initializes the starting time before all test suites are executed.
     */
    @BeforeAll
    static public void initStartingTime() {
        startedAt = Instant.now();
    }

    /**
     * Displays the duration of all test suites after they have been executed.
     */
    @AfterAll
    static public void showTestDuration() {
        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();
        logger.info(MessageFormat.format("Duration of the tests : {0} ms", duration));
    }

    public UserControllerTest(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * Test case for retrieving a user with the given existing ID.
     * It should return a 200 status code.
     */
    @Test
    @Tag("get_api/user/{id}")
    @DisplayName("(HAPPY PATH) it should get the user from the database of the given id")
    public void getUserById_shouldReturnUserWithGivenId() {
        try {
            // Arrange

            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for retrieving a user with an invalid ID.
     * It should return a 400 error status code.
     */
    @Test
    @Tag("get_api/user/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 error for invalid id")
    public void getUserWithInvalidId_shouldReturnNotFoundError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/user/invalid")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for deleting a user with a valid ID.
     * It should return a 200 status code upon successful deletion.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(HAPPY PATH) it should delete the user and return a 200 status code")
    public void deleteUserWithValidId_shouldReturnOk() {
        try {
            // Arrange

            // Act
            ResultActions result = mockMvc.perform(
                    delete("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for deleting a user with an unauthorized user.
     * It should return a 401 status code.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(HAPPY PATH) it should return 401 status code for unauthorized user")
    public void deleteUserWithUnauthorizedUser_shouldReturnUnauthorized() {
        try {
            // Arrange

            // Act
            ResultActions result = mockMvc.perform(
                    delete("/api/user/1")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for deleting a user with an invalid ID.
     * It should return a 404 error status code.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 error for invalid id")
    public void deleteUserWithInvalidId_shouldReturnNotFoundError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    delete("/api/user/invalid")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
