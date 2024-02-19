package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.models.Course;
import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.CourseRepository;
import edu.hogwarts.studentadmin.repositories.StudentRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseController(CourseRepository courseRepository,TeacherRepository teacherRepository,StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(courses);
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
    public ResponseEntity<Set<Student>> getCourseStudents(@PathVariable int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            Set<Student> students = course.getStudents();
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

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course newCourse = courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course origCourse = courseOptional.get();
            // Update course students
            Set<Student> updatedStudents = new HashSet<>();
            for (Student student : course.getStudents()) {
                Optional<Student> foundStudent = studentRepository.findById(student.getId());
                foundStudent.ifPresent(updatedStudents::add);
            }
            origCourse.setStudents(updatedStudents);
            // Update course teacher
            Teacher newTeacher = course.getTeacher();
            Optional<Teacher> teacher = teacherRepository.findById(newTeacher.getId());
            teacher.ifPresent(origCourse::setTeacher);
            if (teacher.isPresent()) {
                origCourse.setTeacher(teacher.get());
            } else {
                return ResponseEntity.notFound().build();
            }
            // Update course info
            origCourse.setSubject(course.getSubject());
            origCourse.setCurrent(course.isCurrent());
            origCourse.setSchoolYear(course.getSchoolYear());
            Course updatedCourse = courseRepository.save(origCourse);
            return ResponseEntity.ok().body(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}/teacher")
    public ResponseEntity<Course> updateCourseTeacher(@PathVariable int id, @RequestBody Teacher teacher) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // Update teacher
            Optional<Teacher> optionalTeacher = teacherRepository.findById(teacher.getId());
            if(optionalTeacher.isPresent()) {
                course.setTeacher(optionalTeacher.get());
                Course updatedCourse = courseRepository.save(course);
                return ResponseEntity.ok().body(updatedCourse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}/students/{studentId}")
    public ResponseEntity<Course> addStudentToCourse(@PathVariable int id, @PathVariable int studentId) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if(courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // Check if student is already enrolled
            if(isStudentPresent(course.getStudents(),studentId)) {
                return ResponseEntity.ok().body(course);
            } else {
                Set<Student> newStudentList = course.getStudents();
                Optional<Student> foundStudent = studentRepository.findById(studentId);
                if(foundStudent.isPresent()) {
                    newStudentList.add(foundStudent.get());
                    course.setStudents(newStudentList);
                    Course updatedCourse = courseRepository.save(course);
                    return ResponseEntity.ok().body(updatedCourse);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            courseRepository.deleteById(id);
            return ResponseEntity.of(courseOptional);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/teacher")
    public ResponseEntity<Course> removeTeacherFromCourse(@PathVariable int id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setTeacher(null);
            courseRepository.save(course);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/students/{studentId}")
    public ResponseEntity<Course> removeStudentFromCourse(@PathVariable int id, @PathVariable int studentId) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // Check if student is enrolled
            if(isStudentPresent(course.getStudents(),studentId)) {
                Set<Student> newStudentList = course.getStudents().stream()
                        .filter(student -> student.getId() != studentId)
                        .collect(Collectors.toSet());
                course.setStudents(newStudentList);
                courseRepository.save(course);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Check if student is already enrolled in course
    private boolean isStudentPresent(Set<Student> students, int id) {
        return students.stream().anyMatch(student -> student.getId() == id);
    }
}
