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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        User user = User.builder()
                .id(30L)
                .firstName("Kenneth")
                .lastName("Lechat")
                .email(username)
                .password("test!1234")
                .admin(true)
                .createdAt(LocalDateTime.parse("2023-08-31T15:20:00"))
                .updatedAt(LocalDateTime.parse("2023-08-31T15:20:00"))
                .build();

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
        assertThat(userDetails.getAdmin()).isNull();
        assertThat(userDetails.getAuthorities()).isEqualTo(requestUserDetails.getAuthorities());
        assertThat(userDetails.getPassword()).isEqualTo(requestUserDetails.getPassword());
        assertThat(userDetails.getUsername()).isEqualTo(requestUserDetails.getUsername());
        assertThat(userDetails.isAccountNonExpired()).isEqualTo(requestUserDetails.isAccountNonExpired());
        assertThat(userDetails.isAccountNonLocked()).isEqualTo(requestUserDetails.isAccountNonLocked());
        assertThat(userDetails.isAccountNonExpired()).isEqualTo(requestUserDetails.isCredentialsNonExpired());
        assertThat(userDetails.isEnabled()).isEqualTo(requestUserDetails.isEnabled());

        verify(userRepository, times(1))
                .findByEmail(username);

    }
    @Test
    @DisplayName("Exception")
    void whenUsernameIsNotFound_thenThrowException(){
        String username = "test";

        when(userRepository.findByEmail(username))
                .thenThrow(UsernameNotFoundException.class);

        Throwable throwable = assertThrows(UsernameNotFoundException.class, ()->{
            userDetailsServiceImpl.loadUserByUsername(username);
        });

        verify(userRepository, times(1))
                .findByEmail(username);

        assertEquals("Exception", UsernameNotFoundException.class, throwable.getClass());
    }
}
