package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

/**
 * Unit tests for the {@link UserService} class.
 * 
 * It tests the functionality of deleting users and finding users by ID.
 * 
 * The tests use Mockito to mock the UserRepository dependency.
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    /**
     * A mock instance of the UserRepository class.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * An instance of the UserService class with mocked dependencies.
     */
    @InjectMocks
    private UserService userService;

    /**
     * Annotated with {@code @BeforeEach}, this method is executed before each
     * test case. It sets up the necessary dependencies.
     */
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    /**
     * Tests the {@code findById()} method of the UserService class.
     * It mocks the {@code findById()} method of the userRepository
     * to return a user object and verifies that the {@code findById()}
     * method is called. It asserts that the returned user object
     * is equal to the original user object.
     */
    @Test
    @Tag("UserService.findById()")
    @DisplayName("Find user by ID → found user")
    public void testFindUserById() {
        // * Arrange
        Long userId = 1L;
        User user = new User("John", "Doe", "john.doe@example.com", "password",
                false);
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // * Act
        User result = userService.findById(userId);

        // * Assert
        verify(userRepository).findById(userId);

        assertEquals(user, result);
    }

    /**
     * Tests the {@code findById()} method of the UserService class when the
     * user is not found.
     * It mocks the {@code findById()} method of the userRepository
     * to return an empty optional and verifies that the {@code findById()}
     * method is called. It asserts that the returned user object is null.
     */
    @Test
    @Tag("UserService.findById()")
    @DisplayName("Find user by ID → user not found")
    public void testFindUserByIdNotFound() {
        // * Arrange
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // * Act
        User result = userService.findById(userId);

        // * Assert
        verify(userRepository).findById(userId);

        assertNull(result);
    }

    /**
     * Tests the {@code delete()} method of the UserService class.
     * It mocks the {@code deleteById()} method of the userRepository
     * and verifies that the {@code deleteById()} method is called.
     */
    @Test
    @Tag("UserService.delete()")
    @DisplayName("Delete a user by its ID")
    public void testDeleteUser() {
        // * Arrange
        Long userId = 1L;

        // * Act
        userService.delete(userId);

        // * Assert
        verify(userRepository).deleteById(userId);
    }
}