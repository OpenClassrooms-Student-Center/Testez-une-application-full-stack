
package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;


    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        Field secretField = JwtUtils.class.getDeclaredField("jwtSecret");
        secretField.setAccessible(true);
        secretField.set(jwtUtils, "testSecret");

        Field expField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        expField.setAccessible(true);
        expField.set(jwtUtils, 1000 * 60);
    }

    @Test
    void testGenerateAndValidateJwtToken() {
        // Utilisation du builder pour créer UserDetailsImpl
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("user")
                .firstName("first")
                .lastName("last")
                .admin(false)
                .password("pwd")
                .build();

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testGetUserNameFromJwtToken() {
        String username = "user";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        String extracted = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(username, extracted);
    }

    @Test
    void testValidateJwtTokenWithInvalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void testValidateJwtTokenWithExpiredToken() {
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date(System.currentTimeMillis() - 120000))
                .setExpiration(new Date(System.currentTimeMillis() - 60000))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForExpiredToken() {
        String expiredToken = "token_expiré"; // génère un vrai token expiré selon ta logique
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForMalformedToken() {
        assertFalse(jwtUtils.validateJwtToken("n'importe quoi"));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForNullToken() {
        assertFalse(jwtUtils.validateJwtToken(null));
    }

    @Test
    void validateJwtToken_shouldReturnFalseForEmptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}