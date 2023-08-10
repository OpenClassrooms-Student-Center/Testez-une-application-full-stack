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
import static org.mockito.Mockito.when;

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

    //Affichage de la liste des sessions
    @Test
    @DisplayName("Session List")
    void whenGetSessionList_thenReturnSessionList(){

        List<Session> sessions = new ArrayList<>();

        when(sessionService.findAll()).thenReturn(sessions);

        List<SessionDto> sessionDto = sessionMapper.toDto(sessions);

        ResponseEntity<?> expectedResponse = ResponseEntity.ok(sessionDto);
        ResponseEntity<?> actualResponse = sessionController.findAll();

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
