package com.openclassrooms.starterjwt.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

/**
 * This class contains unit tests for the {@link SessionService} class.
 * 
 * It tests the functionality of creating, deleting, finding, updating,
 * participating, and no
 * longer participating in sessions.
 * 
 * 
 * The tests use Mockito to mock the SessionRepository and UserRepository
 * dependencies.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {
    /**
     * A mock instance of the SessionService class.
     */
    @Mock
    private SessionService sessionService;

    /**
     * A mock instance of the SessionRepository class.
     */
    @Mock
    private SessionRepository sessionRepository;

    /**
     * A mock instance of the UserRepository class.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * An instance of the Session class used for testing.
     */
    private Session session;

    /**
     * Annotated with {@code @BeforeEach}, this method is executed before each test
     * case. It
     * sets up the necessary dependencies and initializes the session object.
     */
    @BeforeEach
    void setUp() {
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        Teacher teacher = new Teacher();
        teacher
                .setId(1L)
                .setLastName("DELAHAYE")
                .setFirstName("Margot")
                .setCreatedAt(currentTime)
                .setUpdatedAt(currentTime);

        User mockedUser = new User("Toto", "Toto",
                "Toto69", "totoBruv", false);
        mockedUser.setId(1L);

        List<User> arrayOfUsers = new ArrayList<>();
        arrayOfUsers.add(mockedUser);

        session = new Session(1L, "TEST session",
                currentDate, "TEST description for the session",
                teacher,
                arrayOfUsers, currentTime,
                currentTime);

        sessionService = new SessionService(sessionRepository, userRepository);
    }

    /**
     * Tests the {@code create()} method of the
     * SessionService class. It mocks the {@code save()} method of the
     * sessionRepository to
     * return the session object
     * and verifies that the {@code save()} method is called.
     * 
     * It asserts that the returned
     * session object is equal to the original session object.
     */
    @Test
    @Tag("SessionService.create()")
    @DisplayName("Test Creating New Session")
    public void testCreatingNewSession() {
        // * Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        // * Act
        Session result = sessionService.create(session);

        // * Assert
        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }

    /**
     * Tests the {@code delete()} method of the SessionService class.
     * It mocks the {@code deleteById()} method of the sessionRepository
     * and verifies that the {@code deleteById()} method is called.
     */
    @Test
    @Tag("SessionService.delete()")
    @DisplayName("Test Deleting a Session")
    public void testDeletingASession() {
        // * Arrange
        doNothing().when(sessionRepository).deleteById(session.getId());

        // * Act
        sessionService.delete(session.getId());

        // * Assert
        verify(sessionRepository).deleteById(session.getId());
    }

    /**
     * Tests the {@code findAll()} method of the SessionService class.
     * It mocks the {@code findAll()} method of the sessionRepository
     * to return a list of sessions and verifies that the {@code findAll()}
     * method is called. It asserts that the returned list of sessions
     * is equal to the original list of sessions.
     */
    @Test
    @Tag("SessionService.findAll()")
    @DisplayName("Test Finding All Sessions")
    public void testFindingAllSessions() {
        // * Arrange
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessions.add(session);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // * Act
        List<Session> result = sessionService.findAll();

        // * Assert
        verify(sessionRepository).findAll();

        assertEquals(sessions, result);
    }

    /**
     * Tests the {@code getById()} method of the SessionService class.
     * It mocks the {@code findById()} method of the sessionRepository
     * to return the session object and verifies that the {@code findById()}
     * method is called. It asserts that the returned session object
     * is equal to the original session object.
     */
    @Test
    @Tag("SessionService.getById()")
    @DisplayName("Test Get Session by Valid ID")
    public void testGetSessionByValidId() {
        // * Arrange
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        // * Act
        Session result = sessionService.getById(session.getId());

        // * Assert
        verify(sessionRepository).findById(session.getId());

        assertEquals(session, result);
    }

    /**
     * Tests the {@code update()} method of the SessionService class.
     * It mocks the {@code save()} method of the sessionRepository
     * to return the session object and verifies that the {@code save()}
     * method is called. It asserts that the returned session object
     * is equal to the original session object.
     */
    @Test
    @Tag("SessionService.update()")
    @DisplayName("Test Updating a Session")
    public void testUpdatingASession() {
        // * Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        // * Act
        Session result = sessionService.update(session.getId(), session);

        // * Assert
        verify(sessionRepository).save(session);

        assertEquals(session, result);
    }

    /**
     * Tests the {@code participate()} method of the SessionService class.
     * It mocks the {@code findById()} method of the sessionRepository to return
     * the session object. It mocks the {@code findById()} method of the
     * userRepository
     * to return the user object. It verifies that the {@code findById()} methods
     * are called and asserts that the session's users list contains the user
     * object.
     */
    @Test
    @Tag("SessionService.participate()")
    @DisplayName("Test Participate in a Session")
    public void testParticipateSession() {
        // * Arrange
        Long userId = 69L;
        Long sessionId = 1L;

        User mockedUser = new User("Toto", "Toto",
                "Toto69", "totoBruv", false);
        mockedUser.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockedUser));

        // * Act
        sessionService.participate(sessionId, userId);

        // * Assert
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);

        assertTrue(session.getUsers().contains(mockedUser));
    }

    /**
     * Tests the {@code noLongerParticipate()} method of the SessionService class.
     * It mocks the {@code findById()} method of the sessionRepository to return the
     * session object. It mocks the {@code save()} method of the sessionRepository
     * to return the session object. It verifies that the {@code findById()} method
     * is called and asserts that the session's users list does not contain the user
     * object.
     */
    @Test
    @Tag("SessionService.noLogerParticipate()")
    @DisplayName("Test No Longer Participate in a Session")
    public void testNoLongerParticipateSession() {
        // * Arrange
        Long userIdToRemove = 420L;
        Long sessionId = 1L;

        User mockedUser = new User("Toto2", "Toto2",
                "Toto420", "totoBlud", false);
        mockedUser.setId(userIdToRemove);

        session.getUsers().add(mockedUser);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        // * Act
        sessionService.noLongerParticipate(sessionId, userIdToRemove);

        // * Assert
        verify(sessionRepository).findById(sessionId);

        assertFalse(session.getUsers().contains(mockedUser));
    }

}
