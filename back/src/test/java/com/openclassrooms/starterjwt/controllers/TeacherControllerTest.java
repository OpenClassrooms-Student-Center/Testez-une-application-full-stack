package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = TeacherController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                WebSecurityConfig.class,
                AuthTokenFilter.class
        })
})
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeacherService teacherService;
    @MockBean
    private TeacherMapper teacherMapper;

    // Beans nécessaires pour désactiver la sécurité
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void findById_ok() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(dto);

        mockMvc.perform(get("/api/teacher/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_notFound() throws Exception {
        when(teacherService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/{id}", "99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ok() throws Exception {
        Teacher t1 = new Teacher();
        Teacher t2 = new Teacher();

        TeacherDto d1 = new TeacherDto();
        d1.setId(1L);
        d1.setFirstName("Alice");
        d1.setLastName("Smith");

        TeacherDto d2 = new TeacherDto();
        d2.setId(2L);
        d2.setFirstName("Bob");
        d2.setLastName("Brown");

        when(teacherService.findAll()).thenReturn(Arrays.asList(t1, t2));
        when(teacherMapper.toDto(anyList())).thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
