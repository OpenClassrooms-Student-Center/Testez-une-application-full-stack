package com.openclassrooms.starterjwt.unit.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Unit tests for the {@link JwtUtils} class.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JwtUtilsUnitTests {
    /**
     * Mocked authentication instance for simulating user authentication.
     */
    @Mock
    private Authentication authentication;

    /**
     * Instance of JwtUtils for testing.
     */
    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000); // 1 hour
    }

    /**
     * Test generating a JWT token.
     */
    @Test
    @DisplayName("Test generating JWT token")
    void testGenerateJwtToken() {
        // Mocking Authentication
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .firstName("Toto")
                .lastName("Toto")
                .username("toto3@toto.com")
                .password("test!1234")
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("Test getting username from JWT token")
    void testGetUserNameFromJwtToken() {
        // Sample user details using the builder pattern
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .build();

        // Generate a JWT token
        String validToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000)) // 1 hour
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        // Perform the test
        String username = jwtUtils.getUserNameFromJwtToken(validToken);

        // Assert the result
        assertEquals("testUser", username);
    }

    /**
     * Test validating an invalid JWT token with an invalid signature.
     */
    @Test
    @DisplayName("Test validating an invalid JWT token with invalid signature")
    void testValidateJwtTokenInvalidSignature() {
        String invalidToken = "invalidToken";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    @DisplayName("Test validating JWT token with SignatureException")
    void testValidateJwtTokenSignatureException() {
        String invalidToken = "invalidToken";
        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);
        doThrow(new SignatureException("Invalid JWT signature")).when(jwtUtilsMock).validateJwtToken(invalidToken);
        assertThrows(SignatureException.class, () -> jwtUtilsMock.validateJwtToken(invalidToken));
    }

    @Test
    @DisplayName("Test validating JWT token with MalformedJwtException")
    void testValidateJwtTokenMalformedJwtException() {
        String invalidToken = "invalidToken";
        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);
        doThrow(new MalformedJwtException("Invalid JWT token")).when(jwtUtilsMock).validateJwtToken(invalidToken);
        assertThrows(MalformedJwtException.class, () -> jwtUtilsMock.validateJwtToken(invalidToken));
    }

    @Test
    @DisplayName("Test validating JWT token with ExpiredJwtException")
    void testValidateJwtTokenExpiredJwtException() {
        String invalidToken = "invalidToken";
        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);
        doThrow(new ExpiredJwtException(null, null, "JWT token is expired")).when(jwtUtilsMock)
                .validateJwtToken(invalidToken);
        assertThrows(ExpiredJwtException.class, () -> jwtUtilsMock.validateJwtToken(invalidToken));
    }

    @Test
    @DisplayName("Test validating JWT token with UnsupportedJwtException")
    void testValidateJwtTokenUnsupportedJwtException() {
        String invalidToken = "invalidToken";
        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);
        doThrow(new UnsupportedJwtException("JWT token is unsupported")).when(jwtUtilsMock)
                .validateJwtToken(invalidToken);
        assertThrows(UnsupportedJwtException.class, () -> jwtUtilsMock.validateJwtToken(invalidToken));
    }

    @Test
    @DisplayName("Test validating JWT token with IllegalArgumentException")
    void testValidateJwtTokenIllegalArgumentException() {
        String invalidToken = "invalidToken";
        JwtUtils jwtUtilsMock = Mockito.mock(JwtUtils.class);
        doThrow(new IllegalArgumentException("JWT claims string is empty")).when(jwtUtilsMock)
                .validateJwtToken(invalidToken);
        assertThrows(IllegalArgumentException.class, () -> jwtUtilsMock.validateJwtToken(invalidToken));
    }
}
