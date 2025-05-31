package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser_shouldReturnJwtResponse() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@email.com", "Jean", "Dupont",false,"password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(
                new User("test@email.com", "Dupont", "Jean", "password", false)
        ));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(JwtResponse.class, response.getBody());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("fake-jwt", jwtResponse.getToken());
        // Remplace getEmail() par la bonne méthode si nécessaire
        assertEquals("Dupont", jwtResponse.getLastName());
    }

    @Test
    void registerUser_shouldReturnErrorIfEmailExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@email.com");
        when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(MessageResponse.class, response.getBody());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());
    }

    @Test
    void registerUser_shouldRegisterUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@email.com");
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("Dupont");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(MessageResponse.class, response.getBody());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticateUser_shouldThrowExceptionOnFailedAuthentication() {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtUtils jwtUtils = mock(JwtUtils.class);

        AuthController controller = new AuthController(
                authenticationManager,
                passwordEncoder,
                jwtUtils,
                userRepository
        );

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("invalidUser");
        loginRequest.setPassword("wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            controller.authenticateUser(loginRequest);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void authenticateUser_shouldSetIsAdminTrueIfUserIsAdmin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@email.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(2L, "admin@email.com", "Admin", "User", true, "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("admin-jwt");
        // Simule un utilisateur admin
        when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(
                new User("admin@email.com", "User", "Admin", "password", true)
        ));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(JwtResponse.class, response.getBody());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertTrue(jwtResponse.getAdmin());
    }

    @Test
    void authenticateUser_shouldSetIsAdminFalseIfUserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("notfound@email.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(3L, "notfound@email.com", "No", "User", false, "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertFalse(jwtResponse.getAdmin());
    }
}