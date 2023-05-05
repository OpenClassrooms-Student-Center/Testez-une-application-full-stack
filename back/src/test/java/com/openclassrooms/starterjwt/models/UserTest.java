package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void builder() {
        //Given
        User user = User.builder()
                .id(1L)
                .email("email")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .build();
        //When
        //Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("email");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getFirstName()).isEqualTo("firstName");
        assertThat(user.getLastName()).isEqualTo("lastName");
    }
}