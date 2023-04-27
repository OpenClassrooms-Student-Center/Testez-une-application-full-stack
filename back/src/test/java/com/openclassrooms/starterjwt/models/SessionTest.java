package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionTest {

        private Session session;
        private Long id = 1L;
        private String name = "Session1";
        private Date date = new Date();
        private String description = "Description de la session";
        private Teacher teacher = new Teacher();
        private List<User> users = new ArrayList<>();
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        @BeforeEach
        public void setUp() {
                // Initialisation de l'objet Session pour les tests
                session = Session.builder()
                                .id(id)
                                .name(name)
                                .date(date)
                                .description(description)
                                .teacher(teacher)
                                .users(users)
                                .createdAt(createdAt)
                                .updatedAt(updatedAt)
                                .build();
        }

        @Test
        public void testSessionGettersAndSetters() {
                // Vérification des getters par rapport aux valeurs initiales de l'objet Session
                assertEquals(1L, session.getId());
                assertEquals("Session1", session.getName());
                assertNotNull(session.getDate());
                assertEquals("Description de la session", session.getDescription());
                assertEquals(teacher, session.getTeacher());
                assertNotNull(session.getUsers());
                assertNotNull(session.getCreatedAt());
                assertNotNull(session.getUpdatedAt());

                // Modification des valeurs via les setters
                session.setId(2L)
                                .setName("Session2")
                                .setDate(new Date())
                                .setDescription("Nouvelle description")
                                .setTeacher(teacher)
                                .setUsers(new ArrayList<>())
                                .setCreatedAt(LocalDateTime.now())
                                .setUpdatedAt(LocalDateTime.now());

                // Vérification des valeurs via les getters
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
