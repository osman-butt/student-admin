package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.models.ColorType;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.HouseColor;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.services.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SampleTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void testFindById() {

        // Mock data
        int studentId = 1;
        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        student.setLastName("Doe");
        House gryffindor = new House("Gryffindor","Godric Gryffindor",new HouseColor(ColorType.RED,ColorType.YELLOW));
        student.setHouse(gryffindor);

        // Mock the behavior of studentRepository.findById()
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // Call the service method
        Optional<StudentDTO> optionalStudentDTO = studentService.findById(studentId);

        // Assert that the optional contains the expected StudentDTO
        assertEquals(student.getId(), optionalStudentDTO.get().getId());
        assertEquals(student.getFirstName(), optionalStudentDTO.get().getFirstName());
        assertEquals(student.getLastName(), optionalStudentDTO.get().getLastName());
    }

}
