package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                WebSecurityConfig.class,
                AuthTokenFilter.class
        })
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private AuthEntryPointJwt unauthorizedHandler;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("pass");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void findById_ok() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        mockMvc.perform(get("/api/user/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findById_notFound() throws Exception {
        when(userService.findById(2L)).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", "2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findById_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_ok() throws Exception {
        when(userService.findById(1L)).thenReturn(user);
        doNothing().when(userService).delete(1L);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        mockMvc.perform(delete("/api/user/{id}", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_notFound() throws Exception {
        when(userService.findById(2L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/{id}", "2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_badRequest_nonNumeric() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "xyz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_unauthorized() throws Exception {
        when(userService.findById(1L)).thenReturn(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("other@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        mockMvc.perform(delete("/api/user/{id}", "1"))
                .andExpect(status().isUnauthorized());
    }
}
