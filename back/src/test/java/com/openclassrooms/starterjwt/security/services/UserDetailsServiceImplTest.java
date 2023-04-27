package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.MapKeyColumn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class UserDetailsServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Test
    void testLoadUserByUsername() {

        Long userId = 1L;
        String email = "test@mail.com";
        String password = "password";
        String firstName = "Doe";
        String lastName = "John";

        User user = User.builder()
                .id(userId)
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        assert userDetails instanceof UserDetailsImpl;
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        assertEquals(userId, userDetailsImpl.getId());
        assertEquals(email, userDetailsImpl.getUsername());
        assertEquals(firstName, userDetailsImpl.getFirstName());
        assertEquals(lastName, userDetailsImpl.getLastName());
        assertEquals(password, userDetailsImpl.getPassword());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        
      when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

      UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepository);

      assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@mail.com"));
    }

}
