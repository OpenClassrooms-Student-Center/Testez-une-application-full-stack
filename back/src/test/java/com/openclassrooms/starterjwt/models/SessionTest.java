package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SessionTest {

    Session session;

    @Test
    void builder() {
        //Given
        Teacher teacher = Teacher.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .firstName("firstName")
                .lastName("lastName")
                .build();
        User user = User.builder().
                id(1L)
                .email("email")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        List<User> users = new ArrayList<>();
        users.add(user);
        //When
        session = Session.builder()
                .id(4L)
                .name("name")
                .description("description")
                .date(Date.from(LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC)))
                .teacher(teacher)
                .users(users)
                .build();
        //Then
        assertThat(session.getUsers()).isEqualTo(users);
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getId()).isEqualTo(4L);
        assertThat(session.getName()).isEqualTo("name");
        assertThat(session.getDescription()).isEqualTo("description");
        assertThat(session.getDate()).isCloseTo(Date.from(LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC)), 1000);
    }
}