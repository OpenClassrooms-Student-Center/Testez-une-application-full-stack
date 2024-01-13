package com.openclassrooms.starterjwt.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
// @AutoConfigureTestDatabase
public class SessionControllerIntegrationTests {
        /**
         * MockBean for simulating the AuthenticationManager in the integration tests.
         */
        @MockBean
        private AuthenticationManager authenticationManager;

        /**
         * The main entry point for integration tests of Spring MVC applications.
         */
        @Autowired
        private MockMvc mockMvc;

        /**
         * ObjectMapper for converting Java objects to JSON and vice versa.
         */
        @Autowired
        private ObjectMapper objectMapper;

        /**
         * Mocked service for simulating interactions with the teacher database.
         */
        @MockBean
        private SessionService sessionService;

        @Test
        @Tag("post_api/session/---get_api/session/")
        @DisplayName("(HAPPY PATH) the session should be successfully registered and included in the array of all sessions")
        @WithMockUser(username = "yoga@studio.com", roles = "ADMIN")
        public void testSessionCreationAndRetrieval() throws Exception {
                // * Arrange
                SessionDto sessionDto = new SessionDto();
                sessionDto.setName("New INTEGRATION TEST Session");
                sessionDto.setDate(new Date());
                sessionDto.setTeacher_id(1L);
                sessionDto.setDescription("New Session Description");

                // Assuming you have a Session class with appropriate constructors and getters
                String isoString = "2023-12-30T10:27:21";

                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

                Teacher teacher = new Teacher();
                teacher
                                .setId(sessionDto.getTeacher_id())
                                .setLastName("DELAHAYE")
                                .setFirstName("Margot")
                                .setCreatedAt(localDateTime)
                                .setUpdatedAt(localDateTime);

                Session mockSession = Session.builder()
                                .id(69L)
                                .name(sessionDto.getName())
                                .teacher(teacher)
                                .users(null)
                                .description(
                                                sessionDto.getDescription())
                                .date(new Date())
                                .build();

                // when(sessionService.create(any(Session.class))).thenReturn(mockSession);

                // when(sessionService.getById(69L)).thenReturn(mockSession);

                // * Act
                // * Assert
                mockMvc.perform(post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isOk());

                mockMvc.perform(get("/api/session/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                                .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));

        }

        @Test
        @WithMockUser(username = "yoga@studio.com", roles = "ADMIN")
        public void testSessionUpdateAndRetrieval() throws Exception {
                // * Arrange
                SessionDto sessionDto = new SessionDto();
                sessionDto.setName("Updated INTEGRATION TEST Session");
                sessionDto.setDate(new Date());
                sessionDto.setTeacher_id(420L);
                sessionDto.setDescription("Updated Session Description");

                String isoString = "2023-12-30T10:27:21";

                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

                Teacher teacher = new Teacher();
                teacher
                                .setId(sessionDto.getTeacher_id())
                                .setLastName("DELAHAYE")
                                .setFirstName("Margot")
                                .setCreatedAt(localDateTime)
                                .setUpdatedAt(localDateTime);

                Session mockSession = Session.builder()
                                .name(sessionDto.getName())
                                .teacher(teacher)
                                .users(null)
                                .description(sessionDto.getDescription())
                                .date(new Date())
                                .build();

                when(sessionService.update(any(Long.class), any(Session.class))).thenReturn(mockSession);

                when(sessionService.getById(69L)).thenReturn(mockSession);
                // * Act
                mockMvc.perform(put("/api/session/{id}", 69L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isOk());

                // * Assert
                mockMvc.perform(get("/api/session/{id}", 69L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value(sessionDto.getName()))
                                .andExpect(jsonPath("$.description").value(sessionDto.getDescription()));
        }

        @Test
        @WithMockUser(username = "yoga@studio.com", roles = "ADMIN")
        public void testSessionDeletionAndRetrieval() throws Exception {
                // * Arrange
                // * Act
                mockMvc.perform(delete("/api/session/{id}", 69L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                // * Assert
                mockMvc.perform(get("/api/session/{id}", 69L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

}
