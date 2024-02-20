package edu.hogwarts.studentadmin.dto;

import edu.hogwarts.studentadmin.models.Student;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StudentResDTOMapper implements Function<Student,StudentResDTO> {
    @Override
    public StudentResDTO apply(Student student) {
        return new StudentResDTO(
                student.getId(),
                student.getFirstName(),
                student.getMiddleName(),
                student.getLastName(),
                student.getDateOfBirth(),
                student.getHouse().getName(),
                student.isPrefect(),
                student.getEnrollmentYear(),
                student.getGraduationYear(),
                student.isGraduated(),
                student.getSchoolYear());
    }
}
