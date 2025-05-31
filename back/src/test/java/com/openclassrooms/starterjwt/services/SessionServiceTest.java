package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepository.class);
        userRepository = mock(UserRepository.class);
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    void testCreate() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertEquals(session, result);
        verify(sessionRepository).save(session);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        sessionService.delete(id);
        verify(sessionRepository).deleteById(id);
    }

    @Test
    void testFindAll() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(2, result.size());
        verify(sessionRepository).findAll();
    }

    @Test
    void testGetById_Found() {
        Long id = 1L;
        Session session = new Session();
        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(id);

        assertEquals(session, result);
    }

    @Test
    void testGetById_NotFound() {
        Long id = 2L;
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        Session result = sessionService.getById(id);

        assertNull(result);
    }

    @Test
    void testUpdate() {
        Long id = 1L;
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(id, session);

        assertEquals(id, session.getId());
        assertEquals(session, result);
        verify(sessionRepository).save(session);
    }

    @Test
    void testParticipate_Success() {
        Long sessionId = 1L;
        Long userId = 2L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());
        User user = new User();
        user.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.participate(sessionId, userId);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void testParticipate_SessionOrUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));

        Session session = new Session();
        session.setUsers(new ArrayList<>());
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void testParticipate_AlreadyParticipate() {
        Long sessionId = 1L;
        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        Session session = new Session();
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testNoLongerParticipate_Success() {
        Long sessionId = 1L;
        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        Session session = new Session();
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.noLongerParticipate(sessionId, userId);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }

    @Test
    void testNoLongerParticipate_UserNotParticipating() {
        Long sessionId = 1L;
        Long userId = 2L;
        Session session = new Session();
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }
}
