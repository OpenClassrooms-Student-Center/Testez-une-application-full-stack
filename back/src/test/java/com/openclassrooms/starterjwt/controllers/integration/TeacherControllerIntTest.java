package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwt;

    @BeforeEach
    void setUp() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testTeacherFindById() throws Exception {
        mockMvc.perform(get("/api/teacher/1")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    public void testTeacherGetAll() throws Exception {
        mockMvc.perform(get("/api/teacher")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

}