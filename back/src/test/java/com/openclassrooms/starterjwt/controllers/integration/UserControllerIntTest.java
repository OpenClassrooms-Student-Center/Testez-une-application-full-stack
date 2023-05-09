package com.openclassrooms.starterjwt.controllers.integration;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntTest {

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
    public void testUserFindByIdOK() throws Exception {
        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

    }

    @Test
    public void testUserFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/0")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testTeacherFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/notanumber")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());

    }

    // commented to prevent user deletion
    // @Test
    // public void testUserDeleteOK() throws Exception {
    // mockMvc.perform(delete("/api/user/1")
    // .header("Authorization", "Bearer " + jwt))
    // .andExpect(status().isOk());
    // }

    @Test
    public void testUserDeleteBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/notgoodId")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUserDeleteUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/user/1")
                .header("Authorization", "Bearer invalid_jwt"))
                .andExpect(status().isUnauthorized());
    }

}