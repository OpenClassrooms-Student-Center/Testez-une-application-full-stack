// src/test/java/com/openclassrooms/starterjwt/controllers/SessionControllerIT.java
package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();
    }

    @Test
    void findAll_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createAndFindById_shouldReturnSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");

        // Création
        String response = mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Session"))
                .andReturn().getResponse().getContentAsString();

        SessionDto created = objectMapper.readValue(response, SessionDto.class);

        // Récupération par ID
        mockMvc.perform(get("/api/session/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Session"));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/session/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldModifySession() throws Exception {
        Session session = new Session();
        session.setName("Old Name");
        session = sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Name");

        mockMvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void update_shouldReturnBadRequest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Name");
        mockMvc.perform(put("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_shouldRemoveSession() throws Exception {
        Session session = new Session();
        session.setName("ToDelete");
        session = sessionRepository.save(session);

        mockMvc.perform(delete("/api/session/" + session.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + session.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/session/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/abc"))
                .andExpect(status().isBadRequest());
    }
}