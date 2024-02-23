package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.exceptions.NotFoundException;
import edu.hogwarts.studentadmin.services.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable int id){
        return ResponseEntity.ok().body(teacherService.findById(id).orElseThrow(() -> new NotFoundException("Unable to find teacher with id=" + id)));
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.create(teacherDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable int id, @RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.ok().body(teacherService.update(id,teacherDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TeacherDTO> deleteTeacher(@PathVariable int id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
