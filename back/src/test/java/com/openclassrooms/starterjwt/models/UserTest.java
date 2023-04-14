package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'objet User pour les tests
        user = User.builder()
                .id(1L)
                .email("marc.petit@user.com")
                .lastName("Petit")
                .firstName("Marc")
                .password("password")
                .admin(false)
                .build();
    }

    @Test
    public void testUserGetterAndSetters() {
        // Vérification des méthodes getter
        assertEquals(1, user.getId());
        assertEquals("marc.petit@user.com", user.getEmail());
        assertEquals("Petit", user.getLastName());
        assertEquals("Marc", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertFalse(user.isAdmin());

        // Vérification des méthodes setter
        user.setEmail("robert.wu@user.com");
        user.setLastName("Wu");
        user.setFirstName("Robert");
        user.setPassword("new_password");
        user.setAdmin(true);

        assertEquals("robert.wu@user.com", user.getEmail());
        assertEquals("Wu", user.getLastName());
        assertEquals("Robert", user.getFirstName());
        assertEquals("new_password", user.getPassword());
        assertTrue(user.isAdmin());
    }

}
