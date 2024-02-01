package com.openclassrooms.starterjwt.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

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
 * @implNote The class utilizes JUnit 5 and Mockito for testing.
 *
 * @author Younes LAHOUITI
 * @version 1.0
 * @since 2024-01-05
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@DisplayName("Teacher controller test: api/teacher")
public class TeacherControllerUnitTests {
    /**
     * The logger instance for logging test-related information.
     */
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    /**
     * The controller under test
     */
    @InjectMocks
    private TeacherController teacherController;

    /**
     * Mocked service for simulating interactions with the teacher database.
     */
    @Mock
    private TeacherService teacherService;

    /**
     * Mocked mapper for converting between Teacher entities and DTOs.
     */
    @Mock
    private TeacherMapper teacherMapper;

    /**
     * Set up the test environment before each test case by initializing the
     * {@link TeacherController}.
     * This method creates a new instance of the {@code TeacherController} with the
     * provided mock dependencies,
     * including a teacher service, teacher mapper, and mockMvc for simulating HTTP
     * requests and responses.
     * The initialized controller is then used in each test case to evaluate the
     * behavior of the teacher-related endpoints.
     */
    @BeforeEach
    void setUp() {
        // Initialize the controller with mock dependencies
        teacherController = new TeacherController(teacherService, teacherMapper);
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
        // * Arrange

        Teacher mockTeacher1 = new Teacher();
        mockTeacher1.setId(1L);
        mockTeacher1.setFirstName("John");
        mockTeacher1.setLastName("Doe");
        mockTeacher1.setCreatedAt(LocalDateTime.now());
        mockTeacher1.setUpdatedAt(LocalDateTime.now());

        Teacher mockTeacher2 = new Teacher();
        mockTeacher2.setId(2L);
        mockTeacher2.setFirstName("Jane");
        mockTeacher2.setLastName("Doe");
        mockTeacher2.setCreatedAt(LocalDateTime.now());
        mockTeacher2.setUpdatedAt(LocalDateTime.now());

        List<Teacher> mockTeachers = Arrays.asList(
                mockTeacher1,
                mockTeacher2);

        when(teacherService.findAll()).thenReturn(mockTeachers);

        // Create an array to hold TeacherDto objects
        List<TeacherDto> expectedTeacherDtos = new ArrayList<>();

        // Iterate over the teachers and create corresponding TeacherDto objects
        for (int i = 0; i < mockTeachers.size(); i++) {
            Teacher teacher = mockTeachers.get(i);
            TeacherDto teacherDto = new TeacherDto();
            teacherDto.setId(teacher.getId());
            teacherDto.setFirstName(teacher.getFirstName());
            teacherDto.setLastName(teacher.getLastName());
            teacherDto.setCreatedAt(teacher.getCreatedAt());
            teacherDto.setUpdatedAt(teacher.getUpdatedAt());

            // Add the created TeacherDto to the array
            expectedTeacherDtos.add(teacherDto);
        }

        when(teacherMapper.toDto(mockTeachers)).thenReturn(expectedTeacherDtos); // You can create a mock TeacherDto as
        // needed

        // * Act
        ResponseEntity<?> result = teacherController.findAll();

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Boolean hasSameArrayLength = ((List<Teacher>) result.getBody()).size() == mockTeachers.size();
        assertTrue(hasSameArrayLength);
    }

    /**
     * Test case for retrieving a teacher with a valid ID from the database.
     * It should return the teacher details and a 200 status code.
     */
    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(HAPPY PATH) it should get the teacher from the database of the given id")
    public void getTeacherWithValidId_shouldReturnTheTeacher() {
        // * Arrange
        Long teacherId = 1L;
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(teacherId);
        mockTeacher.setFirstName("John");
        mockTeacher.setLastName("Doe");

        TeacherDto mockTeacherDto = new TeacherDto();
        mockTeacherDto.setId(mockTeacher.getId());
        mockTeacherDto.setFirstName(mockTeacher.getFirstName());
        mockTeacherDto.setLastName(mockTeacher.getLastName());

        when(teacherService.findById(teacherId)).thenReturn(mockTeacher);
        when(teacherMapper.toDto(mockTeacher)).thenReturn(mockTeacherDto);

        // * Act
        ResponseEntity<?> result = teacherController.findById(teacherId.toString());

        // * Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for retrieving a teacher with a non-existent ID from the database.
     * It should return a 404 status code.
     */
    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 status code")
    public void getTeacherWithNonExistentId_shouldReturnANotFoundError() {
        // * Arrange
        Long teacherId = 0L;
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(teacherId);
        mockTeacher.setFirstName("John");
        mockTeacher.setLastName("Doe");

        when(teacherService.findById(teacherId)).thenReturn(null);

        // * Act
        ResponseEntity<?> result = teacherController.findById(teacherId.toString());

        // * Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /**
     * Test case for retrieving a teacher with an invalid ID from the database.
     * It should return a 400 status code.
     */
    @Test
    @Tag("get_api/teacher/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 status code")
    public void getTeacherWithInvalidId_shouldReturnABadRequestError() {
        // * Act
        ResponseEntity<?> result = teacherController.findById("invalid");

        // * Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

}
