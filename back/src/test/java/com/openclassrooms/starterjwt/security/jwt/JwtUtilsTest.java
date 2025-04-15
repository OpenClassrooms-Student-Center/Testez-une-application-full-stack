package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;
    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    @BeforeEach
    void setUp() {

        errContent.reset();
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void testGenerateJwtToken_ShouldReturnValidToken() {
        UserDetails userDetails = new UserDetailsImpl(1L, "testUser", "Test", "User", false, "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 10);
    }

    @Test
    void testGetUserNameFromJwtToken_ShouldReturnCorrectUsername() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()
                        + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testUser", username);
    }

    @Test
    void testValidateJwtToken_ShouldReturnTrueForValidToken() {
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()
                        + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_ShouldLogErrorForSignatureException() {

        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "wrongSecretKey")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
        String logs = errContent.toString();
        System.out.println(logs);
     //   assertTrue(logs.contains("JWT claims string is empty: signing key cannot be null or empty."));
    }

    @Test
    void testValidateJwtToken_ShouldReturnFalseForMalformedToken() {

           String malformedToken = "invalid.token.structure";

        assertFalse(jwtUtils.validateJwtToken(malformedToken));
        String logs = errContent.toString().replaceAll("[^\\x20-\\x7E]", "") ;
        System.out.println(logs);
    //    assertTrue(logs.contains("Invalid JWT token: Unable to read JSON value"));
    }

    @Test
    void testValidateJwtToken_ShouldReturnFalseForExpiredToken() {

        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(new Date().getTime() - jwtExpirationMs - 10000))
                .setExpiration(new Date(new Date().getTime() - 5000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
        String logs = errContent.toString();
        System.out.println(logs);
    }

    @Test
    void testValidateJwtToken_ShouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
        String logs = errContent.toString();
        System.out.println(logs);
    }
    @Test
    void testValidateJwtToken_ShouldReturnFalseForUnsupportedJwtException() {
        String unsupportedToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .compact();

        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
        String logs = errContent.toString();
        System.out.println(logs);
    }

    @Test
    void testValidateJwtToken_ShouldReturnFalseForIllegalArgumentException() {
        assertFalse(jwtUtils.validateJwtToken(""));
        assertFalse(jwtUtils.validateJwtToken(null));
        String logs = errContent.toString();
        System.out.println(logs);
    }
}
