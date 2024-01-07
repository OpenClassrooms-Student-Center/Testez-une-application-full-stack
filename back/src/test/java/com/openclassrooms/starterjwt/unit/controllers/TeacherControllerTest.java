package com.openclassrooms.starterjwt.unit.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

/**
 * Controller test class for the {@link TeacherController}, testing the
 * endpoints under "api/teacher".
 * It includes tests for retrieving all teachers, getting a teacher by ID, and
 * handling edge cases for invalid IDs.
 *
 * @implNote This class uses Mockito for mocking dependencies and performs tests
 *           using the Spring MVC Test framework.
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link TeacherController} in handling teacher-related operations.
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
@DisplayName("Teacher controller test: api/teacher")
public class TeacherControllerTest {
    /**
     * The logger instance for logging test-related information.
     */
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    /**
     * The controller under test, annotated with {@link InjectMocks}.
     */
    @MockBean
    private TeacherController teacherController;

    /**
     * A mock repository for simulating interactions with the user database.
     */
    @MockBean
    private UserRepository userRepository;

    /**
     * Mock utility class for handling JWT-related operations.
     */
    @MockBean
    private JwtUtils jwtUtils;

    /**
     * Mock encoder for handling password-related operations.
     */
    @MockBean
    private PasswordEncoder passwordEncoder;

    /**
     * The mockMvc instance for simulating HTTP requests in the tests.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * The timestamp when the test suite started, for measuring test duration.
     */
    static private Instant startedAt;

    /**
     * Initializes the starting time before all the test suites.
     */
    @BeforeAll
    static public void initStartingTime() {
        logger.info("Before all the test suites");
        startedAt = Instant.now();
    }

    /**
     * Displays the duration of the test suites after all the tests have run.
     */
    @AfterAll
    static public void showTestDuration() {
        logger.info("After all the test suites");
        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();
        logger.info(MessageFormat.format("Duration of the tests : {0} ms", duration));
    }

    /**
     * Test case for retrieving all teachers from the database.
     * It should return a JSON array with all teachers and a 200 status code.
     *
     * @throws Exception if there is any unexpected exception during the test.
     */
    @Test
    @Tag("get_api/teacher")
    @DisplayName("(HAPPY PATH) it should get all teachers from the database")
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

    /**
     * Test case for retrieving a teacher with a valid ID from the database.
     * It should return the teacher details and a 200 status code.
     */
    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(HAPPY PATH) it should get the teacher from the database of the given id")
    public void getTeacherWithValidId_shouldReturnTheTeacher() {
        try {
            // Arrange
            Teacher mockedTeacher = new Teacher();
            mockedTeacher.setId(1L);
            mockedTeacher.setFirstName("John");
            mockedTeacher.setLastName("Doe");

            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/teacher/1")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                    .andExpect(jsonPath("$.firstName", Matchers.equalTo("Margot")))
                    .andExpect(jsonPath("$.lastName", Matchers.equalTo("DELAHAYE")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for retrieving a teacher with a non-existent ID from the database.
     * It should return a 404 status code.
     */
    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 status code")
    public void getTeacherWithNonExistentId_shouldReturnANotFoundError() {
        try {
            // Arrange
            // when(userRepository.findById(0L)).thenReturn(Optional.empty());

            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/teacher/0")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for retrieving a teacher with an invalid ID from the database.
     * It should return a 400 status code.
     */
    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 status code")
    public void getTeacherWithInvalidId_shouldReturnABadRequestError() {
        try {
            // Arrange
            // Act
            ResultActions result = mockMvc.perform(
                    get("/api/teacher/invalid")
                            .contentType(MediaType.APPLICATION_JSON));

            // Assert
            result.andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
