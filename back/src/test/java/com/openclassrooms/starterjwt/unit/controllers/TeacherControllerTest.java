package com.openclassrooms.starterjwt.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Teacher controller test: api/teacher")
public class TeacherControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

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

    public TeacherControllerTest(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Tag("get_api/teacher")
    @DisplayName("(HAPPY PATH) it should get the session from the database of the given id")
    public void getAllTeachers_shouldReturnAllTheTeachers() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/teacher")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(HAPPY PATH) it should get the session from the database of the given id")
    public void getTeacherWithValidId_shouldReturnTheTeacher() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(EDGE CASE) it should get the session from the database of the given id")
    public void getTeacherWithNonExistentId_shouldReturnANotFoundError() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(EDGE CASE) it should get the session from the database of the given id")
    public void getTeacherWithInvalidId_shouldReturnABadRequestError() {
        // Arrange

        // Act

        // Assert

    }
}
