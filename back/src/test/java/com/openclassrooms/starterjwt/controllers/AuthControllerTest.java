package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private JwtUtils jwtUtilsMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder  passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Initialize mockMvc with the standalone setup
        mockMvc = MockMvcBuilders.standaloneSetup(
                new AuthController(authenticationManagerMock, passwordEncoder, jwtUtilsMock, userRepositoryMock)
        ).build();
    }
    @Test
    @DisplayName("Login test")
    void testAuthenticateUser() throws Exception {
        // Create a login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Mock authentication and user details
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "Admin", "ADMIN", true, "test!1234");

        when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock user repository
        User user = new User();
        when(userRepositoryMock.findByEmail(any())).thenReturn(Optional.of(user));

        user.setAdmin(true);
        // Mock JWT token
        String token = "test-token";
        when(jwtUtilsMock.generateJwtToken(any(Authentication.class)))
                .thenReturn(token);

        // Perform the POST request and validate the response
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("yoga@studio.com"))
                .andExpect(jsonPath("$.admin").value(true));
    }
    @Test
    @DisplayName("Register test")
    void testUserRegistration() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("guerda@test.com");
        signupRequest.setFirstName("Guerda");
        signupRequest.setLastName("GUERDA");
        signupRequest.setPassword("test!1234");

        when(passwordEncoder.encode(any())).thenReturn(signupRequest.getPassword());

        User user = new User(signupRequest.getEmail(),
                signupRequest.getLastName(),
                signupRequest.getFirstName(),
                signupRequest.getPassword(),
                false);

        when(userRepositoryMock.save(any())).thenAnswer(register -> user);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }
    }
