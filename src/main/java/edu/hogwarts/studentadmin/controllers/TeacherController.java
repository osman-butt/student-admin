package edu.hogwarts.studentadmin.controllers;

import edu.hogwarts.studentadmin.models.House;
import edu.hogwarts.studentadmin.models.Student;
import edu.hogwarts.studentadmin.models.Teacher;
import edu.hogwarts.studentadmin.repositories.HouseRepository;
import edu.hogwarts.studentadmin.repositories.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherRepository teacherRepository;
    private final HouseRepository houseRepository;

    public TeacherController(TeacherRepository teacherRepository, HouseRepository houseRepository) {
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable int id){
        Optional<Teacher> teacher = teacherRepository.findById(id);
        return ResponseEntity.of(teacher);
    }

    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher newTeacher = teacherRepository.save(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTeacher);
    }

    @PutMapping("{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable int id, @RequestBody Teacher teacher) {
        Optional<Teacher> original = teacherRepository.findById(id);
        if(original.isPresent()) {
            Teacher origTeacher = original.get();
            // Get house
            House newHouse = teacher.getHouse();
            Optional<House> house = houseRepository.findById(newHouse.getId());
            house.ifPresent(origTeacher::setHouse);
            // Update Teacher
            origTeacher.setFirstName(teacher.getFirstName());
            origTeacher.setMiddleName(teacher.getMiddleName());
            origTeacher.setLastName(teacher.getLastName());
            origTeacher.setEmployment(teacher.getEmployment());
            origTeacher.setDateOfBirth(teacher.getDateOfBirth());
            origTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            origTeacher.setEmploymentStart(teacher.getEmploymentStart());
            origTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            Teacher updatedTeacher = teacherRepository.save(origTeacher);
            return ResponseEntity.ok().body(updatedTeacher);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Teacher> deleteTeacher(@PathVariable int id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        teacherRepository.deleteById(id);
        return ResponseEntity.of(teacher);
    }
}
