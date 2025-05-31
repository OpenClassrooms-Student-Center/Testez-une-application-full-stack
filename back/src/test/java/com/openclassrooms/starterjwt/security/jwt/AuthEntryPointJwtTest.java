package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthEntryPointJwtTest {

    @Test
    void testCommenceWritesUnauthorizedResponse() throws IOException, ServletException {
        AuthEntryPointJwt entryPoint = new AuthEntryPointJwt();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = mock(AuthenticationException.class);

        when(request.getServletPath()).thenReturn("/api/test");
        when(exception.getMessage()).thenReturn("Unauthorized access");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public boolean isReady() {
                return true;
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        try {
            entryPoint.commence(request, response, exception);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        ObjectMapper mapper = new ObjectMapper();

        var map = mapper.readValue(baos.toByteArray(), java.util.Map.class);

        assertEquals(401, map.get("status"));
        assertEquals("Unauthorized", map.get("error"));
        assertEquals("Unauthorized access", map.get("message"));
        assertEquals("/api/test", map.get("path"));
    }
}