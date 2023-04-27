package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.services.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

class UserControllerTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Test
    public void testUserFindByIdOK() {

        User user;
        Long id = 1L;
        String email = "marc.petit@user.com";
        String lastName = "Petit";
        String firstName = "Marc";
        String password = "password";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        user = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(admin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setLastName(lastName);

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.findById(id.toString());

        verify(userService).findById(id);
        verify(userMapper).toDto(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), userDto);

    }

    @Test
    public void testUserFindByIdNotFound() {

        Long id = 1L;

        when(userService.findById(id)).thenReturn(null);

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.findById(id.toString());

        verify(userService).findById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), null);

    }

    @Test
    public void testTeacherFindByIdBadRequest() {

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.findById("notgoodId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testUserDeleteOK() {

        Long id = 1L;
        String email = "marc.petit@user.com";
        String lastName = "Petit";
        String firstName = "Marc";
        String password = "password";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        User user = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(admin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(id)
                .username(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .admin(admin).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findById(id)).thenReturn(user);
        doNothing().when(userService).delete(id);

        // Act
        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.save(id.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findById(id);
        verify(userService).delete(id);
    }

    @Test
    public void testUserDeleteNotFound() {

        Long id = 1L;

        when(userService.findById(id)).thenReturn(null);

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.save(id.toString());

        verify(userService).findById(id);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testUserDeleteBadRequest() {

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.save("notgoodId");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void testUserDeleteUnauthorized() {

        Long id = 1L;
        String email = "marc.petit@user.com";
        String lastName = "Petit";
        String firstName = "Marc";
        String password = "password";
        boolean admin = true;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        User user = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(admin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserDetailsImpl userDetailsImpl = mock(UserDetailsImpl.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findById(id)).thenReturn(user);
        when(userDetailsImpl.getUsername()).thenReturn("otherEmail@mail.com");

        // Act
        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.save(id.toString());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService).findById(id);
        verify(userService, never()).delete(id);
    }

}