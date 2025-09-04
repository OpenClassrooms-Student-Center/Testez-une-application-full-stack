package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    @Test
    void builderAndGetters_shouldReturnValues() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .password("secret")
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("jdoe@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getAdmin()).isTrue();
        assertThat(user.getPassword()).isEqualTo("secret");
    }

    @Test
    void getAuthorities_shouldReturnEmptyCollection() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertThat(authorities).isNotNull();
        assertThat(authorities).isEmpty();
    }

    @Test
    void accountStatusMethods_shouldReturnTrue() {
        UserDetailsImpl user = UserDetailsImpl.builder().build();

        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void equals_shouldBeBasedOnId() {
        UserDetailsImpl u1 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl u2 = UserDetailsImpl.builder().id(1L).build();
        UserDetailsImpl u3 = UserDetailsImpl.builder().id(2L).build();

        assertThat(u1).isEqualTo(u2);
        assertThat(u1).isNotEqualTo(u3);
    }
}