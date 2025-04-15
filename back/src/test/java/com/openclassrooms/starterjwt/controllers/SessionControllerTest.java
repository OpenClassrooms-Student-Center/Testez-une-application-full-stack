package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;

    @BeforeEach
    void setup() {
        sessionRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testFindAll_Success() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
        teacherRepository.save(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName(" Smith");
        teacherRepository.save(teacher2);

        Session session1 = new Session();
        session1.setName("Yoga Session");
        session1.setDescription("A relaxing yoga session.");
        session1.setDate(new Date());
        session1.setTeacher(teacher1);
        sessionRepository.save(session1);

        Session session2 = new Session();
        session2.setName("Java Workshop");
        session2.setDescription("Intensive Java coding workshop.");
        session2.setDate(new Date());
        session2.setTeacher(teacher2);
        sessionRepository.save(session2);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Yoga Session")))
                .andExpect(jsonPath("$[1].name", is("Java Workshop")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testFindAll_EmptyList() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testFindById_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Yoga Session")))
                .andExpect(jsonPath("$.description", is("A relaxing yoga session.")));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testFindById_NotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testFindById_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreate_Success() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("A relaxing yoga session.");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Yoga Session")));

        assertTrue(sessionRepository.findAll()
                .stream()
                .anyMatch(session -> session.getName().equals("Yoga Session")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreate_MissingFields() throws Exception {

        SessionDto sessionDto = new SessionDto();
        sessionDto.setDate(new Date());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreate_Unauthorized() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session");
        sessionDto.setDescription("A relaxing yoga session.");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdate_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setName("Advanced Yoga Session");
        updatedSessionDto.setDescription("A more advanced yoga session.");
        updatedSessionDto.setDate(new Date());
        updatedSessionDto.setTeacher_id(teacher.getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSessionDto)))
                .andExpect(status().isOk()) // Ожидаем 200 OK
                .andExpect(jsonPath("$.name", is("Advanced Yoga Session")))
                .andExpect(jsonPath("$.description", is("A more advanced yoga session.")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdate_BadRequest() throws Exception {
        SessionDto updatedSessionDto = new SessionDto();
        updatedSessionDto.setDate(new Date());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedSessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDelete_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        assertTrue(sessionRepository.findById(session.getId()).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(sessionRepository.findById(session.getId()).isPresent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDelete_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDelete_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testParticipate_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("test@example.com");
        userRepository.save(user);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertTrue(sessionService.getById(session.getId()).getUsers().contains(user));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testParticipate_SessionNotFound() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/9999" + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testParticipate_UserNotFound() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/" + session.getId() + "/participate/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testParticipate_BadRequest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/abc/participate/xyz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testNoLongerParticipate_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("test@example.com");
        userRepository.save(user);

        sessionService.participate(session.getId(), user.getId());

        assertTrue(sessionService.getById(session.getId()).getUsers().contains(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + session.getId() + "/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(sessionService.getById(session.getId()).getUsers().contains(user));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testNoLongerParticipate_SessionNotFound() throws Exception {
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("test@example.com");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/9999/participate/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testNoLongerParticipate_UserNotFound() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacherRepository.save(teacher);

        Session session = new Session();
        session.setName("Yoga Session");
        session.setDescription("A relaxing yoga session.");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session = sessionRepository.save(session);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/" + session.getId() + "/participate/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testNoLongerParticipate_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/session/abc/participate/xyz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}