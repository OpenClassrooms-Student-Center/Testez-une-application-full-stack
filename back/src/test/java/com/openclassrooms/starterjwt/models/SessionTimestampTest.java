package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SessionTimestampTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Test
    void testCreatedAtAndUpdatedAt_ShouldBeSetAutomatically() throws InterruptedException {

        Session session = Session.builder()
                .name("Test Session")
                .date(new Date())
                .description("Some description")
                .build();

        Session savedSession = sessionRepository.save(session);

        assertNotNull(savedSession.getCreatedAt());
        assertNotNull(savedSession.getUpdatedAt());

        Thread.sleep(1000);
        savedSession.setDescription("Updated description");
        sessionRepository.save(savedSession);

        Session updatedSession = sessionRepository.findById(savedSession.getId()).get();
        assertTrue(updatedSession.getUpdatedAt().isAfter(updatedSession.getCreatedAt()));
    }
}
