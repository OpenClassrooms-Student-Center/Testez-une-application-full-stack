package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidationConstraints() {
        Teacher teacher = new Teacher();
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);
        assertFalse(violations.isEmpty(), "Les contraintes de validation doivent échouer sur un objet vide");
    }

    @Test
    void testBuilderAndEquals() {
        Teacher t1 = Teacher.builder()
                .id(1L)
                .firstName("Jean")
                .lastName("Dupont")
                .build();
        Teacher t2 = Teacher.builder()
                .id(1L)
                .firstName("Jean")
                .lastName("Dupont")
                .build();
        assertEquals(t1, t2);
        assertEquals("Jean", t1.getFirstName());
    }

    @Test
    void testToString() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jean")
                .lastName("Dupont")
                .build();
        assertTrue(teacher.toString().contains("Jean"));
    }

    @Test
    void testEqualsAndHashCode() {
        Teacher t1 = new Teacher();
        t1.setId(1L);
        Teacher t2 = new Teacher();
        t2.setId(1L);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testTeacherBuilder() {
        Teacher.TeacherBuilder builder = Teacher.builder();
        builder.id(10L)
                .firstName("Paul")
                .lastName("Martin");
        Teacher teacher = builder.build();

        assertEquals(10L, teacher.getId());
        assertEquals("Paul", teacher.getFirstName());
        assertEquals("Martin", teacher.getLastName());
    }

    @Test
    void testEqualsWithNull() {
        Teacher teacher = Teacher.builder().id(1L).build();
        assertNotEquals(teacher, null);
    }

    @Test
    void testEqualsWithDifferentType() {
        Teacher teacher = Teacher.builder().id(1L).build();
        assertNotEquals(teacher, "string");
    }

    @Test
    void testEqualsWithSelf() {
        Teacher teacher = Teacher.builder().id(1L).build();
        assertEquals(teacher, teacher);
    }

    @Test
    void testEqualsWithDifferentFields() {
        Teacher t1 = Teacher.builder().id(1L).firstName("A").build();
        Teacher t2 = Teacher.builder().id(2L).firstName("A").build();
        assertNotEquals(t1, t2);
    }

    @Test
    void testHashCodeConsistency() {
        Teacher teacher = Teacher.builder().id(1L).build();
        int hash1 = teacher.hashCode();
        int hash2 = teacher.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testBuilderWithNullFields() {
        Teacher teacher = Teacher.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
        assertNull(teacher.getId());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getLastName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }

    @Test
    void testBuilderWithEmptyStrings() {
        Teacher teacher = Teacher.builder()
                .id(0L)
                .firstName("")
                .lastName("")
                .build();
        assertEquals("", teacher.getFirstName());
        assertEquals("", teacher.getLastName());
    }

    @Test
    void testBuilderWithOnlyId() {
        Teacher teacher = Teacher.builder()
                .id(42L)
                .build();
        assertEquals(42L, teacher.getId());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getLastName());
    }

    @Test
    void testBuilderNoFields() {
        Teacher teacher = Teacher.builder().build();
        assertNull(teacher.getId());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getLastName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }

    @Test
    void testHashCodeWithNullId() {
        Teacher t1 = new Teacher();
        t1.setId(null);
        Teacher t2 = new Teacher();
        t2.setId(null);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testHashCodeWithDifferentIds() {
        Teacher t1 = new Teacher();
        t1.setId(1L);
        Teacher t2 = new Teacher();
        t2.setId(2L);
        assertNotEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testHashCodeWithSameId() {
        Teacher t1 = new Teacher();
        t1.setId(5L);
        Teacher t2 = new Teacher();
        t2.setId(5L);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testHashCodeEqualsContract() {
        Teacher t1 = new Teacher();
        t1.setId(7L);
        Teacher t2 = new Teacher();
        t2.setId(7L);
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testHashCodeWithOneNullId() {
        Teacher t1 = new Teacher();
        t1.setId(null);
        Teacher t2 = new Teacher();
        t2.setId(1L);
        assertNotEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t2.hashCode(), t1.hashCode());
    }

    @Test
    void testTeacherBuilderToString() {
        Teacher.TeacherBuilder builder = Teacher.builder()
                .id(123L)
                .firstName("Alice")
                .lastName("Durand");
        String str = builder.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Durand"));
        assertTrue(str.contains("123"));
    }
}