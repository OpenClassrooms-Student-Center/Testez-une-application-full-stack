package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void testConstructorAndGetters() {
        JwtResponse response = new JwtResponse(
                "jwt-token",
                42L,
                "user@mail.com",
                "Jean",
                "Dupont",
                true
        );

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(42L, response.getId());
        assertEquals("user@mail.com", response.getUsername());
        assertEquals("Jean", response.getFirstName());
        assertEquals("Dupont", response.getLastName());
        assertTrue(response.getAdmin());
    }

    @Test
    void testSetters() {
        JwtResponse response = new JwtResponse(
                "token", 1L, "user", "first", "last", false
        );

        response.setToken("new-token");
        response.setType("CustomType");
        response.setId(99L);
        response.setUsername("newuser");
        response.setFirstName("Pierre");
        response.setLastName("Martin");
        response.setAdmin(true);

        assertEquals("new-token", response.getToken());
        assertEquals("CustomType", response.getType());
        assertEquals(99L, response.getId());
        assertEquals("newuser", response.getUsername());
        assertEquals("Pierre", response.getFirstName());
        assertEquals("Martin", response.getLastName());
        assertTrue(response.getAdmin());
    }

    @Test
    void testTypeDefaultValue() {
        JwtResponse response = new JwtResponse(
                "token", 1L, "user", "first", "last", false
        );
        assertEquals("Bearer", response.getType());
    }
}