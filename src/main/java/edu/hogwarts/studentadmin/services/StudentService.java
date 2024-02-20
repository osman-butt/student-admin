package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.StudentReqDTO;
import edu.hogwarts.studentadmin.dto.StudentResDTO;

import java.util.List;

public interface StudentService {
    StudentResDTO createStudent(StudentReqDTO studentReqDTO);
    StudentResDTO getStudentById(int id);
    List<StudentResDTO> getAllStudents();
    StudentResDTO updateStudent(int id, StudentReqDTO studentReqDTO);
    void deleteStudent(int id);
}