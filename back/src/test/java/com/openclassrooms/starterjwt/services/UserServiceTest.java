package com.openclassrooms.starterjwt.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserService userService = new UserService(userRepository);
        User foundUser = userService.findById(user.getId());
        Assertions.assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        doNothing().when(userRepository).deleteById(anyLong());
        UserService userService = new UserService(userRepository);
        userService.delete(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

}
