package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.models.Course;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseService extends Service<CourseDTO, Course> {
    List<CourseDTO> findAll();
    Optional<CourseDTO> findById(int id);
    TeacherDTO findTeacherById(int id);
    Set<StudentDTO> findStudentsById(int id);
    CourseDTO create(CourseDTO courseDTO);
    CourseDTO update(int id, CourseDTO courseDTO);
    CourseDTO updateTeacher(int id, int teacherId);
    CourseDTO addStudent(int id, int studentId);
    CourseDTO addStudents(int id, CourseDTO courseDTO);
    void delete(int id);
    CourseDTO deleteTeacher(int id);
    CourseDTO deleteStudent(int id, int studentId);
}
