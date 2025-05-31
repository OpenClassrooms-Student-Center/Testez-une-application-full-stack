package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private UserDto dto1;
    private UserDto dto2;
    private User u1;
    private User u2;


    @BeforeEach
    void setUp() {
        dto1 = new UserDto();
        dto1.setId(1L);
        dto1.setEmail("user1@email.com");
        dto1.setLastName("Dupont");
        dto1.setFirstName("Jean");
        dto1.setPassword("FakePassword");

        dto2 = new UserDto();
        dto2.setId(2L);
        dto2.setEmail("user2@email.com");
        dto2.setLastName("Durand");
        dto2.setFirstName("Marie");
        dto2.setPassword("FakePassword");

        u1 = new User();
        u1.setId(1L);
        u1.setEmail("user1@email.com");
        u1.setLastName("Dupont");
        u1.setFirstName("Jean");
        u1.setPassword("FakePassword");

        u2 = new User();
        u2.setId(2L);
        u2.setEmail("user2@email.com");
        u2.setLastName("Durand");
        u2.setFirstName("Marie");
        u2.setPassword("FakePassword");
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        User entity = mapper.toEntity(dto1);
        assertEquals(dto1.getId(), entity.getId());
        assertEquals(dto1.getEmail(), entity.getEmail());
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        User entity = new User();
        entity.setId(2L);
        entity.setEmail("other@email.com");
        entity.setLastName("FakeDurand");
        entity.setFirstName("FakeMarie");
        entity.setPassword("FakePassword2");
        UserDto dto = mapper.toDto(entity);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
    }

    @Test
    void toEntityList_shouldMapDtoListToEntityList() {
        List<User> entities = mapper.toEntity(Arrays.asList(dto1, dto2));
        assertEquals(2, entities.size());
    }

    @Test
    void toDtoList_shouldMapEntityListToDtoList() {
        List<UserDto> dtos = mapper.toDto(Arrays.asList(u1, u2));
        assertEquals(2, dtos.size());
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        assertNull(mapper.toEntity((UserDto) null));
    }

    @Test
    void toDto_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDto((User) null));
    }

    @Test
    void toEntityList_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toEntity((List<UserDto>) null));
    }

    @Test
    void toDtoList_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toDto((List<User>) null));
    }

    @Test
    void toEntityList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<User> result = mapper.toEntity(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<UserDto> result = mapper.toDto(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}