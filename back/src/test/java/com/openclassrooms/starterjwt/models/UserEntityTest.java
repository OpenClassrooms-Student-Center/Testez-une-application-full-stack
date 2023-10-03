package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    private User user;

    // Define sample data for testing
    private Long id = 1L;
    private String email = "roby@email.com";
    private String lastName = "test";
    private String firstName = "Roby";
    private String password = "password";
    private boolean admin = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @BeforeEach
    public void beforeEach() {
        // Initialize a User object for testing
        user = User.builder()
                .id(id)
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .password(password)
                .admin(admin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    @Test
    public void userEntitySetIdOk() {
        // Test setting the ID property of the User object
        user.setId(2L);
        // Verify that the ID is set correctly
        assertThat(user.getId()).isEqualTo(2L);
    }

    @Test
    public void userEntitySetEmailOk() {
        // Test setting the email property of the User object
        String emailTest = "roby@email.com";
        // Verify that the email is set correctly
        assertThat(user.getEmail()).isEqualTo(emailTest);
    }

    @Test
    public void userEntitySetLastNameOk() {
        // Test setting the lastName property of the User object
        String lastNameTest = "test";
        // Verify that lastName is set correctly
        assertThat(user.getLastName()).isEqualTo(lastNameTest);
    }

    @Test
    public void userEntitySetfirstNameOk() {
        // Test setting the firstName property of the User object
        String firstNameTest = "Roby";
        // Verify that firstName is set correctly
        assertThat(user.getFirstName()).isEqualTo(firstNameTest);
    }

    @Test
    public void userEntitySetPasswordOk() {
        // Test setting the password property of the User object
        String passwordTest = "password";
        // Verify that the password is set correctly
        assertThat(user.getPassword()).isEqualTo(passwordTest);
    }

    @Test
    public void userEntitySetAdminToTrue() {
        // Test setting the admin property of the User object
        boolean adminTest = true;
        // Verify that admin is set correctly
        assertThat(user.isAdmin()).isEqualTo(adminTest);
    }

    @Test
    public void userEntitySetCreatedAtOk() {
        // Test setting the createdAt property of the User object
        LocalDateTime nowDateStored = LocalDateTime.now();
        // Verify that createdAt is set correctly
        assertThat(user.getCreatedAt()).isEqualTo(nowDateStored);
    }

    @Test
    public void userEntitySetUpdatedAtOk() {
        // Test setting the updatedAt property of the User object
        LocalDateTime nowDateStored = LocalDateTime.now();
        // Verify that updatedAt is set correctly
        assertThat(user.getUpdatedAt()).isEqualTo(nowDateStored);
    }
}
