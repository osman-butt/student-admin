package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student createStudent(Student student);
    Optional<Student> getStudentById(int id);
    List<Student> getAllStudents();
    Student updateStudent(int id, Student student);
    void deleteStudent(int id);
}