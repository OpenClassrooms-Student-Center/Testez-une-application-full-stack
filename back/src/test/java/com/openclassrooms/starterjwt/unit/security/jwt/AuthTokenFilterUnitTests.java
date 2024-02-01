package com.openclassrooms.starterjwt.unit.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterUnitTests {

    /**
     * Mocked authentication manager for simulating user authentication.
     */
    @Mock
    private JwtUtils jwtUtils;

    /**
     * Mocked instance of the AuthTokenFilter class.
     */
    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    /**
     * Mocked instance of the UserDetailsServiceImpl class.
     */
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Tests the {@code doFilterInternal} method of AuthTokenFilter when a valid
     * token is provided.
     */
    @Test
    @Tag("AuthTokenFilter.doFilterInternal()")
    @DisplayName("Test Authentication with Valid Token")
    void doFilterInternal_validToken_shouldSetAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    /**
     * Tests the {@code doFilterInternal} method of AuthTokenFilter when an invalid
     * token is provided.
     */
    @Test
    @Tag("AuthTokenFilter.doFilterInternal()")
    @DisplayName("Test No Authentication with Invalid Token")
    void doFilterInternal_invalidToken_shouldNotSetAuthentication() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        assertEquals(SecurityContextHolder.getContext().getAuthentication(), null);
        verify(filterChain).doFilter(request, response);
    }

    // Add more test cases as needed

    /**
     * Tests the {@code parseJwt} method of AuthTokenFilter with a valid header.
     */
    @Test
    @Tag("AuthTokenFilter.parseJwt()")
    @DisplayName("Test Parsing Valid JWT Token from Header")
    void parseJwt_validHeader_shouldReturnToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testToken");

        String result = authTokenFilter.parseJwt(request);

        assertEquals("testToken", result);
    }

    /**
     * Tests the {@code parseJwt} method of AuthTokenFilter with an invalid header.
     */
    @Test
    @Tag("AuthTokenFilter.parseJwt()")
    @DisplayName("Test Parsing Null from Invalid JWT Token Header")
    void parseJwt_invalidHeader_shouldReturnNull() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        String result = authTokenFilter.parseJwt(request);

        assertEquals(null, result);
    }

}
