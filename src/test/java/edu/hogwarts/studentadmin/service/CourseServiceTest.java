package edu.hogwarts.studentadmin.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.exceptions.BadRequestException;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
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
    public void Test_addStudents_SUCCES() {
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

    @Test
    public void Test_addStudents_NOT_ELIGIBLE() {
        House gryffindor = new House("Gryffindor","Godric Gryffindor",new HouseColor(ColorType.RED,ColorType.YELLOW));
        // Mock Student data
        Student student1 = new Student(1,"Harry",null,"Potter", LocalDate.parse("1990-01-01"),gryffindor,false,2000,null,false,2);
        Student student2 = new Student(2,"Ron",null,"Weasley", LocalDate.parse("1990-01-01"),gryffindor,false,2000,null,false,1);

        // Mock StudentDTOs
        StudentDTO studentDTO1 = new StudentDTO(1,null,null,null,null,null,false,0,null,false,2);
        StudentDTO studentDTO2 = new StudentDTO(2,null,null,null,null,null,false,0,null,false,1);;


        // Mock Course data
        int courseId = 1;

        Course courseFromDB = new Course();
        courseFromDB.setId(courseId);
        courseFromDB.setSchoolYear(1);
        courseFromDB.setCurrent(false);
        courseFromDB.setSubject("Potions");
        courseFromDB.addStudent(student2);

        // Mock method input course
        CourseDTO courseDTO = new CourseDTO(
                courseId,
                "Potions",
                1,
                false,
                null,
                Set.of(studentDTO1));



        // Mock behavior
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(courseFromDB));
        when(studentService.findOneByIdOrFullName(studentDTO1)).thenReturn(Optional.of(studentDTO1));
        when(studentService.toEntity(studentDTO1)).thenReturn(student1);

        // Call the method and verify if it throws a BadRequestException
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.addStudents(courseId, courseDTO);
        });

        // Verify the exception message if needed
        Assertions.assertThat(exception.getMessage()).isEqualTo("Student Harry Potter (id=1) is not from same school year as course.");
    }

    @Test
    public void Test_addStudents_student_NOT_FOUND() {
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
        when(studentService.findOneByIdOrFullName(studentDTO1)).thenReturn(Optional.empty());


        // Call the method and verify if it throws a BadRequestException
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            courseService.addStudents(courseId, courseDTO);
        });
        // Verify the exception message if needed
        Assertions.assertThat(exception.getMessage()).isEqualTo("Student(s) not found, make sure to specify correct id or name");
    }

    @Test
    public void Test_addStudents_course_NOT_FOUND() {
        // Mock Course data
        int courseId = 1;

        Course courseFromDB = new Course();
        courseFromDB.setId(courseId);

        // Mock method input course
        CourseDTO courseDTO = new CourseDTO(
                courseId,
                "Potions",
                1,
                false,
                null,
                Set.of());



        // Mock behavior
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());


        // Call the method and verify if it throws a BadRequestException
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            courseService.addStudents(courseId, courseDTO);
        });
        // Verify the exception message if needed
        Assertions.assertThat(exception.getMessage()).isEqualTo("Unable to find course with id=1");
    }
}
