package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import java.util.Optional;

@SpringBootTest
class CredentialsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void beforeEach() {
        // Create a test user with sample data
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("usertest@gmail.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword("password");
    }

    @Test
    public void loadUserByUsernameOk() {
        // Test case for loading a user by username
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());

        // Verify that the loaded UserDetails match the testUser data
        assertThat(userDetails.getUsername()).isEqualTo(testUser.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(testUser.getPassword());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    public void shouldThrowUsernameNotFoundException() {
        // Test case for throwing UsernameNotFoundException when user is not found
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Verify that an exception is thrown when the user is not found
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(testUser.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: " + testUser.getEmail());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }
}
