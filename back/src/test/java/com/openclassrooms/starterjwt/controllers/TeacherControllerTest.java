package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_shouldReturnTeacherDto() {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teacherDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound() {
        when(teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void findById_shouldReturnBadRequest() {
        ResponseEntity<?> response = teacherController.findById("abc");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void findAll_shouldReturnListOfTeacherDto() {
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        List<TeacherDto> teacherDtos = Arrays.asList(new TeacherDto(), new TeacherDto());
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teacherDtos, response.getBody());
    }
}