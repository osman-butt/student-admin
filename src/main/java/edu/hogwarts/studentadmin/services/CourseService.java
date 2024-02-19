package edu.hogwarts.studentadmin.services;

import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseService {
    List<Course> getAllCourses();
    Optional<Course> getCourseById(int id);
    Teacher getTeacherFromCourse(int id);
    Set<Student> getCourseStudentsById(int id);
    Course createCourse(Course course);
    Course updateCourse(int id, Course course);
    Course updateCourseTeacherById(int id);
    Course addStudentToCourse(int id, int studentId);
    void deleteCourse(int id);
    void deleteTeacherFromCourse(int id);
    void deleteStudentFromCourse(int id, int studentId);
}