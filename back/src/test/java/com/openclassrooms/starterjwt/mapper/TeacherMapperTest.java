package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TeacherMapperTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void shouldMapAndSaveTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        Teacher savedTeacher = teacherRepository.save(teacher);
        TeacherDto teacherDto = teacherMapper.toDto(savedTeacher);

        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(savedTeacher.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo(savedTeacher.getFirstName());
        assertThat(teacherDto.getLastName()).isEqualTo(savedTeacher.getLastName());
    }

    @Test
    void shouldMapTeacherDtoToTeacherAndPersist() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setFirstName("Bob");
        teacherDto.setLastName("Smith");

        Teacher teacher = teacherMapper.toEntity(teacherDto);
        Teacher savedTeacher = teacherRepository.save(teacher);

        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isNotNull();
        assertThat(savedTeacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(savedTeacher.getLastName()).isEqualTo(teacherDto.getLastName());
    }
}