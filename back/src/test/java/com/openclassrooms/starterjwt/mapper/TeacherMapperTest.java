package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest {

    // Instance MapStruct “standalone” (pas besoin du contexte Spring)
    private final TeacherMapper mapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void toDto_shouldMapEntityToDto() {
        Teacher t = new Teacher();
        t.setId(5L);
        t.setFirstName("Ada");
        t.setLastName("Lovelace");

        TeacherDto dto = mapper.toDto(t);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getFirstName()).isEqualTo("Ada");
        assertThat(dto.getLastName()).isEqualTo("Lovelace");
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(7L);
        dto.setFirstName("Grace");
        dto.setLastName("Hopper");

        Teacher entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(7L);
        assertThat(entity.getFirstName()).isEqualTo("Grace");
        assertThat(entity.getLastName()).isEqualTo("Hopper");
    }

    @Test
    void toDto_list_shouldMapAll() {
        Teacher t1 = new Teacher();
        t1.setId(1L);
        t1.setFirstName("A");
        t1.setLastName("A");

        Teacher t2 = new Teacher();
        t2.setId(2L);
        t2.setFirstName("B");
        t2.setLastName("B");

        List<Teacher> list = Arrays.asList(t1, t2);
        List<TeacherDto> dtos = mapper.toDto(list);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void toEntity_list_shouldMapAll() {
        TeacherDto d1 = new TeacherDto();
        d1.setId(10L);
        d1.setFirstName("X");
        d1.setLastName("X");

        TeacherDto d2 = new TeacherDto();
        d2.setId(20L);
        d2.setFirstName("Y");
        d2.setLastName("Y");

        List<TeacherDto> list = Arrays.asList(d1, d2);
        List<Teacher> entities = mapper.toEntity(list);

        assertThat(entities).hasSize(2);
        assertThat(entities.get(0).getId()).isEqualTo(10L);
        assertThat(entities.get(1).getId()).isEqualTo(20L);
    }

    @Test
    void toDto_list_empty_shouldReturnEmptyList() {
        List<TeacherDto> dtos = mapper.toDto(Collections.<Teacher>emptyList());
        assertThat(dtos).isEmpty();
    }

    @Test
    void toEntity_list_empty_shouldReturnEmptyList() {
        List<Teacher> entities = mapper.toEntity(Collections.<TeacherDto>emptyList());
        assertThat(entities).isEmpty();
    }
}