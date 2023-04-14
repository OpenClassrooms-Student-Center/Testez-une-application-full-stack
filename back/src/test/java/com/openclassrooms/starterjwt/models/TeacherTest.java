package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        // Initialisation de l'objet Teacher pour les tests
        teacher = Teacher.builder()
                .id(1L)
                .lastName("Duchemin")
                .firstName("Margot")
                .build();
    }

    @Test
    public void testTeacherGetterAndSetters() {
        // Vérification des méthodes getter
        assertEquals(1, teacher.getId());
        assertEquals("Duchemin", teacher.getLastName());
        assertEquals("Margot", teacher.getFirstName());

        // Vérification des méthodes setter
        teacher.setId(2L);
        teacher.setLastName("Smith");
        teacher.setFirstName("Jane");

        assertEquals(2, teacher.getId());
        assertEquals("Smith", teacher.getLastName());
        assertEquals("Jane", teacher.getFirstName());

    }

}
