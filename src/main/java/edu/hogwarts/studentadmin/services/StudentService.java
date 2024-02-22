package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);
    StudentDTO getStudentById(int id);
    List<StudentDTO> getAllStudents();
    StudentDTO updateStudent(int id, StudentDTO studentDTO);
    void deleteStudent(int id);
}