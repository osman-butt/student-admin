package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import edu.hogwarts.studentadmin.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("{id}")
    public ResponseEntity<Course> getCourse(@PathVariable int id){
        return ResponseEntity.of(courseService.getCourseById(id));
    }

    @GetMapping("{id}/teacher")
    public ResponseEntity<Teacher> getCourseTeacher(@PathVariable int id) {
        return ResponseEntity.ok(courseService.getTeacherFromCourse(id));
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Set<Student>> getCourseStudents(@PathVariable int id) {
        return ResponseEntity.ok(courseService.getCourseStudentsById(id));
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(course));
    }

    @PutMapping({"{id}"})
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id,course));
    }

    @PutMapping("{id}/teacher/{teacherId}")
    public ResponseEntity<Course> updateCourseTeacher(@PathVariable int id, @PathVariable int teacherId) {
        return ResponseEntity.ok(courseService.updateCourseTeacher(id,teacherId));
    }

    @PutMapping("{id}/students/{studentId}")
    public ResponseEntity<Course> addStudentToCourse(@PathVariable int id, @PathVariable int studentId) {
        return ResponseEntity.ok().body(courseService.addStudentToCourse(id,studentId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}/teacher")
    public ResponseEntity<Course> removeTeacherFromCourse(@PathVariable int id) {
        return ResponseEntity.ok(courseService.removeTeacherFromCourse(id));
    }

    @DeleteMapping("{id}/students/{studentId}")
    public ResponseEntity<Course> removeStudentFromCourse(@PathVariable int id, @PathVariable int studentId) {
        return ResponseEntity.ok(courseService.removeStudentFromCourse(id, studentId));
    }
}
