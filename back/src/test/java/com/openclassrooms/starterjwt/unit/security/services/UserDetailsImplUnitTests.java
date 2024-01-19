package com.openclassrooms.starterjwt.unit.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

/**
 * Unit tests for the {@link UserDetailsImpl} class.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsImplUnitTests {
    /**
     * A mock instance of the {@link UserDetailsImpl} class.
     */
    @InjectMocks
    private UserDetailsImpl userDetails;

    /**
     * Sets up the mock {@link UserDetailsImpl} instance before each test case.
     */
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

    /**
     * Test construction of UserDetailsImpl and its getters.
     */
    @Test
    @Tag("UserDetailsImpl.Construction")
    @DisplayName("Test UserDetailsImpl construction and getters")
    void testUserDetailsConstructionAndGetters() {
        assertEquals(1L, userDetails.getId());
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertFalse(userDetails.getAdmin());
        assertEquals("testPassword", userDetails.getPassword());
    }

    /**
     * Tests the {@link UserDetailsImpl#getAuthorities()} method.
     */
    @Test
    @Tag("UserDetailsImpl.getAuthorities()")
    @DisplayName("Test getAuthorities method")
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    /**
     * Test the {@code isAccountNonExpired()} method.
     */
    @Test
    @Tag("UserDetailsImpl.isAccountNonExpired()")
    @DisplayName("Test isAccountNonExpired method")
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    /**
     * Test the {@code isAccountNonLocked()} method.
     */
    @Test
    @Tag("UserDetailsImpl.isAccountNonLocked()")
    @DisplayName("Test isAccountNonLocked method")
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    /**
     * Test the {@code isCredentialsNonExpired()} method.
     */
    @Test
    @Tag("UserDetailsImpl.isCredentialsNonExpired()")
    @DisplayName("Test isCredentialsNonExpired method")
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    /**
     * Test the {@code isEnabled()} method.
     */
    @Test
    @Tag("UserDetailsImpl.isEnabled()")
    @DisplayName("Test isEnabled method")
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    /**
     * Test the {@code equals()} method with the same user.
     */
    @Test
    @Tag("UserDetailsImpl.equals()")
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

    /**
     * Test the {@code equals()} method with a different user.
     */
    @Test
    @Tag("UserDetailsImpl.equals()")
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

    /**
     * Test the {@code equals()} method with a non-UserDetails object.
     */
    @Test
    @Tag("UserDetailsImpl.equals()")
    @DisplayName("Test equals method with non-UserDetails object")
    void testEqualsNonUserDetailsObject() {
        Object nonUserDetailsObject = new Object();
        assertFalse(userDetails.equals(nonUserDetailsObject));
    }

    /**
     * Test the {@code equals()} method with null.
     */
    @Test
    @Tag("UserDetailsImpl.equals()")
    @DisplayName("Test equals method with null")
    void testEqualsWithNull() {
        assertFalse(userDetails.equals(null));
    }

    /**
     * Test the {@code hashCode()} method.
     */
    @Test
    @Tag("UserDetailsImpl.hashCode()")
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
