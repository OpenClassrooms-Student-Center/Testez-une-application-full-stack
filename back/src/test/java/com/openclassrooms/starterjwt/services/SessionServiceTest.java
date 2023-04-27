package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateSession() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        Session createdSession = sessionService.create(session);
        Assertions.assertEquals(session.getId(), createdSession.getId());
    }

    @Test
    public void testDeleteSession() {
        Session session = new Session();
        session.setId(1L);
        doNothing().when(sessionRepository).deleteById(anyLong());
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        sessionService.delete(session.getId());
        verify(sessionRepository, times(1)).deleteById(session.getId());
    }

    @Test
    public void testFindAllSessions() {
        List<Session> sessionList = new ArrayList<>();
        Session session = new Session();
        session.setId(1L);
        sessionList.add(session);
        when(sessionRepository.findAll()).thenReturn(sessionList);
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        List<Session> foundSessions = sessionService.findAll();
        Assertions.assertEquals(1, foundSessions.size());
    }

    @Test
    public void testGetSessionById() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        Session foundSession = sessionService.getById(session.getId());
        Assertions.assertEquals(session.getId(), foundSession.getId());
    }

    @Test
    public void testUpdateSession() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        Session updatedSession = sessionService.update(session.getId(), session);
        Assertions.assertEquals(session.getId(), updatedSession.getId());
    }

    @Test
    public void testParticipate() {
        Long sessionId = 1L;
        Session session = Session.builder()
                .name("Session Test")
                .id(sessionId)
                .users(new ArrayList<>()).build();

        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .email("test@mail.com")
                .lastName("Doe")
                .firstName("John")
                .password("password").build();

        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        sessionService.participate(session.getId(), user.getId());
        Assertions.assertEquals(1, session.getUsers().size());
        Assertions.assertEquals(user.getId(), session.getUsers().get(0).getId());
    }

    @Test
    public void testParticipateNotFoundException() {

        Long sessionId = 1L;
        Long userId = 2L;

        Session session = new Session();
        session.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(userRepository, times(1)).findById(userId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void testParticipateBadRequestException() {

        Long sessionId = 1L;

        Long userId = 2L;
        User user = User.builder()
                .id(userId)
                .email("test@mail.com")
                .lastName("Doe")
                .firstName("John")
                .password("password").build();

        List<User> users = new ArrayList<>();
        users.add(user);

        Session session = Session.builder()
                .name("Session 1")
                .id(sessionId)
                .users(users).build();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    public void testNoLongerParticipate() {
        Long sessionId = 1L;
        Long userId = 2L;
        Session session = new Session();
        User user = new User();
        user.setId(userId);
        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        SessionService sessionService = new SessionService(sessionRepository, userRepository);
        sessionService.noLongerParticipate(sessionId, userId);

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(session);

        List<User> updatedUsers = session.getUsers();
        assertTrue(updatedUsers.isEmpty());
    }

    @Test
    public void testNoLongerParticipateBadRequestException() {

        Long sessionId = 1L;
        Long userId = 2L;

        Session session = Session.builder()
                .name("Session 1")
                .id(sessionId)
                .users(new ArrayList<>()).build();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

    }

    @Test
    public void testNoLongerParticipateNotFoundException() {
        Long sessionId = 1L;
        Long userId = 2L;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        SessionService sessionService = new SessionService(sessionRepository, userRepository);

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

}
