package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionMapperTest {

    @Autowired
    private SessionMapperImpl sessionMapper;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    // Test for converting SessionDto to Session entity
    @Test
    public void sessionDtoToSessionOk() {
        // Create a teacher and set its id
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Create 2 users and set their ids
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        // Create a SessionDto and set its properties
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Arrays.asList(user1.getId(), user2.getId()));

        // Define the behavior when methods of teacherService and userService are called
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);

        // Call the toEntity method of the sessionMapper
        Session session = sessionMapper.toEntity(sessionDto);

        // Verify that the Session and SessionDto have the same properties
        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
    }

    // Test for converting Session entity to SessionDto
    @Test
    public void sessionToSessionDtoOk() {
        // Create a teacher and set its id
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Create 2 users and set their ids
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        // Create a Session and set its properties
        Session session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        // Call the toDto method of the sessionMapper
        SessionDto sessionDto = sessionMapper.toDto(session);

        // Verify that the SessionDto and the Session have the same properties
        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
    }

    // Test for converting a list of SessionDto to a list of Session entities
    @Test
    public void sessionDtoListToSessionEntityListOk() {
        // Create a teacher and set its id
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Create 2 users and set their ids
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        // Create a SessionDto and set its properties
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setDescription("Test Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(Arrays.asList(user1.getId(), user2.getId()));

        // Create a list of SessionDto and add the SessionDto
        List<SessionDto> sessionDtoList = Arrays.asList(sessionDto);

        // Define the behavior when methods of teacherService and userService are called
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);

        // Call the toEntity method of the sessionMapper
        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        // Verify that the Session list is not null and has the same properties
        assertNotNull(sessionList);
        assertEquals(sessionDtoList.get(0).getId(), sessionList.get(0).getId());
        assertEquals(sessionDtoList.get(0).getDescription(), sessionList.get(0).getDescription());
    }

    // Test for converting a list of Session entities to a list of SessionDto
    @Test
    public void sessionEntityListToSessionDtoListOk() {
        // Create a teacher and set its id
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Create 2 users and set their ids
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        // Create a Session and set its properties
        Session session = new Session();
        session.setId(1L);
        session.setDescription("Test Session");
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        // Create a list of Session and add the Session
        List<Session> sessionList = Arrays.asList(session);

        // Call the toDto method of the sessionMapper
        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        // Verify that the SessionDto list is not null and has the same properties
        assertNotNull(sessionDtoList);
        assertEquals(sessionList.get(0).getId(), sessionDtoList.get(0).getId());
        assertEquals(sessionList.get(0).getDescription(), sessionDtoList.get(0).getDescription());
    }
}
