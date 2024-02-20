package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.dto.StudentReqDTO;
import edu.hogwarts.studentadmin.dto.StudentResDTO;
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
    public ResponseEntity<List<StudentResDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentResDTO> getStudent(@PathVariable int id){
        return ResponseEntity.ok().body(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<StudentResDTO> createStudent(@RequestBody StudentReqDTO studentReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(studentReqDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<StudentResDTO> updateStudent(@PathVariable int id, @RequestBody StudentReqDTO studentReqDTO) {
        return ResponseEntity.ok().body(studentService.updateStudent(id,studentReqDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<StudentResDTO> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
