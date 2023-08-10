package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks
    AuthController authController;
    @Autowired
    private MockMvc mockMvc; //Pour les appeles REST
    @Mock //Mock les services
    private AuthenticationManager authenticationManagerMock;

    @Mock //Mock les services
    private JwtUtils jwtUtilsMock;

    @Mock //Mock les services
    private UserRepository userRepositoryMock;

    @Mock //Mock les services
    private PasswordEncoder  passwordEncoder;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }


    @Test
    @DisplayName("Login test")
    void whenUserIsAuthenticated_thenReturnJwtResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        String token = "Test-token";
        when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn(token);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L) // Assuming id is Long
                .admin(true)
                .username("yoga@studio.com")
                .password("test!1234")
                .firstName("Admin")
                .lastName("ADMIN")
                .build();
        when(authentication.getPrincipal()).thenReturn(userDetails);

        User user = User.builder()
                .id(userDetails.getId())
                .admin(userDetails.getAdmin())
                .email(userDetails.getUsername())
                .password(userDetails.getPassword())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .build();

        when(userRepositoryMock.findByEmail(userDetails.getUsername()))
                .thenReturn(Optional.of(user));


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(jsonPath("$.token").value("Test-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").value(1)) // Match the same data type as id in UserDetailsImpl
                .andExpect(jsonPath("$.username").value("yoga@studio.com"))
                .andExpect(jsonPath("$.firstName").value("Admin"))
                .andExpect(jsonPath("$.lastName").value("ADMIN"))
                .andExpect(jsonPath("$.admin").value(true)).andReturn();
    }


    @Test
    @DisplayName("Error for empty required field")
    void whenEmptyRequiredField_thenReturnAnError(){
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("");
        invalidRequest.setPassword("test!1234");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username(invalidRequest.getEmail())
                .password(invalidRequest.getPassword())
                .build();

        when(authenticationManagerMock.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        authenticationManagerMock.authenticate(new UsernamePasswordAuthenticationToken(
                invalidRequest.getEmail(), invalidRequest.getPassword()));

        Optional<User> invalidEmailExpected = userRepositoryMock.findByEmail(userDetails.getUsername());

        assertThat(invalidEmailExpected).isEqualTo(Optional.empty());

    }
    @Test
    @DisplayName("Register test")
    void whenSignupRequestIsValid_thenSaveUser() throws Exception {
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

        when(userRepositoryMock.save(any(User.class)))
                .thenReturn(user);

        new MessageResponse("User registered successfully!");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"))
                .andReturn();
    }

    // RAF
    //LOGIN :
    //La gestion des erreurs en cas de mauvais login / password

    //REGISTER:
    //L’affichage d’erreur en l’absence d’un champ obligatoire
    }
