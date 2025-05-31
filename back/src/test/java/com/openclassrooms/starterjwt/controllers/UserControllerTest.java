package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDetails userDetails;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void findById_shouldReturnUserDto() {
        User user = new User();
        UserDto userDto = new UserDto();
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void findById_shouldReturnNotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void findById_shouldReturnBadRequest() {
        ResponseEntity<?> response = userController.findById("abc");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void save_shouldDeleteAndReturnOk() {
        User user = new User();
        user.setEmail("test@email.com");
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@email.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService).delete(1L);
    }

    @Test
    void save_shouldReturnNotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void save_shouldReturnBadRequest() {
        ResponseEntity<?> response = userController.save("abc");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void save_shouldReturnUnauthorized() {
        User user = new User();
        user.setEmail("test@email.com");
        when(userService.findById(1L)).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("other@email.com");

        ResponseEntity<?> response = userController.save("1");

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong());
    }
}