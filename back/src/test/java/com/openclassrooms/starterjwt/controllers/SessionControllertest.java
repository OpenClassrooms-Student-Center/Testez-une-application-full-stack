package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnSessionDto() {
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void findById_shouldReturnBadRequest() {
        ResponseEntity<?> response = sessionController.findById("abc");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void findAll_shouldReturnListOfSessionDto() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        List<SessionDto> sessionDtos = Arrays.asList(new SessionDto(), new SessionDto());
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    void create_shouldReturnCreatedSessionDto() {
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void update_shouldReturnUpdatedSessionDto() {
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void update_shouldReturnBadRequest() {
        SessionDto sessionDto = new SessionDto();
        ResponseEntity<?> response = sessionController.update("abc", sessionDto);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void save_shouldDeleteAndReturnOk() {
        Session session = new Session();
        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).delete(1L);
    }

    @Test
    void save_shouldReturnNotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void save_shouldReturnBadRequest() {
        ResponseEntity<?> response = sessionController.save("abc");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void participate_shouldReturnOk() {
        ResponseEntity<?> response = sessionController.participate("1", "2");
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void participate_shouldReturnBadRequest() {
        ResponseEntity<?> response = sessionController.participate("a", "b");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void noLongerParticipate_shouldReturnOk() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void noLongerParticipate_shouldReturnBadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("a", "b");
        assertEquals(400, response.getStatusCodeValue());
    }
}
