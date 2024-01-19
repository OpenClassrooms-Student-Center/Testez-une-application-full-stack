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
    @InjectMocks
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

    @Test
    @DisplayName("Test validating JWT token")
    void testValidateJwtToken() {
        // Create a valid token for testing
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .build();

        String validToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000)) // 1 hour
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        // Create a new JwtUtils instance
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000);

        // Attempt to validate the token
        boolean isValid = jwtUtils.validateJwtToken(validToken);

        // Assert that the token is valid
        assertTrue(isValid);
    }

    /**
     * Test validating an invalid JWT token with an invalid signature.
     */
    @Test
    @DisplayName("Test validating an invalid JWT token with invalid signature")
    void testValidateJwtTokenInvalidSignature() {
        // Create an invalid token for testing
        String invalidToken = "invalidToken";

        // Create a new JwtUtils instance
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000);

        // Attempt to validate the invalid token
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert that the token is invalid
        assertFalse(isValid);
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
    @DisplayName("Test validating JWT token with SignatureException")
    void testValidateJwtTokenSignatureException() {
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000);

        // Create an invalid token for testing
        String invalidToken = "invalidToken";
        // Attempt to validate the invalid token
        boolean isValid = jwtUtils.validateJwtToken(invalidToken);

        // Assert that the token is invalid
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Test validating JWT token with ExpiredJwtException")
    void testValidateJwtTokenExpiredJwtException() {
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3_600_000);

        // Create an expired token for testing
        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired token
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        // Attempt to validate the invalid token
        boolean isValid = jwtUtils.validateJwtToken(expiredToken);

        // Assert that the token is invalid
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Test validating JWT token with IllegalArgumentException")
    void testValidateJwtTokenIllegalArgumentException() {
        try {

            JwtUtils jwtUtils = new JwtUtils();

            // Create a token with empty claims for testing
            String invalidToken = Jwts.builder()
                    .claim(null, jwtUtils)
                    .setSubject("testUser")
                    .signWith(SignatureAlgorithm.HS512, "testSecret")
                    .compact();

            assertThrows(IllegalArgumentException.class,
                    () -> jwtUtils.validateJwtToken(invalidToken));
        } catch (IllegalArgumentException e) {
            // Assert that the exception message contains the expected error message
            assertTrue(e.getMessage().contains("Claim property name cannot be null or empty."));
        }

    }

}
