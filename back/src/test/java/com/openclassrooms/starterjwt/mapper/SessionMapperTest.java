package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    @Test
    void toEntity_withNullTeacherId_setsTeacherNull() {
        // Arrange
        SessionDto dto = new SessionDto();
        dto.setId(123L);
        dto.setName("Null teacher");
        dto.setDescription("desc");
        dto.setTeacher_id(null); // important
        dto.setUsers(Collections.singletonList(2L));

        User user = new User();
        user.setId(2L);

        Mockito.when(userService.findById(2L)).thenReturn(user);

        // Act
        Session entity = sessionMapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getTeacher()).isNull();
        assertThat(entity.getUsers()).hasSize(1);
        assertThat(entity.getUsers().get(0).getId()).isEqualTo(2L);
    }

    @Test
    void toEntity_withNullUsers_mapsEmptyList() {
        // Arrange
        SessionDto dto = new SessionDto();
        dto.setName("No users");
        dto.setDescription("desc");
        dto.setTeacher_id(1L);
        dto.setUsers(null); // important

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);

        // Act
        Session entity = sessionMapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getTeacher()).isNotNull();
        assertThat(entity.getUsers()).isNotNull().isEmpty();
    }

    @Test
    void toEntity_withUnknownUser_keepsNullSlotInList() {
        // Arrange
        SessionDto dto = new SessionDto();
        dto.setName("Unknown user");
        dto.setDescription("desc");
        dto.setTeacher_id(1L);
        dto.setUsers(Collections.singletonList(999L)); // user introuvable

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);
        Mockito.when(userService.findById(999L)).thenReturn(null);

        // Act
        Session entity = sessionMapper.toEntity(dto);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getTeacher()).isNotNull();
        // Le mapper actuel n’élimine pas les nulls -> la liste contient un élément null
        assertThat(entity.getUsers()).hasSize(1);
        assertThat(entity.getUsers().get(0)).isNull();
    }

    @Test
    void toDto_withNullUsers_returnsEmptyIdList() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(7L);

        Session session = new Session();
        session.setId(1L);
        session.setName("DTO null users");
        session.setDescription("d");
        session.setTeacher(teacher);
        session.setUsers(null); // important

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getTeacher_id()).isEqualTo(7L);
        assertThat(dto.getUsers()).isNotNull().isEmpty();
    }

    @Test
    void toDto_withNullTeacher_setsTeacherIdNull() {
        // Arrange
        Session session = new Session();
        session.setId(1L);
        session.setName("Null teacher dto");
        session.setDescription("d");
        session.setTeacher(null);
        session.setUsers(Collections.emptyList());

        // Act
        SessionDto dto = sessionMapper.toDto(session);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getTeacher_id()).isNull();
        assertThat(dto.getUsers()).isNotNull().isEmpty();
    }

    @Test
    void toEntity_list_shouldMapAll_andUseServices() {
        SessionDto d1 = new SessionDto();
        d1.setId(1L);
        d1.setName("S1");
        d1.setDescription("D1");
        d1.setTeacher_id(100L);
        d1.setUsers(Arrays.asList(200L, 201L));

        SessionDto d2 = new SessionDto();
        d2.setId(2L);
        d2.setName("S2");
        d2.setDescription("D2");
        d2.setTeacher_id(101L);
        d2.setUsers(Collections.singletonList(202L));

        Teacher t100 = new Teacher();
        t100.setId(100L);
        Teacher t101 = new Teacher();
        t101.setId(101L);
        User u200 = new User();
        u200.setId(200L);
        User u201 = new User();
        u201.setId(201L);
        User u202 = new User();
        u202.setId(202L);

        Mockito.when(teacherService.findById(100L)).thenReturn(t100);
        Mockito.when(teacherService.findById(101L)).thenReturn(t101);
        Mockito.when(userService.findById(200L)).thenReturn(u200);
        Mockito.when(userService.findById(201L)).thenReturn(u201);
        Mockito.when(userService.findById(202L)).thenReturn(u202);

        List<SessionDto> list = Arrays.asList(d1, d2);
        List<Session> entities = sessionMapper.toEntity(list);

        assertThat(entities).hasSize(2);
        assertThat(entities.get(0).getTeacher().getId()).isEqualTo(100L);
        assertThat(entities.get(0).getUsers()).extracting(User::getId).containsExactly(200L, 201L);
        assertThat(entities.get(1).getTeacher().getId()).isEqualTo(101L);
        assertThat(entities.get(1).getUsers()).extracting(User::getId).containsExactly(202L);
    }

    @Test
    void toDto_list_shouldMapAll_andFlattenIds() {
        Teacher t1 = new Teacher();
        t1.setId(100L);
        Teacher t2 = new Teacher();
        t2.setId(101L);
        User u200 = new User();
        u200.setId(200L);
        User u201 = new User();
        u201.setId(201L);

        Session s1 = new Session();
        s1.setId(1L);
        s1.setName("S1");
        s1.setDescription("D1");
        s1.setTeacher(t1);
        s1.setUsers(Arrays.asList(u200, u201));

        Session s2 = new Session();
        s2.setId(2L);
        s2.setName("S2");
        s2.setDescription("D2");
        s2.setTeacher(t2);
        s2.setUsers(Collections.singletonList(u200));

        List<SessionDto> dtos = sessionMapper.toDto(Arrays.asList(s1, s2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getTeacher_id()).isEqualTo(100L);
        assertThat(dtos.get(0).getUsers()).containsExactly(200L, 201L);
        assertThat(dtos.get(1).getTeacher_id()).isEqualTo(101L);
        assertThat(dtos.get(1).getUsers()).containsExactly(200L);
    }

    @Test
    void toEntity_shouldHandleNullTeacherId_andNullUsers() {
        SessionDto d = new SessionDto();
        d.setId(9L);
        d.setName("Nulls");
        d.setDescription("Null branches");
        d.setTeacher_id(null);
        d.setUsers(null); // branche Optional.ofNullable(...).orElseGet(...)

        Session entity = sessionMapper.toEntity(d);

        assertThat(entity.getTeacher()).isNull();
        assertThat(entity.getUsers()).isEmpty();
    }

    @Test
    void toDto_shouldHandleNullTeacher_andNullUsers() {
        Session s = new Session();
        s.setId(9L);
        s.setName("Nulls");
        s.setDescription("Null branches");
        s.setTeacher(null); // branche session.teacher == null
        s.setUsers(null); // branche Optional.ofNullable(...).orElseGet(...)

        SessionDto dto = sessionMapper.toDto(s);

        assertThat(dto.getTeacher_id()).isNull();
        assertThat(dto.getUsers()).isEmpty();
    }
}