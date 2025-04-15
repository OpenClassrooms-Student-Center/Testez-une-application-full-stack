package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SessionMapperTest {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    void shouldMapAndSaveSession() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Alice");
        teacher.setLastName("Johnson");
        teacher = teacherRepository.save(teacher);

        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1 = userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2 = userRepository.save(user2);

        Session session = new Session();
        session.setName("Yoga session");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(List.of(user1, user2));
        session.setDescription("This is a test session description.");

        Session savedSession = sessionRepository.save(session);
        SessionDto sessionDto = sessionMapper.toDto(savedSession);

        assertNotNull(sessionDto);
        assertNotNull(sessionDto);
        assertEquals(savedSession.getName(), sessionDto.getName());
        assertEquals(savedSession.getDate().getTime(), sessionDto.getDate().getTime());
        assertEquals(savedSession.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(savedSession.getDescription(), sessionDto.getDescription());
        assertEquals(savedSession.getUsers().size(), sessionDto.getUsers().size());
        assertTrue(sessionDto.getUsers().contains(user1.getId()));
        assertTrue(sessionDto.getUsers().contains(user2.getId()));
    }

    @Test
    void shouldMapSessionDtoToSessionAndPersist() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Bob");
        teacher.setLastName("Williams");
        teacher = teacherRepository.save(teacher);
        User user1 = userRepository.save(new User()
                .setEmail("user3@example.com")
                .setFirstName("Emily")
                .setLastName("Brown")
                .setPassword("password123")
                .setAdmin(false));

        User user2 = userRepository.save(new User()
                .setEmail("user4@example.com")
                .setFirstName("Michael")
                .setLastName("Davis")
                .setPassword("password123")
                .setAdmin(false));

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Advanced Yoga Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user1.getId(), user2.getId()));

        Session session = sessionMapper.toEntity(sessionDto);
        Session savedSession = sessionRepository.save(session);

        assertNotNull(savedSession);
        assertNotNull(savedSession.getId());
        assertEquals(sessionDto.getDescription(), savedSession.getDescription());
        assertEquals(sessionDto.getTeacher_id(), savedSession.getTeacher().getId());

        assertEquals(2, savedSession.getUsers().size());
        assertTrue(savedSession.getUsers().stream()
                .map(User::getId)
                .allMatch(id -> List.of(user1.getId(), user2.getId()).contains(id)));

        assertNotNull(savedSession.getCreatedAt());
        assertNotNull(savedSession.getUpdatedAt());
    }
}
