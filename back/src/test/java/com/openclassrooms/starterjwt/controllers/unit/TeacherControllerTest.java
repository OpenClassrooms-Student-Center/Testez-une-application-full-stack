package com.openclassrooms.starterjwt.controllers.unit;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherController teacherController;

    // Test to find a teacher by ID
    @Test
    @WithMockUser(roles = "ADMIN")
    public void teacherFindByIdOk() {
        long id = 1L;
        Teacher teacher = new Teacher();
        // define the behavior of the teacherService and the teacherMapper
        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(new TeacherDto());

        // call the findById method of the teacherController
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(id));
        // verify that the response status code is 200
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    // Test to not find a teacher by ID with a non-numeric ID
    @Test
    @WithMockUser(roles = "ADMIN")
    public void teacherFindByIdBadRequest() {
        String invalidId = "notanumber";
        // call the findById method of the teacherController
        ResponseEntity<?> responseEntity = teacherController.findById(invalidId);
        // verify that the response status code is 400
        assertEquals(HttpStatus.BAD_REQUEST.value(),
                responseEntity.getStatusCodeValue());
    }

    // Test to not find a teacher by inexistent ID
    @Test
    @WithMockUser(roles = "ADMIN")
    public void teacherFindByIdNotFound() {
        long id = 0L;
        // define the findById method of the teacherService that returns null
        when(teacherService.findById(id)).thenReturn(null);
        // call the findById method of the teacherController
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(id));
        // verify that the response status code is 404
        assertEquals(HttpStatus.NOT_FOUND.value(),
                responseEntity.getStatusCodeValue());
    }

    // Test to find all teachers
    @Test
    @WithMockUser(roles = "ADMIN")
    public void teacherFindAllOk() {
        // create a list of teachers
        List<Teacher> teachers = new ArrayList<>();
        // Add a teacher to the list
        teachers.add(new Teacher());
        // define the call of the findAll method of the teacherService that returns the
        // list of teachers
        when(teacherService.findAll()).thenReturn(teachers);
        // define the call of the toDto method of the teacherMapper that returns a list
        // of teacherDtos
        when(teacherMapper.toDto(teachers)).thenReturn(Arrays.asList(new TeacherDto()));
        // call the findAll method of the teacherController
        ResponseEntity<?> responseEntity = teacherController.findAll();
        // verify that the response status code is 200
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
