package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(int id);
    Teacher getTeacherFromCourse(int id);
    Set<Student> getCourseStudentsById(int id);
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(int id, CourseDTO courseDTO);
    CourseDTO updateCourseTeacher(int id, int teacherId);
    CourseDTO addStudentToCourse(int id, int studentId);
    void deleteCourse(int id);
    CourseDTO removeTeacherFromCourse(int id);
    CourseDTO removeStudentFromCourse(int id, int studentId);
}
