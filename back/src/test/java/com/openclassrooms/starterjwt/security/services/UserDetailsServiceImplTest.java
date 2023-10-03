package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootTest
public class UserDetailsServiceImplTest {

  @MockBean // Mocking UserRepository
  UserRepository userRepository;

  @Test
  public void loadUserByUsernameOk() {
    // Sample user data
    Long userId = 1L;
    String email = "test@mail.com";
    String password = "password";
    String firstName = "Doe";
    String lastName = "John";

    // Create a User object
    User user = User.builder()
        .id(userId)
        .email(email)
        .password(password)
        .firstName(firstName)
        .lastName(lastName)
        .build();

    // Mock UserRepository to return the user when findByEmail is called
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

    // Create UserDetailsServiceImpl instance for testing
    UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

    // Load user by email
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    // Ensure the loaded user details are of the correct type
    assert userDetails instanceof UserDetailsImpl;

    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

    // Verify that user details match the expected values
    assertEquals(userId, userDetailsImpl.getId());
    assertEquals(email, userDetailsImpl.getUsername());
    assertEquals(firstName, userDetailsImpl.getFirstName());
    assertEquals(lastName, userDetailsImpl.getLastName());
    assertEquals(password, userDetailsImpl.getPassword());
  }

  @Test
  public void loadUserByUsernameNotFound() {
        // Mock UserRepository to return empty when findByEmail is called
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Create UserDetailsServiceImpl instance for testing
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

        // Verify that UsernameNotFoundException is thrown when the user is not found
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@mail.com"));
    }
}
