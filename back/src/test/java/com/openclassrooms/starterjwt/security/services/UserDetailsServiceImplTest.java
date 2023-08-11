package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    UserRepository userRepository;
    @BeforeEach
    void setUp(){
        userDetailsServiceImpl = new UserDetailsServiceImpl(
                userRepository);
    }

    @Test
    @DisplayName("loadUserByUsername")
    void whenUsernameIsFound_thenReturnUserDetailsImpl(){

        String username = "test@test.com";
        User user = new User();

        when(userRepository.findByEmail(username))
                .thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getEmail())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .password(user.getPassword())
                .build();

        UserDetails requestUserDetails =
                userDetailsServiceImpl.loadUserByUsername(username);

        assertEquals("UserDetails", userDetails, requestUserDetails);
        verify(userRepository, times(1))
                .findByEmail(username);

    }

    @Test
    @DisplayName("Exception")
    void whenUsernameIsNotFound_thenThrowException(){
        String username = null;

        when(userRepository.findByEmail(username))
                .thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, ()->{
            userDetailsServiceImpl.loadUserByUsername(username);
        });
        verify(userRepository, times(1))
                .findByEmail(username);
    }
}
