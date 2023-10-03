package com.openclassrooms.starterjwt.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Test
	void testGetUserById() {
		// Arrange
		User user = new User();
		user.setId(1L);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// Act
		UserService userService = new UserService(userRepository);
		User foundUser = userService.findById(user.getId());

		// Assert
		assertEquals(user.getId(), foundUser.getId());
	}

	@Test
	void testDeleteUser() {
		// Arrange
		User user = new User();
		user.setId(1L);
		doNothing().when(userRepository).deleteById(anyLong());

		// Act
		UserService userService = new UserService(userRepository);
		userService.delete(user.getId());

		// Assert
		verify(userRepository, times(1)).deleteById(user.getId());
	}
}
