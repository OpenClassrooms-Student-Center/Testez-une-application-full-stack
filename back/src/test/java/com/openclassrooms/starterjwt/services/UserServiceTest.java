package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @BeforeEach
    void setUp(){
        userService = new UserService(
                userRepository);
    }

    @Test
    @DisplayName("delete")
    void whenUserIdFound_thenDeleteUser(){
        Long id = 29L;

        userRepository.deleteById(id);

        verify(userRepository, times(1))
                .deleteById(id);
    }

    @Test
    @DisplayName("findById")
    void whenUserIdFound_thenReturnUser(){
        Long id = 30L;
        User user = new User();

        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        User findUser = userService.findById(id);

        assertEquals("User found", user, findUser);
        verify(userRepository, times(1))
                .findById(id);
    }
    @Test
    @DisplayName("null")
    void whenUserIsNull_thenReturnNull(){
        Long id = null;

        User user  = userRepository.findById(id).orElse(null);

        assertThat(user).isNull();
        verify(userRepository, times(1))
                .findById(id);
    }
}
