package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Test
    void testLoadUserByUsername_UserExists() {
        UserRepository userRepository = mock(UserRepository.class);
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);
        UserDetails userDetails = service.loadUserByUsername("test@mail.com");

        assertEquals("test@mail.com", userDetails.getUsername());
        assertEquals("John", ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals("Doe", ((UserDetailsImpl) userDetails).getLastName());
        assertEquals("secret", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        UserDetailsServiceImpl service = new UserDetailsServiceImpl(userRepository);

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("notfound@mail.com");
        });
    }
}