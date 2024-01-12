package com.openclassrooms.starterjwt.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

/**
 * @implSpec The tests cover both the happy path scenarios and edge cases,
 *           asserting the expected outcomes for each endpoint.
 * @implSpec All tests focus on the functionality of the
 *           {@link UserController} in handling teacher-related operations.
 * @implNote The class utilizes JUnit 5 and Mockito for testing.
 *
 * @author Younes LAHOUITI
 * @version 1.0
 * @since 2024-01-05
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("User controller test: api/user")
public class UserControllerTest {
    /**
     * The logger instance for logging test-related information.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * The controller under test, injected with mock dependencies.
     */
    @Mock
    private UserController userController;

    /**
     * Mocked user repository for handling user data during tests.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mocked service for simulating interactions with the user database.
     */
    @MockBean
    private UserService userService;

    /**
     * Mocked mapper for converting between User entities and DTOs.
     */
    @MockBean
    private UserMapper userMapper;

    /**
     * Set up the test environment before each test case by initializing the
     * {@link UserController}.
     * This method creates a new instance of the {@code UserController} with the
     * provided mock dependencies,
     * including a user service, user mapper, and mockMvc for simulating HTTP
     * requests and responses.
     * The initialized controller is then used in each test case to evaluate the
     * behavior of the user-related endpoints.
     * 
     */
    @BeforeEach
    void setUp() {
        // Initialize the controller with mock dependencies
        userController = new UserController(userService, userMapper);
    }

    /**
     * Test case for retrieving a user with the given existing ID.
     * It should return a 200 status code.
     */
    @Test
    @Tag("get_api/user/{id}")
    @DisplayName("(HAPPY PATH) it should get the user from the database of the given id")
    public void getUserById_shouldReturnUserWithGivenId() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(new UserDto());

        // Act
        ResponseEntity<?> result = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for retrieving a user with an invalid ID.
     * It should return a 400 error status code.
     */
    @Test
    @Tag("get_api/user/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 error for an invalid id")
    public void getUserWithInvalidId_shouldReturnNotFoundError() {
        // Assert
        Long nonExistentId = 0L;

        when(userService.findById(nonExistentId)).thenReturn(null);

        // Act
        ResponseEntity<?> result = userController.findById(nonExistentId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /**
     * Test case for deleting a user with a valid ID.
     * It should return a 200 status code upon successful deletion.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(HAPPY PATH) it should delete the user and return a 200 status code")
    public void deleteUserWithValidId_shouldReturnOk() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(mockUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<?> result = userController.save(userId.toString());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case for deleting a user with an unauthorized user.
     * It should return a 401 status code.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(EDGE CASE) it should return a 401 status code for an unauthorized user")
    public void deleteUserWithUnauthorizedUser_shouldReturnUnauthorized() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        when(userService.findById(1L)).thenReturn(mockUser);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("unauthorized@example.com");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<?> result = userController.save(userId.toString());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    /**
     * Test case for deleting a user with a non existent ID.
     * It should return a 404 error status code.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(EDGE CASE) it should return a 404 error for non existent id")
    public void deleteUserWithNonExistentId_shouldReturnNotFoundError() {
        // Arrange
        Long nonExistentId = 0L;
        when(userService.findById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<?> result = userController.save(nonExistentId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /**
     * Test case for deleting a user with an invalid ID.
     * It should return a 400 error status code.
     */
    @Test
    @Tag("delete_api/user/{id}")
    @DisplayName("(EDGE CASE) it should return a 400 error for a bad request")
    public void deleteUserWithInvalidId_shouldReturnNotFoundError() {
        // Act
        ResponseEntity<?> result = userController.save("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
