package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import javax.servlet.ServletException;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthTokenFilterTest {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void beforeEach() {
        // Initialize the request, response, and filterChain for each test
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void testDoFilterInternalWithValidJWT() throws IOException, ServletException {
        // Test case for a valid JWT
        String jwt = "validJwt";
        String username = "test@mail.com";
        UserDetails userDetails = mock(UserDetails.class);

        // Add a valid JWT token to the request header
        request.addHeader("Authorization", "Bearer " + jwt);

        // Mock the behavior of JwtUtils and UserDetailsServiceImpl
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Call the doFilterInternal method of AuthTokenFilter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify that the JWT token is validated, username is retrieved, and user
        // details are loaded
        verify(jwtUtils, times(1)).validateJwtToken(jwt);
        assertTrue(jwtUtils.validateJwtToken(jwt));
        verify(jwtUtils, times(1)).getUserNameFromJwtToken(jwt);
        assertEquals(jwtUtils.getUserNameFromJwtToken(jwt), username);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        assertEquals(userDetailsService.loadUserByUsername(username), userDetails);
    }

    @Test
    void testDoFilterInternalWithInvalidJWT() throws IOException, ServletException {
        // Test case for an invalid JWT
        String jwt = "invalidJwt";

        // Add an invalid JWT token to the request header
        request.addHeader("Authorization", "Bearer " + jwt);

        // Mock the behavior of JwtUtils to return false for validation
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false);

        // Call the doFilterInternal method of AuthTokenFilter
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify that the JWT token is invalidated and user details are not loaded
        verify(jwtUtils, times(1)).validateJwtToken(jwt);
        assertFalse(jwtUtils.validateJwtToken(jwt));
        verify(jwtUtils, never()).getUserNameFromJwtToken(jwt);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void testDoFilterInternalWithoutAuthorization() throws IOException, ServletException {
        // Test case for a request without Authorization header
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify that JwtUtils and UserDetailsServiceImpl methods are never called
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(jwtUtils, never()).getUserNameFromJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}
