package com.openclassrooms.starterjwt.unit.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    /*
     * Test the loadUserByUsername() method with a valid username.
     */
    @Test
    @Tag("UserDetailsServiceImpl.LoadUserByUsername")
    @DisplayName("Test loadUserByUsername with valid username")
    void testLoadUserByUsernameValidUsername() {
        // * Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("testuser@test.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("testPassword");

        when(userRepository.findByEmail("testuser@test.com")).thenReturn(Optional.of(user));

        // * Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser@test.com");

        // * Assert
        assertNotNull(userDetails);
        assertEquals("testuser@test.com", userDetails.getUsername());
        assertEquals("John", ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals("Doe", ((UserDetailsImpl) userDetails).getLastName());
        assertEquals("testPassword", userDetails.getPassword());
    }

    /*
     * Test the loadUserByUsername() method with an invalid username.
     */
    @Test
    @Tag("UserDetailsServiceImpl.LoadUserByUsername")
    @DisplayName("Test loadUserByUsername with invalid username")
    void testLoadUserByUsernameInvalidUsername() {
        // * Arrange
        when(userRepository.findByEmail("nonexistentuser@test.com")).thenReturn(Optional.empty());

        // * Act
        // * Assert
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistentuser@test.com"));
    }
}
