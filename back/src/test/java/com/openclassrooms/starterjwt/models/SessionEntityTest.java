package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionEntityTest {

    private Session session;

    // Define sample data for testing
    private Long id = 11L;
    private String name = "Yoga advanced";
    private Date date = new Date();
    private String description = "Session yoga for advanced level";
    private Teacher teacher = new Teacher();
    private List<User> users = new ArrayList<>();
    private User user = new User();
    private LocalDateTime createdAt = LocalDateTime.MAX;
    private LocalDateTime updatedAt = LocalDateTime.MIN;

    @BeforeEach
    public void beforeEach() {
        // Initialize a Session object for testing
        session = Session.builder()
                .id(id)
                .name(name)
                .date(date)
                .description(description)
                .teacher(teacher)
                .users(users)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    public void sessionEntitySetIdOk() {
        // Test setting the ID property of the Session object
        session.setId(200L);
        // Verify that the ID is set correctly
        assertEquals(200L, session.getId());
    }

    @Test
    public void sessionEntitySetNameOk() {
        // Test setting the name property of the Session object
        session.setName("YogaAdvanced");
        // Verify that the name is set correctly
        assertEquals("YogaAdvanced", session.getName());
    }

    @Test
    public void sessionEntitySetDateOk() {
        // Test setting the date property of the Session object
        Date dateTest = new Date();
        session.setDate(dateTest);
        // Verify that the date is set correctly
        assertEquals(session.getDate().hashCode(), dateTest.hashCode());
    }

    @Test
    public void sessionEntitySetDescriptionOk() {
        // Test setting the description property of the Session object
        session.setDescription("description Maj");
        // Verify that the description is set correctly
        assertEquals("description Maj", session.getDescription());
    }

    @Test
    public void sessionEntitySetTeacherOk() {
        // Test setting the teacher property of the Session object
        Teacher teacherTest = new Teacher();
        session.setTeacher(teacherTest);
        // Verify that the teacher is set correctly
        assertEquals(teacher.hashCode(), session.getTeacher().hashCode());
    }

    @Test
    public void sessionEntitySetUsersOk() {
        // Test setting the users property of the Session object
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        session.setUsers(userList);
        // Verify that the users list is set correctly
        assertEquals(session.getUsers().get(0), user);
    }

    @Test
    public void sessionEntitySetCreatedAtOk() {
        // Test setting the createdAt property of the Session object
        LocalDateTime nowDateStored = LocalDateTime.now();
        session.setCreatedAt(nowDateStored);
        // Verify that createdAt is set correctly
        assertEquals(session.getCreatedAt(), nowDateStored);
    }

    @Test
    public void sessionEntitySetUpdatedAtOk() {
        // Test setting the updatedAt property of the Session object
        LocalDateTime nowDateStored = LocalDateTime.now();
        session.setUpdatedAt(nowDateStored);
        // Verify that updatedAt is set correctly
        assertEquals(session.getUpdatedAt(), nowDateStored);
    }

}
