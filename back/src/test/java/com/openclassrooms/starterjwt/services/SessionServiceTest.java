package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SessionServiceTest {

    SessionService sessionService;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    List<Session> sessions;


    @BeforeEach
    void setUp() {
        sessionService = new SessionService(sessionRepository, userRepository);
        sessions = new ArrayList<>();
        sessions = List.of(
                Session.builder().id(1L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null).users(null).build(),
                Session.builder().id(2L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null).users(null).build(),
                Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null).users(null).build()
        );

    }

    @AfterEach
    void tearDown() {
        sessionService = null;
    }

    @Test
    void create() {
        //Given
        Session session = Session.builder().id(4L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null).users(null).build();
        when(sessionRepository.save(session)).thenReturn(session);
        //When
        Session sessionSaved = sessionService.create(session);
        //Then
        assertThat(sessionSaved).isEqualTo(session);
    }

    @Test
    void delete() {
        //Given
        //When
        sessionService.delete(1L);
        //Then
        assertThat(sessionRepository.existsById(1L)).isFalse();
    }

    @Test
    void findAll() {
        //Given
        when(sessionRepository.findAll()).thenReturn(sessions);
        //When
        List<Session> sessionsFound = sessionService.findAll();
        //Then
        assertThat(sessionsFound).isEqualTo(sessions);
    }

    @Test
    void getById() {
        //Given
        when(sessionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(sessions.get(0)));
        //When
        Session sessionFound = sessionService.getById(1L);
        //Then
        assertThat(sessionFound).isEqualTo(sessions.get(0));
    }

    @Test
    void update() {
        //Given
        Session session = Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null).users(null).build();
        when(sessionRepository.save(session)).thenReturn(session);
        //When
        Session sessionSaved = sessionService.update(3L, session);
        //Then
        assertThat(sessionSaved).isEqualTo(session);
    }

    @Test
    void participate() {
        //Given
        Session session = Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(new Teacher()).users(new ArrayList<User>()).build();
        when(sessionRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(session));
        User user = User.builder().id(1L).firstName("Quincy").lastName("Jones").password("password").email("email").build();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        //When
        sessionService.participate(3L, 1L);
        //Then
        assertThat(session.getUsers().get(0)).isEqualTo(user);
    }

    @Test
    void noLongerParticipate() {
        //Given
        Session session = Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(new Teacher()).users(new ArrayList<User>()).build();
        when(sessionRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(session));
        User user = User.builder().id(1L).firstName("Quincy").lastName("Jones").password("password").email("email").build();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        sessionService.participate(3L, 1L);
        //When
        sessionService.noLongerParticipate(3L, 1L);
        //Then
        assertThat(session.getUsers().size()).isEqualTo(0);
    }
}