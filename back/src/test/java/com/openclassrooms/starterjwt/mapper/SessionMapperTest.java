package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Loads only the generated MapStruct implementation (SessionMapperImpl)
 * and mocks TeacherService / UserService used inside mapping expressions.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SessionMapperImpl.class })
class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @Test
    void toEntity_shouldMapDtoToEntity() {
        // Arrange
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Session");
        dto.setDescription("Relaxation");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setUsers(Collections.singletonList(2L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(2L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user);

        // Act
        Session session = sessionMapper.toEntity(dto);

        // Assert
        assertThat(session.getName()).isEqualTo("Yoga Session");
        assertThat(session.getDescription()).isEqualTo("Relaxation");
        assertThat(session.getDate()).isEqualTo(dto.getDate());
        assertThat(session.getTeacher()).isNotNull();
        assertThat(session.getTeacher().getId()).isEqualTo(1L);
        assertThat(session.getUsers()).hasSize(1);
        assertThat(session.getUsers().get(0).getId()).isEqualTo(2L);
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setId(10L);
        session.setName("Evening Yoga");
        session.setDescription("Advanced class");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("Evening Yoga");
        assertThat(dto.getDescription()).isEqualTo("Advanced class");
        assertThat(dto.getDate()).isEqualTo(session.getDate());
        assertThat(dto.getTeacher_id()).isEqualTo(1L);
        assertThat(dto.getUsers()).containsExactly(2L);
    }

    @Test
    void toEntity_shouldHandleNullTeacherAndNullUsers() {
        // Arrange
        SessionDto dto = new SessionDto();
        dto.setName("Nulls");
        dto.setDescription("No teacher / no users");
        dto.setDate(new Date());
        dto.setTeacher_id(null);
        dto.setUsers(null);

        // Act
        Session session = sessionMapper.toEntity(dto);

        // Assert
        assertThat(session.getTeacher()).isNull();
        assertThat(session.getUsers()).isNotNull(); // mapper builds empty list from null
        assertThat(session.getUsers()).isEmpty();
    }
}