package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userServiceUnderTest;

    User user;

    @BeforeEach
    public void init() {
        userServiceUnderTest = new UserService(userRepository);
        user = User.builder()
                .id(0L)
                .email("user@example.com")
                .lastName("John")
                .firstName("Doe")
                .password("passwd")
                .admin(false)
                .createdAt(LocalDateTime.of(2025, 1, 11, 3, 35, 20, 9))
                .createdAt(LocalDateTime.of(2054, 1, 11, 3, 35, 20, 9))
                .build();
    }

    @Test
    public void testFindById_shouldReturnTheUser_whenExist() {
        User expectedUser = user;
        when(userRepository.findById(0L)).thenReturn(Optional.ofNullable(user));
        User actualUser = userServiceUnderTest.findById(0L);
        verify(userRepository).findById(0L);
        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    public void testFindById_shouldReturnNull_whenDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        User actualUser = userServiceUnderTest.findById(1L);
        verify(userRepository).findById(1L);
        assertThat(actualUser).isNull();
    }

    @Test
    public void testDeleteById_shouldCallUserRepoDeleteById() {
        userServiceUnderTest.delete(0L);

        verify(userRepository).deleteById(0L);
    }
}
