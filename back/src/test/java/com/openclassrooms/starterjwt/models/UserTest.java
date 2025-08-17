package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testBuilderAndEquals() {
        User u1 = User.builder()
                .id(1L)
                .email("test@email.com")
                .firstName("Jean")
                .lastName("Dupont")
                .password("password")
                .admin(true)
                .build();
        User u2 = User.builder()
                .id(1L)
                .email("test@email.com")
                .firstName("Jean")
                .lastName("Dupont")
                .password("password")
                .admin(true)
                .build();
        assertEquals(u1, u2);
        assertEquals("Jean", u1.getFirstName());
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id(1L)
                .email("test@email.com")
                .firstName("Jean")
                .lastName("Dupont")
                .password("password")
                .admin(false)
                .build();
        assertTrue(user.toString().contains("test@email.com"));
    }

    @Test
    void testEqualsAndHashCode() {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(1L);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testUserBuilder() {
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User user = builder.build();

        assertEquals(5L, user.getId());
        assertEquals("builder@test.com", user.getEmail());
        assertEquals("Builder", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals("builderpass", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testUserBuilderWithDate() {
        LocalDateTime now = LocalDateTime.now();
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false)
                .createdAt(now)
                .updatedAt(now);
        User user = builder.build();

        assertEquals(5L, user.getId());
        assertEquals("builder@test.com", user.getEmail());
        assertEquals("Builder", user.getFirstName());
        assertEquals("Test", user.getLastName());
        assertEquals("builderpass", user.getPassword());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertFalse(user.isAdmin());
    }


    @Test
    void testEqualsWithNull() {
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User user = builder.build();
        assertNotEquals(user, null);
    }

    @Test
    void testEqualsWithDifferentType() {
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User user = builder.build();
        assertNotEquals(user, "string");
    }

    @Test
    void testEqualsWithSelf() {
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User user = builder.build();
        assertEquals(user, user);
    }

    @Test
    void testEqualsWithDifferentFields() {
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User u1 = builder.build();

        builder.id(2L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User u2 = builder.build();
        assertNotEquals(u1, u2);
    }

    @Test
    void testHashCodeConsistency() {
        User.UserBuilder builder = User.builder();
        builder.id(5L)
                .email("builder@test.com")
                .firstName("Builder")
                .lastName("Test")
                .password("builderpass")
                .admin(false);
        User user = builder.build();
        int hash1 = user.hashCode();
        int hash2 = user.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testBuilderWithEmptyStrings() {
        User user = User.builder()
                .id(0L)
                .email("")
                .firstName("")
                .lastName("")
                .password("")
                .admin(false)
                .build();
        assertEquals("", user.getEmail());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getPassword());
    }

    @Test
    void testRequiredArgsConstructorNullEmail() {
        assertThrows(NullPointerException.class, () ->
                new User(null, "Dupont", "Jean", "pass", false)
        );
    }

    @Test
    void testRequiredArgsConstructorNullLastName() {
        assertThrows(NullPointerException.class, () ->
                new User("mail@mail.com", null, "Jean", "pass", false)
        );
    }

    @Test
    void testRequiredArgsConstructorNullFirstName() {
        assertThrows(NullPointerException.class, () ->
                new User("mail@mail.com", "Dupont", null, "pass", false)
        );
    }

    @Test
    void testRequiredArgsConstructorNullPassword() {
        assertThrows(NullPointerException.class, () ->
                new User("mail@mail.com", "Dupont", "Jean", null, false)
        );
    }
    @Test
    void testRequiredArgsConstructorAllFields() {
        User user = new User("mail@mail.com", "Dupont", "Jean", "pass", false);
        assertEquals("mail@mail.com", user.getEmail());
        assertEquals("Dupont", user.getLastName());
        assertEquals("Jean", user.getFirstName());
        assertEquals("pass", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testAllArgsConstructorAllFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                1L,
                "mail@mail.com",
                "Dupont",
                "Jean",
                "pass",
                false,
                now,
                now
        );
        assertEquals(1L, user.getId());
        assertEquals("mail@mail.com", user.getEmail());
        assertEquals("Dupont", user.getLastName());
        assertEquals("Jean", user.getFirstName());
        assertEquals("pass", user.getPassword());
        assertFalse(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructorNullEmail() {
        assertThrows(NullPointerException.class, () ->
                new User(1L, null, "Dupont", "Jean", "pass", false, null, null)
        );
    }

    @Test
    void testAllArgsConstructorNullLastName() {
        assertThrows(NullPointerException.class, () ->
                new User(1L, "mail@mail.com", null, "Jean", "pass", false, null, null)
        );
    }

    @Test
    void testAllArgsConstructorNullFirstName() {
        assertThrows(NullPointerException.class, () ->
                new User(1L, "mail@mail.com", "Dupont", null, "pass", false, null, null)
        );
    }

    @Test
    void testAllArgsConstructorNullPassword() {
        assertThrows(NullPointerException.class, () ->
                new User(1L, "mail@mail.com", "Dupont", "Jean", null, false, null, null)
        );
    }

    @Test
    void testAllArgsConstructorEmptyStrings() {
        User user = new User(
                2L,
                "",
                "",
                "",
                "",
                true,
                null,
                null
        );
        assertEquals("", user.getEmail());
        assertEquals("", user.getLastName());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    void testAllArgsConstructorNullDates() {
        User user = new User(
                3L,
                "mail@mail.com",
                "Dupont",
                "Jean",
                "pass",
                false,
                null,
                null
        );
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testEqualsWithNullFields() {
        User user1 = new User();
        User user2 = new User();
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testEqualsWithNullId() {
        User u1 = new User();
        u1.setId(null);
        User u2 = new User();
        u2.setId(null);
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testEqualsWithOneNullId() {
        User u1 = new User();
        u1.setId(null);
        User u2 = new User();
        u2.setId(1L);
        assertNotEquals(u1, u2);
        assertNotEquals(u2, u1);
    }

    @Test
    void testEqualsWithDifferentId() {
        User u1 = new User();
        u1.setId(1L);
        User u2 = new User();
        u2.setId(2L);
        assertNotEquals(u1, u2);
    }

    @Test
    void testEqualsWithSameId() {
        User u1 = new User();
        u1.setId(42L);
        User u2 = new User();
        u2.setId(42L);
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testBuilderWithNullFieldsThrowsException() {
        assertThrows(NullPointerException.class, () -> User.builder()
                .id(null)
                .email(null)
                .firstName(null)
                .lastName(null)
                .password(null)
                .admin(false)
                .createdAt(null)
                .updatedAt(null)
                .build());
    }

    @Test
    void testBuilderWithNoFieldsThrowsException() {
        assertThrows(NullPointerException.class, () -> User.builder().build());
    }

    @Test
    void testBuilderWithOnlyIdThrowsException() {
        assertThrows(NullPointerException.class, () -> User.builder().id(99L).build());
    }

    @Test
    void testUserBuilderToString() {
        User.UserBuilder builder = User.builder()
                .id(10L)
                .email("builder@mail.com")
                .firstName("Alice")
                .lastName("Martin")
                .password("secret")
                .admin(true);
        String str = builder.toString();
        assertTrue(str.contains("builder@mail.com"));
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Martin"));
        assertTrue(str.contains("true"));
    }

    @Test
    void testRequiredArgsConstructorEmptyLastName() {
        User user = new User("mail@mail.com", "", "Jean", "pass", false);
        assertEquals("", user.getLastName());
    }


    @Test
    void testRequiredArgsConstructorEmptyFirstName() {
        User user = new User("mail@mail.com", "Dupont", "", "pass", false);
        assertEquals("", user.getFirstName());
    }


    @Test
    void testRequiredArgsConstructorEmptyPassword() {
        User user = new User("mail@mail.com", "Dupont", "Jean", "", false);
        assertEquals("", user.getPassword());
    }

    @Test
    void testUserBuilderWithNullFields() {
        assertThrows(NullPointerException.class, () -> User.builder()
                .email(null)
                .lastName(null)
                .firstName(null)
                .password(null)
                .build());
    }

    @Test
    void testUserBuilderWithEmptyFields() {
        User user = User.builder()
                .email("")
                .lastName("")
                .firstName("")
                .password("")
                .build();
        assertEquals("", user.getEmail());
        assertEquals("", user.getLastName());
        assertEquals("", user.getFirstName());
        assertEquals("", user.getPassword());
    }

    @Test
    void testUserBuilderWithNonEmptyFields() {
        User user = User.builder()
                .email("test@mail.com")
                .lastName("Dupont")
                .firstName("Jean")
                .password("secret")
                .build();
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("Dupont", user.getLastName());
        assertEquals("Jean", user.getFirstName());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void testUserBuilderWithNullLastName() {
        assertThrows(NullPointerException.class, () -> User.builder()
                .email("mail@mail.com")
                .lastName(null)
                .firstName("Jean")
                .password("pass")
                .build());
    }

    @Test
    void testUserBuilderWithNullFirstName() {
        assertThrows(NullPointerException.class, () -> User.builder()
                .email("mail@mail.com")
                .lastName("Dupont")
                .firstName(null)
                .password("pass")
                .build());
    }

    @Test
    void testUserBuilderWithNullPassword() {
        assertThrows(NullPointerException.class, () -> User.builder()
                .email("mail@mail.com")
                .lastName("Dupont")
                .firstName("Jean")
                .password(null)
                .build());
    }
}