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

    @Test
    @Tag("delete_api/session/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 status code")
    public void deleteSession_withNonExistantId_returnsBadRequest() {
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

    @Test
    @Tag("delete_api/session/{id}/participate/{userId}")
    public void removeUserFromSessionWithValidIds_shouldRemoveTheUserFromSession() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(delete("/api/session/1/participate/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk());
        }
        // catch (JsonProcessingException e) {
        // e.printStackTrace();
        // }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        }
        // catch (JsonProcessingException e) {
        // e.printStackTrace();
        // }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
