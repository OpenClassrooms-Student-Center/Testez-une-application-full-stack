package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void testCreate_ShouldSaveAndReturnSession() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals(session, result);
        verify(sessionRepository, times(1)).save(session);
    }


    @Test
    void testDelete_ShouldDelete() {
        Long sessionId = 1L;
        doNothing().when(sessionRepository).deleteById(sessionId);

        sessionService.delete(sessionId);
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testFindAll_ShouldReturnListOfSessions() {
        Session session1 = new Session();
        Session session2 = new Session();

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session1);
        sessionList.add(session2);

        when(sessionRepository.findAll()).thenReturn(sessionList);

        List<Session> result = sessionService.findAll();

        assertEquals(sessionList, result);
        assertEquals(2, result.size());
        assertTrue(result.contains(session1));
        assertTrue(result.contains(session2));

        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetById_ShouldReturnSessionExist() {
        Long sessionId = 1L;
        Session session = new Session();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        Session result = sessionService.getById(sessionId);

        assertEquals(session, result);
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void testGetById_SessionNotFound_ShouldReturnNull() {
        Long sessionId = 1L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Session result = sessionService.getById(sessionId);

        assertNull(result);
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void testUpdate_ShouldUpdateSession() {
        Session session = new Session();
        Long sessionId = 1L;

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(sessionId, session);

        assertEquals(sessionId, result.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipate_Success() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        Long sessionId = 1L;
        User user = new User();
        Long userId = 10L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any())).thenReturn(session);

        sessionService.participate(sessionId, userId);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testParticipate_UserNotFound() {
        Long sessionId = 1L;
        Long userId = 10L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testParticipate_SessionNotFound() {
        Long sessionId = 1L;
        Long userId = 10L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void testParticipate_UserAlreadyParticipating() {
        Long sessionId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Session session = new Session();
        session.setUsers(userList);


        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testNoLongerParticipate_Success() {
        Long sessionId = 1L, userId = 2L;
        User user = new User();
        user.setId(userId);

        List<User> userlist= new  ArrayList<>();
        userlist.add(user);
        Session session = new Session();
        session.setUsers(userlist);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any())).thenReturn(session);

        sessionService.noLongerParticipate(sessionId, userId);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        Long sessionId = 1L, userId = 2L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }

    @Test
    void testNoLongerParticipate_UserNotParticipating() {
        Long sessionId = 1L, userId = 2L;
        Session session = new Session();
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }
}