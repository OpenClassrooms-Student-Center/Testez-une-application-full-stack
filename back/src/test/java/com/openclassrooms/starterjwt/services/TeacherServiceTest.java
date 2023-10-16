package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @InjectMocks
    TeacherService teacherService;
    @Mock
    TeacherRepository teacherRepository;
    @BeforeEach
    void setUp(){
        teacherService = new TeacherService(
                teacherRepository);
    }

    @Test
    @DisplayName("findAll")
    void whenTeachListIsPresent_thenReturnTeachList(){
        List<Teacher> teacherList = new ArrayList<>();

        when(teacherRepository.findAll()).thenReturn(
                teacherList);

        List<Teacher> requestTeachList = teacherService.findAll();

        assertEquals("List Of Teacher",requestTeachList, teacherList);
        verify(teacherRepository, times(1))
                .findAll();
    }

    @Test
    @DisplayName("finById")
    void whenTeacherIdentityIsFound_thenReturnTeacher(){
        Long id = 28L;
        Teacher teacher = new Teacher();

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        Teacher findTeacher = teacherService.findById(id);

        assertEquals("Teacher id", teacher, findTeacher);
        verify(teacherRepository, times(1))
                .findById(id);
    }

    @Test
    @DisplayName("null")
    void whenTeacherIsNull_thenReturnNull(){
        Long id = null;
        Teacher teacher = new Teacher();

        when(teacherRepository.findById(id))
                .thenReturn(Optional.of(teacher));

        Teacher IsTeacherNull = teacherService.findById(id);

        verify(teacherRepository).findById(id);
        assertEquals("Null", teacher, IsTeacherNull);
    }
}
