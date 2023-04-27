package com.openclassrooms.starterjwt.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class TeacherServiceTest {

    @Mock
    TeacherRepository teacherRepository;

    @Test
    public void testFindAllTeachers() {
        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacherList.add(teacher);
        when(teacherRepository.findAll()).thenReturn(teacherList);
        TeacherService teacherService = new TeacherService(teacherRepository);
        List<Teacher> foundTeachers = teacherService.findAll();
        Assertions.assertEquals(1, foundTeachers.size());
    }

    @Test
    public void testGetTeacherById() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        TeacherService teacherService = new TeacherService(teacherRepository);
        Teacher foundTeacher = teacherService.findById(teacher.getId());
        Assertions.assertEquals(teacher.getId(), foundTeacher.getId());
    }

}
