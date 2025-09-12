package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.FilterChain;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthTokenFilter.
 * - No Spring context
 * - All dependencies mocked
 * - Private fields injected via ReflectionTestUtils
 */
class AuthTokenFilterTest {

    private AuthTokenFilter filter;
    private JwtUtils jwtUtils;
    private com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        filter = new AuthTokenFilter();
        jwtUtils = mock(JwtUtils.class);
        userDetailsService = mock(com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl.class);

        // Inject mocks into private @Autowired fields
        ReflectionTestUtils.setField(filter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(filter, "userDetailsService", userDetailsService);

        // Always start with a clean security context
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    /**
     * When a valid Bearer JWT is present in the Authorization header, the filter
     * should:
     * - validate the token,
     * - load the user details,
     * - set a UsernamePasswordAuthenticationToken into the SecurityContext,
     * - and continue the filter chain.
     */
    void doFilterInternal_setsAuthentication_whenValidJwt() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer good.jwt.token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtils.validateJwtToken("good.jwt.token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("good.jwt.token")).thenReturn("alice@example.com");
        User springUser = new User("alice@example.com", "pwd", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("alice@example.com")).thenReturn(springUser);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);

        // Assert authentication has been set in the SecurityContext
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getPrincipal()).isEqualTo(springUser);
        assertThat(auth.isAuthenticated()).isTrue();
    }

    @Test
    /**
     * When the Authorization header contains a Bearer token but validation fails,
     * the filter should NOT set any authentication and must continue the chain.
     */
    void doFilterInternal_doesNotAuthenticate_whenInvalidJwt() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer bad.jwt.token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtils.validateJwtToken("bad.jwt.token")).thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);

        // No authentication should be set
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    /**
     * If there is no Authorization header, the filter should do nothing and
     * continue the chain
     * without setting any authentication.
     */
    void doFilterInternal_doesNotAuthenticate_whenNoAuthorizationHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(); // no header
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    /**
     * If JWT processing throws an exception, the filter must catch it, not set
     * authentication,
     * and still continue the filter chain (fail-safe behavior).
     */
    void doFilterInternal_continuesChain_whenJwtProcessingThrows() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer boom.jwt.token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(jwtUtils.validateJwtToken("boom.jwt.token")).thenThrow(new RuntimeException("boom"));

        // The filter must catch the exception and continue the chain
        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    /**
     * If the Authorization header does not start with "Bearer ", the filter should
     * ignore it,
     * not attempt token validation, and continue the chain without setting
     * authentication.
     */
    void doFilterInternal_ignoresNonBearerAuthorization() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Token xyz"); // not "Bearer "
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}