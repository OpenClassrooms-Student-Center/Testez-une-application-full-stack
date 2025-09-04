package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void toEntity_shouldMapDtoToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        Teacher entity = teacherMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
    }

    @Test
    void toDto_shouldMapEntityToDto() {
        Teacher entity = new Teacher();
        entity.setId(2L);
        entity.setFirstName("Jane");
        entity.setLastName("Smith");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        TeacherDto dto = teacherMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(entity.getLastName());
    }
}
