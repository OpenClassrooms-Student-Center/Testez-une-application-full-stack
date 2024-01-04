package com.openclassrooms.starterjwt.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Session controller test: api/session")
public class SessionControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    static private Instant startedAt;

    @BeforeAll
    static public void initStartingTime() {
        logger.info("Before all the test suites");

        startedAt = Instant.now();
    }

    @AfterAll
    static public void showTestDuration() {
        logger.info("After all the test suites");

        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();

        logger.info(MessageFormat.format("Duration of the tests : {0} ms", duration));
    }

    public SessionControllerTest(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    @Tag("post_api/session")
    public void createSessionWithValidSessionDto_createsNewSession() {
        try {
            // Arrange
            LocalDateTime now = LocalDateTime.now();

            Teacher teacher = new Teacher();
            teacher
                    .setId(1L)
                    .setLastName("DELAHAYE")
                    .setFirstName("Margot")
                    .setCreatedAt(now)
                    .setUpdatedAt(now);

            Session session = Session.builder()
                    .name("session-1")
                    .teacher(teacher)
                    .users(null)
                    .description("My description for the test")
                    .date(new Date())
                    .build();

            SessionDto sessionDto = new SessionDto();
            sessionDto.setTeacher_id(session.getTeacher().getId());
            sessionDto.setName(session.getName());
            sessionDto.setDate(session.getDate());
            // sessionDto.setCreatedAt(session.getCreatedAt());
            // sessionDto.setUpdatedAt(session.getUpdatedAt());

            // Act
            ResultActions result = mockMvc.perform(post("/api/session/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sessionDto)));

            result.andExpect(status().isOk());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert
    }

    @Test
    @Tag("put_api/session/{id}")
    public void updateSession_withInvalidId_returnsBadRequest() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @Tag("delete_api/session/{id}")
    public void deleteSession_withInvalidId_returnsBadRequest() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithValidIds_shouldRemoveTheUserFromSession() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithInvalidIds_shouldRemoveTheUserFromSession() {
        // Arrange

        // Act

        // Assert
    }
}
