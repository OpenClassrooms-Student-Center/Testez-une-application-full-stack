package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(mockUserRepository);
    }

    @AfterEach
    void tearDown() {
        userServiceUnderTest = new UserService(mockUserRepository);
    }

    @Test
    void delete() {
        //Given
        Long id = 1L;
        //When
        userServiceUnderTest.delete(id);
        //Then
        assertThat(userServiceUnderTest.findById(id)).isNull();
    }

    @Test
    void findById() {
        //Given
        Long id = 1L;
        when(mockUserRepository.findById(id)).thenReturn(Optional.of(User.builder().id(id).email("toto@robert.com").lastName("Robert").firstName("Thomas").password("toto!1234").admin(false).build()));
        //When
        User result = userServiceUnderTest.findById(id);
        //Then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("toto@robert.com");
        assertThat(result.getLastName()).isEqualTo("Robert");
        assertThat(result.getFirstName()).isEqualTo("Thomas");
        assertThat(result.getPassword()).isEqualTo("toto!1234");
        assertThat(result.isAdmin()).isFalse();
    }
}