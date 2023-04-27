package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

class TeacherControllerTest {

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherService;

    @Test
    public void testTeacherFindByIdOK() {

        Long id = 1L;
        String name = "Teacher name";
        Teacher teacher = Teacher
                .builder()
                .id(id)
                .lastName("Doe")
                .build();

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(id);
        teacherDto.setLastName(name);

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findById(id.toString());

        verify(teacherService).findById(id);
        verify(teacherMapper).toDto(teacher);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), teacherDto);

    }

    @Test
    public void testTeacherFindByIdNotFound() {

        Long id = 1L;

        when(teacherService.findById(id)).thenReturn(null);

        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findById(id.toString());

        verify(teacherService).findById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), null);

    }

    @Test
    public void testTeacherFindByIdBadRequest() {

        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> response = teacherController.findById("notgoodId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testTeacherFindAll() {

        // Create a list of sessions
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher());
        teachers.add(new Teacher());
        teachers.add(new Teacher());

        // Create a list of session DTOs
        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(new TeacherDto());
        teacherDtos.add(new TeacherDto());
        teacherDtos.add(new TeacherDto());

        // Mock the sessionService's findAll method to return the list of sessions
        when(teacherService.findAll()).thenReturn(teachers);

        // Mock the sessionMapper's toDto method to return the list of session DTOs
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // Call the controller's findAll method
        TeacherController teacherController = new TeacherController(teacherService, teacherMapper);
        ResponseEntity<?> responseEntity = teacherController.findAll();

        // Verify that the response entity's status code is 200 OK
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Verify that the response entity's body is the list of session DTOs
        assertEquals(teacherDtos, responseEntity.getBody());

    }

}