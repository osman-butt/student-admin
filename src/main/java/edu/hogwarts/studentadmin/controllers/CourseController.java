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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course createCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course course) {
        Optional<Course> original = courseRepository.findById(id);
        if (original.isPresent()) {
            Course origCourse = original.get();
            // Update course students
            List<Student> updatedStudents = new ArrayList<>();
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
        Optional<Course> original = courseRepository.findById(id);
        if (original.isPresent()) {
            Course origCourse = original.get();
            // Update teacher
            Optional<Teacher> foundTeacher = teacherRepository.findById(teacher.getId());
            if(foundTeacher.isPresent()) {
                origCourse.setTeacher(foundTeacher.get());
                Course updatedCourse = courseRepository.save(origCourse);
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
        Optional<Course> original = courseRepository.findById(id);
        if(original.isPresent()) {
            Course origCourse = original.get();
            // Check if student is already enrolled
            if(isStudentPresent(origCourse.getStudents(),studentId)) {
                return ResponseEntity.ok().body(origCourse);
            } else {
                List<Student> newStudentList = origCourse.getStudents();
                Optional<Student> foundStudent = studentRepository.findById(studentId);
                if(foundStudent.isPresent()) {
                    newStudentList.add(foundStudent.get());
                    origCourse.setStudents(newStudentList);
                    Course updatedCourse = courseRepository.save(origCourse);
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
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            courseRepository.deleteById(id);
            return ResponseEntity.of(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/teacher")
    public ResponseEntity<Course> removeTeacherFromCourse(@PathVariable int id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            Course origCourse = course.get();
            origCourse.setTeacher(null);
            Course updatedCourse = courseRepository.save(origCourse);
            return ResponseEntity.ok().body(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}/students/{studentId}")
    public ResponseEntity<Course> removeStudentFromCourse(@PathVariable int id, @PathVariable int studentId) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()) {
            Course origCourse = course.get();
            // Check if student is enrolled
            if(isStudentPresent(origCourse.getStudents(),studentId)) {
                List<Student> newStudentList = origCourse.getStudents().stream()
                        .filter(student -> student.getId() != studentId)
                        .collect(Collectors.toList());
                origCourse.setStudents(newStudentList);
                Course updatedCourse = courseRepository.save(origCourse);
                return ResponseEntity.ok().body(updatedCourse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Check if student is already enrolled in course
    private boolean isStudentPresent(List<Student> students, int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
