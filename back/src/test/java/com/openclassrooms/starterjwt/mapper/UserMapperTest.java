package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Loads only the MapStruct-generated implementation (UserMapperImpl).
 * No repositories or Spring Boot context are started.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserMapperImpl.class })
class UserMapperTest {

    // Spring will inject the generated mapper because it's a @Configuration class
    // here
    @org.springframework.beans.factory.annotation.Autowired
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
        // Provide required fields; leave optional ones null
        UserDto dto = new UserDto();
        dto.setEmail("required@test.com"); // required
        dto.setFirstName("John"); // required
        dto.setLastName("Doe"); // required
        dto.setPassword("secret"); // required
        // optional: id/admin/createdAt/updatedAt left null/default

        User entity = userMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        // required copied
        assertThat(entity.getEmail()).isEqualTo("required@test.com");
        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getPassword()).isEqualTo("secret");
        // optional remain default/null
        assertThat(entity.getId()).isNull();
        assertThat(entity.isAdmin()).isFalse();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();

        // Round-trip back to DTO
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

}