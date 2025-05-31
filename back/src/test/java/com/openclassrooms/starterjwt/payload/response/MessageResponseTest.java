package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void testConstructorAndGetter() {
        MessageResponse response = new MessageResponse("Succès !");
        assertEquals("Succès !", response.getMessage());
    }

    @Test
    void testSetter() {
        MessageResponse response = new MessageResponse("Initial");
        response.setMessage("Modifié");
        assertEquals("Modifié", response.getMessage());
    }

    @Test
    void testNullMessage() {
        MessageResponse response = new MessageResponse(null);
        assertNull(response.getMessage());
        response.setMessage("Texte");
        assertEquals("Texte", response.getMessage());
    }
}