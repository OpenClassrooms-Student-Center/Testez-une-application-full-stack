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
    /**
     * Generate a JWT from an Authentication object, then verify that:
     * - the token is not blank,
     * - the token validates successfully,
     * - the username can be extracted from the token.
     */

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
=======
    /**
     * Build a valid JWT manually (using the test secret) and assert that
     * getUserNameFromJwtToken returns the expected subject (username).
     */
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
=======
    /**
     * Validate a correctly-signed, non-expired token: validateJwtToken should
     * return true.
     */
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
=======
    /**
     * Create a token signed with a different secret and assert that validation
     * fails
     * due to invalid signature.
     */
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
=======
    /**
     * Pass a malformed string that is not a JWT and assert that validation returns
     * false.
     */
    void validateJwtToken_malformed() {
        assertThat(jwtUtils.validateJwtToken("this.is.not.a.jwt")).isFalse();
    }

    @Test
=======
    /**
     * Create a token with a very short expiration, wait until it expires, then
     * assert that
     * validation reports the token as expired (false).
     */
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
=======
    /**
     * Build an unsigned JWT (alg="none") which is unsupported by the parser and
     * assert
     * that validation returns false.
     */
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
=======
    /**
     * Pass an empty string to the validator and assert that it returns false.
     */
    void validateJwtToken_illegalArgument_empty() {
        assertThat(jwtUtils.validateJwtToken("")).isFalse();
    }
}