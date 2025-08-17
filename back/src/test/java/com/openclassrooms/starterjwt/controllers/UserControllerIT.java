// src/test/java/com/openclassrooms/starterjwt/controllers/UserControllerIT.java
package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    void findById_shouldReturnUser() throws Exception {
        User user = new User();
        user.setEmail("test@email.com");
        user = userRepository.save(user);

        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldRemoveUser_whenAuthorized() throws Exception {
        User user = new User();
        user.setEmail("test@email.com");
        user = userRepository.save(user);

        // Mock authentifié avec le même email
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@email.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());

        // Vérifie que l'utilisateur a bien été supprimé
        mockMvc.perform(get("/api/user/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnUnauthorized_whenNotOwner() throws Exception {
        User user = new User();
        user.setEmail("test@email.com");
        user = userRepository.save(user);

        // Mock authentifié avec un autre email
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("other@email.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete_shouldReturnNotFound() throws Exception {
        // Mock authentifié
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@email.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        mockMvc.perform(delete("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnBadRequest() throws Exception {
        // Mock authentifié
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@email.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }
}