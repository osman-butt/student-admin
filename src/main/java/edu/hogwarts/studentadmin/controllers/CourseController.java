package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Course> getCourse(@PathVariable int id){
        Optional<Course> course = courseRepository.findById(id);
        return ResponseEntity.of(course);
    }

    @GetMapping("{id}/teacher")
    public ResponseEntity<Teacher> getCourseTeacher(@PathVariable int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            Teacher teacher = course.getTeacher();
            if (teacher != null) {
                return ResponseEntity.ok(teacher);
            } else {
                // Handle case where course has no teacher
                return ResponseEntity.notFound().build();
            }
        } else {
            // Handle case where course is not found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}/students")
    public ResponseEntity<List<Student>> getCourseStudents(@PathVariable int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            List<Student> students = course.getStudents();
            if (!students.isEmpty()) {
                return ResponseEntity.ok(students);
            } else {
                // Handle case where course has no teacher
                return ResponseEntity.notFound().build();
            }
        } else {
            // Handle case where course is not found
            return ResponseEntity.notFound().build();
        }
    }
}
