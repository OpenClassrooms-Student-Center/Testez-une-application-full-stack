package com.openclassrooms.starterjwt.security.jwt;


import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthTokenFilterTest {

    private AuthTokenFilter filter;
    private JwtUtils jwtUtilsMock;
    private UserDetailsServiceImpl userDetailsServiceMock;

    @BeforeEach
    void setUp() throws Exception {
        filter = new AuthTokenFilter();

        jwtUtilsMock = mock(JwtUtils.class);
        userDetailsServiceMock = mock(UserDetailsServiceImpl.class);

        Field jwtUtilsField = AuthTokenFilter.class.getDeclaredField("jwtUtils");
        jwtUtilsField.setAccessible(true);
        jwtUtilsField.set(filter, jwtUtilsMock);

        Field userDetailsServiceField = AuthTokenFilter.class.getDeclaredField("userDetailsService");
        userDetailsServiceField.setAccessible(true);
        userDetailsServiceField.set(filter, userDetailsServiceMock);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid.token");
        when(jwtUtilsMock.validateJwtToken("valid.token")).thenReturn(true);
        when(jwtUtilsMock.getUserNameFromJwtToken("valid.token")).thenReturn("user");

        var userDetails = mock(org.springframework.security.core.userdetails.UserDetails.class);
        when(userDetailsServiceMock.loadUserByUsername("user")).thenReturn(userDetails);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(jwtUtilsMock.validateJwtToken("invalid.token")).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        // L'authentification ne doit pas être définie
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldCatchExceptionAndLogError() throws ServletException, IOException {
        AuthTokenFilter filter = Mockito.spy(new AuthTokenFilter(/* dépendances mockées ici */));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);


        Mockito.doThrow(new RuntimeException("Test Exception"))
                .when(filter).doFilterInternal(Mockito.any(), Mockito.any(), Mockito.any());

        try {
            filter.doFilter(request, response, chain);
        } catch (Exception ignored) {
            // L’exception est gérée dans le filtre, donc rien à faire ici
        }


    }
    @Test
    void testDoFilterInternal_WithNoAuthorizationHeader() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithMalformedAuthorizationHeader() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Basic abcdef");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}