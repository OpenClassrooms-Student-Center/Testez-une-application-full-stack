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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    AuthController authController;
    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private JwtUtils jwtUtilsMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @BeforeEach
    public void setUp() {
        authController = new AuthController(
                authenticationManagerMock,
                passwordEncoder,
                jwtUtilsMock,
                userRepositoryMock);
    }

    @Test
    @DisplayName("Login test")
    void whenUserIsAuthenticated_thenReturnJwtResponse() throws Exception {
        //GIVEN
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        String token = "Test-token";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L) // Assuming id is Long
                .admin(true)
                .username("yoga@studio.com")
                .password("test!1234")
                .firstName("Admin")
                .lastName("ADMIN")
                .build();

        User user = new User().setAdmin(true);
        boolean isAdmin = user.isAdmin();
        //WHEN
        when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(authentication);

        when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn(token);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(userRepositoryMock.findByEmail(userDetails.getUsername()))
                .thenReturn(Optional.of(user));

        //THEN
        JwtResponse jwtResponse = new JwtResponse(
                token,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                isAdmin);

        ResponseEntity<?> logged = ResponseEntity.ok(jwtResponse);
        ResponseEntity<JwtResponse> login = (ResponseEntity<JwtResponse>) authController.authenticateUser(loginRequest);

        assertEquals("You are logged", logged.getStatusCode(), login.getStatusCode());
        assertThat(login.getBody().getId()).isEqualTo(1L);
        assertThat(login.getBody().getAdmin()).isTrue();
        assertThat(login.getBody().getToken()).isEqualTo("Test-token");
        assertThat(login.getBody().getFirstName()).isEqualTo("Admin");
        assertThat(login.getBody().getLastName()).isEqualTo("ADMIN");
        assertThat(login.getBody().getUsername()).isEqualTo("yoga@studio.com");

        verify(authenticationManagerMock, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        verify(jwtUtilsMock, times(1)).generateJwtToken(authentication);

        verify(userRepositoryMock, times(1)).findByEmail(userDetails.getUsername());
    }

    @Test
    @DisplayName("User is null")
    void whenUserIsNull_isAdminIsFalse() {
        //GIVEN
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@invalid.com");
        loginRequest.setPassword("invalid!1234");

        String token = "Test-token";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(loginRequest.getEmail())
                .password(loginRequest.getPassword())
                .build();

        //WHEN
        when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(authentication);

        when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn(token);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        //THEN
        User user = userRepositoryMock.findByEmail(userDetails.getUsername()
        ).orElse(null);

        ResponseEntity<JwtResponse> login = (ResponseEntity<JwtResponse>) authController.authenticateUser(loginRequest);

        assertThat(user).isNull();
        assertThat(login.getBody().getAdmin()).isFalse();
        verify(authenticationManagerMock, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        verify(jwtUtilsMock, times(1)).generateJwtToken(authentication);

        verify(userRepositoryMock, times(2)).findByEmail(userDetails.getUsername());
    }

    @Test
    @DisplayName("Register test")
    void whenSignupRequestIsValid_thenSaveUser() {
        //GIVEN
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("guerda@test.com");
        signupRequest.setFirstName("Guerda");
        signupRequest.setLastName("GUERDA");
        signupRequest.setPassword("test!1234");

        User user = new User(signupRequest.getEmail(),
                signupRequest.getLastName(),
                signupRequest.getFirstName(),
                signupRequest.getPassword(),
                false);
        //WHEN
        when(passwordEncoder.encode(any())).thenReturn(user.getPassword());

        when(userRepositoryMock.save(any(User.class)))
                .thenReturn(user);
        //THEN
        MessageResponse messageResponse = new MessageResponse("User registered successfully!");

        ResponseEntity<?> messageSent = ResponseEntity.ok(messageResponse);

        ResponseEntity<MessageResponse> sendMessage = (ResponseEntity<MessageResponse>) authController.registerUser(signupRequest);

        assertEquals("Returned message", messageSent.getStatusCode(), sendMessage.getStatusCode());
        assertThat(sendMessage.getBody().getMessage()).isEqualTo(messageResponse.getMessage());
        verify(passwordEncoder, times(1)).encode(user.getPassword());
        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    @DisplayName("BAD REQUEST")
    void whenEmailAlreadyExists_thenReturnBadRequest() {
        //GIVEN
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("guerda@test.com");
        //WHEN
        when(userRepositoryMock.existsByEmail(signupRequest.getEmail()))
                .thenReturn(true);
        //THEN
        MessageResponse messageResponse = new MessageResponse("Error: Email is already taken!");
        ResponseEntity<?> badRequest = ResponseEntity.badRequest()
                .body(messageResponse);

        ResponseEntity<MessageResponse> email = (ResponseEntity<MessageResponse>) authController.registerUser(signupRequest);

        assertThat(email.getStatusCode()).isEqualTo(badRequest.getStatusCode());
        assertThat(email.getBody().getMessage()).isEqualTo(messageResponse.getMessage());
        verify(userRepositoryMock, times(1)).existsByEmail(signupRequest.getEmail());
    }
}
