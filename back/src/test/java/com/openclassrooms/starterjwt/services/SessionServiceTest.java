package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	private SessionService sessionService;

	@BeforeEach
	void setUp() {
		sessionService = new SessionService(sessionRepository, userRepository);
	}

	@Test
	void testCreateSession() {
		// Arrange
		Session session = new Session();
		session.setId(1L);
		when(sessionRepository.save(any(Session.class))).thenReturn(session);

		// Act
		Session createdSession = sessionService.create(session);

		// Assert
		assertEquals(session.getId(), createdSession.getId());
	}

	@Test
	void testDeleteSession() {
		// Arrange
		Session session = new Session();
		session.setId(1L);
		doNothing().when(sessionRepository).deleteById(anyLong());

		// Act
		sessionService.delete(session.getId());

		// Assert
		verify(sessionRepository, times(1)).deleteById(session.getId());
	}

	@Test
	void testFindAllSessions() {
		// Arrange
		List<Session> sessionList = new ArrayList<>();
		Session session = new Session();
		session.setId(1L);
		sessionList.add(session);
		when(sessionRepository.findAll()).thenReturn(sessionList);

		// Act
		List<Session> foundSessions = sessionService.findAll();

		// Assert
		assertEquals(1, foundSessions.size());
	}

	@Test
	void testGetSessionById() {
		// Arrange
		Session session = new Session();
		session.setId(1L);
		when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

		// Act
		Session foundSession = sessionService.getById(session.getId());

		// Assert
		assertEquals(session.getId(), foundSession.getId());
	}

	@Test
	void testUpdateSession() {
		// Arrange
		Session session = new Session();
		session.setId(1L);
		when(sessionRepository.save(any(Session.class))).thenReturn(session);

		// Act
		Session updatedSession = sessionService.update(session.getId(), session);

		// Assert
		assertEquals(session.getId(), updatedSession.getId());
	}

	@Test
	void testParticipate() {
		// Arrange
		Long sessionId = 1L;
		Session session = Session.builder().name("Session Test").id(sessionId).users(new ArrayList<>()).build();

		Long userId = 2L;
		User user = User.builder().id(userId).email("test@mail.com").lastName("Doe").firstName("John")
				.password("password").build();

		when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		// Act
		sessionService.participate(session.getId(), user.getId());

		// Assert
		assertEquals(1, session.getUsers().size());
		assertEquals(user.getId(), session.getUsers().get(0).getId());
	}

	@Test
	void testParticipateNotFoundException() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 2L;

		Session session = new Session();
		session.setId(sessionId);

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));

		// Verify
		verify(sessionRepository, times(1)).findById(sessionId);
		verify(userRepository, times(1)).findById(userId);
		verify(sessionRepository, never()).save(any(Session.class));
	}

	@Test
	void testParticipateBadRequestException() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 2L;
		User user = User.builder().id(userId).email("test@mail.com").lastName("Doe").firstName("John")
				.password("password").build();

		List<User> users = new ArrayList<>();
		users.add(user);

		Session session = Session.builder().name("Session 1").id(sessionId).users(users).build();

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// Act and Assert
		assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
	}

	@Test
	void testNoLongerParticipate() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 2L;
		Session session = new Session();
		User user = new User();
		user.setId(userId);
		List<User> users = new ArrayList<>();
		users.add(user);
		session.setUsers(users);

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

		// Act
		sessionService.noLongerParticipate(sessionId, userId);

		// Verify
		verify(sessionRepository, times(1)).findById(sessionId);
		verify(sessionRepository, times(1)).save(session);

		// Assert
		List<User> updatedUsers = session.getUsers();
		assertTrue(updatedUsers.isEmpty());
	}

	@Test
	void testNoLongerParticipateBadRequestException() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 2L;

		Session session = Session.builder().name("Session 1").id(sessionId).users(new ArrayList<>()).build();

		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

		// Act and Assert
		assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
	}

	@Test
	void testNoLongerParticipateNotFoundException() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 2L;
		when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

		// Verify
		verify(sessionRepository, times(1)).findById(sessionId);
		verify(sessionRepository, times(0)).save(any(Session.class));
	}
}
