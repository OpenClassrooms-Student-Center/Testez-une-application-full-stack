package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class UserDetailsImplTest {

    @Test
    public void testEquals() {

        UserDetailsImpl userDetailsImpl = UserDetailsImpl
                .builder()
                .id(1L)
                .build();

        UserDetailsImpl sameUserDetailsImpl = UserDetailsImpl
                .builder()
                .id(1L)
                .build();

        UserDetailsImpl differentUserDetailsImpl = UserDetailsImpl
                .builder()
                .id(2L)
                .build();

        assertEquals(userDetailsImpl, sameUserDetailsImpl);
        assertNotEquals(userDetailsImpl, differentUserDetailsImpl);

    }

}
