package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private static final String TEST_SECRET = "unit-test-secret-key-which-is-long-enough";
    private static final int EXP_MS = 5000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", EXP_MS);
    }

    @Test
    void generateJwtToken_withAuthentication_returnsValidToken() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl principal = mock(UserDetailsImpl.class);
        when(principal.getUsername()).thenReturn("john@example.com");
        when(authentication.getPrincipal()).thenReturn(principal);

        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(token).isNotBlank();
        assertThat(jwtUtils.validateJwtToken(token)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo("john@example.com");
    }

    @Test
    void getUserNameFromJwtToken_ok() {
        String token = Jwts.builder()
                .setSubject("alice@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP_MS))
                .signWith(SignatureAlgorithm.HS512, TEST_SECRET)
                .compact();

        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo("alice@example.com");
    }

    @Test
    void validateJwtToken_ok() {
        String token = Jwts.builder()
                .setSubject("ok@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP_MS))
                .signWith(SignatureAlgorithm.HS512, TEST_SECRET)
                .compact();

        assertThat(jwtUtils.validateJwtToken(token)).isTrue();
    }

    @Test
    void validateJwtToken_invalidSignature() {
        String otherSecret = "some-other-secret";
        String token = Jwts.builder()
                .setSubject("bob@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXP_MS))
                .signWith(SignatureAlgorithm.HS512, otherSecret)
                .compact();

        assertThat(jwtUtils.validateJwtToken(token)).isFalse();
    }

    @Test
    void validateJwtToken_malformed() {
        assertThat(jwtUtils.validateJwtToken("this.is.not.a.jwt")).isFalse();
    }

    @Test
    void validateJwtToken_expired() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1);

        String token = Jwts.builder()
                .setSubject("late@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1))
                .signWith(SignatureAlgorithm.HS512, TEST_SECRET)
                .compact();

        Thread.sleep(5);
        assertThat(jwtUtils.validateJwtToken(token)).isFalse();
    }

    @Test
    void validateJwtToken_unsupported() {
        // Build a compact JWT with alg "none" (unsigned token): header.payload.
        String headerJson = "{\"alg\":\"none\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"unsupported@example.com\"}";

        // Encode header and payload in Base64Url
        String header = java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(headerJson.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        String payload = java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadJson.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        // Add trailing dot to indicate empty signature part
        String unsignedToken = header + "." + payload + ".";

        // JJWT parser expects a signed JWS; "alg=none" is not supported.
        // This will throw UnsupportedJwtException inside validateJwtToken(),
        // which is caught and should result in false.
        assertThat(jwtUtils.validateJwtToken(unsignedToken)).isFalse();
    }

    @Test
    void validateJwtToken_illegalArgument_empty() {
        assertThat(jwtUtils.validateJwtToken("")).isFalse();
    }
}