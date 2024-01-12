package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;

/**
 * This class contains test cases for the {@link AuthController} class,
 * focusing
 * on API endpoints related to user authentication and registration.
 *
 * @implNote This class uses Mockito for mocking dependencies and performs
 *           tests
 *           using the Spring MVC Test framework.
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link AuthController} in handling teacher-related operations.
 * @implNote The class utilizes JUnit 5 and Mockito for testing.
 *
 * @see UserRepository
 * @see JwtUtils
 * @see PasswordEncoder
 *
 * @author Younes LAHOUITI
 * @version 1.0
 * @since 2024-01-05
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Auth controller test: api/auth")
public class AuthControllerTest {
        /**
         * The controller under test, injected with mock dependencies.
         */
        @Mock
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
         * Set up the test environment before each test case by initializing the
         * {@link AuthController}.
         * This method creates a new instance of the {@code AuthController} with the
         * provided mock dependencies,
         * including an authentication manager, password encoder, JWT utility class, and
         * user repository.
         * The initialized controller is then used in each test case for simulating HTTP
         * requests and responses.
         */
        @BeforeEach
        void setUp() {
                authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
        }

        /**
         * Test case for simulating the successful authentication of a valid user,
         * expecting a JWT result.
         *
         * @throws Exception if there's an error during the test execution.
         */
        @Test
        @Tag("post_api/auth/login")
        @DisplayName("(HAPPY PATH) it should authenticate the user successfully and return a JWT")
        void authenticateValidUser_shouldReturnJwtResponse() throws Exception {
                // * Arrange
                UserDetailsImpl userDetails = UserDetailsImpl.builder()
                                .id(1L)
                                .firstName("Toto")
                                .lastName("Toto")
                                .username("toto3@toto.com")
                                .password("test!1234")
                                .build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null);

                when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), userDetails.getPassword())))
                                .thenReturn(authentication);
                when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");

                User mockedUser = new User(userDetails.getUsername(), userDetails.getLastName(),
                                userDetails.getFirstName(), userDetails.getPassword(), false);

                when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockedUser));

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(userDetails.getUsername());
                loginRequest.setPassword(userDetails.getPassword());

                // * Act
                ResponseEntity<?> result = authController.authenticateUser(loginRequest);

                // * Assert
                assertEquals(HttpStatus.OK, result.getStatusCode());
        }

        /**
         * Test case for simulating the successful registration of a valid user,
         * expecting a success message result.
         *
         * @throws Exception if there's an error during the test execution.
         */
        @Test
        @Tag("post_api/auth/register")
        @DisplayName("(HAPPY PATH) it should register the user")
        void registerValidUser_shouldReturnMessageResponse() throws Exception {
                // * Arrange
                SignupRequest signUpRequest = new SignupRequest();

                signUpRequest.setEmail("toto3@toto.com");
                signUpRequest.setLastName("Toto");
                signUpRequest.setFirstName("Toto");
                signUpRequest.setPassword("test!1234");

                when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

                when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

                when(userRepository.save(any(User.class))).thenReturn(new User());

                when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

                // * Act
                ResponseEntity<?> result = authController.registerUser(signUpRequest);

                // * Assert
                assertEquals(HttpStatus.OK, result.getStatusCode());
                assertEquals("User registered successfully!", ((MessageResponse) result.getBody()).getMessage());

        }

        /**
         * Test case for simulating the registration of an invalid user, expecting a
         * 400
         * status code.
         *
         * @throws Exception if there's an error during the test execution.
         */
        @Test
        @Tag("post_api/auth/register")
        @DisplayName("(EDGE CASE) it should not register the user because the sign up request's payload is invalid")
        void registerAlreadyRegisteredUser_shouldReturnErrorResponse() throws Exception {
                // * Arrange
                SignupRequest signUpRequest = new SignupRequest();

                signUpRequest.setEmail("yoga@studio.com");
                signUpRequest.setLastName("Admin");
                signUpRequest.setFirstName("Admin");
                signUpRequest.setPassword("test!1234");

                when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

                // * Act
                ResponseEntity<?> result = authController.registerUser(signUpRequest);

                // * Assert
                assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
                assertEquals("Error: Email is already taken!", ((MessageResponse) result.getBody()).getMessage());
        }

}