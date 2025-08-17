package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    private TeacherRepository teacherRepository;
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherRepository = mock(TeacherRepository.class);
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void testFindAll() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(teacher1));
        assertTrue(result.contains(teacher2));
    }

    @Test
    void testFindById_TeacherExists() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(id);

        assertNotNull(result);
        assertEquals(teacher, result);
    }

    @Test
    void testFindById_TeacherDoesNotExist() {
        Long id = 2L;
        when(teacherRepository.findById(id)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(id);

        assertNull(result);
    }
}