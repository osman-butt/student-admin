package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService extends Service<StudentDTO,Student> {
    StudentDTO create(StudentDTO studentDTO);
    Optional<StudentDTO> findById(int id);
    Optional<StudentDTO> findOneByIdOrFullName(StudentDTO studentDTO);
    List<StudentDTO> findAll();
    StudentDTO update(int id, StudentDTO studentDTO);
    void deleteStudent(int id);
}