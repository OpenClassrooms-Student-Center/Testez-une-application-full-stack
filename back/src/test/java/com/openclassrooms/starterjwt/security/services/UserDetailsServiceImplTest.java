package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDetailsServiceImplTest {

    @Mock
    UserRepository userRepository;
    User user;
    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Load user by username should return user details with user in database")
    void loadUserByUsername() {
        //Given
        user = new User();
        user.setEmail("thomas@robert.com");
        user.setFirstName("Thomas");
        user.setLastName("Robert");
        user.setPassword("password");
        user.setAdmin(true);
        when(userRepository.findByEmail("thomas@robert.com")).thenReturn(java.util.Optional.of(user));
        //When
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("thomas@robert.com");
        //Then
        assertEquals(userDetails.getUsername(), user.getEmail());
        assertEquals(userDetails.getFirstName(), user.getFirstName());
        assertEquals(userDetails.getLastName(), user.getLastName());
        assertEquals(userDetails.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("Load user by username should not return user details without user in database")
    void loadUserByUsername_KO() {
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("thomas@robert.com");
        });
    }


}