package com.openclassrooms.starterjwt.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;

/**
 * This class contains test cases for the {@link SessionController} class,
 * focusing on API endpoints related to session management.
 *
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link SessionController} in handling teacher-related operations.
 * @implNote The class utilizes JUnit 5 and Mockito for testing.
 *
 * @see UserRepository
 * @see JwtUtils
 * @see PasswordEncoder
 * @see WebApplicationContext
 *
 * @author Younes LAHOUITI
 * @version 1.0
 * @since 2024-01-05
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Session controller test: api/session")
public class SessionControllerTest {

    /**
     * The controller under test, injected with mock dependencies.
     */
    @Mock
    private SessionController sessionController;

    /**
     * Mocked authentication manager for simulating user authentication.
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Mocked user repository for handling user data during tests.
     */
    @Mock
    private SessionRepository sessionRepository;

    /**
     * Mocked session service for handling session-related business logic.
     */
    @Mock
    private SessionService sessionService;

    /**
     * Mocked session mapper for converting between entities and DTOs.
     */
    @Mock
    private SessionMapper sessionMapper;

    /**
     * Set up the test environment before each test case by initializing the
     * {@link SessionController}.
     * This method creates a new instance of the {@code SessionController} with the
     * provided mock dependencies,
     * including a session service, session mapper, and mockMvc for simulating HTTP
     * requests and responses.
     * The initialized controller is then used in each test case to evaluate the
     * behavior of the session-related endpoints.
     */
    @BeforeEach
    void setUp() {
        // Initialize the controller with mock dependencies
        sessionController = new SessionController(sessionService, sessionMapper);
    }

    /**
     * Test case for retrieving a session with the given existing ID.
     * It should return a 200 status code
     */
    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("(HAPPY PATH) it should get the session from the database of the given id")
    public void getSessionById_returnsSessionWithGivenId() throws Exception {
        // Arrange
        Long sessionId = 1L;
        Session mockSession = new Session();

        when(sessionService.getById(sessionId)).thenReturn(mockSession);

        // Act
        ResponseEntity<?> result = sessionController.findById(sessionId.toString());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for retrieving a session from the database with an invalid ID.
     * It should return a 404 error status code.
     */
    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 error")
    public void getSessionById_withInvalidId_returnsBadRequest() {
        // Arrange
        Long sessionId = 0L;

        when(sessionService.getById(sessionId)).thenReturn(null);

        // Act
        ResponseEntity<?> result = sessionController.findById(sessionId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /**
     * Test case for retrieving all sessions from the database.
     * It should return a list of sessions as an empty or full array with a 200
     * status code.
     */
    @Test
    @Tag("get_api/session")
    @DisplayName("(HAPPY PATH) it should retrieve all the sessions from the database as an empty or full array")
    public void getAllSessions_returnsListOfAllSessions() {
        // Arrange
        // Act
        ResponseEntity<?> result = sessionController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for creating a new session with valid data.
     * It should return a 201 status code upon successful creation.
     */
    @Test
    @Tag("post_api/session")
    @DisplayName("(HAPPY PATH) it should successfully create the session and return a 201 status code")
    public void createSessionWithValidSessionDto_createsNewSession() {
        // Arrange
        String isoString = "2023-12-30T10:27:21";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        Session session = Session.builder()
                .id(1L)
                .name("session-1")
                .teacher(teacher)
                .users(null)
                .description("My description for the test")
                .date(new Date())
                .build();

        List<Long> userIdsList = new ArrayList<Long>();
        userIdsList.add(1L);
        userIdsList.add(2L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(session.getId());
        sessionDto.setTeacher_id(session.getTeacher().getId());
        sessionDto.setName(session.getName());
        sessionDto.setUsers(userIdsList);
        sessionDto.setDate(session.getDate());
        sessionDto.setDescription(session.getDescription());
        sessionDto.setCreatedAt(session.getCreatedAt());
        sessionDto.setUpdatedAt(session.getUpdatedAt());

        when(sessionService.create(sessionMapper.toEntity(sessionDto))).thenReturn(session);
        // Act
        ResponseEntity<?> result = sessionController.create(sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for updating an existing session with valid data.
     * It should return a 200 status code upon successful update.
     */
    @Test
    @Tag("put_api/session/{id}")
    @DisplayName("(HAPPY PATH) it should update the existing session and return a 200 status code")
    public void updateSession_withValidId_returnsUpdatedSession() {
        // Arrange
        String isoString = "2023-12-30T10:27:21";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        LocalDateTime localDateTime = LocalDateTime.parse(isoString, formatter);

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        Session session = Session.builder()
                .id(1L)
                .name("updated-session-1")
                .teacher(teacher)
                .users(null)
                .description("My updated description for the test")
                .date(new Date())
                .build();

        List<Long> userIdsList = new ArrayList<Long>();
        userIdsList.add(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(session.getId());
        sessionDto.setTeacher_id(session.getTeacher().getId());
        sessionDto.setName(session.getName());
        sessionDto.setUsers(userIdsList);
        sessionDto.setDate(session.getDate());
        sessionDto.setDescription(session.getDescription());
        sessionDto.setCreatedAt(session.getCreatedAt());
        sessionDto.setUpdatedAt(session.getUpdatedAt());

        when(sessionService.update(sessionDto.getId(), sessionMapper.toEntity(sessionDto))).thenReturn(session);

        // Act
        ResponseEntity<?> result = sessionController.update(sessionDto.getId().toString(), sessionDto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for deleting an existing session with a valid ID.
     * It should return a 200 status code upon successful deletion.
     */
    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(HAPPY PATH) it should delete the session and return a 200 status code")
    public void deleteSession_withValidId_returnsBadRequest() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        Session mockSession = new Session();

        when(sessionService.getById(sessionDto.getId())).thenReturn(mockSession);
        // Act
        ResponseEntity<?> result = sessionController.save(sessionDto.getId().toString());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for deleting a session with a non-existent ID.
     * It should return a 404 status code.
     */
    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 status code")
    public void deleteSession_withNonExistentId_returnsNotFound() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(0L);

        when(sessionService.getById(sessionDto.getId())).thenReturn(null);
        // Act
        ResponseEntity<?> result = sessionController.save(sessionDto.getId().toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /**
     * Test case for deleting a session with an invalid ID.
     * It should return a 400 status code.
     */
    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 status code")
    public void deleteSession_withInvalidId_returnsBadRequest() {
        // Arrange
        // Act
        ResponseEntity<?> result = sessionController.save("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    /**
     * Test case for adding a user to a session with valid IDs.
     * It should return a 201 status code.
     */
    @Test
    @Tag("post_api/session/{id}/participate/{userId}")
    @DisplayName("(HAPPY PATH) it should create the session and return a 200 status code")
    public void addUserToSessionWithValidIds_shouldAddTheUserToSession() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setTeacher_id(1L);

        // Act
        ResponseEntity<?> result = sessionController.participate(sessionDto.getId().toString(),
                sessionDto.getTeacher_id().toString());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
