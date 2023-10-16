package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @InjectMocks
    SessionController sessionController;

    @Mock
    SessionMapper sessionMapper;

    @Mock
    SessionService sessionService;

    @BeforeEach
    void setUp(){
        sessionController = new SessionController(
                sessionService,
                sessionMapper);
    }

    // Cherchez l'utilisateur par l'identifiant
    @Test
    @DisplayName("findById")
    void whenUserIdentityIsOk_thenReturnSession(){
        //GIVEN
        String id = "1";
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();

        //WHEN
        when(sessionService.getById(Long.valueOf(id))).thenReturn(session);

        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> expectedUser = ResponseEntity.ok().body(sessionDto);

        ResponseEntity<?> actualUser = sessionController.findById(id);

        //THEN
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualUser).isEqualTo(expectedUser);

    }

    @Test
    @DisplayName("findById not found")
    void whenUserSessionIsNull_thenReturnNotFound(){
        String id = "1";
        Session session = null;

        when(sessionService.getById(Long.parseLong(id))).thenReturn(session);

        ResponseEntity<?>expectedNotFound = sessionController.findById(id);

        assertThat(expectedNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("findById catch")
    void whenTryFail_thenThrowNumberFormatException(){

        String id = "hello 123";

      Exception exception = assertThrows(NumberFormatException.class, ()->{
          Long identity = Long.parseLong(id);
      });

      assertEquals("For input string: \"hello 123\"", exception.getMessage());
    }
    @Test
    @DisplayName("findAll")
    void whenGetSessionList_thenReturnSessionList(){

        List<Session> sessions = new ArrayList<>();

        when(sessionService.findAll()).thenReturn(sessions);

        List<SessionDto> sessionDto = sessionMapper.toDto(sessions);

        ResponseEntity<?> expectedResponse = ResponseEntity.ok(sessionDto);
        ResponseEntity<?> actualResponse = sessionController.findAll();

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse).isEqualTo(expectedResponse);

    }
    @Test
    @DisplayName("create")
    void whenSessionDtoIsOk_thenReturnSessionBody(){
        //GIVEN
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        //WHEN
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);

        ResponseEntity<?> sessionBody = ResponseEntity.ok().body(
                sessionMapper.toDto(session));

        ResponseEntity<?> createSession = sessionController.create(sessionDto);

        //THEN
        assertThat(createSession.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createSession).isEqualTo(sessionBody);
    }
    @Test
    @DisplayName("Update")
    void whenUserIdentityAndSessionAreGood_thenAllowUpdate(){
    String id ="2";
    SessionDto sessionDto = new SessionDto();
    Session session = new Session();

    when(sessionService.update(Long.parseLong(id),
            sessionMapper.toEntity(sessionDto))).thenReturn(session);

    ResponseEntity<?> updateResponse = ResponseEntity.ok()
            .body(sessionMapper.toDto(session));

    ResponseEntity<?> updateSession = sessionController.update(id, sessionDto);

    assertThat(updateSession).isEqualTo(updateResponse);


    }
    @Test
    @DisplayName("update catch")
    void whenIdentityIsNotCorrect_thenThrowsNumberFormatException(){
        String identity = "hello123";
        Session requestSession = new Session();

       Exception exception = assertThrows(NumberFormatException.class, ()->{
           Long id = Long.parseLong(identity);
       });

       assertEquals("For input string: \"hello123\"", exception.getMessage());
    }
    @Test
    @DisplayName("save")
    void whenSessionIdIdentified_thenDeleteIdentity(){
        //GIVEN
        String id = "3";
        //WHEN
        sessionService.getById(Long.valueOf(id));
        sessionService.delete(Long.parseLong(id));

        //THEN
        verify(sessionService, times(1))
                 .delete(Long.parseLong(id));

    }
    @Test
    @DisplayName("save not found")
    void whenSessionIsNull_thenReturnNotFound(){
        String id = "4";
        Session session = null;

        when(sessionService.getById(Long.valueOf(id))).thenReturn(session);

        ResponseEntity<?> notFound = ResponseEntity.notFound().build();

        ResponseEntity<?> sessionId = sessionController.save(id);

        assertThat(sessionId).isEqualTo(notFound);
        assertThat(sessionId.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("save catch")
    void whenTryFail_thenCatchNumberFormatException(){
        String id = "abc123";

        Exception exception =  assertThrows(NumberFormatException.class, ()->{
            Long identity = Long.valueOf(id);
        });

        assertEquals("For input string: \"abc123\"", exception.getMessage());
    }

    @Test
    @DisplayName("participate")
    void whenIdAndUserIdIsOk_thenReturnResponseEntityOk(){
        String id = "1";
        String userId = "1";

        sessionService.participate(Long.parseLong(id), Long.parseLong(userId));

        assertThat(sessionController.participate(id, userId))
                .isEqualTo(ResponseEntity.ok().build());

        verify(sessionService, times(2))
                .participate(Long.parseLong(id), Long.parseLong(userId));
    }
    @Test
    @DisplayName("participate catch")
    void whenTryFail_thenThrowNumberFormatExceptionForParticipate(){
        String id = "session12";
        String userId = "user1";

        assertThrows(NumberFormatException.class, ()->{
          Long.parseLong(id);
            //Long.parseLong(userId);
        });
    }
    @Test
    @DisplayName("noLongerParticipate")
    void whenIdentityAndUserIdFound_thenDeleteParticipate(){
        String id = "5";
        String userId = "5";

        sessionService.noLongerParticipate(Long.parseLong(id),
                Long.parseLong(userId));

        assertThat(sessionController.noLongerParticipate(id, userId))
                .isEqualTo(ResponseEntity.ok().build());
        verify(sessionService, times(2))
                .noLongerParticipate(Long.parseLong(id), Long.parseLong(userId));
    }
    @Test
    @DisplayName("noLongerParticipate")
    void whenTryFail_thenReturnNumberFormatException(){
        String id = "hello4";
        String userId = "hi5";

        assertThrows(NumberFormatException.class, ()->{
            sessionService.noLongerParticipate(Long.parseLong(id),
                    Long.parseLong(userId));
        });
    }
}
