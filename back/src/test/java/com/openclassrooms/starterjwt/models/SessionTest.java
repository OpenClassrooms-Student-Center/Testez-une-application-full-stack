package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidationConstraints() {
        Session session = new Session();
        Set<ConstraintViolation<Session>> violations = validator.validate(session);
        assertFalse(violations.isEmpty(), "Les contraintes de validation doivent échouer sur un objet vide");
    }

    @Test
    void testBuilderAndEquals() {
        Date date = new Date();
        Session s1 = Session.builder()
                .id(1L)
                .name("Nom")
                .date(date)
                .description("Description")
                .build();
        Session s2 = Session.builder()
                .id(1L)
                .name("Nom")
                .date(date)
                .description("Description")
                .build();
        assertEquals(s1, s2);
        assertEquals("Nom", s1.getName());
    }

    @Test
    void testToString() {
        Session session = Session.builder()
                .id(1L)
                .name("Nom")
                .date(new Date())
                .description("Description")
                .build();
        assertTrue(session.toString().contains("Nom"));
    }

    @Test
    void testEqualsAndHashCode() {
        Session s1 = new Session();
        s1.setId(1L);
        Session s2 = new Session();
        s2.setId(1L);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testEmptyUsersList() {
        Session session = new Session();
        session.setUsers(Collections.emptyList());
        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void testSessionBuilder() {
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(2L);
        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);
        List<User> users = Arrays.asList(user1, user2);

        Session session = Session.builder()
                .id(5L)
                .name("Atelier")
                .date(date)
                .description("Atelier pratique")
                .teacher(teacher)
                .users(users)
                .build();

        assertEquals(5L, session.getId());
        assertEquals("Atelier", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Atelier pratique", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
    }

    @Test
    void testEqualsWithNull() {
        Session session = Session.builder().id(1L).build();
        assertNotEquals(session, null);
    }

    @Test
    void testEqualsWithDifferentType() {
        Session session = Session.builder().id(1L).build();
        assertNotEquals(session, "string");
    }

    @Test
    void testEqualsWithSelf() {
        Session session = Session.builder().id(1L).build();
        assertEquals(session, session);
    }

    @Test
    void testEqualsWithDifferentFields() {
        Session s1 = Session.builder().id(1L).name("A").build();
        Session s2 = Session.builder().id(2L).name("A").build();
        assertNotEquals(s1, s2);
    }

    @Test
    void testEqualsWithSameFields() {
        Date date = new Date();
        Session s1 = Session.builder().id(1L).name("A").date(date).build();
        Session s2 = Session.builder().id(1L).name("A").date(date).build();
        assertEquals(s1, s2);
    }

    @Test
    void testHashCodeConsistency() {
        Session session = Session.builder().id(1L).build();
        int hash1 = session.hashCode();
        int hash2 = session.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testBuilderWithNullFields() {
        Session session = Session.builder()
                .id(null)
                .name(null)
                .date(null)
                .description(null)
                .teacher(null)
                .users(null)
                .build();
        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getDate());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
    }

    @Test
    void testBuilderWithEmptyStringsAndList() {
        Session session = Session.builder()
                .id(0L)
                .name("")
                .description("")
                .users(Collections.emptyList())
                .build();
        assertEquals("", session.getName());
        assertEquals("", session.getDescription());
        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void testBuilderWithOnlyId() {
        Session session = Session.builder()
                .id(42L)
                .build();
        assertEquals(42L, session.getId());
        assertNull(session.getName());
        assertNull(session.getDate());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
    }

    @Test
    void testBuilderNoFields() {
        Session session = Session.builder().build();
        assertNull(session.getId());
        assertNull(session.getName());
        assertNull(session.getDate());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
    }

    @Test
    void testEqualsWithOneNullId() {
        Session s1 = new Session();
        s1.setId(null);
        Session s2 = new Session();
        s2.setId(1L);
        assertNotEquals(s1, s2);
        assertNotEquals(s2, s1);
    }

    @Test
    void testHashCodeWithNullId() {
        Session s1 = new Session();
        s1.setId(null);
        Session s2 = new Session();
        s2.setId(null);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testHashCodeWithDifferentIds() {
        Session s1 = new Session();
        s1.setId(1L);
        Session s2 = new Session();
        s2.setId(2L);
        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testSessionBuilderToString() {
        Session.SessionBuilder builder = Session.builder()
                .id(123L)
                .name("SessionTest")
                .description("Desc");
        String str = builder.toString();
        assertTrue(str.contains("SessionTest"));
        assertTrue(str.contains("Desc"));
        assertTrue(str.contains("123"));
    }
}