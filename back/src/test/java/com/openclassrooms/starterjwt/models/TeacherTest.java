package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TeacherTest {

    Teacher teacherBuilder;

    Teacher teacherSetAttribute;

    @BeforeEach
    public void init() {
        teacherBuilder = Teacher.builder()
                .id(0L)
                .lastName("John")
                .firstName("Doe")
                .createdAt(LocalDateTime.of(1989,12,17,13,30,10,5))
                .createdAt(LocalDateTime.of(1989,12,17,13,30,10,5))
                .build();

        teacherSetAttribute = new Teacher();
        teacherSetAttribute.setId(0L);
        teacherSetAttribute.setLastName("John");
        teacherSetAttribute.setFirstName("Doe");
        teacherSetAttribute.setCreatedAt(LocalDateTime.of(1989,12,17,13,30,10,5));
        teacherSetAttribute.setUpdatedAt(LocalDateTime.of(1989,12,17,13,30,10,5));
    }

    @Test
    public void testSameHash_withSameId() {
        int hashTeacherBuilder = teacherBuilder.hashCode();
        int hashTeacherSetAttribute = teacherSetAttribute.hashCode();
        assertThat(hashTeacherBuilder).isEqualTo(hashTeacherSetAttribute);
    }

    @Test
    public void testDiffHash_withDiffId() {
        teacherSetAttribute.setId(1L);
        int hashTeacherBuilder = teacherBuilder.hashCode();
        int hashTeacherSetAttribute = teacherSetAttribute.hashCode();
        assertThat(hashTeacherBuilder).isNotEqualTo(hashTeacherSetAttribute);
    }

    @Test
    public void testSameObject_withEqualsMethod() {
        Boolean isEquals = teacherBuilder.equals(teacherSetAttribute);
        assertThat(isEquals).isTrue();
    }

    @Test
    public void testDiffObject_withEqualsMethod() {
        teacherSetAttribute.setId(1L);
        Boolean isEquals = teacherBuilder.equals(teacherSetAttribute);
        assertThat(isEquals).isFalse();
    }
    @Test
    void testToString() {
        String teacherString = teacherSetAttribute.toString();
        assertTrue(teacherString.contains("Doe"));
        assertTrue(teacherString.contains("John"));
    }
}
