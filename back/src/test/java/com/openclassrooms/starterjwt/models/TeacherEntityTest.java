package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class TeacherEntityTest {

    // Define sample data for testing
    private String lastName = "Smith";
    private String firstName = "Elvis";
    private LocalDateTime createdAt = LocalDateTime.MAX;
    private LocalDateTime updatedAt = LocalDateTime.MIN;

    private Teacher teacher;

    @BeforeEach
    public void beforeEach() {
        // Initialize a Teacher object for testing
        teacher = Teacher.builder()
                .id(300L)
                .lastName(lastName)
                .firstName(firstName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    public void teacherEntitySetIdOk() {
        // Test setting the ID property of the Teacher object
        teacher.setId(2L);
        // Verify that the ID is set correctly
        assertThat(teacher.getId()).isEqualTo(2L);
    }

    @Test
    public void teacherEntitySetLastNameOk() {
        // Test setting the lastName property of the Teacher object
        teacher.setLastName(lastName);
        // Verify that lastName is set correctly
        assertThat(teacher.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void teacherEntitySetFirstNameOk() {
        // Test setting the firstName property of the Teacher object
        teacher.setLastName(firstName);
        // Verify that firstName is set correctly
        assertThat(teacher.getLastName()).isEqualTo(firstName);
    }

    @Test
    public void teacherEntitySetCreatedAtOk() {
        // Test setting the createdAt property of the Session object
        LocalDateTime nowDateStored = LocalDateTime.now();
        teacher.setCreatedAt(nowDateStored);
        // Verify that createdAt is set correctly
        assertThat(teacher.getCreatedAt()).isEqualTo(nowDateStored);
    }

    @Test
    public void teacherEntitySetUpdatedAtOk() {
        // Test setting the updatedAt property of the teacher object
        LocalDateTime nowDateStored = LocalDateTime.now();
        teacher.setUpdatedAt(nowDateStored);
        // Verify that updatedAt is set correctly
        assertThat(teacher.getUpdatedAt()).isEqualTo(nowDateStored);
    }
}
