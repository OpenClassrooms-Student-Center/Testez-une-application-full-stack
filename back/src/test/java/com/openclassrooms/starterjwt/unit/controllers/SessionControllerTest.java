package com.openclassrooms.starterjwt.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

/**
 * This class contains test cases for the {@link SessionController} class,
 * focusing on API endpoints related to session management.
 *
 * @implNote This class uses Mockito for mocking dependencies and performs tests
 *           using the Spring MVC Test framework.
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link SessionController} in handling teacher-related operations.
 * @implNote The class utilizes JUnit 5, Mockito, and Spring's MockMvc for
 *           testing.
 *
 * @see MockMvc
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
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Session controller test: api/session")
public class SessionControllerTest {

    /**
     * The logger instance for logging test-related information.
     */
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    /**
     * The controller under test, injected with mock dependencies.
     */
    @MockBean
    private SessionController sessionController;

    /**
     * Mocked authentication manager for simulating user authentication.
     */
    @MockBean
    private AuthenticationManager authenticationManager;

    /**
     * Mocked user repository for handling user data during tests.
     */
    @MockBean
    private UserRepository userRepository;

    /**
     * Mocked JWT utility class for working with JSON Web Tokens.
     */
    @MockBean
    private JwtUtils jwtUtils;

    /**
     * Mocked password encoder for encoding and decoding passwords.
     */
    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * The MockMvc instance for simulating HTTP requests and responses.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * ObjectMapper for converting objects to JSON and vice versa.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * The starting time for test suites to calculate the duration of tests.
     */
    static private Instant startedAt;

    /**
     * Initializes the starting time before all test suites are executed.
     */
    @BeforeAll
    static public void initStartingTime() {
        logger.info("Before all the test suites");
        startedAt = Instant.now();
    }

    /**
     * Displays the duration of all test suites after they have been executed.
     */
    @AfterAll
    static public void showTestDuration() {
        logger.info("After all the test suites");
        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();
        logger.info(MessageFormat.format("Duration of the tests: {0} ms", duration));
    }

    /**
     * Test case for retrieving a session with the given existing ID.
     * It should return a 200 status code
     */
    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("(HAPPY PATH) it should get the session from the database of the given id")
    public void getSessionById_returnsSessionWithGivenId() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for retrieving a session from the database with an invalid ID.
     * It should return a 400 error status code.
     */
    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 error")
    public void getSessionById_withInvalidId_returnsBadRequest() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/session/invalid")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for retrieving a session from the database with a non-existent ID.
     * It should return a 404 error status code.
     */
    @Test
    @Tag("get_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 error")
    public void getSessionById_withNonExistentId_returnsBadRequest() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/session/0")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/session/")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for creating a new session with valid data.
     * It should return a 201 status code upon successful creation.
     */
    @Test
    @Tag("post_api/session")
    @DisplayName("(HAPPY PATH) it should successfully create the session and return a 201 status code")
    public void createSessionWithValidSessionDto_createsNewSession() {
        try {
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

            // Act
            ResultActions result = mockMvc.perform(post("/api/session/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            // Assert
            result.andExpect(status().isOk());
        } catch (

        JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for creating a new session with invalid data.
     * It should return a 400 status code.
     */
    @Test
    @Tag("post_api/session")
    @DisplayName("(EDGE CASE) it should not create a session and return a 400 status code")
    public void createSessionWithInvalidSessionDto_createsNewSession() {
        try {
            // Arrange
            SessionDto sessionDto = new SessionDto();

            // Act
            ResultActions result = mockMvc.perform(post("/api/session/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for updating an existing session with valid data.
     * It should return a 200 status code upon successful update.
     */
    @Test
    @Tag("put_api/session/{id}")
    @DisplayName("(HAPPY PATH) it should update the existing session and return a 200 status code")
    public void updateSession_withValidId_returnsUpdatedSession() {
        try {
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

            // Act
            ResultActions result = mockMvc.perform(put("/api/session/" + sessionDto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            // Assert
            result.andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Test case for updating an existing session with an invalid payload.
     * It should return a 400 status code.
     */
    @Test
    @Tag("put_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 status code")
    public void updateSession_withInvalidId_returnsBadRequest() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(put("/api/session/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Test case for deleting an existing session with a valid ID.
     * It should return a 200 status code upon successful deletion.
     */
    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(HAPPY PATH) it should delete the session and return a 200 status code")
    public void deleteSession_withValidId_returnsBadRequest() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for deleting a session with a non-existent ID.
     * It should return a 404 status code.
     */
    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 status code")
    public void deleteSession_withNonExistentId_returnsNotFound() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/0")
                    .contentType(MediaType.APPLICATION_JSON));
            // Assert
            result.andExpect(status().isNotFound());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for deleting a session with an invalid ID.
     * It should return a 400 status code.
     */
    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 status code")
    public void deleteSession_withInvalidId_returnsBadRequest() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/invalid")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for adding a user to a session with valid IDs.
     * It should return a 201 status code.
     */
    @Test
    @Tag("post_api/session/{id}/participate/{userId}")
    @DisplayName("(HAPPY PATH) it should delete the session and return a 200 status code")
    public void addUserToSessionWithValidIds_shouldAddTheUserToSession() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(post("/api/session/1/participate/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isCreated());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for adding a user to a session with invalid session ID.
     * It should return a 404 status code.
     */
    @Test
    @Tag("post_api/session/{id}/participate/{userId}")
    @DisplayName("(EDGE CASE) it should return a 404 status code")
    public void addUserToSessionWithInvalidSessionId_shouldReturnNotFoundError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(post("/api/session/0/participate/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isNotFound());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("post_api/session/{id}/participate/{userId}")
    public void addUserToSessionWithInvalidUserId_shouldReturnAnError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(post("/api/session/1/participate/0")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isNotFound());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for removing a user from a session with invalid session ID.
     * It should return a 404 status code.
     */
    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithInvalidSessionId_shouldReturnNotFoundError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/0/participate/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isNotFound());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for removing a user from a session with valid IDs.
     * It should return a 200 status code upon successful removal.
     */
    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    @DisplayName("(HAPPY PATH) it should delete the session and return a 200 status code")
    public void removeUserFromSessionWithValidIds_shouldRemoveTheUserFromSession() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/1/participate/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for removing a user from a session with invalid IDs.
     * It should return a 404 status code.
     */
    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithInvalidIds_shouldReturnAnError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/1/participate/0")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isNotFound());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
