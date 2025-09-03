package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SessionController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                WebSecurityConfig.class,
                AuthTokenFilter.class
        })
})
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;
    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void findById_ok() throws Exception {
        Session session = new Session();
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Morning Yoga");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setDescription("Desc");
        dto.setUsers(Collections.emptyList());

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(dto);

        mockMvc.perform(get("/api/session/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Morning Yoga"));
    }

    @Test
    void findById_notFound() throws Exception {
        when(sessionService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/session/{id}", "99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(get("/api/session/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ok() throws Exception {
        Session s1 = new Session();
        Session s2 = new Session();

        SessionDto d1 = new SessionDto();
        d1.setId(1L);
        d1.setName("S1");
        d1.setDate(new Date());
        d1.setTeacher_id(1L);
        d1.setDescription("D1");
        d1.setUsers(Collections.emptyList());

        SessionDto d2 = new SessionDto();
        d2.setId(2L);
        d2.setName("S2");
        d2.setDate(new Date());
        d2.setTeacher_id(1L);
        d2.setDescription("D2");
        d2.setUsers(Collections.emptyList());

        when(sessionService.findAll()).thenReturn(Arrays.asList(s1, s2));
        when(sessionMapper.toDto(anyList())).thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void delete_ok() throws Exception {
        Session s = new Session();
        when(sessionService.getById(10L)).thenReturn(s);
        doNothing().when(sessionService).delete(10L);

        mockMvc.perform(delete("/api/session/{id}", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_notFound() throws Exception {
        when(sessionService.getById(11L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/{id}", "11"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(delete("/api/session/{id}", "xyz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void participate_ok() throws Exception {
        doNothing().when(sessionService).participate(5L, 7L);

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", "5", "7"))
                .andExpect(status().isOk());
    }

    @Test
    void participate_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", "a", "7"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", "5", "b"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_ok() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(8L, 12L);

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "8", "12"))
                .andExpect(status().isOk());
    }

    @Test
    void noLongerParticipate_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "x", "12"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", "8", "y"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ok() throws Exception {
        SessionDto dtoIn = new SessionDto();
        dtoIn.setName("Morning Yoga");
        dtoIn.setDate(new Date());
        dtoIn.setTeacher_id(1L);
        dtoIn.setDescription("Description");
        dtoIn.setUsers(Collections.emptyList());

        Session session = new Session();
        SessionDto dtoOut = new SessionDto();
        dtoOut.setId(1L);
        dtoOut.setName("Morning Yoga");
        dtoOut.setDate(dtoIn.getDate());
        dtoOut.setTeacher_id(1L);
        dtoOut.setDescription("Description");
        dtoOut.setUsers(Collections.emptyList());

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(dtoOut);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoIn)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Morning Yoga"));
    }

    @Test
    void update_ok() throws Exception {
        SessionDto dtoIn = new SessionDto();
        dtoIn.setName("Evening Yoga");
        dtoIn.setDate(new Date());
        dtoIn.setTeacher_id(1L);
        dtoIn.setDescription("Updated Description");
        dtoIn.setUsers(Collections.emptyList());

        Session session = new Session();
        SessionDto dtoOut = new SessionDto();
        dtoOut.setId(1L);
        dtoOut.setName("Evening Yoga");
        dtoOut.setDate(dtoIn.getDate());
        dtoOut.setTeacher_id(1L);
        dtoOut.setDescription("Updated Description");
        dtoOut.setUsers(Collections.emptyList());

        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(Mockito.eq(1L), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(dtoOut);

        mockMvc.perform(put("/api/session/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoIn)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Evening Yoga"));
    }

    @Test
    void update_badRequest_nonNumeric() throws Exception {
        SessionDto dtoIn = new SessionDto();
        dtoIn.setName("Invalid");
        dtoIn.setDate(new Date());
        dtoIn.setTeacher_id(1L);
        dtoIn.setDescription("Bad Request");
        dtoIn.setUsers(Collections.emptyList());

        mockMvc.perform(put("/api/session/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoIn)))
                .andExpect(status().isBadRequest());
    }
}
