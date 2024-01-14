package com.openclassrooms.starterjwt.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;

/**
 * Unit tests for the {@link TeacherService} class.
 * 
 * It tests the functionality of finding teachers by ID and retrieving all
 * teachers.
 * 
 * The tests use Mockito to mock the TeacherRepository dependency.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TeacherServiceUnitTest {

    /**
     * A mock instance of the TeacherService class.
     */
    @Mock
    private TeacherService teacherService;

    /**
     * A mock instance of the TeacherRepository class.
     */
    @Mock
    private TeacherRepository teacherRepository;

    /**
     * Annotated with {@code @BeforeEach}, this method is executed before each
     * test
     * case. It
     * sets up the necessary dependencies and initializes the session object.
     */
    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    /**
     * Tests the {@code findAll()} method of the TeacherService class.
     * It mocks the {@code findAll()} method of the teacherRepository
     * to return a list of teachers and verifies that the {@code findAll()}
     * method is called. It asserts that the returned list of teachers
     * is equal to the original list of teachers.
     */
    @Test
    @Tag("TeacherService.findAll()")
    @DisplayName("Find all teacher")
    public void testFindAllTeachers() {
        // * Arrange
        LocalDateTime localDateTime = LocalDateTime.now();

        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        Teacher teacher2 = new Teacher();
        teacher2
                .setId(2L)
                .setLastName("THIERCELIN")
                .setFirstName("Hélène")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        teachers.add(teacher1);
        teachers.add(teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        // * Act
        List<Teacher> result = teacherService.findAll();

        // * Assert
        verify(teacherRepository).findAll();

        assertEquals(teachers, result);
    }

    /**
     * Tests the {@code findById()} method of the TeacherService class.
     * It mocks the {@code findById()} method of the teacherRepository
     * to return a teacher object and verifies that the {@code findById()}
     * method is called. It asserts that the returned teacher object
     * is equal to the original teacher object.
     */
    @Test
    @Tag("TeacherService.findById()")
    @DisplayName("Find teacher by ID → found")
    public void testFindTeacherById() {
        // * Arrange
        LocalDateTime localDateTime = LocalDateTime.now();

        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(localDateTime)
                .setUpdatedAt(localDateTime);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        // * Act
        Teacher result = teacherService.findById(teacherId);

        // * Assert
        verify(teacherRepository).findById(teacherId);

        assertEquals(teacher, result);
    }

    /**
     * Tests the {@code findById()} method of the TeacherService class when the
     * teacher is not found.
     * It mocks the {@code findById()} method of the teacherRepository
     * to return an empty optional and verifies that the {@code findById()}
     * method is called. It asserts that the returned teacher object is null.
     */
    @Test
    @Tag("TeacherService.findById()")
    @DisplayName("Find teacher by ID → not found")
    public void testFindTeacherByIdNotFound() {
        // * Arrange
        Long teacherId = 1L;

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // * Act
        Teacher result = teacherService.findById(teacherId);

        // * Assert
        verify(teacherRepository).findById(teacherId);

        assertNull(result);
    }
}
