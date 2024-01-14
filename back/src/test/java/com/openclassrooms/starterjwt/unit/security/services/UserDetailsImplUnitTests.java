package com.openclassrooms.starterjwt.unit.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsImplUnitTests {

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("testPassword")
                .build();

    }

    @Test
    @DisplayName("Test UserDetailsImpl construction and getters")
    void testUserDetailsConstructionAndGetters() {
        assertEquals(1L, userDetails.getId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertFalse(userDetails.getAdmin());
        assertEquals("testPassword", userDetails.getPassword());
    }

    @Test
    @DisplayName("Test getAuthorities method")
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    @DisplayName("Test isAccountNonExpired method")
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    @DisplayName("Test isAccountNonLocked method")
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    @DisplayName("Test isCredentialsNonExpired method")
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Test isEnabled method")
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    @DisplayName("Test equals method with the same user")
    void testEqualsSameUser() {
        UserDetailsImpl sameUser = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("testPassword")
                .build();
        assertTrue(userDetails.equals(sameUser));
    }

    @Test
    @DisplayName("Test equals method with different user")
    void testEqualsDifferentUser() {
        UserDetailsImpl differentUser = UserDetailsImpl.builder()
                .id(2L)
                .username("anotheruser")
                .firstName("Jane")
                .lastName("Doe")
                .admin(true)
                .password("differentPassword")
                .build();
        assertFalse(userDetails.equals(differentUser));
    }

    @Test
    @DisplayName("Test equals method with non-UserDetails object")
    void testEqualsNonUserDetailsObject() {
        Object nonUserDetailsObject = new Object();
        assertFalse(userDetails.equals(nonUserDetailsObject));
    }

    @Test
    @DisplayName("Test equals method with null")
    void testEqualsWithNull() {
        assertFalse(userDetails.equals(null));
    }

    @Test
    @DisplayName("Test hashCode method")
    void testHashCode() {
        UserDetailsImpl differentUserDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("testPassword")
                .build();

        assertNotEquals(userDetails.hashCode(), differentUserDetails.hashCode());
    }

}
