package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntTests {

    @Autowired
    private MockMvc mockMvc;

    // Test method to find a teacher by ID
    @Test
    @WithMockUser(username = "Admin", password = "test!1234", roles = "ADMIN")
    public void teacherFindByIdOk() throws Exception {
        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn();
    }

    // Test method to find all teachers
    @Test
    @WithMockUser(username = "Admin", password = "test!1234", roles = "ADMIN")
    public void teacherFindAllOk() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andReturn();
    }
}
