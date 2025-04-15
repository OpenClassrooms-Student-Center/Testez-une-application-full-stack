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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EntityMapperTest {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    void shouldMapListOfEntitiesToListOfDtos() {
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

        Session session1 = new Session();
        session1.setName("Yoga session morning");
        session1.setDate(new Date());
        session1.setTeacher(teacher);
        session1.setUsers(List.of(user1, user2));
        session1.setDescription("This is a test session description.");

        sessionRepository.saveAll(List.of(session, session1));

        List<Session> sessions = sessionRepository.findAll();
        List<SessionDto> sessionDtos = sessionMapper.toDto(sessions);

        assertThat(sessionDtos).hasSize(2);
        assertThat(sessionDtos.get(0).getDescription()).isEqualTo(session.getDescription());
        assertThat(sessionDtos.get(1).getDescription()).isEqualTo(session1.getDescription());
    }

    @Test
    void shouldMapListOfDtosToListOfEntities() {
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

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Advanced Yoga Session");
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user1.getId(), user2.getId()));

        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setName("Dance");
        sessionDto1.setDate(new Date());
        sessionDto1.setDescription("Dance Session");
        sessionDto1.setTeacher_id(teacher.getId());
        sessionDto1.setUsers(List.of(user1.getId(), user2.getId()));

        List<SessionDto> sessionDtos = List.of(sessionDto, sessionDto1);

        List<Session> sessions = sessionMapper.toEntity(sessionDtos);
        sessionRepository.saveAll(sessions);
        List<Session> savedSessions = sessionRepository.findAll();

        assertThat(savedSessions).hasSize(2);
        assertThat(savedSessions.get(0).getDescription()).isEqualTo(sessionDto.getDescription());
        assertThat(savedSessions.get(1).getDescription()).isEqualTo(sessionDto1.getDescription());
    }
}