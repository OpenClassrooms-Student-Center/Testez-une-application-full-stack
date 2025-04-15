package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, now, now);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testConstructorWithoutIdAndTimestamps() {
        User user = new User("test@example.com", "Doe", "John", "password123", true);

        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        User differentUser = new User(2L, "different@example.com", "Smith", "Jane", "password456", false, LocalDateTime.now(), LocalDateTime.now());

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        assertNotEquals(user1, differentUser);
        assertNotEquals(user1.hashCode(), differentUser.hashCode());
        assertNotEquals(user1, null);
        assertNotEquals(user1, new Object());
    }

    @Test
    void testToString() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());

        String userString = user.toString();
        assertTrue(userString.contains("test@example.com"));
        assertTrue(userString.contains("Doe"));
        assertTrue(userString.contains("John"));
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setEmail("new@example.com");
        user.setLastName("Smith");
        user.setFirstName("Jane");
        user.setPassword("newpass");

        assertEquals("new@example.com", user.getEmail());
        assertEquals("Smith", user.getLastName());
        assertEquals("Jane", user.getFirstName());
        assertEquals("newpass", user.getPassword());
    }

    @Test
    void testSetCreatedAtAndUpdatedAt() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .lastName("Doe")
                .firstName("John")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testRequiredArgsConstructor() {
        User user = new User("test@example.com", "Doe", "John", "password123", true);

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());

        assertNull(user.getId());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getLastName());
        assertNull(user.getFirstName());
        assertNull(user.getPassword());
        assertFalse(user.isAdmin());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testEquals_NullAndDifferentType() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());

        assertNotEquals(user, null);
        assertNotEquals(user, "Some string");
    }

}
