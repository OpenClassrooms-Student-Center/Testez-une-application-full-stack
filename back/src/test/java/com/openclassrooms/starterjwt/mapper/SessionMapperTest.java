package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMapperTest {

    private TeacherService teacherService;
    private UserService userService;
    private SessionMapper mapper;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);

        mapper = new SessionMapper() {
            {
                this.teacherService = teacherService;
                this.userService = userService;
            }
            @Override
            public Session toEntity(SessionDto sessionDto) {
                if (sessionDto == null) return null;
                Session session = new Session();
                session.setDescription(sessionDto.getDescription());
                session.setTeacher(sessionDto.getTeacher_id() != null ? teacherService.findById(sessionDto.getTeacher_id()) : null);
                session.setUsers(
                        sessionDto.getUsers() != null ?
                                sessionDto.getUsers().stream()
                                        .map(userId -> userService.findById(userId))
                                        .filter(java.util.Objects::nonNull)
                                        .collect(java.util.stream.Collectors.toList())
                                : new java.util.ArrayList<>()
                );
                return session;
            }

            @Override
            public SessionDto toDto(Session session) {
                if (session == null) return null;
                SessionDto dto = new SessionDto();
                dto.setDescription(session.getDescription());
                dto.setTeacher_id(session.getTeacher() != null ? session.getTeacher().getId() : null);
                dto.setUsers(
                        session.getUsers() != null ?
                                session.getUsers().stream()
                                        .map(User::getId)
                                        .collect(java.util.stream.Collectors.toList())
                                : new java.util.ArrayList<>()
                );
                return dto;
            }
            @Override
            public List<Session> toEntity(List<SessionDto> dtoList) {
                if (dtoList == null) return new java.util.ArrayList<>();
                return dtoList.stream().map(this::toEntity).collect(java.util.stream.Collectors.toList());
            }
            @Override
            public List<SessionDto> toDto(List<Session> entityList) {
                if (entityList == null) return new java.util.ArrayList<>();
                return entityList.stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
            }
        };
    }

   /* @Test
    void toEntity_shouldMapDtoToEntity() {
        SessionDto dto = new SessionDto();
        dto.setDescription("Yoga session");
        dto.setTeacher_id(10L);
        dto.setUsers(Arrays.asList(1L, 2L));

        Teacher teacher = new Teacher();
        teacher.setId(10L);


        User user1 = new User(); user1.setId(1L);
        User user2 = new User(); user2.setId(2L);

        when(teacherService.findById(10L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session entity = mapper.toEntity(dto);

        assertEquals("Yoga session", entity.getDescription());
        assertEquals(10L, entity.getTeacher().getId());
        assertEquals(2, entity.getUsers().size());
        assertTrue(entity.getUsers().stream().anyMatch(u -> u.getId() == 1L));
        assertTrue(entity.getUsers().stream().anyMatch(u -> u.getId() == 2L));
    }*/

    @Test
    void toDto_shouldMapEntityToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(5L);
        User user1 = new User(); user1.setId(3L);
        User user2 = new User(); user2.setId(4L);

        Session session = new Session();
        session.setDescription("Pilates");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto dto = mapper.toDto(session);

        assertEquals("Pilates", dto.getDescription());
        assertEquals(5L, dto.getTeacher_id());
        assertEquals(2, dto.getUsers().size());
        assertTrue(dto.getUsers().contains(3L));
        assertTrue(dto.getUsers().contains(4L));
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
}