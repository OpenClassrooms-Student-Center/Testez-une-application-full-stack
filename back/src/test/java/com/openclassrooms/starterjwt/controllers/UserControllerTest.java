package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserMapper userMapper;

    @Mock
    UserService userService;

    @Mock
    SecurityContextHolder securityContext;

    User thomas;
    UserDto thomasDto;

    private UserController controller;

    @BeforeEach
    void setUp() {
        thomas = User.builder()
                .id(1L)
                .firstName("Thomas")
                .lastName("Robert")
                .email("thomas@robert.com")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        thomasDto = new UserDto(1L, "thomas@robert.com", "Robert", "Thomas", true, "password", LocalDateTime.now(), LocalDateTime.now());
        controller = new UserController(userService, userMapper);
    }

    @Test
    @DisplayName("findById returns 200 when user is found")
    void findById_200() {
        //Given
        when(this.userService.findById(1L)).thenReturn(thomas);
        when(this.userMapper.toDto(thomas)).thenReturn(thomasDto);
        //When
        ResponseEntity response = controller.findById("1");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        UserDto responseBody = (UserDto) response.getBody();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getFirstName()).isEqualTo("Thomas");
        assertThat(responseBody.getLastName()).isEqualTo("Robert");
        assertThat(responseBody.getEmail()).isEqualTo("thomas@robert.com");
        assertThat(responseBody.getPassword()).isEqualTo("password");
        assertThat(responseBody.isAdmin()).isTrue();
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("findById returns 404 when user is not found")
    void findById_404() {
        //Given
        when(this.userService.findById(1L)).thenReturn(null);
        //When
        ResponseEntity response = controller.findById("1");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    @WithMockUser(username = "thomas@robert.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 200 when user is found and coherent with the logged user")
    void delete_200() {
        //Given
        when(this.userService.findById(thomas.getId())).thenReturn(thomas);
        //When
        ResponseEntity<?> response = controller.delete("1");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username = "martha@bebert.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 401 when user is found but not coherent with the logged user")
    void delete_401() {
        //Given
        when(this.userService.findById(thomas.getId())).thenReturn(thomas);
        //When
        ResponseEntity<?> response = controller.delete("1");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    @WithMockUser(username = "thomas@robert.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 404 when user is not found")
    void delete_404() {
        //Given
        when(this.userService.findById(thomas.getId())).thenReturn(null);
        //When
        ResponseEntity<?> response = controller.delete("1");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }


    @Test
    @WithMockUser(username = "thomas@robert.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 400 when user id is not a number in string")
    void delete_400() {
        //Given
        //When
        ResponseEntity<?> response = controller.delete("abc");
        //Then
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
}