package com.openclassrooms.starterjwt.controllers.unit;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private JwtUtils jwtUtils;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private UserRepository userRepository;

        private AuthController authController;

        @BeforeEach
        public void setUp() {
                // Create an instance of AuthController
                authController = new AuthController(authenticationManager,
                                passwordEncoder, jwtUtils, userRepository);
        }

        // Test to verify that the authenticateUser method of the authController returns
        // a JwtResponse
        @Test
        public void authenticateUserOk() {
                // Mock user data
                Long id = 2L;
                String email = "alex@gmail.com";
                String password = "password";
                String firstname = "Alex";
                String lastname = "Alex";
                boolean isAdmin = false;

                // Create UserDetailsImpl using the builder pattern
                UserDetailsImpl userDetails = UserDetailsImpl.builder().username(email).firstName(firstname)
                                .lastName(lastname)
                                .id(id).password(password).build();

                // Create a mock authentication token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null);

                // Mock the behavior of authentication and related methods
                when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
                                .thenReturn(authentication);
                when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");
                when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder().id(id).email(email)
                                .password(password).firstName(firstname).lastName(lastname).admin(isAdmin).build()));

                // Create a login request
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(email);
                loginRequest.setPassword(password);

                // Call the authenticateUser method
                ResponseEntity<?> response = authController.authenticateUser(loginRequest);
                JwtResponse responseBody = (JwtResponse) response.getBody();

                // Assertions to verify the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(email, responseBody.getUsername());
                assertEquals(firstname, responseBody.getFirstName());
                assertEquals(lastname, responseBody.getLastName());
                assertEquals(id, responseBody.getId());
                assertEquals(isAdmin, responseBody.getAdmin());
                assertEquals("Bearer", responseBody.getType());
                assertNotNull(responseBody.getToken());
        }

        @Test
        public void registerUserOk() {
                // Mock user data
                String email = "alex@gmail.com";
                String password = "password";

                // Mock the behavior of UserRepository and PasswordEncoder
                when(userRepository.existsByEmail(email)).thenReturn(false);
                when(passwordEncoder.encode(password)).thenReturn("hashed");
                when(userRepository.save(any(User.class))).thenReturn(new User());

                // Create a signup request
                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(email);
                signupRequest.setFirstName("");
                signupRequest.setLastName("");
                signupRequest.setPassword(password);

                // Call the registerUser method
                ResponseEntity<?> response = authController.registerUser(signupRequest);

                // Assertion to verify the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        // Test with bad request and error message when email is already taken
        @Test
        public void registerUserEmailAlreadyTaken() {
                // Mock user data
                String email = "alex@gmail.com";
                String password = "password";

                // Mock the behavior of UserRepository
                when(userRepository.existsByEmail(email)).thenReturn(true);

                // Create a signup request
                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(email);
                signupRequest.setFirstName("");
                signupRequest.setLastName("");
                signupRequest.setPassword(password);

                // Call the registerUser method
                ResponseEntity<?> response = authController.registerUser(signupRequest);

                // Verify that the response contains the expected error message
                MessageResponse messageResponse = (MessageResponse) response.getBody();
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Error: Email is already taken!", messageResponse.getMessage());
        }

}