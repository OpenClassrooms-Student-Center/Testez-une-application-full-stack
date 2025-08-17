package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMapperImplTest {

    private SessionMapperImpl mapper;
    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);
        mapper = (SessionMapperImpl) Mappers.getMapper(SessionMapper.class);
        mapper.teacherService = teacherService;
        mapper.userService = userService;
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        SessionDto dto = new SessionDto();
        dto.setDescription("Yoga");
        dto.setTeacher_id(1L);
        dto.setUsers(Arrays.asList(10L, 20L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(10L)).thenReturn(user1);
        when(userService.findById(20L)).thenReturn(user2);

        Session entity = mapper.toEntity(dto);

        assertEquals("Yoga", entity.getDescription());
        assertEquals(1L, entity.getTeacher().getId());
        assertEquals(2, entity.getUsers().size());
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(2L);
        User user1 = new User(); user1.setId(30L);
        User user2 = new User(); user2.setId(40L);

        Session session = new Session();
        session.setDescription("Pilates");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto dto = mapper.toDto(session);

        assertEquals("Pilates", dto.getDescription());
        assertEquals(2L, dto.getTeacher_id());
        assertTrue(dto.getUsers().contains(30L));
        assertTrue(dto.getUsers().contains(40L));
    }

    @Test
    void toEntity_shouldHandleNullUsers() {
        SessionDto dto = new SessionDto();
        dto.setDescription("No users");
        dto.setTeacher_id(null);
        dto.setUsers(null);

        Session entity = mapper.toEntity(dto);

        assertEquals("No users", entity.getDescription());
        assertNull(entity.getTeacher());
        assertNotNull(entity.getUsers());
        assertTrue(entity.getUsers().isEmpty());
    }

    @Test
    void toDto_shouldHandleNullUsers() {
        Session session = new Session();
        session.setDescription("No users");
        session.setTeacher(null);
        session.setUsers(null);

        SessionDto dto = mapper.toDto(session);

        assertEquals("No users", dto.getDescription());
        assertNull(dto.getTeacher_id());
        assertNotNull(dto.getUsers());
        assertTrue(dto.getUsers().isEmpty());
    }


    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        assertNull(mapper.toEntity((SessionDto) null));
    }

    @Test
    void toDto_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDto((Session) null));
    }

    @Test
    void toEntityList_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toEntity((List<SessionDto>) null));
    }

    @Test
    void toDtoList_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toDto((List<Session>) null));
    }

    @Test
    void toEntityList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<Session> result = mapper.toEntity(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<SessionDto> result = mapper.toDto(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toEntity_shouldHandleUserNotFound() {
        SessionDto dto = new SessionDto();
        dto.setDescription("Test");
        dto.setTeacher_id(null);
        dto.setUsers(Arrays.asList(100L));

        when(userService.findById(100L)).thenReturn(null);

        Session entity = mapper.toEntity(dto);

        assertEquals(1, entity.getUsers().size());
        assertNull(entity.getUsers().get(0));
    }

    @Test
    void toEntityList_shouldHandleNullElement() {
        List<SessionDto> dtos = Arrays.asList(null, new SessionDto());
        List<Session> result = mapper.toEntity(dtos);
        assertEquals(2, result.size());
        assertNull(result.get(0));
    }

    @Test
    void toDtoList_shouldHandleNullElement() {
        List<Session> sessions = Arrays.asList(null, new Session());
        List<SessionDto> result = mapper.toDto(sessions);
        assertEquals(2, result.size());
        assertNull(result.get(0));
    }
}