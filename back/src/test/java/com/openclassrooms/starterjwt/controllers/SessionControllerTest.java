package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionControllerTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    private SessionController sessionController;

    List<Session> sessions;

    Session session;
    SessionDto sessionDto;

    Teacher teacher;

    List<User> users;
    User thomas;

    @BeforeEach
    void setUp() {
        sessionController = new SessionController(sessionService, sessionMapper);
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("teacher")
                .lastName("teacher")
                .build();
        thomas = User.builder()
                .id(1L)
                .firstName("Thomas")
                .lastName("Robert")
                .email("thomas@robert.com")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        users = List.of(thomas);
        session = Session.builder()
                .id(1L)
                .name("session")
                .teacher(teacher)
                .date(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .description("session description")
                .createdAt(null)
                .updatedAt(null)
                .users(users)
                .build();
        sessionDto = new SessionDto(1L, "session", Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), 1L, "session description", users.stream().map(User::getId).collect(Collectors.toList()), null, null );
    }

    @Test
    void findById() {
        //Given
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        //When
        ResponseEntity<?> response = sessionController.findById("1");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        SessionDto sessionResponse = (SessionDto) response.getBody();
        assertThat(sessionResponse.getId()).isEqualTo(1L);
        assertThat(sessionResponse.getName()).isEqualTo("session");
        assertThat(sessionResponse.getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.getDate()).isNotNull();
    }

    @Test
    void findAll() {
        //Given
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(List.of(sessionDto));
        //When
        ResponseEntity<?> response = sessionController.findAll();
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        List<SessionDto> sessionResponse = (List<SessionDto>) response.getBody();
        assertThat(sessionResponse.get(0).getId()).isEqualTo(1L);
        assertThat(sessionResponse.get(0).getName()).isEqualTo("session");
        assertThat(sessionResponse.get(0).getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.get(0).getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.get(0).getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.get(0).getDate()).isNotNull();
    }

    @Test
    @Tag("Create")
    @DisplayName("Should return 200 when session is created")
    void create() {
        //Given
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        //When
        ResponseEntity<?> response = sessionController.create(sessionDto);
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        SessionDto sessionResponse = (SessionDto) response.getBody();
        assertThat(sessionResponse.getId()).isEqualTo(1L);
        assertThat(sessionResponse.getName()).isEqualTo("session");
        assertThat(sessionResponse.getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.getDate()).isNotNull();
    }

    @Test
    @Tag("Update")
    @DisplayName("Should return 200 when session is updated")
    void update() {
        //Given
        when(sessionService.update(session.getId(), session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        //When
        ResponseEntity<?> response = sessionController.update(session.getId().toString(), sessionDto);
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        SessionDto sessionResponse = (SessionDto) response.getBody();
        assertThat(sessionResponse.getId()).isEqualTo(1L);
        assertThat(sessionResponse.getName()).isEqualTo("session");
        assertThat(sessionResponse.getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.getDate()).isNotNull();
    }

    @Test
    @Tag("Update")
    @DisplayName("Should return 400 when session id is not a number")
    void update_400() {
        //Given
        //When
        ResponseEntity<?> response = sessionController.update("abc", sessionDto);
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Tag("Delete")
    @Test
    @DisplayName("Should return 200 when session is deleted")
    void save() {
        //Given
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        when(sessionService.getById(session.getId())).thenReturn(session);
        //When
        ResponseEntity<?> response = sessionController.save(session.getId().toString());
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Tag("Delete")
    @Test
    @DisplayName("Should return 400 when session id is not a number")
    void save_400() {
        //Given
        //When
        ResponseEntity<?> response = sessionController.save("abc");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Tag("Delete")
    @Test
    @DisplayName("Should return 404 when session id is not found")
    void save_404() {
        //Given
        when(sessionService.getById(session.getId())).thenReturn(null);
        //When
        ResponseEntity<?> response = sessionController.save(session.getId().toString());
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Tag("Participate")
    @Test
    @DisplayName("Should return 200 when user participate to a session")
    void participate() {
        //Given
        when(sessionService.getById(session.getId())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        //When
        ResponseEntity<?> response = sessionController.participate(session.getId().toString(), thomas.getId().toString());
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Tag("Participate")
    @Test
    @DisplayName("Should return 400 when user id is not a number")
    void participate_400() {
        //Given
        //When
        ResponseEntity<?> response = sessionController.participate("abc", thomas.getId().toString());
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Tag("noLongerParticipate")
    @Test
    @DisplayName("Should return 200 when user no longer participate to a session")
    void noLongerParticipate() {
        //Given
        when(sessionService.getById(session.getId())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        //When
        ResponseEntity<?> response = sessionController.noLongerParticipate(session.getId().toString(), thomas.getId().toString());
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Tag("noLongerParticipate")
    @Test
    @DisplayName("Should return 400 when user id or session id is not a number")
    void noLongerParticipate_400() {
        //Given
        //When
        ResponseEntity<?> response = sessionController.noLongerParticipate("abc", thomas.getId().toString());
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
}