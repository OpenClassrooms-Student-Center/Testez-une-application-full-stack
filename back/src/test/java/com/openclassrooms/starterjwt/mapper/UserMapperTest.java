package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Loads only the MapStruct-generated implementation (UserMapperImpl).
 * No repositories or Spring Boot context are started.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserMapperImpl.class })
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toEntity_shouldMapDtoToEntity() {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("user@test.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("secret");
        dto.setAdmin(true);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        User entity = userMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getPassword()).isEqualTo(dto.getPassword());
        assertThat(entity.isAdmin()).isEqualTo(dto.isAdmin());
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        User entity = new User();
        entity.setId(2L);
        entity.setEmail("jane@test.com");
        entity.setFirstName("Jane");
        entity.setLastName("Smith");
        entity.setPassword("password");
        entity.setAdmin(false);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        UserDto dto = userMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getEmail()).isEqualTo(entity.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
        assertThat(dto.getPassword()).isEqualTo(entity.getPassword());
        assertThat(dto.isAdmin()).isEqualTo(entity.isAdmin());
    }

    @Test
    void nullOptionalFields_areHandled() {
        UserDto dto = new UserDto();
        dto.setEmail("required@test.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("secret");

        User entity = userMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo("required@test.com");
        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getPassword()).isEqualTo("secret");
        assertThat(entity.getId()).isNull();
        assertThat(entity.isAdmin()).isFalse();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();

        UserDto roundTrip = userMapper.toDto(entity);
        assertThat(roundTrip).isNotNull();
        assertThat(roundTrip.getEmail()).isEqualTo("required@test.com");
        assertThat(roundTrip.getFirstName()).isEqualTo("John");
        assertThat(roundTrip.getLastName()).isEqualTo("Doe");
        assertThat(roundTrip.getPassword()).isEqualTo("secret");
        assertThat(roundTrip.getId()).isNull();
        assertThat(roundTrip.isAdmin()).isFalse();
        assertThat(roundTrip.getCreatedAt()).isNull();
        assertThat(roundTrip.getUpdatedAt()).isNull();
    }

    @Test
    void toDto_list_shouldMapAll() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("a@a.com");
        u1.setFirstName("A");
        u1.setLastName("A");
        u1.setPassword("p");
        u1.setAdmin(false);

        User u2 = new User();
        u2.setId(2L);
        u2.setEmail("b@b.com");
        u2.setFirstName("B");
        u2.setLastName("B");
        u2.setPassword("p");
        u2.setAdmin(true);

        List<User> list = Arrays.asList(u1, u2);
        List<UserDto> dtos = userMapper.toDto(list);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void toEntity_list_shouldMapAll() {
        UserDto d1 = new UserDto();
        d1.setId(10L);
        d1.setEmail("x@x.com");
        d1.setFirstName("X");
        d1.setLastName("X");
        d1.setPassword("p");
        d1.setAdmin(false);

        UserDto d2 = new UserDto();
        d2.setId(20L);
        d2.setEmail("y@y.com");
        d2.setFirstName("Y");
        d2.setLastName("Y");
        d2.setPassword("p");
        d2.setAdmin(true);

        List<UserDto> list = Arrays.asList(d1, d2);
        List<User> entities = userMapper.toEntity(list);

        assertThat(entities).hasSize(2);
        assertThat(entities.get(0).getId()).isEqualTo(10L);
        assertThat(entities.get(1).getId()).isEqualTo(20L);
    }

    @Test
    void toDto_list_empty_shouldReturnEmptyList() {
        List<UserDto> dtos = userMapper.toDto(Collections.<User>emptyList());
        assertThat(dtos).isEmpty();
    }

    @Test
    void toEntity_list_empty_shouldReturnEmptyList() {
        List<User> entities = userMapper.toEntity(Collections.<UserDto>emptyList());
        assertThat(entities).isEmpty();
    }
}