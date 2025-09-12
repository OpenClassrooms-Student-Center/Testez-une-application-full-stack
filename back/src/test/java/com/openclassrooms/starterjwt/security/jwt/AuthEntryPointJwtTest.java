package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        authEntryPointJwt = new AuthEntryPointJwt();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authException = mock(AuthenticationException.class);
    }

    @Test
    /**
     * When authentication fails, the entry point should set the response status to
     * 401,
     * content type to application/json and write a JSON body containing status,
     * error,
     * message and path fields.
     */
    void commence_setsUnauthorizedResponse() throws Exception {
        when(request.getServletPath()).thenReturn("/api/test");
        when(authException.getMessage()).thenReturn("Bad credentials");

        // Mock getOutputStream() for response JSON body capture
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) {
                baos.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                /* no-op */ }
        };
        when(response.getOutputStream()).thenReturn(sos);

        authEntryPointJwt.commence(request, response, authException);

        // Verify status and content type
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Parse JSON
        String json = baos.toString();
        Map<?, ?> body = new ObjectMapper().readValue(json, Map.class);
        assertThat(body.get("status")).isEqualTo(401);
        assertThat(body.get("error")).isEqualTo("Unauthorized");
        assertThat(body.get("message")).isEqualTo("Bad credentials");
        assertThat(body.get("path")).isEqualTo("/api/test");
    }
}