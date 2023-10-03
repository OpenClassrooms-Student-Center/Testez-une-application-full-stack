package com.openclassrooms.starterjwt.services;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	TeacherRepository teacherRepository;

	@Test
	void testFindAllTeachers() {
		// Arrange
		List<Teacher> teacherList = new ArrayList<>();
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacherList.add(teacher);
		when(teacherRepository.findAll()).thenReturn(teacherList);

		// Act
		TeacherService teacherService = new TeacherService(teacherRepository);
		List<Teacher> foundTeachers = teacherService.findAll();

		// Assert
		assertEquals(1, foundTeachers.size());
	}

	@Test
	void testGetTeacherById() {
		// Arrange
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

		// Act
		TeacherService teacherService = new TeacherService(teacherRepository);
		Teacher foundTeacher = teacherService.findById(teacher.getId());

		// Assert
		assertEquals(teacher.getId(), foundTeacher.getId());
	}
}
