// src/test/java/com/openclassrooms/starterjwt/security/jwt/JwtUtilsIT.java
package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "oc.app.jwtSecret=integrationSecret",
        "oc.app.jwtExpirationMs=60000"
})
@ActiveProfiles("integration")
class JwtUtilsIT {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void testGenerateAndValidateJwtToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("integrationUser")
                .firstName("first")
                .lastName("last")
                .admin(false)
                .password("pwd")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtils.generateJwtToken(authentication);
        assertThat(token).isNotBlank();
        assertThat(jwtUtils.validateJwtToken(token)).isTrue();
        assertThat(jwtUtils.getUserNameFromJwtToken(token)).isEqualTo("integrationUser");
    }

    @Test
    void testValidateJwtTokenWithInvalidToken() {
        assertThat(jwtUtils.validateJwtToken("invalid.token.value")).isFalse();
    }
}