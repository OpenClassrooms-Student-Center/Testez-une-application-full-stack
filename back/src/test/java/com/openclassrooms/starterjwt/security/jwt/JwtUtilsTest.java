package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testGenerateJwtToken() {
        // Test case for generating a valid JWT token
        UserDetails userDetails = new UserDetailsImpl(null, "testuser", "testpassword", null, null, null);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        String token = jwtUtils.generateJwtToken(authentication);

        // Verify that the generated token is not empty, and it can be validated
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtUtils.getUserNameFromJwtToken(token));
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void testMalformedJwt() {
        // Test case for validating a malformed (invalid) JWT token
        assertFalse(jwtUtils.validateJwtToken("not-a-valid-jwt"));
    }

    @Test
    public void testInvalidSignatureJwt() {
        // Test case for validating a JWT with an invalid signature
        String jwt = Jwts.builder()
                .setSubject("test@email.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 1000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        // Verify that the JWT with the wrong signature is invalid
        assertFalse(jwtUtils.validateJwtToken(jwt));
    }

    @Test
    public void testExpiredJwt() {
        // Test case for validating an expired JWT
        String jwt = Jwts.builder()
                .setSubject("test@email.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() - 1))
                .signWith(SignatureAlgorithm.HS512, "testSecret")
                .compact();

        // Verify that the expired JWT is invalid
        assertFalse(jwtUtils.validateJwtToken(jwt));
    }
}
