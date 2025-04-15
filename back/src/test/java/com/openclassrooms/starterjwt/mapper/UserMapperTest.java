package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserMapperTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapAndSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("securePassword123");

        User savedUser = userRepository.save(user);
        UserDto userDto = userMapper.toDto(savedUser);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(savedUser.getId());
        assertThat(userDto.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(savedUser.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(savedUser.getLastName());
    }

    @Test
    void shouldMapUserDtoToUserAndPersist() {
        UserDto userDto = new UserDto();
        userDto.setEmail("jane@example.com");
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");
        userDto.setPassword("anotherSecurePass");

        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(savedUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(userDto.getLastName());
    }

    @Test
    void shouldReturnNullWhenUserDtoIsNull() {
        UserDto dto = null;
        User user = userMapper.toEntity(dto);
        assertThat(user).isNull();
    }

    @Test
    void shouldMapUserDtoListToUserList() {
        UserDto userDto1 = new UserDto();
        userDto1.setEmail("test1@example.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setPassword("password1");

        UserDto userDto2 = new UserDto();
        userDto2.setEmail("test2@example.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setPassword("password2");

        List<UserDto> userDtos = List.of(userDto1, userDto2);

        List<User> users = userMapper.toEntity(userDtos);

        assertThat(users)
                .isNotNull()
                .hasSize(2)
                .extracting(User::getEmail)
                .containsExactly("test1@example.com", "test2@example.com");
    }

    @Test
    void shouldMapUserListToUserDtoList() {
        User user1 = new User();
        user1.setEmail("test1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setEmail("test2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setPassword("password2");

        List<User> users = List.of(user1, user2);

        List<UserDto> userDtos = userMapper.toDto(users);

        assertThat(userDtos).hasSize(2);
        assertThat(userDtos.get(0).getEmail()).isEqualTo("test1@example.com");
        assertThat(userDtos.get(1).getEmail()).isEqualTo("test2@example.com");
    }

    @Test
    void shouldHandleEmptyListToUserEntity() {
        List<User> users = userMapper.toEntity(Collections.emptyList());
        assertThat(users).isEmpty();
    }

    @Test
    void shouldHandleEmptyListToUserDto() {
        List<UserDto> userDtos = userMapper.toDto(Collections.emptyList());
        assertThat(userDtos).isEmpty();
    }
}