package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.services.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable int id){
        return ResponseEntity.ok().body(studentService.findById(id).orElseThrow(() -> new NotFoundException("Unable to find student with id=" + id)));
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(studentDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable int id, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok().body(studentService.update(id,studentDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<StudentDTO> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
