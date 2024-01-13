package com.openclassrooms.starterjwt.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {
    @Mock
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private Session session;

    @BeforeEach
    void setUp() {
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(currentTime)
                .setUpdatedAt(currentTime);

        User mockedUser = new User("Toto", "Toto",
                "Toto69", "totoBruv", false);

        List<User> arrayOfUsers = new ArrayList<>();
        arrayOfUsers.add(mockedUser);

        session = new Session(1L, "TEST session",
                currentDate, "TEST description for the session",
                teacher,
                arrayOfUsers, currentTime,
                currentTime);

        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    public void testCreatingNewSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }

    @Test
    public void testDeletingASession() {
        doNothing().when(sessionRepository).deleteById(session.getId());

        sessionService.delete(session.getId());

        verify(sessionRepository).deleteById(session.getId());
    }

    @Test
    public void testFindingAllSessions() {

        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessions.add(session);

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        verify(sessionRepository).findAll();

        assertEquals(sessions, result);
    }

    @Test
    public void testGetSessionByValidId() {

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        Session result = sessionService.getById(session.getId());

        verify(sessionRepository).findById(session.getId());

        assertEquals(session, result);
    }

    @Test
    public void testUpdatingASession() {

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(session.getId(), session);

        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }

    @Test
    public void testParticipateSession() {
        Long userId = 1L;
        Long sessionId = 1L;

        User mockedUser = new User("Toto", "Toto",
                "Toto69", "totoBruv", false);
        mockedUser.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockedUser));

        sessionService.participate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);

        assertTrue(session.getUsers().contains(mockedUser));
    }

}
