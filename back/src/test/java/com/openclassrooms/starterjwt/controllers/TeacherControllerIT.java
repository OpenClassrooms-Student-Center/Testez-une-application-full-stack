// src/test/java/com/openclassrooms/starterjwt/controllers/TeacherControllerIT.java
package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
    }

    @Test
    void findAll_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findAll_shouldReturnListOfTeachers() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setLastName("Dupont");
        teacher1.setFirstName("Jean");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setLastName("Martin");
        teacher2.setFirstName("Marie");
        teacherRepository.save(teacher2);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void findById_shouldReturnTeacher() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setLastName("Durand");
        teacher.setFirstName("Paul");
        teacher = teacherRepository.save(teacher);

        mockMvc.perform(get("/api/teacher/" + teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Durand"))
                .andExpect(jsonPath("$.firstName").value("Paul"));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/abc"))
                .andExpect(status().isBadRequest());
    }
}