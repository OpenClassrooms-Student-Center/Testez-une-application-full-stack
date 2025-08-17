package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void testBuilderAndGetters() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("secret")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.getAdmin());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void testAuthoritiesIsEmpty() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertEquals(new HashSet<>(), user.getAuthorities());
    }

    @Test
    void testAccountStatusMethods() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());
    }

    @Test
    void toString_shouldContainBuilderFields() {
        UserDetailsImpl.UserDetailsImplBuilder builder = UserDetailsImpl.builder()
                .id(42L)
                .username("alice")
                .firstName("Alice")
                .lastName("Smith")
                .admin(true)
                .password("secret");

        String builderString = builder.toString();

        assertTrue(builderString.contains("id=42"));
        assertTrue(builderString.contains("username=alice"));
        assertTrue(builderString.contains("firstName=Alice"));
        assertTrue(builderString.contains("lastName=Smith"));
        assertTrue(builderString.contains("admin=true"));
        assertTrue(builderString.contains("password=secret"));
    }
}