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
    void save() {
        //Given
    }
}