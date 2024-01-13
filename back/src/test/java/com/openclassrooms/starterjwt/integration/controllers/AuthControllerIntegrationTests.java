package com.openclassrooms.starterjwt.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test class for the {@link AuthController}, covering registration
 * and login functionality.
 * This class focuses on testing the actual behavior of the API endpoints by
 * interacting with the complete
 * Spring application context.
 *
 * @implNote This class uses Mockito for mocking dependencies, but it also
 *           involves the real
 *           Spring application context for a more comprehensive integration
 *           test.
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes
 *           for each endpoint.
 * @implSpec All tests focus on the functionality of the {@link AuthController}
 *           in handling user authentication
 *           and registration.
 * @implNote The class utilizes JUnit 5 and Mockito for testing, along with
 *           Spring's Test framework for
 *           integration testing.
 *
 * @see UserRepository
 * @see JwtUtils
 * @see PasswordEncoder
 * @see MockMvc
 *
 * @author Younes LAHOUITI
 * @version 1.0
 * @since 2024-01-05
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AuthControllerIntegrationTests {

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

        /**
         * MockBean for simulating the UserRepository in the integration tests.
         */
        @MockBean
        private UserRepository userRepository;

        /**
         * Mocked password encoder for encoding and decoding passwords.
         */
        @Mock
        private PasswordEncoder passwordEncoder;

        /**
         * MockBean for simulating the AuthenticationManager in the integration tests.
         */
        @MockBean
        private AuthenticationManager authenticationManager;

        /**
         * Integration test for registering and logging in with valid credentials.
         *
         * @throws Exception if an error occurs during the test execution.
         */
        @Test
        @Tag("post_api/auth/register---post_api/auth/login")
        @DisplayName("(HAPPY PATH) it should login the newly registered")
        // Test to register and login a new user
        public void testRegisterAndLoginWithValidCredentials() throws Exception {
                // * Arrange
                String email = "test@test.com";
                String password = "test!1234";

                SignupRequest signUpRequest = new SignupRequest();

                signUpRequest.setEmail(email);
                signUpRequest.setLastName("Tester");
                signUpRequest.setFirstName("Tester");
                signUpRequest.setPassword(password);

                UserDetailsImpl userDetails = UserDetailsImpl.builder()
                                .id(420L)
                                .firstName("Tester")
                                .lastName("Tester")
                                .username(email)
                                .password(password)
                                .build();

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(email);
                loginRequest.setPassword(password);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));

                // * Act
                // * Assert
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("User registered successfully!"));

                mockMvc.perform(post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andExpect(jsonPath("$.type").value("Bearer"))
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.username").value(email))
                                .andExpect(jsonPath("$.firstName").value("Tester"))
                                .andExpect(jsonPath("$.lastName").value("Tester"))
                                .andExpect(jsonPath("$.admin").value(false));
        }
}
