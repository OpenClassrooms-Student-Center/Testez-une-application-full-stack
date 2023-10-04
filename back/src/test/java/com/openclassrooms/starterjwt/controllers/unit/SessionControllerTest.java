package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.services.SessionService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

	@Mock
	private SessionMapper sessionMapper;

	@Mock
	private SessionService sessionService;

	private SessionController sessionController;

	@BeforeEach
	public void setUp() {
		// create a sessionController
		sessionController = new SessionController(sessionService, sessionMapper);
	}

	// Test to verify that the findById method of the sessionController returns a
	// sessionDto
	@Test
	public void sessionFindByIdOk() {
		Long id = 1L;
		String name = "Session name";
		Session session = Session.builder().id(id).name(name).build();

		// create a sessionDto and set its id and name
		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(id);
		sessionDto.setName(name);

		// define the behavior of methods in sessionService and sessionMapper
		when(sessionService.getById(id)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);

		// call findById method
		ResponseEntity<?> response = sessionController.findById(id.toString());

		// verify that the response status code is 200 and the response body is the
		// sessionDto
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), sessionDto);
	}

	// Test to not a session when the id is not found
	@Test
	public void sessionFindByIdNotFound() {
		Long id = 0L;
		// define the behavior of the sessionService method
		when(sessionService.getById(id)).thenReturn(null);

		// call findById method
		ResponseEntity<?> response = sessionController.findById(id.toString());

		// verify that the sessionService method was called
		verify(sessionService).getById(id);

		// verify that the response status code is 404 and the response body is null
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(response.getBody(), null);
	}

	// Test to verify that the findById method of the sessionController returns a
	// bad request when the id is not valid
	@Test
	public void sessionFindByIdBadRequest() {
		// call findById method with an invalid id
		ResponseEntity<?> response = sessionController.findById("notanumber");

		// verify that the response status code is 400
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	// Test to verify that the findAll method of the sessionController returns a
	// list of sessionDto
	@Test
	public void sessionFindAllok() {
		// create a list of sessions and sessionDtos
		List<Session> sessions = new ArrayList<>();
		sessions.add(new Session());
		sessions.add(new Session());

		List<SessionDto> sessionDtos = new ArrayList<>();
		sessionDtos.add(new SessionDto());
		sessionDtos.add(new SessionDto());

		// define the behavior of the sessionService and sessionMapper methods
		when(sessionService.findAll()).thenReturn(sessions);
		when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

		// call findAll method
		ResponseEntity<?> responseEntity = sessionController.findAll();

		// verify that the status code is 200 and the response body is the list of
		// sessionDtos
		assertEquals(200, responseEntity.getStatusCodeValue());
		assertEquals(sessionDtos, responseEntity.getBody());
	}

	// Test to verify that the create method of the sessionController returns a
	// sessionDto
	@Test
	public void sessionCreateOk() {
		String name = "new session";
		// create a session and sessionDto
		Session session = Session.builder().name(name).build();
		SessionDto sessionDto = new SessionDto();
		sessionDto.setName(name);

		// define the behavior of the sessionMapper and sessionService methods
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.create(session)).thenReturn(session);

		// call create method
		ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

		// verify that the status code is 201 and the response body is the sessionDto
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(sessionDto, responseEntity.getBody());
	}

	// Test to update a session
	@Test
	public void sessionUpdateOk() {
		Long id = 1L;
		String name = "created session";
		// create a session and sessionDto
		Session session = Session.builder().id(id).name(name).build();
		SessionDto sessionDto = new SessionDto();
		sessionDto.setId(id);
		sessionDto.setName("updated session");

		// define the behavior of the sessionMapper and sessionService methods
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		when(sessionService.update(id, session)).thenReturn(session);

		// call update method
		ResponseEntity<?> responseEntity = sessionController.update(id.toString(), sessionDto);

		// verify that the status code is 200 and the response body is the sessionDto
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(sessionDto, responseEntity.getBody());
	}

	// Test to delete a session
	public void sessionDeleteOk() {
		Long id = 1L;
		String name = "Session name";
		// create a session
		Session session = Session.builder().id(id).name(name).build();

		// define the behavior of the sessionService method
		when(sessionService.getById(id)).thenReturn(session);

		// call delete method
		ResponseEntity<?> response = sessionController.save(id.toString());

		// verify that the status code is 204
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// Test the participate method
	@Test
	public void sessionParticipateOk() {
		Long userId = 1L;
		Long sessionId = 1L;
		// call participate method
		ResponseEntity<?> response = sessionController.participate(sessionId.toString(), userId.toString());

		// verify that the status code is 200
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// Test the noLongerParticipate method
	@Test
	public void sessionNoLongerParticipateOk() {
		Long userId = 1L;
		Long sessionId = 1L;
		// call noLongerParticipate method
		ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), userId.toString());
		// verify that the status code is 204
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}