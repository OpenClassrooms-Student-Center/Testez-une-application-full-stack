package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc

class SessionControllerIntTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtils jwtUtils;

    private String jwt;

    @BeforeEach
    void setUp() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testSessionFindAll() throws Exception {
        mockMvc.perform(get("/api/session")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testSessionFindAllUnauthorized() throws Exception {
        mockMvc.perform(get("/api/session")
                .header("Authorization", "Bearer not_a_jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSessionFindById() throws Exception {
        mockMvc.perform(get("/api/session/2")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testSessionFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/session/0")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSessionFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/notAnId")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSessionCreate() throws Exception {
        SessionDto session = new SessionDto();
        session.setName("new session");
        session.setDescription("test create session");
        session.setTeacher_id(1L);
        session.setDate(Date.from(Instant.now()));

        mockMvc.perform(post("/api/session")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSessionUpdate() throws Exception {
        SessionDto session = new SessionDto();
        session.setName("new session");
        session.setDescription("updated session");
        session.setTeacher_id(1L);
        session.setDate(Date.from(Instant.now()));

        mockMvc.perform(put("/api/session/2")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSessionUpdateBadRequest() throws Exception {
        SessionDto session = new SessionDto();
        session.setName("new session");
        session.setDescription("updated session");
        session.setTeacher_id(1L);
        session.setDate(Date.from(Instant.now()));

        mockMvc.perform(put("/api/session/notAnId")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSessionDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/session/0")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSessionDeleteBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/notgoodId")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSessionParticipateOK() throws Exception {
        mockMvc.perform(post("/api/session/2/participate/1")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testSessionParticipateBadRequest() throws Exception {
        mockMvc.perform(post("/api/session/2/participate/wrongId")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSessionNoLongerParticipateOK() throws Exception {
        mockMvc.perform(delete("/api/session/2/participate/1")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testSessionNoLongerParticipateBadRequest() throws Exception {
        mockMvc.perform(delete("/api/session/2/participate/wrongId")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

}