package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
public class UserTest {

        private User user;
        private Long id = 1L;
        private String email = "marc.petit@user.com";
        private String lastName = "Petit";
        private String firstName = "Marc";
        private String password = "password";
        private boolean admin = true;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        @BeforeEach
        public void setUp() {
                // Initialisation de l'objet User pour les tests
                user = User.builder()
                                .id(id)
                                .email(email)
                                .lastName(lastName)
                                .firstName(firstName)
                                .password(password)
                                .admin(admin)
                                .createdAt(createdAt)
                                .updatedAt(updatedAt)
                                .build();
        }

        @Test
        public void testUserGetterAndSetters() {
                // Vérification des méthodes getter par rapport aux valeurs d'initialisation
                assertEquals(id, user.getId());
                assertEquals(email, user.getEmail());
                assertEquals(lastName, user.getLastName());
                assertEquals(firstName, user.getFirstName());
                assertEquals(password, user.getPassword());
                assertTrue(user.isAdmin());
                assertEquals(createdAt, user.getCreatedAt());
                assertEquals(updatedAt, user.getUpdatedAt());

                // initialisation des valeurs de modifications
                Long id2 = 2L;
                String email2 = "john.doe@user.com";
                String lastName2 = "Doe";
                String firstName2 = "John";
                String password2 = "password2";
                boolean admin2 = false;
                LocalDateTime createdAt2 = LocalDateTime.now();
                LocalDateTime updatedAt2 = LocalDateTime.now();

                // MOdification des valeurs via les methodes setter
                user.setId(id2);
                user.setEmail(email2);
                user.setLastName(lastName2);
                user.setFirstName(firstName2);
                user.setPassword(password2);
                user.setAdmin(admin2);
                user.setCreatedAt(createdAt2);
                user.setUpdatedAt(updatedAt2);

                // Vérification des valeurs modifiées
                assertEquals(id2, user.getId());
                assertEquals(email2, user.getEmail());
                assertEquals(lastName2, user.getLastName());
                assertEquals(firstName2, user.getFirstName());
                assertEquals(password2, user.getPassword());
                assertFalse(user.isAdmin());
        }
}
