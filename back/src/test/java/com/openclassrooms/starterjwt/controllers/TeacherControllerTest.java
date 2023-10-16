package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @InjectMocks
    TeacherController teacherController;
    @Mock
    TeacherMapper teacherMapper;
    @Mock
    TeacherService teacherService;
    @BeforeEach
    void setUp(){
        teacherController = new TeacherController(
                teacherService,
                teacherMapper
        );
    }

    @Test
    @DisplayName("findById [Teacher]")
    void whenTeacherIdentityFound_thenReturnResponseEntityOk(){
        //GIVEN
        String id = "1";
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();

        //WHEN
        when(teacherService.findById(Long.parseLong(id)))
                .thenReturn(teacher);

       when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

       ResponseEntity<?> responseEntity = ResponseEntity.ok()
               .body(teacherDto);

       ResponseEntity<?> findTeacher = teacherController.findById(id);

       //THEN
       assertThat(findTeacher).isEqualTo(responseEntity);
       assertThat(findTeacher.getStatusCode()).isEqualTo(HttpStatus.OK);
       verify(teacherMapper, times(1))
               .toDto(teacher);
    }
    @Test
    @DisplayName("not found [Teacher]")
    void whenTeacherIdIsNotPresent_thenReturnNotFoundEntity(){
        String id = "1";
        Teacher teacher = null;

        when(teacherService.findById(Long.parseLong(id)))
                .thenReturn(teacher);

        ResponseEntity<?> notFound = ResponseEntity.notFound().build();
        ResponseEntity<?> findTeacher = teacherController.findById(id);

        assertThat(findTeacher).isEqualTo(notFound);
        assertThat(findTeacher.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    @DisplayName("Exception [teacher]")
    void whenLongCanNotParse_thenCatchNumberFormatException(){
        String id = "hey123";

       assertThrows(NumberFormatException.class, ()->{
           Long.parseLong(id);
        });

       ResponseEntity<?> responseEntity = teacherController.findById(id);

       assertThat(responseEntity).isEqualTo(ResponseEntity.badRequest().build());
       assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Teacher List")
    void whenTeacherListIsPresent_thenReturnResponseEntityOfDtoList(){
        //GIVEN
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        List<Teacher> teacherList = new ArrayList<>();

        //WHEN
        when(teacherService.findAll()).thenReturn(teacherList);

        when(teacherMapper.toDto(teacherList)).thenReturn(teacherDtoList);

        ResponseEntity<?> responseEntityDto = ResponseEntity.ok().body(teacherDtoList);

        ResponseEntity<?> teacherListFound = teacherController.findAll();

        //THEN
        assertThat(teacherListFound).isEqualTo(responseEntityDto);
        assertThat(teacherListFound.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teacherMapper, times(1))
                .toDto(teacherList);
    }
}
