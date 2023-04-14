package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {

    private Session session;
    private Teacher teacher;
    private Date date;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'objet User pour les tests
        teacher = new Teacher();
        date = new Date();
        users = new ArrayList<User>();

        session = Session.builder()
                .id(1L)
                .name("Session1")
                .date(date)
                .description("Description de la session")
                .teacher(teacher)
                .users(users)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testSessionGettersAndSetters() {
        // Vérification des getters et setters
        assertEquals(1L, session.getId());
        assertEquals("Session1", session.getName());
        assertNotNull(session.getDate());
        assertEquals("Description de la session", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertNotNull(session.getUsers());
        assertNotNull(session.getCreatedAt());
        assertNotNull(session.getUpdatedAt());

        // Modification des valeurs
        session.setId(2L)
                .setName("Session2")
                .setDate(new Date())
                .setDescription("Nouvelle description")
                .setTeacher(teacher)
                .setUsers(new ArrayList<>())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        assertEquals(2L, session.getId());
        assertEquals("Session2", session.getName());
        assertNotNull(session.getDate());
        assertEquals("Nouvelle description", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertNotNull(session.getUsers());
        assertNotNull(session.getCreatedAt());
        assertNotNull(session.getUpdatedAt());
    }

}
