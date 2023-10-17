package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.http.MediaType;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntTests {

        @Autowired
        private MockMvc mockMvc;

        // Test to find a session by ID
        @Test
        @WithMockUser(roles = "ADMIN")
        public void sessionfindByIdOk() throws Exception {
                // find the session with id 1
                mockMvc.perform(get("/api/session/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id", is(1)))
                                .andReturn();
        }

        // Test to not find a session by ID
        @Test
        @WithMockUser(roles = "ADMIN")
        public void sessionFindByIdNotFound() throws Exception {
                mockMvc.perform(get("/api/session/0"))
                                .andExpect(status().isNotFound());
        }

        // Test to not find a session by ID with a non-numeric ID
        @Test
        @WithMockUser(roles = "ADMIN")
        public void sessionFindByIdBadRequest() throws Exception {
                mockMvc.perform(get("/api/session/notanumber"))
                                .andExpect(status().isBadRequest());
        }

        // Test to find all sessions
        @Test
        @WithMockUser(roles = "ADMIN")
        public void sessionfindAllOk() throws Exception {
                mockMvc.perform(get("/api/session"))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        // Test to verify that a user unlogged cannot find all sessions
        @Test
        public void unauthorizedFindAllSession() throws Exception {
                mockMvc.perform(get("/api/session"))
                                .andExpect(status().isUnauthorized());
        }

        // Test to create a session successfully
        @Test
        @WithMockUser(roles = "ADMIN")
        public void createSessionOk() throws Exception {
                // Create a SessionDto object
                SessionDto sessionDto = new SessionDto();
                sessionDto.setName("Pilate");
                sessionDto.setDate(Date.from(Instant.now()));
                sessionDto.setTeacher_id(2L);
                sessionDto.setDescription("have a good time");
                sessionDto.setUsers(List.of(1L, 2L));

                // Perform the POST request
                mockMvc.perform(post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(sessionDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name", is("Pilate")))
                                .andExpect(jsonPath("$.description", is("have a good time")));

                // check that the session has been created
                mockMvc.perform(get("/api/session/2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name", is("Pilate")))
                                .andExpect(jsonPath("$.description", is("have a good time")))
                                .andReturn();
        }

        // Test to update a session successfully
        @Test
        @WithMockUser(roles = "ADMIN")
        public void updateSessionOk() throws Exception {
                // Create a SessionDto object
                SessionDto sessionDto = new SessionDto();
                sessionDto.setName("New Session Name");
                sessionDto.setDate(Date.from(Instant.now()));
                sessionDto.setTeacher_id(1L);
                sessionDto.setDescription("some description");

                // Perform the PUT request
                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(sessionDto)))
                                .andExpect(status().isOk());

                // check that the session has been updated
                mockMvc.perform(get("/api/session/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name", is("New Session Name")))
                                .andExpect(jsonPath("$.description", is("some description")))
                                .andExpect(jsonPath("$.teacher_id", is(1)))
                                .andReturn();
        }

        // Test to delete a session successfully
        // Commented out because it deletes the session from the database
        // @Test
        // @WithMockUser(roles = "ADMIN")
        // public void sessionDeleteOk() throws Exception {
        // mockMvc.perform(delete("/api/session/1"))
        // .andExpect(status().isOk());
        // }

        // Test to participate to a session
        @Test
        @WithMockUser(roles = "USER")
        public void sessionParticipateOk() throws Exception {
                mockMvc.perform(post("/api/session/1/participate/3"))
                                .andExpect(status().isOk())
                                .andReturn();
        }

}
