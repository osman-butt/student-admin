package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable int id){
        return ResponseEntity.ok(courseService.findById(id).orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id)));
    }

    @GetMapping("{id}/teacher")
    public ResponseEntity<Teacher> getCourseTeacher(@PathVariable int id) {
        return ResponseEntity.ok(courseService.findTeacherById(id));
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Set<Student>> getCourseStudents(@PathVariable int id) {
        return ResponseEntity.ok(courseService.findStudentsById(id));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(courseDTO));
    }

    @PostMapping("/{id}/students")
    public ResponseEntity<CourseDTO> addStudentsToCourse(@PathVariable int id, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.addStudents(id,courseDTO));
    }

    @PutMapping({"{id}"})
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable int id, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.update(id,courseDTO));
    }

    @PutMapping("{id}/teacher/{teacherId}")
    public ResponseEntity<CourseDTO> updateCourseTeacher(@PathVariable int id, @PathVariable int teacherId) {
        return ResponseEntity.ok(courseService.updateTeacher(id,teacherId));
    }

    @PutMapping("{id}/students/{studentId}")
    public ResponseEntity<CourseDTO> addStudentToCourse(@PathVariable int id, @PathVariable int studentId) {
        return ResponseEntity.ok().body(courseService.addStudent(id,studentId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable int id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}/teacher")
    public ResponseEntity<CourseDTO> removeTeacherFromCourse(@PathVariable int id) {
        return ResponseEntity.ok(courseService.deleteTeacher(id));
    }

    @DeleteMapping("{id}/students/{studentId}")
    public ResponseEntity<CourseDTO> removeStudentFromCourse(@PathVariable int id, @PathVariable int studentId) {
        return ResponseEntity.ok(courseService.deleteStudent(id, studentId));
    }
}
