package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TeacherMapperTest {

    // Inject the TeacherMapper using MapStruct
    @Autowired
    private TeacherMapper teacherMapper;

    // Test for converting TeacherDto to Teacher entity
    @Test
    public void TeacherDtoToTeacherEntityOk() {
        // Create a TeacherDto
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");

        // Call the toEntity method of the TeacherMapper
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Verify that the converted Teacher entity is not null
        assertNotNull(teacher);
        // Verify that the properties of TeacherDto match those of the converted Teacher
        // entity
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }

    // Test for converting Teacher entity to TeacherDto
    @Test
    public void TeacherEntityToTeacherDtoOk() {
        // Create a Teacher entity
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");

        // Call the toDto method of the TeacherMapper
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Verify that the converted TeacherDto is not null
        assertNotNull(teacherDto);
        // Verify that the properties of Teacher match those of the converted TeacherDto
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }

    // Test for converting a list of TeacherDto to a list of Teacher entities
    @Test
    public void TeacherDtoListToTeacherEntityListOk() {
        // Create a TeacherDto and add it to a list
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        // Call the toEntity method of the TeacherMapper to convert the list
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Verify that the converted list of Teacher entities is not null
        assertNotNull(teacherList);
        // Verify that the properties of the first TeacherDto match those of the
        // converted Teacher entity
        assertEquals(teacherDtoList.get(0).getId(), teacherList.get(0).getId());
    }

    // Test for converting a list of Teacher entities to a list of TeacherDto
    @Test
    public void TeacherEntityListToTeacherDtoListOk() {
        // Create a Teacher entity and add it to a list
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        // Call the toDto method of the TeacherMapper to convert the list
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        // Verify that the converted list of TeacherDto is not null
        assertNotNull(teacherDtoList);
        // Verify that the properties of the first Teacher entity match those of the
        // converted TeacherDto
        assertEquals(teacherList.get(0).getId(), teacherDtoList.get(0).getId());
    }
}
