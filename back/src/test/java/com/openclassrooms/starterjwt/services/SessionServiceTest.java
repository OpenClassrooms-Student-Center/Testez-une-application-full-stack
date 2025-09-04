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

import java.util.*;

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
    void create_ok() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_ok() {
        doNothing().when(sessionRepository).deleteById(1L);

        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_ok() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(2, result.size());
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_ok() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_notFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
    }

    @Test
    void update_ok() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(1L, session);

        assertNotNull(result);
        assertEquals(1L, session.getId()); // l'id doit être mis à jour
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_ok() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(2L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.participate(1L, 2L);

        assertEquals(1, session.getUsers().size());
        assertTrue(session.getUsers().contains(user));
    }

    @Test
    void participate_sessionOrUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void participate_alreadyParticipate() {
        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void noLongerParticipate_ok() {
        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setUsers(new ArrayList<>(Collections.singletonList(user)));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        sessionService.noLongerParticipate(1L, 2L);

        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void noLongerParticipate_sessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }

    @Test
    void noLongerParticipate_userNotParticipating() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }
}
