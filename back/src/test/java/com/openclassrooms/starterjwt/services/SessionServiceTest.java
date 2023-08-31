package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @InjectMocks
    SessionService sessionService;
    @Mock
    SessionRepository sessionRepository;
    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        sessionService = new SessionService(
                sessionRepository,
                userRepository
        );
    }

    @Test
    @DisplayName("create")
    void createSession(){
        Session session = Session.builder()
                .id(1L)
                .name("Test")
                .date(Date.from(Instant.now()))
                .description("Test Description")
                .createdAt(LocalDateTime.parse("2023-08-31T11:45:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T11:45:00"))
                .build();

        when(sessionRepository.save(session)).thenReturn(session);

        Session createSession = sessionService.create(session);

        verify(sessionRepository, times(1)).save(session);
        assertEquals("Session created", session, createSession);
    }

    @Test
    @DisplayName("delete")
    void whenSessionId_thenDeleteSession(){
        Long id = 16L;

        sessionRepository.deleteById(id);
        sessionService.delete(id);

        verify(sessionRepository, times(2)).deleteById(id);
    }

    @Test
    @DisplayName("findAll")
    void whenSessionListIsFound_thenReturnSessionList(){
        User user = User.builder()
                .id(1L)
                .email("toto3@toto.com")
                .lastName("TOTO")
                .firstName("toto")
                .password("test!1234")
                .admin(false)
                .createdAt(LocalDateTime.parse("2023-08-31T09:52:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T09:52:00"))
                .build();

        List<User> users = Arrays.asList(user);
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("DELAHAYE")
                .firstName("Margot")
                .createdAt(LocalDateTime.parse("2023-08-31T09:52:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T09:52:00"))
                .build();

        Session session = Session.builder()
                .id(1L)
                .name("Test")
                .date(Date.from(Instant.now()))
                .description("Test session list")
                .teacher(teacher)
                .users(users)
                .createdAt(LocalDateTime.parse("2023-08-31T09:52:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T09:52:00"))
                .build();
        List<Session> sessionList = Arrays.asList(session);


        when(sessionRepository.findAll()).thenReturn(sessionList);

        List<Session> sessionListFound = sessionService.findAll();

        assertEquals("SessionList Found",sessionList,sessionListFound);
        assertEquals("Id", sessionList.get(0).getId(), sessionListFound.get(0).getId());
        assertEquals("Name", sessionList.get(0).getName(), sessionListFound.get(0).getName());
        assertEquals("Date", sessionList.get(0).getDate(), sessionListFound.get(0).getDate());
        assertEquals("Description", sessionList.get(0).getDescription(), sessionListFound.get(0).getDescription());
        assertEquals("Teacher", sessionList.get(0).getTeacher(), sessionListFound.get(0).getTeacher());
        assertEquals("Users", sessionList.get(0).getUsers(), sessionListFound.get(0).getUsers());
        assertEquals("CreatedAt", sessionList.get(0).getCreatedAt(), sessionListFound.get(0).getCreatedAt());
        assertEquals("UpdatedAt", sessionList.get(0).getUpdatedAt(), sessionListFound.get(0).getUpdatedAt());

        assertThat(sessionList.size()).isEqualTo(sessionListFound.size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getById")
    void whenSessionIdIsFound_thenReturnSession(){
        Long id = 17L;
        Session session = new Session();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        Session sessionIdFound = sessionService.getById(id);

        assertEquals("Session id Found", session, sessionIdFound);
        verify(sessionRepository).findById(id);
    }
    @Test
    @DisplayName("null")
    void whenSessionIdIsNotPresent_thenReturnNull(){
        Long id = null;
        Session session = new Session();

        when(sessionRepository.findById(id)).thenReturn(
                Optional.of(session));

        Session isSessionNull = sessionService.getById(id);

        assertEquals("Session is Null", session, isSessionNull);
        verify(sessionRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("update")
    void whenSessionId_thenUpdateSession(){
        Long id = 19L;
        Session session = new Session();
        session.setId(id);

        when(sessionRepository.save(session)).thenReturn(session);

        Session sessionUpdated = sessionService.update(id, session);

        assertEquals("Updated Session", session, sessionUpdated);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("participate")
    void whenSessionIdAndUserIdIsPresent_thenSaveSessionAsParticipated(){
        Long id = 21L;
        Long userId = 20L;

        List<User> users = new ArrayList<>();

        Session session = Session.builder()
                .id(id)
                .name("Test")
                .description("Test description")
                .date(Date.from(Instant.now()))
                .users(users)
                .build();

        User user = User.builder()
                .id(userId)
                .email("toto3@toto.com")
                .lastName("TOTO")
                .firstName("Toto")
                .password("test!1234")
                .admin(false)
                .createdAt(LocalDateTime.parse("2023-08-31T10:34:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T10:34:00"))
                .build();

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        sessionRepository.save(session);
        sessionService.participate(id, userId);

        verify(sessionRepository, times(1)).findById(id);
        verify(userRepository, times(1)).findById(userId);
        verify(sessionRepository, times(2)).save(session);
    }

    @Test
    @DisplayName("not found")
    void whenSessionOrUserIsNull_thenReturnNotFound(){
        Long id = null;
        Long userId = null;

        Session session = sessionRepository.findById(id).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        assertThrows(NotFoundException.class, ()->{
            throw new NotFoundException();
        });

        assertThat(session).isNull();
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("BadRequestException")
    void whenAlreadyParticipate_thenThrowBadRequestException(){
        Long id = 23L;
        Long userId= 23L;

        Session session = new Session();
        session.setUsers(new ArrayList<>());
        User user = new User();

        user.setId(userId);
        session.getUsers().add(user);

        when(sessionRepository.findById(id))
                .thenReturn(Optional.of(session));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, ()->{
            sessionService.participate(id, userId);
        });

        verify(sessionRepository, times(1))
                .findById(id);
        verify(userRepository, times(1))
                .findById(userId);
    }
    @Test
    @DisplayName("noLongerParticipate")
    void whenSessionIdIsDifferentOfUserId_thenSaveSession(){
        Long sessionId = 24L;
        Long userId = 25L;

        User user = User.builder()
                .id(userId)
                .lastName("Altina")
                .firstName("Schinati")
                .email("toto3@toto.com")
                .admin(false)
                .createdAt(LocalDateTime.parse("2023-08-31T11:39:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T11:39:00"))
                .password("test!1234")
                .build();
        List<User> users = new ArrayList<>();
        users.add(user);

        Session session = Session.builder()
                .id(sessionId)
                .name("Test")
                .description("Test do not participate")
                .users(users)
                .build();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionRepository.save(session);

        sessionService.noLongerParticipate(sessionId, userId);

        verify(sessionRepository, times(1))
                .findById(sessionId);
        verify(sessionRepository, times(2))
                .save(session);
    }

    @Test
    @DisplayName("not found")
    void whenSessionIsNull_thenThrowNotFound(){
        Long id = null;
        Long userId = 26L;

        sessionRepository.findById(id).orElse(null);

        assertThrows(NotFoundException.class, ()->{
            sessionService.noLongerParticipate(id, userId);
        });

        verify(sessionRepository, times(2))
                .findById(id);
    }
    @Test
    @DisplayName("BadRequest")
    void whenIsNotAlreadyParticipate_thenThrowBadRequestException(){
        Long id = 27L;
        Long userId = 28L;

        Session session = new Session();
        session.setUsers(new ArrayList<>());

        User user = new User();

        when(sessionRepository.findById(id))
                .thenReturn(Optional.of(session));

        user.setId(id);
        session.getUsers().add(user);

        assertThrows(BadRequestException.class, ()->{
            sessionService.noLongerParticipate(id, userId);
        });

        verify(sessionRepository, times(1))
                .findById(id);
    }
}
