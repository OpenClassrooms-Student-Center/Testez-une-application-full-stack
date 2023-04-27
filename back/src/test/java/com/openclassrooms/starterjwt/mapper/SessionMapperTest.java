package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Test
    public void testToEntitySession() {

        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Session de test");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Arrays.asList(user1.getId(), user2.getId()));

        // when
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);

        Session session = sessionMapper.toEntity(sessionDto);

        // Then
        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
    }

    @Test
    public void testToDtoSession() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("Session de test");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
    }

    @Test
    public void testToEntityList() {

        // Given
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Session de test");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Arrays.asList(user1.getId(), user2.getId()));
        List<SessionDto> sessionDtoList = new ArrayList<SessionDto>();
        sessionDtoList.add(sessionDto);

        // when
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);

        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        // Then
        assertNotNull(sessionList);
        assertEquals(sessionDtoList.get(0).getId(), sessionList.get(0).getId());
        assertEquals(sessionDtoList.get(0).getDescription(), sessionList.get(0).getDescription());
    }

    @Test
    public void testToDtoList() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Session session = new Session();
        session.setId(1L);
        session.setDescription("Session de test");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        List<Session> sessionList = new ArrayList<Session>();
        sessionList.add(session);

        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        assertNotNull(sessionDtoList);
        assertEquals(sessionDtoList.get(0).getId(), sessionList.get(0).getId());
        assertEquals(sessionDtoList.get(0).getDescription(), sessionList.get(0).getDescription());
    }

}