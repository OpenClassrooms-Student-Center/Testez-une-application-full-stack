package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

public class UserDetailsImplTest {

    // Test for the equals method of the UserDetailsImpl class
    @Test
    public void equalsMethodOk() {
        // Create a UserDetailsImpl object with ID 1
        UserDetailsImpl userDetailsImpl = UserDetailsImpl.builder().id(1L).build();

        // Create another UserDetailsImpl object with the same ID 1
        UserDetailsImpl sameUserDetailsImpl = UserDetailsImpl.builder().id(1L).build();

        // Create a UserDetailsImpl object with a different ID (2)
        UserDetailsImpl differentUserDetailsImpl = UserDetailsImpl.builder().id(2L).build();

        // Check that objects with the same ID are equal
        assertEquals(userDetailsImpl, sameUserDetailsImpl);

        // Check that objects with different IDs are not equal
        assertNotEquals(userDetailsImpl, differentUserDetailsImpl);
    }
}
