package com.openclassrooms.starterjwt.mapper;

import java.util.ArrayList;
import java.util.List;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {

    // Inject the UserMapper using MapStruct
    @Autowired
    private UserMapper userMapper;

    private User userEntity;

    @BeforeEach
    public void beforeEach() {
        // Initialize a User entity for testing
        userEntity = new User()
                .setId(10L)
                .setEmail("testUser@email.com")
                .setLastName("test")
                .setFirstName("user")
                .setPassword("password")
                .setAdmin(true);
    }

    @Test
    public void userDtoToUserEntityOk() {
        // Create a UserDto for testing
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@mail.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password");
        userDto.setAdmin(true);

        // Call the toEntity method of the UserMapper
        User user = userMapper.toEntity(userDto);

        // Verify that the converted User entity is not null
        assertNotNull(user);
        // Verify that the properties of UserDto match those of the converted User
        // entity
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.isAdmin(), user.isAdmin());
    }

    @Test
    public void userEntityToUserDtoOk() {
        // Call the toDto method of the UserMapper to convert the User entity
        UserDto userDto = userMapper.toDto(this.userEntity);

        // Verify that the converted UserDto is not null
        assertNotNull(userDto);
        // Verify that the properties of the User entity match those of the converted
        // UserDto
        assertEquals(this.userEntity.getId(), userDto.getId());
        assertEquals(this.userEntity.getEmail(), userDto.getEmail());
        assertEquals(this.userEntity.getLastName(), userDto.getLastName());
        assertEquals(this.userEntity.getFirstName(), userDto.getFirstName());
        assertEquals(this.userEntity.getPassword(), userDto.getPassword());
        assertEquals(this.userEntity.isAdmin(), userDto.isAdmin());
    }

    @Test
    public void userDtoListToUserEntityListOk() {
        // Create a UserDto and add it to a list
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@mail.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setPassword("password");
        userDto.setAdmin(true);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        // Call the toEntity method of the UserMapper to convert the list
        List<User> userList = userMapper.toEntity(userDtoList);

        // Verify that the converted list of User entities is not null
        assertNotNull(userList);
        // Verify that the properties of the first UserDto match those of the converted
        // User entity
        assertEquals(userDtoList.get(0).getId(), userList.get(0).getId());
    }

    @Test
    public void userEntityListToUserDtoListOk() {
        // Create a list of User entities and add the User entity
        List<User> userList = new ArrayList<>();
        userList.add(this.userEntity);

        // Call the toDto method of the UserMapper to convert the list
        List<UserDto> userDtoList = userMapper.toDto(userList);

        // Verify that the converted list of UserDto is not null
        assertNotNull(userDtoList);
        // Verify that the properties of the first User entity match those of the
        // converted UserDto
        assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
    }
}
