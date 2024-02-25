package edu.hogwarts.studentadmin.service;

import static org.mockito.Mockito.*;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.models.*;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.services.StudentService;
import edu.hogwarts.studentadmin.services.impl.CourseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    public void testAddStudents() {
        House gryffindor = new House("Gryffindor","Godric Gryffindor",new HouseColor(ColorType.RED,ColorType.YELLOW));
        // Mock Student data
        Student student1 = new Student(1,"Harry",null,"Potter", LocalDate.parse("1990-01-01"),gryffindor,false,2000,null,false,1);
        Student student2 = new Student(2,"Ron",null,"Weasley", LocalDate.parse("1990-01-01"),gryffindor,false,2000,null,false,1);

        // Mock StudentDTOs
        StudentDTO studentDTO1 = new StudentDTO(1,null,null,null,null,null,false,0,null,false,1);
        StudentDTO studentDTO2 = new StudentDTO(2,null,null,null,null,null,false,0,null,false,1);;


        // Mock Course data
        int courseId = 1;

        Course courseFromDB = new Course();
        courseFromDB.setId(courseId);
        courseFromDB.setSchoolYear(1);
        courseFromDB.setCurrent(false);
        courseFromDB.setSubject("Potions");

        // Mock method input course
        CourseDTO courseDTO = new CourseDTO(
                courseId,
                "Potions",
                1,
                false,
                null,
                Set.of(studentDTO1,studentDTO2));



        // Mock behavior
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseFromDB));
        when(studentService.findOneByIdOrFullName(studentDTO1)).thenReturn(Optional.of(studentDTO1));
        when(studentService.findOneByIdOrFullName(studentDTO2)).thenReturn(Optional.of(studentDTO2));
        when(studentService.toEntity(studentDTO1)).thenReturn(student1);
        when(studentService.toEntity(studentDTO2)).thenReturn(student2);
        when(studentService.toDTO(student1)).thenReturn(studentDTO1);
        when(studentService.toDTO(student2)).thenReturn(studentDTO2);
        when(courseRepository.save(courseFromDB)).thenReturn(courseFromDB);

        // Call the method
        CourseDTO result = courseService.addStudents(courseId, courseDTO);

        // Verify the behavior
        verify(courseRepository, times(1)).findById(courseId);
        verify(studentService, times(2)).findOneByIdOrFullName(ArgumentMatchers.any(StudentDTO.class));
        verify(courseRepository, times(1)).save(courseFromDB);

        // Assert
        Assertions.assertThat(result.getStudents().size()).isEqualTo(2);
    }
}
