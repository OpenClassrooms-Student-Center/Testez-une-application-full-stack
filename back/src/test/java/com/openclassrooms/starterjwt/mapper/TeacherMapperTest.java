package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private final TeacherMapper mapper = Mappers.getMapper(TeacherMapper.class);

    private TeacherDto dto1;
    private TeacherDto dto2;
    private Teacher t1;
    private Teacher t2;

    @BeforeEach
    void setUp() {
        dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setLastName("Martin");
        dto1.setFirstName("Paul");

        dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setLastName("Durand");
        dto2.setFirstName("Sophie");

        t1 = new Teacher();
        t1.setId(1L);
        t1.setLastName("Martin");
        t1.setFirstName("Paul");

        t2 = new Teacher();
        t2.setId(2L);
        t2.setLastName("Durand");
        t2.setFirstName("Sophie");
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        Teacher entity = mapper.toEntity(dto1);
        assertEquals(dto1.getId(), entity.getId());
        assertEquals(dto1.getLastName(), entity.getLastName());
        assertEquals(dto1.getFirstName(), entity.getFirstName());
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        TeacherDto dto = mapper.toDto(t2);
        assertEquals(t2.getId(), dto.getId());
        assertEquals(t2.getLastName(), dto.getLastName());
        assertEquals(t2.getFirstName(), dto.getFirstName());
    }

    @Test
    void toEntityList_shouldMapDtoListToEntityList() {
        List<Teacher> entities = mapper.toEntity(Arrays.asList(dto1, dto2));
        assertEquals(2, entities.size());
    }

    @Test
    void toDtoList_shouldMapEntityListToDtoList() {
        List<TeacherDto> dtos = mapper.toDto(Arrays.asList(t1, t2));
        assertEquals(2, dtos.size());
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {
        assertNull(mapper.toEntity((TeacherDto) null));
    }

    @Test
    void toDto_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDto((Teacher) null));
    }

    @Test
    void toEntityList_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toEntity((List<TeacherDto>) null));
    }

    @Test
    void toDtoList_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toDto((List<Teacher>) null));
    }

    @Test
    void toEntityList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<Teacher> result = mapper.toEntity(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<TeacherDto> result = mapper.toDto(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


}