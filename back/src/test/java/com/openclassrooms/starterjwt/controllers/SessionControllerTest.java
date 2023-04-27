package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

class SessionControllerTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    @Test
    public void testSessionFindByIdOK() {

        Long id = 1L;
        String name = "Session name";
        Session session = Session
                .builder()
                .id(id)
                .name(name)
                .build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);

        when(sessionService.getById(id)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findById(id.toString());

        verify(sessionService).getById(id);
        verify(sessionMapper).toDto(session);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), sessionDto);

    }

    @Test
    public void testSessionFindByIdNotFound() {

        Long id = 1L;

        when(sessionService.getById(id)).thenReturn(null);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findById(id.toString());

        verify(sessionService).getById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), null);

    }

    @Test
    public void testSessionFindByIdBadRequest() {

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.findById("notgoodId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSessionFindAll() {

        // Create a list of sessions
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session());
        sessions.add(new Session());
        sessions.add(new Session());

        // Create a list of session DTOs
        List<SessionDto> sessionDtos = new ArrayList<>();
        sessionDtos.add(new SessionDto());
        sessionDtos.add(new SessionDto());
        sessionDtos.add(new SessionDto());

        // Mock the sessionService's findAll method to return the list of sessions
        when(sessionService.findAll()).thenReturn(sessions);

        // Mock the sessionMapper's toDto method to return the list of session DTOs
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Call the controller's findAll method
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> responseEntity = sessionController.findAll();

        // Verify that the response entity's status code is 200 OK
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Verify that the response entity's body is the list of session DTOs
        assertEquals(sessionDtos, responseEntity.getBody());

    }

    @Test
    public void testSessionCreate() {

        String name = "new session";
        Session session = Session
                .builder()
                .name(name)
                .build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName(name);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).create(session);
        verify(sessionMapper).toDto(session);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    public void testSessionUpdateOK() {

        Long id = 1L;
        String name = "created session";
        Session session = Session
                .builder()
                .id(id)
                .name(name)
                .build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName("updated session");

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> responseEntity = sessionController.update(id.toString(), sessionDto);

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(id, session);
        verify(sessionMapper).toDto(session);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    public void testSessionUpdateBadRequest() {

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.update("notgoodId", null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSessionDeleteOK() {

        Long id = 1L;
        String name = "Session name";
        Session session = Session
                .builder()
                .id(id)
                .name(name)
                .build();

        when(sessionService.getById(id)).thenReturn(session);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.save(id.toString());

        verify(sessionService).getById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testSessionDeleteNotFound() {

        Long id = 1L;

        when(sessionService.getById(id)).thenReturn(null);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.save(id.toString());

        verify(sessionService).getById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testSessionDeleteBadRequest() {

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.save("notgoodId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSessionParticipateOK() {

        Long userId = 1L;
        Long sessionId = 1L;

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.participate(sessionId.toString(), userId.toString());

        verify(sessionService).participate(sessionId, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testSessionParticipateBadRequest() {

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.participate("notgoodId", "wrongId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testSessionNoLongerParticipateOK() {

        Long userId = 1L;
        Long sessionId = 1L;

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), userId.toString());

        verify(sessionService).noLongerParticipate(sessionId, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testSessionNoLongerParticipateBadRequest() {

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.noLongerParticipate("notgoodId", "wrongId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}